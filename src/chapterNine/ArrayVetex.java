package chapterNine;

import java.awt.geom.Dimension2D;

import chapterFour.ExtendingTree;
import chapterThree.MyArrayList;
import chapterThree.MyArrayQueue;

/**
 * 使用数组实现邻接表，完成书中的拓扑排序
 * @author 25040
 *
 */
public class ArrayVetex<T> {
	
	public static void main(String[] args) {
		String[] nodeArray = new String[] {"v1","v2","v3","v4","v5","v6","v7"};
		ArrayVetex<String> graph = new ArrayVetex<>(nodeArray);
		
		graph.builtConnection("v1", new String[] {"v2","v3","v4"});
		graph.builtConnection("v2", new String[] {"v4","v5"});
		graph.builtConnection("v3", new String[] {"v6"});
		graph.builtConnection("v4", new String[] {"v3","v6","v7"});
		graph.builtConnection("v5", new String[] {"v4","v7"});
		graph.builtConnection("v6", new String[] {});
		graph.builtConnection("v7", new String[] {"v6"});
		
		graph.printVetexList();
		
		
		try {
			graph.topsortByQueue();;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * 以ArrayLisy存储邻接表
	 */
	private MyArrayList<Vetex<T>> vetexList;
	
	/**
	 * 有参构造，根据数组来初始化顶点邻接表。
	 * @param arr
	 */
	public ArrayVetex(T[] arr) {
		this.vetexList = new MyArrayList<>();
		for (int i = 0; i < arr.length; i++) {
			Node<T> newNode = new Node<T>(arr[i]);
			this.vetexList.add(new Vetex<>(newNode));
		}
	}
	
	/**
	 * 建立顶点之间的邻接关系。
	 * 
	 * @param self 当前顶点的内容
	 * @param others 其他邻接的顶点的内容
	 */
	public void builtConnection(T self, T[] others) {
		Vetex<T> whichVetex = findWhichVetexAndNotExsitAddNewNode(self);
		
		for(int i = 0;i < others.length;++i) {
			Node<T> otherOneNode = findWhichVetexAndNotExsitAddNewNode(others[i]).self;
			whichVetex.addAdjcacencyNode(otherOneNode);
		}
		
	}
	
	/**
	 * 根据邻接表self顶点的内容来确定在哪个邻接表，
	 * 如果该顶点还不存在就是创建新的顶点对象以及创建该顶点对象的邻接表。
	 * 时间界限为线性。因此getVetexBySelfElement函数为线性。
	 * @param self 顶点的内容
	 * @return self对象对应的邻接表对象的引用
	 */
	private Vetex<T> findWhichVetexAndNotExsitAddNewNode(T self) {
		Vetex<T> whichVetex = getVetexBySelfElement(self);
		if(whichVetex == null) {
			Node<T> newNode = new Node<T>(self);
			this.vetexList.add(new Vetex<>(newNode));
			return this.vetexList.get(this.vetexList.size() - 1);
		}
		else {
			return whichVetex;
		}
	}
	
	/**
	 * 根据数组下标索引值返回邻接表的引用,常数操作
	 * @param idx
	 * @return
	 */
	private Vetex<T> getVetexByIndex(int idx) {
		return this.vetexList.get(idx);
	}
	
	/**
	 * 根据邻接表顶点的内容，遍历邻接表数组，寻找邻接表self顶点的内容符合要求的那个邻接表引用。
	 * 时间界限为线性。
	 * @param nodeElement
	 * @return
	 */
	private Vetex<T> getVetexBySelfElement(T nodeElement){
		int i;
		//遍历搜索self的内容属性为nodeElement的邻接表
		for(i = 0;i < this.vetexList.size();++i) {
			//判断两者是否相等
			if(getVetexByIndex(i).getSelfNodeElement().equals(nodeElement))
				break;
		}
		
		if(i != this.vetexList.size()) {
			return getVetexByIndex(i);
		}
		else {
			return null;
		}
	}
	
	/**
	 * 拓扑排序，想要说明的是，顶点之间必须要找的顺序，而不是列举图所有的路径可能性。
	 * 这一点我开始有点误解了，而且到现在我也认为我的代码数据结构没有组织好。
	 * 
	 * 这里勉强是弄懂了。这里是针对有向无圈图求它的拓扑顺序。
	 * 1. 首先，我们需要求解的时拓扑顺序，因此，循环的次数我们时已知的。即数组的size；
	 * 2. 我们通过findNewVertexOfIndegreeZeroAndReturnIndex函数得到当前入度为0的顶点的邻接表下标；
	 * 3. 然后该顶点其下（邻接）的所有的顶点的入度-1；
	 * 4. 删除该邻接表。
	 * 5. 依次删除的顺序就是拓扑顺序。
	 * 
	 * 分析时间界限：
	 * 1. findNewVertexOfIndegreeZeroAndReturnIndex-线性
	 * 2. MyArrayList.remove——线性
	 * 3. 内层for——线性
	 * 因此，总的为顶点数的二次方
	 * 
	 * @throws Exception 当该图不是有向无圈图时抛出异常
	 */
	public void topsort() throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("拓扑顺序：");
		int num_vetex = this.vetexList.size();
		
		for(int i = 0; i < num_vetex;++i) {
			int v = findNewVertexOfIndegreeZeroAndReturnIndex();
			
			if(v < 0) {
				throw new Exception("该图有圈！");
			}
			
			sb.append(getVetexByIndex(v).self.element).append("->");
			//所有与当前点相连的点的入度都-1
			for(int j = 0;j < getVetexByIndex(v).adjacencyList.size();++j) {
				getVetexByIndex(v).adjacencyList.get(j).indegree--;
			}
			//还有删除当前节点
			this.vetexList.remove(v);
		}
		
		System.out.println(sb.toString());
	}
	
	/**
	 * 使用数组队列来优化时间界限。优化来自于每条边都只被计算了一次。
	 * @throws Exception 
	 */
	public void topsortByQueue() throws Exception {
		int counter = 0;
		StringBuffer sb = new StringBuffer();
		sb.append("拓扑顺序：");
		MyArrayQueue<Vetex<T>> queue = new MyArrayQueue<>(10);
		
		/*
		 * 对每个邻接表进行扫描，耗费V（顶点数）次
		 */
		for (int i = 0;i < this.vetexList.size();++i) {
			if(getVetexByIndex(i).self.indegree == 0) {
				queue.enqueue(getVetexByIndex(i));
			}
		}
		
		/*
		 * 入度实际上为指向该顶点边的数目，虽然这里有for嵌套while，
		 * 但是我们深入到if语句判断可知，该段程序实际上只是对每条边计算了一次，也就是
		 * 每个入度执行了-1操作，直到入度为0.
		 * 因此，该段程序的时间界限为E（边数）
		 */
		while(!queue.isEmpty()) {
			Vetex<T> v = queue.dequeue();
			
			sb.append(v.self.element).append("->");
			
			for(int i = 0;i < v.adjacencyList.size();++i) {
				if(--v.adjacencyList.get(i).indegree == 0) {
					//如果某个顶点的入度为0了就进队
					queue.enqueue(getVetexByIndex(v.adjacencyList.get(i).topNum));
				}
			}
			//用于判断图是否有圈
			++counter;
		}
		System.out.println(sb.toString());
		
		if(counter != this.vetexList.size()) {
			throw new Exception("该图有圈，拓扑排序必须是有向无圈图！");
		}
		
	}
	
	/**
	 * 找到入度为0的邻接表的下标，没找到则返回-1
	 * @return
	 */
	private int findNewVertexOfIndegreeZeroAndReturnIndex(){
		for(int i = 0;i < this.vetexList.size();++i) {
			if(getVetexByIndex(i).self.indegree == 0) {
				return i;
			}
		}
		
		return -1;
	}
	
	/**
	 * 打印整个邻接表
	 */
	public void printVetexList() {
		//遍历邻接表数组
		for(int i = 0; i < this.vetexList.size(); ++i) {
			//对每一个邻接表先打印自己
			System.out.println(this.vetexList.get(i));
		}
	}
	
	/**
	 * 顶点邻接表类，需要存储当前的顶点对象以及邻接的顶点链表对象
	 * （这里为了方便分析使用自己写的MyArrayLIstanbul，顺便也可以检验自己写的集合类）
	 * 
	 * 
	 * 使用List来存储当前点相邻
	 * @author 25040
	 *
	 */
	private static class Vetex<T>{
		private Node<T> self;
		private MyArrayList<Node<T>> adjacencyList;
		
		public Vetex(Node<T> self) {
			this.self = self;
			this.adjacencyList = new MyArrayList<>();
		}

		/**
		 * 向邻接表中插入新的相邻顶点,直接使用ArrayList的add方法。
		 * 并且将当前顶点的入度 + 1。
		 * 由于内存中实际上就应该存在n个实际的节点对象（我们邻接表中存储的都是引用）。
		 * 时间界限为常数
		 */
		public void addAdjcacencyNode(Node<T> newAdjcacencyNode) {
			this.adjacencyList.add(newAdjcacencyNode);
			newAdjcacencyNode.indegree++;
		}
		
		/**
		 * 返回当前链接表的self顶点的内容
		 * @return
		 */
		public T getSelfNodeElement() {
			return this.self.element;
		}
		
		/**
		 * 格式化输出邻接表
		 */
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("[");
			sb.append("self:").append(self);
			for(int i = 0;i < this.adjacencyList.size();++i) {
				sb.append("->").append(this.adjacencyList.get(i));
			}
			sb.append("]");
			
			return sb.toString();
		}
	}
	
	/**
	 * 顶点类，为了计算拓扑顺序，我们根据书上的提示得知，每个顶点就拥有三个属性：
	 * 1. 顶点自身的关键字。
	 * 2. 入度。我们把顶点v的入度定义为边(u,v)的条数。
	 * 
	 * @author 25040
	 *
	 */

	private static class Node<T>{
		private static int topNumCnt = 0;
		
		private int topNum;
		private T element;
		private int indegree;
		
		public Node(T element) {
			this.topNum = topNumCnt++;
			this.element = element;
			this.indegree = 0;
		}
		
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("(");
			
			sb.append("id:").append(topNum).append(",");
			sb.append("element:").append(element).append(",");
			sb.append("indegree:").append(indegree);
			
			sb.append(")");
			
			return sb.toString();
		}

		
	}
}

