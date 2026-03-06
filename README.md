# Blood Pressure Tracker - Android App

An AI-powered blood pressure tracking app built with Jetpack Compose and Gemini AI.

[![Build APK](https://github.com/YOUR_USERNAME/bp-tracker-app/actions/workflows/build-apk.yml/badge.svg)](https://github.com/YOUR_USERNAME/bp-tracker-app/actions)

## 📥 Download APK

**Latest Build:** Check the [Releases](../../releases) page for the latest APK

Or download from [GitHub Actions Artifacts](../../actions)

## Architecture

This project follows Clean Architecture principles with MVVM pattern:

```
app/
├── data/           # Data layer
│   ├── local/      # Room database entities, DAOs
│   ├── remote/     # Gemini API integration
│   └── repository/ # Repository implementations
├── domain/         # Domain layer
│   ├── model/      # Domain models
│   └── usecase/    # Business logic use cases
├── presentation/   # Presentation layer
│   ├── home/       # Home screen ViewModels
│   ├── history/    # History screen ViewModels
│   ├── insights/   # AI insights ViewModels
│   ├── camera/     # Camera scanning ViewModels
│   └── settings/   # Settings ViewModels
├── di/             # Dependency injection modules
├── utils/          # Utility classes
└── workers/        # Background workers for reminders
```

## Features

### Core Features
- **Manual BP Entry**: Add blood pressure readings manually
- **Camera Scanning**: Use CameraX to capture BP device readings
- **OCR Processing**: Gemini 2.0 Flash for extracting values from images
- **AI Insights**: Generate health insights using Gemini API
- **Reminders**: WorkManager-based notification system
- **History Tracking**: View and filter reading history
- **Offline-First**: Room database for local storage

### Tech Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose + Material3
- **Architecture**: MVVM + Clean Architecture
- **DI**: Hilt
- **Database**: Room
- **Networking**: Retrofit + OkHttp
- **Image Processing**: CameraX
- **AI Integration**: Gemini 2.0 Flash API
- **Background Tasks**: WorkManager
- **Async**: Kotlin Coroutines + Flow

## Setup

### Prerequisites
- Android Studio Hedgehog or later
- JDK 17
- Android SDK 34
- Minimum SDK 26 (Android 8.0)

### Configuration

1. **Gemini API Key**: Add your Gemini API key to `app/build.gradle.kts`:
```kotlin
buildConfigField("String", "GEMINI_API_KEY", "\"YOUR_API_KEY_HERE\"")
```

Or create a `local.properties` file:
```properties
gemini.api.key=YOUR_API_KEY_HERE
```

2. **Build the project**:
```bash
./gradlew build
```

3. **Run the app**:
```bash
./gradlew installDebug
```

## Database Schema

### BP Readings
- id (Long, Primary Key)
- systolic (Int)
- diastolic (Int)
- pulse (Int)
- timestamp (Instant)
- notes (String, nullable)
- imageUri (String, nullable)
- source (MANUAL/SCANNED)
- createdAt, updatedAt (Instant)

### Reminders
- id (Long, Primary Key)
- time (LocalTime)
- daysOfWeek (Set<DayOfWeek>)
- isEnabled (Boolean)
- label (String, nullable)

## API Integration

### Gemini API Endpoints

**Extract BP Reading**:
- Model: `gemini-2.0-flash-exp`
- Input: Image + extraction prompt
- Output: JSON with systolic, diastolic, pulse values

**Generate Insights**:
- Model: `gemini-2.0-flash-exp`
- Input: Reading history
- Output: Text-based health insights

## Permissions Required

- `CAMERA`: For scanning BP device displays
- `POST_NOTIFICATIONS`: For reminder notifications (Android 13+)
- `INTERNET`: For Gemini API calls
- `SCHEDULE_EXACT_ALARM`: For precise reminder scheduling

## Use Cases

### Domain Layer Use Cases

1. **AddBPReadingUseCase**: Validate and save new readings
2. **GetBPReadingsUseCase**: Retrieve readings with filtering
3. **DeleteBPReadingUseCase**: Remove readings
4. **ScanBPReadingUseCase**: OCR + save scanned readings
5. **GetInsightsUseCase**: Generate AI-powered insights
6. **ManageRemindersUseCase**: CRUD operations for reminders

## ViewModels

- **HomeViewModel**: Recent readings, manual entry
- **HistoryViewModel**: Reading history with date filtering
- **InsightsViewModel**: AI-generated health insights
- **CameraViewModel**: Image capture and OCR processing
- **SettingsViewModel**: Reminder management

## Background Workers

**ReminderWorker**:
- Periodic work for scheduled reminders
- Creates notifications at configured times
- Managed by `ReminderScheduler`

## Testing

```bash
# Unit tests
./gradlew test

# Instrumented tests
./gradlew connectedAndroidTest
```

## Proguard Rules

Proguard rules are configured for:
- Room entities
- Retrofit models
- Hilt components
- Gson serialization

## Build Variants

- **Debug**: Full logging, no obfuscation
- **Release**: Minified, obfuscated, optimized

## UI Development

UI screens and components will be implemented by the UI developer team using:
- Jetpack Compose
- Material3 Design
- Navigation Component
- ViewModels (already created)

## Contributing

This is the Android backend/business logic layer. UI components are pending implementation.

## License

Copyright 2024 BP Tracker Team
