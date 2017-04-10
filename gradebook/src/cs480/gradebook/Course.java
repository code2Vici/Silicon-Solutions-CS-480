package cs480.gradebook;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

@SuppressWarnings("serial")
public class Course implements Serializable{
		private String courseName;
		private String term;
		LinkedList<Student> students = new LinkedList<Student>();
		LinkedList<Assignment> assignments = new LinkedList<Assignment>();
		LinkedList<Category> categories = new LinkedList<Category>();
		
		public Course(String courseName, String term){
			this.courseName = courseName;
			this.term = term;
			
		}
		
		
		public Course(){
			
		}

		public Category getCategoryByName(String categoryName){
			for(Category c : categories){
				if(c.getCategoryName().equals(categoryName)){
					return c;
				}
			}
			return null;
		}
		public String getCourseName(){
			return this.courseName;
		}
		public void categoryGUI(){
			
		}
		public void addGrade(String broncoID, String assignmentName, double score){
			Assignment assignment = getAssignmentByName(assignmentName);
			for(Assignment a: assignments){
				if(a == assignment){
//					System.out.println("Test");
					assignment.addGrade(broncoID, score);
				}
			}
		}
		public Assignment getAssignmentByName(String assignmentName){
			for(Assignment a: assignments){
//				System.out.println(a.getAssignmentName());
//				System.out.println(assignmentName);
				if (a.getAssignmentName().equals(assignmentName)){
//					System.out.println("test");
					return a;
				}
			}
			return null;
		}
		public Student getStudentByID(String broncoID){
			for(Student s: students){
				if(s.getBroncoID().equals(broncoID)){
					return s;
				}
			}
			return null;
		}
		public void addStudent(String broncoID,String lastName, String firstName, String middleName){
			if(students.contains(getStudentByID(broncoID))){
				System.out.println("Student With This ID Already Exists, try again");
			}
			else{
				students.add(new Student(broncoID, lastName, firstName, middleName));
			}
		}
		public void addCategory(String categoryName, double weight){
			if(categoryAlreadyExists(categoryName)){
				System.out.println("Category Already Exists");
			}
			else{
				categories.add(new Category(categoryName,weight));
			}
			}
             /*   public Category getCategory(){
                    for(Category c : categories){
				if(c.getCategoryName().equals(categoryName)){
					return c;
				}
			}
                    return null;
                }*/
                
		public void removeStudent(String broncoID){
			//remove grades stored for student
			for(Assignment a: assignments){
				a.removeGradeForStudent(broncoID);
			}
			//remove student from list of students
			for(Student s: students){
				if(s.getBroncoID().equals(broncoID)){
					students.remove(s);
				}
			}
			
		}
		public void removeAssignment(String assignmentName){
			for(Assignment a: assignments){
				if(a.getAssignmentName().equals(assignmentName)){
					assignments.remove(a);
				}
			}
		}
		public void modifyCategory(String categoryName, String newCategoryName, double newWeight){
			if(categoryAlreadyExists(newCategoryName)){
				System.out.println("Category Already exists");
				return;
			}
			for(Category c: categories){
				if(c.getCategoryName().equals(categoryName)){
					c.setCategoryName(newCategoryName);
					c.setWeight(newWeight);
				}
			}
		}
		public void removeCategory(String categoryName){
			for(Category c: categories){
				if(c.getCategoryName().equals(categoryName)){
					categories.remove(c);
				}
			}
				
			
		}
		public boolean categoryAlreadyExists(String categoryName){
			for(Category c: categories){
				if(c.getCategoryName().equals(categoryName)){
					return true;
				}
			}
			
			return false;
		}
		public boolean catagoriesAddToOne(){
			double count =0;
			for(Category c: categories){
				count +=c.getWeight();
			}
			if(roundDouble(count) == 100){
				return true;
			}
			else{
				return false;
			}
		}
		public void addAssignment(String categoryName, String assignmentName, double totalPoints){
			Category category = getCategoryByName(categoryName);
			if(categories.contains((category))){
				assignments.add(new Assignment(category,assignmentName,totalPoints));
			}
			else{
				System.out.println("Please Create a category with the name " + category);
			}
				
		}
		
		public boolean containsCategory(String categoryName){
			for(Category c : categories){
				if(c.getCategoryName().equals(categoryName)){
					return true;
				}
			}
			return false;
		}
		public boolean containsBroncoID(String broncoID){
			for(Assignment a: assignments){
				if(a.containsBroncoID(broncoID)){
					
				}
			}
			return false;
		}
		public String toString(){
			String returnVal ="";
			returnVal += "BroncoID,LastName,FirstName,MiddleName";
			Collections.sort(assignments);
			for(Assignment a: assignments){
				returnVal +="," + a.getAssignmentName() + "("+a.getTotalPoints()+")";
			}
			returnVal +=",FinalGrade";
			returnVal +="\n";
			Collections.sort(students);
			for(Student s : students){
				// bid first last middle
				returnVal += s.getBroncoID() + "," + s.getLastName() + "," + s.getFirstName() + "," + s.getMiddleName();
				// add scores for all assignments
				for(Assignment a : assignments){
					if(a.containsBroncoID(s.getBroncoID())){
						returnVal += "," + a.getGrade(s.getBroncoID());
					}
					else{
						returnVal += "," + "0";
					}
					
				}
				
				returnVal += "," + roundDouble(calculateGrade(s));
//				returnVal += "," + calculateGrade(s);
				returnVal +="\n";
			}
			return returnVal;
		}
                
               
                
		public double calculateGrade(String bid){
			Student s = getStudentByID(bid);
			return calculateGrade(s);
			
		}
		public double calculateGrade(Student s){
			
			double[] gradePerCategory = new double[categories.size()];
			
			for(int i=0;i<gradePerCategory.length; i++){
				gradePerCategory[i] =0;
			}
			int j=0;
			//this will be increase if there are no assignments in a category
			//this is to stop a category with 0 points from counting against a student in final grade calc
			double weightOffset =1;
			//each index of array is the total score for a different caragory
			for(Category c: categories){
//				System.out.println(c.getCategoryName());
//				System.out.println(c.getWeight());
//				System.out.println(c.getCategoryName());
				double numerator=0;
				double denominator=0;
				for(Assignment a: assignments){
					if(a.getCategory() == c){
						
						numerator += a.getGrade(s.getBroncoID());
						denominator += a.getTotalPoints();

					}
				}
				if(denominator !=0){
					gradePerCategory[j] = (numerator/denominator)*c.getWeight();
				}
				else{
					gradePerCategory[j] =0;
					weightOffset -= c.getWeight();
//					System.out.println(weightOffset + "ss");
				}
				j++;
			}
			//add all grades together
			double finalGrade = 0;
			for(int i=0; i<gradePerCategory.length; i++){
//				System.out.println(gradePerCategory[i]);
				finalGrade +=gradePerCategory[i];
			}
//			System.out.println(weightOffset + "wo");
			return finalGrade/weightOffset;
		}
		public String getTerm(){
			return this.term;
		}
		public void setTerm(String term){
			this.term = term;
		}
		public double roundDouble(double d){
			d=d*100;
			int roundedDown = (int)d;
			int roundedUp = (int) Math.ceil(d);
//			System.out.println(roundedUp);
			if(d-.01 <= roundedDown){
				return roundedDown;
			}
			if(d+0.01 >= roundedUp){
				return roundedUp;
			}
			return d;
		}
		public Course openCourse() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException{
		try{	
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    
		} catch (Exception e){
                    
                }	
                    JFrame frame = new JFrame();
			frame.setLayout(new BorderLayout());
			JFileChooser openFile = new JFileChooser();
	        openFile.showOpenDialog(null);
	        File myFile = openFile.getSelectedFile();
			Course returnVal =null;
			try {
		         FileInputStream fileIn = new FileInputStream(myFile);
		         ObjectInputStream in = new ObjectInputStream(fileIn);
		         returnVal = (Course) in.readObject();
                         System.out.println(returnVal);
		         in.close();
		         fileIn.close();
		      }catch(IOException i) {
		         i.printStackTrace();
		      }catch(ClassNotFoundException c) {
		         System.out.println("Employee class not found");
		         c.printStackTrace();
		      }
			return returnVal;
                
		}
		
		public void saveCourse() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException{
                    try{	
                        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                     
                    } catch (Exception e){
                    
                    }
			JFrame frame = new JFrame();
			frame.setLayout(new BorderLayout());
		    JFileChooser saveFile = new JFileChooser();
	        saveFile.showSaveDialog(null);
	        File myFile = saveFile.getSelectedFile();
			try {
		         FileOutputStream fileOut = new FileOutputStream(myFile+".course");
		         ObjectOutputStream out = new ObjectOutputStream(fileOut);
		         out.writeObject(this);
		         out.close();
		         fileOut.close();
		         System.out.println("Serialized data is saved in: "+myFile);
		      }catch(IOException i) {
		         i.printStackTrace();
		      }
		}
		public void exportToCSV() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException{
			try{	
                            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                        } catch (Exception e){
                    
                        }
			JFrame frame = new JFrame();
			frame.setLayout(new BorderLayout());
		    JFileChooser saveFile = new JFileChooser();
	        saveFile.showSaveDialog(null);
	        File myFile = saveFile.getSelectedFile();
			try {
				PrintWriter out = new PrintWriter(myFile+".csv");
				System.out.print(this.toString());
				out.print(this.toString());
		         out.close();
		         System.out.println("csv file is saved in: "+myFile+".csv");
		      }catch(IOException i) {
		         i.printStackTrace();
		      }
			
		}

}
