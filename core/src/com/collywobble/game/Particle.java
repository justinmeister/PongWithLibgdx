package com.collywobble.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;


public class Particle extends Rectangle {
    Texture image;
    float xVel;
    float yVel;
    long birthTime;
    long shrinkTimer;
    private String state = "stage1";
    ParticleEmitter particleEmitter;

    public Particle(float posX, float posY, float xVel,
                    float yVel, long birthTime, ParticleEmitter particleEmitter) {
        this.particleEmitter = particleEmitter;
        image = particleEmitter.bigTexture;
        setSize();
        x = posX;
        y = posY;
        this.xVel = xVel;
        this.yVel = yVel;
        this.birthTime = birthTime;
        shrinkTimer = birthTime;
    }

    public Texture getImage() {
        return image;
    }

    public void setImage(Texture image) {
        this.image = image;
    }

    public void update(float delta) {
        shrink();
        move(delta);
    }

    private void shrink() {
        if (TimeUtils.timeSinceMillis(shrinkTimer) > 170) {
            if (state.equals("stage1")) {
                state = "stage2";
                image = particleEmitter.mediumTexture;
                setSize();
                shrinkTimer = TimeUtils.millis();
            } else if (state.equals("stage2")) {
                state = "stage3";
                image = particleEmitter.smallTexture;
                setSize();
            }
        }
    }

    public void move(float delta) {
        x += xVel * delta;
        y += yVel * delta;
    }

    private void setSize() {
        this.width = image.getWidth();
        this.height = image.getHeight();
    }
}
