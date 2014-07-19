package com.mygdx.game;


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

public class CreditsScreen implements Screen {
    PongForAndroid game;
    Stage stage;
    Table table;

    public CreditsScreen(PongForAndroid g) {
        int WIDTH = PongForAndroid.WIDTH;
        int HEIGHT = PongForAndroid.HEIGHT;
        game = g;
        stage = new Stage(new StretchViewport(WIDTH, HEIGHT));
        Gdx.input.setInputProcessor(stage);
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        table = new Table();
        table.setFillParent(true);

        Label creditLabel = new Label("PROGRAMMING AND GAME DESIGN \n Justin Armstrong", skin);
        Label musicLabel = new Label("MUSIC \n" +
                "bart: Through Pixelated Clouds\n" +
                "FoxSynergy: Never Stop Running\n" +
                "http://opengameart.org", skin);

        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                game.setScreen(new MainMenuScreen(game));
                dispose();
            }
        });

        table.add(creditLabel).pad(50);
        table.row();
        table.add(musicLabel);
        table.row();

        table.add(backButton).width(200).height(75).expandY().bottom();

        stage.addActor(table);
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.075f, 0.059f, 0.188f, 1);
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
        stage.dispose();

    }
}
