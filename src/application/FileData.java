package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * 一つのファイル（csvであってほしい）から読み込んだデータを表現するクラス
 *
 * @author Cumene
 *
 */
public class FileData {

	private Path file_path; //読み込んだファイルのPathは保持しておく

	private ArrayList<Long> file_data_timestamp;

	private ArrayList<Float>[] file_data_contents;//timestampは除く．とりあえずデータ部はFloat固定します．問題の先送り

	private String[] file_data_title;//timestampは除く

	private boolean  is_timestamp_interval = true;//trueなら設定した間隔以下，falseだとintervalがあいていて問題がある．

	public FileData() {

	}

	/**
	 * @param file_data_timestamp
	 * @param file_data_contents
	 * @param file_data_name
	 */
	public FileData(Path file_path, ArrayList<Long> file_data_timestamp, ArrayList<Float>[] file_data_contents,
			String[] file_data_title) {
		//super();
		this.file_path = file_path;
		this.file_data_timestamp = file_data_timestamp;
		this.file_data_contents = file_data_contents;
		this.file_data_title = file_data_title;
	}

	public Path getFile_path() {
		return file_path;
	}

	public ArrayList<Long> getFile_data_timestamp() {
		return file_data_timestamp;
	}

	public ArrayList<Float>[] getFile_data_contents() {//とりあえずデータ部はFloat固定します．問題の先送り
		return file_data_contents;
	}

	public String[] getFile_data_title() {
		return file_data_title;
	}

	public boolean getIs_timestamp_interval() {
		return is_timestamp_interval;
	}

	/**
	 *
	 * @param file データ元Fileです
	 * @param timestamp_index :timestampがどの列にあるかを指定（一番左なら0,というか一番左にしてくれ．それしかデバッグしてな．．)
	 * @param ms :タイムスタンプの間隔がms以上のとき警告を出すために指定．強制．嫌な事件を忘れない．
	 *
	 *
	 *
	 */
	@SuppressWarnings("unchecked")
	public void import_file(File file, int timestamp_index, long ms) {
		Path path = file.toPath();
		file_path = path;

		try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {//UTF-8のファイルってこと前提にしていいよね．信じてる

			boolean is_first = true;
			boolean is_second = true;
			long timestamp_before = 0;//インターバルチェックに使う．ブロックの都合上．ごめん

			for (String line; (line = br.readLine()) != null;) {//一行ずつテキストをreadする

				String[] split_line = line.split(",");//csvだしね
				//				for (String a : split_line) {
				//					System.out.print(a + ",");
				//				}
				//				System.out.println("");

				if (is_first) {//最初の行のみ行う処理

					//各配列のインスタンス宣言
					final int arraylist_init_size = 6000;//10msに1回のデータが1分間想定．性能気になるならテキトーにいじって
					file_data_timestamp = new ArrayList<Long>(arraylist_init_size);

					file_data_contents = new ArrayList[split_line.length - 1];
					for (int i = 0; i < split_line.length - 1; i++) {
						file_data_contents[i] = new ArrayList<Float>(arraylist_init_size);//とりあえずデータ部はFloat固定します．問題の先送り
					}
					file_data_title = new String[split_line.length - 1];

					//タイトル代入
					int j = 0;
					for (int i = 0; i < split_line.length - 1; i++) {
						if (i == timestamp_index) {
							j++;

						}
						file_data_title[i] = split_line[j];
						j++;
					}

					//					for (String a : file_data_title) {
					//						System.out.print(a + ",");
					//					}
					//					System.out.println("");

					is_first = false;
					continue;
				} else {//2行目以降行う処理

					//データ部分の入力
					int j = 0;

					if (is_second) {//timestampの基準を代入．
						timestamp_before = Long.parseLong(split_line[timestamp_index]);

						is_second = false;
					}
					//long timestamp_after =0;
					for (int i = 0; i < split_line.length; i++) {
						//						System.out.println(split_line[i]);
						if (i == timestamp_index) {//タイムスタンプ部分
							long read_timestamp = Long.parseLong(split_line[i]);
							boolean is_interval = check_interval_timestamp(timestamp_before, read_timestamp, ms);

							if (!is_interval) {

								is_timestamp_interval = false;
								String message = Long.toString(timestamp_before) + " , " + Long.toString(read_timestamp)
								+ " に差があります";

								System.out.println(message);

							}

							file_data_timestamp.add(read_timestamp);
							timestamp_before = read_timestamp;
							j--;

						} else {//実データ部分
							j++;
							file_data_contents[j].add(Float.parseFloat(split_line[i]));//とりあえずデータ部はFloat固定します．問題の先送り

						}

					}

				}

			}
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	/**
	 *
	 * @param ts_before time_stamp1
	 * @param ts_after time_stamp2
	 * @param ms ミリ秒．これ以上の間隔が空いているかをチェックする
	 * @return boolean. 間隔があいてなければtrue,問題があったときfalse
	 */
	private boolean check_interval_timestamp(long ts_before, long ts_after, long ms) {
		long diff = Math.abs(ts_after - ts_before);//これでb >a ,a >bでも大丈夫．．．なはず

		if (ms <= diff) {
			return false;
		} else {
			return true;
		}

	}

	public static void main(String[] args) {//軽いテスト
		FileData fd = new FileData();

		File file = new File("..\\TestData\\testdata.csv");

		fd.import_file(file, 0, 100);

		fd.showAll();

	}

	private void showAll() {
		int fdt = file_data_timestamp.size();
		int[] fdc_array = new int[file_data_contents.length];
		int fdc_array_length = fdc_array.length;
		int count_fdc_array = 0;
		for (ArrayList<Float> array : file_data_contents) {
			fdc_array[count_fdc_array] = array.size();
			count_fdc_array++;
		}

		int fdc_t = file_data_title.length;

		System.out.print(fdt + "," + fdc_array_length + ",");
		for (int num : fdc_array) {
			System.out.print(num + ",");
		}
		System.out.println(fdc_t);

		for (String l : file_data_title) {
			System.out.println(l);
		}

	}

}
