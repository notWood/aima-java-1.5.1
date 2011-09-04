package aima.core.environment.knightspath;

import aima.core.search.framework.HeuristicFunction;

/**
 * Estimates the distance to goal by the number of attacking pairs of queens on
 * the board.
 * 
 * @author R. Lunde
 */
public class ManhattanHeuristic implements HeuristicFunction {

	public double h(Object state) {
		KnightsPathBoard board = (KnightsPathBoard) state;
		return board.getDistance();
	}
}