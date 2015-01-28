package meteorite;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.sql.Time;
import java.util.ResourceBundle;
import java.io.IOException;

/**
 * Created by teddy on 21/1/15.
 */
public class Driver {

    @FXML private ResourceBundle resources;
    @FXML private Pane startingPage;
    @FXML private Button playBtn;
    @FXML private Button stopBtn;
    @FXML private ImageView logo_text;
    @FXML private ImageView logo_meteor;

    @FXML public void initialize() {
        // reposition logo
        logo_text.setTranslateX(Main.SCREEN.WIDTH);
        logo_meteor.setTranslateX(-Main.SCREEN.WIDTH);
        // animate logo
        TranslateTransition tt_txt = new TranslateTransition(Duration.seconds(2), logo_text);
        tt_txt.setInterpolator(Interpolator.SPLINE(0.3, 0, 0.2, 1));
        tt_txt.setToX(0);
        tt_txt.play();
        TranslateTransition tt_meteor = new TranslateTransition(Duration.seconds(2), logo_meteor);
        tt_meteor.setInterpolator(Interpolator.SPLINE(0.3, 0, 0.2, 1));
        tt_meteor.setToX(0);
        tt_meteor.play();
    }

    @FXML
    private void game_start(ActionEvent event) throws IOException {

        Parent root;

        if (event.getSource() == playBtn) {

            root = FXMLLoader.load(getClass().getResource("scene/GamingScreen.fxml"));
            Scene gameplay_scene = new Scene(root, Main.SCREEN.WIDTH, Main.SCREEN.HEIGHT);

            // animate controls fadeout


            // load up other FXML document
            Main.STAGE.setScene(gameplay_scene);
            Main.STAGE.show();

            // key listener
            Main.STAGE.getScene().setOnKeyPressed(e-> {
                GameSystem.handle_key_press(e.getText().toUpperCase());
            });

        }
    }

    /**
     * System: get words and spawn and drop word
     * System: check word count and decide level up or not
     * Word: word position and destroy the word if lower than 600 level
     * Castle: call hp_change() to change the castle hp
     * Castle: get_hp == 0 -> game over
     * System: store currPlayer record and rank the currPlayer
     * System: show game over scene(restart, close game)
     *
     */
    public static void game_update() {
        GameSystem.screen_update_lv();
        GameSystem.screen_update_score();
    }

    public void game_shutdown(ActionEvent event) throws IOException {

    }

    public void show_ranking(ActionEvent event) {
        GameSystem.create_rating_chart();
    }

    public void mute_BGM(ActionEvent event) {

    }

}
