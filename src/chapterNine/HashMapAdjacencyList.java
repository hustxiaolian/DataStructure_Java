package chapterNine;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

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
 * @version v2.0 
 * ������������ʵʵ��ʹ�ñ�׼���е�hashmap�ɣ��Լ�����Ȼ�����������Լ�����������hashmap����ʵ���ǲ���ȫ��������û�е�����
 * ̫���ˡ�
 * 
 * 
 * @author 25040
 *
 */
public class HashMapAdjacencyList<T> {
	
	public static void main(String[] args) {
		HashMapAdjacencyList<String> graph = new HashMapAdjacencyList<>(7);
		/*��Ȩ��Ȧͼ��������
		graph.builtAdjacency("v1", new String[] {"v2","v4"}, new int[] {1,1});
		graph.builtAdjacency("v2", new String[] {"v4","v5"}, new int[] {1,1});
		graph.builtAdjacency("v3", new String[] {"v1","v6"}, new int[] {1,1});
		graph.builtAdjacency("v4", new String[] {"v3","v5","v6","v7"}, new int[] {1,1,1,1});
		graph.builtAdjacency("v5", new String[] {"v7"}, new int[] {1});
		graph.builtAdjacency("v6", new String[] {}, new int[] {});
		graph.builtAdjacency("v7", new String[] {"v6"}, new int[] {1});
		*/
		/*��Ȩ��Ȧͼ�Ĳ�������
		graph.builtAdjacency("v1", new String[] {"v2","v4"}, new int[] {2,1});
		graph.builtAdjacency("v2", new String[] {"v4","v5"}, new int[] {3,10});
		graph.builtAdjacency("v3", new String[] {"v1","v6"}, new int[] {4,5});
		graph.builtAdjacency("v4", new String[] {"v3","v5","v6","v7"}, new int[] {2,2,8,4});
		graph.builtAdjacency("v5", new String[] {"v7"}, new int[] {6});
		graph.builtAdjacency("v6", new String[] {}, new int[] {});
		graph.builtAdjacency("v7", new String[] {"v6"}, new int[] {1});
		*/
		/*����������
		graph.builtAdjacency("A", new String[] {"B","D"},new int[] {1,1});
		graph.builtAdjacency("B", new String[] {"A","C"},new int[] {1,1});
		graph.builtAdjacency("C", new String[] {"B","D","G"},new int[] {1,1,1});
		graph.builtAdjacency("D", new String[] {"A","C","E","F"},new int[] {1,1,1,1});
		graph.builtAdjacency("E", new String[] {"D","F"},new int[] {1,1});
		graph.builtAdjacency("F", new String[] {"D","E"},new int[] {1,1});
		graph.builtAdjacency("G", new String[] {"C"},new int[] {1});
		*/
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
		//System.out.println(graph.toString());
		//System.out.println(graph.breathFirstSearch("v3", "v7"));
		//System.out.println(graph.getShortestPathWithWeightedGraph("v3", "v5"));
		//System.out.println(graph.inDegree("v4"));
		//graph.depthFirstSearchAndShowPath("v1");
		//graph.findArt("A");
		graph.criticalPathAnalysis("start","end");
		
		
		
	}
	
	//���Դ洢һ��HashMap
	private HashMap<T, OneVetexAdjacencyList<T>> map;
	private int currentSize;
	
	/**
	 * ����
	 * @param ͼ�ĵ�ǰ
	 */
	public HashMapAdjacencyList(int vetexNum) {
		this.map = new HashMap<>(vetexNum);
		this.currentSize = 0;
	}
	
	/**
	 * ����ڽӱ�
	 */
	public void makeEmpty() {
		this.currentSize = 0;
		map.clear();
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
		if(this.map.put(selfVetex, new OneVetexAdjacencyList<>(selfVetex)) != null) {
			++this.currentSize;
		}
		OneVetexAdjacencyList<T> adjacencyList = this.map.get(selfVetex);
		
		adjacencyList.insertAdjcancyVetex(otherVetex);
		
	}
	
	/**
	 * ������ǰself�������������������ڽӹ�ϵ
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
	 * ��Ȩ��Ȧͼ�Ĺ���������������·�����⡣
	 * ʹ��LinkedList��HashMap����ɡ�����linkedList��ɵĸ����Ƕ��еĹ�����
	 * 
	 * ��ô�������ˣ�Ϊʲô�����������ص������·����
	 * ��Ϊ����ʵ���ɺ�����һ���ġ����������currDist + 1����Щ�ڽӵ㣬��Զ���ڽӵ�ʱ�����������Ӷ�β��ӡ�
	 * ������֤��һ�����ȴ�������˵�ǰcurrDist�����нڵ���ڴ����Զ���ڽӵ㡣�����紦��ĵ㣬����ռ��map��λ�ã�
	 * ������Ȼ������Ϊ������ں����߾��и��̵�·����Ҳ���������Ѿ�known�ˡ�
	 * 
	 * ����·���ı߶�����������������·���洢�˾�������Щ�ڵ㣬��Ȩֵ�ĺ������������˸��ڵ㵽���ļ�ࡣ
	 * ���û��·���ܴ���㵽�յ㣬����null
	 * 
	 * @param startVetex
	 * @return
	 */
	public List<T> breathFirstSearch(T startVetex, T endVetex) {
		HashMap<T,T> pathMap = new HashMap<>();
		LinkedList<T> q = new LinkedList<>();
		//�������ӡ�
		q.addLast(startVetex);
		while(! q.isEmpty()) {
			//��ȡ��ǰ�Ľڵ���ڽӵ���б����ҴӶ�����ɾ����
			T nowNode = q.removeFirst();
			OneVetexAdjacencyList<T> adjList = this.map.get(nowNode);
			LinkedList<Edge<T>> edgeList = adjList.getAdjacencyVetexList();
			//�������е��ڽӽڵ�
			for (Edge<T> edge : edgeList) {
				T adjVetex = edge.getNextVetex();
				//�жϵ�ǰ�ڵ��Ƿ��Ѿ�known�ˣ����Ѿ�����������
				if(pathMap.get(adjVetex) == null) {
					//knownΪfalse�Ľڵ�,�洢�˽ڵ�·����
					pathMap.put(adjVetex, nowNode);
					q.addLast(adjVetex);
				}
			}
		}
		
		pathMap.put(startVetex, null);
		
		return getPathFromMap(pathMap, startVetex, endVetex);
	}
	
	/**
	 * �����ɵ�map·�����У����������·����
	 * @param pathMap
	 * @param startVetex
	 * @param endVetex
	 * @return
	 */
	private LinkedList<T> getPathFromMap(HashMap<T, T> pathMap, T startVetex, T endVetex) {
		LinkedList<T> result = null;
		
		if(!pathMap.isEmpty()) {
			result = new LinkedList<>();
			//��β����ʼ
			for(T vetex = endVetex;vetex != null;vetex = pathMap.get(vetex))
				result.addFirst(vetex);
		}
		
		return result;
	}
	
	/**
	 * ʹ�ù���������������Ȩ��Ȧͼ�еĵ�Դ���·�����⡣Ī����������İ汾��
	 * ��ʵ���Ҵ����������ϵ���˼�������ײ�����ġ����̸�����̰���������̰���ġ�
	 * 
	 * ͼ�в��ܰ�����ֵ�ߡ�
	 * 
	 * ˼·���Ǹ��������������˼·һ���������ڽӵ�Ĳ�β������Զ�Ľڵ���չ��ֱ���������нڵ㡣
	 * �����㷨���Ҹ������е��㷨�Լ���ģ���������е㲻̫һ������Ŀ���Ǵ������������������еıߡ�
	 * ��̰���㷨��̫һ���ĵط����ڣ����㷨��Ҫ��ÿ�ֶ�ѡ����̾����δ֪�Ľڵ㡣���Ǹ������������һ�����������еıߡ�
	 * known�����жϸõ��Ƿ������У��������е��ڽӵ��Ƿ񶼱������һ�顣
	 * ͬʱֻҪ �ڽӵ��·����Ϣ����  pathMap.get(nowNode).dv + weight < adjVetexInfo.dv  �ͱ�ʾ���ڽӵ�����ɸ��̵�·��������
	 * ��˸�����pv��dvֵ�����۸��ڽӵ��Ƿ�known��
	 * ��˷�����ʱ�����ΪO(E),���������޹�
	 * ����ͨ����
	 * 
	 * @param startVetex ����ڵ�
	 * @param endVetex ����ڵ�
	 * @return
	 */
	public List<Edge<T>> getShortestPathWithWeightedGraph(T startVetex, T endVetex){
		/**
		 * �����ڲ���
		 * @author 25040
		 *
		 */
		class pathInfo{
			//��ǽڵ��Ƿ񱻱�ǹ���
			boolean known;
			//��¼��ǰ�ڵ㵽�������Ȩֵ
			int dv;
			//�������·���Ļ���
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
		 * ������ʽ��ʼ���������㷨�� 
		 */
		
		//������ʼ��
		HashMap<T, pathInfo> pathMap = new HashMap<>();
		LinkedList<T> queue = new LinkedList<>();
		
		//�������е��ڽӵ㣬������table�г�ʼ����
		for(Entry<T,OneVetexAdjacencyList<T>> entry : this.map.entrySet()) {
			pathMap.put(entry.getKey(), new pathInfo());
		}
		//��������ú�
		pathMap.put(startVetex, new pathInfo(true, 0, null));
		
		queue.addLast(startVetex);
		
		/*
		 * ʹ�ù������������ʽ���������бߣ��������еĽڵ���롣
		 */
		while(! queue.isEmpty()) {
			//��ȡ��ǰ�Ľڵ���ڽӵ���б����ҴӶ�����ɾ����
			T nowNode = queue.removeFirst();
			//��ǰ�ڵ�known״̬��Ϊtrue,�����鷳��map�޸ķ�ʽ
			pathInfo nowNodeInfo = pathMap.get(nowNode);
			nowNodeInfo.known = true;
			
			//��ȡ�ڽӱ�
			OneVetexAdjacencyList<T> adjList = this.map.get(nowNode);
			LinkedList<Edge<T>> edgeList = adjList.getAdjacencyVetexList();
			
			//������ǰ�ڵ����е���Ȩ��
			for(Edge<T> eachEdge : edgeList) {
				//��ȡ��ǰ�߶�Ӧ�Ľڵ㣬�Լ�Ȩֵ
				T adjVetex = eachEdge.getNextVetex();
				int weight = eachEdge.getWeight();
				
				//��·����Ϣ����ȡ����ǰ�ڽӵ��Ӧ������
				pathInfo adjVetexInfo = pathMap.get(adjVetex);
				//�жϵ�ǰ�ߵ�Ȩֵ + ��ǰ�Ľڵ��������·����Ȩֵ�ͣ��Ƿ� < �ڽӵ��¼��ֵ
				//�������ݺͽ��ӷֿ�
				if(pathMap.get(nowNode).dv + weight < adjVetexInfo.dv) {
					//�������ݣ�������ӡ�
					adjVetexInfo.dv = pathMap.get(nowNode).dv + weight;
					adjVetexInfo.prevNode = nowNode;
					//����ýڵ㻹û�н��ӣ�����֮��������ڽӵ�������ڽӵ�
					if(!adjVetexInfo.known)
						queue.addLast(adjVetex);
				}
			}
		}
		
		/*
		 * ͨ����������ʾ������·��
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
	 * ���㵥���ڵ����ȡ�
	 * ������ȾͱȽ��鷳�ˡ��ñ������нڵ�������ڽӱ���ȷ�ϡ�
	 * 
	 * 
	 * @param x
	 * @return
	 */
	public int inDegree(T x) {
		int result = 0;
		
		//���������ڽӱ�
		for(Entry<T, OneVetexAdjacencyList<T>> entry : this.map.entrySet()) {
			//����ǵ�ǰ�ڵ���ڽӱ���ȫ����������
			if(x.equals(entry.getKey())) {
				continue;
			}
			//��ȡ�ߵ�list
			OneVetexAdjacencyList<T> thisAdjList = entry.getValue();
			LinkedList<Edge<T>> edgeList = thisAdjList.getAdjacencyVetexList();
			//�������бߣ������е�ǰ�ڽӵ�����ж���
			for (Edge<T> edge : edgeList) {
				//�����Ķ�����ڽӱ��ڵ��ڽӵ���x�Ļ���������+1
				if(x.equals(edge.getNextVetex())) {
					++result;
				}
			}
		}
		return result;
	}
	
	/**
	 * ���㵥���׶εĳ��ȡ������ص�ǰ������ڽӱ�������ߵ�����
	 * @param x
	 * @return
	 */
	public int outDegree(T x) {
		OneVetexAdjacencyList<T> thisList = this.map.get(x);
		return thisList.getAdjacencyVetexList().size();
	}
	
	/**
	 * ʹ��������������ķ�ʽ����ĳ���ڵ㣬���ҷ�������·����
	 * Ϊ��ʵ������ÿ���ڵ��и�visited������Ϊ�˽�������ļ��ɣ�
	 * �����ڷ����ڲ�����һ��map<T,boolean>������hashMap��ʵ�ֶԸ��ڵ�ķ��ʱ�ǡ�
	 * ��Ȼ���ڿ���ȥ������̻�û��ʲôʵ�ʵ��ô������壬���Ϻ���Ӧ�ûὲ��
	 * 
	 * @param startVetex
	 * @return
	 */
	public void depthFirstSearchAndShowPath(T startVetex) {
		HashMap<T,Boolean> visited = new HashMap<>();
		//��map��ʼ��Ϊȫfalse
		for(T key:this.map.keySet()) {
			visited.put(key, false);
		}
		
		dps(visited, startVetex);
	}
	
	/**
	 * ˽�еĵݹ����������������̡�
	 * @param visited
	 * @param x
	 */
	private void dps(HashMap<T,Boolean> visited, T x) {
		//����ǰ�ڵ�ķ��ʱ����Ϊtrue
		visited.put(x, true);
		
		//bug 1 ������ǰ�ڵ���ڽӱ������Ǳ������нڵ�
		//��ȡ��Ӧ������ڽӱ�
		LinkedList<Edge<T>> adjVetexList = this.map.get(x).getAdjacencyVetexList();
			
		//�������еıߡ������еĶ���
		for (Edge<T> edge : adjVetexList) {
			//����жϸõ��Ƿ��Ѿ������ʹ���
			T nowAdjNode = edge.getNextVetex();
			if(! visited.get(nowAdjNode)) {
				//�ݹ�����ڽӵ�������������
				System.out.print(nowAdjNode);
				dps(visited, nowAdjNode);
			}
		}
		//test
		System.out.println();
	}
	
	/*
	 * Ϊ����ɸ���Ѱ�ң���Ҫ��ÿ����Ӷ��������������ʹ��hashMap<T,additionalInfo>����ʽ���洢��
	 * additionalInfo����Ϣ�Է����ڲ������ʽ���洢��
	 */
	private class ArtInfo{
		int num;//�������
		int low;//��ǰ�ڵ����ܴﵽ����ͽڵ�
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
	 * Ϊ�˸���������������Ľڵ��ţ���Ҫһ�����������¼
	 */
	private int count;
	
	/**
	 * ʹ����������������������ͼ�еĸ��
	 * ��δ��������Ƿ��Ǹ��������ж�
	 * @param startNode ������ȿ�ʼ�����
	 */
	public void findArt(T startNode) {
		//��ʼ����������������HashMap
		HashMap<T, ArtInfo> addInfo = new HashMap<>();
		for(T key : this.map.keySet()) {
			addInfo.put(key, new ArtInfo(0, 0, null, false));
		}
		count = 0;
		assignNum(startNode, addInfo);
		//�������нڵ�ķ��ʱ�־ bug 1
		for(T key : this.map.keySet()) {
			ArtInfo nodeInfo = addInfo.get(key);
			nodeInfo.visited = false;
		}
		//bug 2 ��A��parent��Ϊ����
		ArtInfo startInfo = addInfo.get(startNode);
		startInfo.parent = startNode;
		//�ٽ���low����������������ã�
		assignLow(startNode, addInfo);
	}
	
	/**
	 * ������������������ķ�ʽ�ݹ�ı������еĽڵ㡣
	 * 1. ͨ���жϽڵ�visited�������������ظ���
	 * 2. ͨ���жϵ�ǰ�ڵ�v���ڽӵ�w��num��������Ե�֪��ǰ�ı���ǰ��ߣ����Ǳ���ߡ�
	 * 	   2.1 ��w.num > v.num && w.visitedʱ��Ϊǰ���.
	 * 	   2.2 ��w.num < v.numsʱ�����ҵ�v.parent == wʱ��Ϊ�Ѿ��������Ҫ���Եıߣ����ҵ�v.parent != w ʱ�����Ǳ�����ˡ�
	 * 3. ��2.1�м�ǰ��ߵ�����£�����������ȵ�˼�룬ֱ�ӵݹ鵽��һ�ڵ㣬Ȼ��ݹ���ɺ��ж�w.low >= v.num˵��vΪ��㡣
	 *    ���Ի��������������������⣬>=�������ȸ������ڽӵ���ܴﵽ�������ȶ����ڵ�ǰ�ڵ㣬��ô��ǰ�ڵ�һ��ʱ��㡣
	 *    ���ڵĻ���˵������������㡣��󣬸��µ�ǰ�ڵ��lowֵ������������������Ĺ�ϵ����С��lowֵ�����Ƚ���ĵط���һֱ���ϸ��¡�
	 *    ��ˣ�min(w.low, v.low)���ɡ�
	 * 4.����2.2�У�����ߵ������ֻ��Ҫ�ѵ�ǰ�ڵ�lowֵ���ºþ��С���v.low = min(v.low, w.num)
	 * 
	 * ֵ��ע����ǣ����ж�������ʼ���Ƿ��Ǹ�㣬����ʹ��������������Ҫ����һ����
	 * ����������������ĸ��ڵ���һ�����ϵĶ��ӣ����������ϣ��������Ǹ�㡣��Ϊ�������������������ܹ�ͨ������·������Է���
	 * ��ôһ����������Ҫ���ص�A��ת�ơ�˵��������֮�����ͨ�����ڵ���ͨ����������¸��ڵ���Ǹ�㡣
	 * �ٴα������ڵ���ڽӱ��鿴�ڽӵ��parent,���parent = root���ڽӵ���Ŀ>1����ô���ڵ�Ϊ��㣬����Ͳ���
	 * 
	 * @param v
	 * @param addInfo
	 */
	private void assignLow(T v, HashMap<T, ArtInfo> addInfo) {
		//���ȣ�������˵�ȳ�ʼ���µ�ǰ�ڵ�lowֵ
		ArtInfo vInfo = addInfo.get(v);
		if(vInfo.num == 0) {
			throw new IllegalArgumentException("���ȶԸ��ڵ�����������������ţ�");
		}
		vInfo.low = vInfo.num;
		//bug 3 ��Tm������δ�����ʱ�־��
		vInfo.visited = true;
		//������ǰ�ڵ���ڽӱ�
		LinkedList<Edge<T>> adjList = this.map.get(v).getAdjacencyVetexList();
		for (Edge<T> edge : adjList) {
			//��ȡ�ڽӵ������
			T w = edge.getNextVetex();
			//��ȡ�ڽӵ����Ϣ
			ArtInfo adjInfo = addInfo.get(w);
			//ǰ��ߵ������������α����Ļ����ϣ�����˶�visited������ļ�顣
			if(!adjInfo.visited && adjInfo.num > vInfo.num) {
				//������������ݹ�
				assignLow(w, addInfo);
				//���µ�ǰ�ڵ��lowֵ
				vInfo.low = Math.min(adjInfo.low, vInfo.low);
				//�ݹ�������ɺ��жϵ�ǰ�ڵ��Ƿ��Ǹ��
				if(adjInfo.low >= vInfo.num) {
					//��v�Ǹ��ڵ��ʱ����Ҫ��������Ĳ���
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
					
					System.out.println("��㣺" + v);
				}
			}
			else {
				//�ж��Ƿ��Ǳ���ߣ�ע�⵽����ߵ�w�ڵ����Ѿ����ʹ���
				//�����ǰ�ڵ�ĸ��׵����ڽӱ���ô�����������Ѿ������ʹ��ˡ�����Ҫ�����ظ�����
				//bug 2 ��null����eqaul������������parent��Ϊ������
				if(! vInfo.parent.equals(w)) {
					vInfo.low = Math.min(vInfo.low, adjInfo.num);
				}
			}
		}
	}

	/**
	 * ��������������ķ�ʽ�ݹ�����������������������ؽڵ��š�
	 * �����ƶϼ�������ͼ�еĸ�㡣
	 * 
	 * ˼·�Ͳ��裺
	 * 1. ������ǰ�ڵ���ڽӱ�
	 * 2. ��v.visited��־Ϊfalseʱ���ѵ�ǰ�ڵ��num������ֵ��ţ������ֵ�Ĵ�С����ýڵ�������������ȡ��������������ڽӵ㡣
	 * 3. ��1��2����ݹ鵽���ڽӵ��ϡ�
	 * 
	 * @param x
	 * @param additionalInfo
	 */
	private void assignNum(T x, HashMap<T, ArtInfo> addInfo) {
		ArtInfo thisVetexInfo = addInfo.get(x);
		thisVetexInfo.num = ++count;
		thisVetexInfo.visited = true;//bug 1 ���Ǹ���ǰ�ڵ����ñ����ʱ�־�ˡ�
		//�����ýڵ���ڽӱ�����Ȳ�һ��������ǵݹ����̣�����ǵ������̲�һ��
		//��ȡ��Ӧ������ڽӱ�
		LinkedList<Edge<T>> adjVetexList = this.map.get(x).getAdjacencyVetexList();
		for (Edge<T> edge : adjVetexList) {
			//��ȡ�߶����Ӧ�Ľڵ�
			T adjVetex = edge.getNextVetex();
			//���ýڵ�ķ��ʱ��
			if(!addInfo.get(adjVetex).visited) {
				//��ȡ��ǰ�ڽӵ������Ϣ��
				ArtInfo adjVetexInfo = addInfo.get(adjVetex);
				//��ס���ڵ��·����Ϣ��
				adjVetexInfo.parent = x;
				//��������������ķ�ʽ���¼�����š�
				assignNum(adjVetex, addInfo);
			}
		}
	}
	
	/**
	 * ͬ�������·�����㷨һ����ʹ�÷����ڲ��� + HashMap �ķ�ʽ��ڵ�����Ӷ�����Ϣ��
	 * 
	 * EC:���浱ǰ�ڵ����������¼�
	 * LC:���浱ǰ�ڵ�������ӵ�����¼���
	 * visited:�ѷ��ʱ�־
	 * allPreNode:�������浱ǰ�ڵ����е�ǰ�ýڵ㡣
	 * 
	 * @author 25040
	 *
	 */
	private class CriticalPathInfo {
		int EC;//
		int LC;
		int ST;
		boolean visited;
		LinkedList<T> allPreNode;//�൱�ڹ����˸�ͼ�ķ����ڽӱ�
		
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
	 * ����㿪ʼ���йؼ�·��������
	 * �����ȷ����ǰ�����ͼ����Ȩ������Ȧͼ���ſɱ�֤�������ȷ�ԡ�
	 * ͬʱ���ú����Ὣ��ͼ���ɶ����ڵ�ͼ��������ע�������ӿ�ʼ�ͽ����ڵ㡣
	 * 
	 * ��ǰ����ͨ��������������ķ�ʽ��ʹ�ö��������ݽ��Ľ��м��㡣
	 * ���нڵ���������ʱ��ԭ����ʽ��˼·��
	 * ����ԭ��
	 * EC_1 = 0��
	 * EC_i	= max{EC_w + weight_w,v};
	 * ���߱��ʽ����˵��ǰ�ڵ��������ʱ�䣬�����У�ǰ�ýڵ���������ʱ�� + ǰ�ýڵ㵽��ǰ�ڵ��Ȩֵ�������ֵ������
	 * 
	 * ��EΪ��ǰ�¼��ڵ���������¼�
	 * 
	 * ˼·�ͻ������裺
	 * 1. ��ʼ�����������������E��Ϊ0��
	 * 2. �������ڵ���ڽӵ㣬Ӧ�ü��㹫ʽEC_w = Ec_v + edge_weight,�������õ��Ľ���ȸ��ڽӵ��Ѿ��洢��ֵ������¡����򲻸��¡�
	 * 3. ��δ���ʹ����ڽӵ���ӡ�
	 * 
	 * ���нڵ���������ʱ�����ԭ��͹�ʽ��
	 * ����ԭ��͹�ʽ��
	 * EL_n = EC_n;
	 * LC_v = min(LC_w - c_v,w);
	 * 
	 * �������ʽͨ�׵�˵������˵��ǰ�ڵ���������ʱ�䣬�����У����ýڵ���������ʱ�� - ���ýڵ㵽��ǰ�ڵ��Ȩֵ������Сֵ����
	 * 
	 * ˼·�Ͳ��裺�������漰�����ƣ�
	 * 1.��ʼ�������������β���LCֵ��Ϊ�õص�ECֵ��
	 * 2.������ǰ�ڵ��ǰ���ڽӱ�Ӧ�ü��㹫ʽEC_pre = Ec_v - edge_weight,�������õ��Ľ���ȸ��ڽӵ��Ѿ��洢��ֵС������¡����򲻸��¡�
	 *   ͬʱ���ù�ʽST_pre = LC_v - E_pre - weight,���µ�ǰʱ����������ʱ�䡣
	 * 3. ��δ���ʹ��Ľڵ���ӡ�
	 * 
	 * ���ڶ�д����麯����ȷ���û�������ڽӵ㣬���ж�Ӧ�Ľڵ�洢��
	 * 
	 * @param startVetex
	 */
	public void criticalPathAnalysis(T startVetex, T endVetex) {
		//��ֹzz
		if((this.map.get(startVetex) == null) || (this.map.get(endVetex)) == null) {
			System.out.println("����ȷ�������ͽ�����");
			return;
		}
		//ʹ��һ��linkedList����������ʹ��
		LinkedList<T> queue = new LinkedList<>();
		HashMap<T, CriticalPathInfo> InfoMap = new HashMap<>();
		
		assignEC(queue, InfoMap, startVetex);
		assignLCAndST(queue, InfoMap, endVetex);
		
		//test output
		for(T x : this.map.keySet()) {
			if(x.equals(startVetex) || x.equals(endVetex)) {
				continue;
			}
			System.out.println("�¼���" + x + "\t�������ʱ��Ϊ��\t" + InfoMap.get(x).EC + 
					"\t�����ӽ��ȵ�������������ʱ�䣺\t" + InfoMap.get(x).LC + 
					"\t\t�¼��������ӵ�ʱ��Ϊ:\t" + InfoMap.get(x).ST);
		}
	}
	
	/**
	 * ����info�е�allPreNode��EC��ʹ�÷��������������ķ�ʽ���¸�ʱ��ڵ��LC��STֵ
	 * ��������������ȵ�����£���ʱ����������ʱ��͸�ʱ��ĵ���ʱ�䣨���������ʱ�䣩��
	 * @param queue
	 * @param InfoMap
	 * @param endVetex
	 */
	private void assignLCAndST(LinkedList<T> queue, HashMap<T, CriticalPathInfo> InfoMap, T endVetex) {
		//��β�ڵ��LCֵ��ΪEnֵ
		CriticalPathInfo endVInfo = InfoMap.get(endVetex);
		endVInfo.LC = endVInfo.EC;
		
		//����Info��allPreNode�������������������õ������¼����������ʱ��
		queue.addLast(endVetex);
		while(! queue.isEmpty()) {
			//��ȡ��ǰ�ڵ��������Ϣ,���ұ���ѷ���
			T vNode = queue.removeFirst();
			CriticalPathInfo vInfo = InfoMap.get(vNode);
			//ע����һ���������������������ʹ��Ľڵ�����Ϊtrue�����Ѿ����ʹ���
			//��һ�־ͽ�������Ϊfalse�����Ѿ����ʹ���
			vInfo.visited = false;
			
			//������ǰ�ڵ��ǰ���ڽӱ�
			LinkedList<T>  preAdjList = InfoMap.get(vNode).allPreNode;
			for (T preNode : preAdjList) {
				//��ȡ��ǰǰ���ڽӵ㸽��������
				CriticalPathInfo preInfo = InfoMap.get(preNode);
				//��ȡ��ǰ ǰ���ڽӵ㵽�ýڵ�ı�Ȩֵ,��һ���������ķ�������ʱ�䣬�Ժ��ܷ�Ľ�
				int weight = 0;
				LinkedList<Edge<T>> temp = this.map.get(preNode).getAdjacencyVetexList();
				for (Edge<T> edge : temp) {
					if(edge.getNextVetex().equals(vNode)) {
						weight = edge.getWeight();
					}
				}
				
				//�жϹ�ʽ����������LC������,�����ʽ���������ִ��LC��С���򱣴�
				//���ͬʱ�����ǻ����������ǰ�¼�����������¼�
				if(vInfo.LC - weight < preInfo.LC) {
					preInfo.LC = vInfo.LC - weight;
					preInfo.ST = vInfo.LC - preInfo.EC - weight;
				}
				
				//����δ���ʵ�ǰ�ýڵ����,ע����һ�־ͽ�������Ϊfalse�����Ѿ����ʹ���
				//bug 2 ǰ���ڽӱ��е���ң�д��vInfo
				if(preInfo.visited) {
					queue.addLast(preNode);
				}
			}
		}
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * ʹ�ù���������صķ�ʽ��������ڵ��ECֵ������ǰ������ɵ�����ʱ��
	 * @param queue
	 * @param InfoMap
	 * @param startVetex
	 */
	private void assignEC(LinkedList<T> queue, HashMap<T, CriticalPathInfo> InfoMap, T startVetex) {
		//��ʼ������
		queue.addLast(startVetex);
		for(T vetex : this.map.keySet()) {
			InfoMap.put(vetex, new CriticalPathInfo(0, Integer.MAX_VALUE, 0, false));
		}
		
		//��������������õ����м����������¼�����ʵ�൱�ڱ��������еı�
		while(! queue.isEmpty()) {
			//��ȡ��ǰ�ڵ��������Ϣ,���ұ���ѷ���
			T vNode = queue.removeFirst();
			CriticalPathInfo vInfo = InfoMap.get(vNode);
			vInfo.visited = true;
			
			//������ǰ�ڵ���ڽӱ�
			LinkedList<Edge<T>> adjList = this.map.get(vNode).getAdjacencyVetexList();
			for (Edge<T> edge : adjList) {
				//��ȡ��ǰ���ڽӵ�,��Ȩֵ���ڽӵ�ĸ���������
				T adjw = edge.getNextVetex();
				int weight = edge.getWeight();
				CriticalPathInfo wInfo = InfoMap.get(adjw);
				
				//�жϲ��Ҹ����ڽӵ��EC������
				if(vInfo.EC + weight > wInfo.EC) {
					wInfo.EC = vInfo.EC + weight;
				}
				
				//��δ���ʹ����ڽӵ�ѹ�������
				if(! wInfo.visited) {
					queue.addLast(adjw);
				}
				
				//�ѵ�ǰ�ڵ���뵽���ڽӵ��allPreNode�����У����ں����ļ���
				wInfo.addPreNode(vNode);
			}
		}
		
	}
	
	/**
	 * �ѵ�ǰ�����е���Ȩ��Ȧͼ���ɶ����ڵ�ͼ��������Ӧ��ʱ��ڵ�ͼ��
	 * ���Ҵ�ӡ��ʾ����Ӧ���¼��ڵ�ͼ
	 * 
	 * ����2��������ԭ������ԭ������һ�����������ڶ��ǰ�ö���������£�������Ҫ�����Ʊߺ��ƽڵ�����������������Եĸ��
	 * 
	 * ˼·���裺
	 * 1. �������нڵ���ȣ����ҽ���ȴ���1�Ľڵ㣬��¼��һ��map<T, boolean>��
	 * 2. ����map��size���������ɵ�ͼ���ܽڵ�����
	 * 3. ����㿪ʼ������ÿһ���ڵ���ڽӱ�������ԭͼ�����еıߡ�˼·���ǹ��������������,ʹ��һ��queue����ɡ�
	 * @return
	 */
	public HashMapAdjacencyList<T> bulitEventNodeGraph(){
		return null;
	}
}

/**
 * ����һ���ڽӱ����
 * 
 * @author 25040
 *
 * @param <T>
 */
class OneVetexAdjacencyList<T>{
	//�ڽӱ�������������
	/*
	 * ���������ȷʵ��ʹ��hashMap��ʱ��û���ô���
	 */
	private T self;
	//�ڽӱ�����selfָ��������ڽӵ�,ʹ��HashMap�����ǾͲ���Ҫ����
	private LinkedList<Edge<T>> edges;
	
	/**
	 * ����һ���ڽӱ�
	 */
	public OneVetexAdjacencyList(T self) {
		this.self = self;
		this.edges = new LinkedList<>();
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
		return this.edges.isEmpty();
	}
	
	/**
	 * ��ȡ��ǰ������м����ڽӵ㣬����ǰ������ڽӵ���Ŀ
	 * @return
	 */
	public int adjacencyVetexNum() {
		return this.edges.size();
	}
	
	/**
	 * �ڵ�ǰ������ڽӱ��в��뵽�ڽӵ㣬�������øñߵ�ȨֵΪ1
	 * @param otherVetex
	 */
	public void insertAdjcancyVetex(T otherVetex) {
		insertAdjacencyVetex(otherVetex, 1);
	}
	
	/**
	 * �ڵ�ǰ������ڽӱ��в��뵽�ڽӵ㣬�������øñߵ�Ȩֵ
	 * @param otherVetex
	 */
	public void insertAdjacencyVetex(T otherVetex, int weight) {
		this.edges.addLast(new Edge<>(weight, otherVetex));
	}
	
	/**
	 * �ڵ�ǰ������ڽӱ��в������ڽӵ�
	 * @param otherVetexs
	 * @throws java.lang.IllegalArgumentException-�������ڽӵ������Ȩֵ�������Ŀ��ƥ��
	 */
	public void insertAdjacencyVetex(T[] otherVetexs, int[] allEdgesWeight) {
		if(otherVetexs.length != allEdgesWeight.length) {
			throw new IllegalArgumentException("�������ڽӵ������Ȩֵ�������Ŀ��ƥ�䣡");
		}
		for(int i = 0;i < otherVetexs.length; ++i) {
			insertAdjacencyVetex(otherVetexs[i],allEdgesWeight[i]);
		}
	}
	
	/**
	 * ��ʽ�������ǰ������ڽӱ�
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
	 * ����Ȩͼʹ��
	 * @param nextVetex
	 */
	public Edge(T nextVetex) {
		this(1, nextVetex);
	}
	
	/**
	 * ����Ȩͼ����߶���ʹ��
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
	 * �߶���ĸ�ʽ�����
	 */
	public String toString() {
		return (nextVetex +"("+ weight +")"); 
	}
}

