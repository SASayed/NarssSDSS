/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.narss.sdss.dao;

import java.util.List;
import org.narss.sdss.dto.Style;

/**
 *
 * @author Administrator
 */
public interface StyleDAO {
    
     public int add(Style style);
     public Style GetStyleByName(String name);
      public List<Style> getAll();
       public int delete(int id);
        public int update(Style style);
         public Style getStyleById(int id);
         public int updatefile(Style style);
    
}
