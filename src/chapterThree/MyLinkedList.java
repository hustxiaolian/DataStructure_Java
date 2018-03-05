package chapterThree;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

/**
 * 
 * @author 25040
 *
 * @param <AnyType>
 * @version 2018-2-2 ���addFirst,addLast
 */
public class MyLinkedList<AnyType> implements Iterable<AnyType> {
	
	public static void main(String[] args) {
		MyLinkedList<Integer> lst = new MyLinkedList<>();
		for(int i = 0;i < 10;++i) {
			lst.add(i);
		}
		
		System.out.println(lst);
		
		lst.swap(1);
		System.out.println(lst);
		System.out.println(lst.contains(0));
		
		Integer[] a = {1,2,3,4,5};
		Integer[] b = {2,4,6,7};
		
		MyLinkedList<Integer> lst11 = new MyLinkedList<Integer>(a);
		MyLinkedList<Integer> lst21 = new MyLinkedList<Integer>(b);
		
		System.out.println(lst11);
		System.out.println(lst21);
		
		MyLinkedList.crossWithSortedNumberList(lst11, lst21);
		MyLinkedList.andWithSortNumberList(lst11, lst21);
	}
	
	private int theSize;
	private int modCount = 0;
	private int lazyRemovedNum = 0;
	private Node<AnyType> beginMaker;
	private Node<AnyType> endMaker;

	/**
	 * ԭ��������ʵ�ֵģ�ֱ�Ӵ洢�¸���������ã����c�е�ָ��
	 * �ڲ���ʵ���ڴ��еĽڵ����,
	 * �ⲿ����󲻴��ڣ���û���ڲ�����������ܵ������ڣ�������˽�е�
	 * ͬʱע�⵽��static����̬�ģ���ζ���ⲿ�����ֱ�����÷����������ԣ����·������η�Ϊ˽�е�
	 * @author 25040
	 *
	 * @param <AnyType>
	 */
	private static class Node<AnyType> /*implements Comparable<AnyType>*/{
		private AnyType data;
		private Node<AnyType> prev;
		private Node<AnyType> next;
		private boolean isRemoved = false;
		
		public Node(AnyType data,Node<AnyType> prev,Node<AnyType> next) {
			this.data = data;
			this.prev = prev;
			this.next = next;
		}
		
		public String toString() {
			return String.valueOf(this.data);
		}
		/*
		@Override
		public int compareTo(AnyType other) {
			if(this.data.equals(other)) {
				return 0;
			}
			else
			{
				return 1;
			}
		}
		*/
	}
	
	public MyLinkedList() {
		doClear();
	}
	
	public MyLinkedList(AnyType[] args) {
		doClear();
		for(int i = 0;i < args.length;++i) {
			this.add(args[i]);
		}
	}
	
	
	public void clear() {
		doClear();
	}
	
	private void doClear() {
		beginMaker = new Node<AnyType>(null,null,null);
		endMaker = new Node<AnyType>(null,beginMaker,null);
		
		beginMaker.next = endMaker;
	}
	
	public int size() {
		return theSize;
	}
	
	public boolean isEmpty() {
		return theSize == 0;
	}
	
	public boolean add(AnyType x) {
		add(size(),x);
		return true;
	}
	
	public void add(int idx, AnyType x) {
		addBefore(getNode(idx, 0, size()),x);
	}
	
	public AnyType get(int idx) {
		return getNode(idx).data;
	}
	
	public AnyType set(int idx,AnyType newVal) {
		Node<AnyType> p = getNode(idx);
		AnyType oldVal = p.data;
		p.data = newVal;
		return oldVal;
	}
	
	public AnyType remove(int idx) {
		return remove(getNode(idx));
	}
	
	private AnyType remove(Node<AnyType> p) {
		if(p == beginMaker || p == endMaker)
			throw new NullPointerException("������Կ�����ִ��ɾ������");
		if(size() == 0)
			throw new IllegalStateException("������Կ�����ִ��ɾ������");
		
		p.prev.next = p.next;
		p.next.prev = p.prev;
		
		theSize--;
		modCount++;
		
		return p.data;
	}
	
	private void addBefore(Node<AnyType> p,AnyType x) {
		Node<AnyType> newNode = new Node<>(x,p.prev,p);
		
		newNode.prev.next = newNode;
		newNode.next.prev = newNode;
		theSize++;
		modCount++;
	}
	
	private Node<AnyType> getNode(int idx){
		return getNode(idx, 0, size() - 1);
	}
	
	private Node<AnyType> getNode(AnyType data){
		Node<AnyType> p = this.beginMaker.next;
		
		while(p!=this.endMaker) {
			if(p.data.equals(data))
				break;
			p = p.next;
		}
		
		return p;
	}
	
	private Node<AnyType> getNode(int idx,int lower,int upper){
		Node<AnyType> p = null;
		
		if(idx < lower || idx > upper) {
			throw new ArrayIndexOutOfBoundsException();
		}
		//��ʡ��һ���ʱ��
		if(idx < size() / 2) {
			p = beginMaker.next;
			for(int i = 0;i < idx;++i) {
				p = p.next;
			}
		}
		else {
			p = endMaker;//�����ҷ����ˣ�д����endMaker��prev��ʹ�ÿյ�ʱ��addֱ���γɿ�ָ�����
			for(int i = size();i > idx;--i) {
				p = p.prev;
			}
		}
		
		return p;
	}
	
	public void addAll(Iterable<? extends AnyType> items) {
		//������Ժú�����£�Ϊʲô�����Ķ�
		for (AnyType anyType : items) {
			this.add(anyType);
		}
	}
	
	public void removeAll(Iterable<? extends AnyType> items) {
		for (AnyType anyType : items) {
			Node<AnyType> p = getNode(anyType);
			if(p != this.endMaker)
				this.remove(p);
		}	
	}
	
	public void swap(int idx) {
		swap(getNode(idx));
	}
	
	/**
	 * ������p2����ǰ���Ǹ��ڵ��˳��Ϊ�˱�֤��ȷ�ԣ�
	 * ����������ֱ��ʹ��add(p1,remove(p2));����
	 * p2.prev.next = p2.next;
		p2.next.prev = p2.prev;
		
		p2.prev = p1.prev;
		p2.next = p1;
		
		p1.prev.next = p2;
		p1.prev = p2;
	 * @param p2
	 */
	private void swap(Node<AnyType> p2) {
		if(p2.prev == beginMaker || (p2.prev == null || p2 == endMaker))
			throw new ArrayIndexOutOfBoundsException("����ڵ���Ŀ���٣����ߴ���ڵ��ǰ����ͷ�ڵ㣬���ߴ���ڵ�Ϊβ�ڵ�");
		
		Node<AnyType> p1 = p2.prev;
		
		addBefore(p1,remove(p2));
	}
	
	public boolean contains(AnyType x) {
		return containsP(x) == this.endMaker;
	}
	
	private Node<AnyType> containsP(AnyType x){
		Node<AnyType> p = beginMaker.next;
		
		while(p != endMaker) {
			if(p.data.equals(x)){
				break;
			}
			p = p.next;
		}
		
		return p;
	}
	/**
	 * 1.��ʼ������ָ�룬ָ����������Ŀ�ͷ
	 * 2.����˽�е�containsP����Ѱ�ҵڶ����������Ƿ��е�һ������ĵ�һ��Ԫ��
	 * @param otherList
	 */
	/*
	public MyLinkedList<AnyType> crossWithSortedNumberList(MyLinkedList<AnyType> otherList) {
		MyLinkedList<AnyType> cross = new MyLinkedList<AnyType>();
		
		Node<AnyType> p1 = this.beginMaker.next;
		Node<AnyType> p2 = otherList.beginMaker.next;
		
		while(p1 != this.endMaker && p2 != otherList.endMaker) {
			if(p1.data > p2.data) {
				
			}
		}
		
		
		return cross;
	}
	*/
	@Override
	public Iterator<AnyType> iterator() {
		return new LinkedListIterator();
	}
	
	public String toString() {
		Node<AnyType> p = beginMaker.next;
		StringBuilder strbd = new StringBuilder();
		
		strbd.append("[");
		
		while(p != endMaker) {
			strbd.append(p + " ");
			p = p.next;
		}
		
		strbd.append("]");
		
		return strbd.toString();
	}
	
	private class LinkedListIterator implements Iterator<AnyType>{
		private Node<AnyType> current = beginMaker.next;
		private int exceptModCount = modCount;
		private boolean okToRemove = false;

		@Override
		public boolean hasNext() {
			return current != endMaker;
		}

		@Override
		public AnyType next() {
			if(exceptModCount != modCount)
				throw new ConcurrentModificationException();
			if(!hasNext())
				throw new ArrayIndexOutOfBoundsException();
			
			AnyType nextItem = current.data;
			current = current.next;
			okToRemove = true;
			return nextItem;
		}
		
		public void remove() {
			if(exceptModCount != modCount) 
				throw new ConcurrentModificationException();
			if(!okToRemove)
				throw new IllegalStateException();
			
			MyLinkedList.this.remove(current.prev);
			++exceptModCount;
			okToRemove = false;
		}
		
	}
	
	
	public static void crossWithSortedNumberList(MyLinkedList<Integer> lst1,MyLinkedList<Integer> lst2) {
		Node<Integer> p1 = lst1.beginMaker.next;
		Node<Integer> p2 = lst2.beginMaker.next;
		
		MyLinkedList<Integer> cross = new MyLinkedList<Integer>();
		
		while(p1 != lst1.endMaker && p2 != lst2.endMaker) {
			if(p1.data == p2.data) {
				cross.add(p1.data);
				p1 = p1.next;
				p2 = p2.next;
			}
			else {
				if(p1.data > p2.data)
					p2 = p2.next;
				else
					p1 = p1.next;
			}
			
		}
		
		System.out.println("cross:"+cross);	
	}
	
	public static void andWithSortNumberList(MyLinkedList<Integer> lst1,MyLinkedList<Integer> lst2) {
		Node<Integer> p1 = lst1.beginMaker.next;
		Node<Integer> p2 = lst2.beginMaker.next;
		
		MyLinkedList<Integer> and = new MyLinkedList<Integer>();
		
		while(p1 != lst1.endMaker && p2 != lst2.endMaker) {
			if(p1.data == p2.data) {
				and.add(p1.data);
				p1 = p1.next;
				p2 = p2.next;
			}
			else {
				if(p1.data > p2.data) {
					and.add(p2.data);
					p2 = p2.next;
				}
				else {
					and.add(p1.data);
					p1 = p1.next;
				}
			}
		}
		
		while(p1 != lst1.endMaker) {
			and.add(p1.data);
			p1 = p1.next;
		}
		while(p2 != lst2.endMaker) {
			and.add(p2.data);
			p2 = p2.next;
		}
		
		
		System.out.println("and:"+and);
	}
	
	public void addFist(AnyType x) {
		addBefore(beginMaker.next, x);
	}
	
	public void addLast(AnyType x) {
		addBefore(endMaker,x);
	}
	
	public void removeFirst() {
		remove(beginMaker.next);
	}
	
	public void removeLast() {
		remove(endMaker.prev);
	}
	
	public AnyType getFirst() {
		if(size() == 0)
			throw new NullPointerException("����Ϊ��");
		return beginMaker.next.data;
	}
	
	public AnyType getLast() {
		if(size() == 0)
			throw new NullPointerException("����Ϊ��");
		return endMaker.prev.data;
	}
	
	public AnyType lazyRemove(int idx) {
		return lazyRemove(getNode(idx));
	}
	
	private AnyType lazyRemove(Node<AnyType> p) {
		if(size() == 0)
			throw new NullPointerException("����Ϊ��");
		p.isRemoved = true;
		++lazyRemovedNum;
		balanceLazyNum();
		return p.data;
	}
	
	private void balanceLazyNum() {
		if(lazyRemovedNum > (theSize / 2)) {
			for(Node<AnyType> p = beginMaker.next;p!=endMaker;p = p.next) {
				if(p.isRemoved == true) {
					remove(p);
				}
			}
		}
	}
	
	public AnyType selfAdjustingGet(int idx) {
		return selfAdjustingGet(getNode(idx));
	}
	
	public AnyType selfAdjustingGet(AnyType data) {
		return selfAdjustingGet(getNode(data));
	}
	
	private AnyType selfAdjustingGet(Node<AnyType> p) {
		if(p == null || size() == 0)
			throw new NullPointerException();
		AnyType removedData = remove(p);
		addBefore(beginMaker.next, removedData);
		return removedData;
	}

}
