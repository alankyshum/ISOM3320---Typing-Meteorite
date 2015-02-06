package meteorite;

import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class GameSystem {
    public static final ArrayList<Player> playerList = new ArrayList<>();
    public static Player currPlayer;
    public static boolean gameOver = false;
    public static final ArrayList<String> bossList = new ArrayList<>();
    public static final ArrayList<String> wordList = new ArrayList<>();
    public static final ArrayList<Word> WORD_OBJ_LIST = new ArrayList<>();
    private static ArrayList<Word> match_word_obj_list = new ArrayList<>();
    private static ArrayList<Word> match_word_obj_list_del = new ArrayList<>();
    private static int match_til_pos = 0;
    private static boolean wordMatched = false;
    private static boolean bossMatched = false;
    private static boolean partialMatched = false;
    private static boolean gameStopped = false;

    public static void handleKeyPress(KeyEvent KE) {
        String key = KE.getText().toUpperCase();
        if (!key.isEmpty()) {
            if (match_word_obj_list.isEmpty()) {
                match_word_obj_list = (ArrayList<Word>) (WORD_OBJ_LIST.clone());
                match_word_obj_list_del = new ArrayList<>();
                wordMatched = false;
                bossMatched = false;
                partialMatched = false;
                match_til_pos = 0;
            }
            // track all matched + visible words
            match_word_obj_list.forEach(w -> {
                if (w.getContent().startsWith(key, match_til_pos) &&
                        WORD_OBJ_LIST.contains(w)) {
                    partialMatched = true;
                    w.typedLetter(match_til_pos, true);
                    if (match_til_pos == w.getContent().length() - 1) {
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
                    w.typedLetter(0, false);
                }
            });
            // some words matched
            if (partialMatched) match_til_pos++;
            // found target word to destroy
            if (wordMatched) {
                if (bossMatched) {
                    WORD_OBJ_LIST.forEach(all_w -> {
                        all_w.typedLetter(-1, true);
                        Castle.shootWord(all_w);
                    });
                } else {
                    WORD_OBJ_LIST.forEach(all_w -> all_w.typedLetter(0, false));
                }
                match_word_obj_list.clear();
            } else {
                match_word_obj_list_del.forEach(match_word_obj_list::remove);
            }
        }
    }

    public static void createPlayer() {
//		String playerName = JOptionPane.showInputDialog(null, "Name", "You name:", JOptionPane.QUESTION_MESSAGE);
        currPlayer = new Player(1, "Dummy", 0);
    }

    public static void savePlayer(Player player) throws FileNotFoundException {
        List<Player> writeList;
        if (!playerList.isEmpty()) {
            writeList = (List) playerList.clone();
            writeList.add(player);
            writeList.sort((data1, data2) -> (data2.getScore() - data1.getScore()));
        } else {
            writeList = new ArrayList<>();
            writeList.add(0, player);
        }

        java.io.File file = new java.io.File("db/player.txt");
        java.io.PrintWriter output = new java.io.PrintWriter(file);
        int cnt = Main.NUM_RECORDS;
        for (Player p : writeList) {
            cnt--;
            if (cnt < 0) break;
            playerList.add(p);
            output.println(p.getLv() + "," + p.getName() + "," + p.getScore());
        }
        output.close();
    }

    public static void loadToPlayerList() throws IOException {
        FileReader fr = new FileReader("db/player.txt");
        BufferedReader br = new BufferedReader(fr);
        String[] linex;
        String line;
        while ((line = br.readLine()) != null) {
            linex = line.split(",");
            playerList.add(new Player(Integer.parseInt(linex[0].trim()), linex[1], Integer.parseInt(linex[2].trim())));
        }
        fr.close();
    }

    public static void loadToWordList(int lv) throws IOException {
        FileReader fr = new FileReader("db/word.txt");
        BufferedReader br = new BufferedReader(fr);
        String[] linex;
        int curr_lv = 1;
        String line;
        while ((line = br.readLine()) != null) {
            if (curr_lv == lv) {
//                wordList.clear();
                linex = line.split(" ");
                for (String aLinex : linex) {
                    wordList.add(aLinex.trim());
                }
                break;
            } else curr_lv++;
        }
        fr.close();
    }

    public static void loadToBossList() throws IOException {
        FileReader fr = new FileReader("db/boss.txt");
        BufferedReader br = new BufferedReader(fr);
        String line;
        bossList.clear();
        while ((line = br.readLine()) != null) {
            bossList.add(line.trim());
        }
        fr.close();
    }

    public static void spawn_n_dropWord() {
        try {
            loadToWordList(currPlayer.getLv());
        } catch (IOException ioE) {
            System.out.println(ioE);
        } finally {
            // safety lock: loadToWordList may clear wordList before accessing as below
            if (!wordList.isEmpty()) {
                int word_index = (int) (Math.random() * wordList.size());
                int x = (int) (Math.random() * (Main.SCREEN.WIDTH - wordList.get(word_index).length() * 30)) + 10;
                Pos_info temp = new Pos_info(x, 0);
                Word word = new Word(temp, currPlayer.getLv(), wordList.get(word_index));
                word.drop();
            }
        }
    }

    public static void message(String content) {
        PlayController.msg_static.setText(content);
        TranslateTransition msgTT_1 = new TranslateTransition(Duration.seconds(1), PlayController.msg_static);
        msgTT_1.setFromX(-105);
        msgTT_1.setToX(Main.SCREEN.WIDTH / 2 + PlayController.msg_static.getBoundsInParent().getWidth() / 2);
        TranslateTransition msgTT_2 = new TranslateTransition(Duration.seconds(1), PlayController.msg_static);
        msgTT_2.setToX(Main.SCREEN.WIDTH + PlayController.msg_static.getBoundsInParent().getWidth());
        SequentialTransition st = new SequentialTransition(msgTT_1, new TranslateTransition(Duration.seconds(1)), msgTT_2);
        st.play();
    }

    public static void gameEnd() {
        if (!gameOver) {
            PlayController.playerName_static.setDisable(false);
            PlayController.homeBtn_static.setDisable(false);
            PlayController.saveBtn_static.setDisable(false);
            SoundSystem.BGM.stop();
            System.out.println("GAME OVER");

            // stop generating words
            PlayController.genWordTimer_static.pause();
            GameSystem.WORD_OBJ_LIST.forEach(w -> {
                if (w instanceof BossWord) {
                    ((BossWord) w).dropTT.pause();
                } else {
                    w.dropTT.pause();
                }
                w.getWordObj().setEffect(new GaussianBlur(7));
            });
            Main.STAGE.getScene().setOnKeyPressed(e -> {
            });
            PlayController.stopBtn_static.setOnAction(e -> {
            });

            // show final score + level of current player
            PlayController.scoreText_final_static.setText(Integer.toString(GameSystem.currPlayer.getScore()));
            PlayController.levelText_final_static.setText(Integer.toString(GameSystem.currPlayer.getLv()));


            // translate out the panel showing the result
            TranslateTransition panelTT = new TranslateTransition(Duration.seconds(1), PlayController.endGamePanel_static);
            panelTT.setByY(-Main.SCREEN.HEIGHT / 2 - 100);
            panelTT.play();
            gameOver = true;
        }
    }

    public static void gamePause() {
        if (gameStopped) {
            PlayController.stopBtn_static.setText("||");
            PlayController.genWordTimer_static.play();
            GameSystem.WORD_OBJ_LIST.forEach(w -> {
                if (w instanceof BossWord) {
                    ((BossWord)w).dropTT.play();
                } else {
                    w.dropTT.play();
                }
                w.getWordObj().setEffect(new GaussianBlur(0));
            });
            gameStopped = false;
            Main.STAGE.getScene().setOnKeyPressed(GameSystem::handleKeyPress);
        } else {
            PlayController.genWordTimer_static.pause();
            GameSystem.WORD_OBJ_LIST.forEach(w -> {
                if (w instanceof BossWord) {
                    ((BossWord) w).dropTT.pause();
                } else {
                    w.dropTT.pause();
                }
                w.getWordObj().setEffect(new GaussianBlur(7));
            });
            PlayController.stopBtn_static.setText("|>");
            gameStopped = true;
            Main.STAGE.getScene().setOnKeyPressed(e -> {
            });
        }
    }

}