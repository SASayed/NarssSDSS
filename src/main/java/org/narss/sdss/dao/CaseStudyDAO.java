/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.narss.sdss.dao;

import java.sql.Connection;
import java.util.List;
import org.narss.sdss.dto.CaseStudy;

/**
 *
 * @author Sayed
 */
public interface CaseStudyDAO {
    public int addCaseStudy(CaseStudy caseStudy, Connection connection);
    public List<CaseStudy> getAllCaseStudy(Connection connection);
    public CaseStudy getCaseStudy(String name,Connection connection);
   public CaseStudy getCaseStudy(int id);
    public int updateCaseStudy(CaseStudy caseStudy, int id, Connection connection);
    public int deleteCaseStudy(String name, Connection connection);
}
