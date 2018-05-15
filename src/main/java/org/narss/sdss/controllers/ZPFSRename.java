/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.narss.sdss.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author Administrator
 */
public class ZPFSRename {

    public File unZipIt(String zipFile, String outputFolder){
     
     File folder = null;
     byte[] buffer = new byte[1024];

     try{

    	//create output directory is not exists
    	folder = new File(Properties.ZIPFILE+outputFolder);
    	if(!folder.exists()){
    		folder.mkdir();
    	}

    	//get the zip file content
    	ZipInputStream zis =
    		new ZipInputStream(new FileInputStream(Properties.ZIPFILE+zipFile));
    	//get the zipped file list entry
    	ZipEntry ze = zis.getNextEntry();

    	while(ze!=null){

    	   //String fileName = ze.getName();
           File newFile = new File(Properties.ZIPFILE+outputFolder + File.separator + outputFolder + ze.getName().substring(ze.getName().indexOf('.'), ze.getName().length()));

           System.out.println("file unzip : "+ newFile.getAbsoluteFile());

            //create all non exists folders
            //else you will hit FileNotFoundException for compressed folder
            new File(newFile.getParent()).mkdirs();

            FileOutputStream fos = new FileOutputStream(newFile);

            int len;
            while ((len = zis.read(buffer)) > 0) {
       		fos.write(buffer, 0, len);
            }

            fos.close();
            ze = zis.getNextEntry();
    	}

        zis.closeEntry();
    	zis.close();

    	System.out.println("Done");
        
        
     }catch(IOException ex){
       ex.printStackTrace();
    }
     return folder;
   }
    //--------------------------------------------------------------------------
    public void zipIt(String sourceFolder, String zipFile, List<String> fileList){

     byte[] buffer = new byte[1024];

     try{

    	FileOutputStream fos = new FileOutputStream(Properties.ZIPFILE + zipFile);
    	ZipOutputStream zos = new ZipOutputStream(fos);

    	System.out.println("Output to Zip : " + zipFile);

    	for(String file : fileList){

    		System.out.println("File Added : " + file);
    		ZipEntry ze= new ZipEntry(file.substring(file.lastIndexOf('\\')+1, file.length()));
        	zos.putNextEntry(ze);

        	FileInputStream in =
                       new FileInputStream(file);

        	int len;
        	while ((len = in.read(buffer)) > 0) {
        		zos.write(buffer, 0, len);
        	}

        	in.close();
    	}
        
    	zos.closeEntry();
    	//remember close it
    	zos.close();

    	System.out.println("Done");
        new File(Properties.ZIPFILE+sourceFolder).deleteOnExit();
    }catch(IOException ex){
       ex.printStackTrace();
    }
   }
}
