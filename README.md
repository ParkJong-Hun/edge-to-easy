# Edge to Easy

A comprehensive Kotlin library that makes it easier to enable Edge-to-Edge in your Android app with an intuitive DSL approach and reactive programming support.

## Modules

This library consists of two main modules:

- **`edge-to-easy-core`** - Core DSL functionality for edge-to-edge handling
- **`edge-to-easy-flow`** - Reactive programming support with StateFlow and Flow for monitoring WindowInsets changes

## Features

- 🎯 **Intuitive DSL** - Natural language methods like `awayFrom()`, `fillSpace()`, `fillWithMargin()`
- 📐 **Flexible Spacing** - Support for both margin and padding adjustments
- 🔗 **Method Chaining** - Chain multiple views for complex layouts
- 🎨 **Direction Control** - Precise control over which directions to fill
- 🛡️ **Type Safe** - Kotlin-first design with compile-time safety
- ⚡ **Flow Support** - StateFlow and Flow for reactive WindowInsets monitoring
- 🎛️ **Conflict-Free Management** - Shared WindowInsets listeners to prevent listener conflicts
- 🔌 **Pluggable Architecture** - Interface-based provider system for extensible implementations

## Installation

### Core Module Only
```kotlin
dependencies {
    implementation("io.github.parkjonghun:edge-to-easy-core:0.0.1")
}
```

### With Flow Support
```kotlin
dependencies {
    implementation("io.github.parkjonghun:edge-to-easy-core:0.0.1")
    implementation("io.github.parkjonghun:edge-to-easy-flow:0.0.1")
}
```

## Quick Start

### Core Module Usage

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

### Flow Module Usage

```kotlin
// Monitor WindowInsets changes with StateFlow
val insetsStateFlow = view.insetsStateFlow(SystemArea.IME)
insetsStateFlow.collect { insets ->
    val keyboardHeight = insets.bottom
    // Adjust UI based on keyboard height
}

// Monitor all system area changes with Flow
view.insetsFlow()
    .onEach { insets ->
        println("Insets changed: $insets")
    }
    .launchIn(lifecycleScope)

// Use Channel for custom processing
val insetsChannel = view.insetsChannel(SystemArea.StatusBar)
lifecycleScope.launch {
    for (insets in insetsChannel) {
        handleInsetsChange(insets)
    }
}
```

### Advanced Core Usage

```kotlin
// Chain multiple views with different system areas
toolbar.awayFrom(SystemArea.Top).fillWithMargin()
    .then(content).awayFrom(SystemArea.Left).fillWithPadding(FillDirection.Horizontal)
    .then(bottomNav).awayFrom(SystemArea.Bottom).fillSpace()
    .handleEdgeToEdge()

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

### Advanced Flow Usage

```kotlin
// Use Channel Flow for more control over buffering
view.insetsChannelFlow(SystemArea.StatusBar)
    .distinctUntilChanged()
    .debounce(100.milliseconds)
    .onEach { insets ->
        updateToolbarHeight(insets.top)
    }
    .launchIn(lifecycleScope)

// Combine multiple system areas
combine(
    view.insetsFlow(SystemArea.StatusBar),
    view.insetsFlow(SystemArea.NavigationBar)
) { statusInsets, navInsets ->
    statusInsets.top + navInsets.bottom
}.onEach { totalSystemHeight ->
    adjustContentHeight(totalSystemHeight)
}.launchIn(lifecycleScope)

// Use StateFlow for immediate current value access
val keyboardStateFlow = view.insetsStateFlow(SystemArea.IME)
val currentKeyboardHeight = keyboardStateFlow.value.bottom
```

## System Areas

- `SystemArea.StatusBar` / `SystemArea.Top` - Status bar area
- `SystemArea.NavigationBar` - Navigation bar area
- `SystemArea.Bottom` - Navigation bar or IME area (larger of the two)  
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

### Core Module Extensions

#### View Extensions
- `View.awayFrom(systemArea)` - Create distance from system area
- `ViewGroup.addSystemAreaSpacer(systemArea, addToTop)` - Add spacer view with height based on system area insets

#### Fill Methods
- `fillSpace(direction, useMargin)` - Fill space with margin (default) or padding
- `fillWithPadding(direction)` - Fill space with padding
- `fillWithMargin(direction)` - Fill space with margin

#### Chain Control
- `handleEdgeToEdge()` - Complete edge-to-edge setup and consume insets
- `continueToOthers()` - Allow other views to also handle insets
- `then(view)` - Add another view to the chain

### Flow Module Extensions

#### Reactive Extensions
- `View.insetsFlow(systemArea)` - Create Flow that emits WindowInsets changes
- `View.insetsStateFlow(systemArea)` - Create StateFlow for WindowInsets with current state
- `View.insetsChannel(systemArea)` - Create Channel for WindowInsets updates
- `View.insetsChannelFlow(systemArea)` - Create Flow from Channel for WindowInsets

#### Provider Interface
The Flow module uses the `InsetsFlowProvider` interface for extensible and pluggable WindowInsets monitoring:
- `InsetsFlowProvider.createFlow(systemArea)` - Create Flow for system area
- `InsetsFlowProvider.getStateFlow(systemArea)` - Get StateFlow for system area  
- `InsetsFlowProvider.createChannel(systemArea)` - Create Channel for system area
- `InsetsFlowProvider.createChannelFlow(systemArea)` - Create Channel-based Flow

Current implementation uses `SharedInsetsFlowProvider` which prevents listener conflicts by using a single WindowInsets listener per View.

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
