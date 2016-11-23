import java.util.*;

import javax.swing.JOptionPane;


public class ForagerAnt extends AntInsect{
	
	Stack<NodePoint> journeyLocations;
	boolean inQueenSquare;
	boolean hasFood;
	
	boolean inPheroLoop;
		
	int integerID;
	int age;
	
	ColonyNode node;
	
	ForagerAnt(ColonyNode node){
		
		//Age setup + stack to maintain previous order. 
		journeyLocations = new Stack<NodePoint>();
		age = 0;
		
		//start out in queen square, but also with forage mode. Push the queen node point
		inQueenSquare = true;
		journeyLocations.push(new NodePoint(13, 13));
		hasFood = false;
		
		//standard node setup + turn start
		this.node = node;
		turnComplete = false;
		
		//Pheromone loops occasionally happen, getting foragers stuck
		inPheroLoop = false;
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
						
						//If we're in the queen square, we'll check if we have food
						if (inQueenSquare){
							
							//if we have food while in queen square, drop food!
							//Clear our journey stack as well
							if (hasFood){
								dropFood();								
								resetMovementHistory();
								
							}else {								
								//begin foraging because we don't have food + are in queen square
								forageMode();								
							}
							
							//not in queen square, check if we have food
						}else {
							// Check to forage or return to queen
							if (hasFood){								
								//journey towards Queen
								moveTowardsQueen();
							}
							else {
								forageMode();
							}
							
							
						}
						
					}					
					
				}
		
				
				turnComplete = true;
	}
	
	public void die(){
		node.removeAntList(this);
	}
	
	
	public void forageMode(){
		
		//update adjacent node grid
		if (node.adjacentNodeGrid.size() == 0){
			node.updateNearNodes();	
		}
		
		//get adjacent node with highest pheromone, else move random
		ColonyNode node = checkPhermoneContent();
		
		//check if we're in a pheromone loop.		
		if(inPheroLoop){
			//pick a random node other than the looped nodes 
			
			
			
		}else {
			
			//Mandatory check to see if null, else we will move towards node or random
			if (node != null){
				
				//pass the node to move to, then move to that node
				moveTowardsNodePheroOrRandom(node);
				
				//If food exists in our new node, lets pick it up and bring it back to the queen
				pickupFoodIfExists();
				
				
			}else {
				
				//Something bad happened
				JOptionPane.showMessageDialog(null, "A null forager node was encountered. ");
				
			}
			
		}
		
		
		
		
	}
	
	

	
	public ColonyNode checkPhermoneContent(){
		
		//Start by setting the first node as the node to compare
		//we will compare all other nodes to this node
		ColonyNode highestPhermoneNode = node.getColony().colonyBoard[node.adjacentNodeGrid.get(0).x][node.adjacentNodeGrid.get(0).y];
		
		//If there are multiple nodes with the same pheromone, we will have to pick one randomly
		//We will add nodes to this array, then pick one randomly
		ArrayList<ColonyNode> randomNodeToPick = new ArrayList<ColonyNode>();
		
		//Random generator for the randomNodeToPick Array
		Random rand = new Random();	
				
		//for every near node, lets check the pheromone content. If it's greater than current high node, 
		//the new high node is the current high node
		for(NodePoint currentNodePoints: node.adjacentNodeGrid){					
			
			//grab the current Colony Node from the node points
			ColonyNode currentNode = node.getColony().colonyBoard[currentNodePoints.x][currentNodePoints.y];
			
			//if we have two nodes that equal..we'll have to add it to an array to pick one randomly
			if (currentNode.getPheromone() == highestPhermoneNode.getPheromone()){
				
				randomNodeToPick.add(currentNode);
				
				
				//if the current nodes phermone is higher, then that is the new "highest"
			}else if(currentNode.getPheromone() > highestPhermoneNode.getPheromone()){
				
				highestPhermoneNode = currentNode;
				//We have a "highest" node. Clear out our random array
				randomNodeToPick.clear();
				//add the highest node to the random node
				randomNodeToPick.add(highestPhermoneNode);
				
			}
			
		}
		
		//if the random array only contains one node and the node is greater than 0
		//we can return this node, because it's the highest
				
		if(randomNodeToPick.size() == 1 && highestPhermoneNode.getPheromone() > 0){
			
			if (!journeyLocations.empty()){
				//we will still need to check if we're in a pheromone loop via the last two nodes traversed
				NodePoint point1 = journeyLocations.pop();
				NodePoint point2 = null;
				
				if (!journeyLocations.isEmpty()){
					point2 = journeyLocations.pop();
				}
										
				ColonyNode node1 = node.getColony().colonyBoard[point1.x][point1.y];
				ColonyNode node2 =  null;
				
				if (point2 != null){
					 node2 = node.getColony().colonyBoard[point2.x][point2.y];
					 journeyLocations.push(point2);
				}
				
				//push the most recent two back onto the stack
				
				journeyLocations.push(point1);
				
				//If there is pheromone and we've been to it recently
				if (highestPhermoneNode == node1 || highestPhermoneNode == node2){
					
					//return a random visible node that's not one of the above two recent nodes
					ColonyNode nodeToReturn = null;
					
					int counter = 0;
					while(nodeToReturn == node1 || nodeToReturn == node2 || counter != node.adjacentNodeGrid.size() - 2 ){
						
						NodePoint nodee = node.adjacentNodeGrid.get(counter);
						ColonyNode cn = node.getColony().colonyBoard[nodee.x][nodee.y];
						
						nodeToReturn = cn;
						
						counter++;
					}
					
				
					
					return nodeToReturn;
			}
			
			
				
			}else {
				return highestPhermoneNode;
			}		
		
		}else {
			
			//We will pick a node randomly from our list of nodes with 
			//same pheromone. Return this node 
			int randomNodeInt = rand.nextInt(randomNodeToPick.size() - 1);
			
			return randomNodeToPick.get(randomNodeInt);
		}
		return highestPhermoneNode;
		
	}
	
	//this class will adjust the node + move nodes
	public void moveTowardsNodePheroOrRandom(ColonyNode node){
		
		//Only move into the new node if it is visible, else do nothing 
				if (node.getColony().colonyBoard[node.colonyX][node.colonyY].isNodeVisible() == true){
					
					//remove ant from this current node
					this.node.removeAntList(this);
					
					//perform move
					this.node = node;
					this.node.searchingAntsList = true;
					node.addAntToLists(this);
					node.updateListsAndViews();
					
					//update the stack to the new position this ant has been to
					journeyLocations.push(new NodePoint(node.colonyX, node.colonyY));
					
					//check if this is the queens node. Update the variable as necessary
					if(node.isQueenNode() == true){
						inQueenSquare = true;
					}else {
						inQueenSquare = false;
					}
				}	
		
	}
	
	public void moveTowardsQueen(){
		
		//pop the current node off the stack, we're in it. Don't want to stay here
		if (!journeyLocations.isEmpty()){
			
			//check if we're in the queen node now
			ColonyNode newColonyNode = node.getColony().colonyBoard[journeyLocations.peek().x][journeyLocations.peek().y];
			
			//if we're in the queen square, update the queen square node variable
			if(newColonyNode.isQueenNode){
				inQueenSquare = true;
			}
			
			journeyLocations.pop();
			
			//if we're not in the queen node, deposit phermone
			if (!node.isQueenNode){
				//Deposit pheromone into our current node before we move
				depositPheromone();	
			}
			
			if (!journeyLocations.empty()){
				//Lets peek at the stack to get the previous node, we will move to this
				ColonyNode nodeToMoveTo = this.node.getColony().colonyBoard[journeyLocations.peek().x][journeyLocations.peek().y];
				
				//double check we're not in an invisible node if we are, throw an error (warning dialog, actually)
				if(nodeToMoveTo.getColony().colonyBoard[nodeToMoveTo.colonyX][nodeToMoveTo.colonyY].isNodeVisible() == true){
					//remove the ant from the current node
					this.node.removeAntList(this);
					
					//perform the move backwards
					this.node = nodeToMoveTo;
					this.node.searchingAntsList = true;
					this.node.addAntToLists(this);
					this.node.updateListsAndViews();
					
			}
			
			
				
				
				
				
				
			}
			
		}else {
			
		}
		
		
		
		
	}
	
	public void depositPheromone(){	
		//Drop phermone in this node if it's less than 1,000
		
		if (node.getPheromone() < 1000){
			
			node.setPheromone(node.getPheromone() + 10);
			
		}
	}
	
	public void pickupFoodIfExists(){
		
		//As long as we're not in the queen square, we can check
		if (!inQueenSquare){
			
			//if food exists here, lets pick it up!
			if (node.getFood() > 0){
				//We're holding food
				hasFood = true;
				//decrement food of the current node
				node.setFood( node.getFood() - 1);
				
			}
		}
		
		
		
	}
	
	
	public void dropFood(){
		//Update the food in queen node to +1, then it  no longer has food
		node.setFood(node.getFood() + 1);
		hasFood = false;
		
	}
	
	
	public void resetMovementHistory(){
		
		journeyLocations.clear();
		//should push the queen location onto the stack
		journeyLocations.push(new NodePoint(13, 13));
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
		// TODO Auto-generated method stub
		
		turnComplete = complete;
		
	}
	
	
	
	

}
