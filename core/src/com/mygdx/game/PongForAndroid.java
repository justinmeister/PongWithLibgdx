package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class PongForAndroid extends Game {
    public static final int WIDTH=800,HEIGHT=480;
    public SpriteBatch batch;
    public Screen mainMenu;
    public PongBoard pongBoard;
    public boolean musicOn = true;
    public String winningPlayer = "Player 1";

	@Override
	public void create () {


        batch = new SpriteBatch();
        this.setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render () {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
