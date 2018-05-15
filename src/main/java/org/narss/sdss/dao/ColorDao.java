/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.narss.sdss.dao;
import java.util.List;
import org.narss.sdss.dto.color;

/**
 *
 * @author Administrator
 */
public interface ColorDao {
    public List<color> getAll();
    public color GetColorByCode(String Code) ;
    
}
