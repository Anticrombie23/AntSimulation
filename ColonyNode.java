import java.util.ArrayList;

public class ColonyNode {
	
	int pheromone;
	int food;	
	int colonyX, colonyY;	
	
	ArrayList<AntInsect> antArrayList;
	ArrayList<AntInsect> toRemoveList;
	ArrayList<AntInsect> toAddList;
	
	ColonyNodeView cnv;	
	String nodeID;
	boolean nodeVisible;
	boolean isQueenNode;
	
	Colony colony;
	
	boolean searchingAntsList;
	
	//Data counts for each insect
	int balaCount;
	int queenCount;
	int foragerCount;
	int scoutCount;
	int soldierCount;
	
	
	//Near node variables (trying this to see if this works)
	ArrayList<NodePoint> adjacentNodeGrid;
	
	
	
	public Colony getColony() {
		return colony;
	}

	public void setColony(Colony colony) {
		this.colony = colony;
	}
	
	
	public int getColonyX() {
		return colonyX;
	}

	public void setColonyX(int colonyX) {
		this.colonyX = colonyX;
	}

	public int getColonyY() {
		return colonyY;
	}

	public void setColonyY(int colonyY) {
		this.colonyY = colonyY;
	}

	public String getNodeID() {
		return nodeID;
	}

	public void setNodeID(String nodeID) {
		this.nodeID = nodeID;
	}

	public boolean isNodeVisible() {
		return nodeVisible;
	}

	public void setNodeVisible(boolean nodeVisible) {
		
		boolean visibility = nodeVisible;
		
		if (visibility == true){
			cnv.showNode();
			this.nodeVisible = true;
		}
		
		if (visibility == false){
			cnv.hideNode();
		}
		
				
	}

	public boolean isQueenNode() {
		return isQueenNode;
	}
	public void setQueenNode(boolean isQueenNode) {
		this.isQueenNode = isQueenNode;
		cnv.showQueenIcon();
	}

	public int getPheromone() {
		return pheromone;
	}

	public void setPheromone(int pheromone) {
		this.pheromone = pheromone;
		cnv.setPheromoneLevel(pheromone);
	}

	public int getFood() {
		return food;
	}

	public void setFood(int food) {
		this.food = food;
		
		//update the view
		cnv.setFoodAmount(food);
	}

	ColonyNode(ColonyNodeView cnv, int x, int y){
		this.cnv = cnv;		
		colonyX = x;
		colonyY = y;
		
		adjacentNodeGrid = new ArrayList<NodePoint>();
		
		//Initialize the node contents
		initializeNodeContents();		
		
	}
	
	
	
	public void initializeNodeContents(){
		cnv.setID(colonyX + ", " + colonyY);
		cnv.setBalaCount(0);
		cnv.setForagerCount(0);
		cnv.setSoldierCount(0);
		cnv.setScoutCount(0);
		
		pheromone = 0;
		cnv.setPheromoneLevel(pheromone);
		
	//	nodeVisible = false;
		isQueenNode = false;
		
		antArrayList = new ArrayList<AntInsect>();
		toRemoveList = new ArrayList<AntInsect>();
		toAddList = new ArrayList<AntInsect>();
		
	}
	

	
	public ColonyNodeView getColonyNodeView(){
		return cnv;
	}


//	public void addInsect(AntInsect ant){
//		antArrayList.add(ant);
//	}
	
	public void countAnts(){
		
		//Zero out our counters before we count
				queenCount = 0;
				balaCount = 0;
				foragerCount = 0;
				scoutCount = 0;
				soldierCount = 0;
	
	for (AntInsect ant: antArrayList){			
		
		if (ant instanceof QueenAnt){
			queenCount++;
		}
		
		if (ant instanceof BalaAnt){
			balaCount++;
		}
		
		if (ant instanceof ForagerAnt){
			foragerCount++;
		}
		
		if (ant instanceof ScoutAnt){
			scoutCount++;
		}
		if (ant instanceof SoldierAnt){
			soldierCount++;
		}
		
		
	}
		
		
		
	}
	
	
	public void nodeViewUpdate(){
			
			countAnts();
			
			if(queenCount > 0){
				cnv.showQueenIcon();
				cnv.setQueen(true);
			}
			
			if(balaCount > 0){
				cnv.setBalaCount(balaCount);
				cnv.showBalaIcon();
			}else {
				cnv.hideBalaIcon();
				cnv.setBalaCount(balaCount);
			}			
			
			
			if(foragerCount > 0){
				cnv.setForagerCount(foragerCount);
				cnv.showForagerIcon();
			}else {
				cnv.hideForagerIcon();
				cnv.setForagerCount(foragerCount);
			}
			
			
			if (scoutCount > 0){
				cnv.setScoutCount(scoutCount);
				cnv.showScoutIcon();
			}else {
				cnv.hideScoutIcon();
				cnv.setScoutCount(scoutCount);
			}
			
			
			if (soldierCount > 0){
				cnv.showSoldierIcon();
				cnv.setSoldierCount(soldierCount);
			}else {
				cnv.hideSoldierIcon();
				cnv.setSoldierCount(soldierCount);
			}
			
			setPheromone(pheromone);
			
			setFood(food);			
		
	}
	
	public void updateListsAndViews(){
		updateAddDeleteLists();
		nodeViewUpdate();
	}
	
	public void nextTurnNode(int currentTurn){
		
		if (currentTurn % 10 == 0){
			//set pheromone levels in this node to be half after a day (ten turns)
			setPheromone(pheromone / 2); 
		}
		
		
		
		//for each ant in the array, lets perform their action. We're "searching" through the lists
		searchingAntsList = true;
		
		
			for (AntInsect ant: antArrayList){
				
				ant.performAction(currentTurn);
				
			}
		
		searchingAntsList = false;
		
		
//		//Once a turn is complete, lets update ants in this node to ensure we remove/add
		updateAddDeleteLists();
//		
//		//update the view
		nodeViewUpdate();
		
	}
	
	@Override
	public String toString() {
		return "ColonyNode [food=" + food + ", nodeID=" + nodeID + ", nodeVisible=" + nodeVisible + "]";
	}

	public void addAntToLists(AntInsect ant){
		if (searchingAntsList == true){
			toAddList.add(ant);
		}else {
			antArrayList.add(ant);
		}
	}
	
	
	public void removeAntList(AntInsect ant){
		if (searchingAntsList == true){
			toRemoveList.add(ant);
		}else {
			antArrayList.remove(ant);
		}
	}
	
	
	public void updateAddDeleteLists(){
		
		//Add the Additions		
		antArrayList.addAll(toAddList);
		
		//Remove all in the removals
		antArrayList.removeAll(toRemoveList);
		
		//Zero out the helper arrays
		toAddList.clear();
		toRemoveList.clear();		
		
	}
	
	public void updateNearNodes(){
		
		//clear the array to start
		adjacentNodeGrid.clear();
		
		//Left of the current node
		
 		if (colonyX - 1 >= 0){
			NodePoint leftNode = new NodePoint(colonyX - 1, colonyY);
			adjacentNodeGrid.add(leftNode);
		}
		
		if (colonyX -1 >= 0 && colonyY -1 >= 0){
			NodePoint upperLeftNode = new NodePoint(colonyX - 1, colonyY - 1);
			adjacentNodeGrid.add(upperLeftNode);
		}
		
		if (colonyY -1 >= 0){
			NodePoint upNode = new NodePoint(colonyX, colonyY - 1);
			adjacentNodeGrid.add(upNode);
		}
		
		if (colonyY-1 >= 0 && colonyX +1 < 27){
			NodePoint upperRightNode = new NodePoint(colonyX + 1, colonyY - 1);
			adjacentNodeGrid.add(upperRightNode);
		}
		
		if (colonyX +1 < 27){
			NodePoint rightNode = new NodePoint(colonyX + 1, colonyY);
			adjacentNodeGrid.add(rightNode);
		}
		
		if (colonyX + 1 < 27 && colonyY +1 < 27){
			NodePoint lowerRightNode = new NodePoint(colonyX + 1, colonyY + 1);
			adjacentNodeGrid.add(lowerRightNode);
		}
		
		if (colonyY +1 < 27){
			NodePoint lowerNode = new NodePoint(colonyX, colonyY + 1);
			adjacentNodeGrid.add(lowerNode);
		}		
		
		if (colonyX - 1 >= 0 && colonyY +1 < 27){
			NodePoint lowerLeftNode = new NodePoint(colonyX - 1, colonyY +1);
			adjacentNodeGrid.add(lowerLeftNode);
		}
		
		
		
		
		
		
		
		
		
		
				
		
	}
	
	
		

}
