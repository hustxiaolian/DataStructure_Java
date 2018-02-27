package chapterFive;

/**
 * 
 * @author 25040
 *
 */
public final class CrossWordPuzzle {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[][] charMatrix= {{"t","h","i","s"},
								{"w","a","t","s"},
								{"o","a","h","g"},
								{"f","g","d","t"}};
		solution1(charMatrix);
	}
	
	/**
	 * 猜字谜程序。
	 * 首先，程序建立了所有可能单词的散列表，首先以单词字符数散列，然后，然后再以首字母散列到最终的单词
	 * 然后，遍历二维数组中的每个字符，然后不同方向（8个方向），每种可能的字符数依次检测是否在HashTable当前
	 * @param charMatrix 输入二维字符数组，这里为了方便以及避免编码问题选择二维String数组
	 */
	public static void solution1(String[][] charMatrix) {
		
		final int charMinCharNum = 2;
		
		String[] correctWord = {"this","two","fat","that"};
		SeparateChainingHashTable<String> hash = CrossWordPuzzle.builtHashTable(correctWord);
		//每个字符，逐行逐列遍历
		for(int hang = 0;hang < charMatrix.length;++hang) {
			for(int lie = 0;lie < charMatrix[hang].length;++lie) {
				//然后遍历八个方向
				for(int direction = 0;direction < 8;++direction) {
					int charNum = charMinCharNum;
					switch(direction) {
					//竖直向上
					case 0:
						while(hang - charNum >= 0) {
							CrossWordPuzzle.checkInHashTableAndOutput(hash, hang, lie, direction, charNum, charMatrix);
							++charNum;
						}
						break;
					//右上方向
					case 1:
						while(hang - charNum >= 0 && lie + charNum < charMatrix[hang].length) {
							CrossWordPuzzle.checkInHashTableAndOutput(hash, hang, lie, direction, charNum, charMatrix);
							++charNum;
						}
						break;
					//右水平
					case 2:
						while(lie + charNum < charMatrix[hang].length) {
							CrossWordPuzzle.checkInHashTableAndOutput(hash, hang, lie, direction, charNum, charMatrix);
							++charNum;
						}
						break;
					//右下
					case 3:
						while(hang + charNum < charMatrix.length && lie + charNum < charMatrix[hang].length) {
							CrossWordPuzzle.checkInHashTableAndOutput(hash, hang, lie, direction, charNum, charMatrix);
							++charNum;
						}
						break;
					//竖直向下
					case 4:
						while(hang + charNum < charMatrix.length) {
							CrossWordPuzzle.checkInHashTableAndOutput(hash, hang, lie, direction, charNum, charMatrix);
							++charNum;
						}
						break;
					//左下
					case 5:
						while(hang + charNum < charMatrix.length && lie - charNum >= 0) {
							CrossWordPuzzle.checkInHashTableAndOutput(hash, hang, lie, direction, charNum, charMatrix);
							++charNum;
						}
						break;
					//左水平方向
					case 6:
						while(lie - charNum >= 0) {
							CrossWordPuzzle.checkInHashTableAndOutput(hash, hang, lie, direction, charNum, charMatrix);
							++charNum;
						}
						break;
					//左上方向
					case 7:
						while(hang - charNum >= 0 && lie - charNum >= 0) {
							CrossWordPuzzle.checkInHashTableAndOutput(hash, hang, lie, direction, charNum, charMatrix);
							++charNum;
						}
						break;
					}
				}
			}
		}
	}
		
	/**
	 * 调用{@code inHashTable}和{@code getWordFromCharMatrix}静态方法检验四元组得出的单词是否在HashTable中，
	 * 如果在，返回输出信息，否则返回null
	 * @param hashtable
	 * @param hang
	 * @param lie
	 * @param direction
	 * @param charNum
	 * @param charMatrix
	 * @return
	 */
	private static void checkInHashTableAndOutput(SeparateChainingHashTable<String> hashtable,
			int hang, int lie,int direction,
			int charNum,String[][] charMatrix) {
		if(inHashTable(hashtable, CrossWordPuzzle.getWordFromCharMatrix(hang, lie, direction, charNum, charMatrix)))
			System.out.println("首字母的坐标:(" + (hang+1) + "," + (lie + 1) + ") 方向为：" + direction + " 字符数为：" + (1 + charNum)
					+"该单词为：" + CrossWordPuzzle.getWordFromCharMatrix(hang, lie, direction, charNum, charMatrix));
	}
	
	
	/**
	 * 传入单词/数组，使用分离链接法建立的HashTable,并且返回其引用。
	 */
	private static SeparateChainingHashTable<String> builtHashTable(String[] words) {
		SeparateChainingHashTable<String> hashtable = new SeparateChainingHashTable<>();
		//String[] words = {"this","two","fat","that"};
		for (int i = 0; i < words.length; i++) {
			hashtable.insert(words[i]);
		}
		return hashtable;
	}
	
	/**
	 * 传入单词，并且在HashTable查询，返回布尔值。
	 * @param hashtable
	 * @param matrixWord
	 * @return 如果在，则返回true，否则返回false
	 */
	private static boolean inHashTable(SeparateChainingHashTable<String> hashtable,String matrixWord) {
		return hashtable.contain(matrixWord);
	}
	
	/**
	 * 根据四元组的信息（当前字符的行数、列数、方向和字符数）从矩阵中提取成String类型，并且返回
	 * @param hang 当前字符的行数
	 * @param lie	当前字符的列数
	 * @param direction	提取单词的方向（12点方向为0，顺时针类推，共计八个方向）
	 * @param charNum	提取单词的字符数
	 * @param charMatrix	字符二维矩阵
	 * @return	String字符
	 * @throws java.lang.IndexOutOfBoundsException 可能传入了错误的方向或者字符数使得，矩阵游标越界
	 */
	private static String getWordFromCharMatrix(int hang, int lie,int direction,
			int charNum,String[][] charMatrix) {
		String result = "";
		
		switch(direction) {
		//12点方向，竖直向上方向,同时在这里判断是否向上超出界限，是则直接抛出异常
		case 0: 
			if(hang - charNum < 0)
				throw new IndexOutOfBoundsException("单词提取过程中，向上超出边界！");
			else {
				for(int i = 0;i <= charNum;++i) {
					result += charMatrix[hang - i][lie];
				}
			}
			break;
		//1点半，右上方向
		case 1:
			if(hang - charNum < 0 || lie + charNum >= charMatrix.length)
				throw new IndexOutOfBoundsException("单词提取过程中，向右上超出边界！");
			else {
				for(int i = 0;i <= charNum;++i) {
					result += charMatrix[hang - i][lie + i];
				}
			}
			break;
		//三点方向 ，右水平方向
		case 2:
			if(lie + charNum >= charMatrix.length)
				throw new IndexOutOfBoundsException("单词提取过程中，向右超出边界！");
			else {
				for(int i = 0;i <= charNum;++i) {
					result += charMatrix[hang][lie + i];
				}
			}
			break;
		//四点半，右下方向
		case 3:
			if(hang + charNum >= charMatrix.length || lie + charNum >= charMatrix.length)
				throw new IndexOutOfBoundsException("单词提取过程中，向右下超出边界！");
			else {
				for(int i = 0;i <= charNum;++i) {
					result += charMatrix[hang + i][lie + i];
				}
			}
			break;
			
		//六点方向，竖直向下
		case 4:
			if(hang + charNum >= charMatrix.length)
				throw new IndexOutOfBoundsException("单词提取过程中，向下超出边界！");
			else {
				for(int i = 0;i <= charNum;++i) {
					result += charMatrix[hang + i][lie];
				}
			}
			break;
		//七点半方向，左下方向
		case 5:
			if(hang + charNum >= charMatrix.length || lie - charNum < 0)
				throw new IndexOutOfBoundsException("单词提取过程中，向左下超出边界！");
			else {
				for(int i = 0;i <= charNum;++i) {
					result += charMatrix[hang + i][lie - i];
				}
			}
			break;
		//九点方向，左水平方向
		case 6:
			if(lie - charNum < 0)
				throw new IndexOutOfBoundsException("单词提取过程中，向左超出边界！");
			else {
				for(int i = 0;i <= charNum;++i) {
					result += charMatrix[hang][lie - i];
				}
			}
			break;
		//十点半方向，左上方向
		case 7:
			if(lie - charNum < 0 || hang - charNum < 0)
				throw new IndexOutOfBoundsException("单词提取过程中，向左超出边界！");
			else {
				for(int i = 0;i <= charNum;++i) {
					result += charMatrix[hang - i][lie - i];
				}
			}
			break;
		default:
			throw new IllegalArgumentException("方向标志树必须为0-7的整数");
		}
		return result;
	}
}
