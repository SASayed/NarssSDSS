/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.narss.sdss.dao;

import java.util.List;
import org.narss.sdss.dto.User;

/**
 *
 * @author Administrator
 */
public interface UsersDAO {
    public Boolean SelectSpecificUser(String name,String Password,String role);
   public List<User> getAll();
    
}
