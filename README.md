# Silicon Solutions, Inc
Software Engineering Projects

Company Employees:
o   CEO: Nabor Palomera
o   Michael Kries
o   Phuoc Ngo
o   Erick Rivera
o   Kevin De La Torre

Project ID #: 05 (Gradebook) Estimated Development Time: 7 days

Project Description:

Implementation of an interactive gradebook. Its purpose is to facilitate grading assignments with different points and placing it in the correct category where each has different weight as percentage.

Preliminary Development Plan: Developed in Java. Implementation As Specified in Requirements.

Checkpoint 1 (Day 1, 2): Design the graphical interface for grade book program on scratch with the useful tools: 
  o Term(Fall, Winter, Spring, Summer), year 
  o Class o Students (Last, Middle, and First name) 
  o Grading (assignment, weight percentage – should add up to 100% for all categories) 
  o Final grade column (based on weight percentage then compute the letter Grade, user can specify the lower and upper bound to compute     the letter grade) 
  o Save and read the file

Checkpoint 2 (Day 3): 
  o Transfer all the designs of the graphical interface to coding. 
  o Add codes and functions to the Term, Class, and Students by using coding. 
  o For Term, there are adding and removing functions. (Spring 2017) 
      -Removing the Term will remove all the classes in that term. 
  o For Class, there are adding and removing functions. (CS 480) 
      -Removing the class will remove all the students in that class. (including students’ grades, names)
  o For Students, there is a box to add student first, middle, and last name. 
      -After adding the student to the class, show the table of student on the interface to show the student’s first, middle, and last          name, and assignment with score. 
      -Sort the list by student’s last name.

Checkpoint 3 (Day 4, 5): 
  o Design the Grading function for the project. 
      -After add the student to the class, there is a button to add the assignment. 
          * Press the ‘add assignment’ button, the box will popup to prompt the user input the assignment number and (date) for the assignment. Then create the assignment column next to the student’s name and show to the table. 
  o Design the weight column for the assignment
      -The weight column is the total points of all the assignments (computed in percentage %) 
      -Weight = (Total points the student earned/Total points of all assignments) * 100
  o Design the Final Grade to compute the letter grade for the user 
      -The user can specify the lower bound for each letter grade. 
      -Pop up the box for user to input.

Checkpoint 4 (Day 6): 
  o Add the saving and reading file to the program.
  o Do extra credit: Exporting the file as a CSV file.

Checkpoint 5 (Day 7): 
  o Testing, debugging, checking the project runs properly before turn it in.

Checkpoint 6 (Day 8): 
  o Project Due

#######################################################################################

Project ID #: 9 (Tic-Tac-Toe) Estimated Development Time: 3 Days

Project Description:

This is an app that will be android based using Java. It will provide a GUI for the tic-tac-toe grid on a mobile device running Android OS. The users will be able to interact with the graphical display by tapping on the available slots and popup dialogues will display if the wrong slot is selected and to confirm the selected slot is the one that the user meant to choose. We will implement tips so that the app is as user-friendly as possible and there is no confusion when the users are playing the game.

Preliminary Development Plan:

Checkpoint 1 (Day 1):
•	Create Logic and Data Structures

Checkpoint 2 (Day 2):
•	Create GUI and link to logic

Checkpoint 3 (Day 3):
•	Create save dialogue and complete testing

•	Complete documentation and report.

######################################################################################

Project ID #: 2 (Imp Compiler) Estimated Development Time: 7 Days

Project Description:

This program will be developed using the java programming language as a command line program. It will read in a .imp file and compile it to a .imc file. This compilation will be done according to the specifications in the project description. The program will take in files through command line arguments when executed. It will have the ability to compile multiple files by going through given files sequentially. The program will not need any user interaction after the execution, outputted files will be same name as input file but with .imc extension. In terms of development, were going to focus on creating the statement parser first, then proceed to develop the statement handler which will perform the translation into imp code and output it to the imc file.
 
Preliminary Development Plan:

Checkpoint 1 ( Day 1 ):
·       Setup project
·       Begin work on statement parser
o   Parser will read through file and determine whether the statement is valid and which command it corresponds to
o   Will print out error if invalid, give line number, then quit
·       Begin work on statement handler
Checkpoint 2 ( Day 3 ):
·       Setup project
·       Have statement parser done
o   Complete testing on parser
·       Continue work on statement handler
o   Statement handler will process parsed statements and convert to imp code
Checkpoint 3 ( Day 5 ):
·       Finish statement handler
·       Finish up all output and file writing
o   Writing to file happens at end, file is not created if there is error.
·       Perform extensive full program testing
Checkpoint 4 ( Day 7 ):
·       Complete testing
·       Complete documentation and report


######################################################################################

Project ID #: 13 (Password Manager) Estimated Development Time: 10 Days

Project Description:

This is an android based application implemented in Java that handles password storage safely. Its purpose is to help the user store all his/her passwords in one place so that there is no need to remember them. This application gives the user the option to set up a one time master authentication as either a password, pin, or swipe pattern. Whatever the user prefers to use, the authenticity is encrypted using the Blowfish encryption algorithm, a symmetric block cipher. Once logging in, the user enters the vault where an add button is placed to create a new entry with three attributes: website, username, and password. The password can either be entered manually or randomly generated. The program handles duplicate passwords, so that the user’s passwords are all different for each service account. If the user forgets his/her master authentication password to access the vault, we gave the option to delete all data so that it is not breached by any attacker that may be trying to access personal information,
 
Preliminary Development Plan:

Android based application using Java.

Checkpoint 1 (Day 1 & 2):
·        Create an underlying Java class to save, load, encrypt, and decrypt passwords.
 
Checkpoint 2 (Day 3 & 4):
·        Create login screen and credential store for initial login.
 
Checkpoint 3 (Day 5 & 6 & 7):
·        Create GUI for application and link to underlying java class
 
Checkpoint 4 (Day 8 & 9):
·        Test product and fix bugs
 
Checkpoint 4 (Day 10):
·        Finish report and documentation.

######################################################################################

Project ID #: 24 (Phone Contacts Book) Estimated Development Time: 10 Days

Project Description:

This is an android based application implemented in Java that stores contact information on Google’s secure database called Firebase. It’s purpose compared to other typical contacts applications is to allow the user to be able to add custom entries besides the default name, phone, physical address, and email. The user has the choice to add a LinkedIn account, Instagram, Snapchat, or other. There is a limit of five custom entries as the contact list would look to crumbled with contact info. Once a user adds a contact to the list, he has the option to call the contact by clicking on the phone icon. This opens up the phone dialer activity and grabs the specified phone number on the contact list and is just a button click away from making a call to his saved contact. If the user has other apps other than the default android dialer, a dialog will display with options and can choose by preference. The other option the user has it to click on the message button. This open the default email client provider, if there is one. Otherwise, similar to the phone dialer, he has the options to choose from other client providers. It’s important to note that everything is stored securely on Firebase and updates automatically. All the contact information is private to the user and not publicly available.
 
Preliminary Development Plan:

Android based application using Java.

Checkpoint 1 (Day 1 & 2):
·        Create an underlying Java class to save and load data (using Firebase database).
 
Checkpoint 2 (Day 3 & 4):
·       Design the application front-end and rest of architecture that will be used.
 
Checkpoint 3 (Day 5 & 6 & 7):
·        Implement GUI and any other classes designed.
            -Link GUI to underlying Java class
 
Checkpoint 4 (Day 8 & 9):
·        Test product and fix bugs
 
Checkpoint 5 (Day 10):
·        Finish report and documentation.
