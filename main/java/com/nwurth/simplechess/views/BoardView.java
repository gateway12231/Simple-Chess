package com.nwurth.simplechess.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.nwurth.simplechess.R;

public class BoardView extends View {
    public int increment, startX, startY;
    public BoardView (Context context){
        super(context);
        init(null);
    }

    public BoardView(Context context, AttributeSet attrs){
        super(context,attrs);
    }
    public BoardView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context,attrs, defStyleAttr);
    }
    public BoardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
        super(context,attrs, defStyleAttr,defStyleRes);
    }
    private void init(@Nullable AttributeSet set) {

    }

    @Override
    protected void onDraw(Canvas canvas){

        canvas.drawColor(getResources().getColor(R.color.back));

        Paint whiteSquare,blackSquare;
        whiteSquare = new Paint();
        blackSquare = new Paint();
        whiteSquare.setColor(getResources().getColor(R.color.grey1));
        blackSquare.setColor(getResources().getColor(R.color.grey2));
        increment = getWidth()/10;
        startY = getHeight()/2-4*increment;
        startX = increment+1;

        for(int i = 0; i < 8;i = i+1){
            for(int j =0; j<8;j=j+1){
                if ((i+j)%2 == 1){
                    canvas.drawRect(startX+i*increment, startY + (j * increment), startX+(i+1)*increment, startY+(j+1)*increment,blackSquare);
                }
                else{
                    canvas.drawRect(startX+i*increment, startY+j*increment, startX + ((1 + i) * increment), startY+(j+1)*increment,whiteSquare);
                }
            }
        }
    }
}
