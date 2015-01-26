package meteorite;

import java.util.ArrayList;
import java.io.*;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Duration;

import javax.swing.*;
import java.util.ListIterator;

public class GameSystem {
	private static Text score_text;
	private static Text lv_text;

	public static ArrayList<String> word_list = new ArrayList<>();
	public static ArrayList<Word> WORD_OBJ_LIST = new ArrayList<>();
	public static ArrayList<Player> player_list = new ArrayList<>();
	public static final int LV_UP_THRESHOLD = 10;
	public static final int SCOREINCRE = 1;

	public static Player currPlayer;

	private static ArrayList<Word> match_word_obj_list = new ArrayList<>();
	private static ArrayList<Word> match_word_obj_list_del = new ArrayList<>();
	private static int match_til_pos = 0;
	private static boolean wordMatched = false;
	private static boolean partialMatched = false;
	public static void handle_key_press(String key) {
		if (key.isEmpty()) return;
//        System.out.printf("Size of all %s", WORD_OBJ_LIST.size());
		if (match_word_obj_list.isEmpty()) {
			match_word_obj_list = (ArrayList<Word>)(WORD_OBJ_LIST.clone());
//            WORD_OBJ_LIST.forEach(match_word_obj_list::add);
			match_word_obj_list_del = new ArrayList<>();
			wordMatched = false;
			partialMatched = false;
			match_til_pos = 0;
		}
		// track all matched + visible words
		match_word_obj_list.forEach(w -> {
			if (w.get_content().startsWith(key, match_til_pos) &&
					WORD_OBJ_LIST.contains(w)) {
				partialMatched = true;
				w.typed_letter(match_til_pos, true);
				if (match_til_pos == w.get_content().length()-1) {
					wordMatched = true;
					w.destroy();
					w.typed_letter(0, false);
					// break loop here if: only destroy 1 of all same words
				}
			} else {
				match_word_obj_list_del.add(w);
				w.typed_letter(0, false);
			}
		});
		// some words matched
		if (partialMatched) match_til_pos++;
		// found target word to destroy
		if (wordMatched) {
			currPlayer.score_up();
			currPlayer.lv_up();
			Driver.game_update();
			match_word_obj_list.clear();
		}
		else match_word_obj_list_del.forEach(match_word_obj_list::remove);
//        System.out.printf("\tSize of matched %s\n", match_word_obj_list.size());
	}

	public static void create_rating_chart() {
		for (int i = 0; !player_list.isEmpty(); i++) {
			Player temp;
			Text[] texts = new Text[player_list.size()];
			texts[i] = new Text();
			temp = player_list.get(i);
			Text text = new Text(300, 300, "asd");
			text.setText(temp.get_lv() + "     " + temp.get_name() + "    " + temp.get_score());
			text.setX(100);
			text.setY(150 + i * 50);

		}
	}

	public static void create_player() {
//		String playerName = JOptionPane.showInputDialog(null, "Name", "You name:", JOptionPane.QUESTION_MESSAGE);
		currPlayer = new Player(1, "asd", 0);
	}

	public static void save_player(Player player) throws FileNotFoundException {
		Player temp;
		int index = 0;
		if (!player_list.isEmpty()) {
			for (ListIterator < Player > it1 = player_list.listIterator(); it1.hasNext() == true;) {
				index = it1.nextIndex();
				temp = it1.next();
				if (temp.get_score() < player.get_score()) {
					player_list.add(index, player);
					break;
				}
			}
			if (player_list.size() < 3) {
				player_list.add(index, player);
			}
		} else {
			player_list.add(0, player);
		}

		java.io.File file = new java.io.File("player.txt");

		java.io.PrintWriter output = new java.io.PrintWriter(file);

		for (int i = 0; i < player_list.size(); i++) {
			temp = player_list.get(i);
			output.println(temp.get_lv() + "," + temp.get_name() + "," + temp.get_score());
		}

		output.close();
	}

	public static void load_to_player_list() throws IOException {
		System.out.println("Called");
		FileReader fr = new FileReader("player.txt");
		BufferedReader br = new BufferedReader(fr);
		String[] linex;
		String line;
		while ((line = br.readLine()) != null) {
			linex = line.split(",");
			player_list.add(new Player(Integer.parseInt(linex[0].trim()), linex[1], Integer.parseInt(linex[2].trim())));
		}
		fr.close();
	}

	public static void load_to_word_list(int lv) throws IOException {
		FileReader fr = new FileReader("word.txt");
//		FileReader fr = new FileReader(Main.class.getResource("scene/word.txt").toExternalForm());

		BufferedReader br = new BufferedReader(fr);
		//System.out.println(br.readLine());

		String[] linex;
		int curr_lv = 1;
		String line;
		while ((line = br.readLine()) != null) {
			if (curr_lv == lv) {
				linex = line.split(" ");
				for (int i = 0; i < linex.length; i++) {
					word_list.add(linex[i].trim());
				}
				break;
			} else curr_lv++;
		}
		fr.close();
	}

	public static void spawn_n_drop_word() {
		int xpos = (int)(Math.random()*800);
		int ypos = 0;
		int word_index = (int)(Math.random() * (word_list.size() - 1));
		Pos_info temp = new Pos_info(xpos, ypos);
		Word word = new Word(temp, currPlayer.get_lv(), word_list.get(word_index));
		WORD_OBJ_LIST.add(word);
		word.drop();
	}

	public static void screen_update_score() {
		Controller.score_text_static.setText("Scores: " + currPlayer.get_score());
		System.out.println("Score: "+currPlayer.get_score());
	}

	public static void screen_update_lv() {
		Controller.lv_text_static.setText("Level: " + currPlayer.get_lv());
		System.out.println("Level: "+currPlayer.get_lv());
	}


}