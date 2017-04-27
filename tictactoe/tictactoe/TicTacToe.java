import java.util.Scanner;

/**
 * Driver program for TicTacToe app
*/
public class TicTacToe {
	public static void main( String[] args ) {
		final char PLAYER1_SYMBOL = 'X';
		final char PLAYER2_SYMBOL = 'O';

		GameGrid grid = new GameGrid( PLAYER1_SYMBOL, PLAYER2_SYMBOL );
		Scanner input = new Scanner( System.in );

		// Setup
		int x = -1;
		int y = -1;
		boolean valid; // Used for checking if user inputted position is valid
		boolean playing = true; // Is game running boolean
		char playAgain; // Used for y/n input to play again
		int totalMoveCounter = grid.getGridSize(); // Tracks total moves in case of tie game

		while ( playing ) {
			grid.render();
			valid = false;
			System.out.println( "Your move player 1: " );
			while ( valid != true ) {
				x = userInput( "Row: ", input );
				y = userInput( "Column: ", input );
				valid = grid.checkValid( x, y );
				if ( !valid ) {
					System.out.println( "Inavlid position" );
				}
			}
			grid.setState( x, y, 0 );
			totalMoveCounter--;
			if ( grid.checkWin() ) {
				System.out.println( "Congrats Player 1 you win" );
				playing = false;
			} else if ( totalMoveCounter == 0 ) {
				System.out.println( "Tie game" );
				playing = false;
			}
			grid.render();
			if ( playing ) {
				System.out.println( "Your move player 2: " );
				valid = false;
				while ( valid != true ) {
					x = userInput( "Row: ", input );
					y = userInput( "Column: ", input );
					valid = grid.checkValid( x, y );
					if ( !valid ) {
						System.out.println( "Invalid position" );
					}
				}
				grid.setState( x, y, 1 );
				totalMoveCounter--;
				if ( grid.checkWin() ) {
					System.out.println( "Congrats Player 2 you win" );
					playing = false;
				} else if ( totalMoveCounter == 0 ) {
					System.out.println( "Tie game" );
					playing = false;
				}
			} else {
				System.out.print( "Want to play again ( Y/N )? " );
				playAgain = input.next().charAt( 0 );
				if ( playAgain == 'y' || playAgain == 'Y' ) {
					grid.reset();
					playing = true;
					valid = false;
				} else {
					playing = false;
				}
			}
		}

		System.out.println( "\nThanks for playing" );
	}

	/**
	 * Helper function to get user input.
	 * @param prompt
	 * @param input
	 * @return Integer obtained
	 */
	public static int userInput( String prompt, Scanner input ) {
		System.out.print( prompt );
		return input.nextInt();
	}
}
