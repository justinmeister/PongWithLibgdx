package com.collywobble.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.equations.Back;

public class SettingsScreen implements Screen {
    PongForAndroid game;
    Stage stage;
    final int WIDTH = PongForAndroid.WIDTH;
    final int HEIGHT = PongForAndroid.HEIGHT;
    Table table;
    TextButton musicSwitchButton;
    String buttonLabel;
    String state;

    final String NORMAL_STATE = "normal state";
    final String OUTRO_STATE = "outro state";

    public SettingsScreen(PongForAndroid g) {
        game = g;
        state = NORMAL_STATE;
        stage = new Stage(new StretchViewport(WIDTH, HEIGHT));
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        table = new Table();
        table.setFillParent(true);

        if (game.musicOn) {
            buttonLabel = "Turn Off Music";
        } else {
            buttonLabel = "Turn On Music";
        }

        musicSwitchButton = new TextButton(buttonLabel, skin);
        musicSwitchButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent e, float x, float y, int point, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                if (game.musicOn) {
                    game.musicOn = false;
                    musicSwitchButton.setText("Turn On Music");
                    game.musicToPlay.stop();
                } else {
                    game.musicOn = true;
                    musicSwitchButton.setText("Turn Off Music");
                    game.musicToPlay.play();
                }
            }
        });

        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                state = OUTRO_STATE;
                setOutroTween();
                Gdx.input.setInputProcessor(null);
            }
        });


        table.add(musicSwitchButton).width(200).height(75);
        table.row();
        table.add(backButton).pad(10).width(200).height(75);
        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.075f, 0.059f, 0.188f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.tweenManager.update(delta);
        stage.act(delta);
        stage.draw();
        if (state.equals(OUTRO_STATE)) { checkForTweenEnd(); }
    }

    private void checkForTweenEnd() {
        if (!game.tweenManager.containsTarget(table)) {
            game.setScreen(new MainMenuScreen(game));
            dispose();
        }
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
        Tween.to(table, TableAccessor.POSITION_X, .8f)
                .targetRelative(800)
                .ease(Back.IN)
                .start(game.tweenManager);
    }
}
