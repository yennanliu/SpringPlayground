# Quick Start Guide - Chat App V2

Fast setup and testing guide for developers.

## 🚀 Quick Setup (5 minutes)

### 1. Start Backend
```bash
# Terminal 1
cd ../../backend/ChatAppV2
./mvnw spring-boot:run

# Wait for: "Started ChatAppV2Application"
```

### 2. Start Frontend
```bash
# Terminal 2
cd frontend/chatAppV2UI
npm install  # First time only
npm run dev

# Open: http://localhost:5173
```

### 3. Login & Test
1. Enter username (e.g., "alice")
2. Check "Connected" status (green)
3. Send a test message
4. Open another browser tab as "bob"
5. Chat between users!

## 🧪 Quick Test Commands

### Check If Services Are Running
```bash
npm run health
```

### Run All Tests
```bash
npm test                    # Unit tests (91 tests)
npm run test:api           # API integration tests
npm run test:integration   # Full integration suite
```

### Build & Deploy
```bash
npm run build              # Production build
npm run preview            # Test production build
```

## 📋 Quick Testing Checklist

### ✅ Essential Features (5 min)
- [ ] Login with username
- [ ] Send message
- [ ] Receive message (2nd browser)
- [ ] Create new channel
- [ ] Switch channels
- [ ] Edit own message
- [ ] Delete own message

### ✅ Profile Features (2 min)
- [ ] Click avatar in header
- [ ] Edit profile fields
- [ ] Save changes
- [ ] Verify persistence

### ✅ Real-time Features (3 min)
- [ ] Open 2 browsers
- [ ] Start typing in one
- [ ] See typing indicator in other
- [ ] Check online user list

## 🔧 Quick Troubleshooting

### WebSocket Not Connecting?
```bash
# Check backend WebSocket endpoint
curl -I http://localhost:8080/ws

# Restart backend
cd ../../backend/ChatAppV2
./mvnw spring-boot:run
```

### Tests Failing?
```bash
# Run health check first
npm run health

# Then run tests
npm test -- --run
```

### Port Already in Use?
```bash
# Kill process on port 5173
lsof -ti:5173 | xargs kill -9

# Kill process on port 8080
lsof -ti:8080 | xargs kill -9
```

## 📚 Documentation

- **README.md** - Complete project documentation
- **TESTING.md** - Unit testing guide
- **INTEGRATION_TESTING.md** - FE-BE integration testing
- **PHASE3_COMPLETE.md** - Feature documentation

## 🎯 Common Use Cases

### Testing a Feature
```bash
# 1. Start services
npm run health

# 2. Run unit tests
npm test

# 3. Manual test in browser
npm run dev
```

### Before Git Commit
```bash
# Run all checks
npm test -- --run
npm run build
npm run test:api
```

### Demo Preparation
```bash
# 1. Clean slate
rm -rf dist/
localStorage.clear()  # In browser console

# 2. Fresh build
npm install
npm run build

# 3. Start everything
# Terminal 1: Backend
# Terminal 2: npm run dev

# 4. Test all features
```

## 🌟 Pro Tips

1. **Use Incognito Mode** - Test as multiple users
2. **Watch Console** - Catch errors early
3. **Network Tab** - Debug WebSocket issues
4. **Vue DevTools** - Inspect component state
5. **Health Check** - Always start with `npm run health`

## 📞 Getting Help

- Check console for errors
- Run `npm run health`
- Review **INTEGRATION_TESTING.md**
- Check backend logs

---

**Total Setup Time:** ~5 minutes
**Test Time:** ~10 minutes
**Status:** Production Ready
