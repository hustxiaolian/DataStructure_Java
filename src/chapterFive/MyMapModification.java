package chapterFive;

import java.util.LinkedList;
import java.util.List;

/**
 * 书中第5.20题
 * 使用key的值，作为散列的依据，散列存储的内容为一个个Entry块。
 * 因此，其实际上还是可以看作使用分离链接法得到的散列表
 * @author 25040
 *
 * @param <KeyType>
 * @param <ValueType>
 */
public class MyMapModification<KeyType,ValueType> {
	
	private static final int DEFAULT_SIZE = 101;
	
	private List<Entry<KeyType,ValueType>>[] theLists;
	private int currentSize;
	
	/**
	 * 内部嵌套类，用于将value和key组合在一起。
	 * @author 25040
	 *
	 * @param <KeyType>
	 * @param <ValueType>
	 */
	private static class Entry<KeyType,ValueType>{
		KeyType key;
		ValueType value;
		
		public Entry(KeyType key,ValueType value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((key == null) ? 0 : key.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Entry other = (Entry) obj;
			if (key == null) {
				if (other.key != null)
					return false;
			} else if (!key.equals(other.key))
				return false;
			return true;
		}
	}
	
	/**
	 * 无参构造器，使用默认大小{@code DEFAULT_CAPACITY = 101}来初始化散列表
	 */
	public MyMapModification() {
		this(DEFAULT_SIZE);
	}
	
	/**
	 * 有参数构造器，
	 * @param capacity 散列表的容量
	 */
	@SuppressWarnings("unchecked")
	public MyMapModification(int capacity) {
		this.currentSize = capacity;
		//注意到theLists实际上是一个 List<Entry<KeyType,ValueType>> 的数组，
		//其中的元素的类型是List<Entry<KeyType,ValueType>>
		this.theLists = new LinkedList[SeparateChainingHashTable.nextPrime(capacity)];
		for(int i = 0;i < this.theLists.length;++i)
			this.theLists[i] = new LinkedList<>();
	}
	
	/**
	 * 检查散列表是否为空
	 * @return
	 */
	public boolean isEmpty() {
		return currentSize == 0;
	}
	
	/**
	 * 清空散列表
	 */
	public void makeEmpty() {
		this.currentSize = 0;
		for(int i = 0;i < this.theLists.length;++i) {
			this.theLists[i] = null;
		}
	}
	
	/**
	 * 首先，检查填充因子是否已经超过1.如果是，则建立更大的散列表重散列
	 * 新建Entry单元，通过散列码得到它在哪个链表中，然后使用linkedList的add方法插入newEntry
	 * 
	 * @param key 插入的键
	 * @param value 插入的值
	 */
	public void put(KeyType key,ValueType value) {
		//检查填充因子
		if(this.currentSize > this.theLists.length) {
			rehash();
		}
		//新建Entry单元，通过散列码得到它在哪个链表中，然后使用linkedList的add方法插入newEntry
		Entry<KeyType,ValueType> newEntry = new Entry<>(key,value);
		int hashNum = myhash(newEntry.key);
		this.theLists[hashNum].add(newEntry);
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public ValueType get(KeyType key) {
		int hashNum = myhash(key);
		return null;
		//if(this.theLists[hashNum].contains(new Entry<Key>))
	}

	/**
	 * 使用键单独来计算散列码
	 * @param entry 需要进行散列码的entry
	 * @return 该entry在该散列表的散列码
	 */
	private int myhash(KeyType key) {
		int hashNum = key.hashCode() % this.theLists.length;
		if(hashNum < 0)
			hashNum += this.theLists.length;
		return hashNum;
	}
	
	/**
	 * 重散列函数
	 */
	private void rehash() {
		// TODO Auto-generated method stub
		
	}
}
