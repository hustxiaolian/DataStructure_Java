package chapterFive;

import java.util.Random;

public class CucKooHashTable<AnyType> implements CuckooHashFamily<AnyType> {
	/*
	 * 属性
	 */
	private static final double MAX_LOAD = 0.4;
	private static final int ALLOWED_REHASHES = 1;
	private static final int DEFAULT_TABLE_SIZE = 101;
	
	private final CuckooHashFamily<? super AnyType> hashFunctions;
	private final int numHashFunction;
	private AnyType[] array;
	private int currentSize;
	
	private int rehashes = 0;
	private Random r = new Random();
	
	/*
	 * 构造方法
	 */
	public CucKooHashTable(CuckooHashFamily<? super AnyType> hf) {
		this(hf,DEFAULT_TABLE_SIZE);
	}
	
	public CucKooHashTable(CuckooHashFamily<? super AnyType> hf,int size) {
		allocateArray(size);
		doClear();
		hashFunctions = hf;
		numHashFunction = hf.getNumberOfFunction();
	}
	
	/*
	 * 共有方法
	 */
	public void makeEmpty() {
		doClear();
	}
	
	public boolean contains(AnyType x) {
		return findPos(x) != -1;
	}

	public boolean reomove(AnyType x) {
		int pos = findPos(x);
		
		if(pos != -1) {
			array[pos] = null;
			--currentSize;
		}
		
		return pos != -1;
	}
	
	public boolean insert(AnyType x) {
		if(contains(x)) {
			return false;
		}
		if(currentSize >= array.length * MAX_LOAD) {
			expand();
		}
		
		return insertHelper11(x);
	}

	@Override
	public int hash(AnyType element, int which) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumberOfFunction() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void generateNewFunction() {
		// TODO Auto-generated method stub
		
	}
	
	/*
	 * 私有方法
	 */
	@SuppressWarnings("unchecked")
	private void allocateArray(int arraySize) {
		this.array = (AnyType[])new Object[arraySize];
	}
	
	private void doClear() {
		currentSize = 0;
		for(int i = 0;i < this.array.length;++i) {
			array[i] = null;
		}
		
	}
	
	private int findPos(AnyType x) {
		for(int i = 0;i < numHashFunction;++i) {
			int pos = myhash(x,i);
			if(array[i] != null && array[pos].equals(x)) {
				return pos;
			}
		}
		
		return -1;
	}

	private int myhash(AnyType x, int which) {
		int hashVal = hashFunctions.hash(x, which);
		
		hashVal %= array.length;
		if(hashVal < 0) {
			hashVal += array.length;
		}
		
		return hashVal;
	}
	
	private void expand() {
		rehash((int) (array.length / MAX_LOAD));
	}

	private boolean insertHelper11(AnyType x) {
		final int COUNT_LIMIT = 100;
		
		while(true) {
			int lastPos = -1;
			int pos;
			
			for(int count = 0;count < COUNT_LIMIT;++count) {
				for(int i = 0;i < numHashFunction;++i) {
					pos = myhash(x,i);
					
					/*
					 * 找到了空地方，插入退出，否则用下一个散列函数
					 */
					if(array[pos] == null) {
						array[pos] = x;
						++currentSize;
						return true;
					}
				}
				
				//如果使用所有的散列函数都无法插入成功，那就使用随机
				int i = 0;
				do {
					pos = myhash(x,r.nextInt(numHashFunction));
				}while(pos == lastPos && i++ < 5);
				
				//swap
				lastPos = pos;
				AnyType temp = array[lastPos];
				array[pos] = x;
				x = temp;
			}
			
			if(++rehashes > ALLOWED_REHASHES) {
				expand();
				rehashes = 0;
			}
			else {
				rehash();
			}
		}
	}

	private void rehash() {
		hashFunctions.generateNewFunction();
		rehash(array.length);
	}
	
	private void rehash(int newLength) {
		AnyType[] oldArray = this.array;
		allocateArray(SeparateChainingHashTable.nextPrime(newLength));
		currentSize = 0;
		
		for (AnyType str : oldArray) {
			if(str != null) {
				insert(str);
			}
		}
	}
}
