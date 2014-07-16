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
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class PongBoard implements Screen {
    final PongForAndroid game;
    private final int HEIGHT = PongForAndroid.HEIGHT;
    private final int WIDTH = PongForAndroid.WIDTH;
    private Paddle paddle1;
    private Paddle paddle2;
    private Ball ball;
    private OrthographicCamera camera;
    private Array<Paddle> paddleList;
    private Array<Rectangle> net;
    private Texture netTexture;
    private int player1Score;
    private int player2Score;
    private Sound paddleCollisionSound;
    private boolean timeToShake;
    private long startOfShakeTime;
    private ParticleEmitter particleEmitter;
    private int paddleHits;
    private boolean allowScreenShake;
    BitmapFont scoreFont;

    public PongBoard(final PongForAndroid gam) {
        this.game = gam;
        timeToShake = false;
        scoreFont = getScoreFont();
        player1Score = 0;
        player2Score = 0;
        paddleHits = 0;
        paddleCollisionSound = Gdx.audio.newSound(Gdx.files.internal("ping.wav"));
        game.musicToPlay = Gdx.audio.newMusic(Gdx.files.internal("recall_of_the_shadows.mp3"));
        setupPaddles();
        setupNet();
        ball = new Ball();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        particleEmitter = new ParticleEmitter();
        Gdx.input.setInputProcessor(new MainInputProcessor());
    }

    private BitmapFont getScoreFont() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
                Gdx.files.internal("fonts/LiberationMono-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 55;
        BitmapFont titleFont = generator.generateFont(parameter);
        generator.dispose();

        return titleFont;
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
        paddleList = new Array<Paddle>();
        paddleList.add(paddle1);
        paddleList.add(paddle2);
    }

    public void setupNet() {
        net = new Array<Rectangle>();

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
        camera.update();
        screenShake();
        updateBallMovement(delta);
        checkPaddleOutOfBounds();
        checkForGameOver();
        checkTotalPaddleHits();
        particleEmitter.update(ball, delta);
        Gdx.gl.glClearColor(0.075f, 0.059f, 0.188f, 1);
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
                paddleHits++;
                ball.xVel *= -1;
                if (ball.xVel > 0) {ball.xVel += 20;} else {ball.xVel -= 20;}

                paddleCollisionSound.play();
                startScreenShake();
                if (hitPaddle.name.equals("paddle1")) {
                    ball.setPosition((hitPaddle.x + hitPaddle.width), ball.y);
                } else if (hitPaddle.name.equals("paddle2")) {
                    ball.setPosition((hitPaddle.x - ball.width), ball.y);
                }
            }
        }
    }

    private void startScreenShake() {
        if (allowScreenShake) {
            timeToShake = true;
            startOfShakeTime = TimeUtils.millis();
        }
    }

    private void screenShake() {
        if (timeToShake) {
            camera.position.set(400, 240, 0);
            float randomX = (float) (Math.random() * 10 + 1) - 5;
            float randomY = (float) (Math.random() * 10 + 1) - 5;
            camera.translate(randomX, randomY);

            if (TimeUtils.timeSinceMillis(startOfShakeTime) > 200) {
                timeToShake = false;
                camera.position.set(400, 240, 0);
            }
        }
    }

    private void checkForBallOutOfBounds() {
        if (ball.x < 0) {
            ball.resetPosition();
            ball.reverseDirectionX();
            ball.reverseDirectionY();
            ball.resetVelocityX(1);
            player2Score++;
            enterNormalState();
        } else if (ball.getRight() > WIDTH) {
            ball.resetPosition();
            ball.reverseDirectionX();
            ball.reverseDirectionY();
            ball.resetVelocityX(-1);
            player1Score++;
            enterNormalState();
        }
    }

    private void enterNormalState() {
        paddleHits = 0;
        particleEmitter.setState("stop_emit");
        allowScreenShake = false;
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

    private void checkForGameOver() {
        if (player1Score >= 1 || player2Score >= 1) {
            if (player1Score >= 1) {
                game.winningPlayer = "Player 1";
                game.player1Score++;
            } else {
                game.winningPlayer = "Player 2";
                game.player2Score++;
            }
            game.setScreen(new WinScreen(game));
            dispose();
        }
    }

    private void checkTotalPaddleHits() {
        if (paddleHits >= 3) {
            particleEmitter.setState("emit");
            allowScreenShake = true;
        }
    }

    private void batchDraw() {
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(paddle1.paddleImage, paddle1.x, paddle1.y);
        game.batch.draw(paddle2.paddleImage, paddle2.x, paddle2.y);
        for (Rectangle netPiece : net) {
            game.batch.draw(netTexture, netPiece.getX(), netPiece.getY());
        }
        scoreFont.draw(game.batch,
                String.valueOf(player1Score),
                200,
                HEIGHT - 50);
        scoreFont.draw(game.batch,
                String.valueOf(player2Score),
                WIDTH - 200,
                HEIGHT - 50);
        particleEmitter.drawParticles(game.batch);
        game.batch.draw(ball.ballImage, ball.x, ball.y);
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
        if (game.musicOn) {
            game.musicToPlay.play();
        }
    }

    @Override
    public void dispose() {
        paddle1.dispose();
        paddle2.dispose();
        ball.dispose();
        netTexture.dispose();
        paddleCollisionSound.dispose();
        }
    }




