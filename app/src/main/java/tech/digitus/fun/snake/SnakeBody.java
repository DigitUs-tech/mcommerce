package tech.digitus.fun.snake;

import android.content.Context;
import android.graphics.Color;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * Created by walid on 6/19/16.
 */
public class SnakeBody {
    private ArrayList<SnakeElement> snakeBody=new ArrayList<>();

    private int length;

    private SnakeDirection direction;

    private float speed;

    private int color= Color.GREEN;

    private float defaultElementSize=50;

    private Context context;

    public SnakeBody(Context context){
        this.context=context;

    }

    private void init(){
        SnakeElement element=new SnakeElement(context, color, defaultElementSize);
        snakeBody.add(element);

    }

}
