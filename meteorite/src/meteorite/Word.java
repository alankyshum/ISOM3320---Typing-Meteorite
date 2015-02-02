package meteorite;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * Created by Alan on 21/1/2015.
 */
public class Word {

    // ATTRIBUTES
    public Pos_info pos;
    public Wrapper_info wrapper;
    // ANIMATION
    public TranslateTransition drop_tt;
    private double drop_speed;
    private double drop_duration;
    private String content;
    private int attack_pt;
    // GLOBAL VARIABLES ACROSS CLASSES
    private FlowPane that_word;

    public Word(Pos_info pos, double level, String content) {
        this.pos = pos;
        this.drop_duration = Math.max(Main.DROP_DUR_BASE - this.drop_speed, 300);
        this.drop_speed = Main.SCREEN.HEIGHT / this.drop_duration;
        this.content = content;
        this.attack_pt = content.length();
        that_word = new FlowPane();
        that_word.setLayoutX(this.pos.x);
        that_word.setLayoutY(this.pos.y);

        double height = 0;
        double width = 0;
        for (char ch : content.toCharArray()) {
            Text tmp = new Text(0, 0, Character.toString(ch));
            tmp.getStyleClass().add("default_text");
            that_word.getChildren().add(tmp);
            if (tmp.getBoundsInParent().getHeight() > height)
                height = tmp.getBoundsInParent().getHeight();
            width += tmp.getBoundsInParent().getWidth();
        }
        this.wrapper = new Wrapper_info(Math.round(width), Math.round(height));
        that_word.getStyleClass().add("meteorite_wrapper");

        PlayController.word_layer_static.getChildren().add(that_word);
        GameSystem.WORD_OBJ_LIST.add(this);
    }

    public void destroy(boolean hitCastle) {
        if (hitCastle) {
            Castle.hp_down(this.attack_pt);
        } else {
//            Castle.shootWord(this);
            GameSystem.currPlayer.score_up(this.attack_pt);
            GameSystem.currPlayer.checkLv();
        }
        if (!(this instanceof BossWord))
            drop_tt.setOnFinished(e -> {
            });
        PlayController.word_layer_static.getChildren().remove(that_word);
        GameSystem.WORD_OBJ_LIST.remove(this);
    }

    // Typed letter effect
    public void typed_letter(int pos, boolean typed) {
        if (typed) {
            if (pos == -1) {
                that_word.getChildren().forEach(t -> {
                    t.getStyleClass().add("typed_text");
                });
            } else {
                that_word.getChildren().get(pos).getStyleClass().add("typed_text");
            }
        } else {
            that_word.getChildren().forEach(t -> {
                t.getStyleClass().remove("typed_text");
            });
        }
    }

    public void drop() {
        drop_tt = new TranslateTransition(Duration.millis(drop_duration), that_word);
        drop_tt.setToY(Castle.pos.y + Castle.wrapper.height - wrapper.height);
        drop_tt.setCycleCount(1);
        drop_tt.setAutoReverse(false);
        drop_tt.setInterpolator(Interpolator.SPLINE(0.3, 0, 1, 1));
        drop_tt.setOnFinished(e -> {
            this.destroy(true);
        });
        drop_tt.play();
    }

    public String get_content() {
        return this.content;
    }

    public FlowPane get_word_obj() {
        return this.that_word;
    }
}
