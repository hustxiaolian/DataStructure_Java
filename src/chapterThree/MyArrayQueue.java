package chapterThree;

public class MyArrayQueue<T> {
	
	private int front;
	private int back;
	private int currentSize;
	private T[] theItems;
	
	@SuppressWarnings("unchecked")
	public MyArrayQueue(int capacity) {
		this.theItems = (T[]) new Object[capacity];
		front = 0;
		back = -1;
		currentSize = 0;
	}
	
	public boolean isEmpty() {
		return currentSize == 0;
	}
	
	public boolean isFull() {
		return currentSize == this.theItems.length - 1;
	}
	
	public int size() {
		return currentSize;
	}
	
	public int capacitu() {
		return this.theItems.length;
	}
	
	public void enqueue(T x) {
		if(isFull())
			throw new IndexOutOfBoundsException("队列空间已经用完，元素(" + x + ")无法入队");
		++this.currentSize;
		this.theItems[++back % theItems.length] = x;
	}
	
	public T dequeue() {
		if(isEmpty())
			throw new IndexOutOfBoundsException("队列为空，没有元素可以出队");
		--this.currentSize;
		return this.theItems[front++ % theItems.length];
	}
}
