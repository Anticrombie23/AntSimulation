
public class AntSimDriver {

	public static void main(String[] args) {
		
		//Create the GUI
		AntSimGUI gui = new AntSimGUI();	
		
		//Create the Sim class
		Simulation sim = new Simulation(gui);
		
		//add the listener
		gui.addSimulationEventListener(sim);
	

	}

}
