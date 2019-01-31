/*
 * Copyright (c) 2019 Eric Lange
 *
 * Distributed under the MIT License.  See LICENSE.md at
 * https://github.com/LiquidPlayer/caraml-core for terms and conditions.
 */
#import <UIKit/UIKit.h>

/**
 * Protocol for defining a caraml surface.
 */
@protocol LCCaramlSurface

/**
 * Must return the `UIView` of this surface.
 * @return The iOS view wrapped by this surface.
 */
- (UIView*) getView;

/**
 * Called when attachment to the caraml view is complete.
 * @param fromRestore True if attachment is due to a View being restored, false if first attach.
 */
- (void) onAttached:(BOOL)fromRestore;

/**
 * Called when the caraml view detaches this surface view.
 */
- (void) onDetached;

/**
 * Called if an error is encountered during attachment.
 * @param e The thrown exception.
 */
- (void) onError:(NSException*) e;

@end
