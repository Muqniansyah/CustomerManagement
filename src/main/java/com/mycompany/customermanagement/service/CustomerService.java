/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.customermanagement.service;

import com.mycompany.customermanagement.dao.CustomerDAO;
import com.mycompany.customermanagement.model.Customer;
import java.util.List;

// services = tempat logika aplikasi. -> menangani CustomerDAO (jembatan controller dan DAO)
public class CustomerService {
    private final CustomerDAO customerDAO = new CustomerDAO();
    
    public List<Customer> getAll() {
        return customerDAO.getAll();
    }

    public List<Customer> search(String keyword) {
        return customerDAO.search(keyword);
    }
    
    // fungsi filter status aktif dan tidak aktif   
    public List<Customer> filterByStatus(String status) {
        return customerDAO.filterByStatus(status);
    }

    public void delete(int id) {
        customerDAO.delete(id);
    }
    
    // dipanggil dari CustomerFormController waktu mode "Tambah"
    public void save(Customer customer) {
        customerDAO.save(customer);
    }
 
    // dipanggil dari CustomerFormController waktu mode "Edit"
    public void update(Customer customer) {
        customerDAO.update(customer);
    }
}
