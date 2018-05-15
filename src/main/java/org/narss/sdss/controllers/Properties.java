/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.narss.sdss.controllers;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class Properties {

    public static String PROJECT_PATH = "D:\\sdssFiles\\";
    public static final String QUERY_PATH = PROJECT_PATH + "geoserverQueries\\";
    public static final String WPS_OUTPUT = PROJECT_PATH + "geoserverOutput\\";
    static final String MCDA_QUERY_PATH = PROJECT_PATH + "MCDA_Queries\\";
    static final String MCDA_XSL_PATH = PROJECT_PATH + "MCDA_xsl\\";
    public static final String GeoModelling = PROJECT_PATH + "geoModelling\\";
    public static final String MCDA_INPUT = PROJECT_PATH + "MCDA_Inputs\\";
    public static final String MCDA_OUTPUT = PROJECT_PATH + "MCDA_Outputs\\";
     public static final String Style_INPUT = PROJECT_PATH + "styles\\";
    public static final String Style_OUTPUT = Style_INPUT + "output\\";
    public static final String SOAP_REQUESTS = PROJECT_PATH + "SoapRequests\\";
    public static final String ZIPFILE = "D:/sdssFiles/geoserverOutput/";
    public static final String Reports = PROJECT_PATH +"Report\\";
    public static final String TEMP =  "C:\\temp\\";
    //Specialist Files
    public static final String SPECIALIST = PROJECT_PATH + "Specialist\\";
    public static final String CASESTUDY = SPECIALIST + "casestudy.xml";
    public static final String MODEL = SPECIALIST + "model.xml";
    public static final String MCDA = SPECIALIST + "mcdas.xml";
    public static final String CAPABILITIES = SPECIALIST + "getcapabilities.xml";
    
    //Water Sample Locations
   //Water Sample Locations
    public static final String WATER_SAMPLE_LOCATION = PROJECT_PATH + "samples\\";
    public static final String SAMPLES = WATER_SAMPLE_LOCATION + "samplePoints.xml";
    public static final String SAMPLES1 = WATER_SAMPLE_LOCATION + "samplePointsCopy.xml";
    public static final String SOIlCASEID = "Soil_Case_2017030211555820170416124859";

    // geoserver
    public static final String UrlforWPS = "http://192.168.1.160:8080/geoserver/ows?service=wps";
    public static final String UrlforUpload = "http://192.168.1.160:8080/geoserver/rest/workspaces/narss/datastores/publicStore/file.shp";
    public static final String[] extension = {"shp", "cst", "dbf", "prj", "shx"};
    public static final String workSpace = "narss:";
    //question file
    public static final String DECISION_MAKER = PROJECT_PATH + "DecisionMaker\\";
    public static final String DSQUESTIONS = DECISION_MAKER + "question.xml";
   
   
    public static final String GEOSERVER_ADMIN = "admin";
    public static final String GEOSERVER_PASS = "geoserver";
    public static final String Capabilites = SPECIALIST + "getcapabilities.xml";
    //Layer Attributes
    public static final String[] LAYER_ATTRIBUTES = {"slope_ID", "sample1_So", "sample1_Sa", "sample1_Te", "sample1_Ra"};
    public static final String LAYER_NAME = "Case_One_2017022801193920170327015957";

    public static final int NUMBER_OF_ATTRIBUTES = 10;

    public static final String[] SWAPS_ALIAS = {"ID", "Slope", "Shape_Leng", "Shape_Area", "Land_ID", "MU", "SU", "GU", "Intersection",
        "Soil_ID", "CaCO3", "pH", "EC", "Num", "Depth", "Capa_Index", "Salinity", "Temprature", "Rainfull", "LimeStone", "Intersection",
        "Roads_Name", "Roads_Length", "Road_Id"};

    public static final String[] SWAPS_REAL = {"Slope_Slop", "Slope_Slo0", "Slope_Slo1", "Slope_Slo2",
        "Slope_Slo3", "Slope_Slo4", "Slope_Slo5", "Slope_Slo6", "Slope_Slo7",
        "Slope_Soil", "Slope_Soi0", "Slope_Soi1", "Slope_Soi2", "Slope_Soi3", "Slope_Soi4", "Slope_Soi5",
        "Slope_Soi6", "Slope_Soi7", "Slope_Soi8", "Slope_Soi9", "Slope_INTE", "Roads_Name", "Roads_Shap", "Roads_Id"};

    public static final String DRIVER = "org.postgresql.Driver";
    public static final String DATABASE_URL = "jdbc:postgresql://192.168.1.160:5432/";
    public static final String DATABASE_NAME = "sdss";
    public static final String DATABASE_USERNAME = "postgres";
    public static final String DATABASE_PASSWORD = "root";
}
