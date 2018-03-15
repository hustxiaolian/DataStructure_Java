package chapterTwo;

public class BinarySearch {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Integer[] arr = {1,2,6,8,9,10,15,16,19};
		System.out.println(binarySearchByRecurrence(arr, 6, 0, arr.length - 1));
	}
	
	/**
	 * 二分法查找对象，传入的数组必须按照从小到大排序。
	 * 关于二分法查找的具体原理，网上的版本很多，我就不再赘述。
	 * 
	 * 这是迭代实现的版本。
	 * 
	 * @param arr
	 * @param left
	 * @param right
	 * @return
	 */
	public static <T extends Comparable<? super T>> int binarySearch(T[] arr, T x, int left, int right) {
		int mid;
		
		while(left <= right) {
			mid = (left + right) / 2;
			if(x.equals(arr[mid])) {
				return mid;
			}
			else {
				if(x.compareTo(arr[mid]) < 0) {
					right = mid - 1;
				}
				else {
					left = mid + 1;
				}
			}
		}
		
		return -1;
	}
	
	/**
	 * 这是二分法的递归实现的版本.
	 * @param arr
	 * @param x
	 * @param left
	 * @param right
	 * @return
	 */
	public static <T extends Comparable<? super T>> int binarySearchByRecurrence(T[] arr, T x, int left, int right) {
		int mid = (left + right) / 2;
		if(x.equals(arr[mid])) {
			return mid;
		}
		else {
			if(x.compareTo(arr[mid]) < 0) {
				return binarySearchByRecurrence(arr, x, left, mid - 1);
			}
			else {
				return binarySearchByRecurrence(arr, x, mid + 1, right);
			}
		}
	}
}
