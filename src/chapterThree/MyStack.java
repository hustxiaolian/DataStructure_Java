package chapterThree;

public class MyStack<AnyType> {
	
	private static final int DEAFAULT_CAPACITY = 10;
	
	private AnyType[] theItems;
	private int topOfStack;
	
	public MyStack() {
		doClear();
	}
	
	public void clear() {
		doClear();
	}
	
	private void doClear() {
		this.topOfStack = 0;
		ensureCapacity(DEAFAULT_CAPACITY);
	}
	
	@SuppressWarnings("unchecked")
	public void ensureCapacity(int capacity) {
		if(capacity < this.topOfStack)
			return;
		AnyType[] old = this.theItems;
		this.theItems = (AnyType[])new Object[capacity];
		for(int i = 0;i < old.length;++i) {
			this.theItems[i] = old[i];
		}
	}
	
	public AnyType pop() {
		return this.theItems[topOfStack--];
	}
	
	public void push(AnyType x) {
		this.theItems[++topOfStack] = x;
	}
}
