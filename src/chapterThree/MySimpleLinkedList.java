package chapterThree;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

public class MySimpleLinkedList<AnyType> implements Iterable<AnyType>{
	
	private Node<AnyType> beginMaker;
	private int modCount;
	
	private static class Node<AnyType>{
		private AnyType data;
		private Node<AnyType> next;
		
		public Node(AnyType data,Node<AnyType> next) {
			this.data = data;
			this.next = next;
		}
	}
	
	public MySimpleLinkedList() {
		doClear();
	}
	
	public MySimpleLinkedList(AnyType[] arr) {
		doClear();
		for (int i = 0; i < arr.length; i++) {
			addIfNotContain(arr[i]);
		}
	}
	
	public void clear() {
		doClear();
	}
	
	private void doClear() {
		modCount = 0;
		beginMaker = new Node<AnyType>(null,null);
	}
	
	public boolean isEmpty() {
		return beginMaker.next == null;
	}
	
	public int size() {
		Node<AnyType> p = this.beginMaker.next;
		int count = 0;
		
		while(p != null) {
			++count;
			p = p.next;
		}
		
		return count;
	}
	
	public boolean addIfNotContain(AnyType data) {
		Node<AnyType> prev = findPrecious(data);
		
		if(prev.next != null)
			return false;
		++modCount;
		add(prev, data);
		return true;
	}
	
	public boolean removeIfContains(AnyType data) {
		Node<AnyType> prev = findPrecious(data);
		
		if(prev.next == null)
			return false;
		
		++modCount;
		remove(prev);
		return true;
	}
	
	private void remove(Node<AnyType> prev) {
		if(prev.next == null)
			throw new IndexOutOfBoundsException();
		prev.next = prev.next.next;
	}
	
	private void add(Node<AnyType> prev,AnyType data) {
		Node<AnyType> newNode = new Node<>(data, prev.next);
		prev.next = newNode;
	}
	
	public boolean contains(AnyType data) {
		return find(data) != null;
	}

	private Node<AnyType> find(AnyType data){
		Node<AnyType> p = this.beginMaker.next;
		
		while(p != null && !p.data.equals(data)) {
			p = p.next;
		}
		
		return p;
	}
	
	private Node<AnyType> findPrecious(AnyType data){
		Node<AnyType> p = this.beginMaker;
		
		while(p.next != null && !p.next.data.equals(data)) {
			p = p.next;
		}
		
		return p;
	}
	
	
	public String toString() {
		StringBuilder strbd = new StringBuilder();
		Node<AnyType> p = this.beginMaker.next;
		
		strbd.append("header->");
		while(p != null) {
			strbd.append(p.data);
			p = p.next;
		}
		strbd.append("->null");
		
		return strbd.toString();
	}

	@Override
	public Iterator<AnyType> iterator() {
		// TODO Auto-generated method stub
		return new SimpleListIterator();
	}
	
	private class SimpleListIterator implements Iterator<AnyType>{
		private Node<AnyType> current = beginMaker.next;
		private int exceptedModCount = modCount;
		@Override
		public boolean hasNext() {
			return current != null;
		}

		@Override
		public AnyType next() {
			if(exceptedModCount != modCount)
				throw new ConcurrentModificationException();
			if(!hasNext())
				throw new IndexOutOfBoundsException();
			
			AnyType prev = current.data;
			current = current.next;
			return prev;
		}
		
	}
}
