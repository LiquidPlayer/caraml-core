//
//  CaramlJS_private.h
//  caraml-core
//
//  Created by Eric Lange on 1/10/19.
//  Copyright © 2019 LiquidPlayer. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "LCCaramlJS.h"
#import "CaramlView.h"
#import "CaramlView_private.h"

@interface LCCaramlJS(private)
@property (nonatomic, strong) CaramlView *m_view;

- (id) init:(JSContext*)context view:(CaramlView*)view;

@end
