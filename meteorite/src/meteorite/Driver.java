package meteorite;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ResourceBundle;
import java.io.IOException;

/**
 * Created by teddy on 21/1/15.
 */
public class Driver {

    @FXML
    private ResourceBundle resources;

    @FXML
    private Button playBtn;
    @FXML
    private Button stopBtn;

    @FXML
    private void game_start(ActionEvent event) throws IOException{

        Stage stage;
        Parent root;

        if (event.getSource()==playBtn){

            // get reference to the button's stage
            stage = (Stage) playBtn.getScene().getWindow();

            root = FXMLLoader.load(getClass().getResource("scene/Game_Play_Screen.fxml"));
            Scene gameplay_scene = new Scene(root, Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
            gameplay_scene.getStylesheets().addAll(Main.class.getResource("css/master.css").toExternalForm());

            // load up other FXML document
            stage.setScene(gameplay_scene);
            stage.show();

            // key listener
            stage.getScene().setOnKeyPressed(e-> {
                GameSystem.handle_key_press(e.getText().toUpperCase());
            });

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

    public void game_shutdown(ActionEvent event) throws IOException{}

}
