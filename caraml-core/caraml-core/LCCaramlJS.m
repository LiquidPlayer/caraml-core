/*
 * Copyright (c) 2019 Eric Lange
 *
 * Distributed under the MIT License.  See LICENSE.md at
 * https://github.com/LiquidPlayer/caraml-core for terms and conditions.
 */
#import "LCCaramlJS.h"
#import "LCCaramlJS_private.h"
#import "LCCaramlView.h"
#import "LCCaramlView_private.h"

@interface LCCaramlJS()
@property (nonatomic, strong) LCCaramlView *m_view;
@end

@implementation LCCaramlJS
- (id) init:(JSContext*)context view:(LCCaramlView*)view
{
    self = [super init];
    if (self != nil) {
        self.m_view = view;
    }
    return self;
}

- (void) attach:(NSObject<LCCaramlSurface>*)surface
{
    [self.m_view attach:surface onRestore:NO];
}

- (void) detach
{
    [self.m_view detach];
}

+ (LCCaramlJS*) from:(JSValue*)value
{
    if (value != nil && value.isObject) {
        LCCaramlJS *caramlJS = [value toObjectOfClass:LCCaramlJS.class];
        if (caramlJS != nil) return caramlJS;
    }
    
    @throw [NSException exceptionWithName:@"NotACaramlJSObjectException"
                                   reason:@"Object is not an LCCaramlJS object"
                                 userInfo:nil];
}

@end
