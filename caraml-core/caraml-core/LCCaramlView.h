/*
 * Copyright (c) 2019 Eric Lange
 *
 * Distributed under the MIT License.  See LICENSE.md at
 * https://github.com/LiquidPlayer/caraml-core for terms and conditions.
 */
#import <UIKit/UIKit.h>
#import <LiquidCore/LiquidCore.h>

NS_ASSUME_NONNULL_BEGIN

IB_DESIGNABLE
/**
 `LCCaramlView` exposes an `LCMicroService` through a UI.
 */
@interface LCCaramlView : UIView

/**
 The URL of the micro service.  The `URL` is mutually exclusive with the `jsResource`.  Use only
 one or the other.
 @note The property is exposed through the Interface Builder in XCode and should not
 used programmatically.
 */
@property (nonatomic, strong) IBInspectable NSString* URL;

/**
 The path to a JavaScript resource file.  The `jsResource` is mutually exclusive with the `URL`.  Use only
 one or the other.
 @note The property is exposed through the Interface Builder in XCode and is not
 used programmatically.
 */
@property (nonatomic, strong) IBInspectable NSString* jsResource;

/**
 An array of space-delimited arguments to start the micro service with.
 @note The property is exposed through the Interface Builder in XCode and is not
 used programmatically.
 */
@property (nonatomic, strong) IBInspectable NSString* arguments;

/**
 Starts an `LCMicroService` asynchronously.  In Inteface Builder, this can be auto-started using
 the `URL` or `jsResource` and `arguments` properties.
 @param uri  The micro service URI
 @param argv Optional arguments loaded into `process.argv[2:]`
 @return the `LCMicroService`
 */
- (LCMicroService *) start:(NSURL*)uri arguments:(NSString* _Nullable)argv, ...;

/**
 Starts an `LCMicroService` asynchronously.  In Inteface Builder, this can be auto-started using
 the `URL` or `jsResource` properties.
 @param uri  The micro service URI
 @return the `LCMicroService`
 */
- (LCMicroService *) start:(NSURL*)uri;

@end

NS_ASSUME_NONNULL_END
