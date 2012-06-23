package penny.fourrow.viewlogic;

import penny.fourrow.gamefield.GameFieldView;
import penny.fourrow.logic.GameController;
import penny.fourrow.logic.GameFinishedEvent;
import penny.fourrow.logic.GameFinishedListener;
import penny.fourrow.logic.Player;
import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.Toast;

public class FourRowPMActivity extends Activity implements GameFinishedListener {
	
	private static GameController controller = GameController.INSTANCE;
	private boolean activityFirstStarted = false;

    private GameFieldView gameFieldView;
    private AnimationHandler animationHandler;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playingfieldview);
        initializeController();
        animationHandler = new AnimationHandler(this);

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