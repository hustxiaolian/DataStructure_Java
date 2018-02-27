package chapterOne;

/**
 * 
 * @author 25040
 *chapter 1-5
 */

public class getBinaryNum {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(getBinaryNum.solution1(111));
	}
	
	/**
	 * 递归地调用自身求解
	 * 基于这样的事实：如果N是奇数，那么其1的个数等于N/2的二进制表示的个数+1
	 * 显然，如果N为偶数，那么N/2的个数就是N的二进制1的个数
	 * @param Num 
	 * @return
	 */
	
	public static int solution1(int Num) {
		//先编写基准情况
		int result = 0;
		
		if(Num <= 1)
			return Num;
		
		if(Num % 2 ==0)
			result = solution1(Num / 2);
		else
			result =  solution1(Num / 2) + 1;
	
		return result;
	}

}
