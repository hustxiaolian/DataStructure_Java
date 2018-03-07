package chapterNine;

import chapterFive.MyHashMap;
import chapterThree.MyLinkedList;

/**
 * 
 * 这次数据结构上的改进使用在ArrayVetex邻接表表示法中的改进想法：
 * 1. 不再使用数组结构来组织多个点的邻接表了。
 * 2. 改为使用HashMap + LinKedList来组织所有的顶点数据以及顶点之间的邻接顺序。
 * 
 * 选用这两种的组合时基于以下一个重要的理由：
 * 1. 每个顶点的邻接点个数数目在实际情况中一定不会多，也就是linkedList的size会是一个小常数。
 * 所以我们可以任何linkedList使用get，contain等遍历链表的线性方法，在这里可以看成是常数操作。
 * 
 * @version v1.0 无权图版本
 * 后期我再想想怎么更好的组织，融入有权版本，估计还是得新建一个内部私有类(类似ArrayVerex.Edge类)来实现。
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
	
	//属性存储一个HashMap
	private MyHashMap<T, OneVetexAdjacencyList<T>> map;
	private int currentSize;
	
	/**
	 * 构造
	 * @param 图的当前
	 */
	public HashMapAdjacencyList(int vetexNum) {
		this.map = new MyHashMap<>(vetexNum);
		this.currentSize = 0;
	}
	
	/**
	 * 清空邻接表
	 */
	public void makeEmpty() {
		this.currentSize = 0;
		map.makeEmpty();
	}
	
	/**
	 * 建立两个顶点的连接。
	 * 注意时有向的，前一个参数代表的顶点->后一个参数代表的顶点
	 * 
	 * @param vetex
	 * @param otherVetex
	 */
	public void bulitAdjacency(T selfVetex, T otherVetex) {
		//调用map的put方法，如果self顶点不存在，则新建邻接表加入到map中，返回true，否则如果self顶点存在，不作操作，返回false
		if(this.map.put(selfVetex, new OneVetexAdjacencyList<>(selfVetex))) {
			++this.currentSize;
		}
		OneVetexAdjacencyList<T> adjacencyList = this.map.get(selfVetex);
		
		adjacencyList.insertAdjacencyVetex(otherVetex);
		
	}
	
	/**
	 * 建立当前self顶点与其他多个顶点的邻接关系
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
	 * 格式化输出
	 */
	public String toString() {
		return this.map.toString();
	}
	
	/**
	 * 返回当前图中有多个少顶点，即有多少个邻接表
	 * @return
	 */
	public int size() {
		return this.currentSize;
	}
	
	/**
	 * 单独一个邻接表的类
	 * 
	 * @author 25040
	 *
	 * @param <T>
	 */
	public static class OneVetexAdjacencyList<T>{
		//邻接表的自身顶点的属性
		private T self;
		//邻接表中右self指向的所有邻接点,使用HashMap后，我们就不需要跟早
		private MyLinkedList<T> otherAdjacencyVetexs;
		
		/**
		 * 构造一个邻接表。
		 */
		public OneVetexAdjacencyList(T self) {
			this.self = self;
			this.otherAdjacencyVetexs = new MyLinkedList<>();
		}
		
		/**
		 * 返回当前邻接点的当前顶点
		 * @return
		 */
		public T getSelfVetex() {
			return this.self;
		}
		
		/**
		 * 检查当前顶点的邻接点是否为空
		 * @return
		 */
		public boolean hasAdjacencyVetex() {
			return this.otherAdjacencyVetexs.isEmpty();
		}
		
		/**
		 * 获取当前顶点的有几个邻接点，即当前顶点的邻接点数目
		 * @return
		 */
		public int adjacencyVetexNum() {
			return this.otherAdjacencyVetexs.size();
		}
		
		/**
		 * 在当前顶点的邻接表中插入到邻接点
		 * @param otherVetex
		 */
		public void insertAdjacencyVetex(T otherVetex) {
			this.otherAdjacencyVetexs.addLast(otherVetex);
		}
		
		/**
		 * 在当前顶点的邻接表中插入多个邻接点
		 * @param otherVetexs
		 */
		public void insertAdjacencyVetex(T[] otherVetexs) {
			for(int i = 0;i < otherVetexs.length; ++i) {
				insertAdjacencyVetex(otherVetexs[i]);
			}
		}
		
		/**
		 * 格式化输出当前顶点的邻接表
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
