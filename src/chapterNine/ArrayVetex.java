package chapterNine;

import chapterThree.MyLinkedList;

/**
 * 
 * 这次图邻接表表示法采用MyArrayList提到的改进型想法
 * 
 * 总结来说这次数据结构的基本原来如下：
 * 1.取消节点类，不通过节点来保存对象。而是利于每个节点拓扑编号就是对应的索引值。临界点的数据只存储临界点的拓扑编号以及对应的索引值
 * 2.不再使用MyArrayList集合类来组织数据，太麻烦了，而且有点浪费空间。直接使用数组来组织。
 * 	有个问题是，图的扩展性受到限制，即一旦图被构造，难以被修改或者说修改的代价很大。
 * 	好处是，编程简单直接。
 * 
 * 使用这种方式构造一个图出来，限制是在图的构造阶段，也就是调用它的构造函数过程中必须全部把顶点载入，后续建立连接的时候不允许出现新的顶点
 * 
 * 最近的改进型想法：
 * 1. 使用hashMap和LinkedList来存储邻接表，进一步提高性能。
 * 
 * @author 25040
 *
 * @param <T>
 */
public class ArrayVetex<T> {
	
	public static void main(String[] args) {
		ArrayVetex<String> graph = new ArrayVetex<>(new String[] {"v1","v2","v3","v4","v5","v6","v7"});
		
		graph.builtEdgeWithOneVetexAndSomeOtherVetex("v1", new String[] {"v2", "v4"});
		graph.builtEdgeWithOneVetexAndSomeOtherVetex("v2", new String[] {"v4", "v5"});
		graph.builtEdgeWithOneVetexAndSomeOtherVetex("v3", new String[] {"v1", "v6"});
		graph.builtEdgeWithOneVetexAndSomeOtherVetex("v4", new String[] {"v3", "v5", "v6", "v7"});
		graph.builtEdgeWithOneVetexAndSomeOtherVetex("v5", new String[] {"v7"});
		graph.builtEdgeWithOneVetexAndSomeOtherVetex("v6", new String[] {});
		graph.builtEdgeWithOneVetexAndSomeOtherVetex("v7", new String[] {"v6"});
		
		graph.printGraph();
	}
	
	/*
	 * 属性。存储邻接表数组
	 */
	private OneWeightedAdjacencyList<T>[] adjecenyLists;
	
	
	/**
	 * 使用所有的顶点关键字构造所有的邻接表。
	 * 图在构造的过程中，不会查重，哪怕两个顶点的关键字相同 ，也是两个不同的顶点。
	 * 顶点不同的唯一依据是其数组索引值。
	 * 切记不能传入重复的元素，否则要出逻辑错误。
	 * 
	 * 注意点：
	 * 1.this.adjecenyLists =  new OneWeightedAdjacencyList[allVetexElements.length];
	 * 而不能使this.adjecenyLists =  (OneWeightedAdjacencyList<T>[])new Object[allVetexElements.length];
	 * 抛出java.lang.ClassCastException
	 * 
	 * @param AllVetexElements 所有的顶点关键字
	 */
	@SuppressWarnings("unchecked")
	public ArrayVetex(T[] allVetexElements) {
		//
		this.adjecenyLists =  new OneWeightedAdjacencyList[allVetexElements.length];
		//变量顶点关键字数组，依次构建对应的邻接表
		for(int i = 0;i < allVetexElements.length; ++i) {
			//查重，防止两个点拥有两个相同的关键字。可以考虑使用map来构建
			//getIndexByVetexElement(allVetexElements[i], 0, i - 1);
			//每个顶点构建一个邻接表
			this.adjecenyLists[i] = new OneWeightedAdjacencyList<>(allVetexElements[i]);
		}
	}
	
	/**
	 * 在两个顶点之间建立一条有权值的边。
	 * 由于需要扫描数组以获得下标，所以扫描需要最坏需要V（顶点个数）次。该方法最坏需要2 * V次，线性。
	 * @param selfVetexElement (u,v)点对的u，即边的起始
	 * @param otherVetexElement (u,v)点对的v，即边的终止
	 * @param weight (u,v)边的权值
	 */
	public void builtWeightedEdgeWithOneVetexAndOtherOneVetex(T selfVetexElement, T otherVetexElement, int weight ) {
		OneWeightedAdjacencyList<T> whichVetex = this.adjecenyLists[getIndexByVetexElement(selfVetexElement)];
		whichVetex.builtEdgeWithOtherVetex(getIndexByVetexElement(otherVetexElement), weight);
	}
	
	/**
	 * 在两个顶点之间建立一条权值为1的边。线性
	 * @param selfVetexElement
	 * @param otherVetexElement
	 */
	public void builtEdgeWithOneVetexAndOtherOneVetex(T selfVetexElement, T otherVetexElement) {
		builtWeightedEdgeWithOneVetexAndOtherOneVetex(selfVetexElement, otherVetexElement, 1);
	}
	
	/**
	 * 使用一个顶点和其他多个顶点建立无权值的边。时间界限为V的二次方
	 * @param selfVetexElement 多条有向边的起点	
	 * @param otherVetexElements 其他顶点，即多条有向边的终点构成的数组
	 */
	public void builtEdgeWithOneVetexAndSomeOtherVetex(T selfVetexElement, T[] otherVetexElements) {
		OneWeightedAdjacencyList<T> whichVetex = this.adjecenyLists[getIndexByVetexElement(selfVetexElement)];
		
		for(int i = 0; i < otherVetexElements.length; ++i) {
			whichVetex.builtEdgeWithOtherVetex(getIndexByVetexElement(otherVetexElements[i]), 1);
		}
	}
	
	/**
	 * 返回图中顶点的个数
	 * @return
	 */
	public int size() {
		return this.adjecenyLists.length;
	}
	
	/**
	 * 在全部顶点数组范围内依次比对所有顶点的关键字，返回顶点的数组索引（或者叫下标，或者说拓扑编号）.
	 * 不存在则抛出异常。
	 * @param vetexElement
	 * @return
	 */
	public int getIndexByVetexElement(T vetexElement) {
		return getIndexByVetexElement(vetexElement, 0, size() - 1);
	}
	
	/**
	 * 遍历一定范围内邻接表数组，依次比对所有顶点的关键字，返回顶点的数组索引（或者叫下标，或者说拓扑编号）.
	 * 如果不存在则返回-1；
	 * 
	 * 同时，它还进行了查重。其实可以考虑使用map
	 * 
	 * 时间界限为线性。
	 * 
	 * @param vetexElement
	 */
	private int getIndexByVetexElement(T vetexElement, int leftBorder, int rightBorder) {
		int index;
		
		for(index = leftBorder;index <= rightBorder ;++index) {
			if(this.adjecenyLists[index].getVetexElement().equals(vetexElement)) {
				break;
			}
		}
		
		if(index == rightBorder + 1) {
			throw new IllegalArgumentException("该图所有顶点中不存在该顶点");
		}
		else {
			return index;
		}
	}
	
	/**
	 * 图邻接表的格式化输出。
	 */
	public void printGraph() {
		for (OneWeightedAdjacencyList<T> oneWeightedAdjacencyList : adjecenyLists) {
			System.out.println(oneWeightedAdjacencyList);
		}
	}
	
	/**
	 * @since 2018-3-5 
	 * 有向无圈图的最短路径计算.
	 * 估计得使用方法内部类来计算最短路径。
	 * 妈的，有点懵逼。
	 * @param startVetex 起点对应的拓扑编号
	 */
	public void breadthFirstSearch(T startVetex) {
		table[] nodeTable = new table[size()];
		
		for(int i = 0;i < nodeTable.length; ++i) {
			nodeTable[i] = new table(i, false, Integer.MAX_VALUE, 0);
		}
		
		nodeTable[getIndexByVetexElement(startVetex)].dv = 0; 
		
		for(int currDist = 0;currDist < size();++currDist) {
			for (table vetex : nodeTable) {
				if( !vetex.known && vetex.dv == currDist) {
					vetex.known = true;
					for(chapterNine.ArrayVetex.OneWeightedAdjacencyList.Edge edge : this.adjecenyLists[vetex.VetexNum].getAdjacencyEdges()) {
						table adjecenyVetex = nodeTable[edge.adjacencyVetex];
						if(adjecenyVetex.dv == Integer.MAX_VALUE) {
							adjecenyVetex.dv = currDist + 1;
							adjecenyVetex.pv = vetex.VetexNum;
						}
					}
				}
			}
		}	
	}
	
	/**
	 * 使用队列来提高上述代码的运行效率。
	 * @param startVetex 起点
	 */
	public void breadthFirstSearchByQueue(T startVetex) {
		
	}
	
	/**
	 * 用于计算最短路径得表
	 * 
	 * @author 25040
	 *
	 */
	private static class table{
		int VetexNum; //顶点的编号
		boolean known = false;//在广度优先搜索的过程是否已经被找到的标志
		int dv = Integer.MAX_VALUE;//记录起点到该点的路径长
		int pv;//
		
		public table(int vetexNum, boolean known, int dv, int pv) {
			super();
			VetexNum = vetexNum;
			this.known = known;
			this.dv = dv;
			this.pv = pv;
		}
	}
	
	/**
	 * 邻接表对象。
	 * 
	 * @author 25040
	 *
	 * @param <T>
	 */
	private static class OneWeightedAdjacencyList<T> {
		
		private static int VETEX_COUNTER = 0;
		private int topNum;
		private T element;
		private MyLinkedList<Edge> adjacencyEdges;
		
		public OneWeightedAdjacencyList(T elements) {
			this.topNum = VETEX_COUNTER++;
			this.element = elements;
			this.adjacencyEdges = new MyLinkedList<>();
		}
		
		public T getVetexElement() {
			return element;
		}
		
		
		public MyLinkedList<Edge> getAdjacencyEdges() {
			return adjacencyEdges;
		}

		/**
		 * 建立当前顶点和其他顶点的边，并且赋权值。
		 * 时间界限为：常数
		 * 
		 * @param otherVetexIndex
		 * @param edgeWeight
		 */
		public void builtEdgeWithOtherVetex(int otherVetexIndex, int edgeWeight) {
			this.adjacencyEdges.add(new Edge(otherVetexIndex, edgeWeight));
		}
		
		/**
		 * 邻接表的格式化输出。
		 * @return 格式化字符串
		 */
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("[");
			
			sb.append(this.topNum).append("-> ");
			for (Edge edge : adjacencyEdges) {
				sb.append(edge.adjacencyVetex).append(", ");
				//sb.append("(权:").append(edge.getWeight()).append(")").append("->");
			}
			
			sb.append("]");
			return sb.toString();
		} 
		
		/**
		 * 边对象，具体表现为一个带有箭头的边。
		 * 包含两个属性，箭头指向顶点的索引（数组下标，顶点对象其实内化与邻接表对象中，其编号就是其对应的下标，
		 * 邻接表对象的element属性相当于顶点的内容）
		 * 
		 * @author 25040
		 *
		 */
		private static class Edge {
			//临界点编号或者说数组索引
			private int adjacencyVetex;
			//该边的权
			private int weight;
			
			/**
			 * 边的构造函数，输入箭头指向的那个顶点，以及权值，必须和邻接表对象混合使用
			 * @param adjacencyVetex
			 * @param weight
			 */
			public Edge(int adjacencyVetex, int weight) {
				this.adjacencyVetex = adjacencyVetex;
				this.weight = weight;
			}

			public int getAdjacencyVetex() {
				return adjacencyVetex;
			}

			public void setAdjacencyVetex(int adjacencyVetex) {
				this.adjacencyVetex = adjacencyVetex;
			}

			public int getWeight() {
				return weight;
			}

			public void setWeight(int weight) {
				this.weight = weight;
			}
		}
	}
}
