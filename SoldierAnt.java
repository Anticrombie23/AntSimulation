import java.util.ArrayList;
import java.util.Random;

public class SoldierAnt extends AntInsect {
	
	//double hitMissFrequency = 0.5;
	int integerID;	
	int age;	
	ColonyNode node;
	boolean attackMode;
	
	SoldierAnt(ColonyNode node){
		age = 0;
		this.node = node;
		turnComplete = false;
		attackMode = false;
	}
	
	public void die(){
		node.removeAntList(this);
	}

	@Override
	void performAction(int turnNumber) {
		
		//check to see if this ant is old, if old..die
		if (age == LIFE_SPAN){
			die();
		}else {
			
			if (turnComplete == false){
				
				//increment age
				age++;
				
				//Update near node for this node if we haven't already for bala checks
				if (node.adjacentNodeGrid.size() == 0){
					node.updateNearNodes();	
				}	
				
				//Check this node for bala's to see if we are in attack mode, first
				if (node.balaCount > 0){	
					//Find a bala ant in this node to attack
					antAttack();				
				}else {
					//we will perform bala search. If no Bala found, move random			
					balaSearchAndMove();			
				}	
				
				turnComplete = true;	
			}
			
						
		}				
	}
	
	
	public void antAttack(){
		//Get 50/50 change to kill a Bala ant
		Random rand = new Random();		
		int change = rand.nextInt(1) + 1;
		
		//Bala ant will die!
		if (change == 1){
			
			//Find the bala ant we will kill
			for (int i = 0; i < node.antArrayList.size(); i++){
				
				if (node.antArrayList.get(i) instanceof BalaAnt){
					//kill the bala ant in question
					node.antArrayList.get(i).die();
					node.antArrayList.get(i).setTurnComplete(true);
					
					//increment total kills!
					node.getColony().sim.stats.setTotalKills(node.getColony().sim.stats.getTotalKills() + 1);
					
				}
				
			}
			
		}
		
	}
	
	public void balaSearchAndMove(){
		
				//Grab adjacent node point array
				ArrayList<NodePoint> adjacentNodes = node.adjacentNodeGrid;
				
				//variable for holding each nodePoints balaCount. 
				int nodeBalaCount = 0;
				
				//For every node in the adjacent nodes, lets check for Bala's. If we get a hit, move
				for(NodePoint nodePoint: adjacentNodes){			
					
					//for each adjacent node, get their view's bala count. 
					nodeBalaCount = node.getColony().colonyBoard[nodePoint.x][nodePoint.y].balaCount;
					
					//if > 1, move to node, else skip
					if (nodeBalaCount > 0){
						
						move(nodePoint);
						break;
					}
					
					
				}
				
				if (nodeBalaCount == 0){
					moveRandom();
				}
		
	}
	
		
	
	public void move(NodePoint nodePoint){
		
		//Only move into the new node if it is visible, else do nothing 
		if (node.getColony().colonyBoard[nodePoint.x][nodePoint.y].isNodeVisible() == true){
			//remove ant from current node
			node.removeAntList(this);
			
			//perform move
			this.node = node.getColony().colonyBoard[nodePoint.x][nodePoint.y];
			this.node.searchingAntsList = true;
			node.addAntToLists(this);
			node.updateListsAndViews();
		}		
			 
	}
	
	public void moveRandom(){
		//Lets get the size of the near node array, and choose one random from that
		int arraySize = node.adjacentNodeGrid.size();
				
		//pick a random node from the near nodes to enter (one through 6) 
		Random rand = new Random();		
		int thisRandom = rand.nextInt(arraySize);
		
		NodePoint randomNode = node.adjacentNodeGrid.get(thisRandom);
		
		move(randomNode);
		
	}


	@Override
	int getIntegerID() {
		// TODO Auto-generated method stub
		return integerID;
	}
	
	public void setIntegerID(int id){
		integerID = id;
	}
	
	@Override
	int getAge() {
		// TODO Auto-generated method stub
		return age;
	}


	@Override
	void setAge(int age) {
		this.age = age;
		
	}

	@Override
	void setTurnComplete(boolean complete) {
		
		turnComplete = complete;
		
	}
	
	
	
	

}
