package com.alchera.game.Structure.Components.Camera;

import com.badlogic.gdx.math.Vector3;

public class BasicInterpolation implements Interpolation {
    @Override
    public Vector3 Interpolate(Vector3 position, float destinationX, float destinationY, float destinationZ) {
        position.x = position.x + (destinationX - position.x) * 0.1f;
        position.y = position.y + (destinationY - position.y) * 0.1f;
        position.z = position.z + (destinationZ - position.z) * 0.1f;
        return position;
    }
}
