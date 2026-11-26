## Summary

<!-- Describe the problem and the resulting behavior. Link a related issue if available. English and Chinese are welcome. -->

## Validation

<!-- List exact commands and results. Mark checks not run and explain why. See CONTRIBUTING.md for setup. -->

- Java checks and AAR build/staging:
- HBuilderX version and UTS compilation, if run:
- Android device or emulator and Android version, if run:
- Callback and lifecycle cases checked, including whether the custom debug base was rebuilt:

<!-- Java/AAR checks do not verify the UTS bridge or page on a device. DEMO-0001 is a simulated barcode, including in phone runs; it does not verify physical scanner hardware or a vendor SDK. -->

## Checklist

- [ ] The change is focused, with regression coverage for changed behavior where applicable.
- [ ] Usage changes are reflected in both README files and the relevant guide.
- [ ] The public AppID stays empty; private AppIDs, credentials, vendor SDK files and generated build outputs are excluded.
