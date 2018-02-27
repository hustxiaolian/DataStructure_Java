package chapterFive;

import chapterThree.MySimpleLinkedList;

public class SimpleLinkedListSeparateChainingHashTable<T> {
	/*
	 * 属性
	 */
	private final static int DEFAULT_TABLE_SIZE = 101;
	
	private MySimpleLinkedList<T>[] theLists;
	private int currentSize;
	
	/*
	 * 构造方法
	 */
	public SimpleLinkedListSeparateChainingHashTable() {
		this(DEFAULT_TABLE_SIZE);
	}
	
	@SuppressWarnings("unchecked")
	public SimpleLinkedListSeparateChainingHashTable(int size) {
		currentSize = 0;
		theLists = new MySimpleLinkedList[size];
		for(int i = 0;i < theLists.length;++i) {
			theLists[i] = new MySimpleLinkedList<T>();
		}
	}
	
	/*
	 * 共有方法
	 */
	
	public void makeEmpty() {
		currentSize = 0;
		for (int i = 0; i < theLists.length; i++) {
			theLists[i] = null;
		}
	}
	
	public boolean contains(T element) {
		int hashNum = myhash(element);
		return theLists[hashNum].contains(element);
	}
	
	/*
	 * 单链表特殊的add方法，避免了插入重复单元，因此不需要额外的contains检测
	 */
	public void insert(T element) {
		int hashNum = myhash(element);
		if(theLists[hashNum].addIfNotContain(element)) {
			if(++currentSize > theLists.length) {
				rehash();
			}
		}
	}
	
	public void remove(T element) {
		int hashNum = myhash(element);
		if(theLists[hashNum].removeIfContains(element)) 
			--currentSize;
	}
	
	/*
	 * 私有方法
	 */
	private int myhash(T element) {
		int hashNum = element.hashCode();
		
		hashNum %= theLists.length;
		if(hashNum < 0) {
			hashNum += theLists.length;
		}
		
		return hashNum;
	}
	
	@SuppressWarnings("unchecked")
	private void rehash() {
		MySimpleLinkedList<T>[] oldList = this.theLists;
		
		this.theLists = new MySimpleLinkedList[SeparateChainingHashTable.nextPrime(currentSize * 2)];
		for(int i = 0;i < oldList.length;++i) {
			if(!oldList[i].isEmpty()) {
				for (T eachItem : oldList[i]) {
					insert(eachItem);
				}
			}
		}
	}
}
