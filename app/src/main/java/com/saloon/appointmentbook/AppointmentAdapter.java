package com.saloon.appointmentbook;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> {

    public interface OnAppointmentActionListener {
        void onEdit(Appointment appointment);
        void onStatusChange(Appointment appointment, String newStatus);
        void onDelete(Appointment appointment);
    }

    private final Context context;
    private List<Appointment> appointmentList;
    private final OnAppointmentActionListener listener;

    public AppointmentAdapter(Context context, List<Appointment> appointmentList, OnAppointmentActionListener listener) {
        this.context = context;
        this.appointmentList = appointmentList;
        this.listener = listener;
    }

    public void updateList(List<Appointment> newList) {
        this.appointmentList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_appointment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Appointment appointment = appointmentList.get(position);

        holder.tvCustomerName.setText(appointment.getCustomerName());
        holder.tvCustomerPhone.setText(appointment.getCustomerPhone());
        holder.tvServiceName.setText(appointment.getServiceName());
        holder.tvServicePrice.setText(String.format("₹%.0f", appointment.getServicePrice()));
        holder.tvAppointmentDate.setText(appointment.getAppointmentDate());
        holder.tvAppointmentTime.setText(appointment.getAppointmentTime());

        String stylist = appointment.getStylistName();
        if (stylist != null && !stylist.trim().isEmpty()) {
            holder.tvStylistName.setVisibility(View.VISIBLE);
            holder.tvStylistName.setText("Stylist: " + stylist);
        } else {
            holder.tvStylistName.setVisibility(View.GONE);
        }

        // Status badge styling
        String status = appointment.getStatus();
        if (status == null) status = "Booked";
        holder.tvStatusBadge.setText(status);

        if ("Completed".equalsIgnoreCase(status)) {
            holder.tvStatusBadge.setBackgroundResource(R.drawable.badge_status_completed);
            holder.tvStatusBadge.setTextColor(ContextCompat.getColor(context, R.color.status_completed));
        } else if ("Cancelled".equalsIgnoreCase(status)) {
            holder.tvStatusBadge.setBackgroundResource(R.drawable.badge_status_cancelled);
            holder.tvStatusBadge.setTextColor(ContextCompat.getColor(context, R.color.status_cancelled));
        } else {
            holder.tvStatusBadge.setBackgroundResource(R.drawable.badge_status_booked);
            holder.tvStatusBadge.setTextColor(ContextCompat.getColor(context, R.color.status_booked));
        }

        // Quick Call Customer
        holder.btnCallCustomer.setOnClickListener(v -> {
            String phone = appointment.getCustomerPhone();
            if (phone != null && !phone.trim().isEmpty()) {
                Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone.trim()));
                context.startActivity(dialIntent);
            } else {
                Toast.makeText(context, "No phone number available", Toast.LENGTH_SHORT).show();
            }
        });

        // More Options Menu
        holder.btnMoreOptions.setOnClickListener(v -> showPopupMenu(v, appointment));

        // Card Click opens Edit
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEdit(appointment);
            }
        });
    }

    private void showPopupMenu(View view, Appointment appointment) {
        PopupMenu popup = new PopupMenu(context, view);
        popup.inflate(R.menu.menu_appointment_item);

        popup.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.action_mark_completed) {
                if (listener != null) listener.onStatusChange(appointment, "Completed");
                return true;
            } else if (itemId == R.id.action_edit) {
                if (listener != null) listener.onEdit(appointment);
                return true;
            } else if (itemId == R.id.action_mark_cancelled) {
                if (listener != null) listener.onStatusChange(appointment, "Cancelled");
                return true;
            } else if (itemId == R.id.action_delete) {
                if (listener != null) listener.onDelete(appointment);
                return true;
            }
            return false;
        });
        popup.show();
    }

    @Override
    public int getItemCount() {
        return appointmentList != null ? appointmentList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCustomerName, tvCustomerPhone, tvServiceName, tvServicePrice;
        TextView tvAppointmentDate, tvAppointmentTime, tvStylistName, tvStatusBadge;
        ImageView btnCallCustomer, btnMoreOptions;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            tvCustomerPhone = itemView.findViewById(R.id.tvCustomerPhone);
            tvServiceName = itemView.findViewById(R.id.tvServiceName);
            tvServicePrice = itemView.findViewById(R.id.tvServicePrice);
            tvAppointmentDate = itemView.findViewById(R.id.tvAppointmentDate);
            tvAppointmentTime = itemView.findViewById(R.id.tvAppointmentTime);
            tvStylistName = itemView.findViewById(R.id.tvStylistName);
            tvStatusBadge = itemView.findViewById(R.id.tvStatusBadge);
            btnCallCustomer = itemView.findViewById(R.id.btnCallCustomer);
            btnMoreOptions = itemView.findViewById(R.id.btnMoreOptions);
        }
    }
}
