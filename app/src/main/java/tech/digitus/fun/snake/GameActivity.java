package tech.digitus.fun.snake;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

//ToDo type(head,body,tail), Position, Color, Size, Direction
//ToDo SankeElement
/*Todo
 SnakeBody
 SnakeElementMovement
 SnakeBodyMovement
 DetectEvent (null, kill, grow)
 SnakeGrow
 SnakeDriver (thread) (speed,etc.)
 */

public class GameActivity extends AppCompatActivity {

    private SnakeElement Snake;
    private RelativeLayout.LayoutParams snakeParams;
    private float x, x1,x2, y, y1, y2;
    private Thread snakeMover;
    //private ArrayList<View> fullSnake;

    private enum Direction{
        STOP("Stop",0),
        RIGHT("Right",1),
        LEFT("Left",2),
        UP("Up",3),
        DOWN("Down",4);

        private String name;
        private int value;
        private Direction(String name, int value){
            this.name=name;
            this.value=value;
        }

        public int getValue() {
            return value;
        }

        @Override
        public String toString() {
            return name;
        }
    };
    private static final float STEP=3;
    private Direction snakeDirection=Direction.STOP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Snake=new SnakeElement(this,Color.BLUE,50);
        RelativeLayout game_support=(RelativeLayout)findViewById(R.id.game_support);
        game_support.addView(Snake);
        //fullSnake.add(Snake);
        initGame();
    }

    private void initGame() {
        snakeParams=new RelativeLayout.LayoutParams(100, 100);
        Snake.setLayoutParams(snakeParams);
        Snake.setBackgroundColor(Color.GREEN);
        snakeParams.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
        snakeMover=new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    snakeMove(snakeDirection);
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        snakeMover.start();
    }

    private void snakeMove(Direction direction) {
        switch (direction) {
            case RIGHT:
                Snake.setX(Snake.getX()+STEP);
                break;
            case LEFT:
                Snake.setX(Snake.getX()-STEP);
                break;
            case UP:
                Snake.setY(Snake.getY()+STEP);
                break;
            case DOWN:
                Snake.setY(Snake.getY()-STEP);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getActionMasked()) {

            case MotionEvent.ACTION_DOWN:
                x1=event.getX();
                y1=event.getY();
                break;

            case MotionEvent.ACTION_UP:
                x2=event.getX();
                y2=event.getY();
                x=x2-x1;
                y=y2-y1;
                if(Math.abs(x)>Math.abs(y)){
                    //this is a horizontal movement
                    if(x>0)
                        snakeDirection=Direction.RIGHT;
                    else
                        snakeDirection=Direction.LEFT;
                } else {
                    //this is a vertical movement
                    if(y>0)
                        snakeDirection=Direction.UP;
                    else
                        snakeDirection=Direction.DOWN;
                }
                //snakeMove(snakeDirection);
                //if(snakeDirection != null)
                    //Toast.makeText(GameActivity.this, snakeDirection.toString(), Toast.LENGTH_SHORT).show();

                break;

            /*
            case MotionEvent.ACTION_MOVE:
                x2=event.getX();
                y2=event.getY();
                x=x2-x1;
                y=y2-y1;
                if(Math.abs(x)>Math.abs(y)){
                    //this is a horizontal movement
                    if(x>0)
                        snakeDirection=Direction.RIGHT;
                    else
                        snakeDirection=Direction.LEFT;
                } else {
                    //this is a vertical movement
                    if(y>0)
                        snakeDirection=Direction.DOWN;
                    else
                        snakeDirection=Direction.UP;
                }
                break;
            */
        }

        return false;
    }
}
