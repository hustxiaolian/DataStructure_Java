package chapterSeven;

public class SortTest {

	public static void main(String[] args) {
		Integer[] arr = generateMillionRandomIntNum(1000,100000);
		//Integer[] arr = {1,6,5,8,9,7,11,20,96,3,2,11,55,22,99,88,77,20,57,87,30};
		long start = System.currentTimeMillis();
		Sort.binaryHeapSort(arr);
		long end1 = System.currentTimeMillis();
		System.out.println("time1:" + (end1 - start));
		/*
		for (Integer integer : arr) {
			System.out.println(integer + ",");
		}
		*/
		
	}
	
	public static Integer[] generateMillionRandomIntNum(int up,int size) {
		Integer[] arr = new Integer[size];
		
		for(int i = 0;i <arr.length;++i) {
			arr[i] = (int)(Math.random() * up);
		}
		
		return arr;
	}
}
