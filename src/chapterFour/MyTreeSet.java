package chapterFour;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * ����4.11��XX
 * @author 25040
 *
 * @param <AnyType>
 */
public class MyTreeSet<AnyType extends Comparable<? super AnyType>> implements Iterable<AnyType>{
	
	private TreeSetNode<AnyType> root;
	private int modCount = 0;
	
	private static class TreeSetNode<AnyType> {
		private AnyType element;
		private TreeSetNode<AnyType> left;
		private TreeSetNode<AnyType> right;
		private TreeSetNode<AnyType> father;
		
		public TreeSetNode(AnyType data,TreeSetNode<AnyType> left,TreeSetNode<AnyType> right,TreeSetNode<AnyType> father) {
			this.element = data;
			this.left = left;
			this.right = left;
			this.father = father;
		}
	}
	
	public MyTreeSet() {
		root = null;
	}
	
	public void makeEmpty() {
		modCount++;
		root = null;
	}
	
	public boolean isEmpty() {
		return root == null;
	}
	/**
	 * �鷳���ǣ��൱�ڵݹ������ߣ��ʱ�临�Ӷ�Ϊ2logN
	 * @param x
	 * @return bool
	 */
	public boolean add(AnyType x) {
		if(contains(x,root)) {
			insert(x,root,null);
			return true;
		}
		return false;
	}
	
	
	
	private TreeSetNode<AnyType> insert(TreeSetNode<AnyType> t,AnyType x) {
		if(t == null) {
			modCount++;
			return new TreeSetNode<>(x,null,null,null);
		}
		int compareResult = x.compareTo(t.element);
		if(compareResult < 0) {
			t.left = insert(t.left,x);
			t.left.father = t;
		}else {
			if(compareResult > 0) {
				t.right = insert(t.right, x);
				t.right.father = t;
			}
			else
				;
		}
		
		return t;
	}
	
	private TreeSetNode<AnyType> insert(AnyType x,TreeSetNode<AnyType> t,TreeSetNode<AnyType> ft){
		if(t == null) {
			++modCount;
			return new TreeSetNode<>(x,null,null,ft);
		}
		int compareResult = x.compareTo(t.element);
		if(compareResult < 0)
			t.left = insert(x,t.left,t);
		else {
			if(compareResult > 0)
				t.right = insert(x,t.right,t);
			else 
				;
		}
		
		return t;
	}
	
	/**
	 * ֮ǰ�ǣ�ֱ�����ýڵ��ֵ�������ƻ��˶����������ʣ�
	 * ���ڸ�Ϊ��ɾ���ɽڵ㣬�ڲ����½ڵ�
	 * @param oldValue
	 * @param newVal
	 * @return
	 */
	public boolean set(AnyType oldValue,AnyType newVal) {
		if(contains(oldValue, root)) {
			root = remove(root,oldValue);
			root = insert(root,newVal);
		}
		return false;
	}
	
	public boolean contains(AnyType x) {
		return contains(x,this.root);
	}
	
	private boolean contains(AnyType x,TreeSetNode<AnyType> t) {
		if(t == null)
			return false;
		
		int compareResult = x.compareTo(t.element);
		if(compareResult < 0)
			return contains(x,t.left);
		else if(compareResult > 0) 
			return contains(x,t.right);
		else
			return true;
	}
	
	public AnyType findMin() {
		if(isEmpty())
			throw new IndexOutOfBoundsException();
		else
			return findMin(root).element;
	}
	
	private TreeSetNode<AnyType> findMin(TreeSetNode<AnyType> t) {
		if(t != null) {
			while(t.left != null) {
				t = t.left;
			}
			return t;
		}
		return null;
	}
	
	public void remove(AnyType x) {
		root = remove(root,x);
	}
	
	private TreeSetNode<AnyType> remove(TreeSetNode<AnyType> t ,AnyType x){
		if(t == null)
			return null;
		
		int compareResult = x.compareTo(t.element);
		if(compareResult < 0) {
			//����ݹ�
			t.left = remove(t.left, x);
			/*
			//������طǿգ�˵���ж���
			if(t.left != null)
				t.left.father = t;
				*/
		}else {
			if(compareResult > 0) {
				//���ҵݹ�
				t.right = remove(t.right,x);
				/*
				if(t.right != null)
					t.right.father = t;
				*/
			}else {
				//�ҵ��ˣ��жϸýڵ��Ƿ�����������
				if(t.left != null && t.right != null) {
					//��������
					t.element = findMin(t.right).element;
					t.right = remove(t.right,t.element);
					/*
					if(t.right != null)
						t.right.father = t;
						*/
				}else {
					/*
					 * ֱ���ڵ�����������father����ȷ���ӣ������ڵ�ע��������ģ��Ƚ��鷳��
					 */
					TreeSetNode<AnyType> oneChild = (t.left != null)? t.left:t.right;
					//ȷ�����صĲ���null
					if(oneChild != null ) {
						oneChild.father = t.father;
					}
					t = oneChild;
				}
			}
		}
		
		return t;
	}
	
	@Override
	public Iterator<AnyType> iterator() {
		return new TreeSetIterator();
	}
	
	private class TreeSetIterator implements Iterator<AnyType>{
		private TreeSetNode<AnyType> current = findMin(root);
		private TreeSetNode<AnyType> precious;
		private int exceptedModCount = modCount;
		private boolean okToRemove = false;
		private boolean atEnd = false;
		
		@Override
		public boolean hasNext() {
			return !atEnd;
		}
		
		
		/**
		 * ���㷨�Ļ��ڵ�ԭ�����ʵ�ǣ�
		 * 1.�����ҵ���ǰ��������ֵ��С�Ľڵ�
		 * 2.��һ������ֵ��һЩ�Ľڵ�ֻλ�������ط�����ǰ�ڵ�����������Сֵ������������������ߵ�ǰ�ڵ�һֱ����left·������׷��ֱ����������left·������ʱ�Ľڵ�
		 * 3.���У�ǰ���ҵ�ֵһ���Ⱥ���С��������ж���û��������
		 * 			��˼����ڵ���ʵ�ǣ��ڶ��������γɵĹ����У�һ��ֵΪx3�½ڵ㣨x1<x3<x2���������µݹ����ʱ��һ��������x1��left��Ȼ��x2.right��������
		 * 4.�жϽ�����������
		 * 		1.���������������˵������
		 *  	2.��ˣ��ж�������else�У����޷�����left·������׷���ˡ�
		 */
		@Override
		public AnyType next() {
			if(exceptedModCount != modCount)
				throw new ConcurrentModificationException();
			if(!hasNext())
				throw new NoSuchElementException();
			
			AnyType nextItem = current.element;
			precious = current;
			
			//����õ����Ҷ��ӵĻ�������������һ���������������Сֵ
			if(current.right != null) {
				current = findMin(current.right);
			}else {
				/*
				 * ���û���Ҷ��ӣ���������Ѱ�ұ�����һ���ֵ��
				 * ���ݶ������Ĺ��ɣ���һ���ڵ���ֵ��Ҫ���ǵ�ǰ�ڵ������������Сֵ
				 * ��������left·��һֱ��
				 */
				
				TreeSetNode<AnyType> child = current;
				current = current.father;
				
				//�ڶ���������֤�ˣ���������left·����ȥ������������right·��
				while(current != null && current.left != child) {
					child = current;
					current = current.father;
				}
				
				if(current == null) {
					atEnd = true;
				}
			}
			
			okToRemove = true;
			return  nextItem;
			
		}
		
		public void remove() {
			if(exceptedModCount != modCount)
				throw new ConcurrentModificationException();
			if(!okToRemove)
				throw new IllegalStateException();

			okToRemove = false;
			MyTreeSet.this.remove(precious.element);
		}
		
	}

	
}
