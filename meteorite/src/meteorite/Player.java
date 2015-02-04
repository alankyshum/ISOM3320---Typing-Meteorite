package meteorite;

import java.io.IOException;

public class Player {
    private int lv = 0;
    private String name;
    private int score = 0;
    private int hitCnt = 0;

    public Player(int lv, String name, int score) {
        this.lv = lv;
        this.name = name;
        this.score = score;
    }

    public void scoreUp(int incr) {
        score += incr;
        hitCnt++;
        PlayController.scoreText_static.setText("Scores: " + score);
    }

    public void checkLv() {
        if (hitCnt >= Main.LV_UP_THRESHOLD * this.lv) {
            lv++;
            PlayController.lvText_static.setText("Level: " + lv);
            Castle.hpUp();
            GameSystem.message("LEVEL UP");

            // get more advanced words
            try {
                GameSystem.loadToWordList(lv);
            } catch (IOException ioE) {
                System.out.println(ioE);
            }

            // create a boss
            Word boss = new BossWord(new Pos_info((Main.SCREEN.WIDTH - 300) / 2, 0), GameSystem.bossList.get((int) (Math.random() * GameSystem.bossList.size())));
            boss.drop();
        }
    }

    public int getLv() {
        return this.lv;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return this.score;
    }
}