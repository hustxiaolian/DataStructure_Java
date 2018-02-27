package chapterFive;

public class SeparateChainingHashTableTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Employee e1 = new Employee();
		
	}

}

class Employee{
	
	private static int id = 0;
	
	private String name;
	private double salary;
	private int seniority;
	
	public Employee() {
		this("Employ#" + (++id), 800.0 , 0);
	}
	
	public Employee(String name,double salary,int seniority) {
		this.name = name;
		this.salary = salary;
		this.seniority = seniority;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public int getSeniority() {
		return seniority;
	}

	public void setSeniority(int seniority) {
		this.seniority = seniority;
	}

	public boolean equals(Object rhs) {
		return rhs instanceof Employee && name.equals(((Employee)rhs).name);
	}
	
	public int hashCode() {
		return name.hashCode();
	}
}