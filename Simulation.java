import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

public  class Simulation extends Thread implements SimulationEventListener  {
	
	//Sim width + height
	int width = 27;
	int height = 27;
	
	Colony colony;
	AntSimGUI gui;
	
	boolean simulationActive;
	
	int currentTurn;
	
	SimulationStats stats;
	
	static Timer timer;
	TimerTask task;
	
	Simulation(AntSimGUI gui){
		
		this.gui = gui;
		
		//create the colony, which contains it's own view
		colony = new Colony(new ColonyView(width, height), this);
		
		//Initialize the GUI
		gui.initGUI(colony.getCview());
		
		//We're currently Active
		simulationActive = true;
		
		//Sim Stats will start out at 0
		stats = new SimulationStats(0, 0, 0, 0, 0);
		
		//Start turns at zero, first turn will create everything as 1
		currentTurn = 0;
		
		timer = new Timer("MyTimer");
	}
	
	public void turnComplete(){
		
		for (int i = 0; i < 27; i++){
			
			for (int j = 0; j < 27; j++){
				
				ArrayList<AntInsect> arrayToTraverse = colony.colonyBoard[i][j].antArrayList;
				
				for(AntInsect ant: arrayToTraverse){
					ant.turnComplete = false;
				}
				
			}
		}
		
		
	}
	
	
	public void chanceAtBala(){
		
		//3% change of a Bala getting created in the 1, 1 grid slot
		Random rand = new Random();
		int balaChance = rand.nextInt(100) + 1;
		
		if (balaChance <= 3){
			
			BalaAnt bala = new BalaAnt(colony.colonyBoard[1][1]);
			colony.colonyBoard[1][1].addAntToLists(bala);
			colony.colonyBoard[1][1].updateListsAndViews();
			
		}
		
	}
	
	public void simStats(){
		
		
		int numberAntsTotal = 0;
		int numberBala = 0;
		int numberScout = 0;
		int numberForager = 0;
		int numberSoldier = 0;
		
		for (int i = 0; i < 27; i++){
			for (int j = 0; j < 27; j++){
				
				for(AntInsect ant: colony.colonyBoard[i][j].antArrayList){
					numberAntsTotal ++;
					
					if (ant instanceof BalaAnt){
						numberBala++;
					}
					if (ant instanceof ScoutAnt){
						numberScout++;
					}
					if (ant instanceof ForagerAnt){
						numberForager++;
					}
					if (ant instanceof SoldierAnt){
						numberSoldier++;
					}
					
				}
				
				
			}
		}
		
		stats.setNumbAnts(numberAntsTotal);
		stats.setNumbBala(numberBala);
		stats.setNumbForager(numberForager);
		stats.setNumbScout(numberScout);
		stats.setNumbSoldier(numberSoldier);
		
		
		StringBuilder str = new StringBuilder();
		
		str.append("Total Number of Ants: " + stats.getNumbAnts());
		str.append("\nTotal Bala: " + stats.getNumbBala());
		str.append("\nTotal Forager: " + stats.getNumbForager());
		str.append("\nTotal Scout: " + stats.getNumbScout());
		str.append("\nTotal Soldier: " + stats.getNumbSoldier());
		str.append("\nTotal Kills: " + stats.getTotalKills());
		
		JOptionPane.showMessageDialog(null, str.toString(), "Simulation Stats!", JOptionPane.PLAIN_MESSAGE);
		
		
	}
	
	void runMethodToExecute(String speed){
		//we'll get the speed in string, try to parse to int. If we can't, we'll default to a 3 second per turn sim
		int speedinSeconds;
		
		try{
		speedinSeconds = Integer.parseInt(speed);
		speedinSeconds = speedinSeconds * 1000;
		}catch(NumberFormatException e){
			JOptionPane.showMessageDialog(null, "You provided something other than an integer in seconds. Will default to 3 seconds per turn.");
			speedinSeconds =  3000;
		}
		
				
		//Schedule a timer to run the methods
		timer.scheduleAtFixedRate(new TimerTask() {
			
		
			
			@Override
			public void run() {
				
				
				//This runs every 3 seconds by default if something other than "seconds" is put in			
				
				
				if (simulationActive == false){
					System.out.println("Simulation has completed. You can now exit ");
					Thread.currentThread().interrupt();
					JOptionPane.showMessageDialog(null, "Queen has died. \nSimulation has completed. You can now exit. ");
					
					//Create sim stats 
					simStats();
					
					return;
				}else {
					
					//create a bala ant potentially
					chanceAtBala();
					//perform a turn for the entire colony
					colony.newTurn(currentTurn);
					//Update the current turn
					currentTurn++;
				}	
				
				//Turn is complete, lets set all ant's turn complete booleans
				//to false, so a new turn can begin
				turnComplete();
				
				
			}
		}, 30, speedinSeconds);
		
	}
	
	
	

	@Override
	public void simulationEventOccurred(SimulationEvent simEvent) {
		// TODO Auto-generated method stub
		
		if (simEvent.getEventType() == SimulationEvent.NORMAL_SETUP_EVENT){
			colony.initColony();
		}
		
		if (simEvent.getEventType() == SimulationEvent.RUN_EVENT){
			
			String speed = JOptionPane.showInputDialog(null, "How fast in seconds would you like the sim to proceed? ");
			 
			runMethodToExecute(speed);
			
		}
		
		if(simEvent.getEventType() == SimulationEvent.FORAGER_TEST_EVENT){
			
		}
		
		if (simEvent.getEventType() == SimulationEvent.QUEEN_TEST_EVENT){
			
			int balaCount = 0;
			//use this for counting bala ants in the current sim
			for (int i = 0; i < 27; i++){
				for (int j = 0; j < 27; j++){
					
					balaCount += colony.colonyBoard[i][j].balaCount;
					
				}
			}
			
			JOptionPane.showMessageDialog(null, "Number of Bala: " + balaCount);
			
		}
		if (simEvent.getEventType() == SimulationEvent.SCOUT_TEST_EVENT){
			
			
			
		}
		if (simEvent.getEventType() == SimulationEvent.SOLDIER_TEST_EVENT){
			
			//Use this button to create a Bala ant to test to see if Soldier will move towards + kill
			BalaAnt bala = new BalaAnt(colony.colonyBoard[15][15]);
			colony.colonyBoard[15][15].addAntToLists(bala);
			colony.colonyBoard[15][15].updateListsAndViews();
			
			
		}
		if (simEvent.getEventType() == SimulationEvent.STEP_EVENT){
			
			if (simulationActive == false){
				System.out.println("Simulation has completed. You can now exit ");
				Thread.currentThread().interrupt();				
				JOptionPane.showMessageDialog(null, "Queen has died. \nSimulation has completed. You can now exit. ");
				
				//Create sim stats 
				simStats();
				
				return;
			}else {
				
				//create a bala ant potentially
				chanceAtBala();
				//perform a turn for the entire colony
				colony.newTurn(currentTurn);
				//Update the current turn
				currentTurn++;
			}	
			
			//Turn is complete, lets set all ant's turn complete booleans
			//to false, so a new turn can begin
			turnComplete();
					
		}	
		
	}

}
