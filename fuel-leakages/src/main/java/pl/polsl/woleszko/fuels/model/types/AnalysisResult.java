package pl.polsl.woleszko.fuels.model.types;

import java.util.Date;

import javafx.beans.property.SimpleStringProperty;
import lombok.Getter;
import lombok.Setter;

//@Getter
//@Setter
public class AnalysisResult {
//	Date firstDate;
//	Date lastDate;
//	String decision;
//	Double leakRate;
//	Integer tankID;
	
	SimpleStringProperty periodP;
	SimpleStringProperty decisionP;
	SimpleStringProperty leakRateP;
	SimpleStringProperty tankIDP;
	
		
	
	public AnalysisResult(String first, String last, String decision, Double leakRate, Integer tankID){
//		this.firstDate = first;
//		this.lastDate = last;
//		this.decision = decision;
//		this.leakRate = leakRate;
//		this.tankID = tankID;
		
		this.periodP = new SimpleStringProperty(first +" - "+ last);
		this.decisionP = new SimpleStringProperty(decision);
		this.leakRateP = new SimpleStringProperty(leakRate.toString());
		this.tankIDP = new SimpleStringProperty(tankID.toString());
	}
	
	public SimpleStringProperty periodPProperty() {
		return periodP;
	}



	public SimpleStringProperty decisionPProperty() {
		return decisionP;
	}



	public SimpleStringProperty leakRatePProperty() {
		return leakRateP;
	}



	public SimpleStringProperty tankIDPProperty() {
		return tankIDP;
	}

	
	
	
	
}
