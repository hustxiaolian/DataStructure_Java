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
	 * 根据传入的Map，我们构造一张图也是一个map，map中某个entry的key代表着当前的单词，
	 * 而value则是指向了下一个单词（即词梯的下一个单词）。
	 * 它这种构成图的方式尤其值得学习。由此是通过Map<String,String>这种方式来模拟那张表来生成路径实在是妙。
	 * 
	 * @param adjcacentWords key:某个单词，value:key单词改变一个字母能够得到的所有单词。
	 * @param first 		 词梯游戏的首个单词
	 * @param second         词梯游戏的第二个单词
	 * @return
	 */
	public static List<String> findChain(Map<String,List<String>> adjcacentWords, String first, String second){
		Map<String,String> previousWord = new HashMap<>();
		LinkedList<String> q = new LinkedList<>();
		//这里感觉有点把LinkedList当成队列使用，实际上就是。
		q.addLast(first);
		while(! q.isEmpty()) {
			//取出
			String current = q.removeFirst();
			//获得当前节点的邻接点的列表（即所有的邻接点）
			List<String> adj = adjcacentWords.get(current);
			//如果当前节点的出度为零，跳过。即它不能通过一个字母获得其他单词。
			if(adj != null) {
				//遍历所有的邻接点
				for(String adjWord : adj) {
					//在这是用过这种方式来判断这点遍历的邻接点是否已经known，如果know了直接跳过
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
