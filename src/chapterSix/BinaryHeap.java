package chapterSix;

/**
 * ��С����ѣ��޷Ǿ���������в�������ԭ��Ϊ��
 * �������У����α�Ϊ1��λ�ÿ�ʼ��1Ϊ����
 * ��������һ�ڵ�����Ϊx����ýڵ�ĸ���Ϊx / 2,�������Ϊ x * 2,�Ҷ���Ϊ x * 2 + 1
 * 
 * 
 * 
 * @author 25040
 *
 * @param <AnyType>
 */
public class BinaryHeap<AnyType extends Comparable<? super AnyType>> {
	/*
	 * 
	 */
	private static final int DEFAULT_CAPACITY = 10;
	private int currentSize;
	private AnyType[] array;
	
	/**
	 * ������С�������������ݽṹΪ���飬��˳�ʼ�������ṩ��Ӧ��������
	 * Ĭ�ϵ��޲ι�������ʹ��Ĭ������{@code DEFAULT_CAPACITY = 10}����ʼ������
	 */
	public BinaryHeap() {
		this(DEFAULT_CAPACITY);
	}
	
	/**
	 * �вι������������ṩ�Ĳ����������ʵ����
	 * @param capacity ����ѵĴ�С
	 */
	@SuppressWarnings("unchecked")
	public BinaryHeap(int capacity) {
		this.currentSize = 0;
		this.array = (AnyType[]) new Object[capacity + 1];
	}
	
	/**
	 * ʹ��һ�����������������
	 * ����Ĳ�����ǽ�1 - currentSizeλ�õĵ�Ԫ���������˲���
	 * @param items
	 */
	public BinaryHeap(AnyType[] items) {
		this(items.length);
		//�������п�������
		for(int i = 1;i <=  currentSize;++i)
			this.array[i] = items[i - 1];
		//����
		for(int i = currentSize / 2;i > 0;--i) {
			percolateDown(i);
		}
		
	}

	/**
	 * ����ѵĲ������̡�
	 * ��Ȼ���ȣ��Ǳ����ж������С�������������Ļ���Ҫ�������飬�������ԭ�����ݵ�ת��
	 * ��ԭ��Ͳ���Ϊ��
	 * 1.�ڵ�ǰ�����ĩβ����һ�����ݣ���x���룬����������ƻ��˶���ѵĶ����ԣ������ײ��ȶ���С���������Ҫ��x����
	 * 		�ƶ����������רҵ��������ˡ�
	 * 2.�����˵Ĺ��̣������Ÿ��ڵ�����Ѱ�Һ��ʵ�λ�á�
	 * @param x ���������
	 */
	public void insert(AnyType x) {
		//��������С���������ؽ������
		if(currentSize == this.array.length - 1) {
			enlargeArray( 2  * this.array.length + 1);
		}
		//����
		int i;
		
		for(i = ++currentSize;i > 0 && this.array[i / 2].compareTo(x) > 0;i /= 2) {
			this.array[i] = this.array[i / 2];
		}
		
		this.array[i] = x;
	}
	
	/**
	 * ������С����ѵĸ�������Сֵ��ֱ�ӷ��ؼ���
	 * @return ����ѵ���Сֵ
	 */
	public AnyType findMin() {
		return this.array[1];
	}
	
	/**
	 * ɾ����Сֵ����ɾ��������ôλ��1�ճ����ˣ�Ϊ�˱�֤����ѵ����ʣ�����Ҫ�øÿ�λ����
	 * ԭ���裺
	 * 1. ��¼��Сֵ���������һ����������{@code currentSize} ��С�ˣ�
	 * 2. �������ˡ����Ŷ���·����Ѱ{@code lastItem}�����ڲ��ƻ������Եĺ��ʵĵط���
	 * 3
	 * 
	 * @return ����ɾ������Сֵ
	 */
	public AnyType deleteMin() {
		AnyType minItem = findMin();
		this.array[1] = this.array[currentSize--];
		
		percolateDown(1);
		
		return minItem;
	}
	
	/**
	 * ��������Ƿ�Ϊ��
	 * @return ����true���ǿ���false
	 */
	public boolean isEmpty() {
		return this.array[1] == null;
	}
	
	/**
	 * ���ö�������
	 */
	public void makeEmpty() {
		for(int i = 1;i <= currentSize;++i)
			this.array[i] = null;
		currentSize = 0;
	}
	
	/**
	 * �������̡�
	 * ԭ�������£�
	 * 1. ������������ƣ��ȱ���Ҫ���˵�Ԫ��ֵ��
	 * 2. ������С����·�����ˣ�Ѱ�Һ��ʵĵط�����{@code tempItem}�� {@code array[hole]}��Ҳ������������Եĵط���
	 * @param hole 
	 */
	private void percolateDown(int hole) {
		AnyType tempItem = this.array[hole];
		
		int i,child;
		for(i = hole;i * 2 < this.currentSize;i = child) {
			child = 2 * i;
			//������Ӻ��Ҷ������ҳ���С���Ǹ���ͬʱע���ֹֻ������ӵĽڵ�
			if(child + 1 <= currentSize && this.array[child + 1].compareTo(this.array[child]) < 0) {
				child++;
			}
			
			//�ж�lastItem���������Ƿ���ʣ���������ԣ���������룬������������
			if(tempItem.compareTo(this.array[child]) < 0) {
				this.array[i] = tempItem;
			}
			else {
				this.array[i] = this.array[child];
			}
		}
	}
	
	/**
	 * �Ը�������������ؽ���
	 * @param newSize �¶ѵ�����
	 */
	@SuppressWarnings("unchecked")
	private void enlargeArray(int newSize) {
		AnyType[] oldArray = this.array;
		this.array = (AnyType[]) new Object[newSize];
		
		for(int i = 1;i <= currentSize;++i) {
			this.array[i] = oldArray[i];
		}
	}
}
