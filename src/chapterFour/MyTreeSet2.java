package chapterFour;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

@SuppressWarnings("serial")
class UnderflowException extends Exception{}

public class MyTreeSet2<AnyType extends Comparable<? super AnyType>> implements Iterable<AnyType>{
	/**
	 * Ϊ�˱��ڱ�д���뽨����nextMin��nextMax�����ͷβ�ڵ�
	 * 
	 */
	private int modCount = 0;
	private BinaryNode<AnyType> beginMaker;
	private BinaryNode<AnyType> endMaker;
	private BinaryNode<AnyType> root;
	
	
	/**
	 * ���췽��
	 */
	public MyTreeSet2() {
		beginMaker = new BinaryNode<>(null, null, null, null, null);
		endMaker = new BinaryNode<>(null, null, null, beginMaker, null);
		beginMaker.nextMax = endMaker;
		
		root = null;
	}
	
	/**
	 * ����˽�еĲ��뷽����
	 * @param x �����뵽����������е�ֵ
	 */
	public void insert(AnyType x) {
		root = insert(root,beginMaker,endMaker,x);
	}
	
	public void remove(AnyType x) {
		root = remove(root,x);
	}
	
	/**
	 * ʵ��Iterable�ӿڵķ�����
	 * @return �����ڲ�����ĵ���������
	 */
	@Override
	public Iterator<AnyType> iterator() {
		return new TreeSetIterator();
	}
	/*
	 * private��������
	 */
	
	/**
	 * ����ͨ����
	 * ������������ʵ��
	 * 1. ���һ���½ڵ�x�����뵽ĳ���ڵ�y����ߣ���Ϊy�ڵ����ڵ㣬���д�С��ϵy1.nextMin < x < y,�ɴˣ����ݲ���ʽ��ϵ�����ǿ��Խ���nextMin
	 * 	    ��nextMax��˫������
	 * 2. ͬ�����һ���½ڵ�x�����뵽ĳ���ڵ�y���ұߣ���Ϊy�ڵ���ҽڵ㣬���д�С��ϵy < x < y.nextMax
	 * �������µݹ麯���������Դ������4.11�Ĵ�
	 * 
	 * @param t
	 * @param nextMin
	 * @param nextMax
	 * @param x
	 * @return
	 */
	private BinaryNode<AnyType> insert(BinaryNode<AnyType> t,BinaryNode<AnyType> nextMin,BinaryNode<AnyType> nextMax,AnyType x){
		if(t == null) {
			++modCount;
			BinaryNode<AnyType> newNode = new BinaryNode<AnyType>(x,null,null,nextMin,nextMax);
			newNode.nextMin.nextMax = newNode;
			newNode.nextMax.nextMin = newNode;
			return newNode;
		}
		
		int compareResult = x.compareTo(t.element);
		
		if(compareResult < 0) {
			t.left = insert(t.left,t.nextMin,t,x);
		}else {
			if(compareResult > 0) {
				t.right = insert(t.right,t,t.nextMax,x);
			}else {
				;
			}
		}
		
		return t;
	}
	
	private BinaryNode<AnyType> remove(BinaryNode<AnyType> t,AnyType x) {
		if(t == null)
			return null;
		
		int compareResult = x.compareTo(t.element);
		if(compareResult < 0) {
			t.left = remove(t.left,x);
		}else {
			if(compareResult > 0) {
				t.right = remove(t.right,x);
			}else {//��������
				if(t.left != null && t.right != null) {
					t.element = findMin(t.right).element;//�����findMin���������滻Ϊt.nextMin
					t.right = remove(t.right,t.element);
				}else {//�����ӻ�����Ҷ
					++modCount;
					t.nextMax.nextMin = t.nextMin;//����������һ��ʼд�����棬ʹ����˫���ӵ�����£�����ɾ�������Ρ��´�ע��
					t.nextMin.nextMax = t.nextMax;
					t = (t.left != null)? t.left:t.right;
				}
			}
		}
		
		return t;
	}
	
	private BinaryNode<AnyType> findMin(BinaryNode<AnyType> t) {
		if(t != null) {
			while(t.left != null) {
				t = t.left;
			}
			return t;
		}
		return null;
	}
	
	
	/**
	 * 
	 * @author 25040
	 *
	 * @param <AnyType>
	 */
	private static class BinaryNode<AnyType>{
		private AnyType element;
		private BinaryNode<AnyType> left;
		private BinaryNode<AnyType> right;
		private BinaryNode<AnyType> nextMin;
		private BinaryNode<AnyType> nextMax;
		
		/*
		public BinaryNode(AnyType x) {
			this(x,null,null,null,null);
		}
		*/
		
		public BinaryNode(AnyType x,BinaryNode<AnyType> left,BinaryNode<AnyType> right,
				BinaryNode<AnyType> nextMin,BinaryNode<AnyType> nextMax) {
			this.element = x;
			this.left = left;
			this.right = right;
			this.nextMin = nextMin;
			this.nextMax = nextMax;
		}
	}
	
	
	/**
	 * �����������࣬ʹ�����н��ܵļ��ɣ���ǿ���쳣���
	 * @version 2018-2-5 �����next��hasNext����
	 * @author 25040
	 *
	 */
	private class TreeSetIterator implements Iterator<AnyType>{
		private BinaryNode<AnyType> current = beginMaker.nextMax;
		private int exceptedModCount = modCount;
		private boolean okToRemove = false;
		
		@Override
		public boolean hasNext() {
			return current != endMaker;
		}

		@Override
		public AnyType next() {
			if(exceptedModCount != modCount)
				throw new ConcurrentModificationException();
			if(!hasNext()) 
				throw new IndexOutOfBoundsException();
			
			current = current.nextMax;
			okToRemove = true;
			return current.nextMin.element;
		}
		
		public void remove() {
			if(exceptedModCount != modCount)
				throw new ConcurrentModificationException();
			if(okToRemove)
				throw new IllegalArgumentException();
			
			MyTreeSet2.this.remove(root, current.element);
		}
		
	}
}
