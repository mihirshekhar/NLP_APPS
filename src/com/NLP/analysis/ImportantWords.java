package com.NLP.analysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.TreeMap;

public class ImportantWords {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		FileInputStream fis = new FileInputStream("/home/santoshkosgi/Desktop/femaleindex/index.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		String first = br.readLine();
		TreeMap<Integer , ArrayList<String> > value = new TreeMap<Integer , ArrayList<String> >();
		while(first!=null){
			String s[] = first.split("[|@]]");
			int a = Integer.parseInt(s[1])/Integer.parseInt(s[2]);
			if(value.containsKey(a)){
				ArrayList<String> words = value.get(s);
				words.add(s[0]);
				value.put(a, words);
			}
			else{
				ArrayList<String> words = new ArrayList<String>();
				words.add(s[0]);
				value.put(a, words);
			}
			first = br.readLine();
		}
		Writer output;
		output = new BufferedWriter(new FileWriter("/home/santoshkosgi/Desktop/femaleindex/temp.txt", true));
		output.append(value.toString());
		
		output.flush();
		
		output.close();
		br.close();
		fis.close();
		
	}

}
