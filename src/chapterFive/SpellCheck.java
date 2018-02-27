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
 * 书中第5.21题。散列表实现拼写检查程序，这里实现对一个英文TXT文件，进行检查，输出错误（即不在散列表中单词）
 * 由于原始的字典文件是utf-8编码的，本java文件采用了utf-8编码，因此使用BufferedReader类的readLine方法
 * 思路：
 * 1.首先，建立单词得散列表，而且这个散列表（分离链接法）还有点大，首先尝试能不能成功
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
	 * 该种方法只能完成简单的单词词库功能，必须输入完整的单词才行，不能满足实际使用需求
	 * 思路：
	 * 1. 使用java。io。file类打开英文单词文件库
	 * 2. 每读一个单词，就往散列表中存储一个映射
	 * 3. 读完所有单词，关闭io，返回散列表。
	 * 
	 * 测试用时:67ms
	 * 
	 * @param path 英文单词的路径
	 * @return
	 * @throws IOException 可能出现的io异常
	 */
	public static SeparateChainingHashTable<String> builtSimpleHashTableFromFile(String path) throws IOException{
		//初始化各变量
		File dictionaryFile = new File(path);
		SeparateChainingHashTable<String> hashtable = new SeparateChainingHashTable<>();
		
		//每次文件中读一行（即一个单词及其音标和注解）
		if(!dictionaryFile.isFile())
			throw new IllegalArgumentException("传入的参数不是文件路径");
		
		BufferedReader bfr = new BufferedReader(new FileReader(dictionaryFile));
		String oneLine;
		while(( oneLine = bfr.readLine()) != null) {
			hashtable.insert(oneLine.split(" ")[0]);
		}
		bfr.close();
		return hashtable;
	}
	
	/**
	 * 将内存中通过插入建立的散列表进行序列化储存再电脑本地
	 * @param savePath 存储路径
	 * @return 成功则true,失败则false;
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
	 * 第一种方法，暴力循环比较。双循环，一个一个比对两个单词是否满足要求（只有一个字母不同）
	 * 时间界限为O（N^3）
	 * 费时：11706ms
	 * @param path 原始字典文件的路径
	 * @return 建立好的hashMap
	 * @throws IOException
	 */
	public static Map<String,List<String>> getSmartAddOneCharHashMap1(String path) throws IOException{
		Map<String,List<String>> smartHashMap = new HashMap<>();
		Map<Integer,List<String>> wordByLength = builtWordByLengthHashMap(path);
		
		for(Map.Entry<Integer, List<String>> entry : wordByLength.entrySet()) {
			//获取当前一定字符数的单词列表和具体的字符数
			List<String> nowWordList = entry.getValue();
			int wordCharNum = entry.getKey();
			
			//获取当前字符数+1对应的单词列表
			List<String> nextWordList = wordByLength.get(wordCharNum + 1); 
			
			//遇到最多单词的情况
			if(nextWordList == null) {
				continue;
			}
			else {
				//双循环暴力一对对单子检测是否满足要求
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
	 * 第二种方法。不再从短单词入手，查找是否有满足要求的长单词，
	 * 而是从长单词入手，去掉任一字母，去字符数-1的链表查找，是否有相等的单词，有则加入到map中
	 * 时间界限O（N^3）
	 * 费时：1593ms
	 * 嗯~ o(*￣▽￣*)o，这是怎么回事！卧槽，明明都是差不多的原理，差了7倍
	 * 我大概看出来，是因为在第一种方法中subString方法执行了2 * 三次方次，而第二种方法subString方法只执行了2 * 二次方次，
	 * 我估计subString方法花费线性时间
	 * @param path 原始字典文件的路径
	 * @return 建立好的hashMap
	 * @throws IOException
	 */
	public static Map<String,List<String>> getSmartAddOneCharHashMap2(String path) throws IOException{
		Map<String,List<String>> smartHashMap = new HashMap<>();
		Map<Integer,List<String>> wordByLength = builtWordByLengthHashMap(path);
		
		for(Map.Entry<Integer, List<String>> entry : wordByLength.entrySet()) {
			//获取当前一定字符数的单词列表和具体的字符数
			List<String> nowWordList = entry.getValue();
			int wordCharNum = entry.getKey();
			
			//获取当前字符数-1对应的单词列表
			List<String> nextWordList = wordByLength.get(wordCharNum - 1); 
			//当nowWordList为单词字符数为1的单词链表时，再-1对应的为字符数为0的单词链表，不存在，避免java.lang.NullPoiinterException
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
	 * 读取字典原始文件，建立一个按照字母字符长度的HashMap
	 * @param path 字典文件的路径
	 * @return 按照字符数分类的map，键为字符数，值为字符数为键值的单子列表
	 * @throws IOException
	 */
	public static Map<Integer,List<String>> builtWordByLengthHashMap(String path) throws IOException {
		File dictionaryFile = new File(path);
		Map<Integer,List<String>> wordByLength = new HashMap<>();
		
		//每次文件中读一行（即一个单词及其音标和注解）
		if(!dictionaryFile.isFile())
			throw new IllegalArgumentException("传入的参数不是文件路径");
		
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
	 * 静态方法。用于值类型为List<String>的hashMap中新增相应的项
	 * 如果map.get方法得到的对象为null,则新建一个ArrayList集合
	 * 
	 * @param hashMap 需要更新的hashMap，值类型必须为{@code List<String>}
	 * @param key 需要添加到map中的任意类型的键
	 * @param value 需要添加到map中的类型为List<String>的键
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
	 * 循环去掉较长字母的一个字母来再和短的进行比对
	 * 时间界限为线性
	 * @param shortWord 字符数较少的单词
	 * @param longWord 字符数较多的单词
	 * @return 满足条件则true,不满足则false
	 * @throws java.lang.IllegalArgumentException 当两个任何一个为null，抛出异常
	 */
	private static boolean isOnlyAddOneChar(String shortWord,String longWord) {
		if(shortWord == null || longWord == null)
			throw new IllegalArgumentException("两个参数都必须非null!");
		
		for(int i = 0;i < longWord.length();++i) {
			if((longWord.substring(0, i) + longWord.substring(i + 1)).equals(shortWord))
				return true;
		}
		return false;
	}
	
	
	
}
