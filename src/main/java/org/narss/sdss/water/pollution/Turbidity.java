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
public class Turbidity {
    private final double a0;
    private final long a;
    private final int referenceValue; // Jackson unit
    
    public Turbidity() {
        this.a0 = 2.895;
        this.a = 1947;
        this.referenceValue = 5;
    }
    
    public double getA0() {
        return a0;
    }

    public long getA() {
        return a;
    }
    
    public int getReferenceValue() {
        return referenceValue;
    }
}
