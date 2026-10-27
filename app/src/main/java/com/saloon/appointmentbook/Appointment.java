package com.saloon.appointmentbook;

import java.io.Serializable;

public class Appointment implements Serializable {
    private int id;
    private String customerName;
    private String customerPhone;
    private String serviceName;
    private double servicePrice;
    private String appointmentDate;
    private String appointmentTime;
    private String stylistName;
    private String status; // "Booked", "Completed", "Cancelled"
    private String notes;

    public Appointment() {
    }

    public Appointment(int id, String customerName, String customerPhone, String serviceName,
                       double servicePrice, String appointmentDate, String appointmentTime,
                       String stylistName, String status, String notes) {
        this.id = id;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.serviceName = serviceName;
        this.servicePrice = servicePrice;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.stylistName = stylistName;
        this.status = status;
        this.notes = notes;
    }

    public Appointment(String customerName, String customerPhone, String serviceName,
                       double servicePrice, String appointmentDate, String appointmentTime,
                       String stylistName, String status, String notes) {
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.serviceName = serviceName;
        this.servicePrice = servicePrice;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.stylistName = stylistName;
        this.status = status;
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public double getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(double servicePrice) {
        this.servicePrice = servicePrice;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getStylistName() {
        return stylistName;
    }

    public void setStylistName(String stylistName) {
        this.stylistName = stylistName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
