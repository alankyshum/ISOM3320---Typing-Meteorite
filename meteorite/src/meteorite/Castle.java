package meteorite;


/**
 * Created by teddy on 21/1/15.
 */
public class Castle {

    private int hp;
    private Wrapper_info wrapper;

    public Castle() {
        hp = 100;
        wrapper = new Wrapper_info(Main.SCREEN.WIDTH, 300);
    }

    public void hp_change(){
        hp -= 10;
    }

    public int get_hp(){
        return hp;
    }

}
