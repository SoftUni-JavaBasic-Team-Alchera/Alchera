package com.alchera.game.Structure.Components.Camera;

import com.badlogic.gdx.math.Vector3;

public interface Interpolation {
    Vector3 Interpolate(Vector3 position, float destinationX, float destinationY, float destinationZ);
}
