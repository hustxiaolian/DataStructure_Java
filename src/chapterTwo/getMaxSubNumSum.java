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
	 * ��ö�ٷ������������к�
	 * ʱ�临�Ӷ�N^2,�ռ临�Ӹ� 1
	 * @param a ���������
	 * @return ��������к�
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
	 * ���β��������������к�
	 * ʱ�临�Ӷ�NlogN���ռ临�Ӷ�1
	 * @param arr ���������
	 * @param left ��߽�
	 * @param right �ұ߽�
	 * @return
	 */
	public static int solution2(int[] arr,int left,int right) {
		//��׼����
		if(left == right) {
			return arr[left];
		}
		
		//�԰�֣��ݹ�
		int center = (left + right) / 2;
		int maxLeftSum = solution2(arr,left,center);
		int maxRightSum = solution2(arr,center+1,right);
		
		//�������ֵ
		int maxLeftBorderSum = 0;
		int leftBorderSum = 0;
		for(int i = center;i >= left;--i) {
			leftBorderSum += arr[i];
			if(leftBorderSum > maxLeftBorderSum) {
				maxLeftBorderSum = leftBorderSum;
			}
		}
		
		//�Ұ�����ֵ
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
	 * �޵е������㷨��ʱ�����Ϊ���ԣ��ռ临�Ӷ�Ϊ1
	 * ���ڵ���ʵ�ǣ����a[i]�Ǹ��ģ���ô������������������е���㣬ͬ�����a[p]��a[q]�ĺ�Ϊ������
	 * ��ô������������������к͵�ǰ׺
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