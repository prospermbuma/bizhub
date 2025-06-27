# 🔥 Firebase Setup Guide for BizHub

## 📋 Prerequisites
- Google account
- Firebase project (named `bizhub-field-service`)
- Google Cloud Console access

## 🚀 Step-by-Step Setup

### 1. Create Firebase Project
1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Click **"Create a project"**
3. Project name: `bizhub-field-service`
4. Disable Google Analytics (optional)
5. Click **"Create project"**

### 2. Register Android App
1. Click **Android icon** (</>) in Firebase dashboard
2. Package name: `com.extrap.co.bizhub`
3. App nickname: `BizHub Field Service`
4. Skip SHA-1 for now
5. Click **"Register app"**

### 3. Download Configuration
1. Download `google-services.json`
2. Place in: `Bizhub/app/google-services.json`

### 4. Enable Firebase Services

#### Authentication
- Go to **Authentication** → **Sign-in method**
- Enable **Email/Password**
- Click **"Save"**

#### Firestore Database
- Go to **Firestore Database**
- Click **"Create database"**
- Choose **"Start in test mode"**
- Select location (e.g., us-central1)
- Click **"Done"**

#### Firebase Storage
- Go to **Storage**
- Click **"Get started"**
- Choose **"Start in test mode"**
- Select same location as Firestore
- Click **"Done"**

### 5. Google Maps API Setup
1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Select your Firebase project
3. Go to **APIs & Services** → **Library**
4. Enable:
   - **Maps SDK for Android**
   - **Places API**
5. Go to **APIs & Services** → **Credentials**
6. Create **API Key**
7. Copy the API key

### 6. Configure API Key
1. Open `app/src/main/res/values/google_maps_api.xml`
2. Replace `your-api-key-here` with your actual API key
3. Save the file

## ✅ Verification Checklist

- [ ] `google-services.json` is in `app/` directory
- [ ] Google Maps API key is configured in `google_maps_api.xml`
- [ ] Firebase Authentication is enabled
- [ ] Firestore Database is created
- [ ] Firebase Storage is set up
- [ ] Project builds successfully

## 🔧 Testing

1. **Build the project:**
   ```bash
   ./gradlew build
   ```

2. **Run the app:**
   - Open in Android Studio
   - Click Run button
   - Test login functionality
   - Test map features

## 🚨 Troubleshooting

### Common Issues:
1. **"google-services.json not found"**
   - Ensure file is in `app/` directory
   - Check file name spelling

2. **"API key not valid"**
   - Verify API key in `google_maps_api.xml`
   - Check if Maps SDK is enabled in Google Cloud Console

3. **"Authentication failed"**
   - Verify Email/Password is enabled in Firebase Console
   - Check internet connection

## 📞 Support
- [Firebase Documentation](https://firebase.google.com/docs)
- [Google Maps Documentation](https://developers.google.com/maps/documentation/android-sdk)
- [Firebase Console](https://console.firebase.google.com/)
- [Google Cloud Console](https://console.cloud.google.com/) 