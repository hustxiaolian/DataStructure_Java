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
		 * �����ڲ��࣬������̰���㷨��
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

