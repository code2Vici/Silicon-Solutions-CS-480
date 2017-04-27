package cs480.gradebook;

import java.io.Serializable;
import java.util.HashMap;

@SuppressWarnings("serial")
public class Assignment implements Serializable,Comparable<Assignment>{
	private Category category;
	private String assignmentName;
	private double totalPoints;
	private HashMap<String, Double> bidXScore = new HashMap<String, Double>();
	
	public Assignment(Category category, String assignmentName, double totalPoints){
		this.category = category;
		this.assignmentName = assignmentName;
		this.totalPoints = totalPoints;
	}
	public void removeGradeForStudent(String broncoID){
		if(bidXScore.containsKey(broncoID)){
			bidXScore.remove(broncoID);
		}
		else{
			System.out.println("Student does not have a score for" + this.assignmentName);
		}
	}
	
	public Category getCategory(){
		return this.category;
	}
	public void setCategory(Category category){
		this.category = category;
	}
	public String getAssignmentName(){
		return this.assignmentName;
	}
	public void setAssignmentName(String assignmentName){
		this.assignmentName = assignmentName;
	}
	public double getTotalPoints(){
		return this.totalPoints;
	}
	public void settotalPoints(double totalPoints){
		this.totalPoints = totalPoints;
	}
	public void addGrade(String broncoID, double pointsEarned){
		bidXScore.put(broncoID, pointsEarned);
	}
	public double getGrade(String bid){
		if(bidXScore.containsKey(bid)){
			return bidXScore.get(bid);
		}
		else{
			return 0;
		}
	}
	public boolean containsBroncoID(String bid){
		if(bidXScore.containsKey(bid)){
			return true;
		}
		return false;
	}
	@Override
	public int compareTo(Assignment a) {
		// TODO Auto-generated method stub
		String thisCategory = this.category.getCategoryName();
		String otherCategory = a.getCategory().getCategoryName();
		return thisCategory.compareToIgnoreCase(otherCategory);
//		return ;
	}
	
	
}
