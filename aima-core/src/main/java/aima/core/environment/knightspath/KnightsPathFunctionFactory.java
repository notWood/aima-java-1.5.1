package aima.core.environment.knightspath;

import java.util.LinkedHashSet;
import java.util.Set;

import aima.core.agent.Action;
import aima.core.search.framework.ActionsFunction;
import aima.core.search.framework.ResultFunction;
import aima.core.util.datastructure.XYLocation;

/**
 * Provides useful functions for the knight's path problem. The
 * incremental formulation and the complete-state formulation share the same
 * RESULT function but use different ACTIONS functions.
 * 
 * @author Ciaran O'Reilly
 * @author R. Lunde
 * @author J. Navas
 */
public class KnightsPathFunctionFactory {

	private static ActionsFunction _cActionsFunction = null;
	private static ResultFunction _resultFunction = null;


	/**
	 * Returns an ACTIONS function for the complete-state formulation of the
	 * knight's path problem.
	 */
	public static ActionsFunction getActionsFunction() {
		if (null == _cActionsFunction) {
			_cActionsFunction = new KPActionsFunction();
		}
		return _cActionsFunction;
	}

	/**
	 * Returns a RESULT function for the knight's path problem.
	 */
	public static ResultFunction getResultFunction() {
		if (null == _resultFunction) {
			_resultFunction = new KPResultFunction();
		}
		return _resultFunction;
	}


	/**
	 * Provides all possible knight movements as actions.
	 * @author R. Lunde
	 * @author J. Navas
	 */
	private static class KPActionsFunction implements ActionsFunction {

		public Set<Action> actions(Object state) {
			Set<Action> actions = new LinkedHashSet<Action>();
			KnightsPathBoard board = (KnightsPathBoard) state;
			

			int cx = board.getKnightCurrentPosition().getXCoOrdinate();
			int cy = board.getKnightCurrentPosition().getYCoOrdinate();
			
			if(cx - 2 >= 0){
				if(cy - 1 >= 0)
					actions.add(new KnightsPathAction(KnightsPathAction.MOVE_KNIGHT,new XYLocation(cx - 2, cy - 1)));
				if(cy + 1 < board.getSize())
					actions.add(new KnightsPathAction(KnightsPathAction.MOVE_KNIGHT,new XYLocation(cx - 2, cy + 1)));
			}
			
			if(cx - 1 >= 0){
				if(cy - 2 >= 0)
					actions.add(new KnightsPathAction(KnightsPathAction.MOVE_KNIGHT,new XYLocation(cx - 1, cy - 2)));
				if(cy + 2 < board.getSize())
					actions.add(new KnightsPathAction(KnightsPathAction.MOVE_KNIGHT,new XYLocation(cx - 1, cy + 2)));
			}
			
			if(cx + 1 < board.getSize()){
				if(cy - 2 >= 0)
					actions.add(new KnightsPathAction(KnightsPathAction.MOVE_KNIGHT,new XYLocation(cx + 1, cy - 2)));
				if(cy + 2 < board.getSize())
					actions.add(new KnightsPathAction(KnightsPathAction.MOVE_KNIGHT,new XYLocation(cx + 1, cy + 2)));
			}
			
			if(cx + 2 < board.getSize()){
				if(cy - 1 >= 0)
					actions.add(new KnightsPathAction(KnightsPathAction.MOVE_KNIGHT,new XYLocation(cx + 2, cy - 1)));
				if(cy + 1 < board.getSize())
					actions.add(new KnightsPathAction(KnightsPathAction.MOVE_KNIGHT,new XYLocation(cx + 2, cy + 1)));
			}

			return actions;
		}
	}

	/** Supports knight movement actions. */
	private static class KPResultFunction implements ResultFunction {
		public Object result(Object s, Action a) {
			if (a instanceof KnightsPathAction) {
				KnightsPathAction qa = (KnightsPathAction) a;
				KnightsPathBoard board = (KnightsPathBoard) s;
				KnightsPathBoard newBoard = new KnightsPathBoard(board.getSize());
				newBoard.setBoard(board.getKnightCurrentPosition());
				
				if (qa.getName() == KnightsPathAction.MOVE_KNIGHT){
					newBoard.addKnightAt(qa.getLocation());
					newBoard.addKnightGoalAt(board.getKnightGoalPosition());
				}
				s = newBoard;
			}
			// if action is not understood or is a NoOp
			// the result will be the current state.
			return s;
		}
	}
}