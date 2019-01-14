//
//  CaramlView.m
//  caraml-core
//
//  Created by Eric Lange on 1/10/19.
//  Copyright © 2019 LiquidPlayer. All rights reserved.
//

#import "CaramlView.h"
#import "CaramlView_private.h"
#import "LCCaramlJS.h"
#import "LCCaramlJS_private.h"

static NSMutableDictionary *contextMap;

@interface CaramlView() <LCMicroServiceDelegate>
@property (nonatomic, strong) UIView *surfaceView;
@property (nonatomic, strong) NSObject<LCCaramlSurface> *attachedSurface;
@property (nonatomic, strong) LCCaramlJS *caramlJS;
@end

@implementation CaramlView {
    LCMicroService *service_;
    NSArray *argv_;
    unsigned long contextHash_;
}

- (id) initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
    }
    return self;
}

- (id) initWithCoder:(NSCoder *)aDecoder
{
    self = [super initWithCoder:aDecoder];
    if (self) {
    }
    return self;
}

- (void) awakeFromNib
{
    [super awakeFromNib];
    NSMutableArray *array = [[NSMutableArray alloc] init];
    
    if (self.arguments != nil) {
        NSScanner *scanner = [NSScanner scannerWithString:self.arguments];
        NSString *substring;
        
        while (scanner.scanLocation < self.arguments.length) {
            unichar character = [self.arguments characterAtIndex:scanner.scanLocation];
            if (character == '"') {
                [scanner setScanLocation:(scanner.scanLocation + 1)];
                [scanner scanUpToString:@"\"" intoString:&substring];
                [scanner setScanLocation:(scanner.scanLocation + 1)];
            }
            else {
                [scanner scanUpToString:@" " intoString:&substring];
            }
            [array addObject:substring];
            
            if (scanner.scanLocation < self.arguments.length) [scanner setScanLocation:(scanner.scanLocation + 1)];
        }
    }
    
    if (self.jsResource != nil) {
        self.URL = [[[NSBundle mainBundle] URLForResource:_jsResource withExtension:@"js"] absoluteString];
    }
    
    if (self.URL == nil) {
        self.URL = [[LCMicroService devServer] absoluteString];
    }
    
    if (array.count > 0) {
        [self start:[NSURL URLWithString:self.URL] argv:argv_];
    } else {
        NSLog(@"URL = %@", self.URL);
        [self start:[NSURL URLWithString:self.URL]];
    }
}

- (void) layoutSubviews
{
    [super layoutSubviews];
    
    if (self.surfaceView != nil) {
        [self.surfaceView setFrame:self.frame];
        [self.surfaceView setNeedsLayout];
    }
}

- (LCMicroService *) start:(NSURL*)uri argv:(NSArray*)argv
{
    if (uri == nil) {
        uri = [LCMicroService devServer];
    }
    self.URL = uri.absoluteString;
    
    service_ = [[LCMicroService alloc] initWithURL:uri delegate:self];
    if (argv == nil) {
        [service_ start];
    } else {
        [service_ startWithArguments:argv];
    }
    return service_;
}

- (LCMicroService *) start:(NSURL*)uri arguments:(NSString*)argv, ...
{
    if (uri == nil) {
        uri = [LCMicroService devServer];
    }
    self.URL = uri.absoluteString;
    
    service_ = [[LCMicroService alloc] initWithURL:uri delegate:self];
    if (argv == nil) {
        [service_ start];
    } else {
        NSMutableArray *argz = [[NSMutableArray alloc] initWithObjects:argv, nil];
        va_list args;
        va_start(args,argv);
        NSString *arg;
        while(( arg = va_arg(args, id))){
            [argz addObject:arg];
        }
        va_end(args);
        [service_ startWithArguments:argz];
    }
    return service_;
}

- (LCMicroService *) start:(NSURL*)uri
{
    return [self start:uri arguments:nil];
}

- (void) attach:(NSObject<LCCaramlSurface> *)surface onRestore:(BOOL)onRestore;
{
    @try {
        if (service_ == nil) {
            @throw [[NSException alloc] initWithName:@"Service not available"
                                              reason:@"Service not available" userInfo:nil];
        }
        
        dispatch_async(dispatch_get_main_queue(), ^{
            @try {
                UIView *view = [surface getView];
                if (self.surfaceView != nil) {
                    [self.surfaceView removeFromSuperview];
                    self.surfaceView = nil;
                }
                self.surfaceView = view;
                [self.surfaceView setFrame:self.frame];
                [self.surfaceView setNeedsLayout];
                [self addSubview:self.surfaceView];
                
                [self.surfaceView.topAnchor constraintEqualToAnchor:self.topAnchor];
                [self.surfaceView.bottomAnchor constraintEqualToAnchor:self.bottomAnchor];
                [self.surfaceView.leadingAnchor constraintEqualToAnchor:self.leadingAnchor];
                [self.surfaceView.trailingAnchor constraintEqualToAnchor:self.trailingAnchor];
                
                self.attachedSurface = surface;
                [surface onAttached:onRestore];
            } @catch (NSException *e) {
                NSLog(@"exception: %@", e);
                [surface onError:e];
            }
        });
    } @catch (NSException *e) {
        NSLog(@"exception: %@", e);
        [self detach];
        [surface onError:e];
    }
}

- (void) detach
{
    if (self.surfaceView != nil) {
        dispatch_async(dispatch_get_main_queue(), ^
        {
            if (self.surfaceView != nil) {
                [self.surfaceView removeFromSuperview];
                self.surfaceView = nil;
            }
            if (self.attachedSurface != nil) {
                [self.attachedSurface onDetached];
                self.attachedSurface = nil;
            }
        });
    } else if (self.attachedSurface != nil) {
        [self.attachedSurface onDetached];
    }
}

+ (LCCaramlJS* _Nullable) caramlJSFromContext:(JSContext*)context
{
    if (contextMap == nil) return nil;
    
    return contextMap[@((unsigned long)[context JSGlobalContextRef])];
}

#pragma - MicroServiceDelegate

- (void) onStart:(LCMicroService*)service
{
    [service.process sync:^(JSContext* context) {
        self.caramlJS = [[LCCaramlJS alloc] init:context view:self];
        self->contextHash_ = (unsigned long)[context JSGlobalContextRef];
        if (contextMap == nil) {
            contextMap = [[NSMutableDictionary alloc] init];
        }

        contextMap[@(self->contextHash_)] = self.caramlJS;
    }];
}

- (void) onExit:(LCMicroService*)service exitCode:(int)exitCode
{
    [contextMap removeObjectForKey:@(contextHash_)];
}

- (void) onError:(LCMicroService*)service exception:(NSException*)exception
{
    NSLog(@"error: %@", exception);
}

@end
