package chapterFour;

public class ExtendingTree<AnyType extends Comparable<? super AnyType>> {
	/*
	 * 属性
	 */
	private BinaryNode<AnyType> root;
	
	/*
	 * 构造方法
	 */
	public ExtendingTree() {
		this.root = null;
	}
	
	public ExtendingTree(AnyType[] array) {
		for (AnyType item : array) {
			insert(item);
		}
	}
	
	/*
	 * 公有方法
	 */
	public boolean isEmpty() {
		return this.root == null;
	}
	
	public void makeEmpty() {
		this.root = null;
	}
	
	public void insert(AnyType x) {
		root = insert(root,x);
	}
	
	public void remove(AnyType x) {
		remove(root,x);
	}
	
	public boolean findAndRotate(AnyType x) {
		if(isEmpty()) {
			System.out.println("tree is empty!");
		}
		else {
			root = findAndRetate(root, x);
			if(root.element == x) {
				return true;
			}
			else {
				if(root.left != null && root.left.element == x) {
					//旋转左儿子和根节点
					root = rotateRootAndLeftChild(root);
					
					return true;
				}
				if(root.right != null && root.right.element == x) {
					root = rotateRootAndRightChild(root);
					
					return true;
				}
			}
		}
		
		return false;
	}
	
	public void printTree() {
		if(isEmpty()) {
			System.out.println("tree is empty!");
		}
		else {
			printTree(root,0);
		}
	}
	/*
	 * 私有方法
	 */
	
	/**
	 * 使用二叉查找树的insert例程
	 * @param t
	 * @param x
	 * @return
	 */
	private BinaryNode<AnyType> insert(BinaryNode<AnyType> t ,AnyType x) {
		if(t == null) {
			BinaryNode<AnyType> newNode = new BinaryNode<>(x,null,null);
			return newNode;
		}
		
		int compareResult = x.compareTo(t.element);
		if(compareResult < 0) {
			t.left = insert(t.left,x);
		}else {
			if(compareResult > 0) {
				t.right = insert(t.right,x);
			}else {
				;
			}
		}
		
		return t;
	}
	
	private BinaryNode<AnyType> remove(BinaryNode<AnyType> t ,AnyType x){
		if(t == null) {
			return null;
		}
		
		int compareResult = x.compareTo(t.element);
		if(compareResult < 0) {
			t.left = remove(t.left,x);
		}else {
			if(compareResult > 0) {
				t.right = remove(t.right,x);
			}else {
				//double children
				if(t.left != null && t.right != null) {
					t.element = findMin(t.right);
					t.right = remove(t.right,t.element);
				}else {
					//simple children
					t = (t.left != null)? t.left:t.right;
				}
			}
		}
		
		return t;
	}
	
	private AnyType findMin(BinaryNode<AnyType> t) {
		if(t == null)
			return null;
		
		while(t.left != null) {
			t = t.left;
		}
		
		return t.element;
	}
	
	/**
	 * 之字形状态下的伸展树的旋转变化，方向是先左后右。十分类似AVL旋转树的双旋转
	 * @param G
	 * @return
	 */
	private BinaryNode<AnyType> leftZigzagRotate(BinaryNode<AnyType> G){
		BinaryNode<AnyType> P = G.left;
		BinaryNode<AnyType> X = P.right;
		
		P.right = X.left;
		G.left = X.right;
		
		X.left = P;
		X.right = G;
		
		return X;
	}
	
	
	/**
	 * 之字形状态下的伸展树的旋转变化，方向是先右后左。十分类似AVL旋转树的双旋转
	 * @param P
	 * @return
	 */
	private BinaryNode<AnyType> rightZigzagRotate(BinaryNode<AnyType> P){
		BinaryNode<AnyType> G = P.right;
		BinaryNode<AnyType> X = G.left;
		
		P.right = X.left;
		G.left = X.right;
		
		X.left = P;
		X.right = G;
		
		return X;
	}
	
	/**
	 * 左 一字形的旋转
	 * @param G
	 * @return
	 */
	private BinaryNode<AnyType> leftZigzigRotate(BinaryNode<AnyType> G){
		BinaryNode<AnyType> P = G.left;
		BinaryNode<AnyType> X = P.left;
		
		G.left = P.right;
		P.left = X.right;
		X.right = P;
		P.right = G;
		
		return X;
	}
	
	private BinaryNode<AnyType> rightZigzigRotate(BinaryNode<AnyType> X){
		BinaryNode<AnyType> P = X.right;
		BinaryNode<AnyType> G = P.right;
		
		X.right = P.left;
		P.right = G.left;
		G.left = P;
		P.left = X;
		
		return G;
	}
	
	private void printTree(BinaryNode<AnyType> t,int tabNum) {
		if(t != null) {
			printTree(t.right,tabNum + 1);
			for(int i = 0;i < tabNum;++i)
				System.out.print("\t");
			System.out.println(t.element);
			printTree(t.left, tabNum+1);
		}
			
	}
	
	private BinaryNode<AnyType> findAndRetate(BinaryNode<AnyType> t,AnyType x) {
		if(t == null)
			return null;
		
		int compareResult = x.compareTo(t.element);
		
		if(compareResult < 0) {
			t.left = findAndRetate(t.left, x);
			if(t.left.left != null && t.left.left.element == x) {
				//左 一字型
				t = leftZigzigRotate(t);
			}
			else {
				if(t.left.right != null && t.left.right.element == x) {
					//左 之字形
					t = leftZigzagRotate(t);
				}
			}
		}
		else {
			if(compareResult > 0) {
				t.right = findAndRetate(t.right, x);
				if(t.right.left != null && t.right.left.element == x) {
					//右 之字形
					t = rightZigzagRotate(t);
				}
				else {
					if(t.right.right != null && t.right.right.element == x) {
						//右 一字形
						t = rightZigzigRotate(t);
					}
				}
			}
			else {
				//找到了，啥也别做
				;
			}
		}
		
		return t;
	}
	
	private BinaryNode<AnyType> rotateRootAndLeftChild(BinaryNode<AnyType> root){
		//旋转左儿子和根节点
		BinaryNode<AnyType> child = root.left;
		root.left = child.right;
		child.right = root;
		return child;
	}
	
	private BinaryNode<AnyType> rotateRootAndRightChild(BinaryNode<AnyType> root){
		//旋转右儿子和根节点
		BinaryNode<AnyType> child = root.right;
		root.right = child.left;
		child.left = root;
		return child;
	}
	
	/*
	 * 嵌套类
	 */
	private static class BinaryNode<AnyType>{
		
		private AnyType element;
		private BinaryNode<AnyType> left;
		private BinaryNode<AnyType> right;
		
		public BinaryNode(AnyType element,BinaryNode<AnyType> left,BinaryNode<AnyType> right){
			this.element = element;
			this.left = left;
			this.right = right;
		}
	}
}
