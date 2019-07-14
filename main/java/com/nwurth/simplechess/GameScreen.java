package com.nwurth.simplechess;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class GameScreen extends AppCompatActivity {
    private int increment, startX, startY;
    private double frac;
    private Board woody;
    private Player white, black, currentPlayer;
    private Piece [] set;
    private int count,whiteSide,blackSide;
    private Button close;
    private PopupWindow popupWindow;
    private FrameLayout frame;
    private TextView message;
    private String end;

    private class Piece{
        private int letter, number;
        private ImageView sprite;
        private boolean isCap;

        private Piece(ImageView sprite, int letter, int number){
            this.sprite = sprite;
            this.letter = letter;
            this.number = number;
            this.isCap = false;
        }

        private void reassign(int letter, int number) {
            this.letter = letter;
            this.number = number;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);
        frac = 0.5;
        white = new Player("white",getIntent().getExtras().getString("white"));
        black = new Player("black",getIntent().getExtras().getString("black"));
        currentPlayer = white;
        set = new Piece [32];
        count = 0;
        woody = new Board(white,black,getApplicationContext());
        frame = (FrameLayout) findViewById(R.id.root);



        makeBoardVars();
        View temp = (View) findViewById(R.id.Board);
        temp.setOnDragListener(new MyDragListener());
        ImageView BoardSetUp = findViewById(R.id.BlackQueen);
        fixPieces(BoardSetUp,3,7);
        BoardSetUp = findViewById(R.id.BlackRook1);
        fixPieces(BoardSetUp,0,7);
        BoardSetUp = findViewById(R.id.BlackRook2);
        fixPieces(BoardSetUp,7,7);
        BoardSetUp = findViewById(R.id.BlackKnight1);
        fixPieces(BoardSetUp,1,7);
        BoardSetUp = findViewById(R.id.BlackKnight2);
        fixPieces(BoardSetUp,6,7);
        BoardSetUp = findViewById(R.id.BlackBishop1);
        fixPieces(BoardSetUp,2,7);
        BoardSetUp = findViewById(R.id.BlackBishop2);
        fixPieces(BoardSetUp,5,7);
        BoardSetUp = findViewById(R.id.BlackKing);
        fixPieces(BoardSetUp,4,7);
        BoardSetUp = findViewById(R.id.BlackPawn1);
        fixPieces(BoardSetUp,0,6);
        BoardSetUp = findViewById(R.id.BlackPawn2);
        fixPieces(BoardSetUp,1,6);
        BoardSetUp = findViewById(R.id.BlackPawn3);
        fixPieces(BoardSetUp,2,6);
        BoardSetUp = findViewById(R.id.BlackPawn4);
        fixPieces(BoardSetUp,3,6);
        BoardSetUp = findViewById(R.id.BlackPawn5);
        fixPieces(BoardSetUp,4,6);
        BoardSetUp = findViewById(R.id.BlackPawn6);
        fixPieces(BoardSetUp,5,6);
        BoardSetUp = findViewById(R.id.BlackPawn7);
        fixPieces(BoardSetUp,6,6);
        BoardSetUp = findViewById(R.id.BlackPawn8);
        fixPieces(BoardSetUp,7,6);
        BoardSetUp = findViewById(R.id.WhiteQueen);
        fixPieces(BoardSetUp,3,0);
        BoardSetUp = findViewById(R.id.WhiteRook1);
        fixPieces(BoardSetUp,0,0);
        BoardSetUp = findViewById(R.id.WhiteRook2);
        fixPieces(BoardSetUp,7,0);
        BoardSetUp = findViewById(R.id.WhiteKnight1);
        fixPieces(BoardSetUp,1,0);
        BoardSetUp = findViewById(R.id.WhiteKnight2);
        fixPieces(BoardSetUp,6,0);
        BoardSetUp = findViewById(R.id.WhiteBishop1);
        fixPieces(BoardSetUp,2,0);
        BoardSetUp = findViewById(R.id.WhiteBishop2);
        fixPieces(BoardSetUp,5,0);
        BoardSetUp = findViewById(R.id.WhiteKing);
        fixPieces(BoardSetUp,4,0);
        BoardSetUp = findViewById(R.id.WhitePawn1);
        fixPieces(BoardSetUp,0,1);
        BoardSetUp = findViewById(R.id.WhitePawn2);
        fixPieces(BoardSetUp,1,1);
        BoardSetUp = findViewById(R.id.WhitePawn3);
        fixPieces(BoardSetUp,2,1);
        BoardSetUp = findViewById(R.id.WhitePawn4);
        fixPieces(BoardSetUp,3,1);
        BoardSetUp = findViewById(R.id.WhitePawn5);
        fixPieces(BoardSetUp,4,1);
        BoardSetUp = findViewById(R.id.WhitePawn6);
        fixPieces(BoardSetUp,5,1);
        BoardSetUp = findViewById(R.id.WhitePawn7);
        fixPieces(BoardSetUp,6,1);
        BoardSetUp = findViewById(R.id.WhitePawn8);
        fixPieces(BoardSetUp,7,1);
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
        int letter1,number1,letter2,number2;
        @Override
        public boolean onDrag(View v, DragEvent event){

            ImageView piece;
            switch (event.getAction()){
                case DragEvent.ACTION_DRAG_STARTED:
                    letter1 = xCoordinate2letter(event.getX());
                    number1 = yCoordinate2number(event.getY());
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DROP:
                    letter2 = xCoordinate2letter(event.getX());
                    number2 = yCoordinate2number(event.getY());
                    piece = (ImageView) event.getLocalState();
                    moveFilter(piece,letter2,number2);
                    if (woody.movePhase(currentPlayer,letter1,number1,letter2,number2)){
                        collisionCheck(letter2,number2);
                        set[findPiece(letter1,number1)].reassign(letter2,number2);
                        endCheck(currentPlayer);
                        if(currentPlayer.equals(white)){
                            //makeToast(black.getName()+"'s turn");
                            currentPlayer = black;
                        }
                        else{
                            //makeToast(white.getName()+"'s turn");
                            currentPlayer = white;
                        }
                    }
                    else{
                        makeToast(getResources().getString(R.string.generalWarning));
                    }
                    reassemble();
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    break;
            }
            return true;
        }
    }

    private void endCheck(Player current){
        switch(woody.checkGameState(current)){
            case 1:
                end = (white.getName()+" wins!");
                endSequence();
                break;
            case 2:
                end = (black.getName()+" wins!");
                endSequence();
                break;
            case 3:
                end = ("Stalemate.");
                endSequence();
                break;
            case 0:
                break;
        }
    }

    public void endSequence(){
        LayoutInflater layoutInflater = (LayoutInflater) GameScreen.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.popup,null);

        close = (Button) customView.findViewById(R.id.closePopupBtn);

        //instantiate popup window
        popupWindow = new PopupWindow(customView, FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        ((TextView) popupWindow.getContentView().findViewById(R.id.endMessage)).setText(end);
        //display the popup window
        popupWindow.showAtLocation(frame, Gravity.CENTER, 0, 0);
        //message.setText(end);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void moveFilter(ImageView piece, int letter, int number){
        if (letter < 0 || letter > 7 || number < 0 || number > 7){
            makeToast(getResources().getString(R.string.offBoardWarning));
            }
    }

    private void collisionCheck(int letter, int number) {
        for (int i = 0; i < 32; i =i+1){
            if(set[i].letter == letter && set[i].number == number){
                set[i].isCap = true;
                set[i].reassign(-1,-1);
            }
        }
    }

    private int findPiece(int letter, int number) {
        for (int i = 0; i < 32; i =i+1){
            if(set[i].letter == letter && set[i].number == number){
                return i;
            }
        }
        return -1;
    }

    private void reassemble(){
        for (int i = 0; i<32; i = i+1){
            if (set[i].isCap){
                setSideLine(set[i].sprite, i<16);
            }
            else{
                setLocation(set[i].sprite,set[i].letter,set[i].number);
            }
        }
        whiteSide = 0;
        blackSide = 0;
    }

    private void setSideLine(ImageView piece, boolean isBlack){
        int top, left;
        if (isBlack){
            top = startY - increment/2;
            left = startX + blackSide*increment/2;
            blackSide = blackSide+1;
        }
        else{
            top = startY + increment/2 + 8*increment;
            left = startX + whiteSide*increment/2;
            whiteSide = whiteSide+1;
        }
        FrameLayout root = (FrameLayout) findViewById(R.id.root);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int) java.lang.Math.floor(increment * frac/2), (int) java.lang.Math.floor(increment * frac/2));
        params.leftMargin = left;
        params.topMargin = top;
        root.removeView(piece);
        root.addView(piece, params);
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
        set[count] = new Piece(piece,letter,number);
        count = count + 1;
        setLocation(piece,letter,number);
    }

    private void setLocation(ImageView piece, int letter, int number){
        FrameLayout root = (FrameLayout) findViewById(R.id.root);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int) java.lang.Math.floor(increment * frac), (int) java.lang.Math.floor(increment * frac));
        params.leftMargin = leftCalc(letter);
        params.topMargin = topCalc(number);
        root.removeView(piece);
        root.addView(piece, params);
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
