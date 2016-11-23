
public class SimulationStats {
	
	int numbAnts;
	int numbBala;
	int numbForager;
	int numbScout;
	int numbSoldier;
	int totalKills;
	
	SimulationStats(int numbAnts, int numbBala, int numbForager, int numbScout, int numbSoldier){
		
		this.numbAnts = numbAnts;
		this.numbBala = numbBala;
		this.numbForager = numbForager;
		this.numbScout = numbScout;
		this.numbSoldier = numbSoldier;
		totalKills = 0;
	}

	public int getNumbAnts() {
		return numbAnts;
	}

	public void setNumbAnts(int numbAnts) {
		this.numbAnts = numbAnts;
	}

	public int getNumbBala() {
		return numbBala;
	}

	public void setNumbBala(int numbBala) {
		this.numbBala = numbBala;
	}

	public int getNumbForager() {
		return numbForager;
	}

	public void setNumbForager(int numbForager) {
		this.numbForager = numbForager;
	}

	public int getNumbScout() {
		return numbScout;
	}

	public void setNumbScout(int numbScout) {
		this.numbScout = numbScout;
	}

	public int getNumbSoldier() {
		return numbSoldier;
	}

	public void setNumbSoldier(int numbSoldier) {
		this.numbSoldier = numbSoldier;
	}

	public int getTotalKills() {
		return totalKills;
	}

	public void setTotalKills(int totalKills) {
		this.totalKills = totalKills;
	}
	
	

}
