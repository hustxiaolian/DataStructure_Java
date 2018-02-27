package chapterTwo;

public class getMaxSubNumSum {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] arr = {4,-3,5,-2,-1,2,6,-2};
		System.out.println(solution1(arr));
		System.out.println(solution2(arr,0,arr.length-1));
		System.out.println(solution3(arr));
	}
	
	/**
	 * 用枚举法求解最大子序列和
	 * 时间复杂度N^2,空间复杂付 1
	 * @param a 待求解数组
	 * @return 最大子序列和
	 */
	
	public static int solution1(int[] a) {
		int maxSum = 0;
		
		for(int i=0;i < a.length;++i) {
			int lineSum = 0;
			for(int j = i;j < a.length;++j) {
				lineSum += a[j];
				if(lineSum > maxSum) {
					maxSum = lineSum;
				}
			}
		}
		
		return maxSum;
	}
	
	/**
	 * 分治策略求解最大子序列和
	 * 时间复杂度NlogN，空间复杂度1
	 * @param arr 待求解数组
	 * @param left 左边界
	 * @param right 右边界
	 * @return
	 */
	public static int solution2(int[] arr,int left,int right) {
		//基准情形
		if(left == right) {
			return arr[left];
		}
		
		//对半分，递归
		int center = (left + right) / 2;
		int maxLeftSum = solution2(arr,left,center);
		int maxRightSum = solution2(arr,center+1,right);
		
		//左半边最大值
		int maxLeftBorderSum = 0;
		int leftBorderSum = 0;
		for(int i = center;i >= left;--i) {
			leftBorderSum += arr[i];
			if(leftBorderSum > maxLeftBorderSum) {
				maxLeftBorderSum = leftBorderSum;
			}
		}
		
		//右半边最大值
		int maxRightBorderSum = 0;
		int rightBorderSum = 0;
		for(int i = center +1;i <= right;++i) {
			rightBorderSum += arr[i];
			if(rightBorderSum > maxRightBorderSum) {
				maxRightBorderSum = rightBorderSum;
			}
		}
		
		return Math.max(maxRightBorderSum + maxLeftBorderSum, 
				Math.max(maxLeftSum, maxRightSum));
	}
	/**
	 * 无敌的联机算法，时间界限为线性，空间复杂度为1
	 * 基于的事实是：如果a[i]是负的，那么它不可能是最大子序列的起点，同理，如果a[p]到a[q]的和为负数，
	 * 那么它不可能是最大子序列和的前缀
	 * @param arr
	 * @return
	 */
	public static int solution3(int[] arr) {
		int maxSum = 0;
		int lineSum = 0;
		
		for(int i = 0;i < arr.length;++i) {
			lineSum += arr[i];
			if(lineSum > maxSum) {
				maxSum = lineSum;
			}
			else
			{
				if(lineSum < 0) {
					lineSum = 0;
				}
			}
		}
		return maxSum;
	}

}