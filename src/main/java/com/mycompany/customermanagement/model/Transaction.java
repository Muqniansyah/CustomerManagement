package com.mycompany.customermanagement.model;

// Model = representasi data transaksi pelanggan
public class Transaction {

    // Data yang dimiliki setiap transaksi
    private int id;
    private int customerId;
    private String transactionDate;
    private double totalAmount;
    private String paymentStatus;
    private String paymentProof;
    private String notes;

    // Constructor kosong
    public Transaction() {
    }

    // Constructor untuk membuat transaksi sekaligus dengan data
    public Transaction(int id, int customerId, String transactionDate,
                       double totalAmount, String paymentStatus,
                       String paymentProof, String notes) {
        this.id = id;
        this.customerId = customerId;
        this.transactionDate = transactionDate;
        this.totalAmount = totalAmount;
        this.paymentStatus = paymentStatus;
        this.paymentProof = paymentProof;
        this.notes = notes;
    }

    // Getter & Setter untuk mengakses dan mengubah data

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentProof() {
        return paymentProof;
    }

    public void setPaymentProof(String paymentProof) {
        this.paymentProof = paymentProof;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}