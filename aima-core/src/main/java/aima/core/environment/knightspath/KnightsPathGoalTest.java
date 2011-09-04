package aima.core.environment.knightspath;

import aima.core.search.framework.GoalTest;

/**
 * @author R. Lunde
 */
public class KnightsPathGoalTest implements GoalTest {

	public boolean isGoalState(Object state) {
		KnightsPathBoard board = (KnightsPathBoard) state;

		return  board.getDistance() == 0;

	}
}