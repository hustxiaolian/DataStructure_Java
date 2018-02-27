package chapterThree;

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


class PolyNode {
	private double coeff;
	private int exp;
	
	public PolyNode(double coeff,int exp) {
		this.coeff = coeff;
		this.exp = exp;
	}
	
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
	
	public PolyNode add(PolyNode other) {
		if(this.getExp() != other.getExp()) {
			throw new IllegalArgumentException("指数相同的两项才能相加");
		}
		return new PolyNode(this.coeff+other.coeff,this.exp);
	}
	
	public PolyNode muti(PolyNode other) {
		return new PolyNode(this.getCoeff() * other.getCoeff(),this.getExp() + other.getExp());
	}
	
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