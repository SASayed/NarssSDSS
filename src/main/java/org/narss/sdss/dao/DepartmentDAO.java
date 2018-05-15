/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.narss.sdss.dao;

import org.narss.sdss.dto.Department;
import java.util.List;
/**
 *
 * @author Administrator
 */

public interface DepartmentDAO {

   

    public List<Department> getAll();
    public Department GetDeptById(int id) ;

}