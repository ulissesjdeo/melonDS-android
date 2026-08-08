# melonDS Android port
Android port of [melonDS](https://melonds.kuribo64.net/), a DS and DSi emulator.

[<img src="https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png" alt="Get it on Google Play" height="80">](https://play.google.com/store/apps/details?id=me.magnum.melonds&pcampaignid=pcampaignidMKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1)[<img src="https://raw.githubusercontent.com/Kunzisoft/Github-badge/main/get-it-on-github.png" alt="Get it on GitHub" height="80">](https://github.com/ulissesjdeo/melonDS-android/releases/latest)

|Rom List|Dark Theme|Pocket Physics|Layout Editor|
|---|---|---|---|
|![Screenshot 1](./.github/images/screenshot_mobile0.png)|![Screenshot 2](./.github/images/screenshot_mobile1.png)|![Screenshot 3](./.github/images/screenshot_mobile2.png)|![Screenshot 4](./.github/images/screenshot_mobile3.png)|

## Requirements

Use ROM, BIOS, firmware, and NAND dumps legally obtained from hardware and games you own. A ROM directory is needed to populate the in-app game library, but a file manager or third-party frontend can also launch an unscanned ROM directly.

Standard DS games can use melonDS's internal firmware when custom BIOS/firmware is disabled. Dumps from an original DS or DS Lite are needed when custom BIOS/firmware is enabled or when booting the console firmware. DSi features require the corresponding DSi BIOS, firmware, and NAND files when prompted.

Configure custom dumps under Settings > System > Custom BIOS and Firmware. If a game crashes while loading with custom firmware enabled, verify that the selected DS or DSi directory contains valid, uncorrupted files.

## Controls and behavior

- Controller key mapping supports single buttons and two-button combinations. Hold both buttons while the assignment prompt is active to create a combo.
- Fast forward has separate `Toggle` and `Hold` actions under Settings > Input > Key Mapping.
- Settings > Audio > Mute audio while fast forwarding can silence audio for both fast-forward modes.
- Turning the phone screen off while emulating closes the virtual DS lid; waking the phone reopens it. Switching apps while the screen remains on uses normal pause behavior.
- Plain NDS, ZIP, and 7z ROMs are supported. 7z scanning and icon loading are serialized to reduce peak memory usage.

See the [current changelog](./.github/changelog/gitHub.md) for the complete list of integrated updates.

## Missing Features

*  Local Multiplayer
*  DSi SD card support
*  Customizable button skins
*  More display filters

## Performance

Performance is solid on 64 bit devices with thread rendering and JIT enabled, and should run at full speed on flagship devices. Performance on older devices, specially
32 bit devices, is very poor due to the lack of JIT support.

## Integration with third-party frontends

melonDS can launch ROMs from third-party frontends and file managers. A matching scanned ROM reuses its cached metadata and configuration; an unscanned ROM is imported for that launch when the supplied URI can be read. Configure the frontend with:

*  Package name: `me.magnum.melonds`
*  Activity name: `me.magnum.melonds.ui.emulator.EmulatorActivity`
*  Parameters (choose one):
    * Intent data (preferred) - a URI of the NDS ROM (ZIP and 7z files are supported). The caller must grant [`FLAG_GRANT_READ_URI_PERMISSION`](https://developer.android.com/reference/android/content/Intent#FLAG_GRANT_READ_URI_PERMISSION). Persistable permission is used when the provider supports it, but is not required.
    * `uri` (deprecated) - a string with the [SAF](https://developer.android.com/guide/topics/providers/create-document-provider) URI of the NDS ROM (ZIP and 7z files are supported)
    * `PATH` (deprecated) - a string with the absolute path to the NDS ROM (ZIP and 7z files are supported)

### Pegasus metadata files

* [melonds.metadata.txt](./.github/pegasus/melonds.metadata.txt)
* [melonds-nightly.metadata.txt](./.github/pegasus/melonds-nightly.metadata.txt)

### Save files for externally launched ROMs

When launching ROMs from third-party frontends, if melonDS hasn't scanned that particular ROM previously, it won't be able to create the save file next to the ROM file if the
option "Save next to ROM file" is enabled in the settings or the save file directory is not set. Instead, melonDS will create a save file in
`Android/data/me.magnum.melonds/files/saves`.

## Nightly Builds

To have access to the latest changes, install a [nightly build](https://github.com/ulissesjdeo/melonDS-android/releases/tag/nightly-release).

Be aware that these builds can contain more bugs than usual and you may need to clear your app data to get it to work properly after updates.

## Building

The current project requires JDK 21, Android SDK platform 36, NDK `28.0.13004108`, and CMake `3.22.1`. The Gradle wrapper downloads the required Gradle version automatically.

### Build steps

1.  Clone the project, including submodules with:
    
    `git clone --recurse-submodules https://github.com/ulissesjdeo/melonDS-android.git`
2.  Install JDK 21 and the Android SDK, NDK, and CMake versions listed above.
3.  Build with:
    1.  Unix: `./gradlew :app:assembleGitHubProdDebug`
    2.  Windows: `gradlew.bat :app:assembleGitHubProdDebug`
4.  The generated APK can be found at `app/build/outputs/apk/gitHubProd/debug/app-gitHub-prod-debug.apk`.

Personal builds target ARM64 by default. To build every supported ABI, add
`-PmelonDS.abis=armeabi-v7a,arm64-v8a,x86_64` to the Gradle command.

Release builds are unsigned by default. To sign one, add the following fields to `local.properties`:

*  `MELONDS_KEYSTORE=<path_to_your_keystore>`
*  `MELONDS_KEYSTORE_PASSWORD=<keystore_password>`
*  `MELONDS_KEY_ALIAS=<name_of_your_key_alias>`
*  `MELONDS_KEY_PASSWORD=<key_alias_password>`
