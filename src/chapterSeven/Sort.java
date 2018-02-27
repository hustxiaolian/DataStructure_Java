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
	 * 思路
	 */
	
}
