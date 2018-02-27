package chapterFive;

/**
 * 书中第5.20题。
 * 前面的那个MyMapModification有点搞错了题目的意思。
 * 应该是直接使用平方探测法散列表，形成map
 * 
 * @author 25040
 *
 * @param <KeyType>
 * @param <ValueType>
 */
public class MyMap<KeyType,ValueType> {
	
	/**
	 * 测试当前map类的功能
	 * @param args
	 */
	public static void main(String[] args) {
		MyMap<String,String> map = new MyMap<>();
		map.put("this","这，这个");
		System.err.println(map.get("this") != null ? map.get("this"):"不存在！");
	}
	
	/**
	 * 
	 * 内部私有嵌套类，将key和value的数据整合成一个块，利于数据管理
	 *
	 * @author 25040
	 *
	 * @param <KeyType>
	 * @param <ValueType>
	 */
	private static class Entry<KeyType,ValueType>{
		private KeyType key;
		private ValueType value;
		
		public Entry(KeyType key,ValueType value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * 由于在map中，需要能根据键找到相应块，进而找到相应的值
		 * 的功能，因此散列码的数据来源只需要根据Key即可
		 */
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
	
	
	//map私有属性，即存储一个散列表即可
	private QuadraticProbingHashTable<Entry<KeyType,ValueType>> items;
	
	/**
	 * 无参构造器，
	 */
	public MyMap() {
		this.items = new QuadraticProbingHashTable<>();
	}
	
	/**
	 * 将新的键值对存入map中
	 * @param key 键
	 * @param value 值
	 */
	public void put(KeyType key,ValueType value) {
		Entry<KeyType,ValueType> newEntry = new Entry<>(key,value);
		this.items.insert(newEntry);
	}
	
	/**
	 * 从map中取出key对应的value值，如果key值不存在，则返回null
	 * @param key 想要取出的值对应的键
	 * @return 如果不存在则返回null，存在则返回value
	 */
	public ValueType get(KeyType key) {
		return this.items.get(new Entry<KeyType,ValueType>(key,null)).value;
	}
	
	/**
	 * 直接调用散列表的{@code isEmpty()}方法，判断map是否为空
	 * @return 空则true，非空则否
	 */
	public boolean isEmpty() {
		return this.items.isEmpty();
	}
	
	/**
	 * 直接调用平方探测散列表的方法{@code makeEmpty}，清空map
	 */
	public void makeEmpty() {
		this.items.makeEmpty();
	}
}
