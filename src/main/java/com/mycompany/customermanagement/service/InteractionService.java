/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.customermanagement.service;

import com.mycompany.customermanagement.dao.InteractionDAO;
import com.mycompany.customermanagement.model.Interaction;
import java.util.List;

// services = tempat logika aplikasi. -> menangani InteractionDAO
public class InteractionService {
    private final InteractionDAO interactionDAO = new InteractionDAO();
 
    public List<Interaction> getAll() {
        return interactionDAO.getAll();
    }
 
    public List<Interaction> getByCustomerId(int customerId) {
        return interactionDAO.getByCustomerId(customerId);
    }
 
    public void delete(int id) {
        interactionDAO.delete(id);
    }
    
    public void save(Interaction interaction) {
        interactionDAO.save(interaction);
    }
 
    public void update(Interaction interaction) {
        interactionDAO.update(interaction);
    }
}
