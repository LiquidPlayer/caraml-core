/*
 * Copyright (c) 2016-2019 Eric Lange
 *
 * Distributed under the MIT License.  See LICENSE.md at
 * https://github.com/LiquidPlayer/caraml-core for terms and conditions.
 */
package org.liquidplayer.caraml;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.LongSparseArray;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.liquidplayer.javascript.JSContext;
import org.liquidplayer.node.Process;
import org.liquidplayer.service.MicroService;

import java.net.URI;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * LiquidView exposes a MicroService through a UI.  A MicroService attaches to a UI
 * in JavaScript by calling <code>LiquidCore.attach(surface, callback)</code> where
 * 'surface' is a string representing the Surface class
 * (e.g. 'org.liquidplayer.surfaces.console.ConsoleSurface') and 'callback' is a
 * callback function which accepts an 'error' parameter.  If 'error' is undefined, then the
 * Surface was attached correctly and is ready for use.  Otherwise, 'error' is a descriptive
 * error message.
 */
public class CaramlView extends RelativeLayout {

    public CaramlView(Context context) {
        this(context, null);
    }

    public CaramlView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressWarnings("unchecked")
    public CaramlView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setSaveEnabled(true);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.LiquidView,
                0, 0);
        try {
            if (a.hasValue(R.styleable.CaramlView_caraml_URI)) {
                String uri_ = a.getString(R.styleable.CaramlView_caraml_URI);
                if (uri_ != null && uri_.length() != 0) {
                    uri = URI.create(uri_);
                }
            } else if (attrs != null) {
                uri = MicroService.DevServer();
            }
            if (a.hasValue(R.styleable.CaramlView_caraml_argv))
                argv = getResources()
                        .getStringArray(a.getResourceId(R.styleable.CaramlView_caraml_argv, 0));
        } finally {
            a.recycle();
        }

        LayoutInflater.from(context).inflate(R.layout.caraml_view, this);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (surfaceId == View.NO_ID && uri != null) {
            start(uri, argv);
            uri = null;
        }
    }

    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
    private static int generateViewIdCommon() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return generateViewId();
        } else {
            for (; ; ) {
                final int result = sNextGeneratedId.get();
                // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
                int newValue = result + 1;
                if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
                if (sNextGeneratedId.compareAndSet(result, newValue)) {
                    return result;
                }
            }
        }
    }

    /*package*/ void attach(final CaramlSurface surface, final boolean onRestore) {
        final MicroService service = MicroService.getService(serviceId);
        try {
            if (service == null)
                throw new Exception("service not available");
            if (surfaceId == View.NO_ID)
                surfaceId = generateViewIdCommon();

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override @SuppressWarnings("unchecked")
                public void run() {
                    try {
                        surfaceView = surface.getView();
                        surfaceView.setId(surfaceId);
                        ViewGroup parent = (ViewGroup) surfaceView.getParent();
                        if (parent != null) {
                            parent.removeView(surfaceView);
                        }
                        addView(surfaceView);
                        if (childrenStates != null) {
                            for (int i = 0; i < getChildCount(); i++) {
                                getChildAt(i).restoreHierarchyState(childrenStates);
                            }
                            childrenStates = null;
                        }
                        attachedSurface = UUID.randomUUID().toString();
                        surfaceHashMap.put(attachedSurface, surface);
                        surface.onAttached(onRestore);
                    } catch (Exception e) {
                        e.printStackTrace();
                        android.util.Log.d("exception", e.toString());
                        surface.onError(e);
                    }
                }
            });
            /* This is necessary if the view has been restored */
            service.getProcess().addEventListener(new Process.EventListener() {
                public void onProcessStart(Process process, JSContext context) {}
                public void onProcessAboutToExit(Process process, int exitCode) {
                    detach();
                    process.removeEventListener(this);
                }
                public void onProcessExit(Process process, int exitCode) {
                    detach();
                    process.removeEventListener(this);
                }
                public void onProcessFailed(Process process, Exception error) {
                    detach();
                    process.removeEventListener(this);
                }
            });
        } catch (Exception e) {
            android.util.Log.d("exception", e.toString());
            surface.onError(e);
            detach();
        }
    }

    /*package*/ void detach() {
        surfaceId = NO_ID;
        final CaramlSurface surface;
        if (attachedSurface != null && attachedSurface.length() > 0 &&
                surfaceHashMap.get(attachedSurface) != null) {
            String s = attachedSurface;
            attachedSurface = null;
            surface = surfaceHashMap.get(s);
            surfaceHashMap.remove(s);
        } else {
            surface = null;
        }
        if (surfaceView != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    removeView(surfaceView);
                    if (surface!=null) surface.onDetached();
                }
            });
        } else {
            if (surface!=null) surface.onDetached();
        }
    }

    /**
     * Starts a MicroService asynchronously.  In XML layout, this can be auto-started using
     * the "custom:caraml.URI" and "custom:carml.argv" attributes.
     * @param uri  The MicroService URI
     * @param argv Optional arguments loaded into process.argv[2:]
     * @return the MicroService
     */
    public MicroService start(final URI uri, String ... argv) {
        if (getId() == View.NO_ID) {
            setId(generateViewIdCommon());
        }
        if (uri != null) {
            MicroService svc =
            new MicroService(getContext(), uri, new MicroService.ServiceStartListener() {
                @Override
                public void onStart(final MicroService service) {
                    serviceId = service.getId();
                    service.getProcess().addEventListener(new Process.EventListener() {
                        @Override
                        public void onProcessStart(Process process, final JSContext context) {
                            caramlJS = new CaramlJS(context, CaramlView.this);
                            instanceMap.put(serviceId, caramlJS);
                            contextHash = context.getGroup().groupHash();
                            contextMap.put(contextHash, serviceId);
                        }

                        @Override public void onProcessAboutToExit(Process process, int exitCode) {
                            process.removeEventListener(this);
                            instanceMap.remove(serviceId);
                            contextMap.remove(contextHash);
                        }
                        @Override public void onProcessExit(Process process, int exitCode) {
                            process.removeEventListener(this);
                        }
                        @Override public void onProcessFailed(Process process, Exception error) {
                            process.removeEventListener(this);
                        }
                    });
                }
            },
            new MicroService.ServiceErrorListener() {
                @Override
                public void onError(MicroService service, Exception e) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),"Failed to start service at " + uri,
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
            svc.start(argv);
            return svc;
        }

        return null;
    }

    /**
     * Gets the {@code #org.liquidplayer.caraml.CaramlJS} object from the JavaScript
     * context.  This method is used internally and does not need to be called by clients.
     * @param context The JavaScript context.
     * @return The CaramlJS instance object or null if no {@code #CaramlView} has been
     *         associated with this context.
     */
    public static @Nullable CaramlJS caramlJSFromContext(@NonNull JSContext context) {
        String id = contextMap.get(context.getGroup().groupHash());
        if (id != null) {
            return instanceMap.get(id);
        }
        return null;
    }

    /* -- statics -- */
    private static HashMap<String,CaramlSurface> surfaceHashMap = new HashMap<>();
    private static HashMap<String,CaramlJS> instanceMap = new HashMap<>();
    private static LongSparseArray<String> contextMap = new LongSparseArray<>();

    /* -- local privates -- */
    private URI uri;
    private String [] argv;
    private View surfaceView;
    private CaramlJS caramlJS;
    private long contextHash;

    /* -- parcelable privates -- */
    private int surfaceId = View.NO_ID;
    private String serviceId;
    private SparseArray childrenStates;
    private String attachedSurface = null;

    @Override @SuppressWarnings("unchecked")
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.surfaceId = surfaceId;
        ss.serviceId = serviceId;
        ss.childrenStates = new SparseArray();
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).saveHierarchyState(ss.childrenStates);
        }
        ss.attachedSurface = attachedSurface;
        return ss;
    }

    @Override @SuppressWarnings("unchecked")
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        surfaceId = ss.surfaceId;
        serviceId = ss.serviceId;
        childrenStates = ss.childrenStates;
        attachedSurface = ss.attachedSurface;
        if (attachedSurface != null) {
            CaramlSurface s = surfaceHashMap.get(attachedSurface);
            attach(s,true);
        }
        if (serviceId != null) {
            caramlJS = instanceMap.get(serviceId);
            caramlJS.setView(this);
        }
    }

    @Override
    protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
        dispatchFreezeSelfOnly(container);
    }

    @Override
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        dispatchThawSelfOnly(container);
    }

    @SuppressWarnings("unused")
    static class SavedState extends BaseSavedState {
        SparseArray childrenStates;
        private int surfaceId;
        private String serviceId;
        private String attachedSurface;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in, ClassLoader classLoader) {
            super(in);
            surfaceId = in.readInt();
            serviceId = in.readString();
            childrenStates = in.readSparseArray(classLoader);
            attachedSurface = in.readString();
        }

        @Override @SuppressWarnings("unchecked")
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(surfaceId);
            out.writeString(serviceId);
            out.writeSparseArray(childrenStates);
            out.writeString(attachedSurface);
        }

        public static final ClassLoaderCreator<SavedState> CREATOR
                = new ClassLoaderCreator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel source, ClassLoader loader) {
                return new SavedState(source, loader);
            }

            @Override
            public SavedState createFromParcel(Parcel source) {
                return createFromParcel(source, null);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
