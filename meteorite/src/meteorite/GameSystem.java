package meteorite;

import java.util.ArrayList;
import java.io.*;

import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.List;
import java.util.ListIterator;

public class GameSystem {
    private static Text score_text;
    private static Text lv_text;

    public static ArrayList<Player> player_list = new ArrayList<>();
    public static Player currPlayer;
    public static boolean gameOver = false;

    public static ArrayList<String> boss_list = new ArrayList<>();

    public static ArrayList<String> word_list = new ArrayList<>();
    public static ArrayList<Word> WORD_OBJ_LIST = new ArrayList<>();
    private static ArrayList<Word> match_word_obj_list = new ArrayList<>();
    private static ArrayList<Word> match_word_obj_list_del = new ArrayList<>();
    private static int match_til_pos = 0;
    private static boolean wordMatched = false;
    private static boolean bossMatched = false;
    private static boolean partialMatched = false;
    public static void handle_key_press(String key) {
        if (key.isEmpty()) return;
//        System.out.printf("Size of all %s", WORD_OBJ_LIST.size());
        if (match_word_obj_list.isEmpty()) {
            match_word_obj_list = (ArrayList<Word>)(WORD_OBJ_LIST.clone());
//            WORD_OBJ_LIST.forEach(match_word_obj_list::add);
            match_word_obj_list_del = new ArrayList<>();
            wordMatched = false;
            bossMatched = false;
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
                    WORD_OBJ_LIST.remove(w); // remove to retain style
                    if (w instanceof BossWord) {
                        bossMatched = true;
                    }
                    Castle.shootWord(w);
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
            if (bossMatched) {
                WORD_OBJ_LIST.forEach(all_w -> {
                    all_w.typed_letter(-1, true);
                    Castle.shootWord(all_w);
                });
            } else {
                WORD_OBJ_LIST.forEach(all_w -> {
                    all_w.typed_letter(0, false);
                });
            }
            match_word_obj_list.clear();
        } else {
            match_word_obj_list_del.forEach(match_word_obj_list::remove);
        }
//        System.out.printf("\tSize of matched %s\n", match_word_obj_list.size());
    }

	public static void create_player() {
//		String playerName = JOptionPane.showInputDialog(null, "Name", "You name:", JOptionPane.QUESTION_MESSAGE);
		currPlayer = new Player(1, "Dummy", 0);
	}

	public static void save_player(Player player) throws FileNotFoundException {
        List<Player> writeList;
        if (!player_list.isEmpty()) {
            writeList = (List)player_list.clone();
            writeList.add(player);
            writeList.sort((data1, data2) -> {
                return (data2.get_score() - data1.get_score());
            });
		} else {
            writeList = new ArrayList<>();
            writeList.add(0, player);
		}

		java.io.File file = new java.io.File("player.txt");
		java.io.PrintWriter output = new java.io.PrintWriter(file);
        int cnt = 3;
        for (Player p : writeList) {
            cnt--;
            if (cnt < 0) break;
            player_list.add(p);
            output.println(p.get_lv() + "," + p.get_name() + "," + p.get_score());
        }
		output.close();
	}

    public static void load_to_player_list() throws IOException {
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
        BufferedReader br = new BufferedReader(fr);
        String[] linex;
        int curr_lv = 1;
        String line;
        while ((line = br.readLine()) != null) {
            if (curr_lv == lv) {
//                word_list.clear();
                linex = line.split(" ");
                for (int i = 0; i < linex.length; i++) {
                    word_list.add(linex[i].trim());
                }
                break;
            } else curr_lv++;
        }
        fr.close();
    }

    public static void load_to_boss_list() throws IOException {
        FileReader fr = new FileReader("boss.txt");
        BufferedReader br = new BufferedReader(fr);
        String line;
        boss_list.clear();
        while ((line = br.readLine()) != null) {
            boss_list.add(line.trim());
        }
        fr.close();
    }

    public static void spawn_n_drop_word() {
        try {
            load_to_word_list(currPlayer.get_lv());
        } catch (IOException ioE) {
            System.out.println(ioE);
        } finally {
            // safety lock: load_to_word_list may clear word_list before accessing as below
            if (!word_list.isEmpty()) {
                int word_index = (int)(Math.random()*word_list.size());
                int x = (int)(Math.random()*(Main.SCREEN.WIDTH-word_list.get(word_index).length()*30))+10;
                Pos_info temp = new Pos_info(x, 0);
                Word word = new Word(temp, currPlayer.get_lv(), word_list.get(word_index));
                word.drop();
            }
        }
    }

    public static void message(String content) {
        PlayController.msg_static.setText(content);
        TranslateTransition msgTT_1 = new TranslateTransition(Duration.seconds(1), PlayController.msg_static);
        msgTT_1.setFromX(-105);
        msgTT_1.setToX(Main.SCREEN.WIDTH/2+PlayController.msg_static.getBoundsInParent().getWidth()/2);
        TranslateTransition msgTT_2 = new TranslateTransition(Duration.seconds(1), PlayController.msg_static);
        msgTT_2.setToX(Main.SCREEN.WIDTH + PlayController.msg_static.getBoundsInParent().getWidth());
        SequentialTransition st = new SequentialTransition(msgTT_1, new TranslateTransition(Duration.seconds(1)), msgTT_2);
        st.play();
    }

    public static void game_end() {
        if (!gameOver) {
            PlayController.playerName_static.setDisable(false);
            SoundSystem.BGM.stop();
            System.out.println("GAME OVER");

            // stop generating words
            PlayController.genWordTimer_static.pause();
            GameSystem.WORD_OBJ_LIST.forEach(w -> {
                if (w instanceof BossWord) {
                    ((BossWord)w).drop_tt.pause();
                } else {
                    w.drop_tt.pause();
                }
                w.get_word_obj().setEffect(new GaussianBlur(7));
            });
            Main.STAGE.getScene().setOnKeyPressed(e -> {
            });
            PlayController.stopBtn_static.setOnAction(e-> {});

            // show final score + level of current player
            PlayController.scoreText_final_static.setText(Integer.toString(GameSystem.currPlayer.get_score()));
            PlayController.levelText_final_static.setText(Integer.toString(GameSystem.currPlayer.get_lv()));


            // translate out the panel showing the result
            TranslateTransition panelTT = new TranslateTransition(Duration.seconds(1), PlayController.endGamePanel_static);
            panelTT.setByY(-Main.SCREEN.HEIGHT / 2 - 100);
            panelTT.play();
            gameOver = true;
        }
    }

}