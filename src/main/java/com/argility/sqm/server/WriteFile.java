package com.argility.sqm.server;

import java.io.File;
import java.io.FileOutputStream;

public class WriteFile {
    
	private String writeFile = "ehlbm_update0001.sql";
	
	public void writeFile(String s) {
		// Append to default filename
		writeFile(s, writeFile, false);
	}
	
	public void writeFile(String s, boolean createFile) {
		// Write to default filename, create if true
		writeFile(s, writeFile, createFile);
	}
	
	public void writeFile(String s, String sFile, boolean createFile) {
		// Write to filename passed as parameter, create if true
		FileOutputStream fos = null;
		try{
			File f = new File(sFile);
			if(!createFile){
				// Create file only if it does not exist
				if(!f.exists()){
					fos = new FileOutputStream(f);
				} else {
					fos = new FileOutputStream(sFile, true);
				}
			} else {
				fos = new FileOutputStream(f);
			}
			s += "\n";
			fos.write(s.getBytes());
		} catch(Exception ex) {
			System.out.println("Error writing to file : " + ex);
		} finally {
			try {
				fos.close();
			} catch(Exception ex){}
		}
	}
	
	public void setWriteFileName(String s) {
		writeFile = s;
	}
}
