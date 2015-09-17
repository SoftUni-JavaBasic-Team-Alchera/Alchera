package com.alchera.game.Structure.Components.Overlays;

/**
 * Created by Inspix on 14/09/2015.
 */
public interface Transition {
    boolean transitionIn(float delta);
    boolean transitionOut(float delta);
}
