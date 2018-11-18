package application;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class FormController implements Initializable {
	//private ArrayList<FileData> filedata_array = new ArrayList<FileData>();

	//FormのGUI関連の処理を行うやつ

	//ここからフィールド宣言(グローバルは最低限)
	//file関連

	//pane関連

	//	Stage primaryStage;
	//
	//	public void setPrimaryStage(Stage stage) {
	//		primaryStage = stage;
	//	}
	//

	@FXML
	private SplitPane split_pane;

	@FXML
	private AnchorPane split_down_anchorpane;

	//graph関連
	@FXML
	private LineChart<Number, Number> graph_linechart;

	//	public void setLineChart(LineChart lc) {
	//		graph_linechart = lc;
	//	}

	@FXML
	private NumberAxis graph_numberaxis_x;
	//	public void setNumberAxisX(NumberAxis na) {
	//		graph_numberaxis_x = na;
	//	}

	@FXML
	private NumberAxis graph_numberaxis_y;
	//	public void setNumberAxisY(NumberAxis na) {
	//		graph_numberaxis_y = na;
	//	}

	private XYChart.Series<Number, Number> series;

	//ここまでフィールド宣言

	//initialize・・・初期化処理
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("Hello initialize");

//		series = new XYChart.Series<Number, Number>();
//		series.setName("p1");//系統名
//
//		series.getData().add(new XYChart.Data<Number, Number>(10, 22));
//		series.getData().add(new XYChart.Data<Number, Number>(30, 33));
//		series.getData().add(new XYChart.Data<Number, Number>(40, 66));
//		series.getData().add(new XYChart.Data<Number, Number>(60, 69));
//		series.getData().add(new XYChart.Data<Number, Number>(100, 77));
//
//
//		//split_pane = new SplitPane();
//
//		//split_down_anchorpane = new AnchorPane();
//
//		//graph_numberaxis_x = new NumberAxis();
//		//graph_numberaxis_y = new NumberAxis();
//
//		//graph_linechart = new LineChart<>(graph_numberaxis_x, graph_numberaxis_y);
//
//		//series = new XYChart.Series<>();
//
//		//graphパーツのインスタンス作成
//		graph_numberaxis_x = new NumberAxis();
//	    graph_numberaxis_y = new NumberAxis();
//
//		graph_numberaxis_x.setLabel("TimeStamp");//軸のラベル名
//		graph_numberaxis_y.setLabel("Value");
//		//graph_linechart = new LineChart<Number, Number>(graph_numberaxis_x, graph_numberaxis_y);
//		graph_linechart.getData().add(series);
//
//		graph_linechart.setTitle("Graph");//Graphのタイトル



		//個々参考にしてみたら？
		//http://hideoku.hatenablog.jp/entry/2013/06/07/205016
		//Scene graph_scene = new Scene(graph_linechart,800,450);


		//Stage graph_stage = new Stage();
		//graph_stage.setScene(graph_scene);
		//graph_stage.show();

		//split_down_anchorpane = new AnchorPane(graph_linechart);
	}

	/**
	 * csvファイルを読み込んでグラフにプロットする
	 * @param event
	 */
	@FXML
	void onOpenDataAction(ActionEvent event) {

		//ファイル選択するダイアログ
		FileChooser fc = new FileChooser();
		fc.setInitialDirectory(new File("..\\"));
		List<File> list_files;

		list_files = fc.showOpenMultipleDialog(null);

		if (list_files == null) {
			Alert alert = new Alert(AlertType.INFORMATION, "ファイルが選択されていません");
			alert.showAndWait();
			return;
		}

		File file = list_files.get(0);//将来的に複数にするけど，とりあえずプロットするため固定

		//FileDataのインスタンスを作成してデータimport
		FileData filedata = new FileData();
		filedata.import_file(file, 0);//とりあえずtimestampは0想定

		ArrayList<Long> list_timestamp = filedata.getFile_data_timestamp();
		ArrayList<Float>[] list_data_array = filedata.getFile_data_contents();
		String[] title_array = filedata.getFile_data_title();

		//graphパーツ  @FXMLで作成したパーツはNew宣言不要？　（詳しく調べる必要がある

		series = new XYChart.Series<Number, Number>();
		series.setName("p1");//系統名

		series.getData().add(new XYChart.Data<Number, Number>(10, 22));
		series.getData().add(new XYChart.Data<Number, Number>(30, 33));
		series.getData().add(new XYChart.Data<Number, Number>(40, 66));
		series.getData().add(new XYChart.Data<Number, Number>(60, 69));
		series.getData().add(new XYChart.Data<Number, Number>(100, 77));


		graph_numberaxis_x = new NumberAxis();
	    graph_numberaxis_y = new NumberAxis();

		graph_numberaxis_x.setLabel("TimeStamp");//軸のラベル名
		graph_numberaxis_y.setLabel("Value");
		//graph_linechart = new LineChart<Number, Number>(graph_numberaxis_x, graph_numberaxis_y);
		graph_linechart.getData().add(series);

		graph_linechart.setTitle("Graph");//Graphのタイトル

		// graph_linechart.getData().addAll(series1, series2, series3);複数のやつ

		//graph_linechart.setCreateSymbols(false); 特定マークの無効化

		Alert alert2 = new Alert(AlertType.INFORMATION, "グラフまで終わった．．．？");
		alert2.showAndWait();

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
	void onAllClearAction(ActionEvent event) {

	}

	/**
	 * データカット
	 * @param event
	 */
	@FXML
	void onCuttingDataAction(ActionEvent event) {

	}

	/**
	 * ローパス
	 * @param event
	 */
	@FXML
	void onLowPassAction(ActionEvent event) {

	}

	/**
	 * 全て等しく線形補間する
	 * @param event
	 */
	@FXML
	void onLinearInterpolation_E_Action(ActionEvent event) {

	}

	/**
	 * 一方のファイルに時間軸を合わせる線形補間
	 * @param event
	 */
	@FXML
	void onLinearInterpolation_O_Action(ActionEvent event) {

	}

	/**
	 * 特徴量：平均
	 * @param event
	 */
	@FXML
	void onAverageAction(ActionEvent event) {

	}

	/**
	 * 特徴量：分散
	 * @param event
	 */
	@FXML
	void onVarianceAction(ActionEvent event) {

	}

	/**
	 * 特徴量ファイルにラベル(行動別の数字)付与
	 * @param event
	 */
	@FXML
	void onAddLabelAction(ActionEvent event) {

	}

	/**
	 * Config画面を開く
	 * @param event
	 */
	@FXML
	void onConfigAction(ActionEvent event) {
		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource("Config.fxml"));
			AnchorPane root = (AnchorPane) loader.load();
			Scene scene = new Scene(root, 800, 450);//新しくSceneやStageを作る
			Stage stage = new Stage();
			stage.setTitle("Config");
			stage.setScene(scene);
			stage.showAndWait();

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

}
