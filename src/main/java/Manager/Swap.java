/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Manager;

/**
 *
 * @author Administrator
 */
public class Swap {

    private String id;
    private String tureVal;
    private String subVal;
    
    public Swap() {
        id = "";
        tureVal = "";
        subVal = "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTrueVal() {
        return tureVal;
    }

    public void setTrueVal(String val) {
        this.tureVal = val;
    }
    public String getSubVal() {
        return subVal;
    }

    public void setSubVal(String val) {
        this.subVal = val;
    }
}
