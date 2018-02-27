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
	 * 属性
	 */
	private static final int DEFAULT_TABLE_SIZE = 101;
	/*
	 * 我一开始疑惑为什么List<AnyType>[]?
	 * 因此theLists数组中存储的就是一系列List<AnyType>对象的引用，在本次的实现中，实际上的实现类为LinkedList<AnyType>
	 */
	private List<AnyType>[] theLists;
	private int currentSize;
	
	/*
	 * 构造方法
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
	 * public方法部分，核心思想的第一步都是先使用hash function函数找到对应的链表
	 */
	/**
	 * 返回当前散列表的已储存的项目个数
	 * @return 项目个数
	 */
	public int size() {
		return currentSize;
	}
	
	/**
	 * 判断散列表是否为空
	 * @return 空则true,非空则false
	 */
	public boolean isEmpty() {
		return size() == 0;
	}
	
	/**
	 * 获取散列表当前的装填因子
	 * @return 装填因子，0-1;
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
		//System.out.println("插入：" + x + "散列码为"+ myHash(x) + "数组长度为" + this.theLists.length);
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
	 * 私有方法
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
	 *再散列，为了保证分离链接发的填充因子大致为1，需要再散列 
	 * 2018-2-26 由于没初始化，再散列的过程中，调用insert会出现java.lang.NullPointerException
	 */
	@SuppressWarnings("unchecked")
	private void rehash() {
		List<AnyType>[] oldList = this.theLists;
		
		this.theLists = new List[nextPrime(oldList.length * 2)];
		//这里很重要。一开始没有会报空指针异常
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
		System.out.println("再散列成功！当前容量为"+this.theLists.length);
	}
}


