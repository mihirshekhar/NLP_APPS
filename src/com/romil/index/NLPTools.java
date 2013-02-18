package com.romil.index;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.HandlerBase;


import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
/**
 * @author romil
 *
 */
public class NLPTools {

	/**
	 * @param args
	 */
	static final List<String> stopwords=readStopWords("stopwords");
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String str= "My name is Mr. M.S. Dhoni. I am working on IR Project. I wouldn't loves learn maths";
		str=" Read further to discover a whole lot of simple and effective advice to help you improve your home.&lt;br /&gt;;&lt;br /&gt;;If you have a &lt;a href='http://en.pan.netcom/go/out/url=-aHR0cDovL2hvdXNlLWJ1aWxkZXItcmVub3ZhdG9yLnR1bWJsci5jb20vcG9zdC8xOTI3Mzk2NzEzMy9yb29mLXNoaW5nbGVzLWFmZm9yZGFibGUtdG8tbWFudWZhY3R1cmUtd2hpY2gtbWFrZXM_' target='_blank'&gt;;roof shingles&lt;/a&gt;; then ensure you keep an accurate maintenance log, which will really help you to keep organised.&lt;br /&gt;;&lt;br /&gt;;If you are doing a project with potential safety hazards, make sure you are using the proper gear.";
		for (String sentence : newTokenizer(str)) {
			System.out.println(sentence);
		}
		NLPTools t = new NLPTools();
		System.out.println(t.generateMap(str));
	}

	public  List<String> generateKeywords(String sen){
		List<String> keyword=new ArrayList<String>();
		for (String sentence : newTokenizer(sen)) {
			keyword.add(sentence);
		}
		return keyword;
	}
	
	public  Map<String,Integer> generateMap(String sen){
		Map<String,Integer> map= new HashMap<String, Integer>();
		
		for (String sentence : newTokenizer(sen)) {
			if(map.containsKey(sentence))
				map.put(sentence, map.get(sentence)+1);
			else if(!sentence.contains("http:") )
				map.put(sentence, 1);
			else{
				if(map.containsKey("~~URLS~~"))
					map.put("~~URLS~~", map.get("~~URLS~~")+1);
				else
					map.put("~~URLS~~", 1);
			}
				
		}
		return map;
	
	}
	
	@Deprecated
	public static List<String> tokenize(String s) {
		String[] stopWords=fileRead("stopword").split(",");
		System.out.println(stopWords);
		List<String> stopWord = new ArrayList<String>();
		for(String sr:stopWords)
			stopWord.add(sr);
		PTBTokenizer<CoreLabel> ptbt = new PTBTokenizer<CoreLabel>(
				new StringReader(s), new CoreLabelTokenFactory(), "");
		List<String> sentence = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		for (CoreLabel label; ptbt.hasNext();) {
			label = ptbt.next();
			String word = label.word();
			if (word.startsWith("'")) {
				sb.append(word);
			} else {
				if (sb.length() > 2)
					sentence.add(sb.toString());
				sb = new StringBuilder();
				sb.append(word);
			}
		}
		if (sb.length() > 2)
			sentence.add(sb.toString());
		return sentence;
	}
	@Deprecated
	public static List<String> newTokenizer(String s){
		List<String> tokens=new ArrayList<String>();
		PTBTokenizer ptbt = new PTBTokenizer(new StringReader(s),new CoreLabelTokenFactory(),"");			//PTBTokenizer ptbt = ptbTokenizer;
		for (CoreLabel label; ptbt.hasNext(); ) {
			label = (CoreLabel) ptbt.next();
			String token=label.word();
			//int position=label.beginPosition();
			//System.err.println(label.ner());
			Stemmer se = new Stemmer();
			String word=token.trim();
			se.add(word.toCharArray(), word.length());
			se.stem();
			token=se.toString().toLowerCase();
			if(token.trim().length()>2&&!checkStopWord(token)){
				tokens.add(se.toString().toLowerCase());
			}
		}
		return tokens;
	}
	public static List<String> JavaTokenizer(String s){
		List<String> tokens = new ArrayList<String>();
		s=s.replaceAll("[^a-zA-Z]", " ");
		String[] str= s.split("[ -/|_]");
		//str=str.replaceAll("[^a-zA-Z]", " ");
		for(String i:str){
			//i=i.replaceAll("[^a-zA-Z]", " ");
			
			Stemmer se = new Stemmer();
			String word=i.trim();
			se.add(word.toCharArray(), word.length());
			se.stem();
			i=se.toString().toLowerCase();
			
			if(i.trim().length()>2&&!checkStopWord(i))
				tokens.add(i);
		}
		return tokens;
	}
	public static boolean checkStopWord(String word){
		//System.err.println(word);
		if(stopwords.contains(word.trim()))
			return true;
		else 
			return false;
	}
	public static List<String> readStopWords(String args){
		String[] s=fileRead(args).split(",");
		List<String> str=new ArrayList<String>();
		for(String q:s){
			Stemmer se = new Stemmer();
			String word=q.trim();
			se.add(word.toCharArray(), word.length());
			se.stem();
			q=se.toString().toLowerCase();
			//System.out.println(word+"->"+q);
			str.add(q.trim().toLowerCase());

		}
		System.err.println(str);
		return str;

	}
	public static String fileRead(String args)
	{
		String ret="";
		try{
			FileInputStream fstream = new FileInputStream(args);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;

			//Read File Line By Line
			while ((strLine = br.readLine()) != null)   {
				// Print the content on the console
				ret+=strLine;
			}
			//Close the input stream
			in.close();

		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
		//System.err.println(ret);
		return ret;
	}



}