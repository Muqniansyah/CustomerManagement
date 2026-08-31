package com.mycompany.customermanagement.model;

// Model = representasi data pelanggan
public class Customer {

    // Data yang dimiliki setiap pelanggan
    private int id;
    private String email;
    private String name;
    private String phone;
    private String address;
    private String category;
    private String status;
    private String notes;

    // Constructor kosong
    public Customer() {
    }

    // Constructor untuk membuat Customer sekaligus dengan data
    public Customer(int id, String name, String phone, String address,
                    String category, String status, String notes) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.category = category;
        this.status = status;
        this.notes = notes;
    }

    // Getter & Setter untuk mengakses dan mengubah data
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getEmail() { 
        return email; 
    }
    
    public void setEmail(String email) { 
        this.email = email; 
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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