# BP Tracker Android Project Structure

## Overview
Complete Android application backend with Clean Architecture, MVVM, and Hilt DI.

## Project Location
`/workspace/group/bp-tracker-app/`

## Module Structure

### Data Layer (`app/src/main/java/com/bptracker/data/`)

#### Local Database
- **BPReading.kt**: Entity for blood pressure readings
- **Reminder.kt**: Entity for measurement reminders
- **Converters.kt**: Room type converters for Instant, LocalTime, DayOfWeek
- **BPReadingDao.kt**: DAO with Flow-based queries for readings
- **ReminderDao.kt**: DAO for reminder CRUD operations
- **BPDatabase.kt**: Room database with version 1 schema

#### Remote API
- **GeminiApiService.kt**: Retrofit interface for Gemini API
- **models/GeminiModels.kt**: Request/Response models with polymorphic Part serialization
- **models/BPReadingResult.kt**: OCR extraction result model
- **GeminiClient.kt**: High-level client with extractBPReading() and generateInsights()

#### Repositories
- **BPReadingRepository.kt**: Reading data operations with Flow
- **ReminderRepository.kt**: Reminder data operations
- **InsightsRepository.kt**: AI insights generation wrapper

### Domain Layer (`app/src/main/java/com/bptracker/domain/`)

#### Models
- **BPCategory.kt**: Blood pressure classification (Normal, Elevated, Hypertension 1/2, Crisis)
- **DateRange.kt**: Date range helpers with predefined types (Week, Month, 3M, 6M, Year, All)

#### Use Cases
- **AddBPReadingUseCase.kt**: Validate and insert readings
- **GetBPReadingsUseCase.kt**: Query readings with filtering
- **DeleteBPReadingUseCase.kt**: Remove readings
- **ScanBPReadingUseCase.kt**: Extract from image + save
- **GetInsightsUseCase.kt**: Generate AI insights for date range
- **ManageRemindersUseCase.kt**: Full reminder CRUD

### Presentation Layer (`app/src/main/java/com/bptracker/presentation/`)

#### ViewModels
- **home/HomeViewModel.kt**: Recent readings, manual entry, latest category
- **history/HistoryViewModel.kt**: Filterable history with date ranges
- **insights/InsightsViewModel.kt**: AI insights with loading states
- **camera/CameraViewModel.kt**: Image processing, OCR confirmation
- **settings/SettingsViewModel.kt**: Reminder management UI state

All ViewModels:
- Use `StateFlow` for UI state
- Inject use cases via Hilt
- Handle loading/error states
- Support coroutines + Flow

### Dependency Injection (`app/src/main/java/com/bptracker/di/`)

- **DatabaseModule.kt**: Provides Room database and DAOs
- **NetworkModule.kt**: Provides Retrofit, OkHttp, Gson with custom Part serializer
- **WorkManagerModule.kt**: Provides WorkManager instance

### Workers (`app/src/main/java/com/bptracker/workers/`)

- **ReminderWorker.kt**: HiltWorker for showing notifications
- **ReminderScheduler.kt**: Schedules periodic work for each day of week

### Utilities (`app/src/main/java/com/bptracker/utils/`)

- **DateTimeExtensions.kt**: Instant formatting, relative dates
- **BPValidation.kt**: Validation for systolic/diastolic/pulse ranges
- **PermissionHelper.kt**: Camera and notification permission checks
- **ImageUtils.kt**: Bitmap operations, EXIF rotation, resizing

### Application & Activity

- **BPTrackerApplication.kt**: Hilt application with WorkManager configuration
- **MainActivity.kt**: Compose activity with placeholder UI

## Configuration Files

### Gradle
- **settings.gradle.kts**: Single app module configuration
- **build.gradle.kts** (root): Plugin versions, Hilt classpath
- **app/build.gradle.kts**: Full dependency list including:
  - Compose BOM 2024.02.00
  - Room 2.6.1
  - Hilt 2.50
  - WorkManager 2.9.0
  - CameraX 1.3.1
  - Retrofit 2.9.0
  - Kotlin 1.9.22

### Android
- **AndroidManifest.xml**: Permissions, Application class, MainActivity
- **proguard-rules.pro**: Keep rules for Room, Retrofit, Hilt, Gson
- **strings.xml**: All user-facing strings
- **themes.xml**: Material theme configuration

### Resources
- **drawable/ic_notification.xml**: Notification icon vector
- **xml/backup_rules.xml**: Backup configuration
- **xml/data_extraction_rules.xml**: Data extraction rules

## Key Features Implemented

### Database
- Offline-first architecture with Room
- Flow-based reactive queries
- Type converters for Instant, LocalTime, DayOfWeek, Enums
- Date range queries with averages

### API Integration
- Gemini 2.0 Flash API integration
- OCR for BP device reading extraction
- AI-powered health insights generation
- Custom Gson serialization for polymorphic Part types
- Error handling and Result wrappers

### Background Work
- WorkManager with HiltWorker support
- Periodic reminders (weekly)
- Initial delay calculation for correct scheduling
- Notification channel creation (Android O+)

### Architecture
- Clean Architecture layers (data, domain, presentation)
- MVVM with StateFlow
- Dependency injection with Hilt
- Repository pattern with Flow
- Use case pattern for business logic

## Validation Rules

### Blood Pressure
- Systolic: 70-250 mmHg
- Diastolic: 40-150 mmHg
- Pulse: 40-200 bpm
- Systolic must be > Diastolic

### BP Categories
- Normal: <120/<80
- Elevated: 121-129/<80
- Stage 1: 130-139/80-89
- Stage 2: 140-179/90-119
- Crisis: 180+/120+

## Pending Implementation

### UI Layer (Awaiting UI Developer)
- Compose screens for all features
- Navigation graph
- Theme and color scheme
- Camera preview composable
- Charts and visualizations
- Form inputs and validation UI
- Settings screens

### Additional Features
- Data export (CSV/PDF)
- Chart visualizations
- Statistics dashboard
- Dark theme
- User onboarding
- Backup/restore

## Testing Strategy

### Unit Tests (To Be Added)
- Use case tests with fake repositories
- ViewModel tests with coroutines test
- Repository tests with in-memory database
- Validation logic tests

### Integration Tests (To Be Added)
- Room DAO tests
- API client tests
- WorkManager tests

## Build Instructions

1. Open project in Android Studio
2. Add Gemini API key to `app/build.gradle.kts`:
   ```kotlin
   buildConfigField("String", "GEMINI_API_KEY", "\"YOUR_KEY\"")
   ```
3. Sync Gradle
4. Build: `./gradlew assembleDebug`
5. Run: Select device and run MainActivity

## API Requirements

- **Gemini API Key**: Required for OCR and insights
- **Internet Permission**: Required for API calls
- **Minimum Android**: API 26 (Android 8.0)
- **Target Android**: API 34 (Android 14)

## Next Steps

1. UI developer implements Compose screens
2. Connect ViewModels to UI
3. Implement navigation
4. Add charts and visualizations
5. Testing and QA
6. Performance optimization
7. Release preparation
