package com.mygdx.game;


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


public class MainMenuScreen implements Screen {

    private PongForAndroid game;
    private Stage stage;
    private Ball ball;
    ParticleEmitter particleEmitter;
    int WIDTH;
    int HEIGHT;


    public MainMenuScreen(PongForAndroid g) {
        ball = new Ball();
        particleEmitter = new ParticleEmitter();
        particleEmitter.setState("emit");
        WIDTH = PongForAndroid.WIDTH;
        HEIGHT = PongForAndroid.HEIGHT;
        BitmapFont titleFont = getTitleFont();

        game = g;
        if (!game.musicCurrentlyPlaying) {
            game.musicToPlay = Gdx.audio.newMusic(Gdx.files.internal("8bit_airship.ogg"));
            game.musicToPlay.setVolume(0.45f);
        }
        stage = new Stage(new StretchViewport(WIDTH, HEIGHT));
        Gdx.input.setInputProcessor(stage);
        LabelStyle titleStyle = new LabelStyle(titleFont, Color.WHITE);


        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        Table table = new Table();
        table.setFillParent(true);

        Label titleLabel = new Label("PARTICLE PONG", titleStyle);

        TextButton textButton = new TextButton("2-Player \n First to Five", skin);
        textButton.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                game.musicToPlay.stop();
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

        table.add(titleLabel).pad(30);
        table.row();
        table.add(textButton).width(200).height(75);
        table.row();
        table.add(settingsButton).width(200).height(75);
        table.row();
        table.add(creditsButton).width(200).height(75);

        stage.addActor(table);
    }

    private BitmapFont getTitleFont() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
                Gdx.files.internal("fonts/LiberationMono-Regular.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 50;
        BitmapFont titleFont = generator.generateFont(parameter);
        generator.dispose();

        return titleFont;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.075f, 0.059f, 0.188f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        updateBallMovement(delta);
        particleEmitter.update(ball, delta);

        stage.act(delta);
        batchDraw();
        stage.draw();
    }


    private void updateBallMovement(float deltaTime) {
        ball.moveX(deltaTime);
        checkForWallCollision();
        ball.moveY(deltaTime);
        checkForCeilingCollision();
    }

    private void batchDraw() {
        game.batch.setProjectionMatrix(stage.getCamera().combined);
        game.batch.begin();
        particleEmitter.drawParticles(game.batch);
        game.batch.draw(ball.ballImage, ball.x, ball.y);
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
        if (game.musicOn && !game.musicCurrentlyPlaying) {
                game.musicCurrentlyPlaying = true;
                game.musicToPlay.play();
            }
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
