/**
 * 
 */
package com.romil.index;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @author romil
 *
 */
public class copy {

	/**
	 * @param args
	 */



	public static void main(String[] argv) throws IOException {
		File folder = new File("en/");
		File f[]=folder.listFiles();
		File afile=folder.listFiles()[0];
		for(int i = 0; i<1000;i++){
			System.out.println(i);
			afile= f[i];
			String bfile="ens/"+afile.getName();
			FileInputStream inStream = new FileInputStream(afile);
			FileOutputStream outStream = new FileOutputStream(bfile);

			byte[] buffer = new byte[1024];

			int length;
			//copy the file content in bytes 
			while ((length = inStream.read(buffer)) > 0){

				outStream.write(buffer, 0, length);

			}

			inStream.close();
			outStream.close();
		}
	}
}
