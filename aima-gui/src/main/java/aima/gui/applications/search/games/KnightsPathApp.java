package aima.gui.applications.search.games;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.swing.JButton;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.Environment;
import aima.core.agent.EnvironmentState;
import aima.core.agent.Percept;
import aima.core.agent.impl.AbstractEnvironment;
import aima.core.environment.nqueens.AttackingPairsHeuristic;
import aima.core.environment.nqueens.NQueensBoard;
import aima.core.environment.nqueens.NQueensFunctionFactory;
import aima.core.environment.nqueens.NQueensGoalTest;
import aima.core.environment.nqueens.QueenAction;
import aima.core.search.framework.ActionsFunction;
import aima.core.search.framework.GraphSearch;
import aima.core.search.framework.Problem;
import aima.core.search.framework.Search;
import aima.core.search.framework.SearchAgent;
import aima.core.search.framework.TreeSearch;
import aima.core.search.informed.AStarSearch;
import aima.core.search.local.HillClimbingSearch;
import aima.core.search.local.Scheduler;
import aima.core.search.local.SimulatedAnnealingSearch;
import aima.core.search.uninformed.BreadthFirstSearch;
import aima.core.search.uninformed.DepthFirstSearch;
import aima.core.search.uninformed.DepthLimitedSearch;
import aima.core.search.uninformed.IterativeDeepeningSearch;
import aima.core.util.datastructure.XYLocation;
import aima.gui.applications.search.games.NQueensApp.NQueensController;
import aima.gui.applications.search.games.NQueensApp.NQueensEnvironment;
import aima.gui.applications.search.games.NQueensApp.NQueensFrame;
import aima.gui.applications.search.games.NQueensApp.NQueensView;
import aima.gui.framework.AgentAppController;
import aima.gui.framework.AgentAppEnvironmentView;
import aima.gui.framework.AgentAppFrame;
import aima.gui.framework.MessageLogger;
import aima.gui.framework.SimpleAgentApp;
import aima.gui.framework.SimulationThread;

public class KnightsPathApp extends SimpleAgentApp {
	
	/** List of supported algorithms */
	protected static List<String> SEARCH_NAMES = new ArrayList<String>();
	/** List of supported search algorithms. */
	protected static List<Search> SEARCH_ALGOS = new ArrayList<Search>();
	
	/** Adds a new item to the list of supported search algorithms. */
	public static void addSearchAlgorithm(String name, Search algo) {
		SEARCH_NAMES.add(name);
		SEARCH_ALGOS.add(algo);
	}
	
	static {
		addSearchAlgorithm("A* search (attacking pair heuristic)",
				new AStarSearch(new GraphSearch(), new AttackingPairsHeuristic()));
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
	
	/** Returns a <code>NQueensController</code> instance. */
	@Override
	public AgentAppController createController() {
		return new KnightsPathController();
	}
	
	
	protected static class KnightsPathController extends AgentAppController {
		
		protected SearchAgent agent = null;
		protected boolean boardDirty;
		
		/** Updates the status of the frame after simulation has finished. */
		public void update(SimulationThread simulationThread){
			if (simulationThread.isCanceled()) {
				frame.setStatus("Task canceled.");
			} else if (frame.simulationPaused()) {
				frame.setStatus("Task paused.");
			} else {
				frame.setStatus("Task completed.");
			}
		}
		
		/** Starts simulation. */
		@Override
		public void run(MessageLogger logger) {
			logger.log("<simulation-log>");
			try {
				//addAgent();
				while (!agent.isDone() && !frame.simulationPaused()) {
					Thread.sleep(200);
					//env.step();
				}
			} catch (InterruptedException e) {
				// nothing to do...
			} catch (Exception e) {
				e.printStackTrace(); // probably search has failed...
			}
			//logger.log(getStatistics());
			logger.log("</simulation-log>\n");
		}

		/** Executes one simulation step. */
		@Override
		public void step(MessageLogger logger) {
			try {
				//addAgent();
				//env.step();
			} catch (Exception e) {
				e.printStackTrace(); // probably search has failed...
			}
		}
		
		/**
		 * Creates an n-queens environment and clears the current search agent.
		 */
		@Override
		public void prepare(String changedSelector) {
			
		}
		
		/** Prepares next simulation. */
		@Override
		public void clear() {
			prepare(null);
		}
		
		/** Checks whether simulation can be started. */
		@Override
		public boolean isPrepared() {
			//int problemSel = frame.getSelection().getValue(NQueensFrame.PROBLEM_SEL);
			//return problemSel == 1 || (agent == null || !agent.isDone()) && (!boardDirty || env.getBoard().getNumberOfQueensOnBoard() == 0);
			return true;
		}
		
		
	}
	
	protected static class KnightsPathFrame extends AgentAppFrame {
		private static final long serialVersionUID = 1L;
		public static String ENV_SEL = "EnvSelection";
		public static String PROBLEM_SEL = "ProblemSelection";
		public static String SEARCH_SEL = "SearchSelection";

		public KnightsPathFrame() {
			setTitle("Knights Path Application");
			/** setSelectors(new String[] { ENV_SEL, PROBLEM_SEL, SEARCH_SEL },
					new String[] { "Select Environment",
							"Select Problem Formulation", "Select Search" });
			setSelectorItems(ENV_SEL, new String[] { "4 Queens", "8 Queens",
					"16 Queens", "32 Queens" }, 1);
			setSelectorItems(PROBLEM_SEL, new String[] { "Incremental",
					"Complete-State" }, 0);
			setSelectorItems(SEARCH_SEL, (String[]) SEARCH_NAMES
					.toArray(new String[] {}), 0); **/
			setEnvView(new NQueensView());
			setSize(800, 600);
		}
	} 
	
	protected static class KnightsPathView extends AgentAppEnvironmentView implements ActionListener {
		
		private static final long serialVersionUID = 1L;
		protected JButton[] squareButtons;
		protected int currSize = -1;
		
		protected KnightsPathView(){
			
		}
		
		@Override
		public void agentAdded(Agent agent, EnvironmentState resultingState) {
			//showState();
		}
		
		/** Agent value null indicates a user initiated action. */
		@Override
		public void agentActed(Agent agent, Action action, EnvironmentState resultingState) {
			//showState();
			notify((agent == null ? "User: " : "") + action.toString());
		}
		
		/**
		 * When the user presses square buttons the board state is modified
		 * accordingly.
		 */
		@Override
		public void actionPerformed(ActionEvent ae) {
			
		}
	}

}