/*
 * Copyright (c) 2019 Eric Lange
 *
 * Distributed under the MIT License.  See LICENSE.md at
 * https://github.com/LiquidPlayer/caraml-core for terms and conditions.
 */
#import <LiquidCore/LiquidCore.h>
#import "LCCaramlView_private.h"
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
    
    LCCaramlJS *caramlJS = [LCCaramlView caramlJSFromContext:[binding context]];
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
