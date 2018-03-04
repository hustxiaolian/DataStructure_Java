package chapterThree;

/**
 * 模拟两个多项式相称的运算。
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
				if(nodeMutResult.getCoeff() != 0) {//如果系数为0，不用加上
					result.addIfNotContain(nodeMutResult);
				}
			}
		}
		
		return result;
	}
	
	
}

/**
 * 多项式节点类，使用节点类保存多项每项的系数和指数
 * @author 25040
 *
 */
class PolyNode {
	/**
	 * 每个节点的系数和指数属性
	 */
	private double coeff;
	private int exp;
	
	/*
	 * 节点类的有参构造
	 */
	public PolyNode(double coeff,int exp) {
		this.coeff = coeff;
		this.exp = exp;
	}
	
	/*
	 * 属性的get和set方法
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
	 * 两项相加的运算。按照数学运算规则，只有两项指数相同的时才加（合并）
	 * 
	 * @param other
	 * @return
	 */
	public PolyNode add(PolyNode other) {
		if(this.getExp() != other.getExp()) {
			throw new IllegalArgumentException("指数相同的两项才能相加");
		}
		return new PolyNode(this.coeff+other.coeff,this.exp);
	}
	
	/**
	 * 任意两项节点的乘法运算。系数相乘，指数相加。
	 * @param other
	 * @return
	 */
	public PolyNode muti(PolyNode other) {
		return new PolyNode(this.getCoeff() * other.getCoeff(),this.getExp() + other.getExp());
	}
	
	/**
	 * hashcode和equal，这里只使用了指数作为散列计算和相等判断的条件。
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