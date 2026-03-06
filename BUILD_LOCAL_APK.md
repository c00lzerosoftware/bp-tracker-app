# Building Local APK for Testing

This guide will help you build a debug APK to test the Blood Pressure Tracker app on your Android device.

## Prerequisites

1. **Android Studio** (latest stable version)
   - Download from: https://developer.android.com/studio

2. **JDK 17** (bundled with Android Studio)

3. **Android SDK** (installed via Android Studio)
   - Minimum SDK: API 26 (Android 8.0)
   - Target SDK: API 34 (Android 14)

## Setup Steps

### 1. Open Project in Android Studio

1. Launch Android Studio
2. Select "Open an Existing Project"
3. Navigate to `/workspace/group/bp-tracker-app/`
4. Click "OK"

### 2. Sync Gradle

1. Android Studio will automatically prompt to sync Gradle
2. Click "Sync Now" in the notification bar
3. Wait for Gradle sync to complete (may take a few minutes on first run)
4. If you see any SDK-related errors, click "Install missing SDK" when prompted

### 3. Gemini API Key (Already Configured)

The Gemini API key has been added to the build configuration:
- Located in: `app/build.gradle.kts`
- Key: AIzaSyDVGzkRvaNtYB2V5s9q45vmXWc1hA1OCW8

**Note:** For production, store API keys securely in `local.properties` or use environment variables.

## Building the APK

### Method 1: Using Android Studio (Recommended)

1. **Build the APK:**
   - Go to: `Build` → `Build Bundle(s) / APK(s)` → `Build APK(s)`
   - Wait for the build to complete
   - A notification will appear: "APK(s) generated successfully"

2. **Locate the APK:**
   - Click "locate" in the notification, or
   - Navigate to: `app/build/outputs/apk/debug/`
   - File name: `app-debug.apk`

### Method 2: Using Gradle Command Line

1. **Open Terminal in Android Studio** (bottom panel)

2. **Run build command:**
   ```bash
   ./gradlew assembleDebug
   ```

3. **Locate the APK:**
   ```
   app/build/outputs/apk/debug/app-debug.apk
   ```

### Method 3: Using System Terminal

```bash
cd /workspace/group/bp-tracker-app
./gradlew assembleDebug
```

## Installing the APK

### On Physical Device:

1. **Enable Developer Options:**
   - Go to: Settings → About Phone
   - Tap "Build Number" 7 times
   - Go back to Settings → Developer Options
   - Enable "USB Debugging"

2. **Connect Device via USB**

3. **Install via Android Studio:**
   - Click the "Run" button (green play icon)
   - Select your device from the list
   - App will be installed and launched

4. **Install via ADB:**
   ```bash
   adb install app/build/outputs/apk/debug/app-debug.apk
   ```

### On Android Emulator:

1. **Create Emulator** (if not already created):
   - Tools → Device Manager → Create Device
   - Select a device (e.g., Pixel 6)
   - Select system image (API 34 recommended)
   - Click "Finish"

2. **Install and Run:**
   - Click the "Run" button
   - Select your emulator
   - App will launch automatically

### Manual Installation (Transfer APK to Device):

1. Copy `app-debug.apk` to your phone (via USB or cloud storage)
2. On your phone, enable "Install Unknown Apps" for your file manager
3. Tap the APK file to install
4. Grant camera and notification permissions when prompted

## App Permissions

The app will request the following permissions:
- **Camera**: For scanning BP monitor displays
- **Notifications**: For daily reminders
- **Post Notifications** (Android 13+): For reminder alerts

## Testing Features

The debug APK includes all features:

1. **Manual Entry**: Add BP readings manually
2. **Camera Scan**: (Coming soon - requires camera implementation)
3. **Reading History**: View all past readings
4. **AI Insights**: Gemini-powered health insights
5. **Reminders**: Set multiple daily reminders
6. **Charts**: Visualize BP trends over time

## Troubleshooting

### Gradle Sync Failed
```
Solution: File → Invalidate Caches → Invalidate and Restart
```

### SDK Not Found
```
Solution: Tools → SDK Manager → Install required SDKs (API 26, 34)
```

### Build Failed - Memory Issues
```
Solution: Increase Gradle memory in gradle.properties:
org.gradle.jvmargs=-Xmx4096m
```

### App Crashes on Launch
```
Check Logcat in Android Studio for error messages
Common issues:
- Missing permissions in AndroidManifest.xml
- Hilt dependency injection errors
- Database migration issues
```

## Build Variants

- **debug**: For testing, includes debugging tools
- **release**: For production (requires signing key)

Current configuration builds the `debug` variant.

## Next Steps

After testing the debug APK:

1. **Report Issues**: Note any bugs or crashes
2. **Test All Features**: Try manual entry, viewing history
3. **Check Performance**: Monitor battery usage and responsiveness
4. **Test Permissions**: Ensure camera and notifications work
5. **Test Dark Mode**: Toggle system dark mode

## APK File Location

After successful build:
```
/workspace/group/bp-tracker-app/app/build/outputs/apk/debug/app-debug.apk
```

Size: Approximately 8-12 MB (debug build)

## Support

For issues or questions:
- Check Android Studio Logcat for crash logs
- Review gradle build output for errors
- Ensure all dependencies are downloaded
- Verify Android SDK is properly installed

---

**Ready to Build!** Follow the steps above and you'll have a working APK in minutes.
