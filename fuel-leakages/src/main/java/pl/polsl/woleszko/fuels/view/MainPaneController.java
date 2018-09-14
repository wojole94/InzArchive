package pl.polsl.woleszko.fuels.view;

import java.io.File;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import pl.polsl.woleszko.fuels.route.RouteConfig;
import pl.polsl.woleszko.fuels.route.utils.StatusChecker;

public class MainPaneController {

	@FXML
	private TextField TFpathField;
	@FXML
	private TextField TFwindowSize;
	@FXML
	private TextField TFtolMin;
	@FXML
	private TextField TFtolMax;
	@FXML
	private TextField TFtolFac;
	@FXML
	private TextField TFk;
	@FXML
	private TextField TFTL;
	@FXML
	private TextField TFLL;
	@FXML
	private TextField TFtubeTreshold;

	@FXML
	protected void buttonBrowseDirectory(ActionEvent event) {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		File selectedDirectory = directoryChooser.showDialog(null);

		if (selectedDirectory == null) {
			TFpathField.setText("No Directory selected");
		} else {
			TFpathField.setText(selectedDirectory.getAbsolutePath());
		}
	}

	@FXML
	protected void buttonStartAnalysis(ActionEvent event) {
		try {
			Integer windowSize = Integer.parseInt(TFwindowSize.getText());

			Double tolMin = Double.parseDouble(TFtolMin.getText());
			Double tolMax = Double.parseDouble(TFtolMax.getText());
			Double tolFac = Double.parseDouble(TFtolFac.getText());
			Integer k = Integer.parseInt(TFk.getText());

			Double TL = Double.parseDouble(TFTL.getText());
			TL /= 100;
			if (TL < 0 || TL > 1)
				throw new NumberFormatException();

			Double LL = Double.parseDouble(TFLL.getText());
			LL /= 100;
			if (LL < 0 || LL > 1)
				throw new NumberFormatException();

			Double tubeTreshold = Double.parseDouble(TFtubeTreshold.getText());

			String outputPath = TFpathField.getText();

			// Logback configuration - package level grained
			((Logger) LoggerFactory.getLogger("pl.polsl.woleszko.fuels.route")).setLevel(Level.INFO);
			((Logger) LoggerFactory.getLogger("org.apache.camel")).setLevel(Level.DEBUG);

			RouteConfig app = new RouteConfig(windowSize,tolMin,tolMax,tolFac,TL,LL,tubeTreshold,k,outputPath);

			
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/ProgressPane.fxml"));
			//ProgressPaneController controller =new ProgressPaneController();
			//			loader.setController(controller);
			

			AnchorPane newPane = loader.load();
//			ProgressPaneController controller = new ProgressPaneController();
//			ProgressBar pb = loader.<ProgressPaneController>getController().getPBprogress();
//			StatusChecker.currentAnalysisStatus.addListener(new ChangeListener<Number>() {
//				@Override
//				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//					pb.setProgress((double) newValue);				}			
//			});
			
//			 pb.setProgress(0);
//		     pb.progressProperty().unbind();
//		     StatusChecker.currentAnalysisStatus = new SimpleDoubleProperty(0).asObject();
//		     pb.progressProperty().bind(StatusChecker.currentAnalysisStatus);	
		    //
			Stage stage = new Stage();
			Scene scene = new Scene(newPane);
			stage.setScene(scene);
			stage.show();
			
			

		} catch (NullPointerException ex) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Configuration Error");
			alert.setHeaderText("Incomplete configuration!");
			alert.setContentText("Please check fields above and try again...");
			alert.showAndWait();
		} catch (NumberFormatException ex) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Configuration Error");
			alert.setHeaderText("Invalid numeric format!");
			alert.setContentText("Please check fields above and try again...");
			alert.showAndWait();
		} catch (Exception ex) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Configuration Error");
			alert.setHeaderText("Cannot parse configuration!");
			alert.setContentText("Please check fields above and try again...");
			alert.showAndWait();
			ex.printStackTrace();
		}
	}

}
