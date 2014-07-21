package com.collywobble.game;


import com.badlogic.gdx.graphics.Camera;

import aurelienribon.tweenengine.TweenAccessor;

public class CameraAccessor implements TweenAccessor<Camera> {

    public static final int POSITION_X = 1;
    public static final int POSITION_Y = 2;
    public static final int POSITION_XY = 3;

    @Override
    public int getValues(Camera target, int tweenType, float[] returnValues) {
        switch (tweenType) {
            case POSITION_X: returnValues[0] = target.position.x; return 1;
            case POSITION_Y: returnValues[0] = target.position.y; return 1;
            case POSITION_XY:
                returnValues[0] = target.position.x;
                returnValues[1] = target.position.y;
                return 2;
            default: assert false; return -1;

        }
    }

    @Override
    public void setValues(Camera target, int tweenType, float[] newValues) {
        switch (tweenType) {
            case POSITION_X: target.position.x = newValues[0]; break;
            case POSITION_Y: target.position.y = newValues[0]; break;
            case POSITION_XY:
                target.position.x = newValues[0];
                target.position.y = newValues[1];
                break;
            default: assert false; break;
        }
    }
}
