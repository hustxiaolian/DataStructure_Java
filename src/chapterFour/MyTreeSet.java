package chapterFour;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * 书上4.11题XX
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
	 * 麻烦的是，相当于递归了两边，最坏时间复杂度为2logN
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
	 * 之前是，直接设置节点的值，那样破坏了二叉树的性质，
	 * 现在改为先删除旧节点，在插入新节点
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
			//向左递归
			t.left = remove(t.left, x);
			/*
			//如果返回非空，说明有儿子
			if(t.left != null)
				t.left.father = t;
				*/
		}else {
			if(compareResult > 0) {
				//向右递归
				t.right = remove(t.right,x);
				/*
				if(t.right != null)
					t.right.father = t;
				*/
			}else {
				//找到了，判断该节点是否有两个儿子
				if(t.left != null && t.right != null) {
					//两个儿子
					t.element = findMin(t.right).element;
					t.right = remove(t.right,t.element);
					/*
					if(t.right != null)
						t.right.father = t;
						*/
				}else {
					/*
					 * 直接在单儿子来构建father的正确链接，上面在的注释是我想的，比较麻烦了
					 */
					TreeSetNode<AnyType> oneChild = (t.left != null)? t.left:t.right;
					//确保返回的不是null
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
		 * 该算法的基于的原理和事实是：
		 * 1.首先找到当前二叉树的值最小的节点
		 * 2.下一个比他值大一些的节点只位于两个地方：当前节点右子树的最小值（如果有右子树）或者当前节点一直沿着left路径向上追溯直到不能沿着left路径向上时的节点
		 * 3.其中，前者找的值一定比后者小，因此先判断有没有右子树
		 * 			该思想基于的事实是：在二叉树的形成的过程中，一个值为x3新节点（x1<x3<x2），在向下递归插入时，一定是沿着x1。left，然后x2.right这样下来
		 * 4.判断截至的条件：
		 * 		1.首先如果右子树就说明还有
		 *  	2.因此，判读条件在else中，即无法沿着left路径向上追溯了。
		 */
		@Override
		public AnyType next() {
			if(exceptedModCount != modCount)
				throw new ConcurrentModificationException();
			if(!hasNext())
				throw new NoSuchElementException();
			
			AnyType nextItem = current.element;
			precious = current;
			
			//如果该点有右儿子的话，迭代器的下一点就是右字数的最小值
			if(current.right != null) {
				current = findMin(current.right);
			}else {
				/*
				 * 如果没有右儿子，在祖先中寻找比它大一点的值，
				 * 根据二叉树的规律，比一个节点大的值，要不是当前节点的右子树的最小值
				 * 或者沿着left路径一直上
				 */
				
				TreeSetNode<AnyType> child = current;
				current = current.father;
				
				//第二个条件保证了，它是沿着left路径上去遍历，而不是right路径
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
