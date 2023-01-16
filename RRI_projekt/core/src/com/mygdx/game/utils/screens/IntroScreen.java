package com.mygdx.game.utils.screens;

import static com.mygdx.game.ProjectTest.CENTER_GEOLOCATION;
import static com.mygdx.game.ProjectTest.NUM_TILES;
import static com.mygdx.game.ProjectTest.ZOOM;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.ProjectTest;
import com.mygdx.game.RestaurantkoMap;
import com.mygdx.game.utils.Letter;
import com.mygdx.game.utils.MapRasterTiles;
import com.mygdx.game.utils.ZoomXY;
import com.mygdx.game.utils.config.Config;
import com.sun.tools.javac.util.Pair;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IntroScreen extends ScreenAdapter {
    public static final float INTRO_DURATION_IN_SEC = 5.1f;

    private final RestaurantkoMap game;
    private final AssetManager assetManager;
    float rPosX;

    Texture R, e, s, t, a, v, r, n, k, o, restIcon;

    private Viewport viewport;
    private Texture[] textures = new Texture[11];
    private List<Pair<Actor, Letter>> pairs = new ArrayList<>();

    private float duration = 0f;

    private Stage stage;

    public IntroScreen(RestaurantkoMap game) {
        this.game = game;
        assetManager = game.getAssetManager();
    }

    @Override
    public void show() {
        viewport = new FitViewport(Config.HUD_WIDTH, Config.HUD_HEIGHT);
        stage = new Stage(viewport, game.getBatch());

        textures[0] = new Texture("r.png");
        textures[1] = new Texture("e.png");
        textures[2] = new Texture("s.png");
        textures[3] = new Texture("t.png");
        textures[4] = new Texture("a.png");
        textures[5] = new Texture("v.png");
        textures[6] = new Texture("r_small.png");
        textures[7] = new Texture("a.png");
        textures[8] = new Texture("n.png");
        textures[9] = new Texture("t.png");
        textures[10] = new Texture("k.png");
//        textures[11] = new Texture("o.png");

        float totalW = 0;
        for(int i=0; i<textures.length; i++)
            totalW += textures[i].getWidth() + 10 - 30;

        for(int i=0; i<textures.length; i++){
            if(i == 0)
                pairs.add(createLetterAnimation(textures[i], (viewport.getWorldWidth() / 2f) - totalW/2 - 20, 0, i));
            else
                pairs.add(createLetterAnimation(textures[i], pairs.get(i-1).snd.posX, pairs.get(i-1).snd.width, i));
        }

        stage.addActor(createBack());
        for(Pair<Actor, Letter> pair : pairs){
            stage.addActor(pair.fst);
        }
        stage.addActor(createIconAnimation((viewport.getWorldWidth() / 2f) - totalW/2 - 20, pairs.get(0).snd.width));

        try {
            ZoomXY centerTile = MapRasterTiles.getTileNumber(CENTER_GEOLOCATION.lat, CENTER_GEOLOCATION.lng, ZOOM);
            ProjectTest.mapTiles = MapRasterTiles.getRasterTileZone(centerTile, NUM_TILES);
            ProjectTest.beginTile = new ZoomXY(ZOOM, centerTile.x - ((NUM_TILES - 1) / 2), centerTile.y - ((NUM_TILES - 1) / 2));

            ProjectTest.markerTexture = MapRasterTiles.getTextureMarker();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(65 / 255f, 159 / 255f, 221 / 255f, 0f);

        duration += delta;

        if (duration > INTRO_DURATION_IN_SEC) {
            game.setScreen(new ProjectTest(game));
        }

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    private Actor createBack() {
        Image background = new Image(new Texture("mapImg.png"));

        background.setHeight(viewport.getWorldHeight());
        background.setWidth(viewport.getWorldWidth());
        return background;
    }

    private Pair<Actor, Letter> createLetterAnimation(Texture txt, float prevX, float prevW, int delay) {
        Image letter = new Image(txt);
        if(delay == 3 || delay == 5 || delay == 9 || delay == 6 || delay == 10)
            letter.setWidth(letter.getWidth() -25);
        else
            letter.setWidth(letter.getWidth() -30);

        letter.setHeight(letter.getHeight() - 30);

        float posX = prevX + prevW + 10;
        float posY = (viewport.getWorldHeight() / 2f);
        letter.setPosition(posX, posY);

        float finalMoveY = (pairs.size() > 1) ? pairs.get(0).snd.height : letter.getHeight();

        System.out.println(delay/10f);
        letter.addAction(
                Actions.sequence(
                        Actions.parallel(Actions.moveTo(posX, posY, delay/8f + 1.6f)),
                        Actions.parallel(Actions.moveTo(posX, posY + 40,   0.5f)),
                        Actions.parallel(Actions.moveTo(posX, posY, 0.25f)),
                        Actions.parallel(Actions.moveTo(posX, posY, (textures.length - delay)/8f + 0.5f)),
//                        Actions.moveTo(posX,  0 - finalMoveY, 1.0f)
                        Actions.fadeOut(1f)
                )
        );
        return new Pair<Actor, Letter>(letter, new Letter(posX, posY, letter.getWidth(), letter.getHeight()));
    }

    private Actor createIconAnimation(float prevX, float prevW) {
        Image icon = new Image(new Texture("restIcon.png"));
        float icW = icon.getWidth();
        float icH = icon.getHeight();

        icon.setWidth(prevW + 30);
        icon.setHeight((prevW * (icW/icH))+ 30);

        float posX = prevX + prevW + 10;
        float posY = (viewport.getWorldHeight() / 2f);
        icon.setPosition(posX, viewport.getWorldHeight());

        icon.setOrigin(Align.center);
        icon.addAction(
                Actions.sequence(
                        Actions.parallel(Actions.moveTo(posX, viewport.getWorldHeight(), 0.5f)),
                        Actions.parallel(Actions.moveTo(posX, pairs.get(1).snd.posY + pairs.get(1).snd.height + 10,0.8f), Actions.rotateBy(2 * 360, 0.8f)),
                        Actions.parallel(Actions.moveTo(posX, pairs.get(1).snd.posY + pairs.get(1).snd.height + 10,0.3f)),
                        Actions.parallel(Actions.moveTo(pairs.get(2).snd.posX, pairs.get(2).snd.posY + pairs.get(2).snd.height + 10,1/7f)),
                        Actions.parallel(Actions.moveTo(pairs.get(3).snd.posX, pairs.get(3).snd.posY + pairs.get(3).snd.height + 10,1/7f)),
                        Actions.parallel(Actions.moveTo(pairs.get(4).snd.posX, pairs.get(4).snd.posY + pairs.get(4).snd.height + 10,1/7f)),
                        Actions.parallel(Actions.moveTo(pairs.get(5).snd.posX, pairs.get(5).snd.posY + pairs.get(5).snd.height + 10,1/7f)),
                        Actions.parallel(Actions.moveTo(pairs.get(6).snd.posX, pairs.get(6).snd.posY + pairs.get(6).snd.height + 10,1/7f)),
                        Actions.parallel(Actions.moveTo(pairs.get(7).snd.posX, pairs.get(7).snd.posY + pairs.get(7).snd.height + 10,1/7f)),
                        Actions.parallel(Actions.moveTo(pairs.get(8).snd.posX, pairs.get(8).snd.posY + pairs.get(8).snd.height + 10,1/7f)),
                        Actions.parallel(Actions.moveTo(pairs.get(9).snd.posX, pairs.get(9).snd.posY + pairs.get(9).snd.height + 10,1/7f)),
                        Actions.parallel(Actions.moveTo(pairs.get(10).snd.posX, pairs.get(10).snd.posY + pairs.get(10).snd.height + 10,1/7f)),
//                        Actions.parallel(Actions.moveTo(pairs.get(11).snd.posX, pairs.get(11).snd.posY + pairs.get(11).snd.height + 10,1/7f)),
//                        Actions.parallel(Actions.moveTo(pairs.get(11).snd.posX + pairs.get(11).snd.width - 20, pairs.get(11).snd.posY + pairs.get(11).snd.height + 60,0.3f), Actions.rotateBy(360 * 0.3f, 0.3f)),
//                        Actions.parallel(Actions.moveTo(pairs.get(11).snd.posX + pairs.get(11).snd.width + 10, pairs.get(11).snd.posY,0.7f), Actions.rotateBy(360 * 0.7f, 0.7f)),
                        Actions.parallel(Actions.moveTo(pairs.get(10).snd.posX + pairs.get(10).snd.width - 20, pairs.get(10).snd.posY + pairs.get(10).snd.height + 60,0.3f), Actions.rotateBy(360 * 0.3f, 0.3f)),
                        Actions.parallel(Actions.moveTo(pairs.get(10).snd.posX + pairs.get(10).snd.width - 5, pairs.get(10).snd.posY - 30,0.7f), Actions.rotateBy(360 * 0.7f, 0.7f)),
                        Actions.parallel(Actions.moveTo(pairs.get(10).snd.posX + pairs.get(10).snd.width - 5, pairs.get(10).snd.posY - 30,0.3f)),
//                        Actions.moveTo(pairs.get(11).snd.posX + pairs.get(11).snd.width + 10,  0 - pairs.get(0).snd.height, 1.0f)
                        Actions.fadeOut(1f)
                )
        );
        return icon;
    }
}