package chapterThree;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;

/**
 * @since 2018-3-4 第一次review
 * 按照书上的提示，自己完成的ArrayList集合类
 * ArrayList的底层是使用数组实现的，由于每次插入都会动态检查下当前的容量是否满足要求，所以会动态的扩展当前的容量
 * 目前认为可以改进的地方是对于如果当前数组的使用率过低，我们可以适当的反向扩展数组。
 * 有点像散列表中根据填充因子进行再散列一样。
 * 
 * 下次review就是好好研究下Iterator和ListIterator
 * 
 * @author 25040
 *
 * @param <AnyType>
 */
public class MyArrayList<AnyType> implements Iterable<AnyType>{
	//默认的数组容量，用于无参构造器
	private static final int DEFAULT_CAPACITY = 10;
	//记录当前的数组中元素具体的数目
	private int theSize = 0;
	//记录当前集合类自构造初始化后的修改次数，能够修改的函数主要是add和remove
	//同时，也是用于保证迭代器正常工作的重要标志
	private int modCount = 0;
	//具体的元素数组
	private AnyType[] theItems;
	
	/**
	 * 无参构造
	 */
	public MyArrayList() {
		doClear();
	}
	
	/**
	 * 有参构造
	 * @param capacity 指定容量
	 */
	public MyArrayList(int capacity) {
		ensureCapacity(capacity);
	}
	
	/**
	 * 有参构造，使用是一个数组来初始化ArrayList。
	 * 由于数组重新分配内存空间以及将原来数组的内容负值到新数组中需要线性（O（N））时间。
	 * 再加上该函数一个for循环，约等于双倍的线性时间
	 * 
	 * @param arr 用于初始化ArrayList的数组
	 */
	public MyArrayList(AnyType[] arr) {
		ensureCapacity(arr.length * 2);
		for (int i = 0; i < arr.length; i++) {
			add(arr[i]);
		}
	}
	
	/**
	 * 清空当前集合
	 */
	public void clear() {
		doClear();
	}
	
	/**
	 * size参数置零，把数组容量恢复到默认大小
	 */
	private void doClear() {
		this.theSize = 0;
		ensureCapacity(DEFAULT_CAPACITY);
	}
	
	/**
	 * 返回当前集合的项目树（size）
	 * @return
	 */
	public int size() {
		return this.theSize;
	}
	
	/**
	 * 判断当前集合是否为空
	 * @return
	 */
	public boolean isEmpty() {
		return this.size() == 0;
	}
	
	/**
	 * 使集合的容量完全缩进至size数，没有多余的空间，节省空间，但是一旦使用add函数，就会重新
	 * 激活{@link ensureCapacity},因此除非保证以后不再插入新元素，尽量不要使用它。
	 */
	public void trimToSize() {
		ensureCapacity(this.size());
	}
	
	/**
	 * 根据索引，返回元素，时间界限为常数
	 * @param idx 数组下标索引
	 * @return 索引对象的元素
	 * @throws java.lang.IndexOutOfBoundsException idx不满足0<= idx < size时 
	 */
	public AnyType get(int idx){
		if(idx < 0 || idx >= size()) {
			throw new ArrayIndexOutOfBoundsException();
		}
		
		return this.theItems[idx];
	}
	
	/**
	 * 根据数组下标，重新设置对应的元素
	 * @param idx 索引
	 * @param newVal 新值
	 * @return 旧值
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
	 * 在当前集合的末尾插入元素，时间界限为O（1）常数
	 * @param newVal
	 * @return
	 */
	public boolean add(AnyType newVal) {
		add(size(),newVal);
		return true;
	}
	
	/**
	 * 在当前数组集合的中间插入元素，最坏时间界限为线性
	 * @param idx
	 * @param newVal
	 * ps:如果idx越界怎么办，那么它会自动抛出ArrayIndexOutOfBoundsException
	 */
	public void add(int idx,AnyType newVal) {
		if(this.theItems.length == size()) {
			ensureCapacity(2 * size() + 1 );
		}
		//所有的元素后移一位，把idx对应的位置空出来
		for(int i = size();i > idx;--i) {
			this.theItems[i] = this.theItems[i - 1];
		}
		
		this.theItems[idx] = newVal;
		this.theSize++;
		this.modCount++;
	}
	
	/**
	 * @version 1.0
	 * 直接删除返回下标索引位置上的元素，把并且把idx之后的元素全部前移一位，因此最坏时间界限为线性
	 * @version 1.1
	 * 在1.0版本的基础上，检查当前数组的填充因子({@code size() / theItems.length})。根据计算，如果我们
	 * 假设用户在使用时，remove和add的频率差不多的话，为了保证尽量减少使用{@code ensureCapacity}函数的概率，
	 * 我们在再分配数组后，可以add和remove的次数应该相差不多。同时，我们假设每次再分配的容量系数为2（增大）和0.5（缩小）
	 * 因此，我们填充因子就可以计算得到为1/3;
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
	 * 集合类的容量再分配函数，核心函数之一。
	 * 思路很简单清晰，就是按照新的容量重新申请数组，把原来旧数组的元素拷贝到新数组中
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
	 * 这里关于泛型的概念，我还得再去看看书，有点不是很清晰
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
	 * listIterator迭代器，实现方式跟iterator相似，但是增加了向前迭代的能力。
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
				throw new IndexOutOfBoundsException("右边越界");
			return MyArrayList.this.theItems[++current];
		}

		@Override
		public int nextIndex() {
			return current + 1;
		}

		@Override
		public AnyType previous() {
			if(!hasPrevious())
				throw new IndexOutOfBoundsException("左边越界");
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
	 * 内部类,next方法中的next方法中的类型参数是外部类的AnyType，
	 * 该内部类实现了Iterator<E>接口
	 * 
	 * 可以好好研究下，这种迭代器的设计方式，十分严谨合理。
	 * 从下面迭代器的代码我们可以看出，使用迭代器remove时，必须先next才行。
	 * 
	 * 关于语法上，还有一点注意的时，内部类使用外部类的方法时，格式为 类型.this.方法名(参数列表)
	 * 
	 * @author 25040
	 *
	 */
	private class ArrayListIterator implements Iterator<AnyType>{
		private int current = 0;
		private int exceptedModCount = MyArrayList.this.size();//防止在这个过程中用户改变了数组得元素
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
			//内部类使用外部的非静态方法：类名.this.方法名();
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
	 * 书中第3.30a 自调整表的数组实现。也就是把想要get元素不仅仅是返回，同时，将他放在第一个来，然后其他元素后移
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
