package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Paddle extends Rectangle {
    Texture paddleImage;
    String name;
    float diffY;

    public Paddle(String name, int x) {
        this.name = name;
        Pixmap paddlePixmap = new Pixmap(10, 100, Pixmap.Format.RGBA8888);
        paddlePixmap.setColor(Color.WHITE);
        paddlePixmap.fill();
        this.paddleImage = new Texture(paddlePixmap);
        this.width = paddleImage.getWidth();
        this.height = paddleImage.getHeight();
        this.x = x;
        this.y = (PongForAndroid.HEIGHT / 2) - (this.height / 2);
    }

    public void dispose() {
        paddleImage.dispose();
    }

    public float getCenterY() {
        return this.y + (this.height / 2);
    }

    public void setCenterY(float posY) {
        this.y = posY - (this.height / 2);
    }

}