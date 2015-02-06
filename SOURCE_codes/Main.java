package meteorite;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    // CONSTANTS
    public final static int DROP_DUR_BASE = 10000; // Word Drop duration base
    public static final int LV_UP_THRESHOLD = 20; // words to type to finish the level
    public static final double GEN_WORD_INTERVAL = 1.5; // Time interval to generate words
    public static final double FADE_OUT_TIME = 1.5; // Time to fadeout destroyed item
    public static final int NUM_RECORDS = 7; // number of records in honor board
    public static Stage STAGE;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("scene/StartScreen.fxml"));
        STAGE = primaryStage;

        // set other information
        STAGE.setTitle("Word Meteorite v.0.1");
        STAGE.setScene(new Scene(root, SCREEN.WIDTH, SCREEN.HEIGHT));
        STAGE.show();

        // dynamically set screen size
        STAGE.setMinHeight(Main.SCREEN.HEIGHT);
        STAGE.setMinWidth(Main.SCREEN.WIDTH);
        STAGE.setMaxHeight(Main.SCREEN.HEIGHT);
        STAGE.setMaxWidth(Main.SCREEN.WIDTH);
    }

    class SCREEN {
        public static final int HEIGHT = 935;
        public static final int WIDTH = 515;
    }
}
