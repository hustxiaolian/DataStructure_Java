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
	 * �ݹ�ص����������
	 * ������������ʵ�����N����������ô��1�ĸ�������N/2�Ķ����Ʊ�ʾ�ĸ���+1
	 * ��Ȼ�����NΪż������ôN/2�ĸ�������N�Ķ�����1�ĸ���
	 * @param Num 
	 * @return
	 */
	
	public static int solution1(int Num) {
		//�ȱ�д��׼���
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
