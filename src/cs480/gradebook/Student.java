package cs480.gradebook;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Student implements Serializable, Comparable<Student>{
	private String broncoID;
	private String lastName;
	private String middleName;
	private String firstName;
	public Student(String broncoID,String lastName,String firstName, String middleName){
		this.broncoID = broncoID;
		this.lastName = lastName;
		this.firstName = firstName;
		this.middleName = middleName;
	}
	public String getBroncoID(){
		return this.broncoID;
	}
	public void setBroncoID(String bid){
		this.broncoID = bid;
	}
	public String getLastName(){
		return this.lastName;
	}
	public void setLastName(String lastName){
		this.lastName = lastName;
	}
	public String getFirstName(){
		return this.firstName;
	}
	public void setFirstName(String firstName){
		this.firstName = firstName;
	}
	public String getMiddleName(){
		return this.middleName;
	}
	public void setMiddleName(String middleName){
		this.middleName = middleName;
	}
	
	@Override
	public int compareTo(Student s) {
		// TODO Auto-generated method stub
		String thisStudent = this.getLastName();
		String otherStudent = s.getLastName();
		return thisStudent.compareToIgnoreCase(otherStudent);
//		return ;
	}
}
