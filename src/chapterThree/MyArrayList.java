package chapterThree;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;

/**
 * @since 2018-3-4 ��һ��review
 * �������ϵ���ʾ���Լ���ɵ�ArrayList������
 * ArrayList�ĵײ���ʹ������ʵ�ֵģ�����ÿ�β��붼�ᶯ̬����µ�ǰ�������Ƿ�����Ҫ�����Իᶯ̬����չ��ǰ������
 * Ŀǰ��Ϊ���ԸĽ��ĵط��Ƕ��������ǰ�����ʹ���ʹ��ͣ����ǿ����ʵ��ķ�����չ���顣
 * �е���ɢ�б��и���������ӽ�����ɢ��һ����
 * 
 * �´�review���Ǻú��о���Iterator��ListIterator
 * 
 * @author 25040
 *
 * @param <AnyType>
 */
public class MyArrayList<AnyType> implements Iterable<AnyType>{
	//Ĭ�ϵ����������������޲ι�����
	private static final int DEFAULT_CAPACITY = 10;
	//��¼��ǰ��������Ԫ�ؾ������Ŀ
	private int theSize = 0;
	//��¼��ǰ�������Թ����ʼ������޸Ĵ������ܹ��޸ĵĺ�����Ҫ��add��remove
	//ͬʱ��Ҳ�����ڱ�֤������������������Ҫ��־
	private int modCount = 0;
	//�����Ԫ������
	private AnyType[] theItems;
	
	/**
	 * �޲ι���
	 */
	public MyArrayList() {
		doClear();
	}
	
	/**
	 * �вι���
	 * @param capacity ָ������
	 */
	public MyArrayList(int capacity) {
		ensureCapacity(capacity);
	}
	
	/**
	 * �вι��죬ʹ����һ����������ʼ��ArrayList��
	 * �����������·����ڴ�ռ��Լ���ԭ����������ݸ�ֵ������������Ҫ���ԣ�O��N����ʱ�䡣
	 * �ټ��ϸú���һ��forѭ����Լ����˫��������ʱ��
	 * 
	 * @param arr ���ڳ�ʼ��ArrayList������
	 */
	public MyArrayList(AnyType[] arr) {
		ensureCapacity(arr.length * 2);
		for (int i = 0; i < arr.length; i++) {
			add(arr[i]);
		}
	}
	
	/**
	 * ��յ�ǰ����
	 */
	public void clear() {
		doClear();
	}
	
	/**
	 * size�������㣬�����������ָ���Ĭ�ϴ�С
	 */
	private void doClear() {
		this.theSize = 0;
		ensureCapacity(DEFAULT_CAPACITY);
	}
	
	/**
	 * ���ص�ǰ���ϵ���Ŀ����size��
	 * @return
	 */
	public int size() {
		return this.theSize;
	}
	
	/**
	 * �жϵ�ǰ�����Ƿ�Ϊ��
	 * @return
	 */
	public boolean isEmpty() {
		return this.size() == 0;
	}
	
	/**
	 * ʹ���ϵ�������ȫ������size����û�ж���Ŀռ䣬��ʡ�ռ䣬����һ��ʹ��add�������ͻ�����
	 * ����{@link ensureCapacity},��˳��Ǳ�֤�Ժ��ٲ�����Ԫ�أ�������Ҫʹ������
	 */
	public void trimToSize() {
		ensureCapacity(this.size());
	}
	
	/**
	 * ��������������Ԫ�أ�ʱ�����Ϊ����
	 * @param idx �����±�����
	 * @return ���������Ԫ��
	 * @throws java.lang.IndexOutOfBoundsException idx������0<= idx < sizeʱ 
	 */
	public AnyType get(int idx){
		if(idx < 0 || idx >= size()) {
			throw new ArrayIndexOutOfBoundsException();
		}
		
		return this.theItems[idx];
	}
	
	/**
	 * ���������±꣬�������ö�Ӧ��Ԫ��
	 * @param idx ����
	 * @param newVal ��ֵ
	 * @return ��ֵ
	 */
	public AnyType set(int idx,AnyType newVal) {
		if(idx < 0 || idx >=size()) {
			throw new ArrayIndexOutOfBoundsException();
		}
		
		AnyType old = this.theItems[idx];
		this.theItems[idx] = newVal;
		modCount++;
		return old;
	}
	
	/**
	 * �ڵ�ǰ���ϵ�ĩβ����Ԫ�أ�ʱ�����ΪO��1������
	 * @param newVal
	 * @return
	 */
	public boolean add(AnyType newVal) {
		add(size(),newVal);
		return true;
	}
	
	/**
	 * �ڵ�ǰ���鼯�ϵ��м����Ԫ�أ��ʱ�����Ϊ����
	 * @param idx
	 * @param newVal
	 * ps:���idxԽ����ô�죬��ô�����Զ��׳�ArrayIndexOutOfBoundsException
	 */
	public void add(int idx,AnyType newVal) {
		if(this.theItems.length == size()) {
			ensureCapacity(2 * size() + 1 );
		}
		//���е�Ԫ�غ���һλ����idx��Ӧ��λ�ÿճ���
		for(int i = size();i > idx;--i) {
			this.theItems[i] = this.theItems[i - 1];
		}
		
		this.theItems[idx] = newVal;
		this.theSize++;
		this.modCount++;
	}
	
	/**
	 * @version 1.0
	 * ֱ��ɾ�������±�����λ���ϵ�Ԫ�أ��Ѳ��Ұ�idx֮���Ԫ��ȫ��ǰ��һλ������ʱ�����Ϊ����
	 * @version 1.1
	 * ��1.0�汾�Ļ����ϣ���鵱ǰ������������({@code size() / theItems.length})�����ݼ��㣬�������
	 * �����û���ʹ��ʱ��remove��add��Ƶ�ʲ��Ļ���Ϊ�˱�֤��������ʹ��{@code ensureCapacity}�����ĸ��ʣ�
	 * �������ٷ�������󣬿���add��remove�Ĵ���Ӧ�����ࡣͬʱ�����Ǽ���ÿ���ٷ��������ϵ��Ϊ2�����󣩺�0.5����С��
	 * ��ˣ�����������ӾͿ��Լ���õ�Ϊ1/3;
	 * @param idx
	 * @return
	 */
	public AnyType remove(int idx) {
		if((double)(size() / this.theItems.length) < (double)1/ 3) {
			ensureCapacity(size() / 2);
		}
		
		AnyType removedItem = get(idx);
		for(int i = idx;i < size() - 1;++i) {
			this.theItems[i] = this.theItems[i + 1];
		}
		
		this.theSize--;
		this.modCount++;
		return removedItem;
	}
	
	/**
	 * ������������ٷ��亯�������ĺ���֮һ��
	 * ˼·�ܼ����������ǰ����µ����������������飬��ԭ���������Ԫ�ؿ�������������
	 * @param newCapacity
	 */
	@SuppressWarnings("unchecked")
	public void ensureCapacity(int newCapacity) {
		/*
		if(newCapacity < this.theSize) {
			return;
		}
		*/
		AnyType[] old = this.theItems;
		this.theItems = (AnyType[])new Object[newCapacity];
		for(int i = 0;i < this.theSize;++i) {
			this.theItems[i] = old[i];
		}
	}
	
	/**
	 * ������ڷ��͵ĸ���һ�����ȥ�����飬�е㲻�Ǻ�����
	 * @param items
	 */
	public void addAll(Iterable<? extends AnyType> items) {
		for(AnyType anyType:items) {
			this.add(anyType);
			this.modCount++;
		}
	}
	
	public String toString() {
		StringBuilder strbd = new StringBuilder();
		
		strbd.append("[");
		
		for (int i = 0; i < this.size(); i++) {
			strbd.append(this.theItems[i].toString() + " ");
		}
		
		strbd.append("]");
		
		return strbd.toString();
	}
	
	@Override
	public Iterator<AnyType> iterator() {
		return new ArrayListIterator();
	}
	
	public MyListIterator listIterator(){
		return new MyListIterator();
	}
	
	/**
	 * listIterator��������ʵ�ַ�ʽ��iterator���ƣ�������������ǰ������������
	 * @author 25040
	 *
	 */
	private class MyListIterator implements ListIterator<AnyType>	{
		
		private int current = 0;
		
		@Override
		public void add(AnyType arg0) {
			MyArrayList.this.add(current, arg0);
		}

		@Override
		public boolean hasNext() {
			return current < MyArrayList.this.size();
		}

		@Override
		public boolean hasPrevious() {
			return current >= 0;
		}

		@Override
		public AnyType next() {
			if(!hasNext())
				throw new IndexOutOfBoundsException("�ұ�Խ��");
			return MyArrayList.this.theItems[++current];
		}

		@Override
		public int nextIndex() {
			return current + 1;
		}

		@Override
		public AnyType previous() {
			if(!hasPrevious())
				throw new IndexOutOfBoundsException("���Խ��");
			return MyArrayList.this.theItems[--current];
		}

		@Override
		public int previousIndex() {
			return current - 1;
		}

		@Override
		public void remove() {
			MyArrayList.this.remove(--current);
		}

		@Override
		public void set(AnyType arg0) {
			MyArrayList.this.theItems[current] = arg0;
		}
		
	}
	/**
	 * �ڲ���,next�����е�next�����е����Ͳ������ⲿ���AnyType��
	 * ���ڲ���ʵ����Iterator<E>�ӿ�
	 * 
	 * ���Ժú��о��£����ֵ���������Ʒ�ʽ��ʮ���Ͻ�����
	 * ������������Ĵ������ǿ��Կ�����ʹ�õ�����removeʱ��������next���С�
	 * 
	 * �����﷨�ϣ�����һ��ע���ʱ���ڲ���ʹ���ⲿ��ķ���ʱ����ʽΪ ����.this.������(�����б�)
	 * 
	 * @author 25040
	 *
	 */
	private class ArrayListIterator implements Iterator<AnyType>{
		private int current = 0;
		private int exceptedModCount = MyArrayList.this.size();//��ֹ������������û��ı��������Ԫ��
		private boolean okToRemove = false;
		
		@Override
		public boolean hasNext() {
			return current < MyArrayList.this.size();
		}

		@Override
		public AnyType next() {
			if(MyArrayList.this.modCount != exceptedModCount)
				throw new ConcurrentModificationException();
			if(!hasNext()) {
				throw new ArrayIndexOutOfBoundsException();
			}
			okToRemove = true;
			return MyArrayList.this.theItems[current++];
		}
		
		public void remove() {
			//�ڲ���ʹ���ⲿ�ķǾ�̬����������.this.������();
			if(MyArrayList.this.modCount != exceptedModCount)
				throw new ConcurrentModificationException();
			if(!okToRemove)
				throw new IllegalStateException();
			MyArrayList.this.remove(--current);
			exceptedModCount++;
			okToRemove = false;
		}
	}
	
	/**
	 * ���е�3.30a �Ե����������ʵ�֡�Ҳ���ǰ���ҪgetԪ�ز������Ƿ��أ�ͬʱ���������ڵ�һ������Ȼ������Ԫ�غ���
	 * @param idx
	 * @return
	 */
	public AnyType selfAdjustingGet(int idx) {
		if(idx < 0 || idx > size() || size() == 0)
			throw new NullPointerException();
		AnyType tmp = get(idx);
		for(int i = idx;i > 0;--i) {
			this.theItems[i] = this.theItems[i - 1];
		}
		this.theItems[0] = tmp;
		return tmp;
	}
}
