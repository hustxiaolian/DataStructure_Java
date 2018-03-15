package chapterTwo;

public class FabonacciNumbers {

	public static void main(String[] args) {
		System.out.println(getFabanacciNumber(20));
	}
	
	/**
	 * 使用二阶矩阵计算的方式计算斐波那契数列，避免重复计算。
	 * 依据的事实定理如下:一个矩阵表达式，使用数学归纳法可以很容易的证明他的正确性。
	 * Fn   Fn-1  = (1 1)n
	 * Fn-1 Fn-2  = (1 0)
	 * 
	 * 基准情形：
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
	 * 使用递归求解int的pow一样的技巧，使用递归
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
	 * 两个元素类型为整数的矩阵相乘
	 * 第一个矩阵的列数=第二个矩阵的行数。
	 * 根据矩阵乘法的性质
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
	 * 打印矩阵
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
