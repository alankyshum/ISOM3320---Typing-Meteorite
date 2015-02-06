package meteorite;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * Created by Alan on 21/1/2015.
 */
class Word {

    // ATTRIBUTES
    public final Pos_info pos;
    public final Wrapper_info wrapper;
    // ANIMATION
    public TranslateTransition dropTT;
    private final double dropDur;
    private final String content;
    private final int attackPt;
    // GLOBAL VARIABLES ACROSS CLASSES
    private final FlowPane thatWord;

    public Word(Pos_info pos, double level, String content) {
        this.pos = pos;
        double dropSpeed = level * 700;
        this.dropDur = Math.max(Main.DROP_DUR_BASE - dropSpeed, 300);
        this.content = content;
        this.attackPt = content.length();
        thatWord = new FlowPane();
        thatWord.setLayoutX(this.pos.x);
        thatWord.setLayoutY(this.pos.y);

        double height = 0;
        double width = 0;
        for (char ch : content.toCharArray()) {
            Text tmp = new Text(0, 0, Character.toString(ch));
            tmp.getStyleClass().add("default_text");
            thatWord.getChildren().add(tmp);
            if (tmp.getBoundsInParent().getHeight() > height)
                height = tmp.getBoundsInParent().getHeight();
            width += tmp.getBoundsInParent().getWidth();
        }
        this.wrapper = new Wrapper_info(Math.round(width), Math.round(height));
        thatWord.getStyleClass().add("meteorite_wrapper");

        PlayController.wordLayer_static.getChildren().add(thatWord);
        GameSystem.WORD_OBJ_LIST.add(this);
    }

    public void destroy(boolean hitCastle) {
        if (hitCastle) {
            Castle.hpDown(this.attackPt);
        } else {
//            Castle.shootWord(this);
            GameSystem.currPlayer.scoreUp(this.attackPt);
            GameSystem.currPlayer.checkLv();
        }
        if (!(this instanceof BossWord))
            dropTT.setOnFinished(e -> {
            });
        PlayController.wordLayer_static.getChildren().remove(thatWord);
        GameSystem.WORD_OBJ_LIST.remove(this);
    }

    // Typed letter effect
    public void typedLetter(int pos, boolean typed) {
        if (typed) {
            if (pos == -1) {
                thatWord.getChildren().forEach(t -> t.getStyleClass().add("typed_text"));
            } else {
                thatWord.getChildren().get(pos).getStyleClass().add("typed_text");
            }
        } else {
            thatWord.getChildren().forEach(t -> t.getStyleClass().remove("typed_text"));
        }
    }

    public void drop() {
        dropTT = new TranslateTransition(Duration.millis(dropDur), thatWord);
        dropTT.setToY(Castle.pos.y + Castle.wrapper.height - wrapper.height);
        dropTT.setCycleCount(1);
        dropTT.setAutoReverse(false);
        dropTT.setInterpolator(Interpolator.SPLINE(0.3, 0, 1, 1));
        dropTT.setOnFinished(e -> this.destroy(true));
        dropTT.play();
    }

    public String getContent() {
        return this.content;
    }

    public FlowPane getWordObj() {
        return this.thatWord;
    }
}
