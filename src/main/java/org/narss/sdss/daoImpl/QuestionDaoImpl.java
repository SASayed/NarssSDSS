/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.narss.sdss.daoImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.narss.sdss.dao.QuestionDAO;
import org.narss.sdss.dto.Question;

/**
 *
 * @author Sayed
 */
public class QuestionDaoImpl implements QuestionDAO{

    @Override
    public int addQuestion(Question question, Connection connection) {
        int numOfRows = 0;
        String sql = "INSERT INTO question "
                + "(name, description, scope, category ) VALUES ('" + question.getQuestion()
                + "','"+question.getQuestionDescription()+"','"+question.getScopeName()+"','"+question.getCategoryName()+"')";
        try {
            Statement st = null;
            st = connection.createStatement();
            numOfRows = st.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(QuestionDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
       return numOfRows;
    }
    //--------------------------------------------------------------------------
    @Override
    public List<Question> getAllQuestions(Connection connection) {
        List<Question> questionList = new ArrayList<Question>();
        String sql = "SELECT * FROM question";
        try{
            Statement st = null;
            st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                Question question = new Question();
                question.setId(rs.getString("id"));
                question.setQuestion(rs.getString("name"));
                question.setQuestionDescription(rs.getString("description"));
                question.setScopeName(rs.getString("scope"));
                question.setCategoryName(rs.getString("category"));
                questionList.add(question);
            } 
        } catch (SQLException ex) {
            Logger.getLogger(QuestionDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return questionList;
    }
    //--------------------------------------------------------------------------
    @Override
    public int deleteQuestion(String name, Connection connection)
    {
        int numOfRows = 0;
        String sql = "DELETE FROM question WHERE name = '" + name + "'";
        try {
            Statement st = null;
            st = connection.createStatement();
            numOfRows = st.executeUpdate(sql);
            st.close();
                //connection.commit();
                connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(QuestionDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
       return numOfRows;
    }
}
