package aima.gui.applications.search.games;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import javax.swing.JButton;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.Environment;
import aima.core.agent.EnvironmentState;
import aima.core.agent.Percept;
import aima.core.agent.impl.AbstractEnvironment;
import aima.core.environment.knightspath.EuclideanHeuristic;
import aima.core.environment.knightspath.ManhattanHeuristic;
import aima.core.environment.knightspath.KnightsPathBoard;
import aima.core.environment.knightspath.KnightsPathFunctionFactory;
import aima.core.environment.knightspath.KnightsPathGoalTest;
import aima.core.environment.knightspath.KnightsPathAction;
import aima.core.search.framework.ActionsFunction;
import aima.core.search.framework.GraphSearch;
import aima.core.search.framework.Problem;
import aima.core.search.framework.Search;
import aima.core.search.framework.SearchAgent;
import aima.core.search.informed.AStarSearch;
import aima.core.util.datastructure.XYLocation;
import aima.gui.framework.AgentAppController;
import aima.gui.framework.AgentAppEnvironmentView;
import aima.gui.framework.AgentAppFrame;
import aima.gui.framework.MessageLogger;
import aima.gui.framework.SimpleAgentApp;
import aima.gui.framework.SimulationThread;

/**
 * The Graphical Knight's Path game application finds the shortest path between 
 * two squares on a quadratic board in terms of the number of  sequentially connected 
 * legal moves needed to take a knight from one square to the other.
 * 
 * @author J. Navas
 * @author Ruediger Lunde
 */
public class KnightsPathApp extends SimpleAgentApp {

	/** List of supported search algorithm names. */
	protected static List<String> SEARCH_NAMES = new ArrayList<String>();
	/** List of supported search algorithms. */
	protected static List<Search> SEARCH_ALGOS = new ArrayList<Search>();

	/** Adds a new item to the list of supported search algorithms. */
	public static void addSearchAlgorithm(String name, Search algo) {
		SEARCH_NAMES.add(name);
		SEARCH_ALGOS.add(algo);
	}

	static {
		addSearchAlgorithm("A* search (Manhattan Distance heuristic)",
				new AStarSearch(new GraphSearch(),
						new ManhattanHeuristic()));
		addSearchAlgorithm("A* search (Euclidean Distance heuristic)",
				new AStarSearch(new GraphSearch(),
						new EuclideanHeuristic()));
	}

	/** Returns a <code>KnightsPathView</code> instance. */
	public AgentAppEnvironmentView createEnvironmentView() {
		return new KnightsPathView();
	}

	/** Returns a <code>KnightsPathFrame</code> instance. */
	@Override
	public AgentAppFrame createFrame() {
		return new KnightsPathFrame();
	}

	/** Returns a <code>KnightsPathController</code> instance. */
	@Override
	public AgentAppController createController() {
		return new KnightsPathController();
	}

	// ///////////////////////////////////////////////////////////////
	// main method

	/**
	 * Starts the application.
	 */
	public static void main(String args[]) {
		new KnightsPathApp().startApplication();
	}

	// ///////////////////////////////////////////////////////////////
	// some inner classes

	/**
	 * Adds some selectors to the base class and adjusts its size.
	 */
	protected static class KnightsPathFrame extends AgentAppFrame {
		private static final long serialVersionUID = 1L;
		public static String ENV_SEL = "EnvSelection";
		public static String SEARCH_SEL = "SearchSelection";

		public KnightsPathFrame() {
			setTitle("Knight's Path Application");
			setSelectors(new String[] { ENV_SEL, SEARCH_SEL }, new String[] { "Select Environment","Select Search" });
			setSelectorItems(ENV_SEL, new String[] { "6 KnightsPath", "8 KnightsPath","10 KnightsPath", "12 KnightsPath" }, 0);			
			setSelectorItems(SEARCH_SEL, (String[]) SEARCH_NAMES.toArray(new String[] {}), 0);
			setEnvView(new KnightsPathView());
			setSize(800, 600);
		}
	}

	/**
	 * Displays the informations provided by a <code>KnightsPathEnvironment</code>
	 * on a panel.
	 */
	protected static class KnightsPathView extends AgentAppEnvironmentView implements ActionListener {
		
		private static final long serialVersionUID = 1L;
		protected JButton[] squareButtons;
		protected int currSize = -1;
		

		protected KnightsPathView() {
		}

		@Override
		public void setEnvironment(Environment env) {
			super.setEnvironment(env);
			showState();
		}

		/** Agent value null indicates a user initiated action. */
		@Override
		public void agentActed(Agent agent, Action action,
				EnvironmentState resultingState) {
			showState();
			notify((agent == null ? "User: " : "") + action.toString());
		}

		@Override
		public void agentAdded(Agent agent, EnvironmentState resultingState) {
			showState();
		}

		/**
		 * Displays the board state by labeling and coloring the square buttons.
		 */
		protected void showState() {
			KnightsPathBoard board = ((KnightsPathEnvironment) env).getBoard();
			if (currSize != board.getSize()) {
				currSize = board.getSize();
				removeAll();
				setLayout(new GridLayout(currSize, currSize));
				squareButtons = new JButton[currSize * currSize];
				for (int i = 0; i < currSize * currSize; i++) {
					JButton square = new JButton("");
					square.setMargin(new Insets(0, 0, 0, 0));
					square
							.setBackground((i % currSize) % 2 == (i / currSize) % 2 ? Color.WHITE
									: Color.LIGHT_GRAY);
					square.addActionListener(this);
					squareButtons[i] = square;
					add(square);
				}
			}
			for (int i = 0; i < currSize * currSize; i++)
				squareButtons[i].setText("");
			
			Font f = new java.awt.Font(Font.SANS_SERIF, Font.PLAIN, Math.min(getWidth(), getHeight())* 3 / 4 / currSize);
			
			
			/* Show The Start position */
			if(board.getKnightStartPosition() != null){
				JButton startSquare = squareButtons[board.getKnightStartPosition().getXCoOrdinate() + board.getKnightStartPosition().getYCoOrdinate() * currSize];
				startSquare.setForeground(Color.BLACK);
				startSquare.setFont(f);
				startSquare.setText("S");
			}
			
			/* Show The Goal position */
			if(board.getKnightGoalPosition() != null){
				JButton goalSquare = squareButtons[board.getKnightGoalPosition().getXCoOrdinate() + board.getKnightGoalPosition().getYCoOrdinate() * currSize];
				goalSquare.setForeground(Color.BLACK);
				goalSquare.setFont(f);
				goalSquare.setText("G");
			}
			
			for (XYLocation locationVisited : board.getLocationsVisited()) {
				if(locationVisited.getXCoOrdinate() == board.getKnightGoalPosition().getXCoOrdinate() && locationVisited.getYCoOrdinate() == board.getKnightGoalPosition().getYCoOrdinate()){
					JButton goalSquare = squareButtons[board.getKnightGoalPosition().getXCoOrdinate() + board.getKnightGoalPosition().getYCoOrdinate() * currSize];
					goalSquare.setForeground(Color.BLACK);
					goalSquare.setFont(f);
					goalSquare.setText("G");
				}else{
					JButton kSquare = squareButtons[locationVisited.getXCoOrdinate() + locationVisited.getYCoOrdinate() * currSize];
					kSquare.setForeground(Color.BLACK);
					kSquare.setFont(f);
					kSquare.setText("K");
				}
			}
			
			validate();
		}

		/**
		 * When the user presses square buttons the board state is modified
		 * accordingly.
		 */
		@Override
		public void actionPerformed(ActionEvent ae) {
			for (int i = 0; i < currSize * currSize; i++) {
				if (ae.getSource() == squareButtons[i]) {
					KnightsPathController contr = (KnightsPathController) getController();
					XYLocation loc = new XYLocation(i % currSize, i / currSize);
					contr.modifySquare(loc);
				}
			}
		}
	}

	/**
	 * Defines how to react on standard simulation button events.
	 */
	protected static class KnightsPathController extends AgentAppController {

		protected KnightsPathEnvironment env = null;
		protected SearchAgent agent = null;
		protected boolean boardDirty;
		protected boolean knightStartPositionSet;
		protected boolean knightGoalPositionSet;

		/** Prepares next simulation. */
		@Override
		public void clear() {
			prepare(null);
		}

		/**
		 * Creates an n-KnightsPath environment and clears the current search agent.
		 */
		@Override
		public void prepare(String changedSelector) {
			AgentAppFrame.SelectionState selState = frame.getSelection();
			KnightsPathBoard board = null;
			switch (selState.getValue(KnightsPathFrame.ENV_SEL)) {
			case 0: // 4 x 4 board
				board = new KnightsPathBoard(6);
				break;
			case 1: // 8 x 8 board
				board = new KnightsPathBoard(8);
				break;
			case 2: // 8 x 8 board
				board = new KnightsPathBoard(10);
				break;
			case 3: // 32 x 32 board
				board = new KnightsPathBoard(12);
				break;
			}
			env = new KnightsPathEnvironment(board);

			boardDirty = false;
			knightStartPositionSet = false;
			knightGoalPositionSet = false;
			agent = null;
			frame.getEnvView().setEnvironment(env);
		}

		/**
		 * Creates a new search agent and adds it to the current environment if
		 * necessary.
		 */
		protected void addAgent() throws Exception {
			if (agent != null && agent.isDone()) {
				env.removeAgent(agent);
				agent = null;
			}
			if (agent == null) {
				
				int sSel = frame.getSelection().getValue(KnightsPathFrame.SEARCH_SEL);
				
				ActionsFunction af = KnightsPathFunctionFactory.getActionsFunction();
				
				Problem problem = new Problem(env.getBoard(), af,
						KnightsPathFunctionFactory.getResultFunction(),
						new KnightsPathGoalTest());
				
				Search search = SEARCH_ALGOS.get(sSel);
				agent = new SearchAgent(problem, search);
				env.addAgent(agent);
			}
		}

		/** Checks whether simulation can be started. */
		@Override
		public boolean isPrepared() {
			//int problemSel = frame.getSelection().getValue(KnightsPathFrame.PROBLEM_SEL);
			return (agent == null || !agent.isDone()) && (!boardDirty);
		}

		/** Starts simulation. */
		@Override
		public void run(MessageLogger logger) {
			logger.log("<simulation-log>");
			try {
				addAgent();
				while (!agent.isDone() && !frame.simulationPaused()) {
					Thread.sleep(200);
					env.step();
				}
			} catch (InterruptedException e) {
				// nothing to do...
			} catch (Exception e) {
				e.printStackTrace(); // probably search has failed...
			}
			logger.log(getStatistics());
			logger.log("</simulation-log>\n");
		}

		/** Executes one simulation step. */
		@Override
		public void step(MessageLogger logger) {
			try {
				addAgent();
				env.step();
			} catch (Exception e) {
				e.printStackTrace(); // probably search has failed...
			}
		}

		/** Updates the status of the frame after simulation has finished. */
		public void update(SimulationThread simulationThread) {
			if (simulationThread.isCanceled()) {
				frame.setStatus("Task canceled.");
			} else if (frame.simulationPaused()) {
				frame.setStatus("Task paused.");
			} else {
				frame.setStatus("Task completed.");
			}
		}

		/** Provides a text with statistical information about the last run. */
		private String getStatistics() {
			StringBuffer result = new StringBuffer();
			Properties properties = agent.getInstrumentation();
			Iterator<Object> keys = properties.keySet().iterator();
			while (keys.hasNext()) {
				String key = (String) keys.next();
				String property = properties.getProperty(key);
				result.append("\n" + key + " : " + property);
			}
			return result.toString();
		}

		public void modifySquare(XYLocation loc) {
			boardDirty = false;
			String atype;
			
			/* set the action user actions*/
			if(!knightStartPositionSet){
				atype = KnightsPathAction.PLACE_KNIGHT_START;
				knightStartPositionSet = true;
			}else{
				atype = KnightsPathAction.PLACE_KNIGHT_GOAL;
				knightGoalPositionSet = true;
			}

			env.executeAction(null, new KnightsPathAction(atype, loc));
			agent = null;
			frame.updateEnabledState();
		}
	}

	/** Simple environment maintaining just the current board state. */
	public static class KnightsPathEnvironment extends AbstractEnvironment {
		KnightsPathBoard board;

		public KnightsPathEnvironment(KnightsPathBoard board) {
			this.board = board;
		}

		public KnightsPathBoard getBoard() {
			return board;
		}

		/**
		 * Executes the provided action and returns null.
		 */
		@Override
		public EnvironmentState executeAction(Agent agent, Action action) {
			if (action instanceof KnightsPathAction) {
				KnightsPathAction act = (KnightsPathAction) action;
				XYLocation loc = new XYLocation(act.getX(), act.getY());
				
				
				if (act.getName() == KnightsPathAction.PLACE_KNIGHT_START)
					board.addKnightStartAt(loc);
				else if (act.getName() == KnightsPathAction.PLACE_KNIGHT_GOAL)
					board.addKnightGoalAt(loc);
				else if (act.getName() == KnightsPathAction.MOVE_KNIGHT)
					board.addKnightAt(loc);
				if (agent == null)
					updateEnvironmentViewsAgentActed(agent, action, null);
			}
			return null;
		}

		/** Returns null. */
		@Override
		public EnvironmentState getCurrentState() {
			return null;
		}

		/** Returns null. */
		@Override
		public Percept getPerceptSeenBy(Agent anAgent) {
			return null;
		}
	}
}
