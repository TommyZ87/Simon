package com.example.thomas.simon;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Thomas on 10/14/2015.
 */
public class SimonGame
{
    ArrayList<Integer> moves = new ArrayList<>();
    Random random = new Random();
    Button[] buttons = new Button[4];
    int currentMove = 0;
    boolean humanTurn = false;
    Handler handler;
    SimonGame(Button[] buttons, Context context)
    {
        this.buttons=buttons;
        handler = new Handler(context.getMainLooper());
//        addButtonListeners();
        setButtonsOnTouch();
    }

    public void round()
    {
        Log.v("ROUND", "ROUND STARTED");
        currentMove=0;
        generateMove();
        playBack();
    }

    public void generateMove()
    {
        moves.add(random.nextInt(4));
    }

    public void playBack()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                humanTurn=false;
                setButtonsEnabled(humanTurn);
                for(final Integer move: moves)
                {
                    Log.v("ROUND", move + "");
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            highlight(move);
                        }
                    });
                    int highlightTime = 1000;
                    pause(highlightTime);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            unHighlight(move);
                        }
                    });
                    pause(highlightTime/5);
                    humanTurn=true;
                    setButtonsEnabled(humanTurn);
                }
            }
        }).start();
    }

    private void pause(long millis)
    {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    final private static int r1 = Color.parseColor("#FF640000");//Red
    final private static int r2 = Color.parseColor("#FFFF0000");//Red
    final private static int g1 = Color.parseColor("#FF006400");//Green
    final private static int g2 = Color.parseColor("#FF00FF00");//Green
    final private static int b1 = Color.parseColor("#FF000064");//Blue
    final private static int b2 = Color.parseColor("#FF0000FF");//Blue
    final private static int y1 = Color.parseColor("#FF646400");//Yellow
    final private static int y2 = Color.parseColor("#FFFFFF00");//Yellow
    final private static int[] unhighlightColors = {g1,r1,y1,b1};
    final private static int[] highlightColors = {g2,r2,y2,b2};

    public void highlight(int index)
    {
        Log.v("HOUND","HIGHLIGHT" + index);
        buttons[index].setBackgroundColor(highlightColors[index]);
    }

    public void unHighlight(int index)
    {
        Log.v("HOUND","UNHIGHLIGHT" + index);
        buttons[index].setBackgroundColor(unhighlightColors[index]);
    }

    public void setButtonsEnabled(boolean enabled)
    {
        enabled = true;
        for(Button button: buttons)
        {
            button.setEnabled(enabled);
        }
    }

    public void addButtonListeners()
    {
        for(int i = 0; i<buttons.length;i++)
        {
            final int q = i;

            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {

                }
            });
        }
    }

    public void setButtonsOnTouch()
    {
        for(int i = 0; i<4; i++)
        {
            final int q = i;
            buttons[i].setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Log.v("TOUCH", "TOUCH" + q);
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            // PRESSED
                            highlight(q);
//                            buttons[q].setBackgroundColor(highlightColors[q]);
                            return true; // if you want to handle the touch event
                        case MotionEvent.ACTION_UP:
                            // RELEASED
                            unHighlight(q);
                            click(q);
//                            buttons[q].setBackgroundColor(unhighlightColors[q]);
                            return true; // if you want to handle the touch event
                    }
                    return true;
                }
            });
        }
    }

    public void click(int q)
    {
        Log.v("TOUCH","CLICK" + q);
        if(humanTurn)
        {
            if(moves.get(currentMove)==q)
            {
                currentMove++;
                if(currentMove>=moves.size())
                {
                    round();
                }
            }
            else
            {
                Log.v("GAMESTATE", "YOU LOSE");
            }
        }
    }

        boolean highlight = false;
    private void testClick(int q)
    {
        if(highlight)
            unHighlight(q);
        else
            highlight(q);
        highlight = !highlight;
    }
}