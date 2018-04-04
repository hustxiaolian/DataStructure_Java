package chapterEight;

import java.util.HashMap;

/**
 * ���͵Ĳ��ཻ���ࡣ
 * 1.����������ϰ�²��ཻ�����֪ʶ��
 * 2.�ں��������С���������������ʱ�����Ը����ⲿ�ִ���
 * 
 * @version <1> 
 * ����Щ�뷨:
 * 1. ʹ��hashMap<T, Integer> + int��������ʵ���ཻ���ࡣ
 * 2. ֱ��ʹ��hashMap<T, T>����֯��
 * 3. ʹ��T[]+int[] ����֯.ʹ�������±�������������������֮�����ϵ��
 *    �ô��ǣ��ռ��ʱ�临�Ӷȶ��ܿ��ƵñȽϺã����ǻ�����֧�ֶ�̬�ò����ɾ����
 *    
 * ������ʵ��ʹ�ò��ཻ�����ʱ����������֪��ȫ��Ԫ�أ���̬�����Ƶ�ʵ͵����ˡ�
 * ��ѡ�����õ������뷨��ͬʱ��Ҳ����review�¹�ȥ�ô���
 * 
 * �Ľ����뷨������ʹ��һ��HashMap<T,Integer>���ֲ�T[]�������õ�֧��T->int��ӳ������ÿ�ζ�Ҫ����Ѱ�ҡ�
 * ����������Ŀ�Ƚϴ��������ʱ��͸���Һܵ��ۣ���������ֱ���ÿռ任ʱ�䡣
 * 
 * 
 * 
 * @author 25040
 *
 * @param <T>
 */
public class DisjSets<T> {
	
	private T[] indexToObj;
	private int[] s;
	private HashMap<T, Integer> objToindex;

	/**
	 * ���Ͳ��ཻ����Ĺ��캯����
	 * �乤��ְ�ܼܺ򵥾��Ƿ����ڴ�ռ䣬Ȼ���ʼ����������ı�����
	 * ��s�����У�ʹ��-1����ʾ��Ԫ��Ϊ������>0�����������ȼ۹�ϵ����<-1����������ﵱǰ�ȼ����Ĵ�С
	 * ��indexToObj�����У��ڹ���׶ξͽ����������±��Ԫ��֮��Ĺ�ϵ��
	 * 
	 * @param size 
	 */
	@SuppressWarnings("unchecked")
	public DisjSets(int size, T[] allElements) {
		//�ֲ��ڴ�ռ�
		this.indexToObj = (T[]) new Object[size];
		this.s = new int[size];
		this.objToindex = new HashMap<>();
		
		//��ʼ��������
		//���������������ϵͳ�ĺ���֢���뵽�˱���д���ֲ������õĴ��롣��֪�����c��java�ǲ���һ���ģ�
		for(int i = 0; i < size;++i) {
			//�Ӽ���Ϲ�����ڴ����õĽǶ���˵��Ӧ��������һ����ʱ�������洢allElements[i]
			//Ҳ������������ϰ�߰ɣ��������ٲ���Ҫ���ڴ����úͺ�������
			T temp = allElements[i];
			this.indexToObj[i] = temp;
			this.objToindex.put(temp, i);
		}
		
		for(int i = 0;i < size;++i) {
			this.s[i] = -1;
		}
		
	}
	
	/**
	 * union�������ϡ�
	 * 
	 * ˼·�Ͳ��趼�ǳ��򵥣�
	 * 1. ��map�л�ȡԪ�ض�Ӧ�ı��
	 * 2. Ȼ��ִ�а�������С�������㷨��ע���Ǹ����Ĵ�С�ж����⡣
	 * 
	 * @param ele1
	 * @param ele2
	 */
	public void union(T ele1, T ele2) {
		int num1 = this.objToindex.get(ele1);
		int num2 = this.objToindex.get(ele2);
		
		//���Ԫ��1�������󣬼�1<2,��������Ԫ��2�ļ޽ӵ�Ԫ��1������
		if(s[num1] < s[num2]) {
			//Ԫ��1��������
			s[num2] = num1;
			s[num1]--;
		}
		else {
			//Ԫ��2��������
			s[num1] = num2;
			s[num2]--;
		}
	}
	
	
	/**
	 * 
	 * ��find�������̵ĺ���˼��������ǲ�Ҫ�ں������ص����֣�����Ҫ�������ȼ۵�Ԫ��Ӧ�÷���ͬ�������֡�
	 * 
	 * 
	 * @param ele1
	 * @return �õȼ����ĸ��ڵ�
	 */
	public int find(T ele) {
		int num1 = this.objToindex.get(ele);
		return findInPathCompresion(num1);
	}
	
	/**
	 * ˽�еĵݹ�find���̡�
	 * ��·��ѹ���ĵݹ����̡�
	 * @param num1
	 * @return
	 */
	@SuppressWarnings("unused")
	private int find(int num1) {
		if(s[num1] < 0) {
			return num1;
		}
		else {
			return find(s[num1]);
		}
	} 
	
	/**
	 * ·��ѹ���㷨�������˼��������find���̲�ࡣ
	 * ���Ǹ��㷨����һ����
	 * ��ϸ˼���£��ݹ�һ����൱�ڽ�s[num1]=s[num2]=s[num2]= root
	 * ���������ݹ�Ĺ����У���·�������еĽڵ�ĸ������ö�ֱ��ָ���˸��ڵ㡣
	 * �ؼ��ǣ����밴����С�������㷨�������ݡ�
	 * @return
	 */
	private int findInPathCompresion(int num1) {
		if(s[num1] < 0) {
			return num1;
		}
		else {
			//�ؼ�����ɧ��һ��
			return s[num1] = findInPathCompresion(s[num1]);
		}
	}
	
	/**
	 * �ж�����Ԫ���Ƿ�ȼۡ�Ҳ���������Ƿ���һ�������С�
	 * @param ele1 
	 * @param ele2
	 */
	public boolean inSameSet(T ele1, T ele2) {
		int num1 = objToindex.get(ele1);
		int num2 = objToindex.get(ele2);
		
		return findInPathCompresion(num1) == findInPathCompresion(num2);
	}
}
