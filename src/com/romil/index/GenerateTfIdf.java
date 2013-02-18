/**
 * 
 */
package com.romil.index;

import java.io.BufferedWriter;
import java.io.File;
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
public class GenerateTfIdf {

	/**
	 * @param args
	 */
	
	static NLPTools tool= new NLPTools();
	
	
	public static void main(String[] argv) throws IOException {
		// TODO Auto-generated method stub

		long t0=System.currentTimeMillis();
		new File("maleindex").mkdir();
		new File("femaleindex").mkdir();
		//final RandomAccessFile fileWritter=new RandomAccessFile("search_titleindex/index.txt", "rw");
		//FileWriter fileWritter = new FileWriter("maleindex/index.txt");
		//FileWriter fileWritter2 = new FileWriter("femaleindex/index.txt");

		//final BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
		//final BufferedWriter bufferWritter2 = new BufferedWriter(fileWritter2);

		TreeMap<String,String> male= new TreeMap<String,String>();
		TreeMap<String,String> female= new TreeMap<String,String>();
		
		File folder = new File("ens/");
		File file=folder.listFiles()[0];
		int i,j;
		for(i = 0,j=0; folder.listFiles().length>0;){
			try {
				file= folder.listFiles()[0];
				String name[]=file.getName().split("[_.]");
				System.out.println(name[name.length-2]);
				System.out.println(name[0]);
	            DocumentBuilderFactory odbf = DocumentBuilderFactory.newInstance();
	            DocumentBuilder odb =  odbf.newDocumentBuilder();
	            Document odoc = odb.parse (file);
	            odoc.getDocumentElement ().normalize ();
	        	//System.out.println("Root element :" + odoc.getDocumentElement().getNodeName());
	            
	        	NodeList nList = odoc.getElementsByTagName("conversation");
	        	Element e = (Element)nList.item(0);
	        	System.out.println(e.getAttribute("id"));
	        	System.out.println(e.getTextContent().trim());
				if(name[name.length-2].equalsIgnoreCase("male")){
					i++;
					Map<String, Integer> temp = tool.generateMap(e.getTextContent().trim());
					for(String s:temp.keySet()){
						if(male.containsKey(s)){
							male.put(s, male.get(s).concat("$#"+name[0]+","+temp.get(s)));
						}
						else{
							male.put(s, name[0]+","+temp.get(s));
						}
					}
				}
				else{
					j++;
					Map<String, Integer> temp = tool.generateMap(e.getTextContent().trim());
					for(String s:temp.keySet()){
						if(female.containsKey(s)){
							female.put(s, female.get(s).concat("$#"+name[0]+","+temp.get(s)));
						}
						else{
							female.put(s, name[0]+","+temp.get(s));
						}
					}
				}
				if(i%10000==0){
					Integer index=i/10000;
					FileWriter fileWritter = new FileWriter("./maleindex/index"+index.toString()+".txt",true);
					for(String s : male.keySet()){
						String q= s+"$#"+male.get(s)+"\n";
						fileWritter.append(q);
					}
					fileWritter.flush();
					fileWritter.close();
					male.clear();
				}
				if(j%10000==0){
					Integer index=j/10000;
					FileWriter fileWritter = new FileWriter("./femaleindex/index"+index.toString()+".txt",true);
					for(String s : female.keySet()){
						String q= s+"$#"+female.get(s)+"\n";
						fileWritter.append(q);
			
					}
					fileWritter.flush();
					fileWritter.close();
					female.clear();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally{
				file.renameTo(new File("done/"+file.getName()));
				//break;
			}
		}
		if(i%10000!=0){
			Integer index=i/10000;
			index++;
			FileWriter fileWritter = new FileWriter("./maleindex/index"+index.toString()+".txt",true);
			for(String s : male.keySet()){
				String q= s+"$#"+male.get(s)+"\n";
				fileWritter.append(q);
			}
			fileWritter.flush();
			fileWritter.close();
			male.clear();
		}
		if(j%10000!=0){
			Integer index=j/10000;
			index++;
			FileWriter fileWritter = new FileWriter("./femaleindex/index"+index.toString()+".txt",true);
			for(String s : female.keySet()){
				String q= s+"$#"+female.get(s)+"\n";
				fileWritter.append(q);
			}
			fileWritter.flush();
			fileWritter.close();
			female.clear();
		}
		long t1=System.currentTimeMillis();
		//bufferWritter.close();
		//fileWritter.close();
		
		//bufferWritter2.close();
		//fileWritter2.close();
		System.out.println(t1-t0);
	}
}
