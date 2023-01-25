package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.utils.screens.IntroScreen;


public class RestaurantkoMap extends Game {
    private AssetManager assetManager;
    SpriteBatch batch;
    Texture img;

    @Override
    public void create () {
        batch = new SpriteBatch();

        assetManager = new AssetManager();
        assetManager.getLogger().setLevel(Logger.DEBUG);

        setScreen(new IntroScreen(this));
//        setScreen(new ProjectTest(this));
    }

    @Override
    public void dispose () {
        batch.dispose();
        img.dispose();
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

}
