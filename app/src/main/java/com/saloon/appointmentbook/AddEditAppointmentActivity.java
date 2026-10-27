package com.saloon.appointmentbook;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddEditAppointmentActivity extends AppCompatActivity {

    private TextInputEditText etCustomerName, etCustomerPhone, etServicePrice, etNotes;
    private Spinner spinnerService, spinnerStylist, spinnerStatus;
    private TextView tvSelectedDate, tvSelectedTime, tvFormTitle;
    private CardView cardSelectDate, cardSelectTime;
    private Button btnSaveAppointment, btnCancelForm;
    private ImageView btnBack;

    private DatabaseHelper dbHelper;
    private Appointment editingAppointment;
    private boolean isEditMode = false;

    private final Calendar calendar = Calendar.getInstance();
    private String selectedDate = "";
    private String selectedTime = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_appointment);

        dbHelper = new DatabaseHelper(this);

        initViews();
        setupSpinners();
        setupDateTimePickers();

        // Check if editing an existing appointment
        if (getIntent().hasExtra("EXTRA_APPOINTMENT")) {
            editingAppointment = (Appointment) getIntent().getSerializableExtra("EXTRA_APPOINTMENT");
            if (editingAppointment != null) {
                isEditMode = true;
                populateExistingData();
            }
        }

        if (!isEditMode) {
            // Default date to today & time to upcoming hour
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            selectedDate = dateFormat.format(calendar.getTime());
            tvSelectedDate.setText(selectedDate);

            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            selectedTime = timeFormat.format(calendar.getTime());
            tvSelectedTime.setText(selectedTime);
        }

        btnSaveAppointment.setOnClickListener(v -> saveAppointment());
        btnCancelForm.setOnClickListener(v -> finish());
    }

    private void initViews() {
        tvFormTitle = findViewById(R.id.tvFormTitle);
        etCustomerName = findViewById(R.id.etCustomerName);
        etCustomerPhone = findViewById(R.id.etCustomerPhone);
        etServicePrice = findViewById(R.id.etServicePrice);
        etNotes = findViewById(R.id.etNotes);

        spinnerService = findViewById(R.id.spinnerService);
        spinnerStylist = findViewById(R.id.spinnerStylist);
        spinnerStatus = findViewById(R.id.spinnerStatus);

        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        tvSelectedTime = findViewById(R.id.tvSelectedTime);
        cardSelectDate = findViewById(R.id.cardSelectDate);
        cardSelectTime = findViewById(R.id.cardSelectTime);

        btnSaveAppointment = findViewById(R.id.btnSaveAppointment);
        btnCancelForm = findViewById(R.id.btnCancelForm);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());
    }

    private void setupSpinners() {
        // Services spinner
        ArrayAdapter<CharSequence> serviceAdapter = ArrayAdapter.createFromResource(this,
                R.array.services_array, android.R.layout.simple_spinner_item);
        serviceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerService.setAdapter(serviceAdapter);

        spinnerService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!isEditMode || etServicePrice.getText() == null || etServicePrice.getText().toString().isEmpty()) {
                    double defaultPrice = getPriceForServicePosition(position);
                    if (defaultPrice > 0) {
                        etServicePrice.setText(String.format(Locale.getDefault(), "%.0f", defaultPrice));
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        // Stylists spinner
        ArrayAdapter<CharSequence> stylistAdapter = ArrayAdapter.createFromResource(this,
                R.array.stylists_array, android.R.layout.simple_spinner_item);
        stylistAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStylist.setAdapter(stylistAdapter);

        // Status spinner
        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(this,
                R.array.status_array, android.R.layout.simple_spinner_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(statusAdapter);
    }

    private double getPriceForServicePosition(int position) {
        switch (position) {
            case 0: return 250.0;  // Haircut & Styling
            case 1: return 150.0;  // Beard Trim & Shave
            case 2: return 700.0;  // Hair Color & Highlights
            case 3: return 800.0;  // Facial & Skin Glow
            case 4: return 300.0;  // Head Massage & Oil Therapy
            case 5: return 1200.0; // Hair Spa & Keratin
            case 6: return 1500.0; // Royal Grooming Package
            default: return 0.0;
        }
    }

    private void setupDateTimePickers() {
        cardSelectDate.setOnClickListener(v -> {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        Calendar chosenCal = Calendar.getInstance();
                        chosenCal.set(selectedYear, selectedMonth, selectedDay);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                        selectedDate = sdf.format(chosenCal.getTime());
                        tvSelectedDate.setText(selectedDate);
                    }, year, month, day);

            // Restrict selecting past dates
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.show();
        });

        cardSelectTime.setOnClickListener(v -> {
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    (view, hourOfDay, minuteOfHour) -> {
                        Calendar chosenTime = Calendar.getInstance();
                        chosenTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        chosenTime.set(Calendar.MINUTE, minuteOfHour);
                        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                        selectedTime = sdf.format(chosenTime.getTime());
                        tvSelectedTime.setText(selectedTime);
                    }, hour, minute, false);
            timePickerDialog.show();
        });
    }

    private void populateExistingData() {
        tvFormTitle.setText(R.string.edit_appointment);
        btnSaveAppointment.setText("Update Appointment");

        etCustomerName.setText(editingAppointment.getCustomerName());
        etCustomerPhone.setText(editingAppointment.getCustomerPhone());
        etServicePrice.setText(String.format(Locale.getDefault(), "%.0f", editingAppointment.getServicePrice()));
        etNotes.setText(editingAppointment.getNotes());

        selectedDate = editingAppointment.getAppointmentDate();
        tvSelectedDate.setText(selectedDate);

        selectedTime = editingAppointment.getAppointmentTime();
        tvSelectedTime.setText(selectedTime);

        // Match service spinner
        String service = editingAppointment.getServiceName();
        ArrayAdapter adapter = (ArrayAdapter) spinnerService.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).toString().toLowerCase().contains(service.toLowerCase())) {
                spinnerService.setSelection(i);
                break;
            }
        }

        // Match stylist spinner
        String stylist = editingAppointment.getStylistName();
        if (stylist != null) {
            ArrayAdapter stylistAdp = (ArrayAdapter) spinnerStylist.getAdapter();
            for (int i = 0; i < stylistAdp.getCount(); i++) {
                if (stylistAdp.getItem(i).toString().equalsIgnoreCase(stylist)) {
                    spinnerStylist.setSelection(i);
                    break;
                }
            }
        }

        // Match status spinner
        String status = editingAppointment.getStatus();
        if (status != null) {
            ArrayAdapter statusAdp = (ArrayAdapter) spinnerStatus.getAdapter();
            for (int i = 0; i < statusAdp.getCount(); i++) {
                if (statusAdp.getItem(i).toString().equalsIgnoreCase(status)) {
                    spinnerStatus.setSelection(i);
                    break;
                }
            }
        }
    }

    private void saveAppointment() {
        String name = etCustomerName.getText() != null ? etCustomerName.getText().toString().trim() : "";
        String phone = etCustomerPhone.getText() != null ? etCustomerPhone.getText().toString().trim() : "";
        String priceStr = etServicePrice.getText() != null ? etServicePrice.getText().toString().trim() : "";
        String notes = etNotes.getText() != null ? etNotes.getText().toString().trim() : "";

        if (name.isEmpty()) {
            etCustomerName.setError("Customer name is required");
            etCustomerName.requestFocus();
            return;
        }

        if (phone.isEmpty()) {
            etCustomerPhone.setError("Phone number is required");
            etCustomerPhone.requestFocus();
            return;
        }

        double price = 0.0;
        if (!priceStr.isEmpty()) {
            try {
                price = Double.parseDouble(priceStr);
            } catch (NumberFormatException e) {
                etServicePrice.setError("Invalid price");
                return;
            }
        }

        if (selectedDate.isEmpty() || selectedDate.equalsIgnoreCase("Select Date")) {
            Toast.makeText(this, "Please select an appointment date", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedTime.isEmpty() || selectedTime.equalsIgnoreCase("Select Time")) {
            Toast.makeText(this, "Please select an appointment time", Toast.LENGTH_SHORT).show();
            return;
        }

        String rawService = spinnerService.getSelectedItem().toString();
        // Extract clean service name without price tag if needed
        String serviceName = rawService.contains(" (") ? rawService.substring(0, rawService.indexOf(" (")) : rawService;
        String stylistName = spinnerStylist.getSelectedItem().toString();
        String status = spinnerStatus.getSelectedItem().toString();

        if (isEditMode && editingAppointment != null) {
            editingAppointment.setCustomerName(name);
            editingAppointment.setCustomerPhone(phone);
            editingAppointment.setServiceName(serviceName);
            editingAppointment.setServicePrice(price);
            editingAppointment.setAppointmentDate(selectedDate);
            editingAppointment.setAppointmentTime(selectedTime);
            editingAppointment.setStylistName(stylistName);
            editingAppointment.setStatus(status);
            editingAppointment.setNotes(notes);

            int updated = dbHelper.updateAppointment(editingAppointment);
            if (updated > 0) {
                Toast.makeText(this, "Appointment updated successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to update appointment", Toast.LENGTH_SHORT).show();
            }
        } else {
            Appointment newAppointment = new Appointment(name, phone, serviceName, price,
                    selectedDate, selectedTime, stylistName, status, notes);

            long insertedId = dbHelper.insertAppointment(newAppointment);
            if (insertedId != -1) {
                Toast.makeText(this, "Appointment booked successfully!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to book appointment", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
