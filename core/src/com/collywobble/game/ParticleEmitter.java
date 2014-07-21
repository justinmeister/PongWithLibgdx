package com.collywobble.game;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class ParticleEmitter {
    Array<Particle> particles = new Array<Particle>();
    long timer;
    final int MAX_PARTICLE_SPEED = 50;
    public Texture bigTexture;
    public Texture mediumTexture;
    public Texture smallTexture;
    public String state = "stop_emit";

    public ParticleEmitter(PongForAndroid game) {
        bigTexture = game.largeParticleImage;
        mediumTexture = game.mediumParticleImage;
        smallTexture = game.smallParticleImage;
        timer = TimeUtils.millis();
    }

    public void update(Ball ball, float delta) {
        if (state.equals("emit")) {
            makeParticles(ball, delta);
            updateParticles(delta);
            killOldParticles();
        } else {
            updateParticles(delta);
            killOldParticles();
        }
    }

    private void makeParticles(Ball ball, float delta) {
        if (TimeUtils.timeSinceMillis(timer)
                > (14 / ball.getCombinedVelocity(delta))) {
            for (int i = 0; i < 10; i++) {
                float x = ball.getX() + (float) Math.random() * ball.getWidth();
                float y = ball.getY() + (float) Math.random() * ball.getHeight();
                float xVel = (float) Math.random() * MAX_PARTICLE_SPEED - (MAX_PARTICLE_SPEED / 2);
                float yVel = (float) Math.random() * MAX_PARTICLE_SPEED - (MAX_PARTICLE_SPEED / 2);
                particles.add(new Particle(x, y, xVel, yVel, TimeUtils.millis(), this));
            }
            timer = TimeUtils.millis();
        }
    }

    private void updateParticles(float delta) {
        for (Particle particle : particles) {
            particle.update(delta);
        }
    }

    private void killOldParticles() {
        Iterator<Particle> itr = particles.iterator();
        while (itr.hasNext()) {
            Particle particle = itr.next();
            if (TimeUtils.timeSinceMillis(particle.birthTime) > 500) {
                itr.remove();
            }
        }
    }

    public void drawParticles(Batch batch) {
        for (Particle particle : particles) {
            batch.draw(particle.getImage(), particle.getX(), particle.getY());
        }
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
