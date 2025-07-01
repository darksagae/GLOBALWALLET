<<<<<<< HEAD
# GLOBALWALLET

A futuristic, secure, and gamified multi-chain cryptocurrency wallet for Android, built with Jetpack Compose and a 3D Liquid Glass UI.

## Features
- Non-custodial wallet with 12-word mnemonic (BIP39)
- Multi-chain support: Ethereum, Base, BSC (BEP-20), Polygon, Avalanche, Binance Chain (BEP-2, mock), Solana (mock)
- 3D Liquid Glass UI: glassmorphism, neon gradients, liquid/3D animations
- Secure storage (EncryptedSharedPreferences, biometric/passkey)
- Mining, streaks, and gamified GLW rewards
- Referrals, SocialFi, tasks, and leaderboard
- P2P trading, swap, bridge, send/receive (mocked)
- DApp browser, CoinGecko API integration, trending tokens, news
- Settings: security, theme, export/import, badges

## Setup
1. **Clone the repo:**
   ```sh
   git clone <your-repo-url>
   cd globalWallet
   ```
2. **Open in Android Studio.**
3. **Add your Infura key** (for Ethereum) in `WalletManager.kt`.
4. **Build & run** on an emulator or device (minSdk 24).

## Dependencies
- Jetpack Compose 1.x+
- web3j
- bip39 (novacrypto)
- Room, Retrofit, OkHttp
- Lottie, compose-3d, Coil
- AndroidX Security, Biometric
- ZXing (QR/barcode)

## Notes
- BEP-2 and Solana are mocked for demo.
- All sensitive data is encrypted on-device.
- For demo, backend features (P2P, SocialFi, referrals) use local data.

## License
MIT
=======

>>>>>>> a750fed852cd22f2f9d2dd99dac6a59ff9e5283c
