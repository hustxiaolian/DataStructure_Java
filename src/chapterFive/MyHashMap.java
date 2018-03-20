package chapterFive;

import java.util.Iterator;

import chapterThree.MyLinkedList;

/**
 * 书中第5.20题
 * 使用key的值，作为散列的依据，散列存储的内容为一个个Entry块。
 * 因此，其实际上还是可以看作使用分离链接法得到的散列表
 * 
 * 
 * 
 * @author 25040
 *
 * @param <KeyType>
 * @param <ValueType>
 */
public class MyHashMap<KeyType,ValueType> {
	
	public static void main(String[] args) {
		MyHashMap<String, String> map = new MyHashMap<>();
		map.put("xiaolian", "fighting!");
		System.err.println(map.get("xiaolian"));
	}
	
	private static final int DEFAULT_SIZE = 101;
	
	private MyLinkedList<Entry<KeyType,ValueType>>[] theLists;
	private int currentSize;
	
	/**
	 * 内部嵌套类，用于将value和key组合在一起。
	 * @author 25040
	 *
	 * @param <KeyType>
	 * @param <ValueType>
	 */
	public static class Entry<KeyType,ValueType>{
		KeyType key;
		ValueType value;
		
		public Entry(KeyType key,ValueType value) {
			this.key = key;
			this.value = value;
		}
		
		

		public KeyType getKey() {
			return key;
		}



		public ValueType getValue() {
			return value;
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
			@SuppressWarnings("rawtypes")
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
	public MyHashMap() {
		this(DEFAULT_SIZE);
	}
	
	/**
	 * 有参数构造器，
	 * @param capacity 散列表的容量
	 */
	@SuppressWarnings("unchecked")
	public MyHashMap(int capacity) {
		//注意到theLists实际上是一个 List<Entry<KeyType,ValueType>> 的数组，
		//其中的元素的类型是List<Entry<KeyType,ValueType>>
		this.theLists = new MyLinkedList[SeparateChainingHashTable.nextPrime(capacity)];
		makeEmpty();
	}
	
	/**
	 * 检查散列表是否为空
	 * @return
	 */
	public boolean isEmpty() {
		return currentSize == 0;
	}
	
	/**
	 * 清空散列表,查看MyLinkList的构造函数，实际上调用链表的clear程序。
	 */
	public void makeEmpty() {
		this.currentSize = 0;
		for(int i = 0;i < this.theLists.length;++i) {
			this.theLists[i] = new MyLinkedList<>();
		}
	}
	
	/**
	 * 首先，检查填充因子是否已经超过1.如果是，则建立更大的散列表重散列
	 * 新建Entry单元，通过散列码得到它在哪个链表中，然后使用linkedList的add方法插入newEntry
	 * 
	 * @param key 插入的键
	 * @param value 插入的值
	 */
	public boolean put(KeyType key,ValueType value) {
		//检查填充因子
		if(++this.currentSize > this.theLists.length) {
			rehash();
		}
		//新建Entry单元，通过散列码得到它在哪个链表中，然后使用linkedList的add方法插入newEntry
		Entry<KeyType,ValueType> newEntry = new Entry<>(key,value);
		int hashNum = myhash(newEntry.key);
		//奇怪,怎么null我明明在构造器中初始化了
		MyLinkedList<Entry<KeyType,ValueType>> whichList = this.theLists[hashNum];
		if(whichList == null) {
			this.theLists[hashNum]= new MyLinkedList<>();
		}
		return this.theLists[hashNum].addIfAbsent(newEntry);
	}
	
	/**
	 * get方法，可以通过key得到相应的value值。如果key不存在，则返回null。
	 * 
	 * @param key value对应的键
	 * @return 存在该key则返回对应的value，否则返回null
	 */
	public ValueType get(KeyType key) {
		int hashNum = myhash(key);
		//为了加快运行时间，也为了编程方便，使用迭代器进行迭代
		MyLinkedList<Entry<KeyType,ValueType>> whichList = this.theLists[hashNum];
		for (Iterator<Entry<KeyType, ValueType>> iterator = whichList.iterator(); iterator.hasNext();) {
			Entry<KeyType, ValueType> oneEntry = iterator.next();
			if(oneEntry.key.equals(key)) {
				//该key对应value存在，返回
				return oneEntry.value;
			}
		}
		return null;
	}
	
	/**
	 * 返回map中的已经插入的项目个数
	 * @return
	 */
	public int size() {
		return this.currentSize;
	}
	
	/**
	 * 返回该map的装填因子
	 * @return 装填因子（0-1）
	 */
	public double loadFactor() {
		return (double)size() / (double)(this.theLists.length);
	}
	
	/**
	 * 整个hashmap的格式化输出
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		for(int i = 0; i < this.theLists.length; ++i) {
			if(! this.theLists[i].isEmpty()){
				for (Iterator<Entry<KeyType, ValueType>> iterator = this.theLists[i].iterator(); iterator.hasNext();) {
					Entry<KeyType, ValueType> entry = iterator.next();
					sb.append("{key:").append(entry.key).append(", ");
					sb.append("value:").append(entry.value).append("}\n");
				}
			}
		}
		return sb.toString();
	}
	
	/**
	 * 返回所有元素Entry数组
	 * 测试通过。
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Entry<KeyType,ValueType>[] toEntryArray() {
		Entry<KeyType,ValueType>[] result = new Entry[size()];
		int i = 0;//result数组的下标索引
		
		//遍历整个hashmap
		for(int j = 0;j < this.theLists.length; ++j) {
			//判断分离链接法的某个指针是否空，是则跳过
			if(!this.theLists[j].isEmpty()) {
				for (Iterator<Entry<KeyType, ValueType>> iterator = this.theLists[j].iterator(); iterator.hasNext();) {
					Entry<KeyType, ValueType> entry = iterator.next();
					result[i++] = entry;
				}
			}
		}
		
		for (Entry<KeyType, ValueType> entry : result) {
			System.out.println(entry.key + " " + entry.value);
		}
		
		return result;
	}
	
	/**
	 * 返回所有元素的Key数组
	 * 报错：类型转换错误
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public KeyType[] toKeyArray() {
		KeyType[] result = (KeyType[]) new Object[size()];
		int i = 0;
		
		for(int j = 0;j < this.theLists.length; ++j) {
			//判断分离链接法的某个指针是否空，是则跳过
			if(!this.theLists[j].isEmpty()) {
				for (Iterator<Entry<KeyType, ValueType>> iterator = this.theLists[j].iterator(); iterator.hasNext();) {
					Entry<KeyType, ValueType> entry = iterator.next();
					result[i++] = entry.key;
				}
			}
		}
		
		for (KeyType keyType : result) {
			System.out.println(keyType + " ");
		}
		
		return result;
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
	 * 重散列函数,就是把所有元素再重新插入一遍。
	 * 由于数组大小变化了，所以所有项的散列码估计已经都不一样了。
	 */
	@SuppressWarnings("unchecked")
	private void rehash() {
		//先保存原来的，用后续的插入
		MyLinkedList<Entry<KeyType,ValueType>>[] old = this.theLists;
		//创建新的更大容量的数组,这里创建了MyLinkedList原始类型的数组。
		this.theLists = new MyLinkedList[SeparateChainingHashTable.nextPrime(old.length * 2)];
		this.makeEmpty();//这一步必不可少，否则数组的全部元素引用的都是null
		
		for(int i = 0;i < old.length;++i) {
			MyLinkedList<Entry<KeyType,ValueType>> whichList = old[i];
			if(whichList.isEmpty()) {
				//当前链表为空，该散列码没有元素
				continue;
			}
			else {
				//还是使用迭代器吧，使用get方法线性，多次使用get方法，就是二次方。我无法接受。
				for (Iterator<Entry<KeyType, ValueType>> iterator = whichList.iterator(); iterator.hasNext();) {
					Entry<KeyType,ValueType> theEntry = iterator.next();
					put(theEntry.key, theEntry.value);
				}
			}
		}
		
	}
}
