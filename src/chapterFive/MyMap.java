package chapterFive;

/**
 * ���е�5.20�⡣
 * ǰ����Ǹ�MyMapModification�е�������Ŀ����˼��
 * Ӧ����ֱ��ʹ��ƽ��̽�ⷨɢ�б��γ�map
 * 
 * @author 25040
 *
 * @param <KeyType>
 * @param <ValueType>
 */
public class MyMap<KeyType,ValueType> {
	
	/**
	 * ���Ե�ǰmap��Ĺ���
	 * @param args
	 */
	public static void main(String[] args) {
		MyMap<String,String> map = new MyMap<>();
		map.put("this","�⣬���");
		System.err.println(map.get("this") != null ? map.get("this"):"�����ڣ�");
	}
	
	/**
	 * 
	 * �ڲ�˽��Ƕ���࣬��key��value���������ϳ�һ���飬�������ݹ���
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
		 * ������map�У���Ҫ�ܸ��ݼ��ҵ���Ӧ�飬�����ҵ���Ӧ��ֵ
		 * �Ĺ��ܣ����ɢ�����������Դֻ��Ҫ����Key����
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
	
	
	//map˽�����ԣ����洢һ��ɢ�б���
	private QuadraticProbingHashTable<Entry<KeyType,ValueType>> items;
	
	/**
	 * �޲ι�������
	 */
	public MyMap() {
		this.items = new QuadraticProbingHashTable<>();
	}
	
	/**
	 * ���µļ�ֵ�Դ���map��
	 * @param key ��
	 * @param value ֵ
	 */
	public void put(KeyType key,ValueType value) {
		Entry<KeyType,ValueType> newEntry = new Entry<>(key,value);
		this.items.insert(newEntry);
	}
	
	/**
	 * ��map��ȡ��key��Ӧ��valueֵ�����keyֵ�����ڣ��򷵻�null
	 * @param key ��Ҫȡ����ֵ��Ӧ�ļ�
	 * @return ����������򷵻�null�������򷵻�value
	 */
	public ValueType get(KeyType key) {
		return this.items.get(new Entry<KeyType,ValueType>(key,null)).value;
	}
	
	/**
	 * ֱ�ӵ���ɢ�б��{@code isEmpty()}�������ж�map�Ƿ�Ϊ��
	 * @return ����true���ǿ����
	 */
	public boolean isEmpty() {
		return this.items.isEmpty();
	}
	
	/**
	 * ֱ�ӵ���ƽ��̽��ɢ�б�ķ���{@code makeEmpty}�����map
	 */
	public void makeEmpty() {
		this.items.makeEmpty();
	}
}
