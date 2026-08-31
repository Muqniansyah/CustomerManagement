/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.customermanagement.service;

import com.mycompany.customermanagement.dao.UserDAO;
import com.mycompany.customermanagement.model.User;

// services = tempat logika aplikasi. -> menangani UserDAO
public class UserService {
    private final UserDAO userDAO = new UserDAO();

    public User login(String username, String password) {
        return userDAO.login(username, password);
    }
}
