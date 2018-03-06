package chapterNine;

import chapterThree.MyArrayQueue;
import chapterThree.MyLinkedList;

/**
 * 
 * ���ͼ�ڽӱ��ʾ������MyArrayList�ᵽ�ĸĽ����뷨
 * 
 * �ܽ���˵������ݽṹ�Ļ���ԭ�����£�
 * 1.ȡ���ڵ��࣬��ͨ���ڵ���������󡣶�������ÿ���ڵ����˱�ž��Ƕ�Ӧ������ֵ���ٽ�������ֻ�洢�ٽ������˱���Լ���Ӧ������ֵ
 * 2.����ʹ��MyArrayList����������֯���ݣ�̫�鷳�ˣ������е��˷ѿռ䡣ֱ��ʹ����������֯��
 * 	�и������ǣ�ͼ����չ���ܵ����ƣ���һ��ͼ�����죬���Ա��޸Ļ���˵�޸ĵĴ��ۺܴ�
 * 	�ô��ǣ���̼�ֱ�ӡ�
 * 
 * ʹ�����ַ�ʽ����һ��ͼ��������������ͼ�Ĺ���׶Σ�Ҳ���ǵ������Ĺ��캯�������б���ȫ���Ѷ������룬�����������ӵ�ʱ����������µĶ���
 * 
 * ����ĸĽ����뷨��
 * 1. ʹ��hashMap��LinkedList���洢�ڽӱ���һ��������ܡ�
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
	 * ���ԡ��洢�ڽӱ�����
	 * �Լ�������������Ľ��
	 */
	private OneWeightedAdjacencyList<T>[] adjecenyLists;
	private table[] breadthFirstSearchResult;
	
	/**
	 * ʹ�����еĶ���ؼ��ֹ������е��ڽӱ�
	 * ͼ�ڹ���Ĺ����У�������أ�������������Ĺؼ�����ͬ ��Ҳ��������ͬ�Ķ��㡣
	 * ���㲻ͬ��Ψһ����������������ֵ��
	 * �мǲ��ܴ����ظ���Ԫ�أ�����Ҫ���߼�����
	 * 
	 * ע��㣺
	 * 1.this.adjecenyLists =  new OneWeightedAdjacencyList[allVetexElements.length];
	 * ������ʹthis.adjecenyLists =  (OneWeightedAdjacencyList<T>[])new Object[allVetexElements.length];
	 * �׳�java.lang.ClassCastException
	 * 
	 * @param AllVetexElements ���еĶ���ؼ���
	 */
	@SuppressWarnings("unchecked")
	public ArrayVetex(T[] allVetexElements) {
		//
		this.adjecenyLists =  new OneWeightedAdjacencyList[allVetexElements.length];
		//��������ؼ������飬���ι�����Ӧ���ڽӱ�
		for(int i = 0;i < allVetexElements.length; ++i) {
			//���أ���ֹ������ӵ��������ͬ�Ĺؼ��֡����Կ���ʹ��map������
			//getIndexByVetexElement(allVetexElements[i], 0, i - 1);
			//ÿ�����㹹��һ���ڽӱ�
			this.adjecenyLists[i] = new OneWeightedAdjacencyList<>(allVetexElements[i]);
		}
	}
	
	/**
	 * ����������֮�佨��һ����Ȩֵ�ıߡ�
	 * ������Ҫɨ�������Ի���±꣬����ɨ����Ҫ���ҪV������������Ρ��÷������Ҫ2 * V�Σ����ԡ�
	 * 
	 * @param selfVetexElement (u,v)��Ե�u�����ߵ���ʼ
	 * @param otherVetexElement (u,v)��Ե�v�����ߵ���ֹ
	 * @param weight (u,v)�ߵ�Ȩֵ
	 */
	public void builtWeightedEdgeWithOneVetexAndOtherOneVetex(T selfVetexElement, T otherVetexElement, int weight ) {
		OneWeightedAdjacencyList<T> whichVetex = this.adjecenyLists[getIndexByVetexElement(selfVetexElement)];
		whichVetex.builtEdgeWithOtherVetex(getIndexByVetexElement(otherVetexElement), weight);
	}
	
	/**
	 * ����������֮�佨��һ��ȨֵΪ1�ıߡ�����
	 * 
	 * @param selfVetexElement
	 * @param otherVetexElement
	 */
	public void builtEdgeWithOneVetexAndOtherOneVetex(T selfVetexElement, T otherVetexElement) {
		builtWeightedEdgeWithOneVetexAndOtherOneVetex(selfVetexElement, otherVetexElement, 1);
	}
	
	/**
	 * ʹ��һ�����������������㽨����Ȩֵ�ıߡ�ʱ�����ΪV�Ķ��η�
	 * @param selfVetexElement ��������ߵ����	
	 * @param otherVetexElements �������㣬����������ߵ��յ㹹�ɵ�����
	 */
	public void builtEdgeWithOneVetexAndSomeOtherVetex(T selfVetexElement, T[] otherVetexElements) {
		OneWeightedAdjacencyList<T> whichVetex = this.adjecenyLists[getIndexByVetexElement(selfVetexElement)];
		
		for(int i = 0; i < otherVetexElements.length; ++i) {
			whichVetex.builtEdgeWithOtherVetex(getIndexByVetexElement(otherVetexElements[i]), 1);
		}
	}
	
	/**
	 * ����ͼ�ж���ĸ���
	 * @return
	 */
	public int size() {
		return this.adjecenyLists.length;
	}
	
	/**
	 * ��ȫ���������鷶Χ�����αȶ����ж���Ĺؼ��֣����ض�����������������߽��±꣬����˵���˱�ţ�.
	 * ���������׳��쳣��
	 * @param vetexElement
	 * @return
	 */
	public int getIndexByVetexElement(T vetexElement) {
		return getIndexByVetexElement(vetexElement, 0, size() - 1);
	}
	
	/**
	 * ����һ����Χ���ڽӱ����飬���αȶ����ж���Ĺؼ��֣����ض�����������������߽��±꣬����˵���˱�ţ�.
	 * ����������򷵻�-1��
	 * 
	 * ͬʱ�����������˲��ء���ʵ���Կ���ʹ��map
	 * 
	 * ʱ�����Ϊ���ԡ�
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
			throw new IllegalArgumentException("��ͼ���ж����в����ڸö���");
		}
		else {
			return index;
		}
	}
	
	
	public T getVetexElementByIndex(int vetexNum) {
		return this.adjecenyLists[vetexNum].getVetexElement();
	}
	
	/**
	 * ͼ�ڽӱ�ĸ�ʽ�������
	 */
	public void printGraph() {
		for (OneWeightedAdjacencyList<T> oneWeightedAdjacencyList : adjecenyLists) {
			System.out.println(oneWeightedAdjacencyList);
		}
	}
	
	/**
	 * @since 2018-3-5 
	 * ������Ȧͼ�����·������.
	 * ���Ƶ�ʹ�÷����ڲ������������·����
	 * ��ģ��е��±ơ�
	 * @since 2018-3-6
	 * ���������Ҿ͸���úý����£��������֮������ȥ�ɺ������
	 * ����������ϵĻ���˼·��
	 * 1.���ȶ���һ�����forѭ��������һ����ʱ·�����ȣ�����������������Ϊ0-��Զ��������ж��㡣forѭ������ֹ���������ǿ�������Ϊ
	 * 		һ��ͼ�Ŀ��ܳ��ֵ����·�������Ƕ���ĸ�������ʱ��ͼ����ʽ���Ǹ�����
	 * 2.�жϵ�ǰ����������Щ��û����λknownΪtrue�Ķ��㣬����Щ����for���������ڽӵ㣬����Щ�ٽ��ľ������Ը�Ϊ
	 * 		��ǰcurrDist + 1��ֵ��Ȼ��ÿ���ڽӵ������pv ��Ϊ ��ǰ��������˱�š�
	 * 
	 * �ɴˣ����ǿ����Թ�����������ķ�ʽ�������еľ���ĵ㡣���㷨��ʱ�����ΪV�Ķ��η����൱��Ч��
	 * ��Ҫԭ���ڣ�����ܶ�����ѭ���ĸ���û�б�Ҫ������Ϊ���ܹ���������������ֱ�������д��
	 * ��һ������£��������еĶ����known���Զ���true�����ǻ���Ҫ����ѭ�������жϡ�
	 * 
	 * �Լ�zz�ĸĽ��뷨������Ȼ����ʹ�ö��кõ㣩
	 * ����ʹ�ö����⣬�һ���һ���󵨵��뷨��ʹ��һ����������Ҳ���Խ��������⡣�����ܽ������ѭ���Ĵ�����
	 * �������ͼ����������������Σ��ͻ���û��˿���Ľ���
	 * 
	 * 
	 * �У�����˹���������������ʣ����������£���ô��ʾ��ʵ��·����������˵ͨ��pv�����Ļ��ݼ��ɡ�
	 * @param startVetex ����Ӧ�����˱��
	 */
	public void breadthFirstSearch(T startVetex) {
		table[] nodeTable = new table[size()];
		
		for(int i = 0;i < nodeTable.length; ++i) {
			nodeTable[i] = new table(i, false, Integer.MAX_VALUE, 0);
		}
		
		nodeTable[getIndexByVetexElement(startVetex)].dv = 0; 
		int knownVetex = 1;
		
endSearch:		
		//�������п��ܳ��־���Ķ��㣬�������������
		for(int currDist = 0;currDist < size();++currDist) {
			//����ÿ�����㣬
			for (table vetex : nodeTable) {
				//�����ж����еĶ����known�����Ƿ���δ֪�ĺ�dv�����ǵ�ǰ������·����
				if( !vetex.known && vetex.dv == currDist) {
					//��λ��ǰ�����known
					vetex.known = true;
					//������ǰ����������ڽӵ㣨Ҳ��������������еı߶���
					for(chapterNine.ArrayVetex.OneWeightedAdjacencyList.Edge edge : this.adjecenyLists[vetex.VetexNum].getAdjacencyEdges()) {
						table adjecenyVetex = nodeTable[edge.adjacencyVetex];
						//�жϵ�ǰ������ڽӵ�û�б�����������λ��·������������ܳ���ͬһ���㾭������·����
						if(adjecenyVetex.dv == Integer.MAX_VALUE) {
							//�ڽӵ��dv����Ϊ��ǰ����+1
							adjecenyVetex.dv = currDist + 1;
							//���Ǳ����������ڽӵ���Լ�ס���ĸ���������·��������
							adjecenyVetex.pv = vetex.VetexNum;
							/*
							 * ����������ĸĽ����֡�������ȫ�˳������forѭ����ʹ�ô��б�ǩ��break;
							 */
							if(++knownVetex == size()) {
								break endSearch;
							}
						}
						
					}
				}
			}
		}
		//������浽�������
		this.breadthFirstSearchResult = nodeTable;
	}
	
	/**
	 * ���㲢�Ҵ�ӡ������������Ľ��������ǻ�δ�������������Ƚ�������֮���ٴ�ӡ
	 * 
	 * �����ӡ�������·���㷨�����ӣ������е㲻�ñ�ʾ�����ڿ��ܲ��ܽ��B������ӡ��ʾ�����ǲ�ͬ���ǣ�����û�а�����򣬹ؼ������ֽṹ���Լ��������ô�ӡ��ʾ��
	 * �ܲ��ܲο�linuxϵͳ��tree������ 
	 * 
	 * ��Щѭ��������get�������������ۣ�һ��get����һ������ɨ��ʱ�䣬���˰����´�һ��ʹ��map���Ľ���
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
		
		//����ʵ�ʿ��ܵ�·��
		//���Ƶ�ʹ��queue���С�
	}
	
	/**
	 * ʹ�ö���������������������Ч�ʡ�
	 * 
	 * ԭ��
	 * ����һ��ʼֻ�þ���currDist�Ӷ�β��ӣ������ٱ������ڽӵ�ʱ�����ڽӵ㣨������ΪcurrDist + 1���Ӷ�β��ӡ�
	 * ����һ����currDist�����ж���ȫ��������ɺ��ٴ���currDist + 1�Ķ��㡣��������������ķ�ʽ��
	 * 
	 * ���㷨�����˵�һ���㷨�Ķ�����õļ��
	 * 
	 * @param startVetex ���
	 */
	public void breadthFirstSearchByQueue(T startVetex) {
		MyArrayQueue<table> queue = new MyArrayQueue<>(10);
		table[] nodeTable = new table[size()];
		
		for(int i = 0;i < nodeTable.length; ++i) {
			nodeTable[i] = new table(i, false, Integer.MAX_VALUE, 0);
		}
		
		nodeTable[getIndexByVetexElement(startVetex)].dv = 0; 
		//������
		queue.enqueue(nodeTable[getIndexByVetexElement(startVetex)]);
		//
		while(! queue.isEmpty()) {
			//������ȡ����
			table vetexTable = queue.dequeue();
			//�����ö���������ڽӵ�,��ΪEdge����OneWeightedAdjcacencyList���ڲ��࣬���Ա���д�� + ���·��ȫ��
			for(chapterNine.ArrayVetex.OneWeightedAdjacencyList.Edge edge : this.adjecenyLists[vetexTable.VetexNum].getAdjacencyEdges()) {
				//�������ڲ���������һ�ַ�������
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
	 * ���ڼ������·���ñ�
	 * 
	 * @author 25040
	 *
	 */
	private static class table{
		int VetexNum; //����ı��
		boolean known = false;//�ڹ�����������Ĺ����Ƿ��Ѿ����ҵ��ı�־
		int dv = Integer.MAX_VALUE;//��¼��㵽�õ��·����
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
	 * �ڽӱ����
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
		 * ������ǰ�������������ıߣ����Ҹ�Ȩֵ��
		 * ʱ�����Ϊ������
		 * 
		 * @param otherVetexIndex
		 * @param edgeWeight
		 */
		public void builtEdgeWithOtherVetex(int otherVetexIndex, int edgeWeight) {
			this.adjacencyEdges.add(new Edge(otherVetexIndex, edgeWeight));
		}
		
		/**
		 * �ڽӱ�ĸ�ʽ�������
		 * @return ��ʽ���ַ���
		 */
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("[");
			
			sb.append(this.topNum).append("-> ");
			for (Edge edge : adjacencyEdges) {
				sb.append(edge.adjacencyVetex).append(", ");
				//sb.append("(Ȩ:").append(edge.getWeight()).append(")").append("->");
			}
			
			sb.append("]");
			return sb.toString();
		} 
		
		/**
		 * �߶��󣬾������Ϊһ�����м�ͷ�ıߡ�
		 * �����������ԣ���ͷָ�򶥵�������������±꣬���������ʵ�ڻ����ڽӱ�����У����ž������Ӧ���±꣬
		 * �ڽӱ�����element�����൱�ڶ�������ݣ�
		 * 
		 * @author 25040
		 *
		 */
		private static class Edge {
			//�ٽ���Ż���˵��������
			private int adjacencyVetex;
			//�ñߵ�Ȩ
			private int weight;
			
			/**
			 * �ߵĹ��캯���������ͷָ����Ǹ����㣬�Լ�Ȩֵ��������ڽӱ������ʹ��
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
