//
//  CaramlView_private.h
//  caraml-core
//
//  Created by Eric Lange on 1/10/19.
//  Copyright © 2019 LiquidPlayer. All rights reserved.
//

#import "CaramlView.h"
#import "LCCaramlSurface.h"
#import "LCCaramlJS.h"

@interface CaramlView()

- (void) attach:(NSObject<LCCaramlSurface> * _Nonnull)surface onRestore:(BOOL)onRestore;

- (void) detach;

+ (LCCaramlJS* _Nullable) caramlJSFromContext:(JSContext* _Nonnull)context;

@end
