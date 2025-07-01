# Global Wallet - Setup Instructions

## 🚀 Quick Start

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK 24 or higher
- Java 8 or higher
- An Infura account (free)

### 1. Clone and Open Project
```bash
git clone <repository-url>
cd globalWallet
```

Open the project in Android Studio.

### 2. Configure Infura API Key

**IMPORTANT: You must add your Infura API key to use real blockchain features.**

1. Go to [Infura.io](https://infura.io/) and create a free account
2. Create a new project
3. Copy your Project ID (this is your API key)
4. Open `app/src/main/java/com/globalwallet/app/config/AppConfig.kt`
5. Replace `"YOUR_INFURA_API_KEY"` with your actual Project ID:

```kotlin
const val INFURA_API_KEY = "your_actual_project_id_here"
```

### 3. Build and Run

1. Sync the project with Gradle files
2. Connect an Android device or start an emulator
3. Click the "Run" button in Android Studio

## 🔧 Configuration Options

### Network Configuration
In `AppConfig.kt`, you can configure:

```kotlin
// Set to true for testnet, false for mainnet
const val USE_TESTNET = false

// Enable/disable features
const val ENABLE_REAL_BLOCKCHAIN = true
const val ENABLE_MOCK_DATA = true
```

### Supported Networks
- **Ethereum** (Mainnet/Sepolia)
- **Polygon** (Mainnet/Mumbai)
- **Base** (Mainnet/Sepolia)
- **BSC** (Mainnet)
- **Avalanche** (Mainnet)
- **Solana** (Mainnet)

## 🛠️ Troubleshooting

### Common Issues

#### 1. Build Errors
- **Error**: "Cannot resolve symbol 'dagger'"
  - **Solution**: Sync project with Gradle files

- **Error**: "Cannot resolve symbol 'web3j'"
  - **Solution**: Clean and rebuild project

#### 2. Runtime Errors
- **Error**: "Infura API key not configured"
  - **Solution**: Add your Infura Project ID to `AppConfig.INFURA_API_KEY`

- **Error**: "Network connection error"
  - **Solution**: Check internet connection and Infura API key

#### 3. Permission Errors
- **Error**: "Camera permission denied"
  - **Solution**: Grant camera permission in app settings

### Debug Mode
To enable debug logging, add to `AppConfig.kt`:
```kotlin
const val DEBUG_MODE = true
```

## 🔒 Security Features

### Biometric Authentication
- Fingerprint/Face recognition for wallet access
- Secure key storage using Android Keystore
- Encrypted SharedPreferences for sensitive data

### Private Key Management
- Private keys are encrypted and stored securely
- Never stored in plain text
- Protected by biometric authentication

## 📱 App Features

### Core Features
- ✅ Multi-chain wallet support
- ✅ Real-time balance checking
- ✅ Send/receive transactions
- ✅ QR code scanning
- ✅ Biometric authentication
- ✅ Secure key storage

### Social Features
- ✅ SocialFi content creation
- ✅ Community management
- ✅ Referral system
- ✅ Mining with streak bonuses
- ✅ GLW token rewards

### Trading Features
- ✅ P2P trading
- ✅ Token swapping
- ✅ Cross-chain bridging
- ✅ DApp browser
- ✅ Market data integration

## 🎨 UI/UX Features

### Liquid Glass Design
- 3D glassmorphism effects
- Neon blue/purple gradients
- Liquid animations
- Adaptive layouts
- Side-edge scrolling
- Interactive 3D transitions

## 🔧 Development

### Project Structure
```
app/src/main/java/com/globalwallet/app/
├── config/           # App configuration
├── data/
│   ├── api/         # API services
│   ├── blockchain/  # Web3 integration
│   ├── database/    # Room database
│   ├── model/       # Data models
│   ├── network/     # Network utilities
│   ├── repository/  # Data repositories
│   ├── security/    # Security utilities
│   └── wallet/      # Wallet management
├── di/              # Dependency injection
├── ui/
│   ├── components/  # Reusable UI components
│   ├── navigation/  # Navigation
│   ├── screens/     # App screens
│   ├── scanner/     # QR scanner
│   └── theme/       # UI theming
└── MainActivity.kt
```

### Key Dependencies
- **Jetpack Compose** - Modern UI toolkit
- **Hilt** - Dependency injection
- **Room** - Local database
- **Web3j** - Blockchain integration
- **Retrofit** - API networking
- **Biometric** - Authentication
- **ZXing** - QR code scanning

## 🚀 Production Deployment

### Before Release
1. Set `USE_TESTNET = false` in `AppConfig.kt`
2. Set `ENABLE_MOCK_DATA = false` in `AppConfig.kt`
3. Configure production Infura endpoints
4. Test all features thoroughly
5. Update app version in `build.gradle`

### Security Checklist
- [ ] Private keys are encrypted
- [ ] Biometric authentication enabled
- [ ] Network requests use HTTPS
- [ ] Sensitive data excluded from backups
- [ ] ProGuard rules configured
- [ ] API keys are secure

## 📞 Support

For issues or questions:
1. Check the troubleshooting section above
2. Review the error logs in Android Studio
3. Ensure your Infura API key is correctly configured
4. Verify network connectivity

## 📄 License

This project is for educational and demonstration purposes. Use at your own risk.

---

**⚠️ Important Security Note**: This is a demo application. For production use, implement additional security measures and thoroughly audit the code for vulnerabilities. 