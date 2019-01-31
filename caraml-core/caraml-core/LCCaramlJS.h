//
//  CaramlJS.h
/*
 * Copyright (c) 2019 Eric Lange
 *
 * Distributed under the MIT License.  See LICENSE.md at
 * https://github.com/LiquidPlayer/caraml-core for terms and conditions.
 */
#import <JavaScriptCore/JavaScriptCore.h>
#import "LCCaramlSurface.h"

NS_ASSUME_NONNULL_BEGIN

/**
 Protocol to expose LCCaramlJS object to JavaScript
 */
@protocol LCCaramlJSExports<JSExport>
@end

/**
 The object passed from JavaScript which provides the attach/detach API.
 */
@interface LCCaramlJS : NSObject<LCCaramlJSExports>

/**
 Attaches a `CaramlSurface` to the view represented by this `CaramlJS` object.
 @param surface The surface to attach.
 */
- (void) attach:(NSObject<LCCaramlSurface>*)surface;

/**
 If a surface is attached to the view represented by this `LCCaramlJS` object, it will
 be detached, otherwise this will perform no action.
 */
- (void) detach;

/**
 * Gets the `CaramlJS` object from the JavaScript object returned to node.
 * @param value The JavaScript value passed to the node JS API
 * @return The Java API object
 */
+ (LCCaramlJS*) from:(JSValue*)value;

@end

NS_ASSUME_NONNULL_END
