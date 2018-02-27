package chapterFour;

public class BinaryAVLTree<AnyType extends Comparable<? super AnyType>>{
	
	/*
	public static void main(String[] args) {
		BinaryAVLTree<Integer> tree = new BinaryAVLTree<>();
		for(int i = 0;i < 10;++i) {
			tree.insert(i);
		}
		//tree.levelOrderPrintTree();
	}
	*/
	
	private static final int ALLOWED_IMBALANCE = 1;
	private AVLNode<AnyType> root;
	
	
	
	public BinaryAVLTree() {
		root = null;
	}
	
	public void insert(AnyType x) {
		root = insert(root,x);
	}
	
	public void remove(AnyType x) {
		remove(root,x);
	}
	
	/*
	 * 书中4.22题，如何递归检查树高信息和avl平衡正确性
	public boolean checkHeightAndBalance(AVLNode<AnyType> node) {
		return 
	}
	*/
	private AVLNode<AnyType> insert(AVLNode<AnyType> t,AnyType x){
		if(t == null)
			return new AVLNode<AnyType>(x,null,null);
		
		int compareResult = x.compareTo(t.data);
		if(compareResult < 0) {
			t.left = insert(t.left,x);//向左递归
		}else {
			if(compareResult > 0)
				t.right = insert(t.right,x);//向右递归
			else 
				;//找到了 就啥也不做
		}
		
		return balance(t);
	}
	
	private AVLNode<AnyType> remove(AVLNode<AnyType> t,AnyType x){
		if(t == null) {
			return null;
		}
		
		int compareResult = x.compareTo(t.data);
		if(compareResult < 0) {
			t.left = remove(t.left,x);
		}else {
			if(compareResult > 0) {
				t.right = remove(t.right,x);
			}else {
				if(t.left != null && t.right != null) {
					t.data = findMin(t.right).data;
					t.right = remove(t.right,t.data);
				}
				else
					t = (t.left == null)? t.right:t.left;//这里还可以再理解一下
			}
		}
		
		return balance(t);
	}
	
	/**
	 * 精髓啊，一定要默写下来
	 * 在c版本中，这个函数的两个判断是分别处于insert的递归调用该之后，
	 * 高度计算返回在return之前，java的这个版本把他分成了两个函数
	 * 最后的高度重新计算
	 * @param t
	 * @return
	 */
	private AVLNode<AnyType> balance(AVLNode<AnyType> t){
		if(t == null)
			return null;
		
		if(height(t.left)-height(t.right) > ALLOWED_IMBALANCE) {
			if(height(t.left.left) >= height(t.left.right))//这里的大于等于，有点迷，我觉得不可能出现等于这种情况
				t = simpleRotateWithTheLeftChild(t);
			else
				t = doubleWithLeftChild(t);
		}
		
		if(height(t.right)-height(t.left) > ALLOWED_IMBALANCE) {
			if(height(t.right.right) >= height(t.right.left)) {
				t = simpleRotateWithTheRightChild(t);
			}else {
				t = doubleWithRightChild(t);
			}
		}
		//该句子主要用于在没有破坏平衡的时候，也要计算好高度
		t.height = Math.max(height(t.left), height(t.right)) + 1;//这里+1不要忘记了
		return t;
	}
	
	private int height(AVLNode<AnyType> t){
		return t==null? -1:t.height;
	}
	
	private AVLNode<AnyType> simpleRotateWithTheLeftChild(AVLNode<AnyType> k2){
		AVLNode<AnyType> k1 = k2.left;
		
		k2.left = k1.right;
		k1.right = k2;
		
		k2.height = Math.max(height(k2.left), height(k2.right)) + 1;
		k1.height = Math.max(height(k1.left), height(k1.right)) + 1;
		
		return k1;
	}
	
	private AVLNode<AnyType> simpleRotateWithTheRightChild(AVLNode<AnyType> k1){
		AVLNode<AnyType> k2 = k1.right;
		
		k1.right = k2.left;
		k2.left = k1;
		//细节这些+1，不要忘记了,同时不能直接使用k1.left.height,因此其可能为null，对null取height属性会报错
		k1.height = Math.max(height(k1.left), height(k1.right)) + 1;
		k2.height = Math.max(height(k2.left), height(k2.right)) + 1;
		
		return k2;
	}
	
	private AVLNode<AnyType> doubleWithLeftChild(AVLNode<AnyType> k3){
		k3.left = simpleRotateWithTheRightChild(k3.left);
		return simpleRotateWithTheLeftChild(k3);
	}
	
	private AVLNode<AnyType> doubleWithRightChild(AVLNode<AnyType> k1){
		k1.right = simpleRotateWithTheLeftChild(k1.right);
		return simpleRotateWithTheRightChild(k1);
	}
	
	/*
	 * 书中第4.26题
	 */
	@SuppressWarnings("unused")
	private AVLNode<AnyType> myDoubleWithLeftChild(AVLNode<AnyType> k3){
		AVLNode<AnyType> k1 = k3.left;
		AVLNode<AnyType> k2 = k1.right;
		
		k1.right = k2.left;
		k3.left = k2.right;
		
		k2.left = k1;
		k2.right = k3;
		
		k1.height = Math.max(height(k1.left), height(k1.right)) + 1;
		k3.height = Math.max(height(k3.left), height(k3.right)) + 1;
		
		k2.height = Math.max(k1.height, k3.height) + 1;
		return k2;
	}
	
	private AVLNode<AnyType> findMin(AVLNode<AnyType> t){
		if(t == null)
			return null;
		
		while(t.left != null) {
			t = t.left;
		}
		
		return t;
	}
	
	private static class AVLNode<AnyType>{

		private AnyType data;
		private AVLNode<AnyType> left;
		private AVLNode<AnyType> right;
		private int height = 0;
		
		public AVLNode(AnyType data,AVLNode<AnyType> left,AVLNode<AnyType> right) {
			this.data = data;
			this.left = left;
			this.right = right;
		}
		
		
	}
	
	
}
