package pl.polsl.woleszko.fuels.route.utils;

import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import javafx.beans.value.ObservableValue;

public class StatusChecker {
	private static DoubleProperty currentAnalysisStatus;
	private static StatusChecker instance;

	private StatusChecker() {
		currentAnalysisStatus = new SimpleDoubleProperty(0);
	}

	public static StatusChecker getInstance() {
		if (instance == null) {
			instance = new StatusChecker();
		}
		return instance;
	}

	public void dataAqusitionDoneStatus() {
		if (currentAnalysisStatus.getValue() < 0.4) {

			currentAnalysisStatus.set(0.4);
		}
	}

	public void varSummingDoneStatus() {
		if (currentAnalysisStatus.getValue() < 0.6) {
			currentAnalysisStatus.set(0.6);
		}
	}

	public void cvSummingDoneStatus() {
		if (currentAnalysisStatus.getValue() < 0.75) {
			currentAnalysisStatus.set(0.75);
		}
	}

	public void tubeGenerationDoneStatus() {
		if (currentAnalysisStatus.getValue() < 0.85) {
			currentAnalysisStatus.set(0.85);
		}
	}

	public void analysisDoneStatus() {
		if (currentAnalysisStatus.getValue() < 0.95) {
			currentAnalysisStatus.set(0.95);
		}
	}

	public void doneStatus() {
		if (currentAnalysisStatus.getValue() < 100) {
			currentAnalysisStatus.set(1);
		}
	}
	
	public void resetStatus() {
		currentAnalysisStatus.set(0);
	}

	public DoubleProperty getCurrentAnalysisStatus() {		
		return currentAnalysisStatus;
	}

}
