/*
 * Copyright (c) 2019 Eric Lange
 *
 * Distributed under the MIT License.  See LICENSE.md at
 * https://github.com/LiquidPlayer/caraml-core for terms and conditions.
 */
#import "LCCaramlView.h"
#import "LCCaramlSurface.h"
#import "LCCaramlJS.h"

@interface LCCaramlView()

- (void) attach:(NSObject<LCCaramlSurface> * _Nonnull)surface onRestore:(BOOL)onRestore;

- (void) detach;

+ (LCCaramlJS* _Nullable) caramlJSFromContext:(JSContext* _Nonnull)context;

@end
