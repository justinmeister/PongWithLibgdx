package com.mygdx.game;


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

public class SettingsScreen implements Screen {
    PongForAndroid game;
    Stage stage;
    final int WIDTH = PongForAndroid.WIDTH;
    final int HEIGHT = PongForAndroid.HEIGHT;
    Table table;
    TextButton musicSwitchButton;
    String buttonLabel;

    public SettingsScreen(PongForAndroid g) {
        game = g;
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
                } else {
                    game.musicOn = true;
                    musicSwitchButton.setText("Turn Off Music");
                }
            }
        });

        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent e, float x, float y, int point, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                game.setScreen(new MainMenuScreen(game));
                dispose();
            }
        });


        table.add(musicSwitchButton).width(150).height(50);
        table.row();
        table.add(backButton).pad(20);
        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();


    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {

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

    }
}
