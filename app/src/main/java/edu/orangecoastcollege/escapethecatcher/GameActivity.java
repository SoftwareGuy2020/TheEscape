package edu.orangecoastcollege.escapethecatcher;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Controller for The Escape app
 */
public class GameActivity extends Activity implements GestureDetector.OnGestureListener{

    private GestureDetector aGesture;

    //FLING THRESHOLD VELOCITY
    final int FLING_THRESHOLD = 500;

    //BOARD INFORMATION
    final int SQUARE = 150;
    final int OFFSET = 5;
    final int COLUMNS = 7;
    final int ROWS = 8;
    final int gameBoard[][] = {
            {1, 1, 1, 1, 1, 1, 1},
            {1, 2, 2, 1, 2, 1, 1},
            {1, 2, 2, 2, 2, 2, 1},
            {1, 2, 1, 2, 2, 2, 1},
            {1, 2, 2, 2, 2, 1, 1},
            {1, 2, 2, 2, 2, 2, 3},
            {1, 2, 1, 2, 2, 2, 1},
            {1, 1, 1, 1, 1, 1, 1}
    };

    private Player player;
    private Zombie zombie;

    //LAYOUT AND INTERACTIVE INFORMATION
    private ArrayList<ImageView> visualObjects;
    private RelativeLayout activityGameRelativeLayout;
    private ImageView zombieImageView;
    private ImageView playerImageView;
    private ImageView obstacleImageView;
    private ImageView exitImageView;
    private int exitRow;
    private int exitCol;

    //  WINS AND LOSSES
    private int wins;
    private int losses;
    private TextView winsTextView;
    private TextView lossesTextView;

    private LayoutInflater layoutInflater;
    private Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        activityGameRelativeLayout = (RelativeLayout) findViewById(R.id.activity_game);
        winsTextView = (TextView) findViewById(R.id.winsTextView);
        lossesTextView = (TextView) findViewById(R.id.lossesTextView);

        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        resources = getResources();

        visualObjects = new ArrayList<ImageView>();

        wins = 0;
        losses = 0;
        winsTextView.setText(resources.getString(R.string.win) + wins);
        lossesTextView.setText(resources.getString(R.string.losses) + losses);
        aGesture = new GestureDetector(this, this);

        startNewGame();
    }

    private void startNewGame() {
        //TASK 1:  CLEAR THE BOARD (ALL IMAGE VIEWS)
        for (int i = 0; i < visualObjects.size(); i++) {
            ImageView visualObj = visualObjects.get(i);
            activityGameRelativeLayout.removeView(visualObj);
        }
        visualObjects.clear();

        //TASK 2:  BUILD THE  BOARD
        buildGameBoard();

        //TASK 3:  ADD THE CHARACTERS
        createZombie();
        createPlayer();
    }

    private void buildGameBoard() {
        // TODO: Inflate the entire game board (obstacles and exit)
        for (int i = 0; i < ROWS; ++i) {

            for (int j = 0; j < COLUMNS; ++j) {
                if (gameBoard[i][j] == 1) {
                    obstacleImageView = (ImageView) layoutInflater.inflate(R.layout.obstacle_layout,
                            null);
                    obstacleImageView.setX(j * SQUARE + OFFSET);
                    obstacleImageView.setY(i * SQUARE + OFFSET);
                    activityGameRelativeLayout.addView(obstacleImageView);
                }
                else if (gameBoard[i][j] == 3) {
                    exitImageView = (ImageView) layoutInflater.inflate(R.layout.exit_layout, null);
                    exitImageView.setX(j * SQUARE + OFFSET);
                    exitImageView.setY(i * SQUARE + OFFSET);
                    activityGameRelativeLayout.addView(exitImageView);
                    exitRow = i;
                    exitCol = j;
                }
            }
        }
    }

    private void createZombie() {
        // TODO: Determine where to place the Zombie (at game start)
        // TODO: Then, inflate the zombie layout
        int startRow = 5, startCol = 5;
        zombie = new Zombie(startRow, startCol);

        zombieImageView = (ImageView) layoutInflater.inflate(R.layout.zombie_layout, null);
        zombieImageView.setX(startCol * SQUARE + OFFSET);
        zombieImageView.setY(startRow * SQUARE + OFFSET);
        activityGameRelativeLayout.addView(zombieImageView);

        visualObjects.add(zombieImageView);
    }

    private void createPlayer() {
        // TODO: Determine where to place the Player (at game start)
        // TODO: Then, inflate the player layout
        int startRow = 1, startCol = 1;
        player = new Player(startRow, startCol);

        playerImageView = (ImageView) layoutInflater.inflate(R.layout.player_layout, null);
        playerImageView.setX(startCol * SQUARE + OFFSET);
        playerImageView.setY(startRow * SQUARE + OFFSET);
        activityGameRelativeLayout.addView(playerImageView);

        visualObjects.add(playerImageView);
    }



    private void movePlayer(float velocityX, float velocityY) {
        // TODO: This method gets called in the onFling event
        String direction;
        if (Math.abs(velocityX) > Math.abs(velocityY)) {
            direction = (velocityX > FLING_THRESHOLD) ? "RIGHT" : (velocityX < -FLING_THRESHOLD)
                    ? "LEFT" : "";
        }
        else {
            direction = (velocityY > FLING_THRESHOLD) ? "UP" : (velocityY < -FLING_THRESHOLD)
                    ? "DOWN" : "";
        }

        if (!direction.isEmpty()) {
            player.move(gameBoard, direction);
            playerImageView.setX(player.getCol() * SQUARE + OFFSET);
            playerImageView.setY(player.getRow() * SQUARE + OFFSET);
        }
        zombie.move(gameBoard, player.getCol(), player.getRow());
        zombieImageView.setX(zombie.getCol() * SQUARE + OFFSET);
        zombieImageView.setY(zombie.getRow() * SQUARE + OFFSET);

        if (zombie.getCol() == player.getCol() && zombie.getRow() == player.getRow()) {
            lossesTextView.setText(resources.getString(R.string.losses) + (++losses));
        }
        else if (player.getCol() == exitCol && player.getRow() == exitRow) {
            winsTextView.setText(resources.getString(R.string.win) + (++wins));
        }
    }

    /**
     * Called when a touch screen event was not handled by any of the views under it.
     * @param event the event
     * @return Return true if you have consumed the event, false if you haven't. The default implementation always returns false.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        return aGesture.onTouchEvent(event);
    }

    /**
     *  initialize the contents of the Activity's standard options menu
     * @param menu the menu
     * @return boolean Return false to allow normal menu processing to proceed, true to consume it here.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    /**
     *  This hook is called whenever an item in your options menu is selected.
     * @param item the menu item that was selected
     * @return boolean Return false to allow normal menu processing to proceed, true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Notified when a tap occurs with the down MotionEvent that triggered it.
     * @param motionEvent the down motion event
     * @return boolean Return false to allow normal processing to proceed, true to consume it here.
     */
    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    /**
     * The user has performed a down MotionEvent and not performed a move or up yet.
     * @param motionEvent the down motion event
     */
    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    /**
     * Notified when a tap occurs with the up MotionEvent that triggered it.
     * @param motionEvent the up motion event that completed the first tap
     * @return true if the event is consumed, else false
     */
    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    /**
     * Notified when a scroll occurs with the initial on down MotionEvent and the current move
     * MotionEvent. The distance in x and y is also supplied for convenience.
     * @param motionEvent The first down motion event that started the scrolling.
     * @param motionEvent1 The move motion event that triggered the current onScroll.
     * @param v The distance along the X axis that has been scrolled since the last call to onScroll.
     *          This is NOT the distance between e1 and e2.
     * @param v1 The distance along the Y axis that has been scrolled since the last call to onScroll.
     *           This is NOT the distance between e1 and e2.
     * @return true if the event is consumed, else false
     */
    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    /**
     * Notified when a long press occurs with the initial on down MotionEvent that triggered it.
     * @param motionEvent The initial on down motion event that started the longpress.
     */
    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    /**
     * Notified of a fling event when it occurs with the initial on down MotionEvent and the matching
     * up MotionEvent. The calculated velocity is supplied along the x and y axis in pixels per second.
     * @param motionEvent The first down motion event that started the fling.
     * @param motionEvent1 The move motion event that triggered the current onFling.
     * @param v The velocity of this fling measured in pixels per second along the x axis.
     * @param v1 The velocity of this fling measured in pixels per second along the y axis.
     * @return
     */
    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        movePlayer(v, v1);
        return false;
    }
}
