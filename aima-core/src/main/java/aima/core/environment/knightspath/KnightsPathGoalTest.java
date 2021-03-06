package aima.core.environment.knightspath;

import aima.core.search.framework.GoalTest;

/**
 * Determines if a presented goal has been reached
 * 
 * @author R. Lunde
 * @author J. Navas
 */
public class KnightsPathGoalTest implements GoalTest {

	public boolean isGoalState(Object state) {
		KnightsPathBoard board = (KnightsPathBoard) state;

		return  board.getDistance() == 0;

	}
}