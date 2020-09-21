package com.example.mobileui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class DrawingView extends View {

    Bitmap bitmap;

    //brush size
    private static final float STROKE_WIDTH = 20f;
    private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;

    //paint items
    private Paint paint = new Paint();
    private Path path = new Path();

    //last touches
    private float lastTouchX;
    private float lastTouchY;
    private final RectF dirtyRect = new RectF();


    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint.setAntiAlias(true);
        //brush color
        paint.setColor(Color.BLACK);
        //brush style
        paint.setStyle(Paint.Style.STROKE);
        //brush strokejoin
        paint.setStrokeJoin(Paint.Join.ROUND);
        //brush size
        paint.setStrokeWidth(STROKE_WIDTH);
    }

    //clear canvas
    public void clear() {
        path.reset();
        invalidate();
    }

    //saves the image
    public void save(View v) {
        bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(c);

    }

    //set the canvas
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(path, paint);
    }

    //recognise the touch of the user
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(eventX, eventY);
                lastTouchX = eventX;
                lastTouchY = eventY;
                return true;

            case MotionEvent.ACTION_MOVE:

            case MotionEvent.ACTION_UP:

                resetDirtyRect(eventX, eventY);
                int historySize = event.getHistorySize();
                for (int i = 0; i < historySize; i++) {
                    float historicalX = event.getHistoricalX(i);
                    float historicalY = event.getHistoricalY(i);
                    expandDirtyRect(historicalX, historicalY);
                    path.lineTo(historicalX, historicalY);
                }
                path.lineTo(eventX, eventY);
                break;

            default:
                debug("Ignored touch event: " + event.toString());
                return false;
        }

        invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
                (int) (dirtyRect.top - HALF_STROKE_WIDTH),
                (int) (dirtyRect.right + HALF_STROKE_WIDTH),
                (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

        lastTouchX = eventX;
        lastTouchY = eventY;

        return true;
    }

    private void debug(String string) {

        Log.v("log_tag", string);

    }

    private void expandDirtyRect(float historicalX, float historicalY) {
        if (historicalX < dirtyRect.left) {
            dirtyRect.left = historicalX;
        } else if (historicalX > dirtyRect.right) {
            dirtyRect.right = historicalX;
        }

        if (historicalY < dirtyRect.top) {
            dirtyRect.top = historicalY;
        } else if (historicalY > dirtyRect.bottom) {
            dirtyRect.bottom = historicalY;
        }
    }

    private void resetDirtyRect(float eventX, float eventY) {
        dirtyRect.left = Math.min(lastTouchX, eventX);
        dirtyRect.right = Math.max(lastTouchX, eventX);
        dirtyRect.top = Math.min(lastTouchY, eventY);
    }
}