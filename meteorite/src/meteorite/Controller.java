package meteorite;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class Controller {

    /* ======================= */
    /* VARIABLES AND CONST === */
    /* ======================= */
    @FXML private Pane playground; // global playground


    @FXML
    void initialize() {

        Word.importPane(playground);
        GameSystem.importPane(playground);

    }
}
