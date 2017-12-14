package tech.digitus.fun.snake;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by walid on 6/19/16.
 */
public class SnakeElement extends View {

    public enum TYPE {
        HEAD("Head",1),
        BODY("Body",2),
        TAIL("Tail",3);

        private String name;
        private int value;
        private TYPE(String name, int value){
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

    }

    private TYPE type;

    private int color;

    private float size;

    private SnakeDirection direction=new SnakeDirection();

    public SnakeElement(Context context, int color, float size) {
        super(context);
        this.color=color;
        this.size=size;
        init();
    }

    public SnakeElement(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SnakeElement(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SnakeElement(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint=new Paint();
        //paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
        canvas.drawCircle(canvas.getWidth()/2,canvas.getHeight()/2,size,paint);
    }

    /*
    @Override
    public void draw(Canvas canvas) {
        Paint paint=new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
        canvas.drawCircle(0,0,size,paint);
        super.draw(canvas);
    }
    */

    private void init() {
        type=TYPE.HEAD;
        setBackgroundColor(Color.GREEN);
        direction.setX(0);
        direction.setY(0);
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public SnakeDirection getDirection() {
        return direction;
    }

    public void setDirection(SnakeDirection direction) {
        this.direction = direction;
    }
}

