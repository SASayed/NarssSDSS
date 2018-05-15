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
public class TSS {
    private final double a0;
    private final long a;
    private final int referenceValue;
    
    public TSS()
    {
        this.a0 = 694.9;
        this.a = 5051;
        this.referenceValue = 500;
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

