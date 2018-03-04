package chapterOne;

/**
 * ���е�1.14��.������ ���£�û�к���ܹ���֯��ʽ���е����⣬Ȩ�������Ŀ���ѡ�
 * @author 25040
 *
 * @param <T>
 */
public class OrderedCollection<T extends Comparable<? super T>> {
	
	private static final int DEFAULT_CAPACITY = 10;
	
	private T[] items;
	private int currentSize;
	
	/**
	 * �вι��죬ָ���ü��������������
	 * @param capacity
	 */
	@SuppressWarnings("unchecked")
	public OrderedCollection(int capacity) {
		this.currentSize = 0;
		this.items = (T[]) new Object[capacity];
	}
	
	/**
	 * ʹ��Ĭ�ϵ��������켯����
	 */
	public OrderedCollection() {
		this(DEFAULT_CAPACITY);
	}
	
	/**
	 * �����Ƿ�Ϊ0
	 * @return
	 */
	public boolean isEmpty() {
		return this.currentSize == 0;
	}
	
	/**
	 * ��ոü�����
	 */
	public void makeEmpty() {
		this.currentSize = 0;
		for(int i = 0;i < this.items.length;++i) {
			this.items[i] = null;
		}
	}
	
	/**
	 * ����Ԫ���ڼ������ĩβ
	 * @param insertItem
	 */
	public void insert(T insertItem) {
		insert(currentSize, insertItem);
	}
	
	/**
	 * ��Ԫ�ز��뵽�����ָ��λ��
	 * @param idx Ԫ�ز����λ��
	 */
	private void insert(int idx, T insertItem) {
		if(++this.currentSize >= this.items.length ) {
			throw new IndexOutOfBoundsException("�����ڴ�ռ䲻����");
		}
		
		//���е�Ԫ�غ���
		for(int i = this.currentSize;i > idx;--i) {
			this.items[i] = this.items[i - 1];
		}
		
		this.items[idx] = insertItem;
	}
	
	/**
	 * ɾ�����̡�
	 */
	
	public void remove(int idx) {
		if(--this.currentSize < 0) {
			throw new IllegalArgumentException("����Ϊ�ղ���ɾ��");
		}
		
		for(int i = idx;i < currentSize;++i) {
			this.items[i] = this.items[i + 1];
		}
	}
	
	
	/**
	 * �ҳ���Сֵ���ҷ��أ�ʱ�����Ϊ����
	 * @return �����е���Сֵ
	 */
	public T findMin() {
		T result = this.items[0];
		
		for(int i = 1;i < currentSize;++i) {
			if(result.compareTo(this.items[i]) < 0) {
				result = this.items[i];
			}
		}
		
		return result;
	}
	
	/**
	 * �ҳ����ֵ���ҷ��أ�ʱ�����Ϊ����
	 * @return �����е����ֵ
	 */
	public T findMax() {
		T result = this.items[0];
		
		for(int i = 1;i < currentSize;++i) {
			if(result.compareTo(this.items[i]) > 0) {
				result = this.items[i];
			}
		}
		
		return result;
	}
}
