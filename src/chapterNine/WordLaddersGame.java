package chapterNine;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import chapterFive.SpellCheck;

public class WordLaddersGame {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			System.out.println(findChain(SpellCheck.getSmartDiffOneCharHashMap(dicPath), "zero", "five"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static String dicPath = "D:\\javaCode\\DataStructure\\EnglishUTF-8noBOM.txt";
	
	/**
	 * ���ݴ����Map�����ǹ���һ��ͼҲ��һ��map��map��ĳ��entry��key�����ŵ�ǰ�ĵ��ʣ�
	 * ��value����ָ������һ�����ʣ������ݵ���һ�����ʣ���
	 * �����ֹ���ͼ�ķ�ʽ����ֵ��ѧϰ���ɴ���ͨ��Map<String,String>���ַ�ʽ��ģ�����ű�������·��ʵ�����
	 * 
	 * @param adjcacentWords key:ĳ�����ʣ�value:key���ʸı�һ����ĸ�ܹ��õ������е��ʡ�
	 * @param first 		 ������Ϸ���׸�����
	 * @param second         ������Ϸ�ĵڶ�������
	 * @return
	 */
	public static List<String> findChain(Map<String,List<String>> adjcacentWords, String first, String second){
		Map<String,String> previousWord = new HashMap<>();
		LinkedList<String> q = new LinkedList<>();
		//����о��е��LinkedList���ɶ���ʹ�ã�ʵ���Ͼ��ǡ�
		q.addLast(first);
		while(! q.isEmpty()) {
			//ȡ��
			String current = q.removeFirst();
			//��õ�ǰ�ڵ���ڽӵ���б������е��ڽӵ㣩
			List<String> adj = adjcacentWords.get(current);
			//�����ǰ�ڵ�ĳ���Ϊ�㣬��������������ͨ��һ����ĸ����������ʡ�
			if(adj != null) {
				//�������е��ڽӵ�
				for(String adjWord : adj) {
					//�������ù����ַ�ʽ���ж����������ڽӵ��Ƿ��Ѿ�known�����know��ֱ������
					if(previousWord.get(adjWord) == null) {
						previousWord.put(adjWord, current);
						q.addLast(adjWord);
					}
				}
			}
		}
		
		previousWord.put(first, null);
		
		return getChainFromPreviousMap( previousWord, first, second);
	}

	
	/**
	 * 
	 * @param prev
	 * @param first
	 * @param second
	 * @return
	 */
	public static List<String> getChainFromPreviousMap(Map<String, String> prev, String first, String second) {
		LinkedList<String> result = null;
		
		if(prev.get(second) != null) {
			result = new LinkedList<String>();
			for(String str = second;str != null;str = prev.get(str))
				result.addFirst(str);
		}
		
		return result;
		
	}
}
