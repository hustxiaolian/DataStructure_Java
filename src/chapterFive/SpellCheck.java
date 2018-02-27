package chapterFive;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ���е�5.21�⡣ɢ�б�ʵ��ƴд����������ʵ�ֶ�һ��Ӣ��TXT�ļ������м�飬������󣨼�����ɢ�б��е��ʣ�
 * ����ԭʼ���ֵ��ļ���utf-8����ģ���java�ļ�������utf-8���룬���ʹ��BufferedReader���readLine����
 * ˼·��
 * 1.���ȣ��������ʵ�ɢ�б��������ɢ�б��������ӷ������е�����ȳ����ܲ��ܳɹ�
 * 
 * @author 25040
 *
 */
public class SpellCheck {
	
	private static String dicPath = "D:\\javaCode\\DataStructure\\EnglishUTF-8noBOM.txt";
	
	public static void main(String[] args) {
		Map<String, List<String>> hashMap = null;
		try {
			long start = System.currentTimeMillis();
			 hashMap = SpellCheck.getSmartAddOneCharHashMap1(dicPath);
			long end = System.currentTimeMillis();
			System.out.println("use time(ms):" + (end - start));
			//saveHashTable("D:\\javaCode\\DataStructure\\dictionary_2036.txt", hashTable);
			
			//System.out.println("");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		System.out.println(hashMap.get("an"));
	}
	
	
	/**
	 * ���ַ���ֻ����ɼ򵥵ĵ��ʴʿ⹦�ܣ��������������ĵ��ʲ��У���������ʵ��ʹ������
	 * ˼·��
	 * 1. ʹ��java��io��file���Ӣ�ĵ����ļ���
	 * 2. ÿ��һ�����ʣ�����ɢ�б��д洢һ��ӳ��
	 * 3. �������е��ʣ��ر�io������ɢ�б�
	 * 
	 * ������ʱ:67ms
	 * 
	 * @param path Ӣ�ĵ��ʵ�·��
	 * @return
	 * @throws IOException ���ܳ��ֵ�io�쳣
	 */
	public static SeparateChainingHashTable<String> builtSimpleHashTableFromFile(String path) throws IOException{
		//��ʼ��������
		File dictionaryFile = new File(path);
		SeparateChainingHashTable<String> hashtable = new SeparateChainingHashTable<>();
		
		//ÿ���ļ��ж�һ�У���һ�����ʼ��������ע�⣩
		if(!dictionaryFile.isFile())
			throw new IllegalArgumentException("����Ĳ��������ļ�·��");
		
		BufferedReader bfr = new BufferedReader(new FileReader(dictionaryFile));
		String oneLine;
		while(( oneLine = bfr.readLine()) != null) {
			hashtable.insert(oneLine.split(" ")[0]);
		}
		bfr.close();
		return hashtable;
	}
	
	/**
	 * ���ڴ���ͨ�����뽨����ɢ�б�������л������ٵ��Ա���
	 * @param savePath �洢·��
	 * @return �ɹ���true,ʧ����false;
	 * @throws IOException 
	 */
	public static boolean saveHashTable(String savePath,SeparateChainingHashTable<String> hashtable) throws IOException {
		File saveFile = new File(savePath);
		
		if(saveFile.exists()) {
			throw new IllegalArgumentException("File is existed!");
		}
		
		saveFile.createNewFile();
		ObjectOutputStream objout = new ObjectOutputStream(new FileOutputStream(saveFile));
		
		objout.writeObject(hashtable);
		
		objout.close();
		return true;
	}
	
	/**
	 * ��һ�ַ���������ѭ���Ƚϡ�˫ѭ����һ��һ���ȶ����������Ƿ�����Ҫ��ֻ��һ����ĸ��ͬ��
	 * ʱ�����ΪO��N^3��
	 * ��ʱ��11706ms
	 * @param path ԭʼ�ֵ��ļ���·��
	 * @return �����õ�hashMap
	 * @throws IOException
	 */
	public static Map<String,List<String>> getSmartAddOneCharHashMap1(String path) throws IOException{
		Map<String,List<String>> smartHashMap = new HashMap<>();
		Map<Integer,List<String>> wordByLength = builtWordByLengthHashMap(path);
		
		for(Map.Entry<Integer, List<String>> entry : wordByLength.entrySet()) {
			//��ȡ��ǰһ���ַ����ĵ����б�;�����ַ���
			List<String> nowWordList = entry.getValue();
			int wordCharNum = entry.getKey();
			
			//��ȡ��ǰ�ַ���+1��Ӧ�ĵ����б�
			List<String> nextWordList = wordByLength.get(wordCharNum + 1); 
			
			//������൥�ʵ����
			if(nextWordList == null) {
				continue;
			}
			else {
				//˫ѭ������һ�ԶԵ��Ӽ���Ƿ�����Ҫ��
				for(int i = 0;i < nowWordList.size();++i) {
					String shortWord = nowWordList.get(i);
					for(int j = 0;j < nextWordList.size();++j) {
						String longWord = nextWordList.get(j);
						if(isOnlyAddOneChar(shortWord , longWord)) {
							updateMap(smartHashMap, shortWord, longWord);
						}
					}
				}
			}
			
			
		}
		
		return smartHashMap;
	}
	
	/**
	 * �ڶ��ַ��������ٴӶ̵������֣������Ƿ�������Ҫ��ĳ����ʣ�
	 * ���Ǵӳ��������֣�ȥ����һ��ĸ��ȥ�ַ���-1��������ң��Ƿ�����ȵĵ��ʣ�������뵽map��
	 * ʱ�����O��N^3��
	 * ��ʱ��1593ms
	 * ��~ o(*������*)o��������ô���£��Բۣ��������ǲ���ԭ������7��
	 * �Ҵ�ſ�����������Ϊ�ڵ�һ�ַ�����subString����ִ����2 * ���η��Σ����ڶ��ַ���subString����ִֻ����2 * ���η��Σ�
	 * �ҹ���subString������������ʱ��
	 * @param path ԭʼ�ֵ��ļ���·��
	 * @return �����õ�hashMap
	 * @throws IOException
	 */
	public static Map<String,List<String>> getSmartAddOneCharHashMap2(String path) throws IOException{
		Map<String,List<String>> smartHashMap = new HashMap<>();
		Map<Integer,List<String>> wordByLength = builtWordByLengthHashMap(path);
		
		for(Map.Entry<Integer, List<String>> entry : wordByLength.entrySet()) {
			//��ȡ��ǰһ���ַ����ĵ����б�;�����ַ���
			List<String> nowWordList = entry.getValue();
			int wordCharNum = entry.getKey();
			
			//��ȡ��ǰ�ַ���-1��Ӧ�ĵ����б�
			List<String> nextWordList = wordByLength.get(wordCharNum - 1); 
			//��nowWordListΪ�����ַ���Ϊ1�ĵ�������ʱ����-1��Ӧ��Ϊ�ַ���Ϊ0�ĵ������������ڣ�����java.lang.NullPoiinterException
			if(nextWordList == null) {
				continue;
			}
			else {
				for(int i = 0;i < nowWordList.size();++i) {
					String longWord = nowWordList.get(i);
					for(int j = 0;j < longWord.length();++j) {
						String subOneChar = longWord.substring(0,j) + longWord.substring(j+1);
						for(int k = 0;k < nextWordList.size();++k) {
							String shortWord = nextWordList.get(k);
							if(subOneChar.equals(shortWord)) {
								updateMap(smartHashMap, shortWord, longWord);
							}
						}
					}
				}
			}
		}
		
		return smartHashMap;
	}
	
	/**
	 * ��ȡ�ֵ�ԭʼ�ļ�������һ��������ĸ�ַ����ȵ�HashMap
	 * @param path �ֵ��ļ���·��
	 * @return �����ַ��������map����Ϊ�ַ�����ֵΪ�ַ���Ϊ��ֵ�ĵ����б�
	 * @throws IOException
	 */
	public static Map<Integer,List<String>> builtWordByLengthHashMap(String path) throws IOException {
		File dictionaryFile = new File(path);
		Map<Integer,List<String>> wordByLength = new HashMap<>();
		
		//ÿ���ļ��ж�һ�У���һ�����ʼ��������ע�⣩
		if(!dictionaryFile.isFile())
			throw new IllegalArgumentException("����Ĳ��������ļ�·��");
		
		BufferedReader bfr = new BufferedReader(new FileReader(dictionaryFile));
		String oneLine;
		while(( oneLine = bfr.readLine()) != null) {
			String oneWord = oneLine.split(" ")[0];
			updateMap(wordByLength, oneWord.length(), oneWord);
		}
		bfr.close();
		return wordByLength;
	}
	
	/**
	 * ��̬����������ֵ����ΪList<String>��hashMap��������Ӧ����
	 * ���map.get�����õ��Ķ���Ϊnull,���½�һ��ArrayList����
	 * 
	 * @param hashMap ��Ҫ���µ�hashMap��ֵ���ͱ���Ϊ{@code List<String>}
	 * @param key ��Ҫ��ӵ�map�е��������͵ļ�
	 * @param value ��Ҫ��ӵ�map�е�����ΪList<String>�ļ�
	 */
	private static <KeyType> void updateMap(Map<KeyType,List<String>> hashMap,KeyType key,String value) {
		List<String> list = hashMap.get(key);
		if(list == null) {
			list = new ArrayList<String>();
			hashMap.put(key, list);
		}
		list.add(value);
	}
	
	/**
	 * ѭ��ȥ���ϳ���ĸ��һ����ĸ���ٺͶ̵Ľ��бȶ�
	 * ʱ�����Ϊ����
	 * @param shortWord �ַ������ٵĵ���
	 * @param longWord �ַ����϶�ĵ���
	 * @return ����������true,��������false
	 * @throws java.lang.IllegalArgumentException �������κ�һ��Ϊnull���׳��쳣
	 */
	private static boolean isOnlyAddOneChar(String shortWord,String longWord) {
		if(shortWord == null || longWord == null)
			throw new IllegalArgumentException("���������������null!");
		
		for(int i = 0;i < longWord.length();++i) {
			if((longWord.substring(0, i) + longWord.substring(i + 1)).equals(shortWord))
				return true;
		}
		return false;
	}
	
	
	
}
