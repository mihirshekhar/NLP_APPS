package com.mihir.index;
/**
 * This is a script file.It Takes content from Xml corpus,converts it into text
 * strips html tags,and using coreNLP finds tag count in bigram,trigram and unigram
 * Needs merger to merge files.
 * 
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class NGramGenerator {
	//thses constants need to be initialsed to proper values before action
	//Add jsoup jar file
	
	private static final String PathToTrainingData="/home/mihir/workspace/NLPData/pan_author_profiling_training_data/en";
	private static final String PathOfModelFileCoreNLP="/home/mihir/workspace/CoreNLP/stanford-corenlp-full-2012-11-12/models/pos-tagger/wsj-left3words/wsj-0-18-left3words-distsim.tagger";
	private static final String PathOfTempTxtFile="/home/mihir/workspace/NLPData/temp/";
	private static final String PathOfMaleIndex1="/home/mihir/workspace/NLPData/male/unigram/";
	private static final String PathOfMaleIndex2="/home/mihir/workspace/NLPData/male/bigram/";
	private static final String PathOfMaleIndex3="/home/mihir/workspace/NLPData/male/trigram/";
	private static final String PathOfFemaleIndex1="/home/mihir/workspace/NLPData/female/unigram/";
	private static final String PathOfFemaleIndex2="/home/mihir/workspace/NLPData/female/bigram/";
	private static final String PathOfFemaleIndex3="/home/mihir/workspace/NLPData/female/trigram/";
	private static final int BufferSize = 1000;//no of files to keep in memory
	
	private static TreeMap<String,Integer>maleUnigram;
	private static TreeMap<String,Integer>femaleUnigram;
	private static TreeMap<String,Integer>maleBigram;
	private static TreeMap<String,Integer>femaleBigram;
	private static TreeMap<String,Integer>maleTrigram;
	private static TreeMap<String,Integer>femaleTrigram;
	private static int sex =0;
	private static FileWriter fstream ;
	private static BufferedWriter out;
	private static int fileCount=0;
	private static int fileCounter=0;
	public static void main(String[] args) throws InterruptedException, ClassNotFoundException, IOException, ParserConfigurationException, SAXException {

		maleUnigram=new TreeMap<String,Integer>();
		maleBigram=new TreeMap<String,Integer>();
		maleTrigram=new TreeMap<String,Integer>();
		femaleUnigram=new TreeMap<String,Integer>();
		femaleBigram=new TreeMap<String,Integer>();
		femaleTrigram=new TreeMap<String,Integer>();
		
		File folder = new File(PathToTrainingData);
		File files[]=folder.listFiles();

		int i,j;
		for(i = 0,j=folder.listFiles().length; j>0; j--)
		{
            ++fileCount;
			File file= files[j-1];
			
			if(file.getName().contains("female"))
			{
				sex=2;
			}
			else
			{
				sex=1;
			}
			DocumentBuilderFactory odbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder odb =  odbf.newDocumentBuilder();
			Document odoc = odb.parse (file);
			odoc.getDocumentElement ().normalize ();
			//count no of element with converation
			NodeList nList = odoc.getElementsByTagName("conversation");
			
			fstream = new FileWriter(PathOfTempTxtFile+sex+".txt",false);
			out = new BufferedWriter(fstream);
			for(int k = 0;k<nList.getLength();k++){
				Element e = (Element)nList.item(k);
				String text = e.getTextContent().trim();
				text= Jsoup.parse(text).text();
				text=text.replaceAll(";", ".");
				out.write(text);
				out.newLine();
			}
             out.close();
             fstream.close();
			switch(sex)
			{
			case 1: callMaleFunction();
			     break;
			case 2: callFemaleFunction();
			}
			
			++fileCount;
			if(fileCount>=BufferSize)
			{
				++fileCounter;
				fileCount=0;
				WriteIndex(PathOfFemaleIndex1+fileCounter+".txt",femaleUnigram);
				WriteIndex(PathOfFemaleIndex2+fileCounter+".txt",femaleBigram);
				WriteIndex(PathOfFemaleIndex3+fileCounter+".txt",femaleTrigram);
				
				WriteIndex(PathOfMaleIndex1+fileCounter+".txt",maleUnigram);
				WriteIndex(PathOfMaleIndex2+fileCounter+".txt",maleBigram);
				WriteIndex(PathOfMaleIndex3+fileCounter+".txt",maleTrigram);
			}
			
		}

	}

	/**
	 * Writes Index file to folder.Later merge to run to get final index
	 * @param pathOfIndexFile path of file where file to be written
	 * @param map Tree map to write as index.It flushes Treemap after writing index
	 * @throws IOException,FileNotFoundException 
	 * 
	 */
	private static void WriteIndex(String fileName,
			TreeMap<String, Integer> map) throws IOException,FileNotFoundException {
		FileWriter stream = new FileWriter(fileName,false);
		BufferedWriter bo = new BufferedWriter(stream)	;
		System.out.println(fileName);
		Iterator iterator = map.keySet().iterator();
		while(iterator.hasNext())
		{
			String key = iterator.next().toString();
			String count=map.get(key).toString();
			
			bo.write(key+"|"+count);
			bo.newLine();
			System.out.println(key+"|"+count);
			
		}
		
		bo.close();
		stream.close();
		map.clear();
		
	}

	private static void callFemaleFunction() throws ClassNotFoundException, IOException {
		// TODO Auto-generated method stub
		ArrayList<String> tags = Tagging(PathOfTempTxtFile+"2.txt");
		CountOneGram(tags,femaleUnigram);
		CountBiGram(tags,femaleBigram);
		CountTriGram(tags,femaleTrigram);
		
	}

	private static void callMaleFunction() throws ClassNotFoundException, IOException {
		// TODO Auto-generated method stub
		ArrayList<String> tags = Tagging(PathOfTempTxtFile+"1.txt");
		CountOneGram(tags,maleUnigram);
		CountBiGram(tags,maleBigram);
		CountTriGram(tags,maleTrigram);
		
	}

	/**
	 * 
	 * @param path  the path of the file to be provided for generating tags
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @return returns an arraylist with pos tags attached like "[0] such/JJ [1] as/IN [2]Daring/NNP [3]Fireball/NNP"
	 */
	public static ArrayList<String> Tagging(String path) throws IOException, ClassNotFoundException
	{
		ArrayList<String> tags=new ArrayList<String>();
		try {

			int i=0;
			String key;
			String temp;
			int split_pos;
			//Loading the tagger model
			MaxentTagger tagger = new MaxentTagger(PathOfModelFileCoreNLP);

			List<List<HasWord>> sentences;
			//Gets sentences from the document provide to file reader
			sentences = MaxentTagger.tokenizeText(new BufferedReader(new FileReader(path)));
			//for each sentence add only words to the words ArrayList that are marked NOUN by the MaxentTagger.
			for (List<HasWord> sentence : sentences) {
				ArrayList<TaggedWord> tSentence = tagger.tagSentence(sentence);
				i=0;
				while(i<tSentence.size())
				{
					temp=tSentence.get(i).toString();

					if(!temp.contains("//"))
					{
						String [] tagwrd = temp.split("/", 2);
						if(tagwrd[1].length()<8)
						{
						tags.add(tagwrd[1]);
						}
						else
						{
							tags.add("Href");
						}
					}
					i++;
				}
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return tags;
	}

	/**
	 * @param ArrayList<String[]> words withTag as returned by Tagging.
	 * It counts no of Ngrams(1) and save it to hash to be later used to count Ngram
	 */
	public static void CountOneGram (ArrayList<String> wrdtag,TreeMap<String,Integer>map)
	{

		for(int i=0;i<wrdtag.size();i++)
		{
			String key = wrdtag.get(i);
			if(map.containsKey(key))
			{
				int count_tag = map.get(key);
				++count_tag;
				map.put(key, count_tag);
			}
			else
			{
				map.put(key,1);
			}

		}
	}

	/**
	 * Generates all bigrams generated as tag/tag and stores its count in hashfile provided
	 * @param wrdtag It is tag list generated by tagging file 
	 * @param map It is TreeMap of main in which to store counts so as to print file
	 */
	public static void CountBiGram (ArrayList<String> wrdtag,TreeMap<String,Integer>map)
	{

		for(int i=0;i<wrdtag.size()-1;i++)
		{
			String key = wrdtag.get(i)+"|"+wrdtag.get(i+1);
			if(map.containsKey(key))
			{
				int count_tag = map.get(key);
				++count_tag;
				map.put(key, count_tag);
			}
			else
			{
				map.put(key,1);
			}

		}
	}
	/**
	 * Generates all trigrams generated as tag/tag/tag and stores its count in hashfile provided
	 * @param wrdtag It is tag list generated by tagging file 
	 * @param map It is TreeMap of main in which to store counts so as to print file
	 */
	public static void CountTriGram (ArrayList<String> wrdtag,TreeMap<String,Integer>map)
	{

		for(int i=0;i<wrdtag.size()-2;i++)
		{
			String key = wrdtag.get(i)+"|"+wrdtag.get(i+1)+"|"+wrdtag.get(i+2);
			if(map.containsKey(key))
			{
				int count_tag = map.get(key);
				++count_tag;
				map.put(key, count_tag);
			}
			else
			{
				map.put(key,1);
			}

		}
	}



}
