package com.collywobble.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Back;


public class WinScreen implements Screen {
    PongForAndroid game;
    Stage stage;
    Table table;
    final int HEIGHT = PongForAndroid.HEIGHT;
    final int WIDTH = PongForAndroid.WIDTH;

    public WinScreen(PongForAndroid g) {
        BitmapFont titleFont = getFont();

        game = g;
        stage = new Stage(new StretchViewport(WIDTH, HEIGHT));
        LabelStyle labelStyle = new LabelStyle(titleFont, Color.WHITE);
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));


        table = new Table();
        table.setFillParent(true);
        Label winLabel = new Label(game.winningPlayer + " wins.", labelStyle);

        TextButton menuButton = new TextButton("Main Menu", skin);
        menuButton.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                setOutroTween();
            }
        });

        Label player1ScoreLabel = new Label(
                "Player 1 Total Wins: " + String.valueOf(game.player1Score), skin);
        Label player2ScoreLabel = new Label(
                "Player 2 Total Wins: " + String.valueOf(game.player2Score), skin);

        table.add(winLabel);
        table.row();
        table.add(player1ScoreLabel);
        table.row();
        table.add(player2ScoreLabel);
        table.row();
        table.add(menuButton).pad(20).width(200).height(75);
        stage.addActor(table);
    }

    private BitmapFont getFont() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
                Gdx.files.internal("fonts/LiberationMono-Regular.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 32;
        BitmapFont titleFont = generator.generateFont(parameter);
        generator.dispose();

        return titleFont;
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
        setTableTween();
        Gdx.input.setInputProcessor(stage);
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
        game.musicToPlay.stop();
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
