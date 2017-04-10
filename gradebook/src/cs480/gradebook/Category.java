package cs480.gradebook;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Category implements Serializable{
	private String categoryName;
	private double weight;
	
	public Category(String categoryName, double weight){
		this.categoryName = categoryName;
		this.weight = weight;
	}
	
	public String getCategoryName(){
		return this.categoryName;
	}
	public void setCategoryName(String name){
		this.categoryName = name;
	}
	public double getWeight(){
		return this.weight;
	}
	public void setWeight(double weight){
		this.weight = weight;
	}
	
}
