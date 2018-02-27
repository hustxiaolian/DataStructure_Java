package chapterFour;

import java.util.LinkedList;
import java.util.Queue;

public class BinarySearchTree<AnyType extends Comparable<? super AnyType>>{
	
	private BinaryNode<AnyType> root;
	
	public BinaryNode<AnyType> getRoot() {
		return root;
	}

	private static class BinaryNode<AnyType>{

		private AnyType data;
		private BinaryNode<AnyType> left;
		private BinaryNode<AnyType> right;
		
		public BinaryNode(AnyType data,BinaryNode<AnyType> left,BinaryNode<AnyType> right) {
			this.data = data;
			this.left = left;
			this.right = left;

		}
		
		public void printNode(int tabNum) {
			StringBuffer sb = new StringBuffer();
			for(int i = 0;i < tabNum;++i)
				sb.append("\t");
			sb.append(data.toString());
			System.out.println(sb.toString());
		}
	}
	
	public BinarySearchTree() {
		this.root = null;
		this.removedTree = null;
	}
	
	public BinarySearchTree(AnyType[] dataArray) {
		this.root = null;
		this.removedTree = null;
		for (int i = 0; i < dataArray.length; i++) {
			root = insert(root,dataArray[i]);//����ǵ�һ��Ҫʹ�÷��ؽڵ����õ�insert�����ظ�root��
		}
	}
	
	public void makeEmpty() {
		this.root = null;
	}
	
	public boolean isEmpty() {
		return this.root == null;
	}
	
	public boolean contains(AnyType x) {
		return contains(x,this.root);
	}
	
	private boolean contains(AnyType x,BinaryNode<AnyType> t) {
		if(t == null)
			return false;
		
		int compareResult = x.compareTo(t.data);
		if(compareResult < 0)
			return contains(x,t.left);
		else if(compareResult > 0) 
			return contains(x,t.right);
		else
			return true;
	}
	
	public AnyType findMin() throws Exception {
		if(isEmpty())
			throw new Exception();
		return findMin(root).data;
	}
	
	private BinaryNode<AnyType> findMin(BinaryNode<AnyType> t){
		if(t == null)
			return null;
		else if(t.left == null) {
			return t;
		}
		return findMin(t.left);
	}
	
	public AnyType findMax() throws Exception {
		if(isEmpty())
			throw new Exception();
		return findMax(root).data;
	}
	
	private BinaryNode<AnyType> findMax(BinaryNode<AnyType> t){
		if(t == null)
			return null;
		else if(t.right == null)
			return t;
		return findMax(t.right);
	}
	
	public void insert(AnyType x) {
		insert(root,x);
	}
	
	private BinaryNode<AnyType> insert(BinaryNode<AnyType> t,AnyType x) {
		if(t == null) {
			BinaryNode<AnyType> newNode = new BinaryNode<>(x,null,null);
			return newNode;
		}
		int compareResult = x.compareTo(t.data);
		if(compareResult < 0)
			t.left = insert(t.left,x);
		else {
			if (compareResult > 0)
				t.right = insert(t.right,x);
			else
				;
		}
		
		return t;
	}
	
	public void remove(AnyType x) {
		remove(root,x);
	}
	
	private BinaryNode<AnyType> remove(BinaryNode<AnyType> t,AnyType x) {
		if(t == null)
			return null;
		
		int compareResult = x.compareTo(t.data);
		if(compareResult < 0)
			t.left = remove(t.left,x);
		else {
			if(compareResult > 0)
				t.right = remove(t.right,x);
			else {//���濪ʼ��������ҵ���
				if(t.left == null)//1
					return t.right;
				else {
					if(t.right == null)//������1һ�������tֻ��һ�����ӵ����
						return t.left;
					else {//���Ҫ��ɾ���Ľڵ�����������
						t.data = findMin(t.right).data;
						t.right = remove(t.right,t.data);//���ﷸ���ˣ���ס�ˣ�
					}
				}
			}
		}
		
		return t;
	}
	
	public void printTree() {
		if(isEmpty())
			System.out.println("Empty Tree!");
		else
			printTree(root,0);
	}
	
	private void printTree(BinaryNode<AnyType> t,int tabNum) {
		if(t != null) {
			printTree(t.right,tabNum + 1);
			for(int i = 0;i < tabNum;++i)
				System.out.print("\t");
			System.out.println(t.data);
			printTree(t.left, tabNum+1);
		}
			
	}
	
	/*
	 * ��������������Ϊ����֤��4.15��
	 */
	private int removedCount = 0;
	
	public BinaryNode<AnyType> leftRemove(BinaryNode<AnyType> t,AnyType x) {
		if(t == null)
			return null;
		
		int compareResult = x.compareTo(t.data);
		if(compareResult < 0)
			t.left = remove(t.left,x);
		else {
			if(compareResult > 0)
				t.right = remove(t.right,x);
			else {//���濪ʼ��������ҵ���
				if(t.left != null && t.right != null) {
					t.data = findMax(t.left).data;
					t.left = remove(t.left,t.data);
				}else {
					t = (t.left != null)? t.left:t.right;
				}
			}
		}
		
		return t;
	}
	
	public BinaryNode<AnyType> inturnRemove(BinaryNode<AnyType> t,AnyType x){
		if(t == null)
			return null;
		
		int compareResult = x.compareTo(t.data);
		if(compareResult < 0)
			t.left = remove(t.left,x);
		else {
			if(compareResult > 0)
				t.right = remove(t.right,x);
			else {//���濪ʼ��������ҵ���
				if(t.left != null && t.right != null) {
					if(removedCount % 2 == 0) {
						t.data = findMax(t.left).data;
						t.left = remove(t.left,t.data);
					}
					else {
						t.data = findMin(t.right).data;
						t.right = remove(t.right,t.data);
					}
				}else {
					++removedCount;
					t = (t.left != null)? t.left:t.right;
				}
			}
		}
		
		return t;
	}
	
	
	public BinaryNode<AnyType> randomRemove(BinaryNode<AnyType> t,AnyType x){
		if(t == null)
			return null;
		
		int compareResult = x.compareTo(t.data);
		if(compareResult < 0)
			t.left = remove(t.left,x);
		else {
			if(compareResult > 0)
				t.right = remove(t.right,x);
			else {//���濪ʼ��������ҵ���
				int randNum = (int)(Math.random() * 1000);
				if(t.left != null && t.right != null) {
					if(randNum % 2 == 0) {
						t.data = findMax(t.left).data;
						t.left = remove(t.left,t.data);
					}
					else {
						t.data = findMin(t.right).data;
						t.right = remove(t.right,t.data);
					}
				}else {
					t = (t.left != null)? t.left:t.right;
				}
			}
		}
		
		return t;
	}
	
	/**
	 * ���е�4.32��
	 * @param t
	 * @return
	 */
	public boolean checkBinaryTreeTest(BinaryNode<AnyType> t) {
		//null�����
		if(t == null)
			return true;
		//��Ҷ�����
		if(t.left == null && t.right == null) 
			return true;
		
		//�����ӻ���˫���ӵݹ�
		if(t.left != null) {
			return t.data.compareTo(t.left.data) > 0 && checkBinaryTreeTest(t.left);
		}
		if(t.right != null) {
			return t.data.compareTo(t.right.data) < 0 && checkBinaryTreeTest(t.right);
		}
		
		return false;
	}
	
	/**
	 * ���е�4.33��
	 * ˼·������˽�е�ɾ�����������ҵ��ýڵ�ʱ���Ƚ�����ֵ���뱣�浽�µ����֣�Ȼ��ִ��֮ǰ��ɾ������
	 * @param t
	 * @return
	 */
	private BinaryNode<AnyType> removedTree;
	public BinaryNode<AnyType> removeNodeAndReturnRemovedTree(AnyType x){
		root = removeNodeAndReturnRemovedTree(root, x);
		return removedTree;
	}
	
	private BinaryNode<AnyType> removeNodeAndReturnRemovedTree(BinaryNode<AnyType> t,AnyType x){
		if(t == null)
			return null;
		
		int compareResult = x.compareTo(t.data);
		if(compareResult < 0)
			t.left = remove(t.left,x);
		else {
			if(compareResult > 0)
				t.right = remove(t.right,x);
			else {//���濪ʼ��������ҵ���
				if(t.left == null)//1
					return t.right;
				else {
					//�Ƚ�����Ҫɾ���Ľڵ��ֵ������뵽�µ�����
					removedTree = insert(removedTree, t.data);
					//˫���ӵ����
					if(t.left != null && t.right != null) {
						t.data = findMin(t.right).data;
						t.right = remove(t.right, t.data);
					}
					else {
						//�����Ӻ�û�ж��ӵ����
						t = t.left != null ? t.left:t.right;
					}
				}
			}
		}
		
		return t;
	}
	
	/**
	 * ���е�4.34��
	 * @param N
	 */
	public static void testTimeRandomTree(int N) {
		long startTime = System.currentTimeMillis();
		
		getRandomTree(N);
		
		long endTime = System.currentTimeMillis();
		
		System.out.println("running time:" + (endTime - startTime));
	}
	
	public static BinarySearchTree<Integer> getRandomTree(int N){
		BinarySearchTree<Integer> newTree = new BinarySearchTree<Integer>();
		
		for (int i = 0; i < N; i++) {
			newTree.insert((int)(Math.random() * N));
		}
		
		return newTree;
	}
	
	/**
	 * ���е�4.41�⣬���ղ����������������˼��Ҳ����Ӧ�õ��������
	 * �����˼�����ڣ�ʹ�ö��У������������ν��ӣ�Ȼ��˳����������
	 * ʹ����һ�������ڲ���{@code NodeAndDepth}�԰�����Ⱥ�����ڵ����Ϣ�����ڱ�д���롣
	 * @since 2018-2-25
	 */
	public void levelOrderPrintTree() {
		//�����ڲ���
		class NodeAndDepth{
			public int depth;
			public BinaryNode<AnyType> node;
			
			public NodeAndDepth(int depth,BinaryNode<AnyType> node) {
				this.depth = depth;
				this.node = node;
			}
		}
		//StringBuffer sb = new StringBuffer();
		Queue<NodeAndDepth> queue = new LinkedList<>();
		NodeAndDepth nodeAndDepth = new NodeAndDepth(0, root);
		
		queue.add(nodeAndDepth);
		while(! queue.isEmpty()) {
			nodeAndDepth = queue.poll();
			nodeAndDepth.node.printNode(nodeAndDepth.depth); 
			if(nodeAndDepth.node.left != null)
				queue.offer(new NodeAndDepth(nodeAndDepth.depth + 1, nodeAndDepth.node.left));
			if(nodeAndDepth.node.right != null)
				queue.offer(new NodeAndDepth(nodeAndDepth.depth + 1, nodeAndDepth.node.right));
		}
	}
	
}
