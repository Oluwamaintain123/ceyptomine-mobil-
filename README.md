# CryptoMine Mobile (Android)

Android Kotlin project for an explicitly user-controlled mobile mining dashboard and XMRig integration.

## Important status

This is a **buildable starter project**, not yet a verified mining release. The Android UI and GitHub Actions APK build are included. A compatible ARM64 Android XMRig executable is **not bundled** in this starter. Until a trusted native engine is built, audited, and added, the app will clearly report that the engine is unavailable and will not claim to mine or show fabricated earnings.

The target coin is a configurable mineable coin; Monero/RandomX is the initial technical benchmark only. Profitability on a phone is likely poor. Sustained CPU mining can overheat the device and accelerate battery wear.

## Build an APK

1. Create a GitHub repository named `cryptomine-mobile`.
2. Upload the contents of this ZIP to the repository's `main` branch.
3. Open **Actions** and enable workflows if GitHub prompts you.
4. Run **Android APK Build** using **Run workflow**, or push a commit.
5. Download the `CryptoMine-debug-apk` artifact from the completed workflow.
6. Install the APK on your own phone. Android may ask you to allow installs from the browser/file manager used to open it.

The workflow builds the Android application UI. It does **not** magically provide a mining engine.

## Native engine integration requirements

Before real mining is enabled:
- Build XMRig from its upstream source for Android `arm64-v8a` using the Android NDK and its required dependencies.
- Review upstream source, license, dependency versions, and build provenance.
- Add the resulting Android-compatible executable to the app's private assets under `app/src/main/assets/engine/` and implement the copy/permission/process integration in `MiningService`.
- Validate pool TLS, accepted shares, thermal behavior, foreground-service behavior, and payout configuration on-device.
- Do not embed anyone else's wallet seed phrase or private key. A public payout address is enough.

Upstream project: https://github.com/xmrig/xmrig  
Android build reference (community project; inspect before reuse): https://github.com/XMRig-for-Android/xmrig-for-android

## Current safeguards

- Mining is never started automatically.
- The Start action fails closed if the native engine is missing.
- No simulated hashrate, shares, or earnings are shown.
- The app does not request wallet secrets.
- Conservative mode is the default design target; keep CPU thread count limited and stop if the phone becomes hot.
