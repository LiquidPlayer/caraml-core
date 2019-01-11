/*
 * Copyright (c) 2019 Eric Lange
 *
 * Distributed under the MIT License.  See LICENSE.md at
 * https://github.com/LiquidPlayer/caraml-core for terms and conditions.
 */
package org.liquidplayer.addon;

import android.content.Context;

import org.liquidplayer.caraml.BuildConfig;
import org.liquidplayer.caraml.CaramlJS;
import org.liquidplayer.caraml.CaramlView;
import org.liquidplayer.javascript.JSFunction;
import org.liquidplayer.javascript.JSObject;
import org.liquidplayer.javascript.JSValue;
import org.liquidplayer.service.AddOn;
import org.liquidplayer.service.MicroService;

public class Caramlcore implements AddOn {
    public Caramlcore(Context context) {}

    @Override
    public void register(String module) {
        if (BuildConfig.DEBUG && !module.equals("caramlcore")) { throw new AssertionError(); }
        System.loadLibrary("caraml-core.node");

        register();
    }

    @Override
    public void require(JSValue binding, MicroService service) {
        if (BuildConfig.DEBUG && (binding == null || !binding.isObject())) {
            throw new AssertionError();
        }

        final CaramlJS caramlJS = CaramlView.caramlJSFromContext(binding.getContext());
        if (BuildConfig.DEBUG && caramlJS==null) { throw new AssertionError(); }

        final JSFunction getInstance = new JSFunction(binding.getContext(), "getInstance") {
            @SuppressWarnings("unused")
            public JSObject getInstance() {
                return caramlJS;
            }
        };

        JSObject bindingObject = binding.toObject();
        bindingObject.property("getInstance", getInstance);
    }

    native static void register();
}
