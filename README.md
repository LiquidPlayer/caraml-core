(work in progress)

caraml
------

caraml is a native mobile UI markup language designed for running native micro-apps on Android and iOS
from node.js instances.  It is built on top of [LiquidCore](https://github.com/LiquidPlayer/LiquidCore), a
library which provides node-based virtual machines on mobile devices.

caraml is very much a work in progress.

caraml-core
-----------

caraml-core is the basic UI manager for caraml.

This library provides a `CaramlView` subclass of a `View` (`UIView` on iOS) which will enable a UI
surface to be exposed to JavaScript.  Once the view is loaded, it will fetch JavaScript code from a server or
local resource and make the surface available to that JavaScript context.

To integrate, clients add the view in the interface builder or programmatically.  To add the view:

### Step 1: Install LiquidCore and caraml-core

First install the command-line utilities:

```bash
$ npm i -g liquidcore-cli
```

Then, in your project's root directory, create a `package.json` file (in order to create a local `node_modules`
directory) and install caraml-core.

```bash
$ echo "{}" > package.json
$ npm i @liquidcore/caraml-core
```

This will install caraml-core and its dependencies locally.  Finally, generate the required build files for your OS.

On Android:
```bash
$ liquidcore gradle
```

On iOS:
```
$ liquidcore pod <my_project_target> > Podfile
```

In the case of Android, this will create a file called `liquidcore.build.gradle`.  Add this to your project's
`build.gradle` file as described in the output of the `liquidcore` command.  For iOS, this will generate a `Podfile`
that can either be used directly as-is or the pods can be integrated into an existing
`Podfile` if you are already using Cocoapods.  Finally, for iOS:

```bash
$ pod install
```

### Step 2: Integrate `CaramlView`

#### Android layout file

You can insert the view into any layout like so:

```xml
    <org.liquidplayer.caraml.CaramlView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:id="@+id/caramlview"
    />
```

#### iOS Interface Builder

Drag a `UIView` onto your storyboard in a `ViewController`.  Go to the identity inspector on the right and
set its custom class to `LCCaramlView`.

#### Android programmatic

```java
import org.liquidplayer.caraml.CaramlView;
...
    CaramlView caramlView = new CaramlView(androidContext);
```

#### iOS (Swift)
```swift
import caraml_core
...
    let caramlView = LCCaramlView(frame: CGRect.zero)
```

This is all that is required to get it up and running.  `CaramlView` defaults to using the dev server at port
8082.  To use other servers or local resources, the URL can be specified both programmatically and in the
interface builders.


Native Surface API
------------------

To do

JavaScript API
--------------

### caraml Core

The `caraml-core` module provides an object representing the `CaramlView` (Android) or `LCCaramlView` that
created this JavaScript context.  It can be accessed using:

```javascript
const core = require('@liquidcore/caraml-core')
```

The `core` object appears to be an empty object in JavaScript, but is bound to a native representation
of the view.  This object can be passed to native surface implementations which enables the ability to attach
and detach surfaces to the view.
