package chapterEight;

/**
 * �ڰ��� 8.3�ڲ��ཻ����Ļ������ݽṹ
 * @author 25040
 *
 */
public class DisjSets {
	
	/**
	 * ������õ�һ�ַ�����ʹ��һ�����鱣��ÿ��Ԫ�صĵȼ��������
	 */
	private int[] s;
	
	/**
	 * ���ཻ����Ĺ��캯������Ҫ����������ڴ���䣬�Լ������еĵȼ����ʼ��Ϊfalse����-1��
	 * @param numElemnets
	 */
	public DisjSets(int numElemnets) {
		this.s = new int[numElemnets];
		for(int i = 0;i < s.length;++i) {
			s[i] = -1;
		}
	}
	
	/**
	 * �ϲ�����,�������ȼ���ϲ�Ϊһ���ȼ��࣬������ľ������Ϊ����root1 ��Ԫ�� ��Ϊָ��root2���Դ���ʾ����֮��Ĺ�ϵ
	 * �����unionʵ�������������ĸ��������ͻ�ȽϺá�
	 * {@code s[root1] = root2}�������root2���������Ϊ��root1�����������������ȼ���ϲ�Ϊ��һ���ȼ��ࡣ
	 * 
	 * ��Ȼ������˵�ⲻ����õķ���������֪���ˣ�ʹ�������󲢷���������С�͸߶��󲢣����������Ĺ������
	 * @param root1
	 * @param root2
	 */
	public void union(int root1, int root2) {
		this.s[root1] = root2; 
	}
	
	/**
	 * find�������̡�����-1��ʾ���ĸ���Ҳ����˵��������ȼ���ľ�������Ϊ�����±꣬find������Ϊ��Ѱ�ҵȼ�������֣���ˣ�����Ҫ
	 * Ѱ��s[x] == -1���±꣬���s[x] != -1 ���ýڵ㲻Ϊ������Ҫ�����ݹ�find��
	 * ��ס�����ǵ����ⲻҪ��find���������ض������֣���ֻ��Ҫ���ҽ�������Ԫ��������ͬ����ʱ������������Ԫ���ϵ�find������ͬ�����֡�
	 * @param x
	 * @return
	 */
	public int find(int x) {
		if(s[x] < 0) {
			return x;
		}
		else {
			return find(s[x]);
		}
	}
	
	/**
	 * ע��union������ǰ��ʱ��root1��root2��û�й�ϵ��add������Ҫfind��ǰ���
	 * �������㷨�����Է�Ϊ�������Ĵ�С���Լ��������ĸ߶��󲢡��Դ�������ȼ������������ú���⣬��ȷǳ��
	 * �ڰ������Ĵ�С�����󲢷����У���������ʹ�����������洢�ȼ��࣬������������Ԫ�����洢��Ӧ�ȼ�������С�ĸ�ֵ.
	 * ���ڰ������ĸ߶��������󲢷����У�ͬ��������ʹ������Ԫ�����洢��Ӧ�ȼ�����������Ϣ������洢Ϊ�߶ȵĸ�ֵ��-1����Ϊ����Ϊ0�����Ǹ���
	 */
	public void unionByTreeHeight(int root1, int root2) {
		if(s[root2] < s[root1]) {
			//root2���������ߣ������root1��Ϊroot2�����������Ҹ��¸߶���Ϣ
			s[root1] = root2;
		}
		else {
			//root1�����������,��root2��Ϊroot1�����������������Ǹߣ�����Ҫ���¸߶���Ϣ
			if(s[root1] == s[root2]) {
				s[root2]--;//�������ߵĴ�����ʽ�Ǹ�ֵ�������--������++
			}
			s[root2] = root1;
		}
	}
	
	/**
	 * �������Ĵ�С�����󲢷����У���������ʹ�����������洢�ȼ��࣬������������Ԫ�����洢��Ӧ�ȼ�������С�ĸ�ֵ.
	 * @param root1 
	 * @param root2
	 */
	public void unionByTreeSize(int root1, int root2) {
		if(s[root1] < s[root2]) {
			//�����ʾroot1������root2������������Ӧ����root2��Ϊroot1�Ķ���
			//��������׶��Ļ�����root2��daddy is root1;
			s[root2] = root1;
			//��������С����Ϣ
			s[root1]--;
		}
		else {
			//ͬ��
			s[root1] = root2;
			s[root2]--;
		}
	}
	
	/**
	 * ��x��y���߽�����ϵ����ζ���������ϵĺϲ�
	 * @param x
	 * @param y
	 */
	public void add(int x,int y) {
		//�ж������Ƿ��Ѿ������˹�ϵ
		if(findInPathCompresion(x) != findInPathCompresion(y)) {
			unionByTreeSize(findInPathCompresion(x), findInPathCompresion(y));
		}
	}
	
	/**
	 * ·��ѹ���㷨�����ȣ�����Ϊʲôʹ��·��ѹ���㷨�����ǻ��������Ĺ۲죺
	 * ִ�кϲ��������κ��㷨����������ͬ������ε�������Ϊ����Ȼ�����������֮���ƽ�⡣
	 * ·��ѹ����findInPathCompresion���������ڼ���ж�������ִ��union�ķ����޹ء�
	 * ·��ѹ��������Ч������x������·���ϵ�ÿһ���ڵ㶼ʹ�丸�ڵ��Ϊ�����ĸ���
	 * ������ץסһ�㣺s[x] < 0���Ǳ�ʾ���ڵ㣬ͬʱ�������ߣ��������ָ���丸�ڵ㣬
	 * ���൱��һ��ָ�򵹹����Ķ������ֻ�ܴӶ��ӵ����ף������Ǵ�ͳ�ĸ��׵����ӡ�
	 */
	public int findInPathCompresion(int x) {
		if(s[x] < 0) {
			return x;
		}
		else {
			return s[x] = findInPathCompresion(s[x]);
		}
	}
	
	/**
	 * ��������۲�
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for(int i = 0;i < this.s.length; ++i) {
			sb.append("\t").append(i);
		}
		sb.append("\n");
		
		for(int i = 0;i < this.s.length; ++i) {
			sb.append("\t").append(s[i]);
		}
		sb.append("\n");
		
		return sb.toString();
	}
}
