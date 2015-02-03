package meteorite;

import javafx.animation.Timeline;
import javafx.scene.media.AudioClip;

public class SoundSystem {

    public static AudioClip BGM;
    public static AudioClip BGM_Start;
    public static AudioClip wordExplode;
    public static AudioClip buttonSoundEffect;
    public static AudioClip castleExplosion;
    public static AudioClip rocketLaunchSE;
    public static AudioClip treeCollapseSE;

    public static void initSounds() {
        BGM = new AudioClip(Main.class.getResource("music/BGM.wav").toString());
        BGM.setCycleCount(Timeline.INDEFINITE);
        BGM.setVolume(0.8);

        BGM_Start = new AudioClip(Main.class.getResource("music/BGM_Start.wav").toString());
        BGM_Start.setCycleCount(Timeline.INDEFINITE);
        BGM_Start.setVolume(0.5);

        wordExplode = new AudioClip(Main.class.getResource("music/damage.wav").toString());
        wordExplode.setCycleCount(1);

        buttonSoundEffect = new AudioClip(Main.class.getResource("music/button.wav").toString());
        buttonSoundEffect.setCycleCount(1);

        castleExplosion = new AudioClip(Main.class.getResource("music/castle.wav").toString());
        castleExplosion.setCycleCount(1);

        rocketLaunchSE = new AudioClip(Main.class.getResource("music/rocketLaunch.wav").toString());
        rocketLaunchSE.setCycleCount(1);

        treeCollapseSE = new AudioClip(Main.class.getResource("music/treeCollapse.wav").toString());
        treeCollapseSE.setCycleCount(1);
    }

}
