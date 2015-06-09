package dk.headnet;

import java.util.ArrayList;
import java.util.List;

public class LPVacations {
	private String email;
	private List<String[]> periods = new ArrayList<String[]>();
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public List<String[]> getPeriods() {
		return periods;
	}
	public void setPeriod(List<String[]> periods) {
		this.periods = periods;
	}
	
	public void addPeriod(String[] period) {
		this.periods.add(period);
	}

}
