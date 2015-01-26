package meteorite;

import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Duration;

import javax.swing.text.html.ImageView;

public class Controller {

    /* ======================= */
    /* VARIABLES AND CONST === */
    /* ======================= */
    @FXML private Pane playground;
    public static Pane playground_statlc;
    @FXML private javafx.scene.image.ImageView troop;
    @FXML private Text score_text;
    public static Text score_text_static;
    @FXML private Text lv_text;
    public static Text lv_text_static;

    @FXML
    void initialize() {

        playground_statlc = playground;
        score_text_static = score_text;
        lv_text_static = lv_text;

        // animate background troop
        TranslateTransition tf = new TranslateTransition(Duration.minutes(2), troop);
        tf.setToX(Main.SCREEN_WIDTH+100);
        tf.setCycleCount(Timeline.INDEFINITE);
        tf.setAutoReverse(true);
        tf.play();

    }
}
