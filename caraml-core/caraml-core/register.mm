//
//  register.mm
//  caraml-core
//
//  Created by Eric Lange on 1/10/19.
//  Copyright © 2019 LiquidPlayer. All rights reserved.
//

#import <LiquidCore/LiquidCore.h>
#import "CaramlView_private.h"
#include "caraml-core.h"

@interface CaramlCore : NSObject<LCAddOn>

@end

@implementation CaramlCore

- (id) init
{
    self = [super init];
    if (self != nil) {
        
    }
    return self;
}

- (void) register:(NSString*) module
{
    assert([@"caramlcore" isEqualToString:module]);
    register_caramlcore();
}

- (void) require:(JSValue*) binding service:(LCMicroService *)service
{
    assert(binding != nil);
    assert([binding isObject]);
    
    LCCaramlJS *caramlJS = [CaramlView caramlJSFromContext:[binding context]];
    assert(caramlJS != nil);
    
    binding[@"getInstance"] = ^{
        return caramlJS;
    };
}

@end

@interface CaramlCoreFactory : LCAddOnFactory

@end

@implementation CaramlCoreFactory

- (id) init
{
    self = [super init];
    if (self) {
        
    }
    return self;
}

- (id<LCAddOn>)createInstance
{
    return [[CaramlCore alloc] init];
}

@end

__attribute__((constructor))
static void coreJSRegistration()
{
    [LCAddOnFactory registerAddOnFactory:@"caramlcore" factory:[[CaramlCoreFactory alloc] init]];
}
