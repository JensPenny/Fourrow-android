package penny.fourrow.viewlogic;

import java.util.Observable;
import java.util.Observer;

import penny.fourrow.activity.R;
import penny.fourrow.logic.GameController;
import penny.fourrow.logic.GameFinishedEvent;
import penny.fourrow.logic.GameFinishedListener;
import penny.fourrow.logic.IGameControl;
import penny.fourrow.logic.Player;
import support.PointPair;
import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class FourRowPMActivity extends Activity implements Observer, GameFinishedListener{
	
	private static GameController controller = new GameController();
	private boolean activityFirstStarted = false;
	
	private final PointPair pointPair = new PointPair();
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playingfieldview);        
        buildPlayingField();
        
        //TODO: refactor naar iets dynamisch
        controller.getPlayingField().initializeFirstMove(new Point(4,4), R.color.occupied);
    }
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
    	if (!activityFirstStarted && hasFocus){
    		controller.getPlayingField().addGameFinishedListener(this);
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
    	createPlayingFieldView((TableLayout)findViewById(R.id.tblPlayingField), rows, columns);
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
				//button.setMinimumWidth(30);
				//button.setMinimumHeight(30);
				//button.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				//button.setBackgroundResource(R.drawable.drawable_border);
				row.addView(button);
				button.setOnTouchListener(new OnTouchListener() {	
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						switch(event.getAction())
						{
						case MotionEvent.ACTION_UP:
							//TODO: Motion event opvangen -> voorbeeldcode = http://www.ceveni.com/2009/08/android-gestures-detection-sample-code.html
							pointPair.setSecondPoint((Point)v.getTag());
							FourRowPMActivity.controller.getLogger().info("Recorded pointpair: " + pointPair.toString());
							return true;
						
						case MotionEvent.ACTION_DOWN:
							ImageView view = (ImageView)v;
							FourRowPMActivity.controller.playerMakesMove((Point)v.getTag());
							TextView playerLabel = (TextView)findViewById(R.id.lblPlayer);
							playerLabel.setText(controller.getNextPlayer().name);
							pointPair.setFirstPoint((Point)v.getTag());
							return true;
							
						case MotionEvent.ACTION_CANCEL: //Opvangen foutieve cancels van actie door OS
							return false;
						default:
							return false;
						}
					}
				});
			}
			layout.addView(row);
		}

    }

	@Override
	public void update(Observable observable, Object data) {
		Point p = (Point)data;
		TableLayout playingfield = (TableLayout)findViewById(R.id.tblPlayingField);
		TableRow row = (TableRow)playingfield.getChildAt(p.y);
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