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
public class SamplePoint {
    private String ID;
    private String sampleSite;
    private double xOfChlorophyll;
    private double xOfSSC;
    private double xOfTDS;
    private double xOfTSS;
    private double xOfTurbidity;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getSampleSite() {
        return sampleSite;
    }

    public void setSampleSite(String sampleSite) {
        this.sampleSite = sampleSite;
    }

    public double getxOfChlorophyll() {
        return xOfChlorophyll;
    }

    public void setxOfChlorophyll(double xOfChlorophyll) {
        this.xOfChlorophyll = xOfChlorophyll;
    }

    public double getxOfSSC() {
        return xOfSSC;
    }

    public void setxOfSSC(double xOfSSC) {
        this.xOfSSC = xOfSSC;
    }

    public double getxOfTDS() {
        return xOfTDS;
    }

    public void setxOfTDS(double xOfTDS) {
        this.xOfTDS = xOfTDS;
    }

    public double getxOfTSS() {
        return xOfTSS;
    }

    public void setxOfTSS(double xOfTSS) {
        this.xOfTSS = xOfTSS;
    }

    public double getxOfTurbidity() {
        return xOfTurbidity;
    }

    public void setxOfTurbidity(double xOfTurbidity) {
        this.xOfTurbidity = xOfTurbidity;
    }
}
