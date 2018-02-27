package chapterSix;

/**
 * 最小二叉堆，无非就是数组进行操作，其原理为：
 * 在数组中，从游标为1的位置开始，1为根。
 * 假设任意一节点的左边为x，则该节点的父亲为x / 2,其左儿子为 x * 2,右儿子为 x * 2 + 1
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
	 * 由于最小二叉堆其基本数据结构为数组，因此初始化必须提供相应的容量。
	 * 默认的无参构造器将使用默认容量{@code DEFAULT_CAPACITY = 10}来初始化数组
	 */
	public BinaryHeap() {
		this(DEFAULT_CAPACITY);
	}
	
	/**
	 * 有参构造器，根据提供的参数进行类的实例化
	 * @param capacity 二叉堆的大小
	 */
	@SuppressWarnings("unchecked")
	public BinaryHeap(int capacity) {
		this.currentSize = 0;
		this.array = (AnyType[]) new Object[capacity + 1];
	}
	
	/**
	 * 使用一个数组来构建二叉堆
	 * 其核心步骤就是将1 - currentSize位置的单元都进行下滤操作
	 * @param items
	 */
	public BinaryHeap(AnyType[] items) {
		this(items.length);
		//从数组中拷贝数据
		for(int i = 1;i <=  currentSize;++i)
			this.array[i] = items[i - 1];
		//下滤
		for(int i = currentSize / 2;i > 0;--i) {
			percolateDown(i);
		}
		
	}

	/**
	 * 二叉堆的插入例程。
	 * 当然首先，是必须判断数组大小够不够，不够的化需要扩大数组，并且完成原有数据的转移
	 * 其原理和步骤为：
	 * 1.在当前数组的末尾构建一个气泡，将x插入，但是其可能破坏了二叉堆的堆序性（即父亲不比儿子小），因此需要将x向上
	 * 		移动，这项操作专业点就是上滤。
	 * 2.在上滤的过程，即沿着父节点向上寻找合适的位置。
	 * @param x 欲插入的项
	 */
	public void insert(AnyType x) {
		//检查数组大小，不够则重建二叉堆
		if(currentSize == this.array.length - 1) {
			enlargeArray( 2  * this.array.length + 1);
		}
		//下滤
		int i;
		
		for(i = ++currentSize;i > 0 && this.array[i / 2].compareTo(x) > 0;i /= 2) {
			this.array[i] = this.array[i / 2];
		}
		
		this.array[i] = x;
	}
	
	/**
	 * 由于最小二叉堆的根就是最小值，直接返回即可
	 * @return 二叉堆的最小值
	 */
	public AnyType findMin() {
		return this.array[1];
	}
	
	/**
	 * 删除最小值，即删除根，那么位置1空出来了，为了保证二叉堆的性质，必须要让该空位下滤
	 * 原理步骤：
	 * 1. 记录最小值和数组最后一个数（由于{@code currentSize} 减小了）
	 * 2. 气泡下滤。沿着儿子路径找寻{@code lastItem}可以在不破坏堆序性的合适的地方。
	 * 3
	 * 
	 * @return 返回删除的最小值
	 */
	public AnyType deleteMin() {
		AnyType minItem = findMin();
		this.array[1] = this.array[currentSize--];
		
		percolateDown(1);
		
		return minItem;
	}
	
	/**
	 * 检查二叉堆是否为空
	 * @return 空则true，非空则false
	 */
	public boolean isEmpty() {
		return this.array[1] == null;
	}
	
	/**
	 * 将该二叉堆清空
	 */
	public void makeEmpty() {
		for(int i = 1;i <= currentSize;++i)
			this.array[i] = null;
		currentSize = 0;
	}
	
	/**
	 * 下滤例程。
	 * 原理步骤如下：
	 * 1. 与插入例程类似，先保存要下滤单元的值。
	 * 2. 沿着最小儿子路径下滤，寻找合适的地方插入{@code tempItem}即 {@code array[hole]}，也就是满足堆序性的地方。
	 * @param hole 
	 */
	private void percolateDown(int hole) {
		AnyType tempItem = this.array[hole];
		
		int i,child;
		for(i = hole;i * 2 < this.currentSize;i = child) {
			child = 2 * i;
			//在左儿子和右儿子中找出较小的那个，同时注意防止只有左儿子的节点
			if(child + 1 <= currentSize && this.array[child + 1].compareTo(this.array[child]) < 0) {
				child++;
			}
			
			//判断lastItem插入这里是否合适（满足堆序性），是则插入，否则气泡下滤
			if(tempItem.compareTo(this.array[child]) < 0) {
				this.array[i] = tempItem;
			}
			else {
				this.array[i] = this.array[child];
			}
		}
	}
	
	/**
	 * 以更大的数组容量重建堆
	 * @param newSize 新堆的容量
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
