/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.narss.sdss.water.pollution;

import javax.swing.Icon;
import javax.swing.JOptionPane;

/**
 *
 * @author Sayed
 */
public class WaterPollutionTest {
    public static void main(String[] args)
    {
        String pollutant = JOptionPane.showInputDialog(null, "Please Enter the Pollutant Name", "Poluttant Name", JOptionPane.INFORMATION_MESSAGE);
        double x = Double.parseDouble(JOptionPane.showInputDialog(null, "Please Enter the Reflectence Value", "Reflectence Value", JOptionPane.INFORMATION_MESSAGE));
        double y = 0.0;
        
        if(pollutant.equalsIgnoreCase("SSC"))
        {
            SSC ssc = new SSC();
            y = (ssc.getA0() * x) + ssc.getA();
            if(y <= ssc.getReferenceValue())
                JOptionPane.showMessageDialog(null, "Y = " + y + " < "+ ssc.getReferenceValue() + " The Reference Value for " + pollutant +"\nThe water sample is suitable for agriculture", "Result", JOptionPane.INFORMATION_MESSAGE);
            else
                JOptionPane.showMessageDialog(null, "Y = " + y + " > "+ ssc.getReferenceValue() + " The Reference Value for " + pollutant +"\nThe water sample is not suitable for agriculture", "Result", JOptionPane.ERROR_MESSAGE);
        }
        else if(pollutant.equalsIgnoreCase("TDS"))
        {
            TDS tds = new TDS();
            y = (tds.getA0() * x) + tds.getA();
            if(y <= tds.getReferenceValue())
                JOptionPane.showMessageDialog(null, "Y = " + y + " < "+ tds.getReferenceValue() + " The Reference Value for " + pollutant +"\nThe water sample is suitable for agriculture", "Result", JOptionPane.INFORMATION_MESSAGE);
            else
                JOptionPane.showMessageDialog(null, "Y = " + y + " > "+ tds.getReferenceValue() + " The Reference Value for " + pollutant +"\nThe water sample is not suitable for agriculture", "Result", JOptionPane.ERROR_MESSAGE);
        }
        
        else if(pollutant.equalsIgnoreCase("TSS"))
        {
            TSS tss = new TSS();
            y = (tss.getA0() * x) + tss.getA();
            if(y <= tss.getReferenceValue())
                JOptionPane.showMessageDialog(null, "Y = " + y + " < "+ tss.getReferenceValue() + " The Reference Value for " + pollutant +"\nThe water sample is suitable for agriculture", "Result", JOptionPane.INFORMATION_MESSAGE);
            else
                JOptionPane.showMessageDialog(null, "Y = " + y + " > "+ tss.getReferenceValue() + " The Reference Value for " + pollutant +"\nThe water sample is not suitable for agriculture", "Result", JOptionPane.ERROR_MESSAGE);
        }
        
        else if(pollutant.equalsIgnoreCase("TBD"))
        {
            Turbidity tbd = new Turbidity();
            y = (tbd.getA0() * x) + tbd.getA();
            if(y <= tbd.getReferenceValue())
                JOptionPane.showMessageDialog(null, "Y = " + y + " < "+ tbd.getReferenceValue() + " The Reference Value for " + pollutant +"\nThe water sample is suitable for agriculture", "Result", JOptionPane.INFORMATION_MESSAGE);
            else
                JOptionPane.showMessageDialog(null, "Y = " + y + " > "+ tbd.getReferenceValue() + " The Reference Value for " + pollutant +"\nThe water sample is not suitable for agriculture", "Result", JOptionPane.ERROR_MESSAGE);
        }
        
        else if(pollutant.equalsIgnoreCase("PH"))
        {
            Chlorophyll ph = new Chlorophyll();
            y = (ph.getA0() * x) + ph.getA();
            if(y <= ph.getReferenceValue())
                JOptionPane.showMessageDialog(null, "Y = " + y + " < "+ ph.getReferenceValue() + " The Reference Value for " + pollutant +"\nThe water sample is suitable for agriculture", "Result", JOptionPane.INFORMATION_MESSAGE);
            else
                JOptionPane.showMessageDialog(null, "Y = " + y + " > "+ ph.getReferenceValue() + " The Reference Value for " + pollutant +"\nThe water sample is not suitable for agriculture", "Result", JOptionPane.ERROR_MESSAGE);
        }
    }
}
