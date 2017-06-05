//player class
package com.plebs.finalproject;

import java.util.*;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Player{
	
	private int num; //player number (1 to 4 inclusive possible)
	private int starStash; //how many items they've collected
	private int atSpot; //at what spot on the board
	private Sprite player; 
	private String name;
	
	public Player(int n, Texture img){
		num = n;
		starStash = 0;
		atSpot = 0;
		player = new Sprite(img);
		name = "Player " + num;
	}
	public int getNum(){ //returns player number
		return num;
	}
	public int getSpot(){ //returns spot on board player is at
		return atSpot;
	}
	public Sprite getSprite(){ //returns texture of player
		return player;
	}
	public void changeStash(int val){ //adds to or decreases number of items
		starStash += val; 
	}
	public void setPosition(int x, int y){ //sets position of sprite within class
		player.setPosition(x,y);
	}
	
	
}
