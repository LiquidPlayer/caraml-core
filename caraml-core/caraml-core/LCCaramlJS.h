//
//  CaramlJS.h
//  caraml-core
//
//  Created by Eric Lange on 1/10/19.
//  Copyright © 2019 LiquidPlayer. All rights reserved.
//

#import <JavaScriptCore/JavaScriptCore.h>
#import "LCCaramlSurface.h"

NS_ASSUME_NONNULL_BEGIN

@protocol LCCaramlJSExports<JSExport>
@end

@interface LCCaramlJS : NSObject<LCCaramlJSExports>

/**
 Attaches a `CaramlSurface` to the view represented by this `CaramlJS` object.
 @param surface The surface to attach.
 */
- (void) attach:(NSObject<LCCaramlSurface>*)surface;

/**
 If a surface is attached to the view represented by this {@code #CaramlJS} object, it will
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
