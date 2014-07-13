package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;


public class PongBoard implements Screen {
    final PongForAndroid game;
    private final int HEIGHT = PongForAndroid.HEIGHT;
    private final int WIDTH = PongForAndroid.WIDTH;
    private Paddle paddle1;
    private Paddle paddle2;
    private Ball ball;
    private OrthographicCamera camera;
    private ArrayList<Paddle> paddleList;
    private ArrayList<Rectangle> net;
    private Texture netTexture;
    private BitmapFont arialFont;
    private int player1Score;
    private int player2Score;
    private Sound paddleCollisionSound;
    private Music mainMusic;

    public PongBoard(final PongForAndroid gam) {
        this.game = gam;
        arialFont = new BitmapFont();
        arialFont.scale(3);
        player1Score = 0;
        player2Score = 0;
        paddleCollisionSound = Gdx.audio.newSound(Gdx.files.internal("ping.wav"));
        mainMusic = Gdx.audio.newMusic(Gdx.files.internal("tron_music.ogg"));
        setupPaddles();
        setupNet();
        ball = new Ball();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        Gdx.input.setInputProcessor(new MainInputProcessor());
    }

    public class MainInputProcessor implements InputProcessor{
        private final Vector3 tmpV = new Vector3();

        @Override
        public boolean keyDown(int keycode) {
            return false;
        }

        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            setPaddleLocation(camera.unproject(tmpV.set(screenX, screenY, 0)));
            return true;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            setPaddleLocation(camera.unproject(tmpV.set(screenX, screenY, 0)));
            return true;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(int amount) {
            return false;
        }

        public void setPaddleLocation(Vector3 pos) {
            if (pos.x < (WIDTH / 2)) {
                paddle1.setCenterY(pos.y);
            } else if (pos.x > (WIDTH / 2)) {
                paddle2.setCenterY(pos.y);
            }
        }
    }

    public void setupPaddles() {
        paddle1 = new Paddle("paddle1", 50);
        paddle2 = new Paddle("paddle2", 750);
        paddleList = new ArrayList<Paddle>();
        paddleList.add(paddle1);
        paddleList.add(paddle2);
    }

    public void setupNet() {
        net = new ArrayList<Rectangle>();

        for (int i = 0; i < 6; i++) {
            int xPos = (WIDTH / 2);
            int yPos = 0;
            Pixmap netPixmap = new Pixmap(5, 5, Pixmap.Format.RGBA8888);
            netPixmap.setColor(Color.WHITE);
            netPixmap.fill();
            netTexture = new Texture(netPixmap);
            Rectangle newNetPiece = new Rectangle();

            newNetPiece.x = xPos;
            newNetPiece.y = yPos + (i * HEIGHT / 6) + 35;
            newNetPiece.width = netTexture.getWidth();
            newNetPiece.height = netTexture.getHeight();

            net.add(newNetPiece);
            netPixmap.dispose();
        }
    }

    @Override
    public void render(float delta) {
        float deltaTime = Gdx.graphics.getDeltaTime();

        camera.update();
        updateBallMovement(deltaTime);
        checkPaddleOutOfBounds();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batchDraw();
    }

    private void updateBallMovement(float deltaTime) {
        ball.moveX(deltaTime);
        checkForPaddleCollision();
        checkForBallOutOfBounds();
        ball.moveY(deltaTime);
        checkForWallCollision();
    }

    private void checkForPaddleCollision() {
        for (Paddle hitPaddle : paddleList) {
            if (Intersector.overlaps(hitPaddle, ball)) {
                ball.xVel *= -1;
                paddleCollisionSound.play();
                if (hitPaddle.name.equals("paddle1")) {
                    ball.setPosition((hitPaddle.x + hitPaddle.width), ball.y);
                } else if (hitPaddle.name.equals("paddle2")) {
                    ball.setPosition((hitPaddle.x - ball.width), ball.y);
                }
            }
        }
    }

    private void checkForBallOutOfBounds() {
        if (ball.x < 0) {
            ball.resetPosition();
            ball.reverseDirectionX();
            ball.reverseDirectionY();
            player2Score++;
        } else if (ball.getRight() > WIDTH) {
            ball.resetPosition();
            ball.reverseDirectionX();
            ball.reverseDirectionY();
            player1Score++;
        }
    }

    private void checkForWallCollision() {
        if (ball.getTop() > HEIGHT) {
            ball.reverseDirectionY();
            ball.setTop(HEIGHT);
        } else if (ball.getY() < 0) {
            ball.reverseDirectionY();
            ball.setBottom(0f);
        }
    }

    private void checkPaddleOutOfBounds() {
        for (Paddle paddle : paddleList) {
            if (paddle.getTop() > HEIGHT) {
                paddle.setTop(HEIGHT);
            } else if (paddle.y < 0) {
                paddle.setY(0);
            }
        }
    }

    private void batchDraw() {
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(paddle1.paddleImage, paddle1.x, paddle1.y);
        game.batch.draw(paddle2.paddleImage, paddle2.x, paddle2.y);
        game.batch.draw(ball.ballImage, ball.x, ball.y);
        for (Rectangle netPiece : net) {
            game.batch.draw(netTexture, netPiece.getX(), netPiece.getY());
        }
        arialFont.draw(game.batch,
                String.valueOf(player1Score),
                200,
                HEIGHT - 50);
        arialFont.draw(game.batch,
                String.valueOf(player2Score),
                WIDTH - 200,
                HEIGHT - 50);

        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void resume() {}

    @Override
    public void pause() {}

    @Override
    public void hide() {}

    @Override
    public void show() {
        mainMusic.play();
    }

    @Override
    public void dispose() {
        paddle1.dispose();
        paddle2.dispose();
        ball.dispose();
        netTexture.dispose();
        }
    }




