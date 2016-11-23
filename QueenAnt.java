import java.util.Random;

public class QueenAnt extends AntInsect {
	
	
	int hatchTypeFrequency;	
	int integerID = 0;	
	int numberAntsforIDs = 0;
	int lifeSpan = 20;
	int age;
	
	boolean queenLiving;
	
	ColonyNode node;
	
	
	QueenAnt(ColonyNode node){
		this.node = node;
		age = 0;
		queenLiving = true;
	}
	

	@Override
	void performAction(int turnNumber) {
		
		//If the queen is 365 * 10 * 20...20 years old, the simulation ends. She dies
		if (age == 73000){		
			
			queenDies();			
		} else {
			
			//increment age
			age++;
			
			//Consume 1 unit of food and then check to see if it's 0. If zero, she dies. 
			consumeFood();
			
			if (node.getFood() == 0){
				queenDies();
			}
			
			// if it's the first turn of the day
			 if (turnNumber % 10 == 0){
				 hatch();
			 }
			
		}
		
		
		
	}
	
	public void queenDies(){
		
		queenLiving = false;		
		
		// Simulation ends
		System.out.println("Simulation ending -- Queen has passed on by natural causes. There will be a mandatory 10 day grieving period");
		
		//Talk to the sim to end this
		node.getColony().getSim().simulationActive = false;
		
	}
	
	public void consumeFood(){
		
		//consume one unit of food
		node.setFood(node.getFood() - 1);
		
	}
	
	public void initialHatch(AntInsect ant){
		numberAntsforIDs++;
		ant.setIntegerID(numberAntsforIDs);
		node.addAntToLists(ant);
	}
	
	public void hatch() {
		
		
			
			AntInsect newAnt;
			
			Random rand = new Random();			
			int randomInt = rand.nextInt(4) + 1;
			
			//one in four will be a Scout
			if (randomInt == 1){
			numberAntsforIDs++;
			newAnt = new ScoutAnt(node);
			newAnt.setIntegerID(numberAntsforIDs);
			node.addAntToLists(newAnt);
			
				
			//One in four will be a Soldier
			}else if (randomInt == 2){
			numberAntsforIDs++;
			newAnt = new SoldierAnt(node);
			newAnt.setIntegerID(numberAntsforIDs);
			node.addAntToLists(newAnt);
			
			//50% will be Foragers	
			}else if (randomInt > 2){
			numberAntsforIDs++;
			newAnt = new ForagerAnt(node);
			newAnt.setIntegerID(numberAntsforIDs);
			node.addAntToLists(newAnt);
			}
			
       

    }

	@Override
	int getIntegerID() {
		// TODO Auto-generated method stub
		return integerID;
	}


	@Override
	void setIntegerID(int integerID) {
		// TODO Auto-generated method stub
		
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
		
	}

}
