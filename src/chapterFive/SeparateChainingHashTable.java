package chapterFive;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class SeparateChainingHashTable<AnyType> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * ����
	 */
	private static final int DEFAULT_TABLE_SIZE = 101;
	/*
	 * ��һ��ʼ�ɻ�ΪʲôList<AnyType>[]?
	 * ���theLists�����д洢�ľ���һϵ��List<AnyType>��������ã��ڱ��ε�ʵ���У�ʵ���ϵ�ʵ����ΪLinkedList<AnyType>
	 */
	private List<AnyType>[] theLists;
	private int currentSize;
	
	/*
	 * ���췽��
	 */
	public SeparateChainingHashTable() {
		this(DEFAULT_TABLE_SIZE);
	}
	
	@SuppressWarnings("unchecked")
	public SeparateChainingHashTable(int size) {
		currentSize = 0;
		theLists = new LinkedList[nextPrime(size)];
		for(int i = 0;i < theLists.length;++i) {
			theLists[i] = new LinkedList<AnyType>();
		}
	}
	/*
	 * public�������֣�����˼��ĵ�һ��������ʹ��hash function�����ҵ���Ӧ������
	 */
	/**
	 * ���ص�ǰɢ�б���Ѵ������Ŀ����
	 * @return ��Ŀ����
	 */
	public int size() {
		return currentSize;
	}
	
	/**
	 * �ж�ɢ�б��Ƿ�Ϊ��
	 * @return ����true,�ǿ���false
	 */
	public boolean isEmpty() {
		return size() == 0;
	}
	
	/**
	 * ��ȡɢ�б�ǰ��װ������
	 * @return װ�����ӣ�0-1;
	 */
	public double getLoadFactor() {
		return (double)size() / (double)this.theLists.length;
	}
	
	public void makeEmpty() {
		for(int i = 0;i < theLists.length;++i) {
			theLists[i].clear();
		}
		currentSize = 0;
	}
	
	public void insert(AnyType x) {
		List<AnyType> whichList = theLists[myHash(x)];
		//System.out.println("���룺" + x + "ɢ����Ϊ"+ myHash(x) + "���鳤��Ϊ" + this.theLists.length);
		if(!whichList.contains(x)) {
			whichList.add(x);
			if(++currentSize > theLists.length) {
				rehash();
			}
		}
	}
	
	public boolean contain(AnyType x) {
		List<AnyType> whichList = theLists[myHash(x)];
		return whichList.contains(x);
	}
	
	public void remove(AnyType x) {
		List<AnyType> whichList = theLists[myHash(x)];
		if(whichList.contains(x)) {
			whichList.remove(x);
			--currentSize;
		}
	}
	/*
	 * ˽�з���
	 */
	public static int nextPrime(int size) {
		while(true) {
			if(isPrime(size))
				return size;
			++size;
		}
	}
	
	public static boolean isPrime(int Num) {
		for(int i = 2;i <= (int)Math.sqrt(Num);++i) {
			if(Num % i == 0)
				return false;
		}
		return true;
	}
	
	private int myHash(AnyType x) {
		int hashVal = x.hashCode();
		
		hashVal %= theLists.length;
		if(hashVal < 0) {
			hashVal += theLists.length;
		}
		
		return hashVal;
	}
	
	/*
	 *��ɢ�У�Ϊ�˱�֤�������ӷ���������Ӵ���Ϊ1����Ҫ��ɢ�� 
	 * 2018-2-26 ����û��ʼ������ɢ�еĹ����У�����insert�����java.lang.NullPointerException
	 */
	@SuppressWarnings("unchecked")
	private void rehash() {
		List<AnyType>[] oldList = this.theLists;
		
		this.theLists = new List[nextPrime(oldList.length * 2)];
		//�������Ҫ��һ��ʼû�лᱨ��ָ���쳣
		for(int i = 0;i < this.theLists.length;++i)
			this.theLists[i] = new LinkedList<>();
		
		this.currentSize = 0;
		for (int i = 0; i < oldList.length; i++) {
			if(!oldList[i].isEmpty()) {
				for(int j = 0;j < oldList[i].size();++j) {
					this.insert(oldList[i].get(j));
				}
			}
		}
		System.out.println("��ɢ�гɹ�����ǰ����Ϊ"+this.theLists.length);
	}
}


