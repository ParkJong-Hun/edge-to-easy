# Edge to Easy

A Kotlin library that makes it easier to enable Edge-to-Edge in your Android app with an intuitive DSL approach.

## Features

- 🎯 **Intuitive DSL** - Natural language methods like `separateFrom()`, `awayFrom()`, `distanceFrom()`
- 📐 **Flexible Spacing** - Support for both margin and padding adjustments
- 🔗 **Method Chaining** - Chain multiple views for complex layouts
- 🎨 **Direction Control** - Precise control over which directions to fill
- 🛡️ **Type Safe** - Kotlin-first design with compile-time safety

## Installation

Add this to your module's `build.gradle.kts`:

```kotlin
dependencies {
    implementation("io.github.parkjonghun:edge-to-easy:0.0.1")
}
```

## Quick Start

### Basic Usage

```kotlin
// Simple margin-based spacing (default)
view.awayFrom(SystemArea.StatusBar).fillSpace().handleEdgeToEdge()

// Explicit padding-based spacing
view.awayFrom(SystemArea.NavigationBar).fillWithPadding().handleEdgeToEdge()

// Explicit margin-based spacing
view.awayFrom(SystemArea.StatusBar).fillWithMargin().handleEdgeToEdge()

// Specific direction control
view.awayFrom(SystemArea.Everything)
    .fillSpace(FillDirection.Horizontal)
    .handleEdgeToEdge()
```

### Multiple Views

```kotlin
// Chain multiple views with different system areas
toolbar.awayFrom(SystemArea.Top).fillWithMargin()
    .then(content).awayFrom(SystemArea.Left).fillWithPadding(FillDirection.Horizontal)
    .then(bottomNav).awayFrom(SystemArea.Bottom).fillSpace()
    .handleEdgeToEdge()
```

### Advanced Usage

```kotlin
// Continue insets to other views outside the chain
parentView.awayFrom(SystemArea.StatusBar).fillSpace().continueToOthers()

// Handle different system areas
view.awayFrom(SystemArea.TopFull).fillSpace().handleEdgeToEdge()    // Status bar + cutout
view.awayFrom(SystemArea.BottomFull).fillSpace().handleEdgeToEdge()  // Nav bar + cutout + IME
view.awayFrom(SystemArea.IME).fillSpace().handleEdgeToEdge()        // Keyboard area

// Mix different spacing types
toolbar.awayFrom(SystemArea.StatusBar).fillWithMargin()
    .then(content).awayFrom(SystemArea.NavigationBar).fillWithPadding()
    .handleEdgeToEdge()

// Add spacer views to ViewGroups
linearLayout.addSystemAreaSpacer(SystemArea.NavigationBar)  // Add at the end
constraintLayout.addSystemAreaSpacer(SystemArea.StatusBar, addToTop = true)  // Add at the beginning
```

## System Areas

- `SystemArea.StatusBar` / `SystemArea.Top` - Status bar area
- `SystemArea.NavigationBar` / `SystemArea.Bottom` - Navigation bar area  
- `SystemArea.SystemBar` - Both status and navigation bars
- `SystemArea.IME` - Input Method Editor (keyboard) area
- `SystemArea.Cutout` - Display cutout area
- `SystemArea.Everything` - All system areas (system bars + cutout + IME)
- `SystemArea.TopFull` - Status bar + cutout (larger of the two)
- `SystemArea.BottomFull` - Navigation bar + cutout + IME (larger of the three)

## Fill Directions

- `FillDirection.All` - All directions (default)
- `FillDirection.Horizontal` - Left and right only
- `FillDirection.Vertical` - Top and bottom only
- `FillDirection.Left`, `FillDirection.Top`, `FillDirection.Right`, `FillDirection.Bottom` - Single directions
- `FillDirection.TopLeft`, `FillDirection.TopRight`, `FillDirection.BottomLeft`, `FillDirection.BottomRight` - Combinations
- Custom: `FillDirection(top = true, left = false, right = true, bottom = false)`

## API Reference

### Extension Functions

- `View.awayFrom(systemArea)` - Create distance from system area
- `ViewGroup.addSystemAreaSpacer(systemArea, addToTop)` - Add spacer view with height based on system area insets

### Fill Methods

- `fillSpace(direction, useMargin)` - Fill space with margin (default) or padding
- `fillWithPadding(direction)` - Fill space with padding
- `fillWithMargin(direction)` - Fill space with margin

### Chain Control

- `handleEdgeToEdge()` - Complete edge-to-edge setup and consume insets
- `continueToOthers()` - Allow other views to also handle insets
- `then(view)` - Add another view to the chain

## Requirements

- Android API 21+
- Kotlin 1.8+
- AndroidX Core 1.12.0+

## Development Status

⚠️ **This library is currently in early development stage.**

Features and APIs are not yet stable and may change significantly.

## License

```
Copyright (C) 2025 Park Jong Hun

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
