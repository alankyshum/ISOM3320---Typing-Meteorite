package meteorite;

import javafx.animation.*;
import javafx.collections.ObservableArray;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.lang.reflect.Array;

/**
 * Created by teddy on 21/1/15.
 */
public class Driver {
    // Tracker
    public static boolean muted = false;
    // Constants
    public static final int SEC_DUR = 1;

    @FXML private Button playBtn;
    @FXML private Button rankBtn;
    @FXML private ToggleButton muteBtn;
    @FXML private Button backBtn;
    @FXML private ImageView logo_text;
    @FXML private ImageView logo_meteor;
    @FXML private VBox rankingChart;
    @FXML private Text rankingTitle;
    @FXML private Text rankingSubtitle;

    /* ======== HELPER FX */
    /* ================== */
    public void load_scene(String type) throws IOException {
        FXMLLoader sceneFile;
        Parent root;
        Scene scene;

        switch (type) {
            case "start":
                sceneFile = new FXMLLoader(getClass().getResource("scene/StartScreen.fxml"));
                root = sceneFile.load();
                scene = new Scene(root, Main.SCREEN.WIDTH, Main.SCREEN.HEIGHT);
                break;
            case "gaming":
                sceneFile = new FXMLLoader(getClass().getResource("scene/GamingScreen.fxml"));
                root = sceneFile.load();
                scene = new Scene(root, Main.SCREEN.WIDTH, Main.SCREEN.HEIGHT);
                scene.setOnKeyPressed(e -> {
                    GameSystem.handle_key_press(e.getText().toUpperCase());
                });
                break;
            case "ranking":
                sceneFile = new FXMLLoader(getClass().getResource("scene/BoardScreen.fxml"));
                sceneFile.setController(this);
                root = sceneFile.load();
                scene = new Scene(root, Main.SCREEN.WIDTH, Main.SCREEN.HEIGHT);
                break;
            default:
                // default to null, for throwing  error
                System.out.println("Error with type of scene loading");
                scene = null;
                break;
        }
        Main.STAGE.setScene(scene);
        Main.STAGE.show();
    }

    /* ======== FXML FX */
    /* ================ */

    @FXML public void hoverSoundFx() {
        SoundSystem.buttonSoundEffect.play(0.3);
    }

    @FXML public void initialize() {
        // reposition logo
        logo_text.setTranslateX(Main.SCREEN.WIDTH);
        logo_meteor.setTranslateX(-Main.SCREEN.WIDTH);
        // animate logo
        TranslateTransition tt_txt = new TranslateTransition(Duration.seconds(SEC_DUR), logo_text);
        tt_txt.setInterpolator(Interpolator.SPLINE(0.3, 0, 0.2, 1));
        tt_txt.setToX(0);
        TranslateTransition tt_meteor = new TranslateTransition(Duration.seconds(SEC_DUR), logo_meteor);
        tt_meteor.setInterpolator(Interpolator.SPLINE(0.3, 0, 0.2, 1));
        tt_meteor.setToX(0);
        TranslateTransition rank_tt = new TranslateTransition(Duration.seconds(SEC_DUR+0.5), rankBtn);
        rank_tt.setFromX(Main.SCREEN.WIDTH);
        rank_tt.setToX(0);
        TranslateTransition play_tt = new TranslateTransition(Duration.seconds(SEC_DUR+0.5), playBtn);
        play_tt.setFromX(-Main.SCREEN.WIDTH);
        play_tt.setToX(0);
        TranslateTransition mute_tt = new TranslateTransition(Duration.seconds(SEC_DUR+1), muteBtn);
        mute_tt.setFromY(Main.SCREEN.HEIGHT);
        mute_tt.setToY(0);
        ParallelTransition fadeOut = new ParallelTransition(tt_txt, tt_meteor, rank_tt, play_tt, mute_tt);
        fadeOut.play();

        // Initialise Sounds + Fonts
        SoundSystem.initSounds();
        Font.loadFont(Main.class.getResource("fonts/GOTHICB.TTF").toExternalForm(), 12);
        Font.loadFont(Main.class.getResource("fonts/Blitzwing.ttf").toExternalForm(), 12);
        Font.loadFont(Main.class.getResource("fonts/Coburn.otf").toExternalForm(), 12);
        Font.loadFont(Main.class.getResource("fonts/editundo.ttf").toExternalForm(), 12);
        Font.loadFont(Main.class.getResource("fonts/Minecrafter.ttf").toExternalForm(), 12);

        // Load player list
        GameSystem.player_list.clear();
        try {
            GameSystem.load_to_player_list();
        } catch (Exception e) {
            System.out.println(e);
        }
        // load boss word list
        try {
            GameSystem.load_to_boss_list();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    @FXML
    private void game_start() {
        // animate controls fadeout
        TranslateTransition logo_tt = new TranslateTransition(Duration.seconds(SEC_DUR), logo_text);
        logo_tt.setByX(Main.SCREEN.WIDTH);
        logo_tt.setCycleCount(1);
        logo_tt.setOnFinished(ev -> {
            // load scene
            try {
                load_scene("gaming");
            } catch (IOException io_e) {
                System.out.println(io_e);
            }
            // load BGM
            SoundSystem.BGM.play();
        });

        TranslateTransition img_tt = new TranslateTransition(Duration.seconds(SEC_DUR), logo_meteor);
        img_tt.setByX(-Main.SCREEN.WIDTH);
        TranslateTransition rank_tt = new TranslateTransition(Duration.seconds(SEC_DUR+0.5), rankBtn);
        rank_tt.setByX(Main.SCREEN.WIDTH);
        TranslateTransition play_tt = new TranslateTransition(Duration.seconds(SEC_DUR+0.5), playBtn);
        play_tt.setByX(-Main.SCREEN.WIDTH);
        TranslateTransition mute_tt = new TranslateTransition(Duration.seconds(SEC_DUR+1), muteBtn);
        mute_tt.setToY(Main.SCREEN.HEIGHT);
        ParallelTransition fadeOut = new ParallelTransition(logo_tt, img_tt, rank_tt, play_tt, mute_tt);
        fadeOut.play();
    }

    @FXML
    public void show_ranking(ActionEvent event) {
        // animate controls fadeout
        TranslateTransition logo_tt = new TranslateTransition(Duration.seconds(SEC_DUR), logo_text);
        logo_tt.setByX(Main.SCREEN.WIDTH);
        TranslateTransition img_tt = new TranslateTransition(Duration.seconds(SEC_DUR), logo_meteor);
        img_tt.setByX(-Main.SCREEN.WIDTH);
        TranslateTransition rank_tt = new TranslateTransition(Duration.seconds(SEC_DUR), rankBtn);
        rank_tt.setByX(Main.SCREEN.WIDTH);
        TranslateTransition play_tt = new TranslateTransition(Duration.seconds(SEC_DUR), playBtn);
        play_tt.setByX(-Main.SCREEN.WIDTH);
        TranslateTransition mute_tt = new TranslateTransition(Duration.seconds(SEC_DUR), muteBtn);
        mute_tt.setToY(Main.SCREEN.HEIGHT);
        ParallelTransition fadeOut = new ParallelTransition(logo_tt, img_tt, play_tt, rank_tt, mute_tt);
        fadeOut.setOnFinished(e-> {
            // load scene
            try {
                load_scene("ranking");
            } catch (IOException io_e) {
                System.out.println(io_e);
            }
            int width = 120;
            GameSystem.player_list.forEach(player -> {
                HBox row = new HBox();
                row.setAlignment(Pos.CENTER);
                row.setPadding(new Insets(10, 10, 10, 20));
                HBox lv = new HBox(new Text(0, 0, Integer.toString(player.get_lv())));
                lv.setPrefSize(width, 0);
                lv.setAlignment(Pos.CENTER);
                HBox name = new HBox(new Text(0, 0, player.get_name()));
                name.setPrefSize(width, 0);
                name.setAlignment(Pos.CENTER);
                HBox score = new HBox(new Text(0, 0, Integer.toString(player.get_score())));
                score.setPrefSize(width, 0);
                score.setAlignment(Pos.CENTER);

                row.getChildren().addAll(lv, name, score);
                rankingChart.getChildren().add(row);
            });

            TranslateTransition rankTitleTT = new TranslateTransition(Duration.seconds(SEC_DUR), rankingTitle);
            rankTitleTT.setFromY(-500);
            rankTitleTT.setToY(0);
            TranslateTransition rankSubtitleTT = new TranslateTransition(Duration.seconds(SEC_DUR), rankingSubtitle);
            rankSubtitleTT.setFromY(-500);
            rankSubtitleTT.setToY(0);
            TranslateTransition chartTT = new TranslateTransition(Duration.seconds(SEC_DUR), rankingChart);
            chartTT.setFromY(Main.SCREEN.HEIGHT);
            chartTT.setToY(0);
            TranslateTransition backBtnTT = new TranslateTransition(Duration.seconds(SEC_DUR), backBtn);
            backBtnTT.setFromY(Main.SCREEN.HEIGHT);
            backBtnTT.setToY(0);
            ParallelTransition pt = new ParallelTransition(rankTitleTT, rankSubtitleTT, chartTT, backBtnTT);
            pt.play();
        });
        fadeOut.play();
    }

    @FXML public void backToStart(ActionEvent event) {
        TranslateTransition rankTitleTT = new TranslateTransition(Duration.seconds(SEC_DUR), rankingTitle);
        rankTitleTT.setToY(-500);
        TranslateTransition rankSubtitleTT = new TranslateTransition(Duration.seconds(SEC_DUR), rankingSubtitle);
        rankSubtitleTT.setToY(-500);
        TranslateTransition chartTT = new TranslateTransition(Duration.seconds(SEC_DUR), rankingChart);
        chartTT.setToY(Main.SCREEN.HEIGHT);
        TranslateTransition backBtnTT = new TranslateTransition(Duration.seconds(SEC_DUR), backBtn);
        backBtnTT.setToY(Main.SCREEN.HEIGHT);
        ParallelTransition pt = new ParallelTransition(rankTitleTT, rankSubtitleTT, chartTT, backBtnTT);
        pt.setOnFinished(e -> {
            try {
                load_scene("start");
            } catch (IOException io_e) {
                System.out.println(io_e);
            }
        });
        pt.play();
    }

    @FXML
    public void mute_BGM() {
        if (muted) {
            SoundSystem.BGM.setVolume(1);
            muted = false;
        } else {
            SoundSystem.BGM.setVolume(0);
            muted = true;
        }
    }

}
