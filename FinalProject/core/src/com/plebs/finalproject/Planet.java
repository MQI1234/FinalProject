package com.plebs.finalproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;

public class Planet {
	private String name;
	private Texture texture;
	private int x,y;
	private String type;
	private LinkedList<Player> landed; //all the players that are on this planet in order
	private long interval; //time taken when a player lands
	private boolean running; 
	private int page;
	private Object minigame;
	
	public Planet(String info) throws IOException{
		String[] string = info.split(" "); // x , y, file name for texture
		x = Integer.parseInt(string[0]);
		y = Integer.parseInt(string[1]);
		makeName();
		landed = new LinkedList<Player>();
		type = string[3];
		texture = new Texture("planets/"+string[2]);
		page = 0;
		running = false;
		
	}
	
	public boolean getRunning(){
		return running;
	}
	
	public void takeOff(Player p){ //player leaves this planet
		landed.remove(p);
	}
	
	public void land(Player p){ //a player lands on this planet
		landed.add(p);
		running = true;
	}
	
	public void render(){ //activity on the planet
		if(page == 0){ //still on pre-activity instructions/intro
			//TO DO: up close planet texture, name, alien guide, instructions of game
			if(Gdx.input.isKeyJustPressed(Keys.ENTER)){ //player is ready
				page = 1;
				//if statements for all the types that player can be
				interval = System.currentTimeMillis();
				if(type.equals("inthedark")){
					minigame = new InTheDark(landed.getFirst());
				}
				else if(type.equals("sequence")){
					minigame = new Sequence(landed.getFirst(),landed.getFirst(), System.currentTimeMillis());
				}
			}
		}
		else{ //action loop
			running = ((int)((System.currentTimeMillis()-interval)/1000) > 60) ? false : true; //time each game will take
			//minigame.render();
			//System.out.print("NOOB");
		}
	}
	
	public void reset(){
		running = false;
		page = 0;
	}
	public void makeName() throws IOException{
		FileHandle file = Gdx.files.internal("planets/names.txt");
		BufferedReader reader = new BufferedReader(file.reader());
		ArrayList<String> lines = new ArrayList<String>();
		String line = reader.readLine();
		while( line != null ) {
			lines.add(line);
			line = reader.readLine();
		}
		Random r = new Random();
		name = lines.get(r.nextInt(lines.size()));
		System.out.println(name);
	}
	
	public Rectangle getRekt(){ //returns bounding rectangle of texture on board
		return new Rectangle(x,y,texture.getWidth(), texture.getHeight());
	}
	
	public Texture getText(){ 
		return texture;
	}
	
	public void drawPlanet(Batch batch){
		batch.draw(texture, x, y);
	}
	
}