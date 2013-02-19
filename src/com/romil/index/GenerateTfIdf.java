/**
 * 
 */
package com.romil.index;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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


		TreeMap<String,ArrayList<Integer>> index= new TreeMap<String,ArrayList<Integer>>();
		TreeMap<String,ArrayList<Integer>> index2= new TreeMap<String,ArrayList<Integer>>();

		File folder = new File("/home/romil/Documents/pan_author_profiling_training_data/en/");
		File files[]=folder.listFiles();
		int i,j;
		for(i = 0,j=folder.listFiles().length; j>0; j--){
			System.err.println(j);
			try {
				File file=  new File("/home/romil/Documents/pan_author_profiling_training_data/en/fff2a45894aaf04e05e3c3b6509b9f1_en_20s_male.xml");
				String name[]=file.getName().split("[_.]");
				//System.out.println(name[name.length-2]);
				//System.out.println(name[0]);
				DocumentBuilderFactory odbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder odb =  odbf.newDocumentBuilder();
				Document odoc = odb.parse (file);
				odoc.getDocumentElement ().normalize ();
				//System.out.println("Root element :" + odoc.getDocumentElement().getNodeName());

				NodeList nList = odoc.getElementsByTagName("conversation");
				for(int k = 0;k<nList.getLength();k++){
					Element e = (Element)nList.item(k);
					//System.out.println(e.getAttribute("id"));
					String conversation = e.getTextContent().trim();
					//System.out.println(e.getTextContent().trim());
					if(name[name.length-2].equalsIgnoreCase("male")){
						i++;
						Map<String, Integer> temp = tool.generateMap(e.getTextContent().trim());
						for(String s:temp.keySet()){
							if(index.containsKey(s)){
								ArrayList<Integer> t= index.get(s);
								Integer ss = t.get(0) + temp.get(s);
								t.remove(0);
								t.add(0, ss);
								index.put(s, new ArrayList<Integer>(t));

								ArrayList<Integer> t2= index2.get(s);
								Integer ss2 = t2.get(0) + 1;
								t2.remove(0);
								t2.add(0, ss2);
								index2.put(s, new ArrayList<Integer>(t2));


							}
							else{
								ArrayList<Integer> t = new ArrayList<Integer>();
								t.add(temp.get(s));
								t.add(0);
								index.put(s,new ArrayList<Integer>(t));

								ArrayList<Integer> t2 = new ArrayList<Integer>();
								t2.add(1);
								t2.add(0);
								index2.put(s,new ArrayList<Integer>(t2));

							}
						}
					}
					else{
						i++;
						Map<String, Integer> temp = tool.generateMap(e.getTextContent().trim());
						for(String s:temp.keySet()){
							if(index.containsKey(s)){
								ArrayList<Integer> t= index.get(s);
								Integer ss = t.get(1) + temp.get(s);
								t.remove(1);
								t.add(1, ss);
								index.put(s, new ArrayList<Integer>(t));

								ArrayList<Integer> t2= index2.get(s);
								Integer ss2 = t2.get(1) + 1;
								t2.remove(1);
								t2.add(1, ss2);
								index2.put(s, new ArrayList<Integer>(t2));

							}
							else{
								ArrayList<Integer> t = new ArrayList<Integer>();
								t.add(0);
								t.add(temp.get(s));
								index.put(s,new ArrayList<Integer>(t));

								ArrayList<Integer> t2 = new ArrayList<Integer>();
								t2.add(0);
								t2.add(1);
								index2.put(s,new ArrayList<Integer>(t2));

							}
						}
					}
					if(i%10000==0){
						Integer in=i/10000;
						FileWriter fileWritter = new FileWriter("./maleindex/index"+in.toString()+".txt",true);
						FileWriter fileWritter2 = new FileWriter("./femaleindex/index"+in.toString()+".txt",true);
						for(String s : index.keySet()){
							String q= s+"|"+index.get(s).get(0)+"@"+index.get(s).get(1)+"\n";
							fileWritter.append(q);

							String q2= s+"|"+index2.get(s).get(0)+"@"+index2.get(s).get(1)+"\n";
							fileWritter2.append(q2);

						}
						fileWritter.flush();
						fileWritter.close();

						fileWritter2.flush();
						fileWritter2.close();

						index.clear();
						index2.clear();
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally{
				//file.renameTo(new File("done/"+file.getName()));
				break;
			}
		}
		if(i%10000!=0){
			Integer in=i/10000;
			in++;
			FileWriter fileWritter = new FileWriter("./maleindex/index"+in.toString()+".txt",true);
			FileWriter fileWritter2 = new FileWriter("./femaleindex/index"+in.toString()+".txt",true);

			for(String s : index.keySet()){
				String q= s+"|"+index.get(s).get(0)+"@"+index.get(s).get(1)+"\n";
				fileWritter.append(q);

				String q2= s+"|"+index2.get(s).get(0)+"@"+index2.get(s).get(1)+"\n";
				fileWritter2.append(q2);

			}
			fileWritter.flush();
			fileWritter.close();

			fileWritter2.flush();
			fileWritter2.close();

			index.clear();
			index2.clear();
		}

		long t1=System.currentTimeMillis();
		//bufferWritter.close();
		//fileWritter.close();

		//bufferWritter2.close();
		//fileWritter2.close();
		System.out.println(t1-t0);
	}
}
