package penny.fourrow.gamefield;

import android.graphics.Point;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import penny.fourrow.logic.Direction;
import penny.fourrow.logic.GameFinishedListener;
import penny.fourrow.logic.Player;
import penny.fourrow.viewlogic.FourRowBasicParameters;
import penny.fourrow.viewlogic.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created with IntelliJ IDEA
 * All manipulations on the gamefield will be done on this view
 * Logic calls like doing a move will be routed to the logic
 */
public class GameFieldView implements Observer {

    private TableLayout tableLayout;
    private GameFieldLogic linkedGameFieldLogic; //Contains the effective logic and moves of the view
    int amountRows, amountColumns;

    //Klassen voor gesturedetectie
    private GestureDetector gestureDetector;
    //Listener die aan deze view gekoppeld is
    GameFieldGestureListener gestureListener;

    //Animaties
    GameFieldAnimationHandler animationHandler;

    public GameFieldView(TableLayout viewById){
        tableLayout = viewById;
        init(amountRows, amountColumns);
    }

    public int getAmountRows() {
        return amountRows;
    }

    public void setAmountRows(int amountRows) {
        this.amountRows = amountRows;
    }

    public int getAmountColumns() {
        return amountColumns;
    }

    public void setAmountColumns(int amountColumns) {
        this.amountColumns = amountColumns;
    }

    public TableLayout getConstructedGameFieldTableLayout(){
        return tableLayout;
    }

    private void init(int rows, int columns){
        if (rows == 0)
            setAmountRows(FourRowBasicParameters.BASE_ROWS);
        if (columns == 0)
            setAmountColumns(FourRowBasicParameters.BASE_COLUMNS);

        linkedGameFieldLogic = new GameFieldLogic(getAmountRows(), getAmountColumns());
        linkedGameFieldLogic.addObserver(this);

        //Build the gui view
        createPlayingFieldView();

        //Setup the gesturemanager
        gestureListener = new GameFieldGestureListener(this); //Must be able to do moves. In dire need of a refactor
        gestureDetector = new GestureDetector(gestureListener); //TODO:callback handlen
        tableLayout.setOnTouchListener(touchListener);
        gestureListener.recalculateRelativeCellSize();

        animationHandler = new GameFieldAnimationHandler();
    }

    private void createPlayingFieldView()
    {
        for (int i = 0; i < amountRows; i++) //Row: Y value - Top to bottom
        {
            TableRow row = new TableRow(tableLayout.getContext());
            row.setTag(i);
            for (int j = 0; j < amountColumns; j++){ //Col: X - value: Left - Right
                ImageView button = new ImageView(row.getContext());
                button.setPadding(0, 0, 0, 0);
                button.setImageResource(R.drawable.emptycell);
                button.setTag(new Point(j, i));
                button.setClickable(false);
                row.addView(button);
            }
            tableLayout.addView(row);
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        Point p = (Point)data;
        TableRow row = (TableRow) tableLayout.getChildAt(p.y);
        ImageView v = (ImageView)row.getChildAt(p.x);
        v.setImageResource(linkedGameFieldLogic.getValueAtPosition(p));
    }

    private View.OnTouchListener touchListener = new View.OnTouchListener(){
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return gestureDetector.onTouchEvent(motionEvent);
        }
    };

    //WARNING, SMELLS - Stuur naar een GameField die beslist wat er in de view en wat er in de logic moet gebeuren
    public Point getFirstEncounteredPoint(int row, Direction direction){
        return linkedGameFieldLogic.getFirstEncounteredPoint(row, direction);
    }

    public void addGameFinishedListener(GameFinishedListener finishedListener) {
        linkedGameFieldLogic.addGameFinishedListener(finishedListener);
    }

    public void initializeFirstMove(Point point, int occupied) {
        linkedGameFieldLogic.initializeFirstMove(point, occupied);
    }

    public boolean doMove(Point coords, Direction direction, Player player){
        //Check of move mag
        //Doe animatie
        animationHandler.doAnimation(getAllViewsToPoint(coords, direction), direction, player);
        //Doe effectieve move
        return linkedGameFieldLogic.doMove(coords, player.playerColorResource);
    }

    //TODO: refactor
    private List<ImageView> getAllViewsToPoint(Point coords, Direction direction) {
        List<ImageView> viewList = new ArrayList<ImageView>();
        switch (direction) {
            case DOWNWARDS:
                int startAtY = 0;
                int endY = coords.y;
                while (startAtY <= endY){
                    TableRow currentRow = (TableRow)tableLayout.getChildAt(startAtY);
                    ImageView currentRowColumnView = (ImageView)currentRow.getChildAt(coords.x);
                    viewList.add(currentRowColumnView);
                    startAtY++;
                }
                break;
            case UPWARDS:
                startAtY = amountRows - 1;
                endY = coords.y;
                while (startAtY >= endY){
                    TableRow currentRow = (TableRow)tableLayout.getChildAt(startAtY);
                    ImageView currentRowColumnView = (ImageView)currentRow.getChildAt(coords.x);
                    viewList.add(currentRowColumnView);
                    startAtY--;
                }
                break;
            case LEFTTORIGHT:
                int startAtX = 0;
                int endX = coords.x;
                while (startAtX <= endX){
                    TableRow currentRow = (TableRow)tableLayout.getChildAt(coords.y);
                    ImageView currentRowColumnView = (ImageView)currentRow.getChildAt(startAtX);
                    viewList.add(currentRowColumnView);
                    startAtX++;
                }
                break;
            case RIGHTTOLEFT:
                startAtX = amountColumns - 1;
                endX = coords.x;
                while (startAtX >= endX){
                    TableRow currentRow = (TableRow)tableLayout.getChildAt(coords.y);
                    ImageView currentRowColumnView = (ImageView)currentRow.getChildAt(startAtX);
                    viewList.add(currentRowColumnView);
                    startAtX--;
                }
                break;
        }
        return viewList;
    }

    //TODO: Linken aan redraw van deze view, niet aan windowfocusedchanged?
    public void recalculateRelativeCellSize() {
        gestureListener.recalculateRelativeCellSize();
    }

    //WARNING: Rewrite -> View moet alleen viewstuff doen, controller doet alle andere zooi
    public void resetGameField() {
        //TODO: View cleanup

        //Logic cleanup
        linkedGameFieldLogic.resetGameField();
    }
}
