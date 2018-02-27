package chapterFour;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/**
 *���������string���ʣ�Ҫ���������⼯���ڵĵ��ʣ������õ���ֻ����һ����ĸ�ĵ�������Щ
 *�����������㷨��һ��Խ��Խ�߼�
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
	 * �Ե��ʼ���List��Ϊ���룬�ڷ����й���map��������ص�ӳ���ϵ
	 * �ڷ����е���oneCharOff�����������Ե���֮�����ż��ϵ
	 * һ��һ�����ʻ���Ƚϣ����������ģ�
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
					//�����໥��ӳ���ϵ
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
	 * ���㷨�Ƕ��������������㷨�ĸĽ�����ʵ���У����ǿ���֪�������Ա�����Բ�ͬ���ȵĵ��ʣ�������ͬ���ȵĵ��ʽ��з��飬
	 * ��Ӧ��������㷨����ÿ������в���
	 * @param theWords
	 * @return
	 */
	public static Map<String,List<String>> computeAdjacentWords2(List<String> theWords){
		Map<String,List<String>> adjWords = new TreeMap<>();
		Map<Integer,List<String>> wordsByLength = new TreeMap<>();
		
		//���Ȱ��յ��ʳ��Ƚ��з������,���������ʼ��ϱ���һ�飬Ȼ���ղ�ͬ����
		for (String word : theWords) {
			update(wordsByLength,word.length(),word);
		}
		
		//��ÿ����ͬ���ȵļ����ڲ����ٽ��������Ƚ�
		for(List<String> groupWords:wordsByLength.values()) {
			String[] words = new String [groupWords.size()];
			//��list�����������䵽ָ����������
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
		
		//���Ȱ��յ��ʳ��Ƚ��з������,���������ʼ��ϱ���һ�飬Ȼ���ղ�ͬ����
		for (String word : theWords) {
			update(wordsByLength,word.length(),word);
		}
		
		for(Map.Entry<Integer, List<String>> entry:wordsByLength.entrySet()) {
			List<String> groupWords = entry.getValue();
			int groupNum = entry.getKey();
			
			for(int i = 0;i < groupNum;++i) {
				//ǰ���StringΪ���ʽ�ѡ�Σ�����fine��nine�Ľ�ѡ��Ϊine
				Map<String,List<String>> repToWord = new TreeMap<>();
				
				for(String str:groupWords) {
					//ע�⵽����0-i�Լ�i+1֮�󣬼��м�i��Ӧ����ĸ��ȥ���ˣ�����������ֻ��һ����ĸ��ӳ��
					String rep = str.substring(0,i) + str.substring(i + 1);
					update(repToWord,rep,str);
				}
				
				for(List<String> wordClique:repToWord.values()) {
					//ȷ�����иý�ѡ��ӳ��ĵ��ʼ������������ϣ���list�ڲ�����һ��rep��ѡƬ�γ������Լ���������������
					if(wordClique.size() >= 2) {
						//�Ը�list������������ʲ��ȣ������ӳ�䣬����Ϊ��ÿ�����ʶ�������һ�Σ����������ڲ�ֻ��һ��uodate
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