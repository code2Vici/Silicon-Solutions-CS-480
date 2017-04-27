COMPILEFILE = TicTacToe

$(COMPILEFILE).class : $(COMPILEFILE).java
	@echo -n Compiling $(COMPILEFILE)...
	@javac $(COMPILEFILE).java
	@echo Done

edit:
	@make --silent clean;
	@clear
	@vim $(COMPILEFILE).java

clean:
	@echo -n Cleaning...
	@rm -rf *.class
	@echo Done
