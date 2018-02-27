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
	 * ����4.22�⣬��εݹ���������Ϣ��avlƽ����ȷ��
	public boolean checkHeightAndBalance(AVLNode<AnyType> node) {
		return 
	}
	*/
	private AVLNode<AnyType> insert(AVLNode<AnyType> t,AnyType x){
		if(t == null)
			return new AVLNode<AnyType>(x,null,null);
		
		int compareResult = x.compareTo(t.data);
		if(compareResult < 0) {
			t.left = insert(t.left,x);//����ݹ�
		}else {
			if(compareResult > 0)
				t.right = insert(t.right,x);//���ҵݹ�
			else 
				;//�ҵ��� ��ɶҲ����
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
					t = (t.left == null)? t.right:t.left;//���ﻹ���������һ��
			}
		}
		
		return balance(t);
	}
	
	/**
	 * ���谡��һ��ҪĬд����
	 * ��c�汾�У���������������ж��Ƿֱ���insert�ĵݹ���ø�֮��
	 * �߶ȼ��㷵����return֮ǰ��java������汾�����ֳ�����������
	 * ���ĸ߶����¼���
	 * @param t
	 * @return
	 */
	private AVLNode<AnyType> balance(AVLNode<AnyType> t){
		if(t == null)
			return null;
		
		if(height(t.left)-height(t.right) > ALLOWED_IMBALANCE) {
			if(height(t.left.left) >= height(t.left.right))//����Ĵ��ڵ��ڣ��е��ԣ��Ҿ��ò����ܳ��ֵ����������
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
		//�þ�����Ҫ������û���ƻ�ƽ���ʱ��ҲҪ����ø߶�
		t.height = Math.max(height(t.left), height(t.right)) + 1;//����+1��Ҫ������
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
		//ϸ����Щ+1����Ҫ������,ͬʱ����ֱ��ʹ��k1.left.height,��������Ϊnull����nullȡheight���Իᱨ��
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
	 * ���е�4.26��
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
