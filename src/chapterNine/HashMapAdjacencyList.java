package chapterNine;


import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;
import java.util.PriorityQueue;

import chapterEight.DisjSets;

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
		/*有权无圈图的测试数据
		graph.builtAdjacency("v1", new String[] {"v2","v4"}, new int[] {2,1});
		graph.builtAdjacency("v2", new String[] {"v4","v5"}, new int[] {3,10});
		graph.builtAdjacency("v3", new String[] {"v1","v6"}, new int[] {4,5});
		graph.builtAdjacency("v4", new String[] {"v3","v5","v6","v7"}, new int[] {2,2,8,4});
		graph.builtAdjacency("v5", new String[] {"v7"}, new int[] {6});
		graph.builtAdjacency("v6", new String[] {}, new int[] {});
		graph.builtAdjacency("v7", new String[] {"v6"}, new int[] {1});
		*/
		/*割点测试数据
		graph.builtAdjacency("A", new String[] {"B","D"},new int[] {1,1});
		graph.builtAdjacency("B", new String[] {"A","C"},new int[] {1,1});
		graph.builtAdjacency("C", new String[] {"B","D","G"},new int[] {1,1,1});
		graph.builtAdjacency("D", new String[] {"A","C","E","F"},new int[] {1,1,1,1});
		graph.builtAdjacency("E", new String[] {"D","F"},new int[] {1,1});
		graph.builtAdjacency("F", new String[] {"D","E"},new int[] {1,1});
		graph.builtAdjacency("G", new String[] {"C"},new int[] {1});
		*/
		/*关键路径分析数据
		graph.builtAdjacency("start", new String[] {"A","B"},new int[] {3, 2});
		graph.builtAdjacency("A", new String[] {"C","D"},new int[] {3, 2});
		graph.builtAdjacency("B", new String[] {"D","E"},new int[] {2, 1});
		graph.builtAdjacency("C", new String[] {"F"},new int[] {3});
		graph.builtAdjacency("D", new String[] {"F","G"},new int[] {3, 2});
		graph.builtAdjacency("E", new String[] {"G","K"},new int[] {2, 4});
		graph.builtAdjacency("F", new String[] {"H"},new int[] {1});
		graph.builtAdjacency("G", new String[] {"H"},new int[] {1});
		graph.builtAdjacency("H", new String[] {"end"},new int[] {1});
		graph.builtAdjacency("K", new String[] {"H"},new int[] {1});
		graph.builtAdjacency("end", new String[] {},new int[] {});
		*/
		/*欧拉环游的测试数据1
		graph.builtAdjacency("A", new String[] {"B","C"},new int[] {1,1});
		graph.builtAdjacency("B", new String[] {"A","C","D","G"},new int[] {1,1,1,1});
		graph.builtAdjacency("C", new String[] {"A","B","E","G"},new int[] {1,1,1,1});
		graph.builtAdjacency("D", new String[] {"B","E","F","G"},new int[] {1,1,1,1});
		graph.builtAdjacency("E", new String[] {"C","D","F","G"},new int[] {1,1,1,1});
		graph.builtAdjacency("F", new String[] {"D","E"},new int[] {1,1});
		graph.builtAdjacency("G", new String[] {"B","C","D","E"},new int[] {1,1,1,1});
		*/
		/*欧拉环游的测试数据2
		graph.builtAdjacency("1", new String[] {"3","4"},new int[] {1,1});
		graph.builtAdjacency("2", new String[] {"3","8"},new int[] {1,1});
		graph.builtAdjacency("3", new String[] {"1","2","4","6","7","9"},new int[] {1,1,1,1,1,1});
		graph.builtAdjacency("4", new String[] {"1","3","5","7","10","11"},new int[] {1,1,1,1,1,1});
		graph.builtAdjacency("5", new String[] {"4","10"},new int[] {1,1});
		graph.builtAdjacency("6", new String[] {"3","9"},new int[] {1,1});
		graph.builtAdjacency("7", new String[] {"3","4","9","10"},new int[] {1,1,1,1});
		graph.builtAdjacency("8", new String[] {"2","9"},new int[] {1,1});
		graph.builtAdjacency("9", new String[] {"3","6","7","8","10","12"},new int[] {1,1,1,1,1,1});
		graph.builtAdjacency("10", new String[] {"4","5","7","9","11","12"},new int[] {1,1,1,1,1,1});
		graph.builtAdjacency("11", new String[] {"4","10"},new int[] {1,1});
		graph.builtAdjacency("12", new String[] {"9","10"},new int[] {1,1});
		*/
		graph.builtAdjacency("v1", new String[] {"v2","v3","v4"},new int[] {2,4,1});
		graph.builtAdjacency("v2", new String[] {"v1","v4","v5"},new int[] {2,3,10});
		graph.builtAdjacency("v3", new String[] {"v1","v4","v6"},new int[] {4,2,5});
		graph.builtAdjacency("v4", new String[] {"v1","v2","v3","v5","v6","v7"},new int[] {1,3,2,7,8,4});
		graph.builtAdjacency("v5", new String[] {"v2","v4","v7"},new int[] {10,7,6});
		graph.builtAdjacency("v6", new String[] {"v3","v4","v7"},new int[] {5,8,1});
		graph.builtAdjacency("v7", new String[] {"v4","v5","v6"},new int[] {4,6,1});
		//System.out.println(graph.toString());
		//System.out.println(graph.breathFirstSearch("v3", "v7"));
		//System.out.println(graph.getShortestPathWithWeightedGraph("v3", "v5"));
		//System.out.println(graph.inDegree("v4"));
		//graph.depthFirstSearchAndShowPath("v1");
		//graph.findArt("A");
		//graph.criticalPathAnalysis("start","end");
		//graph.eularTour("5");
		//graph.minSpanningTreeByPrim("v1");
		graph.minSpanningTreeByKruskal();
		System.out.println(graph.size());
		
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
		if(this.map.put(selfVetex, new OneVetexAdjacencyList<>(selfVetex)) == null) {
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
		if(this.map.put(selfVetex, new OneVetexAdjacencyList<>(selfVetex)) == null) {
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
	 * 
	 * 用于有权无圈图的遍历搜索路径信息的记录。
	 * 同时，也用于Prim最小生成树的算法
	 * 
	 * @author 25040
	 *
	 */
	private class PathInfo{
		//标记节点是否被标记过了
		boolean known;
		//记录当前节点到起点的最短权值
		int dv;
		//用于最短路径的回溯
		T prevNode;
		
		public PathInfo() {
			this(false, Integer.MAX_VALUE, null);
		}
		
		public PathInfo(boolean known, int dv, T prevNode) {
			super();
			this.known = known;
			this.dv = dv;
			this.prevNode = prevNode;
		}
		
		public String toString() {
			return ("known:" + known + ", dv:" + dv + ", pv:" + prevNode + "\n");
		}
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
		
		
		/*
		 * 下面正式开始进行搜索算法。 
		 */
		
		//变量初始化
		HashMap<T, PathInfo> pathMap = new HashMap<>();
		LinkedList<T> queue = new LinkedList<>();
		
		//遍历所有的邻接点，将它在table中初始化下
		for(Entry<T,OneVetexAdjacencyList<T>> entry : this.map.entrySet()) {
			pathMap.put(entry.getKey(), new PathInfo());
		}
		//将起点设置好
		pathMap.put(startVetex, new PathInfo(true, 0, null));
		
		queue.addLast(startVetex);
		
		/*
		 * 使用广度优先搜索方式来遍历所有边，更新所有的节点距离。
		 */
		while(! queue.isEmpty()) {
			//获取当前的节点的邻接点的列表，并且从队列中删除它
			T nowNode = queue.removeFirst();
			//当前节点known状态置为true,这神级麻烦的map修改方式
			PathInfo nowNodeInfo = pathMap.get(nowNode);
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
				PathInfo adjVetexInfo = pathMap.get(adjVetex);
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
	
	/**
	 * 使用深度优先搜索的方式搜索某个节点，并且返回搜索路径。
	 * 为了实现书中每个节点有个visited数据域。为了将他方便的集成，
	 * 我们在方法内部建立一个map<T,boolean>这样的hashMap来实现对各节点的访问标记。
	 * 虽然现在看上去这个例程还没有什么实际的用处和意义，书上后面应该会讲。
	 * 
	 * @param startVetex
	 * @return
	 */
	public void depthFirstSearchAndShowPath(T startVetex) {
		HashMap<T,Boolean> visited = new HashMap<>();
		//将map初始化为全false
		for(T key:this.map.keySet()) {
			visited.put(key, false);
		}
		
		dps(visited, startVetex);
	}
	
	/**
	 * 私有的递归的深度优先搜索例程。
	 * @param visited
	 * @param x
	 */
	private void dps(HashMap<T,Boolean> visited, T x) {
		//将当前节点的访问标记置为true
		visited.put(x, true);
		
		//bug 1 遍历当前节点的邻接表，而不是遍历所有节点
		//获取对应顶点的邻接表
		LinkedList<Edge<T>> adjVetexList = this.map.get(x).getAdjacencyVetexList();
			
		//遍历所有的边。即所有的顶点
		for (Edge<T> edge : adjVetexList) {
			//检查判断该店是否已经被访问过了
			T nowAdjNode = edge.getNextVetex();
			if(! visited.get(nowAdjNode)) {
				//递归进入邻接点的深度优先搜索
				System.out.print(nowAdjNode);
				dps(visited, nowAdjNode);
			}
		}
		//test
		System.out.println();
	}
	
	/*
	 * 为了完成割点的寻找，需要在每个添加额外的数据域。我们使用hashMap<T,additionalInfo>的形式来存储。
	 * additionalInfo的信息以方法内部类的形式来存储。
	 */
	private class ArtInfo{
		int num;//搜索编号
		int low;//当前节点所能达到的最低节点
		T parent;
		boolean visited;
		
		public ArtInfo(int num, int low, T parent, boolean visited) {
			super();
			this.num = num;
			this.low = low;
			this.parent = parent;
			this.visited = visited;
		}
	}
	/*
	 * 为了给深度优先生成树的节点编号，需要一个类变量来记录
	 */
	private int count;
	
	/**
	 * 使用深度优先搜索来检查无向图中的割点
	 * 还未进行起点是否是割点的特殊判断
	 * @param startNode 深度优先开始的起点
	 */
	public void findArt(T startNode) {
		//初始化额外添加数据域的HashMap
		HashMap<T, ArtInfo> addInfo = new HashMap<>();
		for(T key : this.map.keySet()) {
			addInfo.put(key, new ArtInfo(0, 0, null, false));
		}
		count = 0;
		assignNum(startNode, addInfo);
		//重置所有节点的访问标志 bug 1
		for(T key : this.map.keySet()) {
			ArtInfo nodeInfo = addInfo.get(key);
			nodeInfo.visited = false;
		}
		//bug 2 将A的parent置为本身
		ArtInfo startInfo = addInfo.get(startNode);
		startInfo.parent = startNode;
		//再进行low的深度优先搜索设置，
		assignLow(startNode, addInfo);
	}
	
	/**
	 * 还是以深度优先搜索的方式递归的遍历所有的节点。
	 * 1. 通过判断节点visited数据域来避免重复。
	 * 2. 通过判断当前节点v和邻接点w的num数据域可以得知当前的边是前向边，还是背向边。
	 * 	   2.1 当w.num > v.num && w.visited时，为前向边.
	 * 	   2.2 当w.num < v.nums时，并且当v.parent == w时，为已经处理过需要忽略的边，并且当v.parent != w 时，就是背向边了。
	 * 3. 在2.1中即前向边的情况下，根据深度优先的思想，直接递归到下一节点，然后递归完成后，判断w.low >= v.num说明v为割点。
	 *    可以画出深度优先生成树来理解，>=代表的深度更深，如果邻接点的能达到的最低深度都等于当前节点，那么当前节点一定时割点。
	 *    大于的话，说明还有其他割点。最后，更新当前节点的low值。由于深度优先搜索的关系，较小的low值会从深度较深的地方，一直向上更新。
	 *    因此，min(w.low, v.low)即可。
	 * 4.而在2.2中，背向边的情况，只需要把当前节点low值更新好就行。即v.low = min(v.low, w.num)
	 * 
	 * 值得注意的是，当判断搜索起始点是否是割点，不能使用上诉条件，需要加上一条。
	 * 即深度优先搜索树的根节点有一个以上的儿子（两个及以上），则它是割点。因为，如果这两个儿子如果能够通过其他路径到达对方。
	 * 那么一定不可能需要返回到A来转移。说明两儿子之间必须通过根节点联通，这种情况下根节点就是割点。
	 * 再次遍历根节点的邻接表，查看邻接点的parent,如果parent = root的邻接点数目>1，那么根节点为割点，否则就不是
	 * 
	 * @param v
	 * @param addInfo
	 */
	private void assignLow(T v, HashMap<T, ArtInfo> addInfo) {
		//首先，二话不说先初始化下当前节点low值
		ArtInfo vInfo = addInfo.get(v);
		if(vInfo.num == 0) {
			throw new IllegalArgumentException("请先对各节点进行深度优先搜索编号！");
		}
		vInfo.low = vInfo.num;
		//bug 3 又Tm忘记置未被访问标志了
		vInfo.visited = true;
		//遍历当前节点的邻接表
		LinkedList<Edge<T>> adjList = this.map.get(v).getAdjacencyVetexList();
		for (Edge<T> edge : adjList) {
			//获取邻接点的名字
			T w = edge.getNextVetex();
			//获取邻接点的信息
			ArtInfo adjInfo = addInfo.get(w);
			//前向边的情况，在书上伪代码的基础上，添加了对visited数据域的检查。
			if(!adjInfo.visited && adjInfo.num > vInfo.num) {
				//深度优先搜索递归
				assignLow(w, addInfo);
				//更新当前节点的low值
				vInfo.low = Math.min(adjInfo.low, vInfo.low);
				//递归搜索完成后判断当前节点是否是割点
				if(adjInfo.low >= vInfo.num) {
					//当v是根节点的时候，需要进行特殊的测试
					if(v.equals(vInfo.parent)) {
						int cnt = 0;
						for(Edge<T> twiceEdge : adjList) {
							if(addInfo.get(twiceEdge.getNextVetex()).parent.equals(v)) {
								++cnt;
							}
						}
						if(cnt <= 1) {
							continue;
						}
					}
					
					System.out.println("割点：" + v);
				}
			}
			else {
				//判断是否是背向边，注意到背向边的w节点是已经访问过的
				//如果当前节点的父亲等于邻接表，那么代表这条边已经背访问过了。不需要反向重复操作
				//bug 2 对null调用eqaul方法，将起点的parent置为它本身
				if(! vInfo.parent.equals(w)) {
					vInfo.low = Math.min(vInfo.low, adjInfo.num);
				}
			}
		}
	}

	/**
	 * 以深度优先搜索的方式递归给深度优先搜索生成树的相关节点编号。
	 * 用于推断计算无向图中的割点。
	 * 
	 * 思路和步骤：
	 * 1. 遍历当前节点的邻接表。
	 * 2. 当v.visited标志为false时，把当前节点的num数据域赋值编号（编号数值的大小代表该节点在生成树的深度。）否则跳过该邻接点。
	 * 3. 将1，2步骤递归到该邻接点上。
	 * 
	 * @param x
	 * @param additionalInfo
	 */
	private void assignNum(T x, HashMap<T, ArtInfo> addInfo) {
		ArtInfo thisVetexInfo = addInfo.get(x);
		thisVetexInfo.num = ++count;
		thisVetexInfo.visited = true;//bug 1 忘记给当前节点设置被访问标志了。
		//遍历该节点的邻接表，跟广度不一样，深度是递归例程，广度是迭代例程不一样
		//获取对应顶点的邻接表
		LinkedList<Edge<T>> adjVetexList = this.map.get(x).getAdjacencyVetexList();
		for (Edge<T> edge : adjVetexList) {
			//获取边对象对应的节点
			T adjVetex = edge.getNextVetex();
			//检查该节点的访问标记
			if(!addInfo.get(adjVetex).visited) {
				//获取当前邻接点相关信息。
				ArtInfo adjVetexInfo = addInfo.get(adjVetex);
				//记住父节点的路径信息。
				adjVetexInfo.parent = x;
				//以深度优先搜索的方式往下继续编号。
				assignNum(adjVetex, addInfo);
			}
		}
	}
	
	/**
	 * 同计算最短路径的算法一样，使用方法内部类 + HashMap 的方式向节点上添加额外信息。
	 * 
	 * EC:保存当前节点的最早完成事件
	 * LC:保存当前节点可以拖延的最大事件。
	 * visited:已访问标志
	 * allPreNode:链表，保存当前节点所有的前置节点。
	 * 
	 * @author 25040
	 *
	 */
	private class CriticalPathInfo {
		int EC;//
		int LC;
		int ST;
		boolean visited;
		LinkedList<T> allPreNode;//相当于构建了该图的反向邻接表
		
		public CriticalPathInfo(int eC, int lC, int ST, boolean visited) {
			super();
			EC = eC;
			LC = lC;
			this.ST = ST;
			this.visited = visited;
			this.allPreNode = new LinkedList<>();
		}
		
		public void addPreNode(T preNode) {
			this.allPreNode.addLast(preNode);
		}
	}
	
	/**
	 * 从起点开始进行关键路径分析。
	 * 请务必确保当前对象的图是有权有向无圈图，才可保证计算的正确性。
	 * 同时，该函数会将该图当成动作节点图来处理。请注意务必添加开始和结束节点。
	 * 
	 * 当前函数通过广度优先搜索的方式，使用队列来层层递进的进行计算。
	 * 所有节点的最早完成时间原理。公式和思路：
	 * 基本原理：
	 * EC_1 = 0；
	 * EC_i	= max{EC_w + weight_w,v};
	 * 上诉表达式就是说当前节点最早完成时间，由所有（前置节点的最早完成时间 + 前置节点到当前节点的权值）的最大值决定。
	 * 
	 * 设E为当前事件节点最早完成事件
	 * 
	 * 思路和基本步骤：
	 * 1. 初始化所需变量，将起点的E置为0；
	 * 2. 遍历当节点的邻接点，应用计算公式EC_w = Ec_v + edge_weight,如果计算得到的结果比该邻接点已经存储的值大，则更新。否则不更新。
	 * 3. 将未访问过的邻接点入队。
	 * 
	 * 所有节点的最大拖延时间计算原理和公式：
	 * 基本原理和公式：
	 * EL_n = EC_n;
	 * LC_v = min(LC_w - c_v,w);
	 * 
	 * 上述表达式通俗的说，就是说当前节点的最大拖延时间，是所有（后置节点的最早完成时间 - 后置节点到当前节点的权值）的最小值决定
	 * 
	 * 思路和步骤：（跟上面及其类似）
	 * 1.初始化所需变量，将尾点的LC值置为该地的EC值。
	 * 2.遍历当前节点的前置邻接表，应用计算公式EC_pre = Ec_v - edge_weight,如果计算得到的结果比该邻接点已经存储的值小，则更新。否则不更新。
	 *   同时利用公式ST_pre = LC_v - E_pre - weight,更新当前时间的最大拖延时间。
	 * 3. 将未访问过的节点入队。
	 * 
	 * 后期都写个检查函数，确保用户插入的邻接点，都有对应的节点存储。
	 * 
	 * @param startVetex
	 */
	public void criticalPathAnalysis(T startVetex, T endVetex) {
		//防止zz
		if((this.map.get(startVetex) == null) || (this.map.get(endVetex)) == null) {
			System.out.println("请正确输入起点和结束点");
			return;
		}
		//使用一个linkedList来当作队列使用
		LinkedList<T> queue = new LinkedList<>();
		HashMap<T, CriticalPathInfo> InfoMap = new HashMap<>();
		
		assignEC(queue, InfoMap, startVetex);
		assignLCAndST(queue, InfoMap, endVetex);
		
		//test output
		for(T x : this.map.keySet()) {
			if(x.equals(startVetex) || x.equals(endVetex)) {
				continue;
			}
			System.out.println("事件：" + x + "\t最早完成时间为：\t" + InfoMap.get(x).EC + 
					"\t不拖延进度的情况下最晚完成时间：\t" + InfoMap.get(x).LC + 
					"\t\t事件可以拖延的时间为:\t" + InfoMap.get(x).ST);
		}
	}
	
	/**
	 * 利用info中的allPreNode和EC，使用反向广度优先搜索的方式更新各时间节点的LC和ST值
	 * 即不拖延整体进度的情况下，各时间的最晚完成时间和各时间的弹性时间（即最大拖延时间）。
	 * @param queue
	 * @param InfoMap
	 * @param endVetex
	 */
	private void assignLCAndST(LinkedList<T> queue, HashMap<T, CriticalPathInfo> InfoMap, T endVetex) {
		//把尾节点的LC值设为En值
		CriticalPathInfo endVInfo = InfoMap.get(endVetex);
		endVInfo.LC = endVInfo.EC;
		
		//利用Info的allPreNode，反向广度优先搜索，得到所有事件的最大拖延时间
		queue.addLast(endVetex);
		while(! queue.isEmpty()) {
			//获取当前节点对象及其信息,并且标记已访问
			T vNode = queue.removeFirst();
			CriticalPathInfo vInfo = InfoMap.get(vNode);
			//注意上一轮正向广度优先搜索将访问过的节点设置为true表明已经访问过了
			//这一轮就将它设置为false表明已经访问过了
			vInfo.visited = false;
			
			//遍历当前节点的前置邻接表
			LinkedList<T>  preAdjList = InfoMap.get(vNode).allPreNode;
			for (T preNode : preAdjList) {
				//获取当前前置邻接点附加数据域
				CriticalPathInfo preInfo = InfoMap.get(preNode);
				//获取当前 前置邻接点到该节点的边权值,这一波操作，耗费了线性时间，以后看能否改进
				int weight = 0;
				LinkedList<Edge<T>> temp = this.map.get(preNode).getAdjacencyVetexList();
				for (Edge<T> edge : temp) {
					if(edge.getNextVetex().equals(vNode)) {
						weight = edge.getWeight();
					}
				}
				
				//判断公式条件，更新LC数据域,如果公式计算结果比现存的LC更小，则保存
				//与此同时，我们还可以算出当前事件的最大拖延事件
				if(vInfo.LC - weight < preInfo.LC) {
					preInfo.LC = vInfo.LC - weight;
					preInfo.ST = vInfo.LC - preInfo.EC - weight;
				}
				
				//将还未访问的前置节点入队,注意这一轮就将它设置为false表明已经访问过了
				//bug 2 前置邻接表有点混乱，写成vInfo
				if(preInfo.visited) {
					queue.addLast(preNode);
				}
			}
		}
		
	}
	
	/**
	 * 使用广度优先搜素的方式来计算各节点的EC值，即当前动作完成的最早时间
	 * @param queue
	 * @param InfoMap
	 * @param startVetex
	 */
	private void assignEC(LinkedList<T> queue, HashMap<T, CriticalPathInfo> InfoMap, T startVetex) {
		//初始化变量
		queue.addLast(startVetex);
		for(T vetex : this.map.keySet()) {
			InfoMap.put(vetex, new CriticalPathInfo(0, Integer.MAX_VALUE, 0, false));
		}
		
		//广度优先搜索，得到所有检查的最短完成事件。其实相当于遍历了所有的边
		while(! queue.isEmpty()) {
			//获取当前节点对象及其信息,并且标记已访问
			T vNode = queue.removeFirst();
			CriticalPathInfo vInfo = InfoMap.get(vNode);
			vInfo.visited = true;
			
			//遍历当前节点的邻接表
			LinkedList<Edge<T>> adjList = this.map.get(vNode).getAdjacencyVetexList();
			for (Edge<T> edge : adjList) {
				//获取当前的邻接点,边权值和邻接点的附加数据域
				T adjw = edge.getNextVetex();
				int weight = edge.getWeight();
				CriticalPathInfo wInfo = InfoMap.get(adjw);
				
				//判断并且更新邻接点的EC数据域
				if(vInfo.EC + weight > wInfo.EC) {
					wInfo.EC = vInfo.EC + weight;
				}
				
				//对未访问过的邻接点压入队列中
				if(! wInfo.visited) {
					queue.addLast(adjw);
				}
				
				//把当前节点插入到该邻接点的allPreNode链表中，用于后续的计算
				wInfo.addPreNode(vNode);
			}
		}
		
	}
	
	
	/**
	 * 用于在欧拉环游中，为了便于对访问过的边进行标记。
	 * 其针对的问题就是，边<2,3>与边<3,2>中在无向图中是等价的。
	 * 而在欧拉环游中，这两个在不同的邻接表中，不太好表示。
	 * 同时改变邻接表的本来结构。
	 * 
	 * @author 25040
	 *
	 * @param <T>
	 */
	@SuppressWarnings("hiding")
	private class EdgeUsingInEularTour<T>{
		T v1;
		T v2;
		
		public EdgeUsingInEularTour(T v1, T v2) {
			super();
			this.v1 = v1;
			this.v2 = v2;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((v1 == null) ? 0 : v1.hashCode());
			result = prime * result + ((v2 == null) ? 0 : v2.hashCode());
			return result;
		}
		
		/**
		 * 必须要重新定义equals和hashcode让哈希表能够正确判断两个边是否相等
		 */
		@SuppressWarnings("rawtypes")
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			EdgeUsingInEularTour other = (EdgeUsingInEularTour) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (v1 == null) {
				if (other.v1 != null)
					return false;
			} else if (!v1.equals(other.v1))
				return false;
			if (v2 == null) {
				if (other.v2 != null)
					return false;
			} else if (!v2.equals(other.v2))
				return false;
			return true;
		}

		@SuppressWarnings("rawtypes")
		private HashMapAdjacencyList getOuterType() {
			return HashMapAdjacencyList.this;
		}
		
		
	}
	
	/**
	 * 对无权无向图的欧拉环游分析。
	 * 
	 * 
	 * 
	 * 基本思路和步骤：
	 * 1. 从用户指定的起点开始，进行深度优先搜索。并且对访问过的边进行标记。
	 * 2. 直到重新访问到了圈起点，这样深度优先搜索访问过的边形成了一个圈。这样形成了一条子路径。
	 * 	   我们用链表来保存这个圈。
	 * 3. 我们取出上述链表的倒数第二个节点，再次进行深度优先搜索，注意。不能重复访问标记了的边。
	 *    然后把这次深度优先搜索访问形成的圈，插入上面哪个链表中。我感觉可以使用iterator.add方法来完成
	 * 4.重复1 - 3步骤，直到所有的边都访问过了。
	 * 
	 * 一个问题是：如何保存一条边已经访问过这个信息。
	 * 
	 * 第二个想法，干脆重建一个无权邻接表算了。使用上诉哈希表貌似更加麻烦
	 * 
	 * @param startVetex 开始进行欧拉回路分析的起点。
	 */
	public void eularTour(T startVetex) {
		
		if(!checkHasEularTour()) {
			System.out.println("该图不存在欧拉环游！");
			return;
		}
		
		//用链表来储存结果路径
		LinkedList<T> resultPath = new LinkedList<>();
		//为了便于拼接路径，我们使用迭代器来管理
		ListIterator<T> iter = resultPath.listIterator();
		//用于标记各无向边是否已经被访问过了
		HashMap<EdgeUsingInEularTour<T>,Boolean> allEdgeIsVisited = new HashMap<>();
		//定义一个计数器，用于记录当前图中还有几条边为未被访问
		int edgesUnvisited;
		
		//初始化各变量
		//遍历邻接表，将所有边都插入到用于保存边访问状态的哈希表中
		for(T x: this.map.keySet()) {
			//获取该节点对应的邻接表
			LinkedList<Edge<T>> adjList = this.map.get(x).getAdjacencyVetexList();
			//遍历邻接表中所有的有权边
			for(Edge<T> edge : adjList) {
				//将所有有向边都放入visitedMap中，注意在后续处理中，访问后标记需要同时置两条边都为true
				allEdgeIsVisited.put(new EdgeUsingInEularTour<T>(x, edge.getNextVetex()), false);
			}
		}
		//初始化其他变量
		//获取全图边的条数
		edgesUnvisited = allEdgeIsVisited.size();
		//先插入起点
		iter.add(startVetex);
		T nextStrat = startVetex;
		//反复进行深度优先搜索，直到遍历所有的边
		while((resultPath.size() - 1) != edgesUnvisited / 2) {
			//重新设置迭代器，使他指向正确的位置，保证拼接路径的正确性，同时寻找下一个深度优先搜索的起始点
			nextStrat = iter.previous();
			//bug 4 这里的逻辑应该是当被测节点没有其他可见边的时候，迭代器向前移动
			while(! hasUnvisitedEdege(nextStrat, allEdgeIsVisited)) {
				nextStrat = iter.previous();
			}
			//由于在深度优先搜索中，一开始就会插入一个该圈的起点，所有需要删除这个寻找点，这样才不会重复插入
			iter.remove();
			//欧拉环游的深度优先搜索
			eularTourDps(nextStrat, null, allEdgeIsVisited, nextStrat, iter);
		}
		System.out.println(resultPath);
		
	}
	
	/**
	 * 检查当前图是否有欧拉环游。
	 * 无论是有权图还是无权图都可以欧拉环游。
	 * 即检查当前图中所有的邻接表的size都为偶数，这样就可以保证每个点的出度和入度都为偶数
	 * @return
	 */
	private boolean checkHasEularTour() {
		//遍历图中所有的节点
		for(T vetex : this.map.keySet()) {
			//遍历当前节点的邻接表
			LinkedList<Edge<T>> adjList = this.map.get(vetex).getAdjacencyVetexList();
			if(adjList.size() % 2 != 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 用于深度优先搜索的递归例程。同时在递归的过程中对访问过的边机型标记，并且插入到结果中
	 * 
	 * @param Vetex             深度优先搜索的当前节点
	 * @param lastVetex         用于记录上个节点，这样可以防止在深度优先搜索时，回溯的问题。
	 * 							之前是因为可以通过节点visited数据域来控制它不会这样。
	 * @param allEdgeIsVisited  记录着边是否被访问的哈希表
	 * @param resultPath        用于记录搜索路径的链表
	 * @param startVetex        本次搜索的起点，用于判断是否形成了一个圈。
	 * @param iter              结果链表中的迭代器，记录深度优先搜索的路径
	 */
	private void eularTourDps(T Vetex, T lastVetex, HashMap<EdgeUsingInEularTour<T>, Boolean> allEdgeIsVisited,
			T startVetex, ListIterator<T> iter) {
		//将当前节点插入路径中
		iter.add(Vetex);
		//为了保证插入到链表的末尾，需要next一下
		//iter.next();
		
		//获取当前节点的邻接表
		LinkedList<Edge<T>> adjList = this.map.get(Vetex).getAdjacencyVetexList();
		
		//初始化各变量,
		for(Edge<T> edge : adjList) {
			//获取邻接点
			T adjV = edge.getNextVetex();
			
			/*
			 * //判断当前邻接点是否还有未访问的边，如果有，则递归到它进行深度优先搜索
			//bug 2 直接A-B-A回来了。。。必须保证不能回访，即
			//bug 5 不仅仅要深度优先搜索的下一个邻接点是否有未访问的边，同时，还应该判断从当前点是否有为访问的边能到邻接点
			//防止它往上个过来的节点直接回去了，即避免形成A-B-A这种圈
			//&& hasUnvisitedEdege(adjV, allEdgeIsVisited)) {这个条件判断是多余的，因为只要它能够有路径到达下一个邻接点，
			//由于在前面已经检查了所有节点的度为偶数，所以，能进一定能出
			 * 
			 */
			if(! allEdgeIsVisited.get(new EdgeUsingInEularTour<T>(Vetex, adjV)) && !adjV.equals(lastVetex)) {
				//将该边置为已访问
				updateVisitedMap(Vetex, adjV, allEdgeIsVisited);
				//基准情形，如果形成了一个圈，即深度优先搜索回到了起点，那么就结束递归
				if(adjV.equals(startVetex)) {
					//最后再插入一次，直接返回
					iter.add(adjV);
					break;
				}
				eularTourDps(adjV, Vetex, allEdgeIsVisited, startVetex, iter);
				//bug 3 形成一个圈后，比如A-B-C-A，它会回到C节点，遍历邻接表，寻找下一个开始深度优先搜索的节点
				//所以，找到一个完整的圈后，直接break退出循环。
				break;
			}
			
		}
	}
	
	/**
	 * 更新已访问边的哈希表，将当前访问边置为true。
	 * 注意，双向都要置为true
	 * @param vetex
	 * @param adjV
	 * @param allEdgeIsVisited
	 */
	private void updateVisitedMap(T vetex, T adjV, HashMap<EdgeUsingInEularTour<T>, Boolean> allEdgeIsVisited) {
		//bug 1 写成了false，弱智了
		allEdgeIsVisited.put(new EdgeUsingInEularTour<>(vetex, adjV), true);
		allEdgeIsVisited.put(new EdgeUsingInEularTour<>(adjV, vetex), true);
	}

	/**
	 * 判断当前该节点的邻接表中是否还有未访问的边
	 * 
	 * @param adjV
	 * @return
	 */
	private boolean hasUnvisitedEdege(T adjV,HashMap<EdgeUsingInEularTour<T>, Boolean> allEdgeIsVisited) {
		//获取邻接表
		LinkedList<Edge<T>> adjList = this.map.get(adjV).getAdjacencyVetexList();
		//遍历邻接表
		for (Edge<T> edge : adjList) {
			//新建一各暂存量，用于判断邻接表是否还有未访问的边
			EdgeUsingInEularTour<T> eularEdge = new EdgeUsingInEularTour<>(adjV, edge.getNextVetex());
			//有一条边未访问，就返回true
			if(!allEdgeIsVisited.get(eularEdge)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @version <1>
	 * 使用贪婪算法解决无向图有权图中的最小生成树问题。
	 * 
	 * 使用Prim算法的思想来解决最小生成树的问题。
	 * 这里复用求有权无圈图最短路径算法的PathInfo类，来跟踪路径信息。
	 * 不过这里dv表示 当前节点到start路径中最小的边权值。pv是记录引起改变当前节点dv值的节点。
	 * 
	 * 核心：选取当前节点所有输出边的权值最小的边。所有这样的边构成的树就是最小生成树。
	 * 
	 * 算法套路我是理解了，但是还是有点不理解，为什么这样一定能形成最小生成树
	 * 
	 * 思路和步骤基本和广度优先搜索的思路基本相同：
	 * 1. 从起点开始，遍历各个节点的邻接表。
	 * 2. 针对当前节点的每个未知的邻接表，判断并且更新当前邻接点的dv和pv值
	 * 3. 直到完成所有的节点的遍历。也就是需要访问所有的边
	 * 
	 * @version <2> 2018-04-01 重新理了一遍书上的思路，发现不知道是我理解的问题，亦或是算法本身的问题。
	 * 我打算按照我自己的理解走一遍，就如同上面核心中的描述：选取当前节点所有未知节点的最小边权值。
	 * 
	 * 基本思路和步骤：
	 * 1. 从起点开始遍历该点的邻接表，选取未知边中具有最小边权值的边。记录该边权值在v节点的dv值中，该边对应的邻接点记录在pv值中。
	 * 2. 对未知的邻接点入队，重复步骤1，遍历图中所有的节点。
	 * 3. 最后根据infoMap即可输出最小生成树。
	 * 
	 * 上面想法有个大bug，不好解决成圈问题。
	 * 
	 * @version <3> 
	 * 算了，还是老老实实学习书上的贪婪算法吧，然后贪婪地求解吧。真是想不贪婪都是不行啊。
	 * 
	 */
	public void minSpanningTreeByPrim(T startVetex) {
		//声明需要的各项变量
		LinkedList<T> queue = new LinkedList<>();
		HashMap<T,PathInfo> infoMap = new HashMap<>();
		
		//初始化各变量
		for(T vetex : this.map.keySet()) {
			infoMap.put(vetex, new PathInfo(false, Integer.MAX_VALUE, null));
		}
		//将起点的pathInfo值，置为合适
		PathInfo sInfo = infoMap.get(startVetex);
		sInfo.dv = 0;
		//向队列中插入起点
		queue.addLast(startVetex);
		
		//广度优先搜索,每次都得贪婪得选取下个节点，看来这里还不得不使用二叉堆啊。
		while(! queue.isEmpty()) {
			//获取当前节点得信息
			T nowV = queue.removeFirst();
			PathInfo nowVInfo = infoMap.get(nowV);
			nowVInfo.known = true;
			
			//遍历邻接表，加入优先队列，获得当前节点所有未知输出边中，权值最小得
			PriorityQueue<Edge<T>> heap = new PriorityQueue<>(new EdgeComparatorWithWeight());
			LinkedList<Edge<T>> adjList = this.map.get(nowV).getAdjacencyVetexList();
			heap.addAll(adjList);
			
			//让优先队列按照从小到大的输出各边,直到二叉堆为空
			while(!heap.isEmpty()) {
				//取出当前边权值最小的边
				Edge<T> adjEdeg = heap.poll();
				//获取当前边的权值及其其他信息
				int edgeWeight = adjEdeg.getWeight();
				T adjV = adjEdeg.getNextVetex();
				PathInfo adjVInfo = infoMap.get(adjV);
				//必须针对未知节点,并且边权值比记录值更小
				//bug 1 针对未知邻接点要让别人入队。
				if(!adjVInfo.known && edgeWeight < adjVInfo.dv) {
					queue.addLast(adjV);
					adjVInfo.dv = edgeWeight;
					adjVInfo.prevNode = nowV;
				}
			}
		}
		
		//根据infoMap的preNode信息，既可以显示出生成树的所有路径了
		//直接遍历infoMap即可，跳过起点
		for(T vetex : infoMap.keySet()) {
			if(!vetex.equals(startVetex)) {
				System.out.printf("{%s,%s}\n", vetex, infoMap.get(vetex).prevNode);
			}
		}
		
	}
	
	/**
	 * 基于权值的边的比较器。用于Prim最小生成树的算法。
	 * @author 25040
	 *
	 */
	private class EdgeComparatorWithWeight implements Comparator<Edge<T>>{

		@Override
		public int compare(Edge<T> arg0, Edge<T> arg1) {
			return Integer.compare(arg0.getWeight(), arg1.getWeight());
		}
		
	}
	
	/**
	 * 使用第二种贪婪策略。
	 * 
	 * 其基本核心思想和步骤：
	 * 1. 将所有的无向边按照边权值从小到大排序。
	 * 2. 依次测试每天是否满足条件。此条件为该边的加入是否与此时已经连接的边成圈。
	 *    也就是说，判断新插入边的两个端点是否属于一个集合中。这里就用到了第八章不相交集类的union/find例程。
	 * 
	 * 傻逼了，我根本不用把有向图化为无向图，我反正使用不相交集类，在<u,v>和<v,u>是等价的。
	 * 代价是双倍与无向边的空间和时间复杂度。
	 * 我直接把所有有向边导入到优先队列中即可。
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void minSpanningTreeByKruskal() {
		//初始化包括优先队列在内的变量
		PriorityQueue<EdgeUsingKruskal> queue = new PriorityQueue<>(new EdgeComparatorWithWeight2());
		//获取当前图中的所有节点的数组。
		T[] allVetex = (T[])this.map.keySet().toArray();
		//初始化不相交集类
		DisjSets<T> set = new DisjSets<>(allVetex.length, allVetex);
		
		//遍历邻接表把所有的有向边都加入到优先队列中
		for(T vetex : this.map.keySet()) {
			LinkedList<Edge<T>> adjList = this.map.get(vetex).getAdjacencyVetexList();
			for (Edge<T> edge : adjList) {
				queue.add(new EdgeUsingKruskal(vetex, edge.getNextVetex(), edge.getWeight()));
			}
		}
		
		//对优先队列执行deleteMin操作直到优先队列为空为止,
		while(!queue.isEmpty()) {
			//获得当前队列中权值最小的边
			EdgeUsingKruskal edge = queue.remove();
			
			//判断改变是否满足条件,不属于一个集合中，即不成圈，那么改变就可以接受。
			if(!set.inSameSet(edge.v1, edge.v2)) {
				//将这两点放入一个集合中，表示它们已经连在一起了。即在一棵树上了。
				set.union(edge.v1, edge.v2);
				//test
				System.out.printf("(%s,%s)\n", edge.v1, edge.v2);
			}
			
			
		}
	}
	

	/**
	 * 又是把无向图的算法应用在有向图中的问题。我到现在还没有想到一个很好的方法。
	 * 这里复用欧拉环游的Edge类，而且集成了它关于判断边相等的equal方法.
	 * 该类用于Kruskal最小生成树算法。
	 * 
	 * @author 25040
	 *
	 */
	private class EdgeUsingKruskal extends EdgeUsingInEularTour<T>{
		//创建两个数据域，包括边的边权值和是否被采纳。
		private int edgeWeight;
		
		public EdgeUsingKruskal(T v1, T v2, int weight) {
			super(v1, v2);
			this.edgeWeight = weight;
		}
		
	}
	
	/**
	 * 还是基于边权值的比较器。
	 * 该比较器用于Kruskal最小生成树算法。
	 * @author 25040
	 *
	 */
	private class EdgeComparatorWithWeight2 implements Comparator<EdgeUsingKruskal>{

		@Override
		public int compare(HashMapAdjacencyList<T>.EdgeUsingKruskal arg0,
				HashMapAdjacencyList<T>.EdgeUsingKruskal arg1) {
			return Integer.compare(arg0.edgeWeight, arg1.edgeWeight);
		}
		
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
	/*
	 * 这个数据域确实在使用hashMap的时候没点用处。
	 */
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

