package chapterFive;

import chapterThree.MySimpleLinkedList;

public class SimpleLinkedListSeparateChainingHashTable<T> {
	/*
	 * ����
	 */
	private final static int DEFAULT_TABLE_SIZE = 101;
	
	private MySimpleLinkedList<T>[] theLists;
	private int currentSize;
	
	/*
	 * ���췽��
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
	 * ���з���
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
	 * �����������add�����������˲����ظ���Ԫ����˲���Ҫ�����contains���
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
	 * ˽�з���
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
