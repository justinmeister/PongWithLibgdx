package com.collywobble.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Back;

public class CreditsScreen implements Screen {
    PongForAndroid game;
    Stage stage;
    Table table;
    Screen nextScreen;

    public CreditsScreen(PongForAndroid g) {
        int WIDTH = PongForAndroid.WIDTH;
        int HEIGHT = PongForAndroid.HEIGHT;
        game = g;
        stage = new Stage(new StretchViewport(WIDTH, HEIGHT));
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        table = new Table();
        table.setFillParent(true);

        Label creditLabel0 = new Label("PROGRAMMING AND GAME DESIGN", skin);
        Label creditLabel1 = new Label("Justin Armstrong", skin);
        Label creditLabel2 = new Label(" ", skin);
        Label musicLabel0 = new Label("MUSIC", skin);
        Label musicLabel1 = new Label("bart: Through Pixelated Clouds", skin);
        Label musicLabel2 = new Label("FoxSynergy: Never Stop Running", skin);
        Label musicLabel3 = new Label("http://opengameart.org", skin);

        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                Gdx.input.setInputProcessor(null);
                setOutroTween();
            }
        });

        table.add(creditLabel0);
        table.row();
        table.add(creditLabel1);
        table.row();
        table.add(creditLabel2);
        table.row();
        table.add(musicLabel0);
        table.row();
        table.add(musicLabel1);
        table.row();
        table.add(musicLabel2);
        table.row();
        table.add(musicLabel3);
        table.row();

        table.add(backButton).width(200).height(75).pad(50);

        stage.addActor(table);
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.075f, 0.059f, 0.188f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.tweenManager.update(delta);

        stage.act(delta);
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        setTableTween();


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

    private void setTableTween() {
        table.setX(-800);
        Tween.to(table, TableAccessor.POSITION_XY, .8f)
                .targetRelative(800, 0)
                .ease(Back.OUT)
                .start(game.tweenManager);

    }

    private void setOutroTween() {
        TweenCallback tweenCallback = new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                dispose();
                game.setScreen(new MainMenuScreen(game));
            }
        };

        Tween.to(table, TableAccessor.POSITION_X, .8f)
                .targetRelative(800)
                .ease(Back.IN)
                .setCallback(tweenCallback)
                .start(game.tweenManager);
    }
}
