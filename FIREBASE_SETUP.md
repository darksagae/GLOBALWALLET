# Firebase Setup for Global Wallet

This guide will help you set up Firebase for your Global Wallet Android app.

## Prerequisites

1. A Google account
2. Android Studio installed
3. Your Global Wallet project ready

## Step 1: Create a Firebase Project

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Click "Create a project" or "Add project"
3. Enter a project name (e.g., "Global Wallet")
4. Choose whether to enable Google Analytics (recommended)
5. Click "Create project"

## Step 2: Add Android App to Firebase

1. In your Firebase project console, click the Android icon (</>) to add an Android app
2. Enter your package name: `com.globalwallet.app`
3. Enter app nickname: "Global Wallet"
4. Click "Register app"

## Step 3: Download Configuration File

1. Download the `google-services.json` file
2. Place it in the `app/` directory of your Android project
3. **Important**: Never commit this file to version control (it's already in .gitignore)

## Step 4: Enable Firebase Services

### Authentication
1. In Firebase Console, go to "Authentication"
2. Click "Get started"
3. Go to "Sign-in method" tab
4. Enable "Email/Password" authentication
5. Click "Save"

### Firestore Database
1. Go to "Firestore Database"
2. Click "Create database"
3. Choose "Start in test mode" (for development)
4. Select a location close to your users
5. Click "Done"

### Storage
1. Go to "Storage"
2. Click "Get started"
3. Choose "Start in test mode" (for development)
4. Select a location
5. Click "Done"

### Cloud Messaging (Optional)
1. Go to "Cloud Messaging"
2. The service is automatically enabled

### Crashlytics (Optional)
1. Go to "Crashlytics"
2. Click "Enable Crashlytics"
3. Follow the setup instructions

## Step 5: Security Rules

### Firestore Security Rules
Replace the default rules with:

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Users can only access their own data
    match /users/{userId} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }
    
    // Users can access their own wallets
    match /users/{userId}/wallets/{walletId} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }
    
    // Users can access their own transactions
    match /users/{userId}/transactions/{transactionId} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }
    
    // Users can access their own mining sessions
    match /users/{userId}/mining_sessions/{sessionId} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }
    
    // Public social posts (read-only for all authenticated users)
    match /social_posts/{postId} {
      allow read: if request.auth != null;
      allow write: if request.auth != null;
    }
    
    // Referrals
    match /referrals/{referralId} {
      allow read, write: if request.auth != null;
    }
    
    // GLW rewards
    match /glw_rewards/{rewardId} {
      allow read, write: if request.auth != null;
    }
  }
}
```

### Storage Security Rules
Replace the default rules with:

```javascript
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    // Users can only upload their own profile images
    match /profile_images/{userId} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }
  }
}
```

## Step 6: Test the Setup

1. Build and run your app
2. Try to create an account or sign in
3. Check Firebase Console to see if data is being created

## Step 7: Production Considerations

Before going to production:

1. **Update Security Rules**: Change from "test mode" to proper security rules
2. **Enable App Check**: Add App Check to prevent abuse
3. **Set up Monitoring**: Enable Performance Monitoring and Crashlytics
4. **Configure Analytics**: Set up custom events for better insights
5. **Backup Strategy**: Set up automated backups for your Firestore data

## Troubleshooting

### Common Issues

1. **"google-services.json not found"**
   - Make sure the file is in the `app/` directory
   - Clean and rebuild your project

2. **Authentication errors**
   - Check if Email/Password authentication is enabled
   - Verify your package name matches exactly

3. **Firestore permission errors**
   - Check your security rules
   - Ensure you're in test mode during development

4. **Build errors**
   - Make sure all Firebase dependencies are added to build.gradle
   - Sync your project with Gradle files

### Getting Help

- [Firebase Documentation](https://firebase.google.com/docs)
- [Firebase Support](https://firebase.google.com/support)
- [Stack Overflow](https://stackoverflow.com/questions/tagged/firebase)

## Next Steps

After setting up Firebase:

1. Test all authentication flows
2. Verify data is being saved to Firestore
3. Test file uploads to Storage
4. Set up push notifications
5. Configure analytics events
6. Test the app on different devices

Your Global Wallet app is now ready to use Firebase for backend services! 