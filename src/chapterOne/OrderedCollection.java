package chapterOne;

/**
 * 书中第1.14题.随便完成 了下，没有合理架构组织形式。有点随意，权当完成题目而已。
 * @author 25040
 *
 * @param <T>
 */
public class OrderedCollection<T extends Comparable<? super T>> {
	
	private static final int DEFAULT_CAPACITY = 10;
	
	private T[] items;
	private int currentSize;
	
	/**
	 * 有参构造，指定该集合类的数组容量
	 * @param capacity
	 */
	@SuppressWarnings("unchecked")
	public OrderedCollection(int capacity) {
		this.currentSize = 0;
		this.items = (T[]) new Object[capacity];
	}
	
	/**
	 * 使用默认的容量构造集合类
	 */
	public OrderedCollection() {
		this(DEFAULT_CAPACITY);
	}
	
	/**
	 * 集合是否为0
	 * @return
	 */
	public boolean isEmpty() {
		return this.currentSize == 0;
	}
	
	/**
	 * 清空该集合类
	 */
	public void makeEmpty() {
		this.currentSize = 0;
		for(int i = 0;i < this.items.length;++i) {
			this.items[i] = null;
		}
	}
	
	/**
	 * 插入元素在集合类的末尾
	 * @param insertItem
	 */
	public void insert(T insertItem) {
		insert(currentSize, insertItem);
	}
	
	/**
	 * 将元素插入到数组的指定位置
	 * @param idx 元素插入的位置
	 */
	private void insert(int idx, T insertItem) {
		if(++this.currentSize >= this.items.length ) {
			throw new IndexOutOfBoundsException("集合内存空间不够！");
		}
		
		//所有的元素后移
		for(int i = this.currentSize;i > idx;--i) {
			this.items[i] = this.items[i - 1];
		}
		
		this.items[idx] = insertItem;
	}
	
	/**
	 * 删除例程。
	 */
	
	public void remove(int idx) {
		if(--this.currentSize < 0) {
			throw new IllegalArgumentException("数组为空不能删除");
		}
		
		for(int i = idx;i < currentSize;++i) {
			this.items[i] = this.items[i + 1];
		}
	}
	
	
	/**
	 * 找出最小值并且返回，时间界限为线性
	 * @return 集合中的最小值
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
	 * 找出最大值并且返回，时间界限为线性
	 * @return 集合中的最大值
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
