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
			root = insert(root,dataArray[i]);//这里记得一定要使用返回节点引用的insert，返回给root；
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
			else {//下面开始处理如果找到了
				if(t.left == null)//1
					return t.right;
				else {
					if(t.right == null)//和条件1一起处理如果t只有一个儿子的情况
						return t.left;
					else {//如果要被删除的节点有两个儿子
						t.data = findMin(t.right).data;
						t.right = remove(t.right,t.data);//这里犯错了，记住了！
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
	 * 下面三个方法是为了验证书4.15题
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
			else {//下面开始处理如果找到了
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
			else {//下面开始处理如果找到了
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
			else {//下面开始处理如果找到了
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
	 * 书中第4.32题
	 * @param t
	 * @return
	 */
	public boolean checkBinaryTreeTest(BinaryNode<AnyType> t) {
		//null的情况
		if(t == null)
			return true;
		//树叶的情况
		if(t.left == null && t.right == null) 
			return true;
		
		//单儿子或者双儿子递归
		if(t.left != null) {
			return t.data.compareTo(t.left.data) > 0 && checkBinaryTreeTest(t.left);
		}
		if(t.right != null) {
			return t.data.compareTo(t.right.data) < 0 && checkBinaryTreeTest(t.right);
		}
		
		return false;
	}
	
	/**
	 * 书中第4.33题
	 * 思路：类似私有的删除方法，当找到该节点时，先将他的值插入保存到新的树种，然后执行之前的删除操作
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
			else {//下面开始处理如果找到了
				if(t.left == null)//1
					return t.right;
				else {
					//先将马上要删除的节点的值保存插入到新的树中
					removedTree = insert(removedTree, t.data);
					//双儿子的情况
					if(t.left != null && t.right != null) {
						t.data = findMin(t.right).data;
						t.right = remove(t.right, t.data);
					}
					else {
						//单儿子和没有儿子的情况
						t = t.left != null ? t.left:t.right;
					}
				}
			}
		}
		
		return t;
	}
	
	/**
	 * 书中第4.34题
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
	 * 书中第4.41题，按照层序输出二叉树，其思想也可以应用到多叉树。
	 * 其核心思想在于，使用队列，从上至下依次进队，然后顺序出队输出。
	 * 使用了一个方法内部类{@code NodeAndDepth}以包含深度和输出节点的信息，便于编写代码。
	 * @since 2018-2-25
	 */
	public void levelOrderPrintTree() {
		//方法内部类
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
