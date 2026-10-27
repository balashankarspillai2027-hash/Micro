# ✂️ Saloon Appointment Book - Android Micro Project

An offline, SQLite-powered Android application built in **Java** to book, schedule, and manage salon customer appointments effortlessly. Designed for salons, barbershops, and beauty parlors to streamline daily booking management, customer contact, and appointment tracking.

---

## 📌 Project Overview
- **Project Title:** Saloon Appointment Book
- **Platform:** Android
- **Language:** Java (JDK 17 / Java 8 compatible)
- **Database:** SQLite (`SQLiteOpenHelper`)
- **Architecture:** Native Android MVC / Helper-Model-Adapter Pattern
- **UI Framework:** Material Components for Android (Material Design)

---

## 🚀 Key Features

1. **📊 Real-time Dashboard & Statistics:**
   - **Total Bookings Counter**
   - **Today's Bookings Counter** (automatically calculated using current date)
   - **Completed Bookings Counter**

2. **📅 Comprehensive Booking Form (`AddEditAppointmentActivity`):**
   - **Strict Input Validations**:
     - **Customer Name**: Enforces alphabetical characters, periods, apostrophes, and 2–50 character length.
     - **Phone Number**: Strips formatting symbols and enforces valid 10–13 digit mobile numbers.
     - **Price**: Requires non-empty, non-negative numbers up to ₹1,00,000.
     - **Time & Date Guardrails**: Disallows selecting past hours on today's date.
     - **Stylist Conflict Detection**: Warns if a stylist already has an active appointment in that time slot.
   - Pre-configured salon services menu with automated price calculation:
     - Haircut & Styling (₹250)
     - Beard Trim & Shave (₹150)
     - Hair Color & Highlights (₹700)
     - Facial & Skin Glow (₹800)
     - Head Massage & Oil Therapy (₹300)
     - Hair Spa & Keratin (₹1200)
     - Royal Grooming Package (₹1500)
     - Custom Service (editable price)
   - Stylist / Barber assignment.
   - **Interactive Date Picker:** Calendar dialog with past-date blocking.
   - **Interactive Time Picker:** 12-hour AM/PM time selector.
   - Special notes / instructions field.

3. **🔍 Search & Multi-Filter:**
   - **Instant Search:** Search in real-time by customer name, phone number, or service name.
   - **Status Filter Chips:** Filter bookings quickly by `All`, `Booked`, `Completed`, or `Cancelled`.

4. **⚡ Quick Actions & Status Lifecycle:**
   - **One-tap Call Customer:** Direct integration with Android Dialer (`tel:`) to contact customers instantly.
   - **Status Transitions:** Change status between `Booked` ➔ `Completed` ➔ `Cancelled` from card menu.
   - **Full Edit:** Re-open any booking to modify time, date, barber, or notes.
   - **Delete:** Remove booking with confirmation alert dialog.

5. **💾 Offline SQLite Storage:**
   - 100% offline database; persists data permanently on the device across app restarts.
   - Auto-seeds realistic sample bookings on first launch for instant demonstration.

---

## 🗄️ Database Architecture (SQLite)

**Database Name:** `SaloonAppointments.db`  
**Table:** `appointments`

| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | `INTEGER` | `PRIMARY KEY AUTOINCREMENT` | Unique booking identifier |
| `customer_name` | `TEXT` | `NOT NULL` | Customer's full name |
| `customer_phone` | `TEXT` | `NOT NULL` | Customer's phone number |
| `service_name` | `TEXT` | `NOT NULL` | Selected salon service |
| `service_price` | `REAL` | | Price in ₹ |
| `appointment_date` | `TEXT` | `NOT NULL` | Date (e.g. `27 Oct 2026`) |
| `appointment_time` | `TEXT` | `NOT NULL` | Time (e.g. `10:30 AM`) |
| `stylist_name` | `TEXT` | | Assigned stylist/barber |
| `status` | `TEXT` | `DEFAULT 'Booked'` | `Booked` \| `Completed` \| `Cancelled` |
| `notes` | `TEXT` | | Optional notes / requests |

---

## 📁 Project Structure

```text
SaloonAppointmentBook/
├── app/
│   ├── build.gradle.kts
│   ├── proguard-rules.pro
│   └── src/
│       └── main/
│           ├── AndroidManifest.xml
│           ├── java/com/saloon/appointmentbook/
│           │   ├── Appointment.java              # Model class (Data Entity)
│           │   ├── DatabaseHelper.java           # SQLite database CRUD operations
│           │   ├── AppointmentAdapter.java       # RecyclerView Adapter & Card bindings
│           │   ├── MainActivity.java             # Dashboard, Search & Appointments List
│           │   └── AddEditAppointmentActivity.java # Booking & Edit Form with Pickers
│           └── res/
│               ├── drawable/                     # Vector icons & status badge backgrounds
│               ├── layout/
│               │   ├── activity_main.xml         # Dashboard layout with stats & list
│               │   ├── activity_add_edit_appointment.xml # Form layout
│               │   └── item_appointment.xml     # Material CardView item layout
│               ├── menu/
│               │   └── menu_appointment_item.xml # Card popup options (Edit, Status, Delete)
│               └── values/
│                   ├── colors.xml                # Modern Salon theme color palette
│                   ├── strings.xml               # App strings, services & stylists arrays
│                   └── themes.xml                # MaterialComponents NoActionBar theme
├── gradle/
│   ├── libs.versions.toml
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── build.gradle.kts
├── settings.gradle.kts
└── gradle.properties
```

---

## 💻 How to Open and Run in Android Studio

1. **Launch Android Studio**.
2. Select **Open** (or **File > Open**).
3. Browse and select the project folder:  
   `E:\SaloonAppointmentBook`
4. Wait for Gradle Sync to finish automatically.
5. Connect your Android device via USB debugging or start an Android Virtual Device (AVD / Emulator).
6. Click the green **Run (▶)** button or press `Shift + F10`.
