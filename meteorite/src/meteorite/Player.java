package meteorite;

import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Player {
    private int lv = 0;
    private String name;
    private int score = 0;
    private int hitCnt = 0;

    public Player(int lv, String name, int score){
        this.lv = lv;
        this.name = name;
        this.score = score;
    }

    public void score_up (int incr){
        score += incr;
        hitCnt++;
        PlayController.score_text_static.setText("Scores: " + score);
    }

    public void checkLv(){
        if (hitCnt >= Main.LV_UP_THRESHOLD*this.lv) {
            lv++;
            PlayController.lv_text_static.setText("Level: " + lv);
            Castle.hp_up();
            GameSystem.message("LEVEL UP");

            // get more advanced words
            try {
                GameSystem.load_to_word_list(lv);
            } catch (IOException ioE) {
                System.out.println(ioE);
            }

            // create a boss
            Word boss = new BossWord(new Pos_info((Main.SCREEN.WIDTH-300)/2, 0), 1, GameSystem.boss_list.get((int)(Math.random() * GameSystem.boss_list.size())));
            boss.drop();
        }
    }

    public void set_name(String name){
        this.name = name;
    }

    public int get_lv(){
        return this.lv;
    }

    public String get_name(){
        return this.name;
    }

    public int get_score(){
        return this.score;
    }
}