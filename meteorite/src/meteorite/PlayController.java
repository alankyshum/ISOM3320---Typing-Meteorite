package meteorite;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;

public class PlayController {

    /* ======================= */
    /* VARIABLES AND CONST === */
    /* ======================= */
    @FXML private HBox lava;
    @FXML private Pane playground;
    public static Pane playground_statlc;
    @FXML private Text score_text;
    public static Text score_text_static;
    @FXML private Text lv_text;
    public static Text lv_text_static;

    @FXML void initialize() {

        playground_statlc = playground;
        score_text_static = score_text;
        lv_text_static = lv_text;

        // animate lava
        TranslateTransition tf = new TranslateTransition(Duration.seconds(25), lava);
        tf.setByX(-50);
        tf.setCycleCount(Timeline.INDEFINITE);
        tf.setAutoReverse(true);
        tf.play();

        // Create Player
        GameSystem.create_player();

        // Load Words + Generate Words
        try {
            GameSystem.load_to_word_list(GameSystem.currPlayer.get_lv());
        } catch (IOException e) {
            System.out.println(e);
        } finally {
            Timeline timeGen = new Timeline(new KeyFrame(Duration.seconds(1), e-> {
                GameSystem.spawn_n_drop_word();
            }));
            timeGen.setCycleCount(Timeline.INDEFINITE);
            timeGen.play();
        }

        // animate background stars
        Timeline genStar = new Timeline(new KeyFrame(Duration.seconds(1), e-> {
            Rectangle star = new Rectangle();
            star.setWidth(5);
            star.setHeight(5);
            star.getStyleClass().add("star");
            star.setLayoutX(Math.random()*Main.SCREEN.WIDTH);
            star.setLayoutY(Math.random()*(Main.SCREEN.HEIGHT-300));
            playground.getChildren().add(star);
            FadeTransition star_ft = new FadeTransition(Duration.seconds(10), star);
            star_ft.setToValue(0);
            star_ft.setCycleCount(1);
            star_ft.setOnFinished(ev-> {
                playground.getChildren().remove(star);
            });
            star_ft.play();
        }));
        genStar.setCycleCount(Timeline.INDEFINITE);
        genStar.play();

    }
}
