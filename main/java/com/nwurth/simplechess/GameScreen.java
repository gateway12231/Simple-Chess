package com.nwurth.simplechess;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ActionBar;
import android.content.ClipData;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nwurth.simplechess.views.BoardView;

public class GameScreen extends AppCompatActivity {
    private int increment, startX, startY;
    private double frac;
    private GamePiece BP1,BP2,BP3,BP4,BP5,BP6,BP7,BP8;
    private GamePiece WP1,WP2,WP3,WP4,WP5,WP6,WP7,WP8;
    private GamePiece BR1,BN1,BB1,BQ,BK,BB2,BN2,BR2;
    private GamePiece WR1,WN1,WB1,WQ,WK,WB2,WN2,WR2;
    private Board woody;
    private Player white, black, currentPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);
        frac = 0.5;
        white = new Player("white",getIntent().getExtras().getString("white"));
        black = new Player("black",getIntent().getExtras().getString("black"));
        currentPlayer = white;
        woody = new Board(white,black,getApplicationContext());

        makeBoardVars();
        View temp = (View) findViewById(R.id.Board);
        temp.setOnDragListener(new MyDragListener());
        ImageView BoardSetUp = (ImageView) findViewById(R.id.BlackPawn1);
        woody.getPiece(0,6).setImageView(BoardSetUp);
        fixPieces(BoardSetUp,0,6);
        BoardSetUp = (ImageView) findViewById(R.id.BlackPawn2);
        woody.getPiece(1,6).setImageView(BoardSetUp);
        fixPieces(BoardSetUp,1,6);
        BoardSetUp = (ImageView) findViewById(R.id.BlackPawn3);
        woody.getPiece(2,6).setImageView(BoardSetUp);
        fixPieces(BoardSetUp,2,6);
        BoardSetUp = (ImageView) findViewById(R.id.BlackPawn4);
        woody.getPiece(3,6).setImageView(BoardSetUp);
        fixPieces(BoardSetUp,3,6);
        BoardSetUp = (ImageView) findViewById(R.id.BlackPawn5);
        woody.getPiece(4,6).setImageView(BoardSetUp);
        fixPieces(BoardSetUp,4,6);
        BoardSetUp = (ImageView) findViewById(R.id.BlackPawn6);
        woody.getPiece(5,6).setImageView(BoardSetUp);
        fixPieces(BoardSetUp,5,6);
        BoardSetUp = (ImageView) findViewById(R.id.BlackPawn7);
        woody.getPiece(6,6).setImageView(BoardSetUp);
        fixPieces(BoardSetUp,6,6);
        BoardSetUp = (ImageView) findViewById(R.id.BlackPawn8);
        woody.getPiece(7,6).setImageView(BoardSetUp);
        fixPieces(BoardSetUp,7,6);
        BoardSetUp = (ImageView) findViewById(R.id.BlackRook1);
        woody.getPiece(0,7).setImageView(BoardSetUp);
        fixPieces(BoardSetUp,0,7);
        BoardSetUp = (ImageView) findViewById(R.id.BlackKnight1);
        woody.getPiece(1,7).setImageView(BoardSetUp);
        fixPieces(BoardSetUp,1,7);
        BoardSetUp = (ImageView) findViewById(R.id.BlackBishop1);
        woody.getPiece(2,7).setImageView(BoardSetUp);
        fixPieces(BoardSetUp,2,7);
        BoardSetUp = (ImageView) findViewById(R.id.BlackQueen);
        woody.getPiece(3,7).setImageView(BoardSetUp);
        fixPieces(BoardSetUp,3,7);
        BoardSetUp = (ImageView) findViewById(R.id.BlackKing);
        woody.getPiece(4,7).setImageView(BoardSetUp);
        fixPieces(BoardSetUp,4,7);
        BoardSetUp = (ImageView) findViewById(R.id.BlackBishop2);
        woody.getPiece(5,7).setImageView(BoardSetUp);
        fixPieces(BoardSetUp,5,7);
        BoardSetUp = (ImageView) findViewById(R.id.BlackKnight2);
        woody.getPiece(6,7).setImageView(BoardSetUp);
        fixPieces(BoardSetUp,6,7);
        BoardSetUp = (ImageView) findViewById(R.id.BlackRook2);
        woody.getPiece(7,7).setImageView(BoardSetUp);
        fixPieces(BoardSetUp,7,7);
        BoardSetUp = (ImageView) findViewById(R.id.WhitePawn1);
        woody.getPiece(0,1).setImageView(BoardSetUp);
        fixPieces(BoardSetUp,0,1);
        BoardSetUp = (ImageView) findViewById(R.id.WhitePawn2);
        woody.getPiece(1,1).setImageView(BoardSetUp);
        fixPieces(BoardSetUp,1,1);
        BoardSetUp = (ImageView) findViewById(R.id.WhitePawn3);
        woody.getPiece(2,1).setImageView(BoardSetUp);
        fixPieces(BoardSetUp,2,1);
        BoardSetUp = (ImageView) findViewById(R.id.WhitePawn4);
        woody.getPiece(3,1).setImageView(BoardSetUp);
        fixPieces(BoardSetUp,3,1);
        BoardSetUp = (ImageView) findViewById(R.id.WhitePawn5);
        woody.getPiece(4,1).setImageView(BoardSetUp);
        fixPieces(BoardSetUp,4,1);
        BoardSetUp = (ImageView) findViewById(R.id.WhitePawn6);
        woody.getPiece(5,1).setImageView(BoardSetUp);
        fixPieces(BoardSetUp,5,1);
        BoardSetUp = (ImageView) findViewById(R.id.WhitePawn7);
        woody.getPiece(6,1).setImageView(BoardSetUp);
        fixPieces(BoardSetUp,6,1);
        BoardSetUp = (ImageView) findViewById(R.id.WhitePawn8);
        woody.getPiece(7,1).setImageView(BoardSetUp);
        fixPieces(BoardSetUp,7,1);
        BoardSetUp = (ImageView) findViewById(R.id.WhiteRook1);
        woody.getPiece(0,0).setImageView(BoardSetUp);
        fixPieces(BoardSetUp,0,0);
        BoardSetUp = (ImageView) findViewById(R.id.WhiteKnight1);
        woody.getPiece(1,0).setImageView(BoardSetUp);
        fixPieces(BoardSetUp,1,0);
        BoardSetUp = (ImageView) findViewById(R.id.WhiteBishop1);
        woody.getPiece(2,0).setImageView(BoardSetUp);
        fixPieces(BoardSetUp,2,0);
        BoardSetUp = (ImageView) findViewById(R.id.WhiteQueen);
        woody.getPiece(3,0).setImageView(BoardSetUp);
        fixPieces(BoardSetUp,3,0);
        BoardSetUp = (ImageView) findViewById(R.id.WhiteKing);
        woody.getPiece(4,0).setImageView(BoardSetUp);
        fixPieces(BoardSetUp,4,0);
        BoardSetUp = (ImageView) findViewById(R.id.WhiteBishop2);
        woody.getPiece(5,0).setImageView(BoardSetUp);
        fixPieces(BoardSetUp,5,0);
        BoardSetUp = (ImageView) findViewById(R.id.WhiteKnight2);
        woody.getPiece(6,0).setImageView(BoardSetUp);
        fixPieces(BoardSetUp,6,0);
        BoardSetUp = (ImageView) findViewById(R.id.WhiteRook2);
        woody.getPiece(7,0).setImageView(BoardSetUp);
        fixPieces(BoardSetUp,7,0);
    }

    private final class MyTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent){
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                ClipData data = ClipData.newPlainText("","");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDragAndDrop(data,shadowBuilder,view,0);
                return true;
            }
            else{
                return false;
            }
        }
    }

    class MyDragListener implements View.OnDragListener{
        float letter1,number1,letter2,number2;
        @Override
        public boolean onDrag(View v, DragEvent event){

            //ImageView piece;
            switch (event.getAction()){
                case DragEvent.ACTION_DRAG_STARTED:
                    letter1 = event.getX();
                    number1 = event.getY();
                    //piece = (ImageView) event.getLocalState();
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DROP:
                    letter2 = event.getX();
                    number2 = event.getY();
                    //piece = (ImageView) event.getLocalState();
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    break;
            }
            return true;
        }
    }

    private int makeMove(ImageView piece, int letter, int number){
        if (letter < 0 || letter > 7 || number < 0 || number > 7){
            makeToast("@strings/offBoardWarning");
            return 0;
        }
        else{

        }
        setLocation(piece,letter,number);
        return 0;
    }

    private void makeBoardVars (){
        Display disp = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);
        int width = size.x;
        Resources resources = getApplicationContext().getResources();
        //int nID = resources.getIdentifier("navigation_bar_height","dimen","android");
        int sID = resources.getIdentifier("status_bar_height","dimen","android");
        int statusH = getResources().getDimensionPixelSize(sID);
        int height = size.y-statusH;
        increment = width/10;
        startY = (height/2)-(4*increment);
        startX = increment+1;
    }

    private void fixPieces(ImageView piece, int letter, int number){
        piece.setOnTouchListener(new MyTouchListener());
        setLocation(piece,letter,number);
    }

    private void setLocation(ImageView piece, int letter, int number){
        FrameLayout root = (FrameLayout)findViewById(R.id.root);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int) java.lang.Math.floor(increment*frac), (int) java.lang.Math.floor(increment*frac));
        params.leftMargin = leftCalc(letter);
        params.topMargin  = topCalc(number);
        root.removeView(piece);
        root.addView(piece, params);
    }

    private void setLocation(View board, int letter, int number){
        FrameLayout root = (FrameLayout)findViewById(R.id.root);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(increment*8, (int) increment*8);
        params.leftMargin = leftCalc(letter);
        params.topMargin  = topCalc(number);
        root.removeView(board);
        root.addView(board, params);
    }

    private int leftCalc(int n){
        return (int) java.lang.Math.floor(startX+(frac/2+n)*increment);
    }

    private int topCalc(int n) {
        return (int) java.lang.Math.floor(startY+((frac/2)+7-n)*increment);
    }

    private int xCoordinate2letter(float n){
        return (int) java.lang.Math.floor((n-startX)/increment);
    }

    private int yCoordinate2number(float n){
        return (int) java.lang.Math.floor((startY+8*increment-n)/increment);
    }

    private void makeToast (String s) {
        Toast t = Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG);
        t.show();
    }
}
