<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# Changelog

## Unreleased
### Added
- GML support
  - Syntax highlighting
  - Basic completion: navigate from a component to it's C++ definition
  - A formatter which completely supports Serenity's GML styling
  - Widget and property autocompletion
  - Documentation on element hover for both widgets and properties
  - Type linting

## 1.1.0 - 2024-01-21

### Added

- Support for IPC files
  - Full syntax highlighting
  - "#include" path resolution
  - Line markers on endpoint that lead to any implementers
    - The implementers also get line markers that lead back to the IPC endpoint
  - Resolution of C++ types
- Added IDL -> C++ line markers
