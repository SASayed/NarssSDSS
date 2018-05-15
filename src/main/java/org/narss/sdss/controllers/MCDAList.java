/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.narss.sdss.controllers;

import org.narss.sdss.dto.MCDA;
import java.util.List;

/**
 *
 * @author wh_sayed
 */
public class MCDAList {
    private List<MCDA> mcdaList;

    public List<MCDA> getMcdaList() {
        return mcdaList;
    }

    public void setMcdaList(List<MCDA> mcdaList) {
        this.mcdaList = mcdaList;
    }
}
