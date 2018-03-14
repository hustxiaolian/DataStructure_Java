package chapterSeven;

import java.util.Arrays;

public class QuickSelect {

	public static void main(String[] args) {
		Integer[]  arr = {3,9,18,15,6,7,8,3,5,2,1,11};
		//Integer[]  arr = {1,1,1,1,1,1,1,1,1,1,1,1,1};
		//int pivotIndex = quickSelectKthElement(arr, 3);
		System.out.println(quickSelectKthElement(arr, 10));
		/*
		for (Integer integer : arr) {
			System.out.print(integer + " ");
		}
		System.out.println("pivot:" + arr[pivotIndex] + ",pivotIndex:" + pivotIndex);
		*/
	}
	
	public static <T extends Comparable<? super T>> T quickSelectKthElement(T[] arr, int kth) {
		return quickSelectKthElement(arr, kth, 0, arr.length - 1);
	}
	
	/**
	 * 快速选择的实际例程
	 * @param arr
	 * @param k
	 * @return
	 */
	public static <T extends Comparable<? super T>> T quickSelectKthElement(T[] arr, int i, int left, int right) {
		if(left + 3 <= right)
		{
			int r = partition(arr, left, right);
			int k = r - left + 1;
			if(i ==  k)
				//lucky,直接找到了
				return arr[r];
			else {
				if(i < k) {
					return quickSelectKthElement(arr, i, left, r - 1);
				}
				else {
					return quickSelectKthElement(arr, i - k, r + 1, right);
				}
			}
		}
		else {
			Sort.insertionSort(arr, left, right);
			return arr[left + i - 1];
		}
		
	}
	
	/**
	 * 使用三值分割的方法选取枢纽元，之后，把整个数组根据大小关系，把小于枢纽元的元素放在枢纽元的左边，
	 * 大于枢纽元的元素放在枢纽元的右边。
	 * 
	 * 要清楚三值分割不仅仅只有选取枢纽元的左右，更为重要的是设置了边界，使得i，j永远不会发生越界情况。
	 * @param arr
	 * @param left
	 * @param right
	 * @return
	 */
	public static <T extends Comparable<? super T>> int partition(T[] arr, int left, int right) {
		int i,j;
		
		T pivot = median3(arr, left, right);
		
		i = left;j = right - 1;
		for(;;) {
			//如果位置i的元素小于枢纽元就掠过，右移。当遇到大于等于枢纽元的元素时，就停下
			//如果位置j的元素大于枢纽元就掠过，左移。当遇到小于等于枢纽元的元素时，就停下。
			while(arr[++i].compareTo(pivot) < 0) {}
			while(arr[--j].compareTo(pivot) > 0) {}
			//当i在j的左边时候，就知道需要交换两个元素，反之，如果i > j就退出。
			//仔细当i == j时，而且该元素 == 枢纽元，因此没必要多此一举，再交换一次。
			if(i < j) {
				arraySwap(arr, i, j);
			}
			else {
				break;
			}
		}
		//交换i和枢纽元，为什么这里是i？
		//因为根据上述算法，退出循环时i为大于枢纽元的所有元素中位置最左边的那个，其他的更左边都已经被上述算法
		//交换到右边去了。同时，这时枢纽元的位置就在i。
		arraySwap(arr, i, right - 1);
		return i;
	}
	
	/**
	 * 三值分割，
	 * @param arr
	 * @param left
	 * @param right
	 */
	public static <T extends Comparable<? super T>> T median3(T[] arr, int left, int right) {
		int median = (left + right) / 2;
		//把这三个数放在正确的位置
		if(arr[left].compareTo(arr[median]) > 0)
			arraySwap(arr, left, median);
		if(arr[left].compareTo(arr[right]) > 0)
			arraySwap(arr, left, right);
		if(arr[median].compareTo(arr[right]) > 0)
			arraySwap(arr, median, right);
		//把枢纽元交换到待分割区域之外
		arraySwap(arr, median, right - 1); 
		//返回枢纽元的值
		return arr[right - 1];
	}
	
	/**
	 * 交换数组两个指定坐标的元素
	 * @param arr 需要进行交换操作的数组
	 * @param i 数组坐标
	 * @param j 数组坐标
	 * @throws java.lang.IndexOutOfBoundsException 传入坐标越界
	 */
	private static final <AnyType> void arraySwap(AnyType[] arr,int i,int j) {
		AnyType temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}
}
