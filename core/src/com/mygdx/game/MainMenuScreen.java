package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.equations.Back;



public class MainMenuScreen implements Screen {

    private PongForAndroid game;
    private Stage stage;
    private Ball ball;
    Table table;
    ParticleEmitter particleEmitter;
    int WIDTH;
    int HEIGHT;
    String state;
    Screen nextScreen;

    final String INTRO_STATE = "intro state";
    final String NORMAL_STATE = "normal state";
    final String OUTRO_STATE = "outro state";


    public MainMenuScreen(PongForAndroid g) {
        game = g;
        state = INTRO_STATE;
        particleEmitter = new ParticleEmitter(game);
        WIDTH = PongForAndroid.WIDTH;
        HEIGHT = PongForAndroid.HEIGHT;

        if (!game.musicCurrentlyPlaying) {
            game.musicToPlay = game.assetManager.get("8bit_airship.ogg", Music.class);
            game.musicToPlay.setVolume(0.45f);
        }
        stage = new Stage(new StretchViewport(WIDTH, HEIGHT));
        Gdx.input.setInputProcessor(stage);


        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

        table = new Table();
        table.setFillParent(true);

        Label titleLabel = new Label("PARTICLE PONG", game.titleStyle);

        TextButton textButton = new TextButton("2-Player \n First to Five", skin);
        textButton.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                nextScreen = new PongBoard(game);
                setOutroTween();
                state = OUTRO_STATE;

            }
        });

        TextButton settingsButton = new TextButton("Settings", skin);
        settingsButton.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                nextScreen = new SettingsScreen(game);
                state = OUTRO_STATE;
                setOutroTween();

            }
        });

        TextButton creditsButton = new TextButton("Credits", skin);
        creditsButton.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                nextScreen = new CreditsScreen(game);
                state = OUTRO_STATE;
                setOutroTween();

            }
        });




        table.add(titleLabel).pad(30);
        table.row();
        table.add(textButton).width(200).height(75);
        table.row();
        table.add(settingsButton).width(200).height(75);
        table.row();
        table.add(creditsButton).width(200).height(75);

        stage.addActor(table);


    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.075f, 0.059f, 0.188f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (state.equals(INTRO_STATE)) {
            introUpdate(delta);
        } else if (state.equals(NORMAL_STATE)) {
            normalUpdate(delta);
        } else if (state.equals(OUTRO_STATE)) {
            outroUpdate(delta);
        }
    }

    private void introUpdate(float delta) {
        game.tweenManager.update(delta);
        stage.act();
        stage.draw();
        endStateCheck();
    }

    private void endStateCheck() {
        if (!game.tweenManager.containsTarget(table)) {
            if (state.equals(INTRO_STATE)) {
                state = NORMAL_STATE;
                ball = new Ball(game);
                particleEmitter.setState("emit");
            } else if (state.equals(OUTRO_STATE)) {
                game.setScreen(nextScreen);
                dispose();
            }
        }
    }

    private void normalUpdate(float delta) {
        updateBallMovement(delta);
        particleEmitter.update(ball, delta);
        stage.act(delta);
        batchDraw();
        stage.draw();
    }

    private void outroUpdate(float delta) {
        updateBallMovement(delta);
        particleEmitter.update(ball, delta);
        stage.act();
        batchDraw();
        stage.draw();
        game.tweenManager.update(delta);
        endStateCheck();

    }

    private void updateBallMovement(float deltaTime) {
        if (!(ball == null)) {
            ball.moveX(deltaTime);
            checkForWallCollision();
            ball.moveY(deltaTime);
            checkForCeilingCollision();
        }
    }

    private void batchDraw() {
        game.batch.setProjectionMatrix(stage.getCamera().combined);
        game.batch.begin();
        particleEmitter.drawParticles(game.batch);
        if (!(ball == null)) {
            game.batch.draw(ball.ballImage, ball.x, ball.y);
        }
        game.batch.end();
    }

    private void checkForCeilingCollision() {
        if (ball.getTop() > HEIGHT) {
            ball.reverseDirectionY();
            ball.setTop(HEIGHT);
        } else if (ball.getBottom() < 0) {
            ball.reverseDirectionY();
            ball.setBottom(0f);
        }
    }

    private void checkForWallCollision() {
        if (ball.getRight() > WIDTH) {
            ball.xVel *= -1;
            ball.setRight(WIDTH);
        } else if (ball.getX() < 0) {
            ball.xVel *= -1;
            ball.setX(0);
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {
        setTableTween();

        if (game.musicOn && !game.musicCurrentlyPlaying) {
                game.musicCurrentlyPlaying = true;
                game.musicToPlay.play();
                game.musicToPlay.setLooping(true);
            }
        }

    private void setTableTween() {
        table.setX(-800);
        Tween.to(table, TableAccessor.POSITION_XY, .8f)
                .targetRelative(800, 0)
                .ease(Back.OUT)
                .start(game.tweenManager);
    }

    private void setOutroTween() {
        Tween.to(table, TableAccessor.POSITION_X, .8f)
                .targetRelative(800)
                .ease(Back.IN)
                .start(game.tweenManager);
    }


    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}
