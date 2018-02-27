package chapterSeven;

public final class Sort {
	
	
	/**
	 * 按照从小到大的顺序进行插入排序
	 * 插入排序基于，在数组位置0-i的子数组已经完成排序，因此位置i+1的元素只需要依次向前比对，插入到前面子数组合适的位置即可
	 * 时间界限为O（N^2）
	 * @param arr 需要排序的数组
	 */
	public static <AnyType extends Comparable<? super AnyType>> void insertionSort(AnyType[] arr) {
		int insertionPos;
		
		for(int nowPos = 1;nowPos < arr.length; ++nowPos) {
			//记录当前需要向前插入的元素
			AnyType nowItem = arr[nowPos];
			
			//向前比对，寻求插入的合适位置
			for(insertionPos = nowPos - 1;insertionPos >= 0 && 
					arr[insertionPos].compareTo(arr[insertionPos + 1]) > 0;--insertionPos) {
				
				arr[insertionPos + 1] =  arr[insertionPos];
			}
			//插入
			arr[insertionPos] = nowItem;
		}
	}
	
	/**
	 * 使用希尔增量进行希尔排序
	 * 希尔排序的的核心在hk排序性在之后的增量排序中保持不变
	 * 思路步骤：
	 * 1. 首先必须有建立，或者外部传入一个增量序列，其第一个值必须为1.
	 * 2. 再按照插入排序的思路，进行hk排序。
	 * @param arr 需要排序的数组
	 */
	public static <AnyType extends Comparable<? super AnyType>> void shellSort(AnyType[] arr) {
		for(int gap = arr.length / 2 ;gap > 0 ;gap /= 2) {
			int j;
			
			//有了希尔增量后，之后的思路和插入排序类型
			for(int i = gap;i < arr.length; ++i) {
				AnyType nowItem = arr[i];
				
				for(j = i; j > 0 && arr[j].compareTo(arr[j - gap]) < 0; --j) {
					arr[j] = arr[j - gap];
				}
				
				arr[j] = nowItem;
			}
		}
	}
	
	/**
	 * 堆排序
	 * 思路：
	 * 1. 首先使用传入的数组进行下滤操作，建立最大（最小）二叉堆。
	 * 2. 使用二叉堆的deleteMin操作（这个操作中包含又一次的下滤操作），进行多次，并且把返回值放入二叉堆使用数组的后面，节省空间。
	 * 
	 */
	public static <AnyType extends Comparable<? super AnyType>> void binaryHeapSort(AnyType[] arr) {
		//循环下滤建立二叉堆
		for(int i = arr.length / 2;i >= 0;--i) {
			percDown(arr, i, arr.length);
		}
		//使用二叉堆的deleteMin操作
		for(int i = arr.length - 1;i > 0;--i) {
			arraySwap(arr, 0, i);
			percDown(arr, 0, i);
		}
	}
	
	/**
	 * 堆排序在建立二叉堆过程中使用的下滤操作。
	 * @param arr
	 * @param hole
	 */
	private static <AnyType extends Comparable<? super AnyType>> void percDown(AnyType[] arr , int hole , int heapLength) {
		int i,child;
		AnyType percDownItem = arr[hole];
		
		for(i = hole; i * 2 < heapLength;i = child) {
			child = i * 2;
			//寻找儿子中更小的
			if(child + 1 < arr.length && arr[child + 1].compareTo(arr[child]) < 0) {
				++child;
			}
			//看当前的hole是否满足堆序性要求
			if(arr[i].compareTo(arr[child]) < 0) {
				break;
			}
			//否则将hole继续沿着儿子路径下滤
			else {
				arr[i] = arr[child];
			}
		}
		
		arr[i] = percDownItem;
	}
	
	/**
	 * 交换数组两个指定坐标的元素
	 * @param arr 需要进行交换操作的数组
	 * @param i 数组坐标
	 * @param j 数组坐标
	 * @throws java.lang.IndexOutOfBoundsException 传入坐标越界
	 */
	private static <AnyType> void arraySwap(AnyType[] arr,int i,int j) {
		AnyType temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}
	
	/**
	 * 归并排序，是后面快速排序的基础。
	 * 归并排序的原理：
	 * 1. 递归划分两半。
	 * 2. 对两半进行排序。实际上，当递归进入到基准情形（只有一个元素时），
	 * 根本不需要排序，而具有两个元素的子数组，可以通过两个一元素子数组通过{@code mergeTwoSortedArray}方法
	 * 完成排序。
	 * 3. 完成之后对已经排序过的子数组进行合并操作。
	 * 
	 * 同时，值得注意的是，这里有一个不起眼但是很重要的亮点。就是事先就声明了一个等长度
	 * 的暂时数组，用作排序过程中的临时缓冲区，
	 */
	@SuppressWarnings("unchecked")
	public static <AnyType extends Comparable<? super AnyType>> void mergeSort(AnyType[] arr) {
		AnyType[] tempArray = (AnyType[]) new Comparable[arr.length];
		
		mergeSort(arr, tempArray, 0, arr.length - 1);
	}
	
	/**
	 * 归并排序的实际运作程序，程序思路很简单清晰。分别对左右两个子数组进行排序，然后合并。
	 * 当left < right时，说明子数组至少有两个元素，因此需要排序。
	 * 当left == right时，说明子数组只有一个元素了，啥也不用干，直接退出。
	 * @param arr
	 * @param temp
	 * @param left
	 * @param right
	 */
	private static <AnyType extends Comparable<? super AnyType>> void 
	mergeSort(AnyType[] arr, AnyType[] temp ,int left, int right) {
		//当left < right时，说明子数组至少有两个元素，因此需要排序
		//当left == right时，说明子数组只有一个元素了，啥也不用干，直接退出
		if(left < right) {
			int center = (left + right) / 2;
			mergeSort(arr, temp, left, center);
			mergeSort(arr, temp, center + 1, right);
			mergeTwoSortedArray(arr, temp, left, center + 1, right);
		}
	}
	
	/**
	 * 将两个已经完成排序的相邻数组进行合并。值得注意的是，当传入的子数组大小都是1时，就相当于完成了基准情形的排序。
	 * 
	 * @param arr 需要进行归并排序的数组
	 * @param tempArray 暂时缓冲数组
	 * @param leftPos 左子数组的起始位置
	 * @param rightPos 右子数组的起始位置
	 * @param rightEnd 右子数组的终止位置
	 */
	private static <AnyType extends Comparable<? super AnyType>> void 
			mergeTwoSortedArray (AnyType[] arr,AnyType[] tempArray,int leftPos,int rightPos ,int rightEnd) {
		int leftEnd = rightPos - 1;
		int tempPos = leftPos;
		int numElements = rightEnd - leftPos + 1;
		
		while(leftPos <= leftEnd && rightPos <= rightEnd ) {
			if(arr[leftPos].compareTo(arr[rightPos]) < 0) {
				tempArray[tempPos++] = arr[leftPos++];
			}
			else {
				tempArray[tempPos++] = arr[rightPos++];
			}
		}
		
		while(leftPos <= leftEnd) {
			tempArray[tempPos++] = arr[leftPos++];
		}
		
		while(rightPos <= rightEnd) {
			tempArray[tempPos++] = arr[rightPos++];
		}
		
		for(int i = 0;i < numElements;++i,--rightEnd) {
			arr[rightEnd] = tempArray[rightEnd];
		}
	}
	
	/**
	 * 传说中大名鼎鼎的快排！
	 * @param arr
	 */
	public static <AnyType extends Comparable<? super AnyType>> void quickSort(AnyType[] arr) {
		
	}
}
