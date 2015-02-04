package meteorite;

/**
 * Created by Alan on 21/1/2015.
 */
public class Wrapper_info {
    public long width;
    public final long height;

    public Wrapper_info() {
        width = 0;
        height = 0;
    }

    public Wrapper_info(long w, long h) {
        this.width = w;
        this.height = h;
    }
}
