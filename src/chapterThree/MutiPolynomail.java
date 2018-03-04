package chapterThree;

/**
 * ģ����������ʽ��Ƶ����㡣
 * @author 25040
 *
 */
public class MutiPolynomail {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PolyNode[] arr1 = {new PolyNode(1.0,2),new PolyNode(2.0,4),new PolyNode(3.0,5)};
		PolyNode[] arr2 = {new PolyNode(8.0,2),new PolyNode(9.0,3),new PolyNode(-1.0,5),new PolyNode(8.0,7)};
		MySimpleLinkedList<PolyNode> poly1 = new MySimpleLinkedList<>(arr1);
		MySimpleLinkedList<PolyNode> poly2 = new MySimpleLinkedList<>(arr2);
		
		System.out.println(poly1);
		System.out.println(poly2);
		
		System.out.println(polyMuti(poly1,poly2));
	}
	
	public static MySimpleLinkedList<PolyNode> polyMuti(MySimpleLinkedList<PolyNode> p1,MySimpleLinkedList<PolyNode> p2){
		MySimpleLinkedList<PolyNode>  result = new MySimpleLinkedList<PolyNode>();
		
		for (PolyNode pos1 : p1) {
			for(PolyNode pos2 : p2) {
				PolyNode nodeMutResult = pos1.muti(pos2);
				if(nodeMutResult.getCoeff() != 0) {//���ϵ��Ϊ0�����ü���
					result.addIfNotContain(nodeMutResult);
				}
			}
		}
		
		return result;
	}
	
	
}

/**
 * ����ʽ�ڵ��࣬ʹ�ýڵ��ౣ�����ÿ���ϵ����ָ��
 * @author 25040
 *
 */
class PolyNode {
	/**
	 * ÿ���ڵ��ϵ����ָ������
	 */
	private double coeff;
	private int exp;
	
	/*
	 * �ڵ�����вι���
	 */
	public PolyNode(double coeff,int exp) {
		this.coeff = coeff;
		this.exp = exp;
	}
	
	/*
	 * ���Ե�get��set����
	 */
	public double getCoeff() {
		return coeff;
	}
	public void setCoeff(double coeff) {
		this.coeff = coeff;
	}
	public int getExp() {
		return exp;
	}
	public void setExp(int exp) {
		this.exp = exp;
	}
	
	/**
	 * ������ӵ����㡣������ѧ�������ֻ������ָ����ͬ��ʱ�żӣ��ϲ���
	 * 
	 * @param other
	 * @return
	 */
	public PolyNode add(PolyNode other) {
		if(this.getExp() != other.getExp()) {
			throw new IllegalArgumentException("ָ����ͬ������������");
		}
		return new PolyNode(this.coeff+other.coeff,this.exp);
	}
	
	/**
	 * ��������ڵ�ĳ˷����㡣ϵ����ˣ�ָ����ӡ�
	 * @param other
	 * @return
	 */
	public PolyNode muti(PolyNode other) {
		return new PolyNode(this.getCoeff() * other.getCoeff(),this.getExp() + other.getExp());
	}
	
	/**
	 * hashcode��equal������ֻʹ����ָ����Ϊɢ�м��������жϵ�������
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + exp;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PolyNode other = (PolyNode) obj;
		if (exp != other.exp)
			return false;
		return true;
	}

	public String toString() {
		return ("\t" + (coeff > 0? "+":"" ) + coeff + "x(" + exp + ")");
	}
	
}