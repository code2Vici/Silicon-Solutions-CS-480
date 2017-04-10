package cs480.gradebook.Test;

import cs480.gradebook.Course;
import cs480.gradebook.Course;
import cs480.gradebook.Student;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.LinkedList;

public class Driver {
	public static void main(String[] args){
		Course myCourse= new Course("CS480", "fall 2016");
		myCourse.addCategory("Tests", .2);
		myCourse.addCategory("Homework", .2);
		//myCourse.addCatagory("Homework1", .2);
		//myCourse.addCatagory("Homework2", .4);
		myCourse.addStudent("1", "Kries", "Michael", "Taylor");
		myCourse.addStudent("2", "2Kries", "2Michael", "2Taylor");
		myCourse.addStudent("3", "3Kries", "3Michael", "3Taylor");
		myCourse.addStudent("4", "4Kries", "4Michael", "4Taylor");
		myCourse.addAssignment("Tests", "test1", 50);
		myCourse.addAssignment("Tests", "test2", 50);
		myCourse.addAssignment("Homework", "HW1", 50);
		myCourse.addGrade("1", "test1", 10);
		myCourse.addGrade("1", "test2", 10);
		myCourse.addGrade("2", "test1", 20);
		myCourse.addGrade("3", "test1", 100);
		myCourse.addGrade("3", "test2", 100);
                myCourse.addGrade("3", "HW1", 100);
         
                System.out.println("asdadasd" + myCourse.getCategoryByName("Tests"));
//		myCourse.addGrade("3", myCourse.getAssignmentByName("Tests1"), 50);
		System.out.println(myCourse);
		//Saving myCourse to a serializable text file named test.course
		//myCourse.modifyCategory("Tests", "Tests1", .4);
		//myCourse.modifyCatagory("Homework2", "hw2", .2);
		if(myCourse.catagoriesAddToOne()){
			System.out.println("yes");
		}
                
//		System.out.println(myCourse);
		saveCourse(myCourse,"test.course");
		//setting myCourse to null to demonstrate openCourse
                
           
		myCourse = null;
		System.out.println(myCourse);
		System.out.println();
		// now I call the openCourse method which opens the test.ser file and returns a Course object
		
		myCourse = openCourse("test.course");
		System.out.println(myCourse);
		
                System.out.println(myCourse.calculateGrade("3"));
	}
	public static void saveCourse(Course c, String filePath){
		try {
	         FileOutputStream fileOut = new FileOutputStream(filePath);
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(c);
	         out.close();
	         fileOut.close();
	         System.out.println("Serialized data is saved in: "+filePath);
	      }catch(IOException i) {
	         i.printStackTrace();
	      }
	}
	public static Course openCourse(String filePath){
		Course returnVal =null;
		try {
	         FileInputStream fileIn = new FileInputStream(filePath);
	         ObjectInputStream in = new ObjectInputStream(fileIn);
	         returnVal = (Course) in.readObject();
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
        public static void saveCourseTxt(Course c){
		try{
			File file = new File("course.txt");
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter writer = new PrintWriter(bw);
			bw.write(c.toString() + "\n");
			
			bw.close();
		} catch(Exception i){
			i.printStackTrace();
		}
	}
}
	
	
	

