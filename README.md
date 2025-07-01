# GLOBALWALLET

A futuristic, secure, and gamified multi-chain cryptocurrency wallet for Android, built with Jetpack Compose and a 3D Liquid Glass UI, powered by Firebase backend services.

## Features
- **Firebase Integration**: Complete backend with Authentication, Firestore, Storage, and Cloud Messaging
- **Non-custodial wallet** with 12-word mnemonic (BIP39)
- **Multi-chain support**: Ethereum, Base, BSC (BEP-20), Polygon, Avalanche, Binance Chain (BEP-2, mock), Solana (mock)
- **3D Liquid Glass UI**: glassmorphism, neon gradients, liquid/3D animations
- **Secure storage** (EncryptedSharedPreferences, biometric/passkey)
- **Mining, streaks, and gamified GLW rewards**
- **Referrals, SocialFi, tasks, and leaderboard**
- **P2P trading, swap, bridge, send/receive** (mocked)
- **DApp browser, CoinGecko API integration, trending tokens, news**
- **Settings**: security, theme, export/import, badges

## Firebase Backend Services

- **Authentication**: Email/password signup and login
- **Firestore Database**: User data, wallets, transactions, mining sessions, social posts
- **Cloud Storage**: Profile images and file uploads
- **Cloud Messaging**: Push notifications for rewards and updates
- **Crashlytics**: Error tracking and crash reporting
- **Analytics**: User behavior and app performance tracking

## Setup

### 1. Firebase Setup
1. **Create a Firebase project** at [Firebase Console](https://console.firebase.google.com/)
2. **Add your Android app** with package name `com.globalwallet.app`
3. **Download `google-services.json`** and place it in the `app/` directory
4. **Enable services**: Authentication, Firestore, Storage, Cloud Messaging
5. **Set up security rules** (see `FIREBASE_SETUP.md` for detailed instructions)

### 2. Android Project Setup
1. **Clone the repo:**
   ```sh
   git clone https://github.com/darksagae/GLOBALWALLET.git
   cd globalWallet
   ```
2. **Open in Android Studio**
3. **Add your Infura key** (for Ethereum) in `WalletManager.kt`
4. **Build & run** on an emulator or device (minSdk 24)

## Dependencies
- **Jetpack Compose** 1.x+
- **Firebase BOM** 32.7.0 (Authentication, Firestore, Storage, Messaging, Crashlytics)
- **web3j** for blockchain interactions
- **bip39** (novacrypto) for mnemonic generation
- **Room, Retrofit, OkHttp** for local storage and API calls
- **Lottie, compose-3d, Coil** for animations and image loading
- **AndroidX Security, Biometric** for secure storage
- **ZXing** for QR/barcode scanning
- **Dagger Hilt** for dependency injection

## Project Structure

```
app/
├── src/main/java/com/globalwallet/app/
│   ├── data/
│   │   ├── api/           # CoinGecko API integration
│   │   ├── database/      # Room database and DAOs
│   │   ├── model/         # Data models
│   │   ├── repository/    # Repository pattern implementation
│   │   ├── security/      # Security manager
│   │   └── wallet/        # Wallet management
│   ├── di/                # Dependency injection modules
│   ├── ui/
│   │   ├── components/    # Reusable UI components
│   │   ├── navigation/    # Navigation system
│   │   ├── screens/       # Main app screens
│   │   └── theme/         # App theming
│   └── viewmodels/        # ViewModels for each screen
```

## Firebase Collections Structure

- **users**: User profiles and settings
- **users/{userId}/wallets**: User's cryptocurrency wallets
- **users/{userId}/transactions**: Transaction history
- **users/{userId}/mining_sessions**: Mining activity data
- **social_posts**: SocialFi posts and interactions
- **referrals**: Referral tracking and rewards
- **glw_rewards**: GLW token reward history

## Security Features

- **Encrypted local storage** for sensitive data
- **Biometric authentication** for app access
- **Firebase Authentication** for user accounts
- **Secure Firestore rules** for data access control
- **Encrypted wallet storage** with mnemonic backup

## Notes
- BEP-2 and Solana are mocked for demo purposes
- All sensitive data is encrypted on-device
- Firebase provides real-time data synchronization
- Push notifications for mining rewards and social interactions
- Analytics tracking for user engagement and app performance

## License
MIT

## Support

For Firebase setup help, see `FIREBASE_SETUP.md`
For general setup instructions, see `SETUP_INSTRUCTIONS.md`
For running the demo, see `RUN_DEMO.md`
