package meteorite;


import com.sun.scenario.animation.SplineInterpolator;
import javafx.animation.*;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

/**
 * Created by teddy on 21/1/15.
 */
class Castle {

    private static int hp;
    public static SimpleDoubleProperty hpBind;
    public static Wrapper_info wrapper;
    public static Pos_info pos;

    public Castle() {
        hp = 100;
        hpBind = new SimpleDoubleProperty(Main.SCREEN.WIDTH);

        double width = 0;
        double height = 0;
        for (Node building : PlayController.castle_static.getChildren()) {
            if (building.getBoundsInParent().getHeight() > height)
                height = building.getBoundsInParent().getHeight();
            width += building.getBoundsInParent().getWidth();
        }

        wrapper = new Wrapper_info(Math.round(width), Math.round(height));
        pos = new Pos_info(Math.round(PlayController.castle_static.getLayoutX()), Math.round(PlayController.castle_static.getLayoutY()));
    }

    public static void hpDown(int decrement) {
        hp -= decrement;
        ParallelTransition pt;
        FadeTransition ft_1, ft_2;
        switch (hp / 10) {
            case 0:
            case 1:
                // coming to the end of life, enjoy
                ft_1 = new FadeTransition(Duration.seconds(Main.FADE_OUT_TIME), PlayController.castleImg_static);
                ft_1.setFromValue(1.0);
                ft_1.setToValue(0.5);
                ft_1.setCycleCount(Timeline.INDEFINITE);
                ft_1.setAutoReverse(true);
                ft_1.play();
            case 2:
                ft_1 = new FadeTransition(Duration.seconds(Main.FADE_OUT_TIME), PlayController.innerTree2_static);
                ft_1.setToValue(0);
                ft_1.setCycleCount(1);
                ft_1.setOnFinished(e -> PlayController.playground_static.getChildren().remove(PlayController.innerTree2_static));
                ft_1.play();
            case 3:
                // inner tree
                ft_1 = new FadeTransition(Duration.seconds(Main.FADE_OUT_TIME), PlayController.innerTree1_static);
                ft_1.setToValue(0);
                ft_1.setCycleCount(1);
                ft_1.setOnFinished(e -> PlayController.playground_static.getChildren().remove(PlayController.innerTree1_static));
                ft_1.play();
            case 4:
            case 5:
                // Sphinx
                ft_1 = new FadeTransition(Duration.seconds(Main.FADE_OUT_TIME), PlayController.sphinx_static);
                ft_1.setToValue(0);
                ft_1.setCycleCount(1);
                ft_1.setOnFinished(e -> PlayController.playground_static.getChildren().remove(PlayController.sphinx_static));
                ft_1.play();
            case 6:
            case 7:
                // Eiffel Tower
                ft_1 = new FadeTransition(Duration.seconds(Main.FADE_OUT_TIME), PlayController.eiffelTower_static);
                ft_1.setToValue(0);
                ft_1.setCycleCount(1);
                ft_1.setOnFinished(e -> PlayController.playground_static.getChildren().remove(PlayController.eiffelTower_static));
                ft_1.play();
            case 8:
                // Outer Tree 3+4
                ft_1 = new FadeTransition(Duration.seconds(Main.FADE_OUT_TIME), PlayController.outerTree3_static);
                ft_1.setToValue(0);
                ft_1.setCycleCount(1);
                ft_1.setOnFinished(e -> PlayController.playground_static.getChildren().remove(PlayController.outerTree3_static));
                ft_2 = new FadeTransition(Duration.seconds(Main.FADE_OUT_TIME), PlayController.outerTree4_static);
                ft_2.setToValue(0);
                ft_2.setCycleCount(1);
                ft_2.setOnFinished(e -> PlayController.playground_static.getChildren().remove(PlayController.outerTree4_static));
                pt = new ParallelTransition(ft_1, ft_2);
                pt.play();
            case 9:
                // Outer Tree 1+2
                ft_1 = new FadeTransition(Duration.seconds(Main.FADE_OUT_TIME), PlayController.outerTree1_static);
                ft_1.setToValue(0);
                ft_1.setCycleCount(1);
                ft_1.setOnFinished(e -> PlayController.playground_static.getChildren().remove(PlayController.outerTree1_static));
                ft_2 = new FadeTransition(Duration.seconds(Main.FADE_OUT_TIME), PlayController.outerTree2_static);
                ft_2.setToValue(0);
                ft_2.setCycleCount(1);
                ft_2.setOnFinished(e -> PlayController.playground_static.getChildren().remove(PlayController.outerTree2_static));
                pt = new ParallelTransition(ft_1, ft_2);
                pt.play();
        }
        hpBind.set(Main.SCREEN.WIDTH * hp / 100);
        if (hp <= 0) {
            if (!GameSystem.gameOver) {
                GameSystem.message("GAME OVER");
                GameSystem.gameEnd();
            }
        }
        if (GameSystem.gameOver) {
            SoundSystem.castleExplosion.play();
        } else {
            SoundSystem.treeCollapseSE.play();
        }
    }

    public static void hpUp() {
        // called only when level up
        hp = 100;
        hpBind.set(Main.SCREEN.WIDTH * hp / 100);
    }

    public static void shootWord(Word target) {
        // stop the word from moving
        if (target instanceof BossWord) {
            ((BossWord) target).dropTT.stop();
        } else {
            target.dropTT.stop();
        }

        // Sound Effect
        SoundSystem.rocketLaunchSE.play();

        // create bullet
        int bulletH = 10, bulletW = 10;
        Circle bullet = new Circle(bulletW, bulletH, 5, Color.rgb(153, 0, 0));
        PlayController.wordLayer_static.getChildren().add(bullet);

        Path shootPath = new Path();
        shootPath.getElements().add(new MoveTo(Math.round(pos.x + wrapper.width / 2), Math.round(pos.y + wrapper.height / 2)));
        if (target instanceof BossWord) {
            shootPath.getElements().add(new LineTo(target.getWordObj().getBoundsInParent().getMinX() + target.getWordObj().getBoundsInParent().getWidth() / 2, target.getWordObj().getBoundsInParent().getMinY() + target.getWordObj().getBoundsInParent().getHeight() / 2));
        } else {
            shootPath.getElements().add(new LineTo(target.pos.x + target.wrapper.width / 2, (target.dropTT.getNode().getBoundsInParent().getMinY() + target.dropTT.getNode().getBoundsInParent().getMaxY()) / 2));
        }
        PathTransition shootPT = new PathTransition(Duration.seconds(0.5), shootPath, bullet);
        shootPT.setInterpolator(new SplineInterpolator(0.1, 0, 1, 0));
        shootPT.setOnFinished(e -> {
            ScaleTransition enlargeBomb = new ScaleTransition(Duration.seconds(0.5), bullet);
            enlargeBomb.setByX(10);
            enlargeBomb.setByY(10);
            enlargeBomb.setOnFinished(enlarge -> {
                SoundSystem.wordExplode.play();
                PlayController.wordLayer_static.getChildren().remove(bullet);
                target.destroy(false);
            });
            enlargeBomb.play();
        });
        shootPT.play();
    }

}
