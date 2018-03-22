package chapterNine;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

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
 * @version v2.0 
 * 哎，还是老老实实的使用标准库中的hashmap吧，自己的虽然不错，但是我自己独立开发的hashmap功能实在是不健全，尤其是没有迭代器
 * 太伤了。
 * 
 * 
 * @author 25040
 *
 */
public class HashMapAdjacencyList<T> {
	
	public static void main(String[] args) {
		HashMapAdjacencyList<String> graph = new HashMapAdjacencyList<>(7);
		/*无权无圈图测试数据
		graph.builtAdjacency("v1", new String[] {"v2","v4"}, new int[] {1,1});
		graph.builtAdjacency("v2", new String[] {"v4","v5"}, new int[] {1,1});
		graph.builtAdjacency("v3", new String[] {"v1","v6"}, new int[] {1,1});
		graph.builtAdjacency("v4", new String[] {"v3","v5","v6","v7"}, new int[] {1,1,1,1});
		graph.builtAdjacency("v5", new String[] {"v7"}, new int[] {1});
		graph.builtAdjacency("v6", new String[] {}, new int[] {});
		graph.builtAdjacency("v7", new String[] {"v6"}, new int[] {1});
		*/
		graph.builtAdjacency("v1", new String[] {"v2","v4"}, new int[] {2,1});
		graph.builtAdjacency("v2", new String[] {"v4","v5"}, new int[] {3,10});
		graph.builtAdjacency("v3", new String[] {"v1","v6"}, new int[] {4,5});
		graph.builtAdjacency("v4", new String[] {"v3","v5","v6","v7"}, new int[] {2,2,8,4});
		graph.builtAdjacency("v5", new String[] {"v7"}, new int[] {6});
		graph.builtAdjacency("v6", new String[] {}, new int[] {});
		graph.builtAdjacency("v7", new String[] {"v6"}, new int[] {1});
		//System.out.println(graph.toString());
		//System.out.println(graph.breathFirstSearch("v3", "v7"));
		//System.out.println(graph.getShortestPathWithWeightedGraph("v3", "v5"));
		//System.out.println(graph.inDegree("v4"));
	}
	
	//属性存储一个HashMap
	private HashMap<T, OneVetexAdjacencyList<T>> map;
	private int currentSize;
	
	/**
	 * 构造
	 * @param 图的当前
	 */
	public HashMapAdjacencyList(int vetexNum) {
		this.map = new HashMap<>(vetexNum);
		this.currentSize = 0;
	}
	
	/**
	 * 清空邻接表
	 */
	public void makeEmpty() {
		this.currentSize = 0;
		map.clear();
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
		if(this.map.put(selfVetex, new OneVetexAdjacencyList<>(selfVetex)) != null) {
			++this.currentSize;
		}
		OneVetexAdjacencyList<T> adjacencyList = this.map.get(selfVetex);
		
		adjacencyList.insertAdjcancyVetex(otherVetex);
		
	}
	
	/**
	 * 建立当前self顶点与其他多个顶点的邻接关系
	 * 
	 * @param selfVetex
	 * @param otherVetexs
	 */
	public void builtAdjacency(T selfVetex, T[] otherVetexs, int[] allEdgeWeight) {
		if(this.map.put(selfVetex, new OneVetexAdjacencyList<>(selfVetex)) != null) {
			++this.currentSize;
		}
		OneVetexAdjacencyList<T> adjacencyList = this.map.get(selfVetex);

		adjacencyList.insertAdjacencyVetex(otherVetexs, allEdgeWeight);
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
	 * 无权无圈图的广度优先搜索求最短路径问题。
	 * 使用LinkedList和HashMap来完成。其中linkedList完成的更像是队列的工作。
	 * 
	 * 那么问题来了，为什么这样做，返回的是最短路径。
	 * 因为，其实理由和书上一样的。当我们添加currDist + 1的那些邻接点，更远的邻接点时，我们让它从队尾入队。
	 * 这样保证了一定是先处理完成了当前currDist的所有节点后，在处理更远的邻接点。而更早处理的点，更早占据map的位置，
	 * 我们自然可以认为它相对于后来者具有更短的路径。也就是书中已经known了。
	 * 
	 * 返回路径的边对象的链表。储存了最短路径存储了经过了那些节点，其权值的含义在这里变成了各节点到起点的间距。
	 * 如果没有路径能从起点到终点，返回null
	 * 
	 * @param startVetex
	 * @return
	 */
	public List<T> breathFirstSearch(T startVetex, T endVetex) {
		HashMap<T,T> pathMap = new HashMap<>();
		LinkedList<T> q = new LinkedList<>();
		//让起点入队。
		q.addLast(startVetex);
		while(! q.isEmpty()) {
			//获取当前的节点的邻接点的列表，并且从队列中删除它
			T nowNode = q.removeFirst();
			OneVetexAdjacencyList<T> adjList = this.map.get(nowNode);
			LinkedList<Edge<T>> edgeList = adjList.getAdjacencyVetexList();
			//遍历所有的邻接节点
			for (Edge<T> edge : edgeList) {
				T adjVetex = edge.getNextVetex();
				//判断当前节点是否已经known了，即已经被遍历过了
				if(pathMap.get(adjVetex) == null) {
					//known为false的节点,存储了节点路径。
					pathMap.put(adjVetex, nowNode);
					q.addLast(adjVetex);
				}
			}
		}
		
		pathMap.put(startVetex, null);
		
		return getPathFromMap(pathMap, startVetex, endVetex);
	}
	
	/**
	 * 从生成的map路径表中，返回正向的路径。
	 * @param pathMap
	 * @param startVetex
	 * @param endVetex
	 * @return
	 */
	private LinkedList<T> getPathFromMap(HashMap<T, T> pathMap, T startVetex, T endVetex) {
		LinkedList<T> result = null;
		
		if(!pathMap.isEmpty()) {
			result = new LinkedList<>();
			//从尾部开始
			for(T vetex = endVetex;vetex != null;vetex = pathMap.get(vetex))
				result.addFirst(vetex);
		}
		
		return result;
	}
	
	/**
	 * 使用广度优先搜索解决有权无圈图中的单源最短路径问题。莫名其妙相除的版本。
	 * 其实是我错误地理解书上的意思，误打误撞出来的。过程根本不贪，但结果是贪婪的。
	 * 
	 * 图中不能包含负值边。
	 * 
	 * 思路还是根广度优先搜索的思路一样，按照邻接点的层次层层往更远的节点扩展，直到覆盖所有节点。
	 * 该种算法是我根据书中的算法自己想的，其根书上有点不太一样，我目的是从起点出发遍历更新所有的边。
	 * 跟贪婪算法不太一样的地方在于，该算法不要求每轮都选择最短距离的未知的节点。而是跟广度优先搜索一样。遍历所有的边。
	 * known用来判断该点是否进入队列，即其所有的邻接点是否都被处理的一遍。
	 * 同时只要 邻接点的路径信息满足  pathMap.get(nowNode).dv + weight < adjVetexInfo.dv  就表示该邻接点可以由更短的路径过来。
	 * 因此更新其pv和dv值，不论该邻接点是否known。
	 * 因此分析的时间界限为O(E),跟顶点数无关
	 * 测试通过。
	 * 
	 * @param startVetex 输入节点
	 * @param endVetex 输出节点
	 * @return
	 */
	public List<Edge<T>> getShortestPathWithWeightedGraph(T startVetex, T endVetex){
		/**
		 * 方法内部类，仅用于贪婪算法的
		 * @author 25040
		 *
		 */
		class pathInfo{
			//标记节点是否被标记过了
			boolean known;
			//记录当前节点到起点的最短权值
			int dv;
			//用于最短路径的回溯
			T prevNode;
			
			public pathInfo() {
				this(false, Integer.MAX_VALUE, null);
			}
			
			public pathInfo(boolean known, int dv, T prevNode) {
				super();
				this.known = known;
				this.dv = dv;
				this.prevNode = prevNode;
			}
			
			public String toString() {
				return ("known:" + known + ", dv:" + dv + ", pv:" + prevNode + "\n");
			}
		}
		
		/*
		 * 下面正式开始进行搜索算法。 
		 */
		
		//变量初始化
		HashMap<T, pathInfo> pathMap = new HashMap<>();
		LinkedList<T> queue = new LinkedList<>();
		
		//遍历所有的邻接点，将它在table中初始化下
		for(Entry<T,OneVetexAdjacencyList<T>> entry : this.map.entrySet()) {
			pathMap.put(entry.getKey(), new pathInfo());
		}
		//将起点设置好
		pathMap.put(startVetex, new pathInfo(true, 0, null));
		
		queue.addLast(startVetex);
		
		/*
		 * 使用广度优先搜索方式来遍历所有边，更新所有的节点距离。
		 */
		while(! queue.isEmpty()) {
			//获取当前的节点的邻接点的列表，并且从队列中删除它
			T nowNode = queue.removeFirst();
			//当前节点known状态置为true,这神级麻烦的map修改方式
			pathInfo nowNodeInfo = pathMap.get(nowNode);
			nowNodeInfo.known = true;
			
			//获取邻接表
			OneVetexAdjacencyList<T> adjList = this.map.get(nowNode);
			LinkedList<Edge<T>> edgeList = adjList.getAdjacencyVetexList();
			
			//遍历当前节点所有的有权边
			for(Edge<T> eachEdge : edgeList) {
				//获取当前边对应的节点，以及权值
				T adjVetex = eachEdge.getNextVetex();
				int weight = eachEdge.getWeight();
				
				//从路径信息表中取出当前邻接点对应的数据
				pathInfo adjVetexInfo = pathMap.get(adjVetex);
				//判断当前边的权值 + 当前的节点过往的总路径（权值和）是否 < 邻接点记录的值
				//更新数据和进队分开
				if(pathMap.get(nowNode).dv + weight < adjVetexInfo.dv) {
					//更新数据，并且入队。
					adjVetexInfo.dv = pathMap.get(nowNode).dv + weight;
					adjVetexInfo.prevNode = nowNode;
					//如果该节点还没有进队，用于之后遍历该邻接点的所有邻接点
					if(!adjVetexInfo.known)
						queue.addLast(adjVetex);
				}
			}
		}
		
		/*
		 * 通过回溯来显示正常的路径
		 */
		LinkedList<Edge<T>> correctPath = null;
		if(!pathMap.isEmpty()) {
			correctPath = new LinkedList<>();
			for(T vetex = endVetex; vetex != null; vetex = pathMap.get(vetex).prevNode) {
				correctPath.addFirst(new Edge<>(pathMap.get(vetex).dv, vetex));
			}
		}
		return correctPath;
	}
	
	/**
	 * 计算单个节点的入度。
	 * 计算入度就比较麻烦了。得遍历所有节点的所有邻接表来确认。
	 * 
	 * 
	 * @param x
	 * @return
	 */
	public int inDegree(T x) {
		int result = 0;
		
		//遍历所有邻接表
		for(Entry<T, OneVetexAdjacencyList<T>> entry : this.map.entrySet()) {
			//如果是当前节点的邻接表完全可以跳过。
			if(x.equals(entry.getKey())) {
				continue;
			}
			//获取边的list
			OneVetexAdjacencyList<T> thisAdjList = entry.getValue();
			LinkedList<Edge<T>> edgeList = thisAdjList.getAdjacencyVetexList();
			//遍历所有边，即所有当前邻接点的所有顶点
			for (Edge<T> edge : edgeList) {
				//如果别的顶点的邻接表内的邻接点是x的画，计数器+1
				if(x.equals(edge.getNextVetex())) {
					++result;
				}
			}
		}
		return result;
	}
	
	/**
	 * 计算单个阶段的出度。即返回当前顶点的邻接表内输出边的条数
	 * @param x
	 * @return
	 */
	public int outDegree(T x) {
		OneVetexAdjacencyList<T> thisList = this.map.get(x);
		return thisList.getAdjacencyVetexList().size();
	}
}

/**
 * 单独一个邻接表的类
 * 
 * @author 25040
 *
 * @param <T>
 */
class OneVetexAdjacencyList<T>{
	//邻接表的自身顶点的属性
	private T self;
	//邻接表中右self指向的所有邻接点,使用HashMap后，我们就不需要跟早
	private LinkedList<Edge<T>> edges;
	
	/**
	 * 构造一个邻接表。
	 */
	public OneVetexAdjacencyList(T self) {
		this.self = self;
		this.edges = new LinkedList<>();
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
		return this.edges.isEmpty();
	}
	
	/**
	 * 获取当前顶点的有几个邻接点，即当前顶点的邻接点数目
	 * @return
	 */
	public int adjacencyVetexNum() {
		return this.edges.size();
	}
	
	/**
	 * 在当前顶点的邻接表中插入到邻接点，并且设置该边的权值为1
	 * @param otherVetex
	 */
	public void insertAdjcancyVetex(T otherVetex) {
		insertAdjacencyVetex(otherVetex, 1);
	}
	
	/**
	 * 在当前顶点的邻接表中插入到邻接点，并且设置该边的权值
	 * @param otherVetex
	 */
	public void insertAdjacencyVetex(T otherVetex, int weight) {
		this.edges.addLast(new Edge<>(weight, otherVetex));
	}
	
	/**
	 * 在当前顶点的邻接表中插入多个邻接点
	 * @param otherVetexs
	 * @throws java.lang.IllegalArgumentException-欲插入邻接点数组和权值数组的数目不匹配
	 */
	public void insertAdjacencyVetex(T[] otherVetexs, int[] allEdgesWeight) {
		if(otherVetexs.length != allEdgesWeight.length) {
			throw new IllegalArgumentException("欲插入邻接点数组和权值数组的数目不匹配！");
		}
		for(int i = 0;i < otherVetexs.length; ++i) {
			insertAdjacencyVetex(otherVetexs[i],allEdgesWeight[i]);
		}
	}
	
	/**
	 * 格式化输出当前顶点的邻接表
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[ ");
		
		//sb.append("self:").append(getSelfVetex()).append("->(");
		for(int i = 0;i < adjacencyVetexNum();++i) {
			sb.append(this.edges.get(i)).append(", ");
		}
		
		sb.append(" ]");
		return sb.toString();
	}
	
	public LinkedList<Edge<T>> getAdjacencyVetexList(){
		return this.edges;
	}
}

/**
 * 
 * @author 25040
 *
 */
class Edge<T>{
	private int weight;
	private T nextVetex;
	
	/**
	 * 供无权图使用
	 * @param nextVetex
	 */
	public Edge(T nextVetex) {
		this(1, nextVetex);
	}
	
	/**
	 * 供有权图构造边对象使用
	 * @param weight
	 * @param nextVetex
	 */
	public Edge(int weight, T nextVetex) {
		super();
		this.weight = weight;
		this.nextVetex = nextVetex;
	}
	
	
	
	public int getWeight() {
		return weight;
	}

	public T getNextVetex() {
		return nextVetex;
	}

	/**
	 * 边对象的格式化输出
	 */
	public String toString() {
		return (nextVetex +"("+ weight +")"); 
	}
	
	
}

