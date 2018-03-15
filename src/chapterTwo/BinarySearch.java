package chapterTwo;

public class BinarySearch {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Integer[] arr = {1,2,6,8,9,10,15,16,19};
		System.out.println(binarySearchByRecurrence(arr, 6, 0, arr.length - 1));
	}
	
	/**
	 * ���ַ����Ҷ��󣬴����������밴�մ�С��������
	 * ���ڶ��ַ����ҵľ���ԭ�����ϵİ汾�ܶ࣬�ҾͲ���׸����
	 * 
	 * ���ǵ���ʵ�ֵİ汾��
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
	 * ���Ƕ��ַ��ĵݹ�ʵ�ֵİ汾.
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
