package meteorite;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * Created by Alan on 21/1/2015.
 */
public class Word {

    // ATTRIBUTES
    private Pos_info drop_pt;
    private double drop_speed;
    private Pos_info pos;
    private String content;
    private int attack_pt;
    private Wrapper_info wrapper;

    // CONSTANTS
    private final int BASE_DUR = 500000;

    // GLOBAL VARIABLES ACROSS CLASSES
    private HBox that_word;
    private static Pane playground;

    public Word(Pos_info pos, double speed, String content) {
        this.drop_pt = pos;
        this.pos = pos;
        this.drop_speed = speed/2;
        this.content = content;
        this.attack_pt = content.length();

        that_word = new HBox();
        that_word.setLayoutX(this.drop_pt.x);
        that_word.setLayoutY(this.drop_pt.y);
        for (char ch: content.toCharArray()) {
            Text tmp = new Text(0, 0, Character.toString(ch));
            tmp.getStyleClass().add("default_text");
            that_word.getChildren().add(tmp);
        }
        that_word.getStyleClass().add("meteorite_wrapper");

        playground.getChildren().add(that_word);
        GameSystem.WORD_OBJ_LIST.add(this);
    }

    public void destroy() {
        playground.getChildren().remove(that_word);
        GameSystem.WORD_OBJ_LIST.remove(this);
    }

    // Port playground here to class-scope
    public static void importPane(Pane pane_ground) {
        playground = pane_ground;
    }

    // Typed letter effect
    public void typed_letter(int pos, boolean typed) {
        if (typed)
            ((Text)that_word.getChildren().get(pos)).getStyleClass().add("typed_text");
        else
            that_word.getChildren().forEach(t-> {
                ((Text)t).getStyleClass().remove("typed_text");
            });
    }

    public boolean check_hit() {
        return false;
    }

    public void drop() {

//        System.out.println(playground.getChildren());

        TranslateTransition tf = new TranslateTransition(Duration.millis(BASE_DUR/this.drop_speed), that_word);
        tf.setToY(Main.SCREEN_HEIGHT);
        tf.setCycleCount(1);
        tf.setAutoReverse(false);
        tf.setInterpolator(Interpolator.SPLINE(0.3, 0, 1, 1));
        tf.setOnFinished(e-> {
            this.destroy();
        });
        tf.play();

    }

    public Pos_info get_pos() {
        return this.pos;
    }

    public String get_content() { return this.content; }

    public HBox get_word_obj() { return this.that_word; }
}
