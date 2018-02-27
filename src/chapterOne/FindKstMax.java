package chapterOne;

import java.util.Arrays;
/**
 * 
 * @author 25040
 *chapter 1-1
 */
public class FindKstMax {
	
	public static void main(String[] args) {
		int[] a = {3,4,1,9,8,3,5};
		System.out.println(FindKstMax.solution2(a, a.length/2));
		System.out.println(Arrays.toString(a));
	}

	
	/**
	 * 冒泡法对其全部排序后再返回第k个
	 * 时间界限是N^2
	 * @param arr 引用传递，会改变数组的数据
	 * @return k st integer
	 */
	public static int solution1(int[] arr,int k) {
		//冒泡法，把大的往后面滤
		FindKstMax.maoPaoSort(arr);
		return arr[k-1];
	}
	/**
	 * 冒泡排序法
	 * @param arr 需要进行冒泡排序的数组
	 */
	
	public static void maoPaoSort(int[] arr) {
		for(int i =0;i < arr.length;++i) {
			for(int j=0;j < arr.length - i - 1;++j) {
				if(arr[j] < arr[j+1]) {
					int temp = arr[j];
					arr[j] = arr[j+1];
					arr[j+1] = temp;
				}
			}
		}
	}
	
	/**
	 * 生成一个size = K的数组，然后对它完成递减排序，然后将依次遍历后面的元素，将它们到生成数组的合适的位置
	 * 时间界限为N*K，空间复杂度为K
	 * 且它没有改变原数组
	 */
	public static int solution2(int[] arr,int k) {
		int[] arrK = new int[k];
		for(int i = 0;i < k;++i)
			arrK[i] = arr[i];
		//对生成的数组排序
		FindKstMax.maoPaoSort(arrK);
		System.out.println(Arrays.toString(arrK));
		//然后循环遍历
		for(int i = k;i < arr.length;++i) {
			//如果后面的元素比数组内的最小数（即最后一个数）,这里使用了二叉堆算法中的技巧
			int j = k - 1;
			for(;j > 0 && arrK[j] < arr[i];--j) {
				if(arrK[j-1] > arr[i])
					break;
				else
					arrK[j-1] = arrK[j];
			}
			arrK[j] = arr[i];
		}
		
		return arrK[k-1];
		
	}

}
