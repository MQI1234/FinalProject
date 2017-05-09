//a minigame, where a single player tries to collect as many items as possible in a dark map where
//only the immediate area of the player is visible, and an enemy constantly chases them
//items are auto-generated whenever a player collects one (same amount on screen at all times)

package com.qi.finalproject;

import java.awt.geom.Point2D;
import java.util.*;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
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

public class InTheDark{
	Random rand = new Random();
	
	//Sprites and Textures
	private Player user;
	private Sprite userSprite;
	private SpriteBatch batch;
	private Texture shadow = new Texture("shadow.png"); 
	private Texture map = new Texture("dungeon.jpg");
	private Sprite enemy = new Sprite(new Texture("oni.png")); 
	private Texture item = new Texture("item.png");
	
	//holds information
	private ArrayList<Rectangle> itemList = new ArrayList<Rectangle>();
	int itemsCollected = 0;
	private long startTime; //time when class is first created
	
	//counter
	private int seconds;
	
	public InTheDark(Player p1){
		batch = new SpriteBatch();
		user = p1;
		userSprite = p1.getSprite();
		user.setPosition(10,10);
		for(int i = 0; i<15; i++){ //loops to create item locations on map
			int x = rand.nextInt(1200);
			int y = rand.nextInt(600);
			itemList.add(new Rectangle(x, y, item.getWidth(), item.getHeight()));
		}
		seconds = 0;
	}
	
	public boolean running(){ //if player still has time
		if(seconds > 10){
			return false;
		}
		return true;
	}
	public void movePlayer(){ //needs keyboard input 
		//player will not be able to move off screen
		if(Gdx.input.isKeyPressed(Keys.LEFT) && userSprite.getX()>0){
			user.setPosition(userSprite.getX() - 4, userSprite.getY());
		} 
		else if(Gdx.input.isKeyPressed(Keys.RIGHT) && userSprite.getX()<(Gdx.graphics.getWidth() - userSprite.getWidth())){
			user.setPosition(userSprite.getX() + 4, userSprite.getY());
		}
		if(Gdx.input.isKeyPressed(Keys.DOWN) && userSprite.getY()>4){
			user.setPosition(userSprite.getX(), userSprite.getY() - 4);
		} 
		else if(Gdx.input.isKeyPressed(Keys.UP) && userSprite.getY()<(Gdx.graphics.getHeight() - userSprite.getHeight())){
			user.setPosition(userSprite.getX(), userSprite.getY() + 4);
		}
	}
	
	public void update(){
		timer();
		movePlayer();
		collect();
		newItems();
		moveEnemy();
		userSprite = user.getSprite();
	}
	
	public void render(){
		showTime();
		batch.begin();
		batch.draw(map, 0, 0);
		for(Rectangle r : itemList){
			batch.draw(item, r.x, r.y);
		}
		userSprite.draw(batch);
		enemy.draw(batch);
		//batch.draw(shadow, userSprite.getX() + userSprite.getWidth()/2 - 1650, userSprite.getY() + userSprite.getHeight()/2 - 1000);
		//positions picture so that player texture drawn to center of transparent circle
		batch.end();
	}
	
	public void moveEnemy(){ //moves according to location of player
		enemy.setPosition((float)(enemy.getX() + (userSprite.getX() - enemy.getX())*0.005), (float)(enemy.getY() + (userSprite.getY() - enemy.getY())*0.005));
	}
	
	public void collect(){ //checks if player is touching items
		for(int i = 0; i<itemList.size(); i++){
			if(userSprite.getBoundingRectangle().overlaps(itemList.get(i))){ //player sprite collides with item
				itemsCollected += 1;
				itemList.remove(i); //removes item from list, will not check or render it anymore 
			}
		}
	}
	public void timer(){ //times game
		if((int)((System.currentTimeMillis() - startTime)/1000) > seconds){
			seconds += 1;
		}
	}
	public void newItems(){ //generates new item every 5 seconds
		if((int)((System.currentTimeMillis() - startTime)/5000) > (int)(seconds/5)){
			itemList.add(new Rectangle(rand.nextInt(1200), rand.nextInt(600),item.getWidth(),item.getHeight())); //generates new item
		}
	}
	public void setStartTime(long time){
		startTime = time;
	}
	
	public void showTime(){
		//System.out.println((int)((System.currentTimeMillis() - startTime)/1000));
	}
}