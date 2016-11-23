
public abstract class AntInsect {
	
	public static int LIFE_SPAN = 3650;
	public int moveDirectionsCapable;
	public int integerID;
	public boolean turnComplete;
	public ColonyNode node;
	
	abstract void setTurnComplete(boolean complete);
	
		
	abstract int getIntegerID();
	abstract void setIntegerID(int integerID);
	
	abstract int getAge();
	abstract void setAge(int age);
	
	abstract void performAction(int turnNumber);
	
	public void die(){
		node.removeAntList(this);
	}
	
	

}
