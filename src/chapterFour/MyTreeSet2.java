package chapterFour;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

@SuppressWarnings("serial")
class UnderflowException extends Exception{}

public class MyTreeSet2<AnyType extends Comparable<? super AnyType>> implements Iterable<AnyType>{
	/**
	 * 为了便于编写代码建立了nextMin，nextMax链表的头尾节点
	 * 
	 */
	private int modCount = 0;
	private BinaryNode<AnyType> beginMaker;
	private BinaryNode<AnyType> endMaker;
	private BinaryNode<AnyType> root;
	
	
	/**
	 * 构造方法
	 */
	public MyTreeSet2() {
		beginMaker = new BinaryNode<>(null, null, null, null, null);
		endMaker = new BinaryNode<>(null, null, null, beginMaker, null);
		beginMaker.nextMax = endMaker;
		
		root = null;
	}
	
	/**
	 * 调用私有的插入方法中
	 * @param x 欲插入到二叉查找树中的值
	 */
	public void insert(AnyType x) {
		root = insert(root,beginMaker,endMaker,x);
	}
	
	public void remove(AnyType x) {
		root = remove(root,x);
	}
	
	/**
	 * 实现Iterable接口的方法，
	 * @return 返回内部定义的迭代器对象
	 */
	@Override
	public Iterator<AnyType> iterator() {
		return new TreeSetIterator();
	}
	/*
	 * private方法部分
	 */
	
	/**
	 * 测试通过！
	 * 基于这样的事实：
	 * 1. 如果一个新节点x，插入到某个节点y的左边，成为y节点的左节点，则有大小关系y1.nextMin < x < y,由此，根据不等式关系，我们可以建立nextMin
	 * 	    和nextMax的双向链表。
	 * 2. 同理如果一个新节点x，插入到某个节点y的右边，成为y节点的右节点，则有大小关系y < x < y.nextMax
	 * 建立如下递归函数的灵感来源于书中4.11的答案
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
			}else {//两个儿子
				if(t.left != null && t.right != null) {
					t.element = findMin(t.right).element;//这里的findMin函数可以替换为t.nextMin
					t.right = remove(t.right,t.element);
				}else {//单儿子或者树叶
					++modCount;
					t.nextMax.nextMin = t.nextMin;//这里两句我一开始写在外面，使得在双儿子的情况下，连续删除了两次。下次注意
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
	 * 迭代器对象类，使用书中介绍的技巧，加强了异常检查
	 * @version 2018-2-5 添加了next和hasNext方法
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
