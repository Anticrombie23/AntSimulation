import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;

public class BalaAnt extends AntInsect{
	
	
	int integerID;	
	int age;
	
	ColonyNode node;
	
		
	BalaAnt(ColonyNode node){
		age = 0;
		this.node = node;
		turnComplete = false;
		
	}

	@Override
	void performAction(int turnNumber) {
		// TODO Auto-generated method stub
		
		//Ant dies at one year
		if (age == LIFE_SPAN){
			
			//Ant dies
			die();
			
		}else {
			
			if (turnComplete == false){
				
				//increment age
				age++;
				
				//update near nodes for all ant type checks if it hasn't been yet 
				if (node.adjacentNodeGrid.size() == 0){
					node.updateNearNodes();	
				}	
				
				//check all node counts, if there are ants, attack
				if (node.isQueenNode || node.soldierCount > 0 || node.foragerCount > 0 || node.scoutCount > 0){
					
					antAttack();
					
				}else {
					
					moveRandom();
					
				}
				
				
				
			}		
			
			
		}
		
		
		turnComplete = true;
		
	}
	
	public void antAttack(){
		//Get 50/50 change to kill a Bala ant
		Random rand = new Random();		
		int change = rand.nextInt(2) + 1;
		
		//Bala ant will die!
		if (change == 1){
			
			//create new list to iterate through
			ArrayList<AntInsect> toIterate = node.antArrayList;
			
			//Find the (ant) we will kill. Don't kill bala, hence can't kill self
			for (int i = 0; i < toIterate.size(); i++){
				
				if (toIterate.get(i) instanceof QueenAnt){
					//kill the Queen first
					node.colony.queenLiving = false;
					node.colony.sim.simulationActive = false;
					//Queen has been killed
					System.out.println("Queen has been killed! Simulation is complete");
					//increment total kills!
					node.getColony().sim.stats.setTotalKills(node.getColony().sim.stats.getTotalKills() + 1);
					//Only kill one ant, break after kill
					break;
				}else if (toIterate.get(i) instanceof ScoutAnt){
					//Kill the scout second
					node.antArrayList.get(i).die();
					node.antArrayList.get(i).setTurnComplete(true);
					//increment total kills!
					node.getColony().sim.stats.setTotalKills(node.getColony().sim.stats.getTotalKills() + 1);
					//Only kill one ant, break after kill
					break;
				}else if (toIterate.get(i) instanceof SoldierAnt){
					node.antArrayList.get(i).die();
					node.antArrayList.get(i).setTurnComplete(true);
					//increment total kills!
					node.getColony().sim.stats.setTotalKills(node.getColony().sim.stats.getTotalKills() + 1);
					//Only kill one ant, break after kill
					break;
				}else if (toIterate.get(i) instanceof ForagerAnt){
					
				//	ForagerAnt ant = (ForagerAnt) node.antArrayList.get(i);
					
					node.antArrayList.get(i).die();
					node.antArrayList.get(i).setTurnComplete(true);
					//increment total kills!
					node.getColony().sim.stats.setTotalKills(node.getColony().sim.stats.getTotalKills() + 1);
					//Only kill one ant, break after kill
					break;
				}
				
			}
			
		}
		
	}
	
	
	
	



public void move(NodePoint nodePoint){

//ant will always move randomly

	//remove ant from current node
	node.removeAntList(this);
	
	
	try{
		
		this.node = node.getColony().colonyBoard[nodePoint.x][nodePoint.y];
		this.node.searchingAntsList = true;
		node.addAntToLists(this);
		node.updateListsAndViews();	
		
	}catch(ArrayIndexOutOfBoundsException e){
		e.printStackTrace();
		JOptionPane.showMessageDialog(null,
				"Array Index Out of Bounds exception: " +
		"\n X value: " + nodePoint.x + 
		"\n Y value: " + nodePoint.y
				);
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
	
public void die(){
	node.removeAntList(this);
}
	
	public void setIntegerID(int id){
		integerID = id;
	}


	@Override
	int getIntegerID() {
		// TODO Auto-generated method stub
		return integerID;
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
		// TODO Auto-generated method stub
		turnComplete = complete;
	}

}
