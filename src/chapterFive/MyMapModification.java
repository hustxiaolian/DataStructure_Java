package chapterFive;

import java.util.LinkedList;
import java.util.List;

/**
 * ���е�5.20��
 * ʹ��key��ֵ����Ϊɢ�е����ݣ�ɢ�д洢������Ϊһ����Entry�顣
 * ��ˣ���ʵ���ϻ��ǿ��Կ���ʹ�÷������ӷ��õ���ɢ�б�
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
	 * �ڲ�Ƕ���࣬���ڽ�value��key�����һ��
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
	 * �޲ι�������ʹ��Ĭ�ϴ�С{@code DEFAULT_CAPACITY = 101}����ʼ��ɢ�б�
	 */
	public MyMapModification() {
		this(DEFAULT_SIZE);
	}
	
	/**
	 * �в�����������
	 * @param capacity ɢ�б������
	 */
	@SuppressWarnings("unchecked")
	public MyMapModification(int capacity) {
		this.currentSize = capacity;
		//ע�⵽theListsʵ������һ�� List<Entry<KeyType,ValueType>> �����飬
		//���е�Ԫ�ص�������List<Entry<KeyType,ValueType>>
		this.theLists = new LinkedList[SeparateChainingHashTable.nextPrime(capacity)];
		for(int i = 0;i < this.theLists.length;++i)
			this.theLists[i] = new LinkedList<>();
	}
	
	/**
	 * ���ɢ�б��Ƿ�Ϊ��
	 * @return
	 */
	public boolean isEmpty() {
		return currentSize == 0;
	}
	
	/**
	 * ���ɢ�б�
	 */
	public void makeEmpty() {
		this.currentSize = 0;
		for(int i = 0;i < this.theLists.length;++i) {
			this.theLists[i] = null;
		}
	}
	
	/**
	 * ���ȣ������������Ƿ��Ѿ�����1.����ǣ����������ɢ�б���ɢ��
	 * �½�Entry��Ԫ��ͨ��ɢ����õ������ĸ������У�Ȼ��ʹ��linkedList��add��������newEntry
	 * 
	 * @param key ����ļ�
	 * @param value �����ֵ
	 */
	public void put(KeyType key,ValueType value) {
		//����������
		if(this.currentSize > this.theLists.length) {
			rehash();
		}
		//�½�Entry��Ԫ��ͨ��ɢ����õ������ĸ������У�Ȼ��ʹ��linkedList��add��������newEntry
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
	 * ��ɢ�к���
	 */
	private void rehash() {
		// TODO Auto-generated method stub
		
	}
}
