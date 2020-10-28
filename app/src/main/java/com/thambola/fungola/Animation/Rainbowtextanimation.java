package com.thambola.fungola.Animation;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.TextView;

import com.thambola.fungola.R;

public class Rainbowtextanimation extends TextView {



    private Matrix mMatrix;
    private float mTranslate;
    private float colorSpeed;
    private float colorSpace;
    //private int[] colors = {0xFFFF2B22, 0xFFFF7F22, 0xFFEDFF22, 0xFF22FF22, 0xFF22F4FF, 0xFF2239FF, 0xFF5400F7};
    private int[] colors = {0xFFFFFFFF, 0xFFECBC6A,0xFFECBC6A,0xFFFFFFFF, 0xFFEECA8C,0xFFEECA8C, 0xFFFFFFFF,0xFFC99743};
    private LinearGradient mLinearGradient;
    private Paint mTextPaint;

    public Rainbowtextanimation(Context context) {
        this(context, null);
    }

    public Rainbowtextanimation(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Rainbowtextanimation(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    /*@Override
    public void setAnimationListener(Animation.AnimationListener listener) {
        throw new UnsupportedOperationException("Invalid operation for rainbow");
    }*/

    private void init(AttributeSet attrs, int defStyleAttr) {

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.RainbowTextView);
        colorSpace = typedArray.getDimension(R.styleable.RainbowTextView_colorSpace, 150);
        colorSpeed = typedArray.getDimension(R.styleable.RainbowTextView_colorSpeed, 15);
        typedArray.recycle();

        mMatrix = new Matrix();
        initPaint();
    }

    public float getColorSpace() {
        return colorSpace;
    }

    public void setColorSpace(float colorSpace) {
        this.colorSpace = colorSpace;
    }

    public float getColorSpeed() {
        return colorSpeed;
    }

    public void setColorSpeed(float colorSpeed) {
        this.colorSpeed = colorSpeed;
    }

    public void setColors(int... colors) {
        this.colors = colors;
        initPaint();
    }

    private void initPaint() {

        mTextPaint = new Paint();
    //    mTextPaint.setColor(mTextColor);
        // mTextPaint.setTextSize(mTextSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setAntiAlias(true);

        mLinearGradient = new LinearGradient(0, 0, colorSpace, 0, colors, null, Shader.TileMode.MIRROR);
        getPaint().setShader(mLinearGradient);
    }

   /* @Override
    public void setProgress(float progress) {
    }

    @Override
    public void animateText(CharSequence text) {
        setText(text);
    }*/

    @Override
    protected void onDraw(Canvas canvas) {
        if (mMatrix == null) {
            mMatrix = new Matrix();
        }
      //  mTranslate += colorSpeed;
        mTranslate += (int)canvas.getWidth()/20;
        mMatrix.setTranslate(mTranslate, 0);
        mLinearGradient.setLocalMatrix(mMatrix);
        super.onDraw(canvas);
        postInvalidateDelayed(100);
    }
}
