package penny.fourrow.viewlogic;

import java.util.Observable;
import java.util.Observer;

import android.view.GestureDetector;
import penny.fourrow.activity.R;
import penny.fourrow.logic.GameController;
import penny.fourrow.logic.GameFinishedEvent;
import penny.fourrow.logic.GameFinishedListener;
import penny.fourrow.logic.Player;
import support.PointPair;
import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

public class FourRowPMActivity extends Activity implements Observer, GameFinishedListener {
	
	private static GameController controller = new GameController();
	private boolean activityFirstStarted = false;
	
	private final PointPair pointPair = new PointPair();

    private GestureDetector detector;
    TableLayoutGestureListener gestureListener;
    private TableLayout playingFieldView;

    private View.OnTouchListener touchListener = new OnTouchListener(){
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return detector.onTouchEvent(motionEvent);
        }
    };
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playingfieldview);
        playingFieldView = (TableLayout)findViewById(R.id.tblPlayingField);
        buildPlayingField();
        
        //TODO: refactor naar iets dynamisch
        controller.getPlayingField().initializeFirstMove(new Point(4,4), R.color.occupied);

        //Gesturedetector initialiseren adhv gerenderde playingFieldView + listener setten
        gestureListener = new TableLayoutGestureListener(playingFieldView, controller); //Must be able to do moves. In dire need of a refactor
        detector = new GestureDetector(gestureListener); //TODO:callback handlen
        playingFieldView.setOnTouchListener(touchListener);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
    	if (!activityFirstStarted && hasFocus){ //WARNING: won't update relative cell size when app stays but window changes
    		controller.getPlayingField().addGameFinishedListener(this);
            gestureListener.recalculateRelativeCellSize();
    		activityFirstStarted = true;
    	}
    }
    
    private void buildPlayingField()
    {
    	//TODO: softcode
    	int rows = 9;
    	int columns = 9;
    	
    	controller.addPlayer(new Player("Jens", R.drawable.p1cell));
    	controller.addPlayer(new Player("Test", R.drawable.p2cell));
    	controller.initNewGame(rows, columns); //Initialiseert de gamecontroller
    	controller.getPlayingField().addObserver(this);
    	controller.startGame();
    	createPlayingFieldView(playingFieldView, rows, columns);
    }
    private void createPlayingFieldView(TableLayout layout, int rows, int columns)
    {
		for (int i = 0; i < rows; i++) //Row: Y value - Top to bottom
		{
			TableRow row = new TableRow(layout.getContext());
			row.setTag(i);
			for (int j = 0; j < columns; j++){ //Col: X - value: Left - Right
				ImageView button = new ImageView(row.getContext());
				button.setPadding(0, 0, 0, 0);
				button.setImageResource(R.drawable.emptycell);
				button.setTag(new Point(j, i));
                button.setClickable(false);
				row.addView(button);
			}
			layout.addView(row);
		}
    }

	@Override
	public void update(Observable observable, Object data) {
		Point p = (Point)data;
		TableRow row = (TableRow) playingFieldView.getChildAt(p.y);
		ImageView v = (ImageView)row.getChildAt(p.x);
		v.setImageResource(controller.getPlayingField().gameField[p.x][p.y]);
	}

	@Override
	public void gamefinishedEventHappened(GameFinishedEvent event) {
		Player winner = controller.getNextPlayer();
		Toast t = Toast.makeText(getApplicationContext(), "Winner! Congratulations " + winner.name, Toast.LENGTH_LONG);
		t.show();
		controller.getPlayingField().resetGameField(9, 9);
	}
}