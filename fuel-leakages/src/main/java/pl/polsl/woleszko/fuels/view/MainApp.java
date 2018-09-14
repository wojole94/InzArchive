package pl.polsl.woleszko.fuels.view;

import java.io.IOException;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MainApp extends Application {

	
	@Override
	public void start(Stage primaryStage) {

			FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/MainPane.fxml"));
			AnchorPane stackPane;
			try {
				stackPane = loader.load();

			Scene scene = new Scene(stackPane);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Leakage detection");
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			    @Override
			    public void handle(final WindowEvent arg0) {
				System.exit(0);
			    }
			});
			primaryStage.show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}
	public static void main(String[] args) {

		launch(args);
	}

}
