/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.narss.sdss.dao;

import java.sql.Connection;
import java.util.List;
import org.narss.sdss.dto.Question;

/**
 *
 * @author Sayed
 */
public interface QuestionDAO {
    public int addQuestion(Question question, Connection connection);
    public List<Question> getAllQuestions(Connection connection);
    public int deleteQuestion(String name, Connection connection);
}
