/*
 * Copyright (c) 2019 Eric Lange
 *
 * Distributed under the MIT License.  See LICENSE.md at
 * https://github.com/LiquidPlayer/caraml-core for terms and conditions.
 */
package org.liquidplayer.caraml;

import android.support.annotation.NonNull;

import org.liquidplayer.javascript.JSContext;
import org.liquidplayer.javascript.JSObject;
import org.liquidplayer.javascript.JSValue;

/**
 * The object passed from JavaScript which provides the attach/detach API.
 */
public class CaramlJS extends JSObject {
    private static class NotACaramlJSObjectException extends RuntimeException {
        NotACaramlJSObjectException() {
            super("Object is not a CaramlJS API object");
        }
    }

    CaramlJS(@NonNull JSContext context, @NonNull CaramlView view) {
        super(context);
        m_view = view;
    }

    /*package*/ void setView(@NonNull CaramlView view) {
        m_view = view;
    }

    /**
     * Attaches a {@code #CaramlSurface} to the view represented by this {@code #CaramlJS} object.
     * @param surface The surface to attach.
     */
    public void attach(@NonNull CaramlSurface surface) {
        m_view.attach(surface,false);
    }

    /**
     * If a surface is attached to the view represented by this {@code #CaramlJS} object, it will
     * be detached, otherwise this will perform no action.
     */
    public void detach() {
        m_view.detach();
    }

    /**
     * Gets the {@code #CaramlJS} object from the JavaScript object returned to node.
     * @param object The JavaScript value passed to the node JS API
     * @return The Java API object
     */
    public static CaramlJS from(JSValue object) {
        if (CaramlJS.class.isAssignableFrom(object.getClass())) {
            return (CaramlJS)object;
        }
        throw new NotACaramlJSObjectException();
    }

    private CaramlView m_view;
}
