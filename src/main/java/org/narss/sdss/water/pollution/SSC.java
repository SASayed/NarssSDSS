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
public class SSC {
    private final double a0;
    private final long a;
    private final int referenceValue;
    
    public SSC()
    {
        this.a0 = 7.221;
        this.a = 2487;
        this.referenceValue = 30;
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
