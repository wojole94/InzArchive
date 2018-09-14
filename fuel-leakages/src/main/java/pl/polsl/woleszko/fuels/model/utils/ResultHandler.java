package pl.polsl.woleszko.fuels.model.utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.polsl.woleszko.fuels.model.types.AnalysisResult;

public class ResultHandler {
	private ObservableList<AnalysisResult> resultsList;
	private static ResultHandler instance;
	
	ResultHandler(){
		resultsList = FXCollections.observableArrayList();
	}
	
	public void resultsGroupper(AnalysisResult result) {
		resultsList.add(result);
	}
	
	public static ResultHandler getInstance() {
		if(instance == null) {
			instance = new ResultHandler();
		}
		return instance;
	}
	
	public ObservableList<AnalysisResult> getResultsList() {
		return resultsList;
	}
}
