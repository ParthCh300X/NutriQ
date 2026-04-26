# 🟢 NutriQ — Scan. Analyze. Decide.

> “You’re not just eating food — you’re consuming consequences.”

NutriQ is a real-time **food intelligence scanner** that helps users make healthier decisions instantly by analyzing packaged food through barcode scanning.

---

## 🚀 What It Does

NutriQ transforms a simple scan into actionable insight:

- 📸 Scan any packaged food barcode  
- 🧠 Analyze ingredients using rule-based intelligence  
- ⚠️ Detect hidden sugars, processed fats, additives  
- 🎯 Classify food into:
  - 🟢 SAFE  
  - 🟡 MODERATE  
  - 🔴 RISKY  
- 📊 Show reasons behind classification  
- 🧾 Maintain scan history (deduplicated & ordered)

---

## 🧠 Core Idea

Most people don’t read labels.

NutriQ solves this by answering one question instantly:

> **“What will this food do to me?”**

---

## 📱 Features Implemented

### 🔍 Scanner System
- CameraX powered live barcode scanning  
- ML Kit barcode detection  
- Single-scan locking (prevents spam scans)  
- Visual feedback (toast + green flash)  
- Smooth transition → result screen  

---

### 🧪 Food Analysis Engine
- Ingredient parsing  
- Detection of:
  - Added sugars (syrup, maltodextrin)
  - Processed fats (palm oil, vegetable fat)
  - Additives (flavours, emulsifiers)
- Rule-based risk classification  

---

### 🎯 Result Screen
- Clean, centered UI  
- Risk badge (SAFE / MODERATE / RISKY)  
- Highlighted harmful ingredients  
- Reason-based explanation  
- Scrollable layout for long ingredient lists  

---

### 📚 History System
- Stored using Room Database  
- Deduplicated entries (same product updates, not duplicates)  
- Sorted by latest scan  
- Clickable cards → reopen result view  
- Visual indicators (color-coded risk dots)

---

### 🎨 UI/UX Enhancements
- Bottom navigation (Home / Scanner / History)  
- Animated transitions (scanner → result)  
- Scan overlay (focus frame + scan line)  
- Premium card-based layout  
- Empty states for better UX  

---

## 🧱 Tech Stack

| Layer        | Technology |
|-------------|-----------|
| UI          | Jetpack Compose |
| Architecture| MVVM |
| DI          | Hilt |
| Database    | Room |
| Networking  | Retrofit |
| Async       | Coroutines + Flow |
| Camera      | CameraX |
| ML          | ML Kit (Barcode Scanning) |

---

## 📂 Project Structure
