package application;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Stream;

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

	private ArrayList<FileData> global_list_filedata = new ArrayList<FileData>();

	//pane関連


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

	private XYChart.Series<Number, Number>[] series_array;

	//ここまでフィールド宣言

	//initialize・・・初期化処理
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//		System.out.println("Hello initialize");

		//個々参考にしてみたら？
		//http://hideoku.hatenablog.jp/entry/2013/06/07/205016
		//Scene graph_scene = new Scene(graph_linechart,800,450);

	}

	/**
	 * csvファイルを読み込んでグラフにプロットする
	 * @param event
	 */
	@SuppressWarnings("unchecked")
	@FXML
	void onOpenDataAction(ActionEvent event) {

		//ファイル選択するダイアログ
		List<File> list_files = showOpenMultipleDialog_fc();
		if (list_files==null) {
			return;//ユーザーメッセージはshowOpenMultipleDialog_fcで出す．
		}

		File file = list_files.get(0);//将来的に複数にするけど，とりあえずプロットするため固定

		//FileDataのインスタンスを作成してデータimport
		FileData filedata = new FileData();
		filedata.import_file(file, 0);//とりあえずtimestampは0想定

		global_list_filedata.add(filedata);//読み込んだファイルdataはglobal_list_filedataに追加すること．

		ArrayList<Long> list_timestamp = filedata.getFile_data_timestamp();
		ArrayList<Float>[] list_data_array = filedata.getFile_data_contents();
		String[] title_array = filedata.getFile_data_title();

		//graphパーツ  @FXMLで作成したパーツはNew宣言不要？ (詳しく調べる必要がある

		//グラフ描画 //とりあえずx => timestamp , y => value

		//データ

		series_array = Stream.<XYChart.Series<Number, Number>> generate(XYChart.Series::new)//
				.limit(list_data_array.length)
				.toArray(XYChart.Series[]::new);//Streamを用いてジェネリクス宣言でXYChart.Seriesをlimit分生成し，toArrayで各配列の中身をnewで初期化する．

		for (int i = 0; i < series_array.length; i++) {
			series_array[i].setName(title_array[i]);//凡例名の設定
		}

		//データ設定 ...timestampは初期を0として，差分を足していく
		long timestamp_before = list_timestamp.get(0);
		long timestamp_after = list_timestamp.get(0);
		long timestamp_diffsum = 0;

		boolean is_scale = false;//横にのびるため，横軸に対してスケール処理をおこなう場合のフラグ
		double scale = 0.3;//スケールどれくらい
		for (int i = 0; i < list_timestamp.size(); i++) {
			timestamp_after = list_timestamp.get(i);

			long timestamp_diff = timestamp_after - timestamp_before;
			for (int j = 0; j < series_array.length; j++) {//データ登録部分
				long timestamp_register = timestamp_diffsum + timestamp_diff;
				if (is_scale) {
					double timestamp_register_scale = timestamp_register*scale;
					series_array[j].getData()
							.add(new XYChart.Data<Number, Number>(timestamp_register_scale, list_data_array[j].get(i)));
				} else {
					series_array[j].getData()
							.add(new XYChart.Data<Number, Number>(timestamp_register, list_data_array[j].get(i)));
				}
			}

			timestamp_diffsum += timestamp_diff;

			timestamp_before = timestamp_after;
		}

		//軸
		graph_numberaxis_x = new NumberAxis();
		graph_numberaxis_y = new NumberAxis();

		graph_numberaxis_x.setLabel("TimeStamp");//軸のラベル名
		graph_numberaxis_y.setLabel("Value");

		//データをグラフ領域に追加
		for (XYChart.Series<Number, Number> series : series_array) {
			graph_linechart.getData().add(series);
		}

		graph_linechart.setTitle("Graph");//Graphのタイトル
		graph_linechart.setCreateSymbols(false); //特定マークの無効化

		Alert alert2 = new Alert(AlertType.INFORMATION, "グラフまで終わった．．．？");
		alert2.showAndWait();

	}

	/**
	 *
	 * @return List<File> なにもファイルが選択されないとnull
	 */
	private List<File> showOpenMultipleDialog_fc() {
		FileChooser fc = new FileChooser();
		fc.setInitialDirectory(new File("..\\"));//初期Pathの位置
		List<File> list_files;

		list_files = fc.showOpenMultipleDialog(null);

		if (list_files == null) {
			Alert alert = new Alert(AlertType.INFORMATION, "ファイルが選択されていません");
			alert.showAndWait();

		}
		return list_files;
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
