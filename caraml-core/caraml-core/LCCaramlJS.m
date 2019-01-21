//
//  CaramlJS.m
//  caraml-core
//
//  Created by Eric Lange on 1/10/19.
//  Copyright © 2019 LiquidPlayer. All rights reserved.
//

#import "LCCaramlJS.h"
#import "LCCaramlJS_private.h"
#import "CaramlView.h"
#import "CaramlView_private.h"

@interface LCCaramlJS()
@property (nonatomic, strong) CaramlView *m_view;
@end

@implementation LCCaramlJS
- (id) init:(JSContext*)context view:(CaramlView*)view
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
                                   reason:@"Object is not a CaramlJS object"
                                 userInfo:nil];
}

@end
