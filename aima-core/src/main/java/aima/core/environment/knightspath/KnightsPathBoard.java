package aima.core.environment.knightspath;


import aima.core.util.datastructure.XYLocation;

/**
 * Represents a quadratic board with a matrix of squares on which a knight can be
 * placed (only one per square) and moved.
 * 
 * @author Ravi Mohan
 * @author R. Lunde
 * @author J. Navas
 */
public class KnightsPathBoard {

	/**
	 * X---> increases left to right with zero based index Y increases top to
	 * bottom with zero based index | | V
	 */
	int[][] squares;
	
	XYLocation _knightCurrentPosition;
	XYLocation _knightStartPosition;
	XYLocation _KnightGoalPosition;

	int size;

	
	public KnightsPathBoard(int n) {
		size = n;
		squares = new int[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				squares[i][j] = 0;
			}
		}
	}

	public void clear() {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				squares[i][j] = 0;
			}
		}
	}

	
	public void setBoard(XYLocation loc) {
		clear();
		addKnightAt(loc);
	}

	public int getSize() {
		return size;
	}

	/* set the knight current location*/
	public void addKnightAt(XYLocation l) {
		_knightCurrentPosition = l;
	}
	
	/* Set the knight starting location */
	public void addKnightStartAt(XYLocation l) {
		_knightStartPosition = l;
		_knightCurrentPosition = l;	
	}
	
	/* Set the knight goal location*/
	public void addKnightGoalAt(XYLocation l) {
		_KnightGoalPosition = l;
	}
	
	/* Get the knight starting location*/
	public XYLocation getKnightStartPosition() {
		return _knightStartPosition;
	}
	
	/* Get the knight location*/
	public XYLocation getKnightCurrentPosition() {	
		return _knightCurrentPosition;
	}
	
	/* Get the knight goal location*/
	public XYLocation getKnightGoalPosition() {	
		return _KnightGoalPosition;
	}
	
	
	public double getDistance(){
		return getManhattanDistance();
	}
	
	/* Determines the distance between the knight position and the goal position */
	private double getManhattanDistance(){ 
		// Manhattan distance
		int cx = getKnightCurrentPosition().getXCoOrdinate();
		int cy = getKnightCurrentPosition().getYCoOrdinate();
		int gx = getKnightGoalPosition().getXCoOrdinate();
		int gy = getKnightGoalPosition().getYCoOrdinate();
		
		int x = Math.abs(cx-gx);
	    int y = Math.abs(cy-gy);
	    int z = (x+y);
	    return z;
	}
	


	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if ((o == null) || (this.getClass() != o.getClass()))
			return false;

		return true;
	}

}