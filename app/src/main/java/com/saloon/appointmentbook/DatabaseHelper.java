package com.saloon.appointmentbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SaloonAppointments.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "appointments";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "customer_name";
    public static final String COLUMN_PHONE = "customer_phone";
    public static final String COLUMN_SERVICE = "service_name";
    public static final String COLUMN_PRICE = "service_price";
    public static final String COLUMN_DATE = "appointment_date";
    public static final String COLUMN_TIME = "appointment_time";
    public static final String COLUMN_STYLIST = "stylist_name";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_NOTES = "notes";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT NOT NULL, "
                + COLUMN_PHONE + " TEXT NOT NULL, "
                + COLUMN_SERVICE + " TEXT NOT NULL, "
                + COLUMN_PRICE + " REAL, "
                + COLUMN_DATE + " TEXT NOT NULL, "
                + COLUMN_TIME + " TEXT NOT NULL, "
                + COLUMN_STYLIST + " TEXT, "
                + COLUMN_STATUS + " TEXT DEFAULT 'Booked', "
                + COLUMN_NOTES + " TEXT"
                + ")";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long insertAppointment(Appointment appointment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, appointment.getCustomerName());
        values.put(COLUMN_PHONE, appointment.getCustomerPhone());
        values.put(COLUMN_SERVICE, appointment.getServiceName());
        values.put(COLUMN_PRICE, appointment.getServicePrice());
        values.put(COLUMN_DATE, appointment.getAppointmentDate());
        values.put(COLUMN_TIME, appointment.getAppointmentTime());
        values.put(COLUMN_STYLIST, appointment.getStylistName());
        values.put(COLUMN_STATUS, appointment.getStatus());
        values.put(COLUMN_NOTES, appointment.getNotes());

        return db.insert(TABLE_NAME, null, values);
    }

    public int updateAppointment(Appointment appointment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, appointment.getCustomerName());
        values.put(COLUMN_PHONE, appointment.getCustomerPhone());
        values.put(COLUMN_SERVICE, appointment.getServiceName());
        values.put(COLUMN_PRICE, appointment.getServicePrice());
        values.put(COLUMN_DATE, appointment.getAppointmentDate());
        values.put(COLUMN_TIME, appointment.getAppointmentTime());
        values.put(COLUMN_STYLIST, appointment.getStylistName());
        values.put(COLUMN_STATUS, appointment.getStatus());
        values.put(COLUMN_NOTES, appointment.getNotes());

        return db.update(TABLE_NAME, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(appointment.getId())});
    }

    public int updateStatus(int id, String newStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STATUS, newStatus);
        return db.update(TABLE_NAME, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    public int deleteAppointment(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    public List<Appointment> getFilteredAppointments(String query, String statusFilter) {
        List<Appointment> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        StringBuilder sql = new StringBuilder("SELECT * FROM " + TABLE_NAME + " WHERE 1=1");
        List<String> args = new ArrayList<>();

        if (statusFilter != null && !statusFilter.equalsIgnoreCase("All")) {
            sql.append(" AND " + COLUMN_STATUS + " = ?");
            args.add(statusFilter);
        }

        if (query != null && !query.trim().isEmpty()) {
            sql.append(" AND (" + COLUMN_NAME + " LIKE ? OR "
                    + COLUMN_PHONE + " LIKE ? OR "
                    + COLUMN_SERVICE + " LIKE ?)");
            String searchPattern = "%" + query.trim() + "%";
            args.add(searchPattern);
            args.add(searchPattern);
            args.add(searchPattern);
        }

        sql.append(" ORDER BY " + COLUMN_DATE + " DESC, " + COLUMN_ID + " DESC");

        Cursor cursor = db.rawQuery(sql.toString(), args.toArray(new String[0]));
        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    Appointment a = new Appointment();
                    a.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                    a.setCustomerName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
                    a.setCustomerPhone(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)));
                    a.setServiceName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SERVICE)));
                    a.setServicePrice(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE)));
                    a.setAppointmentDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)));
                    a.setAppointmentTime(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME)));
                    a.setStylistName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STYLIST)));
                    a.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS)));
                    a.setNotes(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTES)));
                    list.add(a);
                }
            } finally {
                cursor.close();
            }
        }
        return list;
    }

    public int getTotalCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);
        int count = 0;
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    count = cursor.getInt(0);
                }
            } finally {
                cursor.close();
            }
        }
        return count;
    }

    public int getTodayCount(String todayDate) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE " + COLUMN_DATE + " = ?",
                new String[]{todayDate});
        int count = 0;
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    count = cursor.getInt(0);
                }
            } finally {
                cursor.close();
            }
        }
        return count;
    }

    public int getCompletedCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE " + COLUMN_STATUS + " = 'Completed'", null);
        int count = 0;
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    count = cursor.getInt(0);
                }
            } finally {
                cursor.close();
            }
        }
        return count;
    }

    public void seedInitialDataIfEmpty(String todayDate) {
        if (getTotalCount() == 0) {
            insertAppointment(new Appointment("Rajesh Kumar", "+91 98765 43210", "Haircut & Styling", 250.0, todayDate, "10:30 AM", "Rahul (Master Barber)", "Booked", "Prefers short sides"));
            insertAppointment(new Appointment("Amit Sharma", "+91 98123 45678", "Beard Trim & Shave", 150.0, todayDate, "11:15 AM", "Aman (Grooming Expert)", "Booked", "Clean shave"));
            insertAppointment(new Appointment("Suresh Verma", "+91 97654 32109", "Royal Grooming Package", 1500.0, todayDate, "02:00 PM", "Priya (Hair & Color Specialist)", "Completed", "Wedding prep"));
        }
    }
}
