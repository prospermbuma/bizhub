# FSP - Field Service Management App

[![Android](https://img.shields.io/badge/Android-API%2024+-green.svg)](https://developer.android.com/about/versions/android-14.0)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Build Status](https://img.shields.io/badge/Build-Passing-brightgreen.svg)]()

## 📱 Overview

FSP is a comprehensive Android field service management application designed for technicians, service providers, and field workers. The app streamlines work order management, customer relationship management, and field operations with offline capabilities and real-time synchronization.

## ✨ Features

### 🔐 Authentication & Security
- **User Authentication**: Secure login/signup with Firebase Auth
- **Role-based Access**: Support for admin and technician roles
- **Password Reset**: Email-based password recovery
- **Session Management**: Secure token handling

### 📋 Work Order Management
- **Create & Assign**: Generate work orders with priority levels
- **Status Tracking**: Real-time status updates (Pending, In Progress, Completed)
- **Priority Management**: High, Medium, Low, and Urgent priority levels
- **Work Order Details**: Comprehensive order information and history
- **Photo Documentation**: Capture and attach photos to work orders

### 👥 Customer Management
- **Customer Database**: Store customer information and contact details
- **Customer Profiles**: Complete customer history and preferences
- **Premium Customer Support**: Special handling for premium customers
- **Contact Integration**: Direct calling and messaging capabilities

### 📊 Dashboard & Analytics
- **Real-time Dashboard**: Overview of daily tasks and performance metrics
- **Performance Analytics**: Revenue tracking and job completion statistics
- **Customer Satisfaction**: Feedback and rating system
- **Reports Generation**: Daily, weekly, and monthly reports

### 🗺️ Location & Mapping
- **GPS Integration**: Real-time location tracking
- **Google Maps Integration**: Interactive maps for work order locations
- **Route Optimization**: Efficient route planning for field visits
- **Location History**: Track technician movements

### 📷 Camera & Scanning
- **Camera Integration**: Photo capture for documentation
- **QR Code Scanner**: Scan equipment and product codes
- **Barcode Scanning**: Inventory and equipment tracking
- **Document Scanning**: Capture and store important documents

### 🔧 Hardware Integration
- **Bluetooth Support**: Connect to field equipment and devices
- **RFID Reader**: Equipment identification and tracking
- **Printer Support**: Print receipts and work orders
- **External Device Support**: Various hardware integrations

### 📱 Offline Capabilities
- **Offline Mode**: Work without internet connection
- **Data Synchronization**: Automatic sync when connection restored
- **Local Database**: Room database for offline data storage
- **Conflict Resolution**: Handle data conflicts during sync

### 🔔 Notifications
- **Push Notifications**: Real-time work order updates
- **SMS Notifications**: Customer communication
- **Email Integration**: Automated email notifications
- **Custom Notification Channels**: Organized notification categories

### ⚙️ Settings & Configuration
- **User Preferences**: Customizable app settings
- **Sync Settings**: Configure synchronization preferences
- **Notification Settings**: Manage notification preferences
- **Account Management**: Profile and security settings

## 🛠️ Technical Stack

### Core Technologies
- **Language**: Java
- **Minimum SDK**: API 24 (Android 7.0)
- **Target SDK**: API 34 (Android 14)
- **Architecture**: MVVM with Repository pattern

### Key Libraries & Dependencies
- **UI Components**: Material Design, Navigation Component
- **Database**: Room Database with SQLite
- **Networking**: Retrofit2, OkHttp3
- **Authentication**: Firebase Auth
- **Cloud Services**: Firebase Firestore, Firebase Storage
- **Maps & Location**: Google Play Services Location & Maps
- **Image Loading**: Glide
- **Camera**: CameraX
- **QR/Barcode**: ZXing Android Embedded
- **Charts**: MPAndroidChart
- **Permissions**: Dexter

## 📁 Project Structure

```
app/
├── src/main/
│   ├── java/com/extrap/co/bizhub/
│   │   ├── activities/          # Activity classes
│   │   ├── adapters/           # RecyclerView adapters
│   │   ├── data/               # Database and data layer
│   │   │   ├── dao/           # Data Access Objects
│   │   │   └── entities/      # Database entities
│   │   ├── fragments/         # Fragment classes
│   │   ├── services/          # Background services
│   │   ├── utils/             # Utility classes
│   │   └── viewmodels/        # ViewModel classes
│   ├── res/
│   │   ├── layout/            # UI layouts
│   │   ├── values/            # Resources (strings, colors, etc.)
│   │   ├── drawable/          # Icons and graphics
│   │   └── menu/              # Menu layouts
│   └── AndroidManifest.xml
```

## 🚀 Getting Started

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK API 24+
- Google Play Services
- Firebase project setup

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-username/bizhub.git
   cd bizhub
   ```

2. **Setup Firebase**
   - Create a Firebase project
   - Download `google-services.json` and place it in the `app/` directory
   - Enable Authentication, Firestore, and Storage services

3. **Configure Google Maps**
   - Get Google Maps API key from Google Cloud Console
   - Add the API key to your project

4. **Build and Run**
   ```bash
   ./gradlew build
   ./gradlew installDebug
   ```

### Configuration

1. **Firebase Setup**
   - Enable Email/Password authentication
   - Create Firestore database
   - Configure Firebase Storage rules

2. **Google Services**
   - Enable Maps SDK for Android
   - Enable Places API
   - Configure API restrictions

3. **App Permissions**
   - Camera access for photo capture
   - Location access for GPS tracking
   - Storage access for file management
   - Bluetooth access for device connectivity

## 📱 Screenshots

*[Screenshots will be added here]*

## 🔧 Configuration

### Environment Variables
```properties
# Firebase Configuration
FIREBASE_PROJECT_ID=your-project-id
GOOGLE_MAPS_API_KEY=your-maps-api-key

# App Configuration
APP_VERSION=1.0.0
MIN_SDK_VERSION=24
TARGET_SDK_VERSION=34
```

### Build Variants
- **Debug**: Development build with logging enabled
- **Release**: Production build with optimizations

## 🧪 Testing

### Unit Tests
```bash
./gradlew test
```

### Instrumented Tests
```bash
./gradlew connectedAndroidTest
```

### Test Coverage
```bash
./gradlew jacocoTestReport
```

## 📦 Deployment

### Release Build
```bash
./gradlew assembleRelease
```

### APK Signing
1. Generate keystore file
2. Configure signing in `build.gradle`
3. Build signed APK

### Google Play Store
1. Create release bundle
2. Upload to Google Play Console
3. Configure store listing
4. Submit for review

## 🔒 Security

### Data Protection
- **Encryption**: All sensitive data encrypted at rest
- **Network Security**: HTTPS for all API communications
- **Authentication**: Secure token-based authentication
- **Permissions**: Minimal required permissions

### Privacy Compliance
- **GDPR Compliance**: User data protection
- **Data Retention**: Configurable data retention policies
- **User Consent**: Explicit consent for data collection

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Code Style
- Follow Android coding conventions
- Use meaningful variable and method names
- Add comments for complex logic
- Write unit tests for new features

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Support

### Documentation
- [User Guide](docs/user-guide.md)
- [API Documentation](docs/api.md)
- [Troubleshooting](docs/troubleshooting.md)

### Contact
- **Email**: support@bizhub.com
- **Phone**: +1-800-BIZHUB
- **Website**: https://bizhub.com

### Community
- [GitHub Issues](https://github.com/your-username/bizhub/issues)
- [Discussions](https://github.com/your-username/bizhub/discussions)
- [Wiki](https://github.com/your-username/bizhub/wiki)

## 🙏 Acknowledgments

- [Android Developers](https://developer.android.com/)
- [Firebase](https://firebase.google.com/)
- [Google Maps Platform](https://developers.google.com/maps)
- [Material Design](https://material.io/design)

## 📈 Roadmap

### Version 1.1
- [ ] Advanced reporting features
- [ ] Multi-language support
- [ ] Dark theme
- [ ] Widget support

### Version 1.2
- [ ] AI-powered scheduling
- [ ] Voice commands
- [ ] AR equipment identification
- [ ] Advanced analytics

### Version 2.0
- [ ] Web dashboard
- [ ] API for third-party integrations
- [ ] Multi-tenant support
- [ ] Advanced workflow automation

---

**Made with ❤️ for field service professionals** 
