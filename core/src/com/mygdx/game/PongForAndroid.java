package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class PongForAndroid extends Game {
    public static final int WIDTH=800,HEIGHT=480;
    public SpriteBatch batch;

	@Override
	public void create () {
        batch = new SpriteBatch();
        setScreen(new PongBoard(this));
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
