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
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Player{
	
	private int starStash; //how many items they've collected
	private int atSpot; //at what spot on the board
	private Sprite player; //their sprite, universal for all minigames
	private Sprite ship; //spaceship sprite, used to represent travel on the board
	//private TextureRegion[][]allFrames; //array of all possible frames of player's sprite
	private TextureRegion allFrames;
	private int frame = 1; //frame number when drawing from region (column)
	private int direct = 0; //row of different directions
	
	public Player(TextureRegion img){
		//allFrames = TextureRegion.split(img.getTexture(), 48, 58);
		allFrames = img;
		starStash = 0;
		atSpot = 0;
		player = new Sprite(new TextureRegion(img, 232, 48, 48, 58)); //as a placeholder for position, not actually drawn
		player.setPosition(700, 30);
		ship = new Sprite(new Texture("ship.png"));
		ship.setPosition(200, 100);
		ship.setOriginCenter();
	}
	public int getSpot(){ //returns spot on board player is at
		return atSpot;
	}
	public int getStars(){ //returns number of stars player has accumulated
		return starStash;
	}
	public Sprite getSprite(){ //returns sprite of player
		return player;
	}
	public Sprite getSSprite(){ //returns sprite of ship
		return ship;
	}
	public void changeStash(int val){ //adds to or decreases number of items
		starStash = Math.max(0, starStash + val); //least number of stars they have is 0
	}
	public void setSpot(int steps){
		atSpot += steps;
		atSpot = atSpot%7; //never goes off the board
	}
	public void setPosition(float x, float y){ //sets position of player sprite within class
		player.setPosition(x,y);
	}
	public void setSPosition(float x, float y){ //sets position of ship sprite
		ship.setPosition(x,y);
	}
	public void setDirect(int d){ //direction of movement determines which row to draw frames from
		direct = d;
	}
	public void addFrame(){ //in movement, changes frame 
		frame = (frame + 1)%3;
	}
	public void render(SpriteBatch batch){
		//batch.draw(allFrames[direct][frame], player.getX(), player.getY());
		batch.draw(new TextureRegion(allFrames, frame*48, direct*58, 48, 58), player.getX(), player.getY());
		
	}
	
	
	
	
}