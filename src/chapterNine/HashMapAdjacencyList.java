package chapterNine;

import chapterFive.MyHashMap;
import chapterThree.MyLinkedList;

/**
 * 
 * ������ݽṹ�ϵĸĽ�ʹ����ArrayVetex�ڽӱ��ʾ���еĸĽ��뷨��
 * 1. ����ʹ������ṹ����֯�������ڽӱ��ˡ�
 * 2. ��Ϊʹ��HashMap + LinKedList����֯���еĶ��������Լ�����֮����ڽ�˳��
 * 
 * ѡ�������ֵ����ʱ��������һ����Ҫ�����ɣ�
 * 1. ÿ��������ڽӵ������Ŀ��ʵ�������һ������࣬Ҳ����linkedList��size����һ��С������
 * �������ǿ����κ�linkedListʹ��get��contain�ȱ�����������Է�������������Կ����ǳ���������
 * 
 * @version v1.0 ��Ȩͼ�汾
 * ��������������ô���õ���֯��������Ȩ�汾�����ƻ��ǵ��½�һ���ڲ�˽����(����ArrayVerex.Edge��)��ʵ�֡�
 * 
 * @author 25040
 *
 */
public class HashMapAdjacencyList<T> {
	
	public static void main(String[] args) {
		HashMapAdjacencyList<String> graph = new HashMapAdjacencyList<>(7);
		graph.builtAdjacency("v1", new String[] {"v2","v3","v4"});
		graph.builtAdjacency("v2", new String[] {"v1","v3","v4"});
		
		System.out.println(graph.toString());
	}
	
	//���Դ洢һ��HashMap
	private MyHashMap<T, OneVetexAdjacencyList<T>> map;
	private int currentSize;
	
	/**
	 * ����
	 * @param ͼ�ĵ�ǰ
	 */
	public HashMapAdjacencyList(int vetexNum) {
		this.map = new MyHashMap<>(vetexNum);
		this.currentSize = 0;
	}
	
	/**
	 * ����ڽӱ�
	 */
	public void makeEmpty() {
		this.currentSize = 0;
		map.makeEmpty();
	}
	
	/**
	 * ����������������ӡ�
	 * ע��ʱ����ģ�ǰһ����������Ķ���->��һ����������Ķ���
	 * 
	 * @param vetex
	 * @param otherVetex
	 */
	public void bulitAdjacency(T selfVetex, T otherVetex) {
		//����map��put���������self���㲻���ڣ����½��ڽӱ���뵽map�У�����true���������self������ڣ���������������false
		if(this.map.put(selfVetex, new OneVetexAdjacencyList<>(selfVetex))) {
			++this.currentSize;
		}
		OneVetexAdjacencyList<T> adjacencyList = this.map.get(selfVetex);
		
		adjacencyList.insertAdjacencyVetex(otherVetex);
		
	}
	
	/**
	 * ������ǰself�������������������ڽӹ�ϵ
	 * 
	 * @param selfVetex
	 * @param otherVetexs
	 */
	public void builtAdjacency(T selfVetex, T[] otherVetexs) {
		if(this.map.put(selfVetex, new OneVetexAdjacencyList<>(selfVetex))) {
			++this.currentSize;
		}
		OneVetexAdjacencyList<T> adjacencyList = this.map.get(selfVetex);
		
		adjacencyList.insertAdjacencyVetex(otherVetexs);
	}
	
	/**
	 * ��ʽ�����
	 */
	public String toString() {
		return this.map.toString();
	}
	
	/**
	 * ���ص�ǰͼ���ж���ٶ��㣬���ж��ٸ��ڽӱ�
	 * @return
	 */
	public int size() {
		return this.currentSize;
	}
	
	/**
	 * ����һ���ڽӱ����
	 * 
	 * @author 25040
	 *
	 * @param <T>
	 */
	public static class OneVetexAdjacencyList<T>{
		//�ڽӱ�������������
		private T self;
		//�ڽӱ�����selfָ��������ڽӵ�,ʹ��HashMap�����ǾͲ���Ҫ����
		private MyLinkedList<T> otherAdjacencyVetexs;
		
		/**
		 * ����һ���ڽӱ�
		 */
		public OneVetexAdjacencyList(T self) {
			this.self = self;
			this.otherAdjacencyVetexs = new MyLinkedList<>();
		}
		
		/**
		 * ���ص�ǰ�ڽӵ�ĵ�ǰ����
		 * @return
		 */
		public T getSelfVetex() {
			return this.self;
		}
		
		/**
		 * ��鵱ǰ������ڽӵ��Ƿ�Ϊ��
		 * @return
		 */
		public boolean hasAdjacencyVetex() {
			return this.otherAdjacencyVetexs.isEmpty();
		}
		
		/**
		 * ��ȡ��ǰ������м����ڽӵ㣬����ǰ������ڽӵ���Ŀ
		 * @return
		 */
		public int adjacencyVetexNum() {
			return this.otherAdjacencyVetexs.size();
		}
		
		/**
		 * �ڵ�ǰ������ڽӱ��в��뵽�ڽӵ�
		 * @param otherVetex
		 */
		public void insertAdjacencyVetex(T otherVetex) {
			this.otherAdjacencyVetexs.addLast(otherVetex);
		}
		
		/**
		 * �ڵ�ǰ������ڽӱ��в������ڽӵ�
		 * @param otherVetexs
		 */
		public void insertAdjacencyVetex(T[] otherVetexs) {
			for(int i = 0;i < otherVetexs.length; ++i) {
				insertAdjacencyVetex(otherVetexs[i]);
			}
		}
		
		/**
		 * ��ʽ�������ǰ������ڽӱ�
		 */
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("[ ");
			
			sb.append("self:").append(getSelfVetex()).append("->(");
			for(int i = 0;i < adjacencyVetexNum();++i) {
				sb.append(this.otherAdjacencyVetexs.get(i)).append(", ");
			}
			
			sb.append(") ]");
			return sb.toString();
		}
	}
}
