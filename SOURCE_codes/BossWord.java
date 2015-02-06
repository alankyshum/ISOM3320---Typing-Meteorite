package meteorite;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.util.Duration;

/**
 * Created by Alan on 2/2/2015.
 */
public class BossWord extends Word {

    public Timeline dropTT;

    public BossWord(Pos_info pos, String content) {
        // boss words always drops at the same speed
        super(pos, 1, content);
        wrapper.width = 300;
        getWordObj().setMaxWidth(wrapper.width);
        getWordObj().setAlignment(Pos.CENTER);
    }

    @Override
    public void drop() {
//        PlayController.playground_static.getChildren().add(new Circle(PlayController.castle_static.getBoundsInParent().getMinX(), PlayController.castle_static.getBoundsInParent().getMinY(), 5, Color.RED));
        dropTT = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            this.getWordObj().setLayoutY(this.getWordObj().getLayoutY() + 10);
//            PlayController.playground_static.getChildren().add(new Circle(this.getWordObj().getLayoutX(), this.getWordObj().getBoundsInParent().getMaxY(), 5, Color.RED));
            if (this.getWordObj().getBoundsInParent().getMaxY() >= PlayController.castle_static.getBoundsInParent().getMinY()+20) {
                this.destroy(true);
                dropTT.stop();
            }
        }));
        dropTT.setCycleCount(Timeline.INDEFINITE);
        dropTT.play();
    }

}
