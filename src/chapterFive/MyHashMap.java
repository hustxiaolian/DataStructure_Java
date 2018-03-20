package chapterFive;

import java.util.Iterator;

import chapterThree.MyLinkedList;

/**
 * ���е�5.20��
 * ʹ��key��ֵ����Ϊɢ�е����ݣ�ɢ�д洢������Ϊһ����Entry�顣
 * ��ˣ���ʵ���ϻ��ǿ��Կ���ʹ�÷������ӷ��õ���ɢ�б�
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
	 * �ڲ�Ƕ���࣬���ڽ�value��key�����һ��
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
	 * �޲ι�������ʹ��Ĭ�ϴ�С{@code DEFAULT_CAPACITY = 101}����ʼ��ɢ�б�
	 */
	public MyHashMap() {
		this(DEFAULT_SIZE);
	}
	
	/**
	 * �в�����������
	 * @param capacity ɢ�б������
	 */
	@SuppressWarnings("unchecked")
	public MyHashMap(int capacity) {
		//ע�⵽theListsʵ������һ�� List<Entry<KeyType,ValueType>> �����飬
		//���е�Ԫ�ص�������List<Entry<KeyType,ValueType>>
		this.theLists = new MyLinkedList[SeparateChainingHashTable.nextPrime(capacity)];
		makeEmpty();
	}
	
	/**
	 * ���ɢ�б��Ƿ�Ϊ��
	 * @return
	 */
	public boolean isEmpty() {
		return currentSize == 0;
	}
	
	/**
	 * ���ɢ�б�,�鿴MyLinkList�Ĺ��캯����ʵ���ϵ��������clear����
	 */
	public void makeEmpty() {
		this.currentSize = 0;
		for(int i = 0;i < this.theLists.length;++i) {
			this.theLists[i] = new MyLinkedList<>();
		}
	}
	
	/**
	 * ���ȣ������������Ƿ��Ѿ�����1.����ǣ����������ɢ�б���ɢ��
	 * �½�Entry��Ԫ��ͨ��ɢ����õ������ĸ������У�Ȼ��ʹ��linkedList��add��������newEntry
	 * 
	 * @param key ����ļ�
	 * @param value �����ֵ
	 */
	public boolean put(KeyType key,ValueType value) {
		//����������
		if(++this.currentSize > this.theLists.length) {
			rehash();
		}
		//�½�Entry��Ԫ��ͨ��ɢ����õ������ĸ������У�Ȼ��ʹ��linkedList��add��������newEntry
		Entry<KeyType,ValueType> newEntry = new Entry<>(key,value);
		int hashNum = myhash(newEntry.key);
		//���,��ônull�������ڹ������г�ʼ����
		MyLinkedList<Entry<KeyType,ValueType>> whichList = this.theLists[hashNum];
		if(whichList == null) {
			this.theLists[hashNum]= new MyLinkedList<>();
		}
		return this.theLists[hashNum].addIfAbsent(newEntry);
	}
	
	/**
	 * get����������ͨ��key�õ���Ӧ��valueֵ�����key�����ڣ��򷵻�null��
	 * 
	 * @param key value��Ӧ�ļ�
	 * @return ���ڸ�key�򷵻ض�Ӧ��value�����򷵻�null
	 */
	public ValueType get(KeyType key) {
		int hashNum = myhash(key);
		//Ϊ�˼ӿ�����ʱ�䣬ҲΪ�˱�̷��㣬ʹ�õ��������е���
		MyLinkedList<Entry<KeyType,ValueType>> whichList = this.theLists[hashNum];
		for (Iterator<Entry<KeyType, ValueType>> iterator = whichList.iterator(); iterator.hasNext();) {
			Entry<KeyType, ValueType> oneEntry = iterator.next();
			if(oneEntry.key.equals(key)) {
				//��key��Ӧvalue���ڣ�����
				return oneEntry.value;
			}
		}
		return null;
	}
	
	/**
	 * ����map�е��Ѿ��������Ŀ����
	 * @return
	 */
	public int size() {
		return this.currentSize;
	}
	
	/**
	 * ���ظ�map��װ������
	 * @return װ�����ӣ�0-1��
	 */
	public double loadFactor() {
		return (double)size() / (double)(this.theLists.length);
	}
	
	/**
	 * ����hashmap�ĸ�ʽ�����
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
	 * ��������Ԫ��Entry����
	 * ����ͨ����
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Entry<KeyType,ValueType>[] toEntryArray() {
		Entry<KeyType,ValueType>[] result = new Entry[size()];
		int i = 0;//result������±�����
		
		//��������hashmap
		for(int j = 0;j < this.theLists.length; ++j) {
			//�жϷ������ӷ���ĳ��ָ���Ƿ�գ���������
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
	 * ��������Ԫ�ص�Key����
	 * ��������ת������
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public KeyType[] toKeyArray() {
		KeyType[] result = (KeyType[]) new Object[size()];
		int i = 0;
		
		for(int j = 0;j < this.theLists.length; ++j) {
			//�жϷ������ӷ���ĳ��ָ���Ƿ�գ���������
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
	 * ʹ�ü�����������ɢ����
	 * @param entry ��Ҫ����ɢ�����entry
	 * @return ��entry�ڸ�ɢ�б��ɢ����
	 */
	private int myhash(KeyType key) {
		int hashNum = key.hashCode() % this.theLists.length;
		if(hashNum < 0)
			hashNum += this.theLists.length;
		return hashNum;
	}
	
	/**
	 * ��ɢ�к���,���ǰ�����Ԫ�������²���һ�顣
	 * ���������С�仯�ˣ������������ɢ��������Ѿ�����һ���ˡ�
	 */
	@SuppressWarnings("unchecked")
	private void rehash() {
		//�ȱ���ԭ���ģ��ú����Ĳ���
		MyLinkedList<Entry<KeyType,ValueType>>[] old = this.theLists;
		//�����µĸ�������������,���ﴴ����MyLinkedListԭʼ���͵����顣
		this.theLists = new MyLinkedList[SeparateChainingHashTable.nextPrime(old.length * 2)];
		this.makeEmpty();//��һ���ز����٣����������ȫ��Ԫ�����õĶ���null
		
		for(int i = 0;i < old.length;++i) {
			MyLinkedList<Entry<KeyType,ValueType>> whichList = old[i];
			if(whichList.isEmpty()) {
				//��ǰ����Ϊ�գ���ɢ����û��Ԫ��
				continue;
			}
			else {
				//����ʹ�õ������ɣ�ʹ��get�������ԣ����ʹ��get���������Ƕ��η������޷����ܡ�
				for (Iterator<Entry<KeyType, ValueType>> iterator = whichList.iterator(); iterator.hasNext();) {
					Entry<KeyType,ValueType> theEntry = iterator.next();
					put(theEntry.key, theEntry.value);
				}
			}
		}
		
	}
}
