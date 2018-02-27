package chapterFour;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/**
 *输入大量的string单词，要求输入任意集合内的单词，输出与该单词只相差的一个字母的单词有哪些
 *下面是三种算法，一次越来越高级
 *
 * @author 25040
 *
 */
public class MapTreeDemo {

	public static void main(String[] args) {

	}
	
	public static void printHighChangeables(Map<String,List<String>> adjWords,int minWords) {
		for(Map.Entry<String, List<String>> entry:adjWords.entrySet()) {
			List<String> words = entry.getValue();
			
			if(words.size() >= minWords) {
				System.out.print(entry.getKey() + "(");
				System.out.print(words.size() + "):");
				for(String w:words) {
					System.out.print(" " + w);
				}
				System.out.println();
			}
		}
	}
	
	private static boolean oneCharOff(String word1,String word2) {
		if(word1.length() != word2.length()) {
			return false;
		}
		
		int diffs = 0;
		
		for(int i = 0;i < word1.length();++i) {
			if(word1.charAt(i) != word2.charAt(i)) {
				if(++diffs > 1)
					return false;
			}
		}
		
		return diffs == 1;
	}
	/**
	 * 以单词集合List作为输入，在方法中构造map，建立相关的映射关系
	 * 在方法中调用oneCharOff方法蛮力测试单词之间的序偶关系
	 * 一个一个单词互相比较，满足条件的，
	 * @param theWords
	 * @return
	 */
	public static Map<String,List<String>> computeAdjacentWords1(List<String> theWords){
		Map<String,List<String>> adjWords = new TreeMap<>();
		
		String[] words = new String[theWords.size()];
		
		theWords.toArray();
		for(int i = 0;i < words.length;++i) {
			for(int j = i + 1;j < words.length;++j) {
				if(oneCharOff(words[i], words[j])) {
					//建立相互的映射关系
					update(adjWords,words[i],words[j]);
					update(adjWords,words[j],words[i]);
				}
			} 
		}
		
		return adjWords;
	}
	
	private static <keyType> void update(Map<keyType,List<String>> m, keyType key , String value) {
		List<String> lst = m.get(key);
		if(lst == null) {
			lst = new ArrayList<>();
			m.put(key, lst);
		}
		
		lst.add(value);
	}
	
	/**
	 * 该算法是对上面蛮力测试算法的改进，从实际中，我们可以知道，可以避免测试不同长度的单词，即将不同长度的单词进行分组，
	 * 再应用上面的算法来对每个组进行测试
	 * @param theWords
	 * @return
	 */
	public static Map<String,List<String>> computeAdjacentWords2(List<String> theWords){
		Map<String,List<String>> adjWords = new TreeMap<>();
		Map<Integer,List<String>> wordsByLength = new TreeMap<>();
		
		//首先按照单词长度进行分类添加,对整个单词集合遍历一遍，然后按照不同长度
		for (String word : theWords) {
			update(wordsByLength,word.length(),word);
		}
		
		//对每个不同长度的集合内部，再进行两两比较
		for(List<String> groupWords:wordsByLength.values()) {
			String[] words = new String [groupWords.size()];
			//把list，变成数组填充到指定的数组中
			groupWords.toArray(words);
			
			for(int i = 0;i < words.length;++i) {
				for(int j = i + 1;j < words.length;++j) {
					if(oneCharOff(words[i], words[j])) {
						update(adjWords,words[i],words[j]);
						update(adjWords,words[j],words[i]);
					}
				}
			}
		}
		
		
		return adjWords;
	}
	
	public static Map<String,List<String>> computeAdjacentWords3(List<String> theWords){
		Map<String,List<String>> adjWords = new TreeMap<>();
		Map<Integer,List<String>> wordsByLength = new TreeMap<>();
		
		//首先按照单词长度进行分类添加,对整个单词集合遍历一遍，然后按照不同长度
		for (String word : theWords) {
			update(wordsByLength,word.length(),word);
		}
		
		for(Map.Entry<Integer, List<String>> entry:wordsByLength.entrySet()) {
			List<String> groupWords = entry.getValue();
			int groupNum = entry.getKey();
			
			for(int i = 0;i < groupNum;++i) {
				//前面的String为单词节选次，比如fine和nine的节选词为ine
				Map<String,List<String>> repToWord = new TreeMap<>();
				
				for(String str:groupWords) {
					//注意到，是0-i以及i+1之后，即中间i对应的字母被去掉了，依次来生成只差一个字母的映射
					String rep = str.substring(0,i) + str.substring(i + 1);
					update(repToWord,rep,str);
				}
				
				for(List<String> wordClique:repToWord.values()) {
					//确保有有该节选词映射的单词集合有两个以上，在list内部，即一个rep节选片段除了它自己，还有其它单词
					if(wordClique.size() >= 2) {
						//对该list遍历，如果单词不等，则添加映射，又因为对每个单词都遍历了一次，所有在最内层只有一次uodate
						for(String s1:wordClique) {
							for(String s2:wordClique) {
								if(s1 != s2)
									update(adjWords,s1,s2);
							}
						}
					}
				}
			}
		}
		
		return adjWords;
	}
}