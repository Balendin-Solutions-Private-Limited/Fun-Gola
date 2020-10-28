package com.thambola.fungola.Animation;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;

import androidx.annotation.Nullable;

import com.thambola.fungola.R;

import java.util.ArrayList;
import java.util.Random;

/**
 * A loading view with wave animation
 *
 * @author ChenLittlePing (562818444@qq.com)
 * @version v1.0
 * @Datetime 2017-05-10 15:00
 */

public class WaveProgressBar extends View {

    private Context mContext;

    /**The wave path to draw*/
    private Path mWavePath;

    /**The round shape path*/
    private Path mCirclePath;

    /**The paint to draw the wave*/
    private Paint mWavePaint;

    /**The paint to stroke the round canvas*/
    private Paint mCircleStrokePaint;

    /**The paint to fill the round canvas*/
    private Paint mCircleFillPaint;

    /**The paint draw text*/
    private Paint mTextPaint;

    /**The wave length*/
    private int mOneWaveLength = 0;

    /**The wave height*/
    private int mOneWaveHeight = 0;

    /**The wave's scrolling distance in X direction*/
    private float mAnimDx = 0;

    /**The wave's scrolling distance in Y direction*/
    private int mAnimDY = 0;

    /**Wave animator scroller*/
    private Scroller mScroller;

    /**Progress animator*/
    private ValueAnimator mProgressAnimator;

    /**Duration to move one wave length*/
    private int mWaveDuration = 2500;

    /**Loading progress*/
    private float mProgress = 0;

    private Random mRandom = new Random();

    private int mWaveColor = Color.GREEN;
    private int mFillColor = Color.WHITE;
    private int mStrokeColor = Color.GREEN;
    private int mTextColor = Color.WHITE;
    private float mStrokeWidth = 35;
    private float mTextSize = 10;
    private boolean mIsTextAnim = false;
    private boolean mStopTextAnimateAtCenter = true;
    private boolean mIsTextOverMiddle = false;
    private String tickettext;


    private final int LEFT = 0;
    private final int TOP = 1;
    private final int RIGHT = 2;
    private final int BOTTOM = 4;
    private final int LEFTTOP = 8;
    private final int RIGHTTOP = 16;
    private final int RIGHTBOTTOM = 32;
    private final int LEFTBOTTOM = 64;

    private int shadowDirection = LEFTBOTTOM;
    private ArrayList<Shadow> shadowList;
    //private Rect textBound;
    private String shadowSize;
    private RadialGradient gradient;
    private Bitmap bitmap;
    private Paint circlepaint;

    public WaveProgressBar(Context context) {
        super(context);
        mContext = context;
        init(context);
    }

    public WaveProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        getAttrs(context, attrs);
        init(context);
    }

    public WaveProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        getAttrs(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mWavePath = new Path();
        mCirclePath = new Path();

        //init wave paint
        mWavePaint = new Paint();
        mWavePaint.setColor(mWaveColor);
        mWavePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mWavePaint.setAntiAlias(true);

        //init stroke paint
        mCircleStrokePaint = new Paint();
        mCircleStrokePaint.setColor(mStrokeColor);
        mCircleStrokePaint.setStyle(Paint.Style.STROKE);
        mCircleStrokePaint.setStrokeWidth(mStrokeWidth);
        mWavePaint.setAntiAlias(true);

        //init fill paint
        mCircleFillPaint = new Paint();
        mCircleFillPaint.setColor(mFillColor);
        mCircleFillPaint.setStyle(Paint.Style.FILL);
        mWavePaint.setAntiAlias(true);

        //init text paint
        mTextPaint = new Paint();
        mTextPaint.setColor(mTextColor);
       // mTextPaint.setTextSize(mTextSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setAntiAlias(true);



        circlepaint = new Paint();
        circlepaint.setColor(Color.GREEN);
        circlepaint.setStrokeWidth(2);

        circlepaint.setTextAlign(Paint.Align.CENTER);
        circlepaint.setStyle(Paint.Style.FILL_AND_STROKE);
        //canvas.drawCircle(0, 0, (float) (width1/(1.4)), paint);


        // Initialize a typeface object to draw text on canvas
        Typeface typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD_ITALIC);

        // Set the paint font
       // mTextPaint.setTypeface(typeface);

        // Initialize a new EmbossMaskFilter instance
        EmbossMaskFilter filter = new EmbossMaskFilter(
                new float[]{1,5,1}, // direction of the light source
                0.5f, // ambient light between 0 to 1
                10, // specular highlights
                7.5f // blur before applying lighting
        );

      //  mTextPaint.setMaskFilter(filter);


        //shadowSize=""+mTextSize;

        /*zwenkai:sv_direction="right_bottom"
        zwenkai:sv_shadowColor="#a13c32"
        zwenkai:sv_shadowSize="60dp"
        zwenkai:sv_text="S"
        zwenkai:sv_textColor="#FFE6D4"
        zwenkai:sv_textSize="60sp"*/

        //textBound = new Rect();
       // mTextPaint.getTextBounds("90", 0, 2, textBound);
       // calcShadow();

        Rect rectangle = new Rect();

                /*
                    public void getTextBounds (String text, int start, int end, Rect bounds)
                        Return in bounds (allocated by the caller) the smallest rectangle that
                        encloses all of the characters, with an implied origin at (0,0).
                    Parameters
                        text : String to measure and return its bounds
                        start : Index of the first char in the string to measure
                        end : 1 past the last char in the string measure
                        bounds : Returns the unioned bounds of all the text. Must be allocated by
                            the caller.
                */
        mTextPaint.getTextBounds(
                "90", // text
                0, // start
                2, // end
                rectangle // bounds
        );

        // Apply the MaskFilter to draw embossed text
      //  mTextPaint.setMaskFilter(filter);

       /* // Initialize a new RadialGradient object
         gradient = new RadialGradient(
                mRandom.nextInt(rectangle.right),
                mRandom.nextInt(rectangle.bottom),
                mRandom.nextInt(rectangle.right),
                new int[]{
                        getRandomHSVColor(),
                        getRandomHSVColor(),
                        getRandomHSVColor(),
                        getRandomHSVColor(),
                        getRandomHSVColor()
                },
                null, // Stops position is undefined
                Shader.TileMode.MIRROR // Shader tiling mode
        );*/

        // Apply the RadialGradient as paint shader
       // mTextPaint.setShader(gradient);

    }

    // Custom method to generate random HSV color
    protected int getRandomHSVColor(){
        // Generate a random hue value between 0 to 360
        int hue = mRandom.nextInt(361);

        // We make the color depth full
        float saturation = 1.0f;

        // We make a full bright color
        float value = 1.0f;

        // We avoid color transparency
        int alpha = 255;

        // Finally, generate the color
        int color = Color.HSVToColor(alpha,new float[]{hue,saturation,value});

        // Return the color
        return color;
    }

    private void getAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.WaveProgressBar);

        mWaveColor = ta.getColor(R.styleable.WaveProgressBar_waveColor, mWaveColor);
        mFillColor = ta.getColor(R.styleable.WaveProgressBar_fillColor, mFillColor);
        mStrokeColor = ta.getColor(R.styleable.WaveProgressBar_strokeColor, mStrokeColor);
        mTextColor = ta.getColor(R.styleable.WaveProgressBar_textColor, mTextColor);
        mStrokeWidth = ta.getDimension(R.styleable.WaveProgressBar_strokeWidth, mStrokeWidth);

        mTextSize = sp2px(context, mTextSize);
        mTextSize = ta.getDimension(R.styleable.WaveProgressBar_textSize, mTextSize);

        ta.recycle();
    }

    /**
     * Change sp to px
     */
    private static int sp2px(Context context, float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        clipCanvasIntoCircle(canvas);
        drawFill(canvas);
        drawWave(canvas);
        drawStroke(canvas);
        drawText(canvas);
        startAnim();
    }

    /**
     * Clip the canvas into round shape
     */
    private void clipCanvasIntoCircle(Canvas canvas) {
        mCirclePath.reset();
        mCirclePath.addCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2, Path.Direction.CW);
        canvas.clipPath(mCirclePath);
    }

    /**
     * Fill color to the circle
     */
    private void drawFill(Canvas canvas) {
        //Fill the circle
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2, mCircleFillPaint);
    }

    /**
     * Draw the wave
     */
    private void drawWave(Canvas canvas) {
        if (mOneWaveLength == 0) mOneWaveLength = getWidth() * 5 / 3; //default wave length
        if (mOneWaveHeight == 0) mOneWaveHeight = mOneWaveLength / 10; //default wave height

        if (mProgressAnimator == null) {
            mAnimDY = Math.round(mProgress/100 * (getHeight() + mOneWaveHeight));
        }

        mWavePath.reset();

        //刚开始时，起始点为X方向往左移动一个波长，Y方向在View的高往下移动半个波峰高度，
        // 即波浪的升降范围为（-mOneWaveHeight/2 ~ getHeight(()+mOneWaveHeight/2）
        mWavePath.moveTo(-mOneWaveLength + mAnimDx, (getHeight() + mOneWaveHeight/2) - mAnimDY);

        for (int waveLength = 0; waveLength < getWidth() + mOneWaveLength; waveLength += mOneWaveLength) {//画满整个View的宽度（前后各多出一个波长）
            mWavePath.rQuadTo(mOneWaveLength / 4, mOneWaveHeight, mOneWaveLength / 2, 0); //波浪前半个波长
            mWavePath.rQuadTo(mOneWaveLength / 4,  -mOneWaveHeight, mOneWaveLength / 2, 0); //波浪后半个波长
        }
        mWavePath.lineTo(getWidth(), getHeight());
        mWavePath.lineTo(0, getHeight());
        mWavePath.close();
        //Draw the wave
        canvas.drawPath(mWavePath, mWavePaint);
    }

    /**
     * Draw the circle stroke
     */
    private void drawStroke(Canvas canvas) {
        //Stroke the circle
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2, mCircleStrokePaint);
    }

    /**
     * Draw progress text
     */
    private void drawText(Canvas canvas) {
        Paint.FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();
        int baseline = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        if (mIsTextAnim && !mIsTextOverMiddle) {
            int y = (getHeight() + mOneWaveHeight / 2) - mAnimDY - getTextYPos();
            if (mStopTextAnimateAtCenter && (getHeight() + mOneWaveHeight/2) - mAnimDY < baseline) {
                if (Math.abs(y - baseline) < 3) { //stop at center
                    mIsTextOverMiddle = true;
                }
            }


            /*textBound = new Rect();

            mTextPaint.setTe(tickettext, 0, tickettext.length(), textBound);
            calcShadow();*/

            //calcShadow();

           /* mTextPaint.setColor(Color.RED);
            for (Shadow shadow : shadowList) {
                canvas.drawText(""+tickettext, getWidth() / 2  + shadow.getDx(), y  + shadow.getDy(), mTextPaint);
            }*/

            //mTextPaint.setColor(Color.GREEN);

           // mTextPaint.setShader(gradient);
         //   canvas.drawCircle(getWidth()/2,y,50,circlepaint);
          //  canvas.drawBitmap(bitmap,getWidth() / 2 - bitmap.getWidth()/2, (float) (y -( bitmap.getWidth()/1.5 )), null);

            mTextPaint.setColor(Color.WHITE);
         //   canvas.drawText(""+tickettext, getWidth() / 2, y , mTextPaint);



           // paint.setColor(textColor);
        //    canvas.drawText(text, getWidth() / 2 - textBound.width() / 2, getHeight() / 2 + textBound.height() / 2, paint);
            canvas.drawText(Math.round(mProgress) + "%", getWidth() / 2, y, mTextPaint);
         //   canvas.drawText(""+tickettext, getWidth() / 2, y, mTextPaint);
        } else {
           // canvas.drawText(Math.round(mProgress) + "%", getWidth()/2, baseline, mTextPaint);

           /* mTextPaint.setColor(Color.RED);
            for (Shadow shadow : shadowList) {
                canvas.drawText(""+tickettext, getWidth() / 2  + shadow.getDx(), baseline  + shadow.getDy(), mTextPaint);
            }*/
         //   canvas.drawBitmap(bitmap,getWidth() / 2 - bitmap.getWidth()/2, baseline - bitmap.getHeight()/2, null);

         //   canvas.drawCircle(getWidth()/2,baseline,50,circlepaint);

            mTextPaint.setColor(Color.WHITE);
           // canvas.drawText(""+tickettext, getWidth()/2, baseline, mTextPaint);

        }
    }


    private int getTextYPos() {
        int y;
        float t;
        if ((mAnimDx - (getWidth() / 2) % (mOneWaveLength)) > (mOneWaveLength / 2)) { //位置在半个波长以内，并且移动距离 超过 指定位置大于半个波长
            t = (mOneWaveLength + (getWidth() / 2)%mOneWaveLength - mAnimDx) * 1.0f / (mOneWaveLength / 2);
        } else if (((getWidth() / 2) % (mOneWaveLength) - mAnimDx) > (mOneWaveLength / 2)) { //指定位置大于移动距离在半个波长以上
            t = -((getWidth() / 2) % (mOneWaveLength) - mAnimDx - mOneWaveLength / 2) * 1.0f / (mOneWaveLength / 2);
        } else { //位置在半个波长以内，并且移动距离 不超过 指定的位置半个波长
            t = (((getWidth() / 2) % mOneWaveLength) - mAnimDx) * 1.0f / (mOneWaveLength / 2);
        }
        if (t > 0) {
            y = (int) (/*(Math.pow((1 - t), 2) * 0) + */(2 * t * (1 - t) * (-mOneWaveHeight))/* + Math.pow(t, 2) * 0*/); //前后两项Y坐标为0可以不计算
        } else {
            t = 1 + t;
            y = (int) (/*(Math.pow((1 - t), 2) * 0) + */(2 * t * (1 - t) * mOneWaveHeight)/* + Math.pow(t, 2) * 0*/);//前后两项Y坐标为0可以不计算
        }
        return y;
    }

    /**
     * Start wave animation
     */
    private void startAnim() {
        startWaveAnim();
     //   startProgressAnim();
    }

    /**
     * Calculate the wave moving speed according to current loading progress
     * <p><b>Higher progress, lower speed</b>
     * @return wave moving speed
     */
    private int calculateWaveSpeed() {
        int speed = Math.round(mProgress / 100 * mWaveDuration);
        if (speed > mWaveDuration) speed = mWaveDuration;
        else if (speed < 1000) speed = 1000;
        return speed;
    }

    /**
     * Start moving wave
     */
    private void startWaveAnim() {
        if (mScroller == null) {
            mScroller = new Scroller(mContext, new LinearInterpolator());
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    performWaveAnim();
                }
            }, 400);
        } else {
            performWaveAnim();
        }
    }

    /**
     * Perform the wave animation
     */
    private void performWaveAnim() {
        if (mScroller != null && mScroller.isFinished()) {
            mScroller.startScroll(0, 0, mOneWaveLength, 0, calculateWaveSpeed());
            postInvalidate();
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller != null && mScroller.computeScrollOffset() && !mScroller.isFinished()) {
            mAnimDx = mScroller.getCurrX();
            postInvalidate();
        }
        super.computeScroll();
    }

    /**
     * Automatically raise the wave, when the progress is 100%, start form 0% again.
     */
    public void startProgressAnim() {
        if (mProgressAnimator != null && mProgressAnimator.isRunning()) return;
        mProgressAnimator = ValueAnimator.ofInt(0, getHeight() + mOneWaveHeight);
        mProgressAnimator.setDuration(20000);
        mProgressAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mProgressAnimator.setInterpolator(new LinearInterpolator());
        mProgressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimDY = (int)animation.getAnimatedValue();
                mProgress = mAnimDY * 1.0f / (getHeight() + mOneWaveHeight) * 100;
            }
        });
        mProgressAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                mIsTextOverMiddle = false;
            }
        });
        mProgressAnimator.start();
    }

    /**
     * Set loading progress by percent.
     * @param percent loading progress
     */
    public void setProgress(int percent) {
        mProgress = percent;
    }

    /**
     * Set the wave length
     */
    public void setWaveLength(int length) {
        mOneWaveLength = length;
    }

    /**
     * Set the wave length
     */
    public void setWaveHeight(int height) {
        mOneWaveHeight = height;
    }

    /**
     * Animate the text by the wave
     * @param anim true: animate, false:draw in center
     * @param stopAtMiddle true: stop when text arrive at view center, false: never stop
     */
    public void AnimateText(boolean anim, boolean stopAtMiddle) {
        mIsTextAnim = anim;
        mStopTextAnimateAtCenter = stopAtMiddle;
    }

    public void setText(String tickettext) {

        this.tickettext=tickettext;
    }

    public void setBitmap(Bitmap bitmap) {

        this.bitmap=bitmap;
    }


    class Shadow {

        private float dx;
        private float dy;

        public Shadow(float dx, float dy) {
            super();
            this.dx = dx;
            this.dy = dy;
        }

        public float getDx() {
            return dx;
        }

        public float getDy() {
            return dy;
        }

    }

    private void calcShadow() {
        if(shadowList!=null)
        shadowList.clear();
        shadowList = new ArrayList<>();
        for (int i = 0, len = shadowSize.length(); i < len; i++) {
            switch (shadowDirection) {
                case LEFT:
                    shadowList.add(new Shadow(-i, 0));
                    break;
                case TOP:
                    shadowList.add(new Shadow(0, -i));
                    break;
                case RIGHT:
                    shadowList.add(new Shadow(i, 0));
                    break;
                case BOTTOM:
                    shadowList.add(new Shadow(0, i));
                    break;
                case LEFTTOP:
                    shadowList.add(new Shadow(-i, -i));
                    break;
                case RIGHTTOP:
                    shadowList.add(new Shadow(i, -i));
                    break;
                case LEFTBOTTOM:
                    shadowList.add(new Shadow(-i, i));
                    break;
                case RIGHTBOTTOM:
                    shadowList.add(new Shadow(i, i));
                    break;
            }
        }
    }
}
