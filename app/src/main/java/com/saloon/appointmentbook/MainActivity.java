package com.saloon.appointmentbook;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements AppointmentAdapter.OnAppointmentActionListener {

    private DatabaseHelper dbHelper;
    private RecyclerView recyclerView;
    private AppointmentAdapter adapter;
    private LinearLayout layoutEmptyState;
    private TextView tvTotalCount, tvTodayCount, tvCompletedCount;
    private EditText etSearch;
    private ChipGroup chipGroupFilter;

    private String currentFilterStatus = "All";
    private String currentSearchQuery = "";
    private String todayDateStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        todayDateStr = sdf.format(new Date());

        // Seed initial mock data if first run
        dbHelper.seedInitialDataIfEmpty(todayDateStr);

        initViews();
        setupRecyclerView();
        setupListeners();
        loadAppointments();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAppointments();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerViewAppointments);
        layoutEmptyState = findViewById(R.id.layoutEmptyState);
        tvTotalCount = findViewById(R.id.tvTotalCount);
        tvTodayCount = findViewById(R.id.tvTodayCount);
        tvCompletedCount = findViewById(R.id.tvCompletedCount);
        etSearch = findViewById(R.id.etSearch);
        chipGroupFilter = findViewById(R.id.chipGroupFilter);
        ExtendedFloatingActionButton fabAddAppointment = findViewById(R.id.fabAddAppointment);

        fabAddAppointment.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditAppointmentActivity.class);
            startActivity(intent);
        });
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AppointmentAdapter(this, null, this);
        recyclerView.setAdapter(adapter);
    }

    private void setupListeners() {
        // Search filter listener
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentSearchQuery = s.toString();
                loadAppointments();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        // Chip group filter
        chipGroupFilter.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.chipBooked) {
                currentFilterStatus = "Booked";
            } else if (checkedId == R.id.chipCompleted) {
                currentFilterStatus = "Completed";
            } else if (checkedId == R.id.chipCancelled) {
                currentFilterStatus = "Cancelled";
            } else {
                currentFilterStatus = "All";
            }
            loadAppointments();
        });
    }

    private void loadAppointments() {
        List<Appointment> list = dbHelper.getFilteredAppointments(currentSearchQuery, currentFilterStatus);
        adapter.updateList(list);

        if (list.isEmpty()) {
            layoutEmptyState.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            layoutEmptyState.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        // Update Stats
        tvTotalCount.setText(String.valueOf(dbHelper.getTotalCount()));
        tvTodayCount.setText(String.valueOf(dbHelper.getTodayCount(todayDateStr)));
        tvCompletedCount.setText(String.valueOf(dbHelper.getCompletedCount()));
    }

    @Override
    public void onEdit(Appointment appointment) {
        Intent intent = new Intent(MainActivity.this, AddEditAppointmentActivity.class);
        intent.putExtra("EXTRA_APPOINTMENT", appointment);
        startActivity(intent);
    }

    @Override
    public void onStatusChange(Appointment appointment, String newStatus) {
        int updated = dbHelper.updateStatus(appointment.getId(), newStatus);
        if (updated > 0) {
            Toast.makeText(this, "Status updated to " + newStatus, Toast.LENGTH_SHORT).show();
            loadAppointments();
        } else {
            Toast.makeText(this, "Failed to update status", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDelete(Appointment appointment) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Appointment")
                .setMessage("Are you sure you want to delete the booking for " + appointment.getCustomerName() + "?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    int deleted = dbHelper.deleteAppointment(appointment.getId());
                    if (deleted > 0) {
                        Toast.makeText(MainActivity.this, "Appointment deleted", Toast.LENGTH_SHORT).show();
                        loadAppointments();
                    } else {
                        Toast.makeText(MainActivity.this, "Failed to delete", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
