package penny.fourrow.viewlogic;


import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import penny.fourrow.activity.R;

/**
 * Created with IntelliJ IDEA.
 * User: Jens
 * Date: 28/05/12
 * Time: 17:45
 * To change this template use File | Settings | File Templates.
 */
public class AnimationHandler {

    private ImageView[] viewArray;

    private int animationPlayer1 = R.drawable.animate_player1;
    private int animationPlayer2 = R.drawable.animate_player2;

    private final Activity caller;

    public AnimationHandler(Activity mainActivity){
        caller = mainActivity;
    }

    public AnimationHandler(Activity mainActivity, ImageView[] viewArray){
        this.viewArray = viewArray;
        caller = mainActivity;
    }

    public void setViewArray(ImageView[] viewArray){
        this.viewArray = viewArray;
    }

    public void animateViewArray(){
        AnimationDrawable animation = (AnimationDrawable)caller.getResources().getDrawable(R.drawable.animate_player1);

        for (ImageView view : viewArray){
            Drawable originalBackground = view.getBackground();
        }
    }

    public void animateRow(){

    }


}
