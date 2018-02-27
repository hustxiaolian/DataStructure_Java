package chapterThree;

public class MyDeque<T> {
	private int front;
	private int back;
	private int currentSize;
	private T[] theItems;
	
	@SuppressWarnings("unchecked")
	public MyDeque(int capacity) {
		this.theItems = (T[]) new Object[capacity];
		front = this.theItems.length / 2;
		back = front;
		currentSize = 0;
	}
	
	public boolean isEmpty() {
		return currentSize == 0;
	}
	
	public boolean isFull() {
		return currentSize == this.theItems.length;
	}
	
	public void push(T x) {
		if(isFull())
			throw new IllegalStateException("队列已满");
		this.theItems[resetIndex(--front)] = x;
		++this.currentSize;
	}
	
	public T pop() {
		if(isEmpty())
			throw new IllegalStateException("队列为空");
		--currentSize;
		T popItem = this.theItems[front];
		front = resetIndex(++front);
		return popItem;
	}
	
	public void inject(T x) {
		if(isFull()){
			throw new IllegalStateException("队列已满");
		}
		this.theItems[resetIndex(++back)] = x;
		++currentSize;
	}
	
	public T eject() {
		if(isEmpty()) {
			throw new IllegalStateException("队列为空");
		}
		--currentSize;
		T ejectItem = this.theItems[back];
		back = resetIndex(--back);
		return ejectItem;
	}
	
	private int resetIndex(int front) {
		if(front < 0)
			front = this.theItems.length + front;
		else if(front >= this.theItems.length) 
			front = 0;
		return front;
	}
}
