package com.plebs.finalproject;
//TO DO:, planetactivate !!!, PLAYER SELECTION, RESET, PAUSE, stats board working, pretty things up
//fix astronaut

//TO SEARCH: multi-line text, SPRITESHEET PLSSS

import java.util.*;
import java.io.*;
import java.math.*;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;

public class FinalProject extends ApplicationAdapter implements InputProcessor{

	//additional objects
	Random rand;
	
	//flags, counters, data structure
	Player[]players; //holds players
	Planet[]planets; //holds planets
	int page; //tracks what page user is on (menu, game, etc.)
	int turn; // counter for whose turn it is
	long interval; //holds the time when one function ends, used to mark difference for start of next function
	Player curPlayer; //active player
	boolean[]checkpoints; 
	int rollNum; //number of spots to advance
	
	boolean activeRoll; //currently rolling for spots
	boolean activeBoard; //currently transitioning on the board
	boolean activePlanet; //currently on the planet
	boolean activeStats; //player is currently viewing the stats
	
	
	//textures and sprite stuff
	SpriteBatch batch;
	Texture img;
	Texture menu;
	Texture credits;
	Texture instruct;
	Texture board;
	Texture[] diceFaces;
	Sprite astro; //interesting thing on menu
	
	//font stuff
	BitmapFont font120;
	BitmapFont font75;
	FreeTypeFontGenerator generator;
	FreeTypeFontParameter parameter;
	
	//extra stuff
	int astroVX;
	int astroVY;
	Rectangle screen;
	
	//minigame testing
	InTheDark minigame1;
	Sequence rem;
	
	//Shapes
	ShapeRenderer shaperenderer;
	
	
	@Override
	public void dispose(){ //disposes of certain objects to free up memory
		//generator.dispose();
	}
	public void centerText(BitmapFont font, String str, int y){ //centers text when drawing
		font.draw(batch, str, 0, y, 1500, 1, true);
	}
	public void centerImg(Texture img, int y){ //centers images when drawing
		int w = img.getWidth();
		batch.draw(img, 750 - w/2, y);
	}
	
	//menu and instructional pages, game 
	public void menu(){ //add screen saver type animations
		batch.draw(menu, 0, 0);
		astro.rotate((float)(5));
		astro.draw(batch);
		if(Gdx.input.isKeyPressed(Keys.ENTER)){ //gameplay started
			page = 1;
			activeRoll = true; //starts game by activating the roll mechanism
		}
		else if(Gdx.input.isKeyPressed(Keys.UP)){ //instructional page
			page = 2;
		}
		else if(Gdx.input.isKeyPressed(Keys.DOWN)){ //credits page
			page = 3;
		}
		astro.setPosition(astro.getX() + astroVX, astro.getY() + astroVY);
		if(!astro.getBoundingRectangle().overlaps(screen)){ //sprite is completely offscreen
			astroVX *= -1;
			astroVY *= -1;
		}
	}
	
	public void instruct(){
		batch.draw(instruct, 0, 0);
		if(Gdx.input.isKeyPressed(Keys.LEFT)){
			page = 0; //back to menu
		}
	}
	
	public void credits(){
		batch.draw(credits, 0, 0);
		if(Gdx.input.isKeyPressed(Keys.LEFT)){
			page = 0; //back to menu
		}
	}
	
	public void seeStats(){
		//shaperenderer.begin(ShapeType.Filled);
		shaperenderer.setColor(0.5f, 0.5f, 0.5f, 0.2f);
		shaperenderer.rect(0,0,1500,900);
		//shaperenderer.end();
		
		centerText(font120, "CURRENT STATS:", 900);
		int i = 1;
		font75.draw(batch, "PLAYER", 50, 700);
		font75.draw(batch, "ACTIVE", 550, 700);
		font75.draw(batch, "STARS", 1050, 700);
		for(Player p : players){
			font75.draw(batch, "" + p.getNum(), 50, 690-i*100);
			if(p.equals(curPlayer)){ //it is this player's active turn
				font75.draw(batch, "YEE", 550, 690-i*100);
			}
			else{
				font75.draw(batch, "NO", 550, 690-i*10);
			}
			font75.draw(batch, "" + p.getStars(), 1050, 690-i*120);
			i++;
		}
	}
	
	public void getRoll(){
		centerText(font120, "YOU ROLLED A:", 700);
		centerText(font120, "" + (rollNum), 400);
		centerImg(diceFaces[rollNum-1], 500);
		
	}
	
	public void game(){ //game loop functions each player has to do for their turn
		int num;
		
		font120.draw(batch, "PLAYER " + (turn%4 + 1) + " TURN", 0, 900);
		if(activeRoll == true){
			num = roll();
			if(num != -1){
				activeRoll = false; //not doing the roll anymore
				activeBoard = true;
				rollNum = num+1; //number of spots they advance on the board
				
				checkpoints = new boolean[rollNum]; //state of landing on each of the planets on the way to destination
				for(int i = 0; i<checkpoints.length;){
					checkpoints[i] = false; //haven't landed on any yet
					i++;
				}
				
				interval = System.currentTimeMillis(); //marks when player ends roll
			}		
		}
		
		else if(activeBoard == true){ //shows changing of spots on the board
			if((System.currentTimeMillis() - interval)/1000 < 3){ //first shows players what they rolled for 3 seconds
				getRoll();
			}
			else{
				if(planetMove(curPlayer.getSSprite()) == false){ //if they've arrived to destination
					activeBoard = false;
					activePlanet = true;
					curPlayer.setSpot(rollNum);
					//rem = new Sequence(curPlayer, players[(turn+1)%4], System.currentTimeMillis()); //TO DO: initiate planet stuff properly thru 1 ancestral method
					minigame1 = new InTheDark(curPlayer);
					//interval = System.currentTimeMillis();
				}
			}
		}
		
		else if(activePlanet == true){
			if(minigame1.getRunning() == true){
				minigame1.render(batch, shaperenderer);
			}
			else{
				activePlanet = false;
				activeRoll = true;
				turn += 1;
				curPlayer = players[turn%4]; //changes active player to next one
			}
		}
		
		if(Gdx.input.isKeyPressed(Keys.Z)){ //activates or deactivates stats screen
			if(activeStats == true){
				activeStats = false;
			}
			else{
				activeStats = true;
			}
		}
		if(activeStats == true && activePlanet == false){ //not doing activity on the planet
			seeStats();
		}
	}
	
	public boolean planetMove(Sprite shipSprite){ //shows player moving to destination planet step by step, returns if player there yet
		
		int nextStop = 0;
		for(boolean boo : checkpoints){
			nextStop++;
			if(boo == false){
				break;
			}
		}
		Rectangle planRect = planets[players[turn%4].getSpot() + nextStop].getRekt(); //the next area player's ship will be moving to
		
		batch.draw(board,  0, 0);
		for(int i = 0; i<planets.length; i++){ //to draw all the planets on the board
			batch.draw(planets[i].getText(), planets[i].getRekt().getX(), planets[i].getRekt().getY());
		}
		for(Player p : players){ 
			if(!p.equals(curPlayer)){ //not current player
				p.getSSprite().draw(batch); //draws dormant ship on their spot while active players is moving
			}
		}

		if(shipSprite.getBoundingRectangle().overlaps(planRect)){ //ship sprite reached this planet
			checkpoints[nextStop-1] = true; //landed on that planet, moving on
		}
		//!shipSprite.getBoundingRectangle().overlaps(planets[players[turn%4 - 1].getSpot() + rollNum].getRekt())
		if(checkpoints[checkpoints.length-1] == true){ //player sprite reaches target planet
			return false;
		}
		curPlayer.setSPosition((float)(shipSprite.getX() + (planRect.getX() - shipSprite.getX())*0.09), (float)(shipSprite.getY() + (planRect.getY() - shipSprite.getY())*0.09));
		shipSprite.draw(batch);
		return true;	
	}
	
	public int roll(){ //returns the die number when player chooses to stop roll
		centerText(font120, "<SPACE> TO STOP", 700);
		int num = rand.nextInt(6); //random number
		centerImg(diceFaces[num], 400);
		if(Gdx.input.isKeyPressed(Keys.SPACE)){
			return num;
		}
		return -1;
	}
	public void reset(){ //resets all the values if user finishes game or returns to menu
		for(Player p : players){
			p.setSpot(-p.getSpot());
		}
	}
	
	@Override
	public void create () { //loading and initializing data
		//classes commonly used 
		batch = new SpriteBatch();
		rand = new Random();
		shaperenderer = new ShapeRenderer();
		
		screen = new Rectangle(0,0,1500,900);
		page = 0; //menu screen
		turn = 0; //tracks what turn it is, %4 to get player index in players array
		rollNum = 0; //number player rolls
		
		//stand-in player list
		players = new Player[4];
		players[0] = new Player(1, new Texture("inthedark/player.png"));
		players[1] = new Player(1, new Texture("inthedark/player.png"));
		players[2] = new Player(1, new Texture("inthedark/player.png"));
		players[3] = new Player(1, new Texture("inthedark/player.png")); 
		
		//flags
		activeRoll = false;
		activeBoard = false;
		activePlanet = false;
		activeStats = false;
		
		//font generation
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Galaxy Force.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 120; //font size 120 pixels
		parameter.color = Color.WHITE;
		font120 = generator.generateFont(parameter); 
		//font120.setColor(Color.WHITE);
		parameter.size = 75;
		font75 = generator.generateFont(parameter);
		//font75.setColor(Color.WHITE);
		generator.dispose(); //disposes to avoid memory leaks
		//----------------
		
		//image loading
		menu = new Texture("menu.png");
		credits = new Texture("credits.png");
		instruct = new Texture("instruct.png");
		astro = new Sprite(new Texture("astronaut.png"));
		astro.setOriginCenter();
		board = new Texture("planets/galaxySpots.png");
		astro.setPosition(0,0);
		astroVX = 5;
		astroVY = 5;
		
		diceFaces = new Texture[6];
		for(int i = 0; i<6; i++){
			diceFaces[i] = new Texture(Gdx.files.internal("dice/" + (i+1) + ".png"));
		}
		//-------------
		
		//planet creation
		//reads from spotsinfo text file to create all planets, put into array
		planets = new Planet[7];
		FileHandle file = Gdx.files.internal("planets/spotsinfo.txt");
		BufferedReader reader = new BufferedReader(file.reader());
		
		String line = null;
		int i = 0;
		try {
			line = reader.readLine();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while( line != null ) {
			try {
				planets[i] = new Planet(line);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				line = reader.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			i++;
		}
		//------
		
		curPlayer = players[0];
		
		//misc---
		boolean[]checkpoints = new boolean[6];
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		shaperenderer.begin(ShapeType.Filled);
		
		if(page == 0){
			menu();
		}
		else if(page == 1){

			game();
		}
		else if(page == 2){
			instruct();
		}
		else if(page == 3){
			credits();
		}
		shaperenderer.end();
		batch.end();
		
		//time delay
		try{
			Thread.sleep(100); //delays loop for 100 milliseconds so input, operations slowed down for user
		}
		catch(InterruptedException ie){
			System.out.print("NOOB");
		}
		
	}
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
	
}


