**Changelog:**

### Unreleased

* Reduce APK size and download footprint through resource shrinking and native-code optimization
* Improve stability and reduce fatal out-of-memory failures when scanning or opening 7z ROMs ([#1524](https://github.com/rafaelvcaetano/melonDS-android/pull/1524))
* Map phone screen sleep/wake to DS lid close/open while emulating ([#1589](https://github.com/rafaelvcaetano/melonDS-android/pull/1589))
* Add two-button controller hotkey combinations ([#1606](https://github.com/rafaelvcaetano/melonDS-android/pull/1606))
* Add a hold-to-fast-forward controller action alongside the existing toggle ([#1608](https://github.com/rafaelvcaetano/melonDS-android/pull/1608))
* Add an option to mute audio while fast-forwarding ([#1625](https://github.com/rafaelvcaetano/melonDS-android/pull/1625))
* Fix ROM-not-found errors when launching from file managers and third-party frontends, including providers without persistable URI permissions ([#1648](https://github.com/rafaelvcaetano/melonDS-android/pull/1648))
* Clarify ROM-library and BIOS/firmware requirements in the README ([#1035](https://github.com/rafaelvcaetano/melonDS-android/pull/1035))

### 2.0.1

* Allow components in custom layouts to be placed within the display cutout area
* Improve OpenGL renderer performance under most scenarios
* Update Simplified Chinese translation
* Multiple crash fixes
