package com.mygdx.game.utils;

import com.badlogic.gdx.graphics.Texture;

public class Marker {
    PixelPosition pos;
    Texture text;

    public Marker(PixelPosition pos,Texture text) {
        this.pos = pos;
        this.text = text;
    }

    public Texture getText() {
        return this.text;
    }

    public void setText(Texture text) { this.text = text; }

    public PixelPosition getPos() { return pos; }
}
