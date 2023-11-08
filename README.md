# SerenityOS DSL IntelliJ Plugin

![Build](https://github.com/mattco98/serenityos-intellij-plugin/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/SerenityOS-DSL.svg)](https://plugins.jetbrains.com/plugin/SerenityOS-DSL)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/SerenityOS-DSL.svg)](https://plugins.jetbrains.com/plugin/SerenityOS-DSL)

<!-- Plugin description -->
Adds syntax highlighting for SerenityOS-specific file types. Currently only supports .idl files.

Features:
- Syntax highlighting
- Block/import folding
- Import and simple type resolution
  - control-click on a type to go to its definition, or on an import to go to that file
- Simple syntax errors
  - Type not found (i.e. missing import statements)
  - Missing spec links for interfaces
- Adds line markers to C++ types/methods that have corresponding IDL definition
<!-- Plugin description end -->

## Installation

- Using the IDE built-in plugin system:
  
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "SerenityOS DSL"</kbd> >
  <kbd>Install</kbd>
  
- Manually:

  Download the [latest release](https://github.com/mattco98/serenityos-intellij-plugin/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

## Attribution

The icon used as the plugin logo and file icon is the [WebIDL logo](https://resources.whatwg.org/logo-webidl.svg), 
licensed under [CC BY 4.0](https://github.com/whatwg/webidl/blob/main/LICENSE). It has been altered slightly to have
a transparent background instead of a white background. 
