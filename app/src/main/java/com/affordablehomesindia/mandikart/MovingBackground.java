package com.affordablehomesindia.mandikart;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MovingBackground extends SurfaceView implements SurfaceHolder.Callback {

    private Bitmap backGround;

    public MovingBackground(Context context) {
        super(context);
        init(context, null);
    }

    public MovingBackground(Context context, AttributeSet attrs){
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        backGround = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.vegetables);
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        doDrawRunning(canvas);
        invalidate();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    private int mBGFarMoveX = 0;

    private void doDrawRunning(Canvas canvas) {

        mBGFarMoveX = mBGFarMoveX - 1;

        int newFarX = backGround.getWidth() - (-mBGFarMoveX);

        if (newFarX <= 0) {
            mBGFarMoveX = 0;
            canvas.drawBitmap(backGround, mBGFarMoveX, 0, null);
        } else {
            canvas.drawBitmap(backGround, mBGFarMoveX, 0, null);
            canvas.drawBitmap(backGround, newFarX, 0, null);
        }
    }
}

