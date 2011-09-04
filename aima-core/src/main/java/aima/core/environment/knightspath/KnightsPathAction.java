package aima.core.environment.knightspath;

import aima.core.agent.impl.DynamicAction;
import aima.core.util.datastructure.XYLocation;

/**
 * The starting knight position, goal knight position and the current position can be placed.
 * 
 * @author Ravi Mohan
 * @author R. Lunde
 * @author j. Navas
 */
public class KnightsPathAction extends DynamicAction {
	
	public static final String PLACE_KNIGHT_START = "PlaceKnightStartAt";
	public static final String PLACE_KNIGHT_GOAL = "placeKnightGoalAt";
	public static final String MOVE_KNIGHT = "moveKnightTo";

	public static final String ATTRIBUTE_KNIGHT_LOC = "location";

	/**
	 * Creates a queen action. Supported values of type are {@link #PLACE_QUEEN}
	 * , {@link #REMOVE_QUEEN}, or {@link #MOVE_QUEEN}.
	 */
	public KnightsPathAction(String type, XYLocation loc) {
		super(type);
		setAttribute(ATTRIBUTE_KNIGHT_LOC, loc);
	}

	public XYLocation getLocation() {
		return (XYLocation) getAttribute(ATTRIBUTE_KNIGHT_LOC);
	}

	public int getX() {
		return getLocation().getXCoOrdinate();
	}

	public int getY() {
		return getLocation().getYCoOrdinate();
	}
}
