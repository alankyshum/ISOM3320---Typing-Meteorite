package meteorite;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

/**
 * Created by Alan on 2/2/2015.
 */

public class BossWord extends Word {

    public Timeline drop_tt;

    public BossWord(Pos_info pos, double level, String content) {
        // boss words always drops at the same speed
        super(pos, 1, content);
        wrapper.width = 300;
        get_word_obj().setMaxWidth(wrapper.width);
        get_word_obj().setAlignment(Pos.CENTER);
    }

    @Override
    public void drop() {
        drop_tt = new Timeline(new KeyFrame(Duration.seconds(1), e-> {
            this.get_word_obj().setLayoutY(this.get_word_obj().getLayoutY() + 10);
            if (this.get_word_obj().getBoundsInParent().getMaxY() >= PlayController.castle_static.getBoundsInParent().getMinY()) {
                this.destroy(true);
                drop_tt.stop();
            }
        }));
        drop_tt.setCycleCount(Timeline.INDEFINITE);
        drop_tt.play();
    }

}
