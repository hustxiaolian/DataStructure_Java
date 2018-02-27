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
	 * �����ճ���
	 * ���ȣ������������п��ܵ��ʵ�ɢ�б������Ե����ַ���ɢ�У�Ȼ��Ȼ����������ĸɢ�е����յĵ���
	 * Ȼ�󣬱�����ά�����е�ÿ���ַ���Ȼ��ͬ����8�����򣩣�ÿ�ֿ��ܵ��ַ������μ���Ƿ���HashTable��ǰ
	 * @param charMatrix �����ά�ַ����飬����Ϊ�˷����Լ������������ѡ���άString����
	 */
	public static void solution1(String[][] charMatrix) {
		
		final int charMinCharNum = 2;
		
		String[] correctWord = {"this","two","fat","that"};
		SeparateChainingHashTable<String> hash = CrossWordPuzzle.builtHashTable(correctWord);
		//ÿ���ַ����������б���
		for(int hang = 0;hang < charMatrix.length;++hang) {
			for(int lie = 0;lie < charMatrix[hang].length;++lie) {
				//Ȼ������˸�����
				for(int direction = 0;direction < 8;++direction) {
					int charNum = charMinCharNum;
					switch(direction) {
					//��ֱ����
					case 0:
						while(hang - charNum >= 0) {
							CrossWordPuzzle.checkInHashTableAndOutput(hash, hang, lie, direction, charNum, charMatrix);
							++charNum;
						}
						break;
					//���Ϸ���
					case 1:
						while(hang - charNum >= 0 && lie + charNum < charMatrix[hang].length) {
							CrossWordPuzzle.checkInHashTableAndOutput(hash, hang, lie, direction, charNum, charMatrix);
							++charNum;
						}
						break;
					//��ˮƽ
					case 2:
						while(lie + charNum < charMatrix[hang].length) {
							CrossWordPuzzle.checkInHashTableAndOutput(hash, hang, lie, direction, charNum, charMatrix);
							++charNum;
						}
						break;
					//����
					case 3:
						while(hang + charNum < charMatrix.length && lie + charNum < charMatrix[hang].length) {
							CrossWordPuzzle.checkInHashTableAndOutput(hash, hang, lie, direction, charNum, charMatrix);
							++charNum;
						}
						break;
					//��ֱ����
					case 4:
						while(hang + charNum < charMatrix.length) {
							CrossWordPuzzle.checkInHashTableAndOutput(hash, hang, lie, direction, charNum, charMatrix);
							++charNum;
						}
						break;
					//����
					case 5:
						while(hang + charNum < charMatrix.length && lie - charNum >= 0) {
							CrossWordPuzzle.checkInHashTableAndOutput(hash, hang, lie, direction, charNum, charMatrix);
							++charNum;
						}
						break;
					//��ˮƽ����
					case 6:
						while(lie - charNum >= 0) {
							CrossWordPuzzle.checkInHashTableAndOutput(hash, hang, lie, direction, charNum, charMatrix);
							++charNum;
						}
						break;
					//���Ϸ���
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
	 * ����{@code inHashTable}��{@code getWordFromCharMatrix}��̬����������Ԫ��ó��ĵ����Ƿ���HashTable�У�
	 * ����ڣ����������Ϣ�����򷵻�null
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
			System.out.println("����ĸ������:(" + (hang+1) + "," + (lie + 1) + ") ����Ϊ��" + direction + " �ַ���Ϊ��" + (1 + charNum)
					+"�õ���Ϊ��" + CrossWordPuzzle.getWordFromCharMatrix(hang, lie, direction, charNum, charMatrix));
	}
	
	
	/**
	 * ���뵥��/���飬ʹ�÷������ӷ�������HashTable,���ҷ��������á�
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
	 * ���뵥�ʣ�������HashTable��ѯ�����ز���ֵ��
	 * @param hashtable
	 * @param matrixWord
	 * @return ����ڣ��򷵻�true�����򷵻�false
	 */
	private static boolean inHashTable(SeparateChainingHashTable<String> hashtable,String matrixWord) {
		return hashtable.contain(matrixWord);
	}
	
	/**
	 * ������Ԫ�����Ϣ����ǰ�ַ���������������������ַ������Ӿ�������ȡ��String���ͣ����ҷ���
	 * @param hang ��ǰ�ַ�������
	 * @param lie	��ǰ�ַ�������
	 * @param direction	��ȡ���ʵķ���12�㷽��Ϊ0��˳ʱ�����ƣ����ư˸�����
	 * @param charNum	��ȡ���ʵ��ַ���
	 * @param charMatrix	�ַ���ά����
	 * @return	String�ַ�
	 * @throws java.lang.IndexOutOfBoundsException ���ܴ����˴���ķ�������ַ���ʹ�ã������α�Խ��
	 */
	private static String getWordFromCharMatrix(int hang, int lie,int direction,
			int charNum,String[][] charMatrix) {
		String result = "";
		
		switch(direction) {
		//12�㷽����ֱ���Ϸ���,ͬʱ�������ж��Ƿ����ϳ������ޣ�����ֱ���׳��쳣
		case 0: 
			if(hang - charNum < 0)
				throw new IndexOutOfBoundsException("������ȡ�����У����ϳ����߽磡");
			else {
				for(int i = 0;i <= charNum;++i) {
					result += charMatrix[hang - i][lie];
				}
			}
			break;
		//1��룬���Ϸ���
		case 1:
			if(hang - charNum < 0 || lie + charNum >= charMatrix.length)
				throw new IndexOutOfBoundsException("������ȡ�����У������ϳ����߽磡");
			else {
				for(int i = 0;i <= charNum;++i) {
					result += charMatrix[hang - i][lie + i];
				}
			}
			break;
		//���㷽�� ����ˮƽ����
		case 2:
			if(lie + charNum >= charMatrix.length)
				throw new IndexOutOfBoundsException("������ȡ�����У����ҳ����߽磡");
			else {
				for(int i = 0;i <= charNum;++i) {
					result += charMatrix[hang][lie + i];
				}
			}
			break;
		//�ĵ�룬���·���
		case 3:
			if(hang + charNum >= charMatrix.length || lie + charNum >= charMatrix.length)
				throw new IndexOutOfBoundsException("������ȡ�����У������³����߽磡");
			else {
				for(int i = 0;i <= charNum;++i) {
					result += charMatrix[hang + i][lie + i];
				}
			}
			break;
			
		//���㷽����ֱ����
		case 4:
			if(hang + charNum >= charMatrix.length)
				throw new IndexOutOfBoundsException("������ȡ�����У����³����߽磡");
			else {
				for(int i = 0;i <= charNum;++i) {
					result += charMatrix[hang + i][lie];
				}
			}
			break;
		//�ߵ�뷽�����·���
		case 5:
			if(hang + charNum >= charMatrix.length || lie - charNum < 0)
				throw new IndexOutOfBoundsException("������ȡ�����У������³����߽磡");
			else {
				for(int i = 0;i <= charNum;++i) {
					result += charMatrix[hang + i][lie - i];
				}
			}
			break;
		//�ŵ㷽����ˮƽ����
		case 6:
			if(lie - charNum < 0)
				throw new IndexOutOfBoundsException("������ȡ�����У����󳬳��߽磡");
			else {
				for(int i = 0;i <= charNum;++i) {
					result += charMatrix[hang][lie - i];
				}
			}
			break;
		//ʮ��뷽�����Ϸ���
		case 7:
			if(lie - charNum < 0 || hang - charNum < 0)
				throw new IndexOutOfBoundsException("������ȡ�����У����󳬳��߽磡");
			else {
				for(int i = 0;i <= charNum;++i) {
					result += charMatrix[hang - i][lie - i];
				}
			}
			break;
		default:
			throw new IllegalArgumentException("�����־������Ϊ0-7������");
		}
		return result;
	}
}
