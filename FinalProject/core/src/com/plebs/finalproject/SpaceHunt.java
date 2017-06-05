package com.plebs.finalproject;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.*;
import java.io.*;

public class SpaceHunt extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	ArrayList<Planet>allPlanets;
	Scanner inFile;
	
	private Texture board;
	
	@Override
	public void create(){
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		board = new Texture("board.png");
		//remember = new Remember(new Player(1, new Texture("badlogic.jpg")),new Player(2,new Texture("badlogic.jpg")));
		//remember.setStartTime(System.currentTimeMillis());
		FileHandle file = Gdx.files.internal("spotsinfo.txt");
		BufferedReader reader = new BufferedReader(file.reader());
		allPlanets = new ArrayList<Planet>();
		String line = null;
		try {
			line = reader.readLine();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while( line != null ) {
			try {
				allPlanets.add(new Planet(line));
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
		}
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		batch.draw(board, 0, 0);
		for (Planet plan: allPlanets){
			plan.drawPlanet(batch);
		}
		//remember.render(batch);
		batch.end();
		
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
