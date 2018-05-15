/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.narss.sdss.water.pollution;

/**
 *
 * @author Sayed
 */
public class Chlorophyll {
    private final double a0;
    private final double a;
    private final double referenceValue;
    
    public Chlorophyll() {
        this.a0 = 0.002;
        this.a = 1.331;
        this.referenceValue = 7.75;
    }
    
     public double getA0() {
        return a0;
    }

    public double getA() {
        return a;
    }

    public double getReferenceValue() {
        return referenceValue;
    }
}
