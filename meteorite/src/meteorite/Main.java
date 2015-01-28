package meteorite;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    class SCREEN {
        public static final int HEIGHT = 935;
        public static final int WIDTH = 515;
    }

    public static Stage STAGE;

    @Override
    public void start(Stage primaryStage) throws Exception{
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


    public static void main(String[] args) {
        launch(args);
    }
}
