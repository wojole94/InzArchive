package pl.polsl.woleszko.fuels.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import pl.polsl.woleszko.fuels.model.types.AnalysisResult;
import pl.polsl.woleszko.fuels.model.utils.ResultHandler;
import pl.polsl.woleszko.fuels.route.utils.StatusChecker;

public class ProgressPaneController {

	@FXML
	ProgressBar PBprogress;
	@FXML
	Text TdataAq;
	@FXML
	Text Tvars;
	@FXML
	Text Tcvs;
	@FXML
	Text TgenTubs;
	@FXML
	Text Tanalysis;
	@FXML
	Text Tdone;
	@FXML
	Button BTresults;
	@FXML
	TableView<AnalysisResult> resultsTable;
	@FXML
	private TableColumn<AnalysisResult, String> colPeriod;
	@FXML
	private TableColumn<AnalysisResult, String> colTankID;
	@FXML
	private TableColumn<AnalysisResult, String> colDecision;
	@FXML
	private TableColumn<AnalysisResult, String> colLeakRate;

	@FXML
	public void initialize() {
		// PBprogress.progressProperty().bind(StatusChecker.getInstance().getCurrentAnalysisStatus());
		// StatusChecker.getInstance().dataAqusitionDoneStatus();
		
		StatusChecker.getInstance().getCurrentAnalysisStatus().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				PBprogress.setProgress(newValue.doubleValue());
					if (newValue.doubleValue() < 0.4) {
						TdataAq.underlineProperty().set(true);	
						TdataAq.strikethroughProperty().set(false);
						Tvars.strikethroughProperty().set(false);	
						Tcvs.strikethroughProperty().set(false);	
						TgenTubs.strikethroughProperty().set(false);	
						Tanalysis.strikethroughProperty().set(false);	
						Tdone.underlineProperty().set(false);
					}
					if (newValue.doubleValue() >= 0.4) {
						TdataAq.underlineProperty().set(false);
						TdataAq.strikethroughProperty().set(true);	
						Tvars.underlineProperty().set(true);
						
					}									

					if (newValue.doubleValue() >= 0.6) {
						Tvars.underlineProperty().set(false);
						Tvars.strikethroughProperty().set(true);	
						Tcvs.underlineProperty().set(true);
					}

					if (newValue.doubleValue() >= 0.75) {
						Tcvs.underlineProperty().set(false);
						Tcvs.strikethroughProperty().set(true);	
						TgenTubs.underlineProperty().set(true);
					}

					if (newValue.doubleValue() >= 0.85) {
						TgenTubs.underlineProperty().set(false);
						TgenTubs.strikethroughProperty().set(true);	
						Tanalysis.underlineProperty().set(true);
					}

					if (newValue.doubleValue() >= 0.95) {
						Tanalysis.underlineProperty().set(false);
						Tanalysis.strikethroughProperty().set(true);	
						Tdone.underlineProperty().set(true);
					}

					
			}
		});
		ObservableList<AnalysisResult> resultsList = ResultHandler.getInstance().getResultsList();
		resultsTable.setItems(resultsList);
		colPeriod.setCellValueFactory(new PropertyValueFactory<AnalysisResult, String>("periodP"));
		colTankID.setCellValueFactory(new PropertyValueFactory<AnalysisResult, String>("tankIDP"));;
		colDecision.setCellValueFactory(new PropertyValueFactory<AnalysisResult, String>("decisionP"));;
		colLeakRate.setCellValueFactory(new PropertyValueFactory<AnalysisResult, String>("leakRateP"));

	}

	public ProgressBar getPBprogress() {
		return PBprogress;
	}

}
