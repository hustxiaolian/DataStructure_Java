package chapterThree;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;

/**
 * 
 * @author 25040
 *
 * @param <AnyType>
 */
public class MyArrayList<AnyType> implements Iterable<AnyType>{
	
	
	private static final int DEFAULT_CAPACITY = 10;
	
	private int theSize;
	private int modCount = 0;
	private AnyType[] theItems;
	
	
	public MyArrayList() {
		doClear();
	}
	
	public MyArrayList(AnyType[] arr) {
		doClear();
		for (int i = 0; i < arr.length; i++) {
			add(arr[i]);
		}
	}
	
	public void clear() {
		doClear();
	}
	
	private void doClear() {
		this.theSize = 0;
		ensureCapacity(DEFAULT_CAPACITY);
	}
	
	public int size() {
		return this.theSize;
	}
	
	public boolean isEmpty() {
		return this.size() == 0;
	}
	
	public void trimToSize() {
		ensureCapacity(this.size());
	}
	
	public AnyType get(int idx){
		if(idx < 0 || idx >= size()) {
			throw new ArrayIndexOutOfBoundsException();
		}
		
		return this.theItems[idx];
	}
	
	public AnyType set(int idx,AnyType newVal) {
		if(idx < 0 || idx >=size()) {
			throw new ArrayIndexOutOfBoundsException();
		}
		
		AnyType old = this.theItems[idx];
		this.theItems[idx] = newVal;
		modCount++;
		return old;
	}
	
	public boolean add(AnyType newVal) {
		add(size(),newVal);
		return true;
	}
	
	/**
	 * 
	 * @param idx
	 * @param newVal
	 * ps:如果idx越界怎么办，那么它会自动抛出ArrayIndexOutOfBoundsException
	 */
	public void add(int idx,AnyType newVal) {
		if(this.theItems.length == size()) {
			ensureCapacity(2 * size() + 1 );
		}
		for(int i = size();i > idx;--i) {
			this.theItems[i] = this.theItems[i - 1];
		}
		this.theItems[idx] = newVal;
		this.theSize++;
		this.modCount++;
	}
	
	public AnyType remove(int idx) {
		AnyType removedItem = get(idx);
		for(int i = idx;i < size() - 1;++i) {
			this.theItems[i] = this.theItems[i + 1];
		}
		
		this.theSize--;
		this.modCount++;
		return removedItem;
	}
	
	@SuppressWarnings("unchecked")
	public void ensureCapacity(int newCapacity) {
		if(newCapacity < this.theSize) {
			return;
		}
		
		AnyType[] old = this.theItems;
		this.theItems = (AnyType[])new Object[newCapacity];
		for(int i = 0;i < this.theSize;++i) {
			this.theItems[i] = old[i];
		}
	}
	
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
	 * @author xiaolian
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
