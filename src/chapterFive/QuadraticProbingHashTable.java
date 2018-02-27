package chapterFive;

import chapterFive.SeparateChainingHashTable;


/**
 * 在基于平方探测法的散列表中，有以下几点比较重要：
 * 1. size一定要为素数，且填充因子必须小于0.5，才能保证平方探测一定可以插入
 * 2. 会产生二次聚集的问题
 * @author 25040
 *
 * @param <AnyType>
 */
public class QuadraticProbingHashTable<AnyType> {
	/*
	 * 属性
	 */
	private static int DEFAULT_TABLE_SIZE = 11;
	private HashEntry<AnyType>[] arrays;
	private int currentSize;
	
	/*
	 * 构造方法
	 */
	public QuadraticProbingHashTable() {
		this(DEFAULT_TABLE_SIZE);
	}
	
	public QuadraticProbingHashTable(int size) {
		allocatArrays(size);
		makeEmpty();
	}
	
	/*
	 * 共有方法
	 */
	public boolean isEmpty() {
		return size() == 0;
	}
	
	public int size() {
		return currentSize;
	}
	
	public double loadFactor() {
		return (double)size() / (double)this.arrays.length;
	}
	
	public boolean contains(AnyType x) {
		int currentPos= findPos(x);
		return isActive(currentPos);
	}
	
	public AnyType get(AnyType x) {
		int currentPos = findPos(x);
		if(isActive(currentPos)) {
			return this.arrays[currentPos].element;
		}
		return null;
	}
	
	public void insert(AnyType x) {
		int currentPos = findPos(x);
		if(isActive(currentPos))
			return;
		
		arrays[currentPos] = new HashEntry<AnyType>(x);
		
		if(++currentSize > arrays.length / 2){
			rehash();
		}
	}
	
	/*
	 * 书中5.6实现，有点不是很清楚的它的意思，它的意思是尽量使用非激活的entry存储新插入的value吗？
	 */
	/*
	public void insertToNoTActive(AnyType x) {
		int offset = 1;
		int currentPos = myhash(x);
		
		while(this.arrays[currentPos] != null && this.arrays)
	}
	*/
	public void remove(AnyType x) {
		int currentPos = findPos(x);
		if(isActive(currentPos)) {
			arrays[currentPos].isActive = false;
			//--currentSize;
		}
	}
	
	public void makeEmpty() {
		currentSize = 0;
		for(int i = 0;i < this.arrays.length;++i) {
			arrays[i] = null;
		}
	}
	
	/*
	 * 私有方法，内部使用
	 */
	@SuppressWarnings("unchecked")
	private void allocatArrays(int arraySize) {
		this.arrays = new HashEntry[SeparateChainingHashTable.nextPrime(arraySize)];
	}
	
	private boolean isActive(int currentPos) {
		return this.arrays[currentPos] != null && this.arrays[currentPos].isActive;
	}
	
	private int findPos(AnyType x) {
		int offset = 1;
		int currentPos = myhash(x);
		
		while(this.arrays[currentPos] != null && this.arrays[currentPos].equals(x)) {
			currentPos += offset;//利用加法避免不必要的乘法运算f(i+1) - f(i) = 2 * i + 1
			offset += 2;
			if(currentPos >= this.arrays.length)
				currentPos -= this.arrays.length;
		}
		
		return currentPos;
	}
	
	private int myhash(AnyType x) {
		int hashNum = x.hashCode();
		hashNum %= this.arrays.length;
		if(hashNum < 0)
			hashNum += arrays.length;
		return hashNum;
	}
	
	private void rehash() {
		HashEntry<AnyType>[] oldArray = this.arrays;
		
		allocatArrays(SeparateChainingHashTable.nextPrime(oldArray.length * 2));
		currentSize = 0;
		
		for(int i = 0;i < oldArray.length;++i) {
			if(oldArray[i] != null && oldArray[i].isActive == true) {
				this.insert(oldArray[i].element);
			}
		}
	}
	
	/*
	 * 私有的嵌套类
	 */
	private static class HashEntry<AnyType> {
		private AnyType element;
		private boolean isActive;
		
		public HashEntry(AnyType x) {
			this(x, true);
		}
		
		public HashEntry(AnyType x,boolean isActive) {
			this.element = x;
			this.isActive = isActive;
		}
	}
}
