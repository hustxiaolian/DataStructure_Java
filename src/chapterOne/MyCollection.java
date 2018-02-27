package chapterOne;

import java.util.Arrays;

public class  MyCollection{
	
	private int size;
	private Object[] elementArray;
	
	public MyCollection(Object[] arr) {
		this.size = arr.length;
		this.elementArray = new Object[arr.length * 2];
		
		for(int i = 0;i < size;++i) {
			this.elementArray[i] = arr[i];
		}
	}
	
	private boolean isFull() {
		return this.size == this.elementArray.length;
	}
	
	public boolean isEmpty() {
		return this.size == 0;
	}
	
	public void makeEmpty() {
		this.size = 0;
	}
	
	public void add(Object x) {
		if(this.isFull()) {
			Object[] temp = this.elementArray;
			this.elementArray = new Object[this.size * 2];
			
			for(int i = 0;i < temp.length;++i) {
				this.elementArray[i] = temp[i];
			}
		}
		this.elementArray[size++] = x;
	}
	
	public void insert(int index,Object x) {
		if(index > this.size) {
			System.out.println("插入位置无效！");
			return;
		}
		
		if(index == this.size) {
			this.add(x);
		}
		
		int i = this.size;
		for(;i >= index;--i) {
			this.elementArray[i] = this.elementArray[i-1];
		}
		
		this.elementArray[i] = x;
	}
	
	public boolean isPresent(Object x) {
		
		for(int i = 0;i < this.size;++i) {
			if(this.elementArray[i].equals(x)) {
				return true;
			}
		}
		
		return false;
	}
	
	public String toString() {
		return Arrays.toString(this.elementArray);
	}
}


