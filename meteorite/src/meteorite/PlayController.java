package meteorite;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;

public class PlayController {

    public static Timeline genWordTimer_static;
    // Static property for static fx access
    public static Pane playground_static;
    public static Pane word_layer_static;
    public static Text score_text_static;
    public static Text lv_text_static;
    public static Text msg_static;
    public static VBox castle_static;
    public static VBox endGamePanel_static;
    public static Button stopBtn_static;
    public static Text scoreText_final_static;
    public static Text levelText_final_static;
    public static TextField playerName_static;
    public static Button saveBtn_static;
    public static Button homeBtn_static;
    public static ImageView outerTree1_static;
    public static ImageView outerTree2_static;
    public static ImageView outerTree3_static;
    public static ImageView outerTree4_static;
    public static ImageView innerTree1_static;
    public static ImageView innerTree2_static;
    public static ImageView sphinx_static;
    public static ImageView eiffelTower_static;
    public static ImageView castleImg_static;
    /* ================ */
    /* TRACKERS ======= */
    /* ================ */
    private Timeline genWordTimer;
    /* ======================= */
    /* VARIABLES AND CONST === */
    /* ======================= */
    @FXML
    private HBox lava;
    @FXML
    private Pane playground;
    @FXML
    private Pane word_layer;
    @FXML
    private Text score_text;
    @FXML
    private Text lv_text;
    @FXML
    private Text msg;
    @FXML
    private VBox castle;
    @FXML
    private Rectangle hp_bar;
    @FXML
    private Button stopBtn;
    @FXML
    private Button saveBtn;
    @FXML
    private Button homeBtn;
    @FXML
    private VBox endGamePanel;
    @FXML
    private Text scoreText_final;
    @FXML
    private Text levelText_final;
    @FXML
    private TextField playerName;
    // HP related animation elements
    @FXML
    private ImageView outerTree1;
    @FXML
    private ImageView outerTree2;
    @FXML
    private ImageView outerTree3;
    @FXML
    private ImageView outerTree4;
    @FXML
    private ImageView innerTree1;
    @FXML
    private ImageView innerTree2;
    @FXML
    private ImageView sphinx;
    @FXML
    private ImageView eiffelTower;
    @FXML
    private ImageView castleImg;

    @FXML
    void initialize() {

        castle_static = castle;
        new Castle();
        playground_static = playground;
        word_layer_static = word_layer;
        score_text_static = score_text;
        lv_text_static = lv_text;
        scoreText_final_static = scoreText_final;
        levelText_final_static = levelText_final;
        playerName_static = playerName;
        stopBtn_static = stopBtn;
        homeBtn_static = homeBtn;
        saveBtn_static = saveBtn;
        endGamePanel_static = endGamePanel;
        msg_static = msg;
        hp_bar.widthProperty().bind(Castle.hp_bind);

        // static variables for external access
        outerTree1_static = outerTree1;
        outerTree2_static = outerTree2;
        outerTree3_static = outerTree3;
        outerTree4_static = outerTree4;
        innerTree1_static = innerTree1;
        innerTree2_static = innerTree2;
        sphinx_static = sphinx;
        eiffelTower_static = eiffelTower;
        castleImg_static = castleImg;

        // animate lava
        TranslateTransition tf = new TranslateTransition(Duration.seconds(25), lava);
        tf.setByX(-50);
        tf.setCycleCount(Timeline.INDEFINITE);
        tf.setAutoReverse(true);
        tf.play();

        // Create Player
        GameSystem.create_player();

        // Initialise status
        GameSystem.gameOver = false;

        // Load Words + Generate Words
        try {
            GameSystem.word_list.clear();
            GameSystem.load_to_word_list(GameSystem.currPlayer.get_lv());
        } catch (IOException e) {
            System.out.println(e);
        } finally {
            genWordTimer = new Timeline(new KeyFrame(Duration.seconds(Main.GEN_WORD_INTERVAL), e -> {
                GameSystem.spawn_n_drop_word();
            }));
            genWordTimer.setCycleCount(Timeline.INDEFINITE);
            genWordTimer.play();
            genWordTimer_static = genWordTimer;
        }

        // animate background stars
        Timeline genStar = new Timeline(new KeyFrame(Duration.seconds(0.7), e -> {
            Rectangle star = new Rectangle();
            star.setWidth(5);
            star.setHeight(5);
            star.getStyleClass().add("star");
            star.setLayoutX(Math.random() * Main.SCREEN.WIDTH);
            star.setLayoutY(Math.random() * (Main.SCREEN.HEIGHT * 0.6));
            word_layer.getChildren().add(star);
            FadeTransition star_ft = new FadeTransition(Duration.seconds(10), star);
            star_ft.setToValue(0);
            star_ft.setCycleCount(1);
            star_ft.setOnFinished(ev -> {
                word_layer.getChildren().remove(star);
            });
            star_ft.play();
        }));
        genStar.setCycleCount(Timeline.INDEFINITE);
        genStar.play();
    }

    @FXML
    public void pauseGame() {
        GameSystem.gamePause();
    }

    @FXML
    public void endGame() {
        GameSystem.game_end();
    }

    @FXML
    public void backHome() {
        Driver dr = new Driver();

        TranslateTransition panelTT = new TranslateTransition(Duration.seconds(1), PlayController.endGamePanel_static);
        panelTT.setByY(Main.SCREEN.HEIGHT / 2 + 100);
        panelTT.setOnFinished(e -> {
            // Bug in JavaFx, needa load twice to by-pass loadException
            try {
                dr.load_scene("start");
            } catch (Exception ex) {
//                System.out.println(ex);
                try {
                    dr.load_scene("start");
                } catch (Exception ex1) {
                    System.out.println(ex1);
                }
            }
        });
        panelTT.play();
    }

    @FXML
    public void savePlayer() {
        String name = playerName.getText();
        if (name.length() > 15) {
            name = name.substring(0, 15) + "...";
        }
        GameSystem.currPlayer.set_name(name);
        try {
            GameSystem.save_player(GameSystem.currPlayer);
        } catch (IOException ex) {
            System.out.println(ex);
        }
        saveBtn.setDisable(true);
    }

    @FXML
    public void hoverSoundFx() {
        SoundSystem.buttonSoundEffect.play(0.3);
    }
}
