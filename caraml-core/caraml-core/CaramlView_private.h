//
//  CaramlView_private.h
//  caraml-core
//
//  Created by Eric Lange on 1/10/19.
//  Copyright © 2019 LiquidPlayer. All rights reserved.
//

#import "CaramlView.h"
#import "LCCaramlSurface.h"
#import "CaramlJS.h"

@interface CaramlView(private)

- (void) attach:(NSObject<LCCaramlSurface> *)surface onRestore:(BOOL)onRestore;

- (void) detach;

+ (CaramlJS* _Nullable) caramlJSFromContext:(JSContext*)context;

@end
