package penny.fourrow.viewlogic;

import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.*;
import penny.fourrow.gamefield.GameFieldView;
import penny.fourrow.logic.GameController;
import penny.fourrow.logic.GameFinishedEvent;
import penny.fourrow.logic.GameFinishedListener;
import penny.fourrow.logic.Player;
import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;

public class FourRowPMActivity extends Activity implements GameFinishedListener {
	
	private static GameController controller = GameController.INSTANCE;
	private boolean activityFirstStarted = false;

    private GameFieldView gameFieldView;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playingfieldview);
        initializeController();
        initializeTests();
    }

    private void initializeTests() {
        LinearLayout lolout = (LinearLayout)findViewById(R.id.player2container);
        Button b = new Button(this);
        b.setWidth(100);
        b.setHeight(50);
        b.setText("Animatie");
        ImageView v = new ImageView(this);
        v.setBackgroundResource(R.drawable.animate_player1);
        final AnimationDrawable drawable = (AnimationDrawable)v.getBackground();
        drawable.setOneShot(true);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawable.stop();
                drawable.start();
            }
        });
        lolout.addView(b);
        lolout.addView(v);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
    	if (!activityFirstStarted && hasFocus){ //WARNING: won't update relative cell size when app stays but window changes
    		gameFieldView.addGameFinishedListener(this);
            gameFieldView.recalculateRelativeCellSize();
    		activityFirstStarted = true;
    	}
    }
    
    private void initializeController()
    {
    	controller.addPlayer(new Player("Jens", R.drawable.p1cell));
    	controller.addPlayer(new Player("Test", R.drawable.p2cell));
    	controller.initNewGame(FourRowBasicParameters.BASE_ROWS, FourRowBasicParameters.BASE_COLUMNS); //Initialiseert de gamecontroller
        gameFieldView = new GameFieldView((TableLayout)findViewById(R.id.tblPlayingField));
        gameFieldView.initializeFirstMove(new Point(4, 4), R.color.occupied);
        controller.setGameFieldView(gameFieldView);
        controller.startGame();
    }

    @Override
    public void gamefinishedEventHappened(GameFinishedEvent event) {
        Player winner = controller.getNextPlayer();
        Toast t = Toast.makeText(getApplicationContext(), "Winner! Congratulations " + winner.name, Toast.LENGTH_LONG);
        t.show();
        gameFieldView.resetGameField();
    }
}