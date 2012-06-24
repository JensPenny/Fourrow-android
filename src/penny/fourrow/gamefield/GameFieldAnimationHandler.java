package penny.fourrow.gamefield;

import android.app.Application;
import android.app.Service;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import penny.fourrow.logic.Direction;
import penny.fourrow.logic.Player;
import penny.fourrow.viewlogic.R;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Jens
 * Date: 24/06/12
 * Time: 13:10
 * To change this template use File | Settings | File Templates.
 */
public class GameFieldAnimationHandler{

    private int player1StartAnimation = R.drawable.animate_player1;
    private int player1EndAnimation;
    private int player2StartAnimation = R.drawable.animate_player2;

    public GameFieldAnimationHandler(){

    }

    public void doAnimation(List<ImageView> toAnimateViews, Direction direction, final Player player){
        int animation = 0;
        if (player.playerColorResource == R.drawable.p1cell){
            animation =  player1StartAnimation;
        }else if (player.playerColorResource == R.drawable.p2cell){
            animation = player2StartAnimation;
        }

        final AnimationDrawable animationDrawable;
        toAnimateViews.get(0).setBackgroundResource(animation);
        animationDrawable = (AnimationDrawable)toAnimateViews.get(0).getBackground();
        animationDrawable.start();
        /*
        for (ImageView view : toAnimateViews){
            Animation frameAnimation = AnimationUtils.loadAnimation(view.getContext().getApplicationContext(), animation);
            final ImageView currentView = view;
            frameAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    System.out.printf("Animation started");
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    currentView.setBackgroundResource(player.playerColorResource);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    System.out.printf("Animation repeated");
                }
            });
            frameAnimation.start();
        }
        */
    }
}
