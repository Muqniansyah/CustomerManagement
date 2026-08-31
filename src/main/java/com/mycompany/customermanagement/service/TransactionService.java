/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.customermanagement.service;

import com.mycompany.customermanagement.dao.TransactionDAO;
import com.mycompany.customermanagement.model.Transaction;
import java.util.List;

// services = tempat logika aplikasi. -> menangani TransactionDAO
public class TransactionService {
    
    private final TransactionDAO transactionDAO = new TransactionDAO();
 
    public List<Transaction> getAll() {
        return transactionDAO.getAll();
    }
 
    public List<Transaction> getByCustomerId(int customerId) {
        return transactionDAO.getByCustomerId(customerId);
    }
 
    public void delete(int id) {
        transactionDAO.delete(id);
    }
}
