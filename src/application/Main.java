package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class Main extends Application {
	//ここからスタート
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Form.fxml"));
			AnchorPane root = (AnchorPane)loader.load();
			Scene scene = new Scene(root,1280,720);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			//Controllerに必要なパーツを受け取る記述
			//http://hideoku.hatenablog.jp/entry/2013/06/07/205016

//			FormController formCon = (FormController) loader.getController();
//			LineChart lc = (LineChart)root.lookup("#graph_linechart");
//			NumberAxis nax = (NumberAxis)root.lookup("#graph_numberaxis_x");
//			NumberAxis nay = (NumberAxis)root.lookup("#graph_numberaxis_y");
//
//			formCon.setLineChart(lc);
//			formCon.setNumberAxisX(nax);
//			formCon.setNumberAxisY(nay);


			primaryStage.setTitle("Make Features");
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
