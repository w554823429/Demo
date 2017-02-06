package com.example.demo.bar;

import com.example.demo.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

public class MyProgressBar extends ProgressBar {
	/**
     * Ĭ�ϵ�����
     */
    private static final int DEFAULT_VALUES = 10;//Ĭ�ϵ�ֵ
    private static final int DEFAULT_INDICATOR_HEIGHT = DEFAULT_VALUES * 2;//�����εĵױ߿�
    private static final int DEFAULT_INDICATOR_COLOR = 0xffFF0000;//ָʾ��Ĭ�ϵ���ɫ(��)
    private static final int DEFAULT_BACK_LINE_HEIGHT = DEFAULT_VALUES;//������Ĭ�ϸ߶ȣ��޽��ȣ�
    private static final int DEFAULT_BACK_LINE_COLOR = 0xffFF8080;//������Ĭ����ɫ(��)
    private static final int DEFAULT_FORE_LINE_HEIGHT = DEFAULT_VALUES;//������Ĭ�ϸ߶ȣ��н��ȣ�
    private static final int DEFAULT_FORE_LINE_COLOR = 0xff95CAFF;//������Ĭ����ɫ(��)
    /**
     * ����
     */
    private int indicator_height = dp2px(DEFAULT_INDICATOR_HEIGHT);
    private int indicator_color = DEFAULT_INDICATOR_COLOR;
    private int back_height = dp2px(DEFAULT_BACK_LINE_HEIGHT);
    private int back_color = DEFAULT_BACK_LINE_COLOR;
    private int fore_height = dp2px(DEFAULT_FORE_LINE_HEIGHT);
    private int fore_color = DEFAULT_FORE_LINE_COLOR;
    private int progress_width;//�����ߵĿ��
    private int triangle_width = indicator_height;//�ױ߿��һ��
    /**
     * ����
     */
    private Paint indicator_paint;//����ָʾ���Ļ���
    private Paint back_paint;//���ƽ������ĵײ�
    private Paint fore_paint;//���ƽ������Ľ���
    private int line_endX;//���������յ�����
    /**
     * ���췽��
     * @param context
     */
    public MyProgressBar(Context context) {
        this(context,null);
    }

    public MyProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //��ȡ�Զ�������
        obtainAttributes(attrs);
        //��ʼ������
        initPaint();
    }
    
    /**
     * ���ƻ���
     */
    private void initPaint() {
        //����ָʾ���Ļ���
        indicator_paint = new Paint();
        indicator_paint.setAntiAlias(true);
        indicator_paint.setStyle(Paint.Style.FILL);//���
        indicator_paint.setColor(indicator_color);//������ɫ
        //���ƽ������ĵײ�
        back_paint = new Paint();
        back_paint.setStyle(Paint.Style.FILL);//���
        back_paint.setColor(back_color);//������ɫ
        back_paint.setStrokeWidth(back_height);
        //���ƽ������Ľ���
        fore_paint = new Paint();
        fore_paint.setStyle(Paint.Style.FILL);//���
        fore_paint.setColor(fore_color);//������ɫ
        fore_paint.setStrokeWidth(fore_height);
    }

    /**
     * ��ȡ�Զ�������
     */
    private void obtainAttributes(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(
                attrs, R.styleable.MyProgressBar);
        //ָʾ��
        indicator_height = (int) typedArray.getDimension(
                R.styleable.MyProgressBar_indicator_height,indicator_height);
        indicator_color = typedArray.getColor(
                R.styleable.MyProgressBar_indicator_color,DEFAULT_INDICATOR_COLOR);
        //�ײ�������
        back_height = (int) typedArray.getDimension(
                R.styleable.MyProgressBar_back_line_height,back_height);
        back_color = typedArray.getColor(
                R.styleable.MyProgressBar_back_line_color,DEFAULT_BACK_LINE_COLOR);
        //��������
        fore_height = (int) typedArray.getDimension(
                R.styleable.MyProgressBar_fore_line_height,fore_height);
        fore_color = typedArray.getColor(
                R.styleable.MyProgressBar_fore_line_color,DEFAULT_FORE_LINE_COLOR);
        //������Դ
        typedArray.recycle();
    }

    /**
     * ����
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        setMeasuredDimension(width, height);
        progress_width = getMeasuredWidth()
                - getPaddingRight() - getPaddingLeft();//�������Ŀ��
        line_endX = progress_width - triangle_width;//�������ߵ��յ�
    }
    private int measureHeight(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);//��ȡ�����߶�ģʽ
        int specSize = MeasureSpec.getSize(measureSpec);//��ȡ�����߶ȵ�ֵ
        if (specMode == MeasureSpec.EXACTLY) {//��ȷ�Ĳ���ģʽ
            result = specSize;
        } else {
            result = (getPaddingTop() + getPaddingBottom()
                    + Math.max(back_height, fore_height)) + indicator_height;
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }
    /**
     * ����
     * @param canvas
     */
    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.save();
        canvas.translate(getPaddingLeft(), getHeight()/2);
        canvas.drawLine(triangle_width,0,line_endX,0,back_paint);//�ײ�
        float progress_with = getProgress()*1.0f/getMax();
        float progressPosX = (int) ((line_endX
                - triangle_width) * progress_with);
        canvas.drawLine(triangle_width,0,progressPosX
                + triangle_width,0,fore_paint);//����
        canvas.translate(getPaddingLeft(), getHeight()/2+getPaddingTop() +Math.max(back_height, fore_height));
        //����������
        setTriangle(canvas , progressPosX);
//        canvas.restore();
    }

    /**
     * ����������
     * @param canvas
     */
    private void setTriangle(Canvas canvas,float progressPosX) {
        // ���Ƶȱ�������
        float w = line_endX/2;
        float h = (float) (line_endX * Math.sin(45));
        Path path = new Path();
        path.moveTo(progressPosX + line_endX, - back_height/2);//���
        path.lineTo(w + progressPosX+ line_endX,-h- back_height/2);
        path.lineTo(-w  + progressPosX+ line_endX,-h- back_height/2);
        path.close();
        canvas.drawPath(path, indicator_paint);
    }
    /**
     * dpתpx
     * @param dpValues
     * @return
     */
    private int dp2px(int dpValues) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dpValues,
                getResources().getDisplayMetrics());
    }
}
