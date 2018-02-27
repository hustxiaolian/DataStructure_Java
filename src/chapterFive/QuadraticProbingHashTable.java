package chapterFive;

import chapterFive.SeparateChainingHashTable;


/**
 * �ڻ���ƽ��̽�ⷨ��ɢ�б��У������¼���Ƚ���Ҫ��
 * 1. sizeһ��ҪΪ��������������ӱ���С��0.5�����ܱ�֤ƽ��̽��һ�����Բ���
 * 2. ��������ξۼ�������
 * @author 25040
 *
 * @param <AnyType>
 */
public class QuadraticProbingHashTable<AnyType> {
	/*
	 * ����
	 */
	private static int DEFAULT_TABLE_SIZE = 11;
	private HashEntry<AnyType>[] arrays;
	private int currentSize;
	
	/*
	 * ���췽��
	 */
	public QuadraticProbingHashTable() {
		this(DEFAULT_TABLE_SIZE);
	}
	
	public QuadraticProbingHashTable(int size) {
		allocatArrays(size);
		makeEmpty();
	}
	
	/*
	 * ���з���
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
	 * ����5.6ʵ�֣��е㲻�Ǻ������������˼��������˼�Ǿ���ʹ�÷Ǽ����entry�洢�²����value��
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
	 * ˽�з������ڲ�ʹ��
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
			currentPos += offset;//���üӷ����ⲻ��Ҫ�ĳ˷�����f(i+1) - f(i) = 2 * i + 1
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
	 * ˽�е�Ƕ����
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
