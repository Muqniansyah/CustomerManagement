package com.mycompany.customermanagement.model;

// Model = representasi data interaksi pelanggan
public class Interaction {

    // Data yang dimiliki setiap interaksi
    private int id;
    private int customerId;
    private String interactionDate;
    private String type;
    private String description;
    private String notes;
    
    // Field TAMBAHAN -- sama seperti di Transaction.java, cuma titipan nama
    // pelanggan untuk ditampilkan, diisi lewat JOIN query di InteractionDAO
    private String customerName;

    // Constructor kosong
    public Interaction() {
    }

    // Constructor untuk membuat Interaction sekaligus dengan data
    public Interaction(int id, int customerId, String interactionDate,
                       String type, String description) {
        this.id = id;
        this.customerId = customerId;
        this.interactionDate = interactionDate;
        this.type = type;
        this.description = description;
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

    public String getInteractionDate() {
        return interactionDate;
    }

    public void setInteractionDate(String interactionDate) {
        this.interactionDate = interactionDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getNotes() { 
        return notes; 
    }
    
    public void setNotes(String notes) { 
        this.notes = notes; 
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}