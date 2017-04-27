package com.myapp.project.tictactoe;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class GameActivity extends AppCompatActivity {

    private final int ROW_COL_SIZE = 3;
    private ImageButton tile1;
    private ImageButton tile2;
    private ImageButton tile3;
    private ImageButton tile4;
    private ImageButton tile5;
    private ImageButton tile6;
    private ImageButton tile7;
    private ImageButton tile8;
    private ImageButton tile9;
    public TextView textView;
    private Button saveButton;
    private boolean gameLoaded = false;
    private FileOutputStream outputStream;
    public String fileName;
    AlertDialog errorDialog;
    AlertDialog tieDialog;
    AlertDialog.Builder winningBuilder;
    public int[][] array = new int[ROW_COL_SIZE][ROW_COL_SIZE];
    private int curPlayer = 1;
    private int moveCounter = 0;
    private Intent intent;

    /**
     * Game activity with Tic-tac-toe grid
     *
     * @param savedInstanceState Instance of the activity to load.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
        }

        //Error display dialogue when tapping a taken grid spot
        AlertDialog.Builder errorBuilder = new AlertDialog.Builder(GameActivity.this);
        errorBuilder.setTitle("Error").setMessage("Pick something else!");
        errorBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                System.out.println("Test click");
            }
        });
        intent = getIntent();
        gameLoaded = intent.hasExtra("data");
        errorDialog =  errorBuilder.create();

        winningBuilder = new AlertDialog.Builder(GameActivity.this);

        //Save dialogue
        AlertDialog.Builder saveBuilder = new AlertDialog.Builder(GameActivity.this);
        View view = LayoutInflater.from(this).inflate(R.layout.save_dialog,null);
        final EditText input = (EditText) view.findViewById(R.id.edit_dialog);
        saveBuilder.setTitle("Enter a name for your game.").setView(view);
        saveBuilder.setPositiveButton(R.string.saveConfirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                fileName = input.getText().toString();
                try {
                        String s = "";
                        for(int in = 0; in < ROW_COL_SIZE; in++){
                            for(int j = 0; j < ROW_COL_SIZE; j++){
                                s += String.valueOf(array[in][j]);
                            }
                        }
                        s += String.valueOf(curPlayer);
                        outputStream =  openFileOutput(fileName, Context.MODE_PRIVATE);
                        outputStream.write(s.getBytes());
                        outputStream.close();

                } catch (Exception e) {
                        e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAndRemoveTask();
                }
                else{
                    finishAffinity();
                }
            }
        });
        saveBuilder.setNegativeButton(R.string.doNotSave, null);
        final AlertDialog saveDialog = saveBuilder.create();

        AlertDialog.Builder tieBuilder = new AlertDialog.Builder(GameActivity.this);
        tieBuilder.setTitle("GAME OVER");
        tieBuilder.setMessage("This is a tie game!").setCancelable(false)
                .setPositiveButton(R.string.ok,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id)
                    {
                        finish();
                    }
                });
        tieDialog = tieBuilder.create();

        tile1 = (ImageButton) findViewById(R.id.imageButton1);
        tile2 = (ImageButton) findViewById(R.id.imageButton2);
        tile3 = (ImageButton) findViewById(R.id.imageButton3);
        tile4 = (ImageButton) findViewById(R.id.imageButton4);
        tile5 = (ImageButton) findViewById(R.id.imageButton5);
        tile6 = (ImageButton) findViewById(R.id.imageButton6);
        tile7 = (ImageButton) findViewById(R.id.imageButton7);
        tile8 = (ImageButton) findViewById(R.id.imageButton8);
        tile9 = (ImageButton) findViewById(R.id.imageButton9);
        saveButton = (Button) findViewById(R.id.saveButton);
        textView = (TextView) findViewById(R.id.player);

        if(gameLoaded){
            loadGame(intent.getStringExtra("data"));
        }

        tile1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(array[0][0] != 0){
                    errorDialog.show();
                }
                else {
                    showSelectionDialog(0,0,tile1);
                }
            }
        });
        tile2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(array[0][1] != 0){
                    errorDialog.show();
                }
                else {
                    showSelectionDialog(0,1,tile2);
                }
            }
        });
        tile3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(array[0][2] != 0 ){
                    errorDialog.show();
                }
                else{
                    showSelectionDialog(0,2,tile3);
                }
            }
        });
        tile4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(array[1][0] != 0 ){
                    errorDialog.show();
                }
                else{
                    showSelectionDialog(1,0,tile4);
                }
            }
        });
        tile5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(array[1][1] != 0 ){
                    errorDialog.show();
                }
                else{
                    showSelectionDialog(1,1,tile5);
                }
            }
        });
        tile6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(array[1][2] != 0 ){
                    errorDialog.show();
                }
                else{
                    showSelectionDialog(1,2,tile6);
                }
            }
        });
        tile7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(array[2][0] != 0 ){
                    errorDialog.show();
                }
                else{
                    showSelectionDialog(2,0,tile7);
                }
            }
        });
        tile8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(array[2][1] != 0 ){
                    errorDialog.show();
                }
                else {
                    showSelectionDialog(2,1,tile8);
                }
            }
        });
        tile9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(array[2][2] != 0 ){
                    errorDialog.show();
                }
                else {
                    showSelectionDialog(2,2,tile9);
                }
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDialog.show();
            }
        });
    }


    private void gameLogic(int x, int y, ImageButton tile){
        if(curPlayer == 1){
            array[x][y] = 1;
            tile.setImageResource(R.drawable.o_symbol);
            textView.setText("Player 2:");
            curPlayer = 2;
        }
        else{
            array[x][y] = 2;
            tile.setImageResource(R.drawable.x_symbol);
            textView.setText("Player 1:");
            curPlayer = 1;
        }
        if(checkWinner()){
            winningBuilder.setMessage("Congrats Player " + curPlayer + "! You win!");
            winningBuilder.setPositiveButton(R.string.endGame, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            winningBuilder.setCancelable(false);
            AlertDialog winning = winningBuilder.create();
            for(int i = 0; i < ROW_COL_SIZE; i++){
                for(int j = 0; j < ROW_COL_SIZE; j++) {
                    Log.i("ARRAY: i - ", i + " j - " + j + " : value - " + array[i][j]);
                }
            }
            Vibrator v = (Vibrator) GameActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(700);
            winning.show();
        }
        Log.i("Counter", String.valueOf(moveCounter));
        if(moveCounter == 8){
            tieDialog.show();
        }
        moveCounter++;
    }

    /**
     * Checks for winning player
     * @return winner
     */
    private boolean checkWinner(){

        //check rows
        for(int i = 0; i < ROW_COL_SIZE; i++){
            if(valueCheck(array[i][0],array[i][1],array[i][2])){
                return true;
            }
        }

        //check cols
        for(int i = 0; i < ROW_COL_SIZE; i++){
            if(valueCheck(array[0][i],array[1][i],array[2][i])){
                return true;
            }
        }

        //check dialog
        if(valueCheck(array[0][0],array[1][1],array[2][2]))
            return true;

        //check reverse dialog
        if(valueCheck(array[0][2],array[1][1],array[2][0]))
            return true;

        return false;
    }

    private boolean valueCheck(int a, int b , int c){
        if((a == b) && (a == c) && a != 0){
            return true;
        }
        return false;
    }

    private void loadGame(String s){
        Log.i("STRING LOADED", s);
        moveCounter = 0;
        int count = 0;
        for(int i = 0; i < ROW_COL_SIZE; i ++){
            for(int j = 0; j < ROW_COL_SIZE;j++){
                array[i][j] = Integer.parseInt(String.valueOf(s.charAt(count)));
                Log.i("ARRAY CHECK", String.valueOf(array[i][j]));
                if(array[i][j] != 0){
                    moveCounter++;
                }
                loadTile(i,j);
                count++;
            }
        }
        curPlayer = Integer.parseInt(String.valueOf(s.charAt(s.length() - 1)));
        textView.setText("Player " + String.valueOf(curPlayer) + ":");
        Log.i("current player", String.valueOf(curPlayer));
    }

    private void loadTile(int x, int y){
        if(x == 0 && y == 0){
            if(array[x][y] == 1){
                tile1.setImageResource(R.drawable.o_symbol);
            }
            if(array[x][y] == 2){
                tile1.setImageResource(R.drawable.x_symbol);
            }
        }

        if(x == 0 && y == 1){
            if(array[x][y] == 1){
                tile2.setImageResource(R.drawable.o_symbol);
            }
            if(array[x][y] == 2){
                tile2.setImageResource(R.drawable.x_symbol);
            }
        }

        if(x == 0 && y == 2){
            if(array[x][y] == 1){
                tile3.setImageResource(R.drawable.o_symbol);
            }
            if(array[x][y] == 2){
                tile3.setImageResource(R.drawable.x_symbol);
            }
        }

        if(x == 1 && y == 0){
            if(array[x][y] == 1){
                tile4.setImageResource(R.drawable.o_symbol);
            }
            if(array[x][y] == 2){
                tile4.setImageResource(R.drawable.x_symbol);
            }
        }

        if(x == 1 && y == 1){
            if(array[x][y] == 1){
                tile5.setImageResource(R.drawable.o_symbol);
            }
            if(array[x][y] == 2){
                tile5.setImageResource(R.drawable.x_symbol);
            }
        }

        if(x == 1 && y == 2){
            if(array[x][y] == 1){
                tile6.setImageResource(R.drawable.o_symbol);
            }
            if(array[x][y] == 2){
                tile6.setImageResource(R.drawable.x_symbol);
            }
        }

        if(x == 2 && y == 0){
            if(array[x][y] == 1){
                tile7.setImageResource(R.drawable.o_symbol);
            }
            if(array[x][y] == 2){
                tile7.setImageResource(R.drawable.x_symbol);
            }
        }

        if(x == 2 && y == 1){
            if(array[x][y] == 1){
                tile8.setImageResource(R.drawable.o_symbol);
            }
            if(array[x][y] == 2){
                tile8.setImageResource(R.drawable.x_symbol);
            }
        }

        if(x == 2 && y == 2){
            if(array[x][y] == 1){
                tile9.setImageResource(R.drawable.o_symbol);
            }
            if(array[x][y] == 2){
                tile9.setImageResource(R.drawable.x_symbol);
            }
        }
    }

    public void showSelectionDialog(final int x, final int y, final ImageButton img)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GameActivity.this);
        alertDialogBuilder.setTitle("Confirmation");
        alertDialogBuilder.setMessage("Are you sure you want to pick this one?").setCancelable(false)
                .setPositiveButton(R.string.yes,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id)
                    {
                        gameLogic(x,y,img);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
}
