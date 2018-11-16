package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class FormController {

	//FormのGUI関連の処理

	/**
	 * csvファイルを読み込んでグラフにプロットする
	 * @param event
	 */
	@FXML
	void onReadAndPlotAction(ActionEvent event){

	}

	/**
	 * グラフの再読み込みを行う
	 * @param event
	 */
	@FXML
	void onReloadAction(ActionEvent event) {

	}

	/**
	 * 読み込んだデータを消去する
	 * @param event
	 */
	@FXML
	void onAllClearAction(ActionEvent event){

	}

	/**
	 * Config画面を開く
	 * @param event
	 */
	@FXML
	void onConfigAction(ActionEvent event) {
		try {
			showConfigWindow();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	private void showConfigWindow() throws IOException {

		FXMLLoader loader = new FXMLLoader(getClass().getResource("Config.fxml"));
		AnchorPane root = (AnchorPane) loader.load();
		Scene scene = new Scene(root,800,450);
		Stage stage = new Stage();
		stage.setTitle("Config");
		stage.setScene(scene);
		stage.showAndWait();

	}

}
