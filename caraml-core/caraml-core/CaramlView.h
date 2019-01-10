//
//  CaramlView.h
//  caraml-core
//
//  Created by Eric Lange on 1/10/19.
//  Copyright © 2019 LiquidPlayer. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <LiquidCore/LiquidCore.h>

NS_ASSUME_NONNULL_BEGIN

IB_DESIGNABLE
/**
 `LCLiquidView` exposes an `LCMicroService` through a UI.  An `LCMicroService` attaches to a UI
 in JavaScript by calling `LiquidCore.attach(surface, callback)` where
 `surface` is a string representing the `LCSurface` class canonical name
 (e.g. `org.liquidplayer.surfaces.console.ConsoleSurface`) and `callback` is a
 callback function which accepts an `error` parameter.  If `error` is `undefined`, then the
 `LCSurface` was attached correctly and is ready for use.  Otherwise, `error` is a descriptive
 error message.
 */
@interface CaramlView : UIView

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
