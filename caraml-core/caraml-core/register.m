//
//  register.m
//  caraml-core
//
//  Created by Eric Lange on 1/10/19.
//  Copyright © 2019 LiquidPlayer. All rights reserved.
//

#import <LiquidCore/addon/LCAddOn.h>

extern void _register_caramlcore();

@interface CaramlCore : NSObject<LCAddOn>

@end

@implementation CaramlCore

- (id) init
{
    if (self != nil) {
        
    }
    return self;
}

- (void) register:(NSString*) module
{
    _register_caramlcore();
}

- (void) require:(JSValue*) binding;
{
    
}

@end

@interface CaramlCoreFactory : LCAddOnFactory

@end

@implementation CaramlCoreFactory

- (id<LCAddOn>)createInstance
{
    return [[CaramlCore alloc] init];
}

@end
