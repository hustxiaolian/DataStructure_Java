package chapterTwo;

public class PowerNum {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(pow(4.0, 5));
	}
	
	/**
	 *  π”√µ›πÈº∆À„x^n°£
	 * @param x
	 * @param n
	 * @return
	 */
	public static double pow(double x, int n) {
		if(n == 0)
			return 1;
		if(n == 1)
			return x;
		
		if(n % 2 == 0)
			return pow(x * x, n / 2);
		else {
			return pow(x * x, n / 2) * x;
		}
	}

}
