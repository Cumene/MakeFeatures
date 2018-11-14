package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class FormController {

	@FXML
	void onConfigAction(ActionEvent event) {
		try {
			showConfigWindow();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	void showConfigWindow() throws IOException {

		FXMLLoader loader = new FXMLLoader(getClass().getResource("Config.fxml"));
		BorderPane root = (BorderPane) loader.load();
		Scene scene = new Scene(root,500,500);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.showAndWait();

	}

}
