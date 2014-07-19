package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;


public class PongForAndroid extends Game {
    public static final int WIDTH=800,HEIGHT=480;
    public SpriteBatch batch;
    public Screen mainMenu;
    public PongBoard pongBoard;
    public boolean musicOn = true;
    public String winningPlayer = "Player 1";
    public int player1Score = 0;
    public int player2Score = 0;
    public Music musicToPlay;
    public boolean musicCurrentlyPlaying = false;
    public TweenManager tweenManager;

	@Override
	public void create () {
        tweenManager = new TweenManager();
        Tween.registerAccessor(Camera.class, new CameraAccessor());
        Tween.registerAccessor(Table.class, new TableAccessor());

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
