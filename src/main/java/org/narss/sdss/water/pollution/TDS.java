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
public class TDS {
    private final long a0;
    private final long a;
    private final int referenceValue;

    public TDS() {
        this.a0 = 2025;
        this.a = 3180;
        this.referenceValue = 2000;
    }
    
    public long getA0() {
        return a0;
    }

    public long getA() {
        return a;
    }
    
    public int getReferenceValue() {
        return referenceValue;
    }
}