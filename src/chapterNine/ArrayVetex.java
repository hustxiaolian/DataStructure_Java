package chapterNine;

import chapterThree.MyArrayQueue;
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
		
		System.out.println("----------------------------------");
		
		graph.printBreadthFirstSearchPath("v3");
		
	}
	
	/*
	 * 属性。存储邻接表数组
	 * 以及广度优先搜索的结果
	 */
	private OneWeightedAdjacencyList<T>[] adjecenyLists;
	private table[] breadthFirstSearchResult;
	
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
	 * 
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
	 * 
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
	
	
	public T getVetexElementByIndex(int vetexNum) {
		return this.adjecenyLists[vetexNum].getVetexElement();
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
	 * @since 2018-3-6
	 * 来，今天我就跟你好好较量下，理解完你之后，我再去干红黑树。
	 * 先理解下书上的基本思路。
	 * 1.我先定义一个外层for循环，定义一个临时路径长度，用来迭代遍历距离为0-最远距离的所有顶点。for循环的终止条件，我们可以设置为
	 * 		一个图的可能出现的最大路径长就是顶点的个数，这时候图的样式就是个链表。
	 * 2.判断当前距离下有哪些还没有置位known为true的顶点，对这些顶点for遍历它的邻接点，把这些临界点的距离属性改为
	 * 		当前currDist + 1的值，然后将每个邻接点的属性pv 改为 当前顶点的拓扑编号。
	 * 
	 * 由此，我们可以以广度优先搜索的方式遍历所有的距离的点。该算法的时间界限为V的二次方。相当低效。
	 * 主要原因处在，后面很多次外层循环的根本没有必要。但是为了能够处理最差的情况，又必须这样写。
	 * 在一般情况下，明明所有的顶点的known属性都是true，但是还是要进行循环检索判断。
	 * 
	 * 自己zz的改进想法；（当然还是使用队列好点）
	 * 除了使用队列外，我还有一个大胆的想法。使用一个计数器，也可以解决这个问题。起码能降低外层循环的次数。
	 * 但是如果图是链表那样的最坏情形，就还是没有丝毫改进。
	 * 
	 * 
	 * 行，理解了广度优先搜索的性质，再来考虑下，怎么显示出实际路径。书中是说通过pv变量的回溯即可。
	 * @param startVetex 起点对应的拓扑编号
	 */
	public void breadthFirstSearch(T startVetex) {
		table[] nodeTable = new table[size()];
		
		for(int i = 0;i < nodeTable.length; ++i) {
			nodeTable[i] = new table(i, false, Integer.MAX_VALUE, 0);
		}
		
		nodeTable[getIndexByVetexElement(startVetex)].dv = 0; 
		int knownVetex = 1;
		
endSearch:		
		//遍历所有可能出现距离的顶点，即广度优先搜索
		for(int currDist = 0;currDist < size();++currDist) {
			//遍历每个顶点，
			for (table vetex : nodeTable) {
				//检索判断所有的顶点的known属性是否是未知的和dv属性是当前检索的路径长
				if( !vetex.known && vetex.dv == currDist) {
					//置位当前顶点的known
					vetex.known = true;
					//遍历当前顶点的所有邻接点（也就是我这里的所有的边对象）
					for(chapterNine.ArrayVetex.OneWeightedAdjacencyList.Edge edge : this.adjecenyLists[vetex.VetexNum].getAdjacencyEdges()) {
						table adjecenyVetex = nodeTable[edge.adjacencyVetex];
						//判断当前顶点的邻接点没有被搜索过，置位过路径长，否则可能出现同一顶点经过两条路径。
						if(adjecenyVetex.dv == Integer.MAX_VALUE) {
							//邻接点的dv属性为当前距离+1
							adjecenyVetex.dv = currDist + 1;
							//薄记变量，这样邻接点可以记住从哪个顶点搜索路径过来的
							adjecenyVetex.pv = vetex.VetexNum;
							/*
							 * 加入计数器的改进部分。必须完全退出最外层for循环，使用带有标签的break;
							 */
							if(++knownVetex == size()) {
								break endSearch;
							}
						}
						
					}
				}
			}
		}
		//结果保存到类变量中
		this.breadthFirstSearchResult = nodeTable;
	}
	
	/**
	 * 计算并且打印深度优先搜索的结果，如果是还未进行搜索，则先进行搜索之后，再打印
	 * 
	 * 具体打印出具体的路径算法不复杂，但是有点不好表示，后期看能不能结合B树来打印显示。但是不同得是，这里没有半点排序，关键是那种结构，以及如果合理得打印显示。
	 * 能不能参考linux系统得tree方法。 
	 * 
	 * 这些循环遍历的get方法看得我心累，一个get就是一个线性扫描时间，好伤啊，下次一定使用map来改进。
	 * 
	 * @param startVetex
	 */
	public void printBreadthFirstSearchPath(T startVetex) {
		if(this.breadthFirstSearchResult == null) {
			breadthFirstSearch(startVetex);
			//breadthFirstSearchByQueue(startVetex);
		}
		
		for (table table : this.breadthFirstSearchResult) {
			if(getVetexElementByIndex(table.VetexNum).equals(startVetex))
				System.out.println("|\t" + getVetexElementByIndex(table.VetexNum) 
				+ "|\t" + table.known + "|\t" + table.dv + "|\t" + 0);
			System.out.println("|\t" + getVetexElementByIndex(table.VetexNum) 
			+ "|\t" + table.known + "|\t" + table.dv + "|\t" + getVetexElementByIndex(table.pv));
		}
		
		//计算实际可能的路径
		//估计得使用queue队列。
	}
	
	/**
	 * 使用队列来提高上述代码的运行效率。
	 * 
	 * 原理：
	 * 我们一开始只让距离currDist从队尾入队，接着再遍历其邻接点时，让邻接点（即距离为currDist + 1）从队尾入队。
	 * 所以一定是currDist的所有顶点全部处理完成后，再处理currDist + 1的顶点。即广度优先搜索的方式。
	 * 
	 * 该算法避免了第一种算法的多次无用的检测
	 * 
	 * @param startVetex 起点
	 */
	public void breadthFirstSearchByQueue(T startVetex) {
		MyArrayQueue<table> queue = new MyArrayQueue<>(10);
		table[] nodeTable = new table[size()];
		
		for(int i = 0;i < nodeTable.length; ++i) {
			nodeTable[i] = new table(i, false, Integer.MAX_VALUE, 0);
		}
		
		nodeTable[getIndexByVetexElement(startVetex)].dv = 0; 
		//起点入队
		queue.enqueue(nodeTable[getIndexByVetexElement(startVetex)]);
		//
		while(! queue.isEmpty()) {
			//出队提取顶点
			table vetexTable = queue.dequeue();
			//遍历该顶点得所有邻接点,因为Edge类是OneWeightedAdjcacencyList的内部类，所以必须写包 + 类的路径全称
			for(chapterNine.ArrayVetex.OneWeightedAdjacencyList.Edge edge : this.adjecenyLists[vetexTable.VetexNum].getAdjacencyEdges()) {
				//到这里内部操作跟第一种方法类似
				table adjacencyTable = nodeTable[edge.adjacencyVetex];
				if(adjacencyTable.dv == Integer.MAX_VALUE) {
					adjacencyTable.dv = vetexTable.dv + 1;
					adjacencyTable.pv = vetexTable.VetexNum;
					queue.enqueue(adjacencyTable);
				}
			}
		}
		this.breadthFirstSearchResult = nodeTable;
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
