
public class Colony {
	
		
	//This will be the board/grid
	ColonyNode[][] colonyBoard;	
	ColonyView cview;	
	
	boolean queenLiving;
	
	Simulation sim;
	
	
	public ColonyView getCview() {
		return cview;
	}

	Colony(ColonyView cview, Simulation sim){
		
		this.cview = cview;		
			
		//Sim just starting, queen is living
		queenLiving = true;
		
		//Set colony size
		colonyBoard = new ColonyNode[27][27];
		
		this.sim = sim;
		
	}
	
	public Simulation getSim() {
		return sim;
	}

	public void setSim(Simulation sim) {
		this.sim = sim;
	}

	public void initColony(){
		
		//For every x value in the grid board
		for (int i = 0; i < 27; i++){
			
			//for every y value in the grid board
			for (int j = 0; j < 27; j++){
				
				//If it's the middle grid spot, perform queen setup
				if (i == 13 && j == 13){
					
					queenNodeSetup();
					
				}else {
					
					normalNodeSetup(i, j);
					
				}		
				
			}
			
		}
		
		//unhide the nodes surrounding the queen
		colonyBoard[12][12].setNodeVisible(true);
		colonyBoard[13][12].setNodeVisible(true);
		colonyBoard[14][12].setNodeVisible(true);
		colonyBoard[12][13].setNodeVisible(true);
		colonyBoard[14][13].setNodeVisible(true);
		colonyBoard[12][14].setNodeVisible(true);
		colonyBoard[13][14].setNodeVisible(true);
		colonyBoard[14][14].setNodeVisible(true);
				
	}
	
	public void queenNodeSetup(){
		
		//create a view for the new colony node
		ColonyNodeView newCNV = new ColonyNodeView();
				
		//Create a new colony node
		ColonyNode cn = new ColonyNode(newCNV, 13, 13);	
		
		//Create the queen, all hail
		QueenAnt queen = new QueenAnt(cn);
		
		//Add the queen to the ant array in this node
		cn.addAntToLists(queen);		
				
		//Update this nodes model to provide initial queen constraints
		cn.setQueenNode(true);
		cn.setFood(1000);
		
					
		//Perform loops for initial ants near queen
		//soldier
		for (int i = 0; i < 10; i++){
			SoldierAnt soldier = new SoldierAnt(cn);
			queen.initialHatch(soldier);			
		}
		
		//Forager
		for (int i = 0; i < 50; i++){
			ForagerAnt forager = new ForagerAnt(cn);
			queen.initialHatch(forager);
		}
		
//		//Scout
		for(int i = 0; i < 4; i++){
			ScoutAnt scout = new ScoutAnt(cn);
			queen.initialHatch(scout);
			
		}
				
		//Add the queen to the board holder	
		colonyBoard[13][13] = cn;		
			
		//Add it to the colony view
		cview.addColonyNodeView(cn.getColonyNodeView(), 13, 13);	
		
		//set the node visible since it's queen node
		cn.setNodeVisible(true);	
		
		//Add the colony to the queen ant, only because it needs to talk to the sim to end if she dies
		cn.setColony(this);
		
		//Update the node view
		cn.nodeViewUpdate();
		
	}
	
	public void normalNodeSetup(int i, int j){
		//create a view for the new colony node
		ColonyNodeView newCNV = new ColonyNodeView();
		
		//Create a new colony node data model + Set node ID to the X, Y
		ColonyNode cn = new ColonyNode(newCNV, i, j);	
		cn.setColony(this);
		cn.setNodeID(i + ", " + j);
		
		//add the colony node view to the colony view
		cview.addColonyNodeView(cn.getColonyNodeView(), i, j);
					
		//Add the colony node (which contains it's own view) to the grid/board
		colonyBoard[i][j] = cn;
		
		
	}
	
	
	public void newTurn(int currentTurn){
		
		if (queenLiving == true){
			
			for (int i = 0; i < 27; i++){
				
				for (int j = 0; j < 27; j++){
					
					colonyBoard[i][j].nextTurnNode(currentTurn);					
										
				}
				
			}
		
			
		}
		
		
		
		
	}

	
	
	
	

}
