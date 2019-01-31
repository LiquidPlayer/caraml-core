/*
 * Copyright (c) 2019 Eric Lange
 *
 * Distributed under the MIT License.  See LICENSE.md at
 * https://github.com/LiquidPlayer/caraml-core for terms and conditions.
 */
#import <UIKit/UIKit.h>
#import "LCCaramlJS.h"
#import "LCCaramlView.h"
#import "LCCaramlView_private.h"

@interface LCCaramlJS(private)
@property (nonatomic, strong) LCCaramlView *m_view;

- (id) init:(JSContext*)context view:(LCCaramlView*)view;

@end
