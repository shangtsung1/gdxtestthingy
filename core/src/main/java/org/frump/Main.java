package org.frump;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Main extends ApplicationAdapter implements InputProcessor {

    private OrthographicCamera mainCamera;
    private ExtendViewport mainViewport;
    private SpriteBatch mainSpriteBatch;

    FrameBuffer fBuffer;
    private OrthographicCamera bufferCamera;
    private ExtendViewport bufferViewport;
    private TextureRegion bufferTexture;
    private ShapeRenderer bufferShapeRenderer;

    private int BUFFER_SIZE = 128;

    @Override
    public void create() {
        mainCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        mainViewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), mainCamera);
        mainSpriteBatch = new SpriteBatch();

        fBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, BUFFER_SIZE, BUFFER_SIZE, false);
        bufferTexture = new TextureRegion(fBuffer.getColorBufferTexture());
        bufferTexture.flip(false, true);
        bufferCamera = new OrthographicCamera(BUFFER_SIZE, BUFFER_SIZE);
        bufferCamera.setToOrtho(false, BUFFER_SIZE, BUFFER_SIZE);
        bufferCamera.viewportWidth = BUFFER_SIZE;
        bufferCamera.viewportHeight = BUFFER_SIZE;
        bufferCamera.update();
        bufferViewport = new ExtendViewport(BUFFER_SIZE, BUFFER_SIZE, bufferCamera);
        bufferViewport.update(BUFFER_SIZE, BUFFER_SIZE, false);
        bufferViewport.apply();
        bufferShapeRenderer = new ShapeRenderer();

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render() {
        fBuffer.begin();
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        bufferShapeRenderer.setProjectionMatrix(bufferCamera.combined);
        bufferShapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        bufferShapeRenderer.setColor(Color.RED);
        //draw a 1 pixel dot at 0,0 and 10,10 to be known coordinates.
        bufferShapeRenderer.rect(0,0,1,1);
        bufferShapeRenderer.rect(10,10,1,1);
        bufferShapeRenderer.end();
        fBuffer.end();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mainSpriteBatch.setProjectionMatrix(mainCamera.combined);
        mainSpriteBatch.begin();
        mainSpriteBatch.draw(bufferTexture,0,0,BUFFER_SIZE,BUFFER_SIZE);
        mainSpriteBatch.end();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        String output = "Screen("+screenX+","+screenY+")";
        //get world coordinates where we clicked(this will get where we clicked on the framebuffer.(assuming we clicked it))
        Vector3 worldCoords = mainCamera.unproject(new Vector3(screenX, screenY, 0));
        output += " MainCameraWorldCoords("+worldCoords.x+","+worldCoords.y+")";

        //since we now have world coords, we can determine where we clicked on the framebuffer by unprojecting with its camera?
        Vector3 inBufferWorldCoords = bufferCamera.unproject(worldCoords);
        output += " inBufferWorldCoords("+inBufferWorldCoords.x+","+inBufferWorldCoords.y+")";
        System.out.println(output);
        return false;
    }


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
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
