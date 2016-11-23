import java.util.ArrayList;
import java.util.Random;

public class ScoutAnt extends AntInsect {
	
	
	//boolean foundNodeFood = false;
	int integerID;
	int age;
	
	ColonyNode node;
	
	ScoutAnt(ColonyNode node){
		age = 0;
		this.node = node;
		turnComplete = false;
	}

	@Override
	void performAction(int turnNumber) {
		
		if (age == LIFE_SPAN){
			die();
		}else {
			if(turnComplete == false){
				
				//increment age
				age++;
				
				enterNearNode();
			}	
		}
				
		
		turnComplete = true;
	}
	
	public void die(){
		node.removeAntList(this);
	}
	
	public void enterNearNode(){
		//update the adjacentNodes arraylist for this node to get all near nodes that are 
		//not outside the boundary of the 27x27 grid (e.g -1, 0) 
		
		if (node.adjacentNodeGrid.size() == 0){
			node.updateNearNodes();	
		}		
		
		//Lets get the size of the near node array, and choose one random from that
		int arraySize = node.adjacentNodeGrid.size();
		
		//pick a random node from the near nodes to enter (one through 6) 
		Random rand = new Random();		
		int thisRandom = rand.nextInt(arraySize);
		
		//remove the ant from the current node, wait for turn GUI to update current nodes list + view so all 
		//ants are accounted for properly
		node.removeAntList(this);
	
		if (thisRandom > arraySize){
			System.out.println("what happened");
		}
		
		//This is the new node to enter
		int xValue = node.adjacentNodeGrid.get(thisRandom).x;
		int yValue = node.adjacentNodeGrid.get(thisRandom).y;
		
		
		//have this scout enter the new node
		try{
			this.node = node.getColony().colonyBoard[xValue][yValue];
		}
		catch(ArrayIndexOutOfBoundsException e){
			e.printStackTrace();
			
			String arrayContents = printAdjacentArrayForDebugging(node.adjacentNodeGrid);
			
			System.out.println("The array size was: " + arraySize
					+ "\n the random integer chosen was: " + thisRandom+
					"\n Array Contents were: " + arrayContents + "\nthe x Value was: " + xValue + "\nthe Y value was: " + yValue );
		}
		
		//We're searching the list, need to update
		this.node.searchingAntsList = true;
		node.addAntToLists(this);
		
		node.updateListsAndViews();
				
				
		//If node is not visible, open the new node
		if (node.nodeVisible == false){
			node.setNodeVisible(true);
			getFoodInNode();
			
		}
		
		
		
		
	}
	
	String printAdjacentArrayForDebugging(ArrayList<NodePoint> adjGrid){
		
		StringBuilder str = new StringBuilder();
		
		int counter = 1;
		str.append("The below are the adjacent nodes: \n");
		for(NodePoint node: adjGrid){
			
			str.append("Node" + counter + ": " + node.toString() +"\n");
			counter++;
		}
		
		return str.toString();
		
	}
	
	void getFoodInNode(){
		
		Random rand = new Random();
		
		//see if food will exist in this new square, 25% chance
		int foodExists = rand.nextInt(4) + 1;
		
		//Food exists, update with something between 500-1000
		if (foodExists == 4){
			
			int foodQuantity = rand.nextInt(1000)  + 1;
			
			while (foodQuantity <= 500){
				foodQuantity = rand.nextInt(1000) + 1;
			}
			
			node.setFood(foodQuantity);
			
			
			
		}
		
		
		
		
		
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
