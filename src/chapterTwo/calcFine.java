package chapterTwo;

public class calcFine {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println();
	}
	
	public static int solution1(int initialFine ,int day){
		int finalFine = initialFine;
		for(int i = 1;i <= day;++i) {
			finalFine *= finalFine;
		}
		return finalFine;
	}
}
