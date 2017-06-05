package com.plebs.finalproject;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

import java.util.*;
import java.io.*;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;

public class Remember{
	//sprites
	private Texture redx;
	private Texture redcheck;
	private Texture background;
	private Texture UP;
	private Texture DOWN;
	private Texture LEFT;
	private Texture RIGHT;
	
	private Player player1;
	private Player player2;
	private int score1;//how many levels player 1 been through
	private int score2;//how many levels player 2 been through
	private int key1; //the key player and 2 must hit
	private int key2;
	private String[]order1;
	private String[]order2;
	private Random rand = new Random();
	private int bpress1;
	private int bpress2;
	
	private String winner;
	
	private long startTime;
	private int seconds;
	private int showseconds;
	private boolean gamerun;
	
	//font things
	BitmapFont font;
	FreeTypeFontGenerator generator;
	FreeTypeFontParameter parameter;
	
	public Remember(Player p1, Player p2){
		player1 = p1;
		player2 = p2;
		key1 = 0;
		key2 = 0;
		score1 = 0;
		score2 = 0;
		changeOrder(player1);
		changeOrder(player2);
		seconds = 0;
		showseconds = 0;
		gamerun = true;
		
		redx = new Texture("x.png");
		redcheck = new Texture("check.jpg");
		background = new Texture("rememberbackground.png");
		UP = new Texture("UP.png");
		DOWN = new Texture("DOWN.png");
		LEFT = new Texture("LEFT.png");
		RIGHT = new Texture("RIGHT.png");
		
		bpress1 = 0;
		bpress2 = 0;
		
		font = new BitmapFont();
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("space.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 72;
		parameter.borderColor = Color.BLACK;
		font = generator.generateFont(parameter);
		generator.dispose();
	}
	public int checkButton(String direct, Player p){
		String pKey = "";
		if (Gdx.input.isKeyJustPressed(Keys.DOWN) == true && p.equals(player2)|| Gdx.input.isKeyJustPressed(Keys.S) == true && p.equals(player1)){
			pKey = "DOWN";
		}
		else if (Gdx.input.isKeyJustPressed(Keys.UP) == true && p.equals(player2)|| Gdx.input.isKeyJustPressed(Keys.W) == true && p.equals(player1)){
			pKey = "UP";
		}
		else if (Gdx.input.isKeyJustPressed(Keys.LEFT) == true && p.equals(player2)|| Gdx.input.isKeyJustPressed(Keys.A) == true && p.equals(player1)){
			pKey = "LEFT";
		}
		else if (Gdx.input.isKeyJustPressed(Keys.RIGHT) == true && p.equals(player2)|| Gdx.input.isKeyJustPressed(Keys.D) == true && p.equals(player1)){
			pKey = "RIGHT";
		}
		if (direct.equals(pKey)){
			return 1;
		}
		else if(pKey.equals("")){
			return 0;
		}
		return 2;
	}
	public void changeOrder(Player p){
		String[]directs = {"UP","DOWN","LEFT","RIGHT"};
		String[]order = new String[5];
		for (int i = 0;i<5;i++){
			order[i] = directs[rand.nextInt(4)];
		}
		if (p.equals(player1)){
			order1 = order;
		}
		else{
			order2 = order;
		}
		System.out.println(Arrays.toString(order));
	}
	public void timer(){
		if ((int)((System.currentTimeMillis() - startTime)/1000) > seconds){
			seconds += 1;
		}
	}
	public void setStartTime(long time){
		startTime = time;
	}
	public Texture findDirectTexture(String direct){
		Texture arrow = null;
		if (direct.equals("DOWN")){
			arrow = DOWN;
		}
		else if (direct.equals("UP")){
			arrow = UP;
		}
		else if (direct.equals("LEFT")){
			arrow = LEFT;
		}
		else if (direct.equals("RIGHT")){
			arrow = RIGHT;
		}
		return arrow;
	}
	public void drawDirectTexture(Player p, Batch batch){
		Texture arrow = null;
		String direct = null;
		int x = 100;
		int y = 400;
		if (p.equals(player1)){
			direct = order1[key1];
		}
		else if (p.equals(player2)){
			direct = order2[key2];
			x += 750;
		}
		arrow = findDirectTexture(direct);
		batch.draw(arrow, x, y);
	}
	public void drawPlayerOrder(Player p, Batch batch){
		String[]order = new String[5];
		int key = 0;
		int x = 100;
		int y = 700;
		if (p.equals(player1)){
			order = order1;
			key = key1;
		}
		else if(p.equals(player2)){
			order = order2;
			key = key2;
			x+=750;
		}
		for (int i = 0; i<5; i++){
			Texture arrow = findDirectTexture(order[i]);
			batch.draw(arrow, x + (64*i), y,64,64);
			if (i<key){
				batch.draw(redx,x + (64*i),y,64,64);
			}
		}
	}
	public void level(Player p , int key, int score, String[]order, int bpress){
		String direct = order[key];
		if (seconds - showseconds >=1){
			bpress = 0;
		}
		if (checkButton(direct , p) == 1){
			bpress = 1;
			showseconds = seconds;
			key +=1;
			if (key<5){
				direct = order[key];
			}
			else{
				score+=1;
				key = 0;
				changeOrder(p);
			}
		}
		else if (checkButton(direct, p) == 2){
			bpress = 2;
			showseconds = seconds;
		}
		if (p.equals(player1)){
			score1 = score;
			key1 = key;
			bpress1 = bpress;

		}
		else{
			score2 = score;
			key2 = key;
			bpress2 = bpress;
		}
		
	}
	public void update(){
		if (gamerun == true){
			level(player1, key1, score1, order1, bpress1);
			level(player2, key2, score2, order2, bpress2);
		}
		timer();
		if (seconds < 60){
			gamerun = true;
		}
		else{
			gamerun = false;
		}
	}
	public void render(Batch batch){
		update();
		batch.draw(background,0,0);
		if (gamerun == true){
			drawPlayerOrder(player1,batch);
			drawPlayerOrder(player2,batch);
			drawDirectTexture(player1,batch);
			drawDirectTexture(player2,batch);
			if (bpress1 == 1){
				batch.draw(redcheck,100,0);
			}
			else if(bpress1 == 2){
				batch.draw(redx,0,0);
			}
			if (bpress2 == 1){
				batch.draw(redcheck,900,0);
			}
			else if(bpress2 == 2){
				batch.draw(redx,800,0);
			}
			font.draw(batch, ""+score1, 600, 600);
		}
	}
		
}
