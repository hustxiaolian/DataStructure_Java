package chapterTwo;

public class FabonacciNumbers {

	public static void main(String[] args) {
		System.out.println(getFabanacciNumber(20));
	}
	
	/**
	 * ʹ�ö��׾������ķ�ʽ����쳲��������У������ظ����㡣
	 * ���ݵ���ʵ��������:һ��������ʽ��ʹ����ѧ���ɷ����Ժ����׵�֤��������ȷ�ԡ�
	 * Fn   Fn-1  = (1 1)n
	 * Fn-1 Fn-2  = (1 0)
	 * 
	 * ��׼���Σ�
	 * f0 = 0
	 * f1 = 1
	 * f2 = 1
	 * 
	 * @param n
	 * @return
	 */
	public static int getFabanacciNumber(int n) {
		return fabanacciMatrixPow(new int[][] {{1,1},{1,0}}, n - 1) [0][0];
	}
	
	/**
	 * ʹ�õݹ����int��powһ���ļ��ɣ�ʹ�õݹ�
	 * @param matrix
	 * @param n
	 * @return
	 */
	private static int[][] fabanacciMatrixPow(int[][] matrix, int n){
		if(n == 1) {
			return matrix;
		}
		
		if(n % 2 == 0)
			return fabanacciMatrixPow(intMatrixMul(matrix, matrix), n / 2);
		else 
			return intMatrixMul(fabanacciMatrixPow(intMatrixMul(matrix, matrix), n / 2), matrix);
	}
	
	/**
	 * ����Ԫ������Ϊ�����ľ������
	 * ��һ�����������=�ڶ��������������
	 * ���ݾ���˷�������
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static int[][] intMatrixMul(int[][] a, int[][] b){
		int aRow = a[0].length;
		int aCol = a.length;
		
		int bRow = b[0].length;
		int bCol = b.length;
		
		if(aCol != bRow) {
			throw new IllegalArgumentException();
		}
		int[][] result = new int[aRow][bCol];
		
		for(int i = 0; i < aRow;++i) {
			for(int j = 0;j < bCol;++j) {
				for(int k = 0;k < aCol;++k) {
					result[i][j] += a[i][k] * b[k][j];
				}
			}
		}
		
		return result;
	}
	
	/**
	 * ��ӡ����
	 * @param m
	 */
	public static void printMatrix(int[][] m) {
		for(int i = 0;i < m.length;++i) {
			for(int j = 0;j < m[i].length;++j) {
				System.out.print(m[i][j] + "\t");
			}
			System.out.println();
		}
	}
}
