package chapterNine;

import java.awt.geom.Dimension2D;

import chapterFour.ExtendingTree;
import chapterThree.MyArrayList;
import chapterThree.MyArrayQueue;

/**
 * ʹ������ʵ���ڽӱ�������е���������
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
	 * ��ArrayLisy�洢�ڽӱ�
	 */
	private MyArrayList<Vetex<T>> vetexList;
	
	/**
	 * �вι��죬������������ʼ�������ڽӱ�
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
	 * ��������֮����ڽӹ�ϵ��
	 * 
	 * @param self ��ǰ���������
	 * @param others �����ڽӵĶ��������
	 */
	public void builtConnection(T self, T[] others) {
		Vetex<T> whichVetex = findWhichVetexAndNotExsitAddNewNode(self);
		
		for(int i = 0;i < others.length;++i) {
			Node<T> otherOneNode = findWhichVetexAndNotExsitAddNewNode(others[i]).self;
			whichVetex.addAdjcacencyNode(otherOneNode);
		}
		
	}
	
	/**
	 * �����ڽӱ�self�����������ȷ�����ĸ��ڽӱ�
	 * ����ö��㻹�����ھ��Ǵ����µĶ�������Լ������ö��������ڽӱ�
	 * ʱ�����Ϊ���ԡ����getVetexBySelfElement����Ϊ���ԡ�
	 * @param self ���������
	 * @return self�����Ӧ���ڽӱ���������
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
	 * ���������±�����ֵ�����ڽӱ������,��������
	 * @param idx
	 * @return
	 */
	private Vetex<T> getVetexByIndex(int idx) {
		return this.vetexList.get(idx);
	}
	
	/**
	 * �����ڽӱ�������ݣ������ڽӱ����飬Ѱ���ڽӱ�self��������ݷ���Ҫ����Ǹ��ڽӱ����á�
	 * ʱ�����Ϊ���ԡ�
	 * @param nodeElement
	 * @return
	 */
	private Vetex<T> getVetexBySelfElement(T nodeElement){
		int i;
		//��������self����������ΪnodeElement���ڽӱ�
		for(i = 0;i < this.vetexList.size();++i) {
			//�ж������Ƿ����
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
	 * ����������Ҫ˵�����ǣ�����֮�����Ҫ�ҵ�˳�򣬶������о�ͼ���е�·�������ԡ�
	 * ��һ���ҿ�ʼ�е�����ˣ����ҵ�������Ҳ��Ϊ�ҵĴ������ݽṹû����֯�á�
	 * 
	 * ������ǿ��Ū���ˡ����������������Ȧͼ����������˳��
	 * 1. ���ȣ�������Ҫ����ʱ����˳����ˣ�ѭ���Ĵ�������ʱ��֪�ġ��������size��
	 * 2. ����ͨ��findNewVertexOfIndegreeZeroAndReturnIndex�����õ���ǰ���Ϊ0�Ķ�����ڽӱ��±ꣻ
	 * 3. Ȼ��ö������£��ڽӣ������еĶ�������-1��
	 * 4. ɾ�����ڽӱ�
	 * 5. ����ɾ����˳���������˳��
	 * 
	 * ����ʱ����ޣ�
	 * 1. findNewVertexOfIndegreeZeroAndReturnIndex-����
	 * 2. MyArrayList.remove��������
	 * 3. �ڲ�for��������
	 * ��ˣ��ܵ�Ϊ�������Ķ��η�
	 * 
	 * @throws Exception ����ͼ����������Ȧͼʱ�׳��쳣
	 */
	public void topsort() throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("����˳��");
		int num_vetex = this.vetexList.size();
		
		for(int i = 0; i < num_vetex;++i) {
			int v = findNewVertexOfIndegreeZeroAndReturnIndex();
			
			if(v < 0) {
				throw new Exception("��ͼ��Ȧ��");
			}
			
			sb.append(getVetexByIndex(v).self.element).append("->");
			//�����뵱ǰ�������ĵ����ȶ�-1
			for(int j = 0;j < getVetexByIndex(v).adjacencyList.size();++j) {
				getVetexByIndex(v).adjacencyList.get(j).indegree--;
			}
			//����ɾ����ǰ�ڵ�
			this.vetexList.remove(v);
		}
		
		System.out.println(sb.toString());
	}
	
	/**
	 * ʹ������������Ż�ʱ����ޡ��Ż�������ÿ���߶�ֻ��������һ�Ρ�
	 * @throws Exception 
	 */
	public void topsortByQueue() throws Exception {
		int counter = 0;
		StringBuffer sb = new StringBuffer();
		sb.append("����˳��");
		MyArrayQueue<Vetex<T>> queue = new MyArrayQueue<>(10);
		
		/*
		 * ��ÿ���ڽӱ����ɨ�裬�ķ�V������������
		 */
		for (int i = 0;i < this.vetexList.size();++i) {
			if(getVetexByIndex(i).self.indegree == 0) {
				queue.enqueue(getVetexByIndex(i));
			}
		}
		
		/*
		 * ���ʵ����Ϊָ��ö���ߵ���Ŀ����Ȼ������forǶ��while��
		 * �����������뵽if����жϿ�֪���öγ���ʵ����ֻ�Ƕ�ÿ���߼�����һ�Σ�Ҳ����
		 * ÿ�����ִ����-1������ֱ�����Ϊ0.
		 * ��ˣ��öγ����ʱ�����ΪE��������
		 */
		while(!queue.isEmpty()) {
			Vetex<T> v = queue.dequeue();
			
			sb.append(v.self.element).append("->");
			
			for(int i = 0;i < v.adjacencyList.size();++i) {
				if(--v.adjacencyList.get(i).indegree == 0) {
					//���ĳ����������Ϊ0�˾ͽ���
					queue.enqueue(getVetexByIndex(v.adjacencyList.get(i).topNum));
				}
			}
			//�����ж�ͼ�Ƿ���Ȧ
			++counter;
		}
		System.out.println(sb.toString());
		
		if(counter != this.vetexList.size()) {
			throw new Exception("��ͼ��Ȧ���������������������Ȧͼ��");
		}
		
	}
	
	/**
	 * �ҵ����Ϊ0���ڽӱ���±꣬û�ҵ��򷵻�-1
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
	 * ��ӡ�����ڽӱ�
	 */
	public void printVetexList() {
		//�����ڽӱ�����
		for(int i = 0; i < this.vetexList.size(); ++i) {
			//��ÿһ���ڽӱ��ȴ�ӡ�Լ�
			System.out.println(this.vetexList.get(i));
		}
	}
	
	/**
	 * �����ڽӱ��࣬��Ҫ�洢��ǰ�Ķ�������Լ��ڽӵĶ����������
	 * ������Ϊ�˷������ʹ���Լ�д��MyArrayLIstanbul��˳��Ҳ���Լ����Լ�д�ļ����ࣩ
	 * 
	 * 
	 * ʹ��List���洢��ǰ������
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
		 * ���ڽӱ��в����µ����ڶ���,ֱ��ʹ��ArrayList��add������
		 * ���ҽ���ǰ�������� + 1��
		 * �����ڴ���ʵ���Ͼ�Ӧ�ô���n��ʵ�ʵĽڵ���������ڽӱ��д洢�Ķ������ã���
		 * ʱ�����Ϊ����
		 */
		public void addAdjcacencyNode(Node<T> newAdjcacencyNode) {
			this.adjacencyList.add(newAdjcacencyNode);
			newAdjcacencyNode.indegree++;
		}
		
		/**
		 * ���ص�ǰ���ӱ��self���������
		 * @return
		 */
		public T getSelfNodeElement() {
			return this.self.element;
		}
		
		/**
		 * ��ʽ������ڽӱ�
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
	 * �����࣬Ϊ�˼�������˳�����Ǹ������ϵ���ʾ��֪��ÿ�������ӵ���������ԣ�
	 * 1. ��������Ĺؼ��֡�
	 * 2. ��ȡ����ǰѶ���v����ȶ���Ϊ��(u,v)��������
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

