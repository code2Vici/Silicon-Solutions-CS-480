import java.util.Arrays;

public class GameGrid {
	final int X_WIDTH = 3;
	final int Y_WIDTH = 3; 
	final char NULLSYMBOL = '-';

	private int[][] grid;
	private boolean winConditionMet;
	private char p1Symbol;
	private char p2Symbol;

	/**
	 * Creates a 3x3 table for game and fills with "empty" value ( -1 ) 
	 * Also sets up symbols for players
	 */
	public GameGrid( char p1, char p2 ) {
		grid = new int[ X_WIDTH ][ Y_WIDTH ];
		reset();
		p1Symbol = p1;
		p2Symbol = p2;
	}

	/**
	 * Returns total grid size
	 * @return int total grid sie
	 */
	public int getGridSize() {
		return ( X_WIDTH * Y_WIDTH );
	}

	
	/**
	 * Changes state for given position in table to value.
	 * @param x
	 * @param y
	 * @param player
	 */
	public void setState( int x, int y, int player ) {
		// Player values:
		// 0 = Player 1
		// 1 = Player 2
		grid[ x ][ y ] = player;
	}

	/**
	 * Draws table
	 */
	public void render() {
		System.out.println( "\n" );
		for ( int i = 0; i < X_WIDTH; i++ ) {
			System.out.print( " " ); // Initial table space
			for ( int j = 0; j < Y_WIDTH; j++ ) {
				printChar( i, j );
				if ( j != ( Y_WIDTH - 1 ) ) {
					// Used in case of final num in table, doesnt print outline
					System.out.print( " | " );
				} else {
					System.out.println();
				}
			}
			if ( i != ( X_WIDTH - 1 ) ) {
				// Draws horizontal lines in table
				for ( int k = 0; k < ( 3 * X_WIDTH + ( X_WIDTH - 1 ) ); k++ ) {
					System.out.print( '-' );
				}
				System.out.println();
			}
		}
	}

	/**
	 * Prints character at given position
	 */
	private void printChar( int x, int y ) {
		if ( grid[ x ][ y ] == 0 ) {
			System.out.print( p1Symbol );
		} else if ( grid[ x ][ y ] == 1 ) {
			System.out.print( p2Symbol );
		} else {
			System.out.print( NULLSYMBOL );
		}
	}

	/**
	 * Checks if somebody has won game.
	 */
	public boolean checkWin() {
		boolean win = true;

		// Checks all horizontal win conditions 
		for ( int j = 0; j < Y_WIDTH; j++ ) {
			win = true;
			for ( int i = 1; i < X_WIDTH; i++ ) {
				if ( win ) {
					if ( grid[ i ][ j ] == grid[ i - 1 ][ j ] && grid[ i ][ j ] != -1 ) {
						win = true;
					} else { win = false; }

				}
			}
			if ( win ) { return win; }
		}

		// Checks all vertical win conditions
		win = true; // Needed for default check
		for ( int j = 0; j < X_WIDTH; j++ ) {
			win = true;
			for ( int i = 1; i < Y_WIDTH; i++ ) {
				if ( win ) {
					if ( grid[ j ][ i ] == grid[ j ][ i - 1 ] && grid[ i ][ j ] != -1 ) {
						win = true;
					} else { win = false; }

				}
			}
			if ( win ) { return win; }
		}

		// Upper left to bottom right diagonal check
		if ( X_WIDTH == Y_WIDTH ) {
			// For generality, if table isn't square diagonal not valid.
			win = true;
			for ( int i = 1; i < X_WIDTH; i++ ) {
				if ( win ) {
					if ( grid[ i ][ i ] == grid[ i- 1 ][ i - 1 ] && grid[ i ][ i ] != -1 ) {
						win = true;
					} else { win = false; }

				}
			}
		}

		return win;
	}

	/**
	 * Checks if given position is within table or position has been played.
	 * @param x
	 * @param y
	 * @return Position validity.
	 */
	public boolean checkValid( int x, int y ) {
		return !( x < 0 || x >= X_WIDTH  	// Checks if x within table 
			|| y < 0 || y >= Y_WIDTH	// Checks if y within table 
			|| grid[ x ][ y ] != -1 );	// Checks if spot already used
	}

	/**
	 * Resets grid back to empty.
	 */
	public void reset() {
		for ( int i = 0; i < X_WIDTH; i++ ) {
			for ( int j = 0; j < Y_WIDTH; j++ ) {
				grid[ i ][ j ] = -1;
			}
		}
	}
}
