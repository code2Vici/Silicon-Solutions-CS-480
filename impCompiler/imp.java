import java.io.*;
import java.util.*;
import java.lang.*;



public class imp {
	
	// Global counters used to keep track of labels in case of multiple uses
	public static int elseCounter = 1; 
	public static int whileCounter = 1;

	public static void main( String[] args ) throws Exception {
		for ( int i = 0; i < args.length; i++ ) {
			File currentFile = new File( args[ i ] );
			// Delimiter used in following for getting semicolons
			try ( Scanner reader = new Scanner( currentFile ).useDelimiter( "\\s*;\\s*|\\n|\\s*thens*|\\s*dos*" ) ) {
				String baseName = args[ i ].split( "\\." )[ 0 ];
				File writeTo = new File( baseName + ".imc" );
				StringBuilder writer = new StringBuilder();
				String line;
				String[] statement;

				line = reader.next(); // REMOVE - TO GET SKIP BEGIN STATEMENT FOR TEST RUNS UNTIL ACTUALLY IMPLEMENTED
				statement = lineParse( line );
				if ( !statement[ 0 ].equals( "begin" ) && !statement[ 1 ].equals( baseName ) ) {
					throw new Exception( "File( " + args[ i ] + " ) -> Header Error( \"" + line + "\" )\n" ); 
				}

				while ( reader.hasNext() ) {
					line = readNext( reader );
					if ( line.equals( "" ) ) { continue; }
					statement =  lineParse( line );
					int valid = handle( reader, writer, statement );
					if ( valid == 0 ) {
						throw new Exception( "File( " + args[ i ] + " ) -> Syntax Error( \"" + line + "\" )\n" );
					} else if ( valid == -1 ) {
						break;
					}
				}

				PrintWriter write = new PrintWriter( writeTo );
				write.print( writer.toString().trim() );
				write.close();
			} catch ( FileNotFoundException e ) {
				System.out.println( "File not found ( " + args[ i ] + " )" );
			} catch ( Exception e ) {
				System.out.println( e.getMessage() );
			}
		}
	}

	public static String readNext( Scanner reader ) {
		return reader.next().trim();
	}

	public static String[] lineParse( String line ) {
		return line.trim().split( " " );
	}

	public static int handle( Scanner reader, StringBuilder writer, String[] statement ) {
		/*
		 * valid - 1
		 * invalid - 0
		 * end - -1
		 */
		switch ( statement[ 0 ] ) {
			case "X":
			case "Y":
			case "Z":
				return assignment( writer, statement );
			case "if":
				return conditional( reader, writer, statement[ 1 ] );
			case "while":
				return loop( reader, writer, statement[ 1 ] );
			case "end":
				return -1;
			default:
				return 0; 
		}
	}

	public static int loop( Scanner reader, StringBuilder writer, String var ) {
		try {
			if ( !isValidVar( var ) ) {
				return 0;
			}
	
			writer.append( "loop" + whileCounter + ": JPZ Reg" + var + " endwhile" + whileCounter + "\n" );
			CharSequence end = "endwhile";
			
			String line = readNext( reader );
			int status = 1;

			while ( !line.contains( end ) ) {
				status = handle( reader, writer, lineParse( line ) );
				if ( status != 1 ) { return status; }
				line = readNext( reader );
			}
			status = handle( reader, writer, lineParse( line.split( "endwhile" )[ 0 ] ) );
			if ( status != 1 ) { return status; }
			writer.append( "JMP loop" + whileCounter + "\n" );
			writer.append( "endwhile" + whileCounter++ + ": " );
			return status;
		} catch ( Exception e ) {
			return 0;
		}	
	}

	public static int conditional( Scanner reader, StringBuilder writer, String var ) {
		try {
			if ( !isValidVar( var ) ) {
				return 0; // invalid conditional variable
			}

			writer.append( "JPZ Reg" + var + " else" + elseCounter + "\n" );
			String line = readNext( reader );
			CharSequence other = "else"; // Used to split line when read
			CharSequence end = "endif"; // Used as end at finish
			int status = 1; // Used for detecting error

			while ( !line.contains( other ) ) { // Scanner is written to scan up to a semicolon, so this'll take care of any that are in between then and else
				status = handle( reader, writer, lineParse( line ) );
				if ( status != 1 ) { return status; }
				line = readNext( reader );
			}

			String[] statements = line.split( "else" );
			// If we split at else the way I wrote the scanner, it means there is still one true statement left before the else statements so we process that first
			status = handle( reader, writer, lineParse( statements[ 0 ] ) );
			if ( status != 1 ) { return status; }
			writer.append( "JMP endif" + elseCounter + "\n" );
			writer.append( "else" + elseCounter + ": " );
			line = statements[ 1 ];
			while ( !line.contains( end ) ) { // Semicolon statements between else and endif
				status = handle( reader, writer, lineParse( line ) );
				if ( status != 1 ) { return status; }
				line = readNext( reader );
			}

			statements = line.split( "endif" );
			// Same reason as above but for else statements, one statement before the end if
			status = handle( reader, writer, lineParse( statements[ 0 ] ) );
			writer.append( "endif" + elseCounter++ + ": " );
			return status;
		} catch ( Exception e ) {
			return 0;
		}
	}

	public static int assignment( StringBuilder writer, String[] statement ) {
		if ( !statement[ 1 ].equals( ":=" ) ) {
			return 0;
		}

		try {
			if ( statement.length == 3 ) {
				// Assign int
				writer.append( "STO Reg" + statement[ 0 ] + " " + toInt( statement[ 2 ] )  + "\n" );
			} else if ( statement.length == 5 ) {
				// Assign Expression
				String first = statement[ 2 ];
				String second = statement[ 4 ];
				switch ( statement[ 3 ] ) {
					case "+":
						if ( isInt( first ) && isInt( second ) ) { // reg = int + int
							writer.append( "STO RegU " + first + "\n" );
							writer.append( "ADD RegU " + second + "\n" );
						} else if ( isInt( first ) || isInt( second ) ) { // reg = reg + int
							writer.append( "ADD Reg" );
							if ( isValidVar( first ) ) {
								writer.append( first + " " + second + "\n" );
							} else {
								writer.append( second + " " + first + "\n" );
							}
						} else { // reg = reg + reg
							writer.append( "ADR Reg" + first + " Reg" + second + "\n" );
						}
						break;
					case "*":
						if ( isInt( first ) && isInt( second ) ) { // reg = int * int
							writer.append( "STO RegU " + first + "\n" );
							writer.append( "MUL RegU " + second + "\n" );
						} else if ( isInt( first ) || isInt( second ) ) { // reg = reg * int
							writer.append( "MUL Reg" );
							if ( isValidVar( first ) ) {
								writer.append( first + " " + second + "\n" );
							} else {
								writer.append( second + " " + first + "\n" );
							}
						} else { // reg = reg * reg
							writer.append( "MLR Reg" + first + " Reg" + second + "\n" );
						}
						break;
					case "-":
						if ( isInt( first ) && isInt( second ) ) {
							writer.append( "STO RegU " + first + "\n" );
							writer.append( "SUB RegU " + second + "\n" );
						} else if ( isInt( first ) || isInt( second ) ) {
							if ( isValidVar( first ) ) {
								writer.append( "SUB Reg" + first + " " + second + "\n" );
							} else {
								writer.append( "STO RegU " + first + "\n" );
								writer.append( "SBR Reg" + second + " RegU\n" );
							}
						} else {
							writer.append( "SBR Reg" + second + " Reg" + first + "\n" );
						}
						break;
					case "/":
						if ( isInt( first ) && isInt( second ) ) {
							writer.append( "STO RegU " + first + "\n" );
							writer.append( "DIV RegU " + second + "\n" );
						} else if ( isInt( first ) || isInt( second ) ) {
							if ( isValidVar( first ) ) {
								writer.append( "DIV Reg" + first + " " + second + "\n" );
							} else {
								writer.append( "STO RegU " + first + "\n" );
								writer.append( "DVR RegU Reg" + second + "\n" );
							}
						} else {
							writer.append( "DVR Reg" + first + " Reg" + second + "\n" );
						}
						break;
					default:
						return 0;
				}
				writer.append( "MOV Reg" + statement[ 0 ] + " RegU\n" ); // Result stored in RegU 
			} else {
				// Not a valid statement
				return 0;
			}
			return 1;
		} catch ( Exception e ) {
			return 0;

		}
	}

	public static boolean isValidVar( String str ) {
		return ( str.equals( "X" ) || str.equals( "Y" ) || str.equals( "Z" ) );
	}


	public static int toInt( String str ) {
		return Integer.parseInt( str );
	}

	public static boolean isInt( String str ) {
		try {
			Integer.parseInt( str );
			return true;
		} catch ( Exception e ) {
			return false;
		}
	}
}
