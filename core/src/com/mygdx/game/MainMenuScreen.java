package com.mygdx.game;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainMenuScreen implements Screen {

    private PongForAndroid game;
    private Stage stage;
    private final int WIDTH = PongForAndroid.WIDTH;
    private final int HEIGHT = PongForAndroid.HEIGHT;
    private Table table;

    public MainMenuScreen(PongForAndroid g) {
        game = g;
        stage = new Stage(new StretchViewport(WIDTH, HEIGHT));
        Gdx.input.setInputProcessor(stage);
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        table = new Table();
        table.setFillParent(true);

        TextButton textButton = new TextButton("New Game", skin);
        textButton.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                game.setScreen(new PongBoard(game));
                dispose();
            }
        });

        TextButton settingsButton = new TextButton("Settings", skin);
        settingsButton.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                game.setScreen(new SettingsScreen(game));
                dispose();
            }
        });

        TextButton creditsButton = new TextButton("Credits", skin);
        creditsButton.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                game.setScreen(new CreditsScreen(game));
                dispose();
            }
        });

        table.add(textButton).width(150).height(50);
        table.row();
        table.add(settingsButton).width(150).height(50);
        table.row();
        table.add(creditsButton).width(150).height(50);
        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
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
        stage.dispose();
    }

}
