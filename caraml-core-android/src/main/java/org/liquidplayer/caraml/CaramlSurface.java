/*
 * Copyright (c) 2019 Eric Lange
 *
 * Distributed under the MIT License.  See LICENSE.md at
 * https://github.com/LiquidPlayer/caraml-core for terms and conditions.
 */
package org.liquidplayer.caraml;

import android.view.View;

/**
 * Interface for defining a caraml surface.  This object must expose a public constructor that
 * takes an Android {@code #android.content.Context} as a parameter.
 */
public interface CaramlSurface {

    /**
     * Must return the {@code #android.view.View} of this surface.
     * @return The android View wrapped by this surface.
     */
    View getView();

    /**
     * Called when attachment to the caraml view is complete.
     * @param fromRestore True if attachment is due to a View being restored, false if first attach.
     */
    void onAttached(boolean fromRestore);

    /**
     * Called when the caraml view detaches this surface view.
     */
    void onDetached();

    /**
     * Called if an error is encountered during attachment.
     * @param e The thrown exception.
     */
    void onError(Exception e);
}
