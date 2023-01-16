package com.mygdx.game;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static com.mongodb.client.model.Filters.eq;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision._btMprSupport_t;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mygdx.game.utils.Geolocation;
import com.mygdx.game.utils.MapRasterTiles;
import com.mygdx.game.utils.PixelPosition;
import com.mygdx.game.utils.ZoomXY;
import com.mygdx.game.utils.config.Config;
import com.mygdx.game.utils.db.ConnectToDB;
import com.mygdx.game.utils.db.Restaurant;

import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jdk.internal.net.http.common.Pair;

public class ProjectTest extends ScreenAdapter implements GestureDetector.GestureListener {

    private ShapeRenderer shapeRenderer;
    public static SpriteBatch batch;

    public BitmapFont font;

    private Vector3 touchPosition;

    private TiledMap tiledMap;
    private TiledMapRenderer tiledMapRenderer;
    private OrthographicCamera camera;

    public static Texture[] mapTiles;
    public static Texture markerTexture;
    public static ZoomXY beginTile;   // top left tile

    private final RestaurantkoMap game;
    private Viewport viewport;

    public static final int NUM_TILES = 6;
    public static final int ZOOM = 15;
    public static final Geolocation CENTER_GEOLOCATION = new Geolocation(46.557314, 15.637771);
    private final int WIDTH = MapRasterTiles.TILE_SIZE * NUM_TILES;
    private final int HEIGHT = MapRasterTiles.TILE_SIZE * NUM_TILES;
    //databse connection
    CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
    CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));
    private final ConnectToDB db =  new ConnectToDB();
    private final MongoCollection<Restaurant> collection = db.database.getCollection("resturants", Restaurant.class).withCodecRegistry(pojoCodecRegistry);;
    Array<PixelPosition> markerArr = new Array<>();
    Array<Restaurant>  restaurants = new Array<>();

    public static GlyphLayout layout = new GlyphLayout();
    Texture restInfo;
    boolean showRestaurantInfo = false;
    boolean addingMarker = false;
    PixelPosition positionOfDisplay = new PixelPosition(0,0);
    String restaurantDisplayName = "null";
    String restaurantDisplayStreet = "null";

    private Stage stage;

    public ProjectTest(RestaurantkoMap game) {
        this.game = game;
    }
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }


    @Override
    public void show() {
        viewport = new FitViewport(Config.HUD_WIDTH, Config.HUD_HEIGHT);

        restInfo = new Texture("gray_background.jpg");
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();

        font = new BitmapFont();
        font.getData().setScale(1.5f);
        font.setColor(Color.BLACK);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, WIDTH, HEIGHT);
        camera.position.set(WIDTH / 2f, HEIGHT / 2f, 0);
        camera.viewportWidth = WIDTH / 2f;
        camera.viewportHeight = HEIGHT / 2f;
        camera.zoom = 2f;
        camera.update();

        touchPosition = new Vector3();
        Gdx.input.setInputProcessor(new GestureDetector(this));

        stage = new Stage(viewport, game.getBatch());

//        try {
//            //in most cases, geolocation won't be in the center of the tile because tile borders are predetermined (geolocation can be at the corner of a tile)
//            ZoomXY centerTile = MapRasterTiles.getTileNumber(CENTER_GEOLOCATION.lat, CENTER_GEOLOCATION.lng, ZOOM);
//            mapTiles = MapRasterTiles.getRasterTileZone(centerTile, NUM_TILES);
//            //you need the beginning tile (tile on the top left corner) to convert geolocation to a location in pixels.
//            beginTile = new ZoomXY(ZOOM, centerTile.x - ((NUM_TILES - 1) / 2), centerTile.y - ((NUM_TILES - 1) / 2));
//
//            markerTexture = MapRasterTiles.getTextureMarker();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        tiledMap = new TiledMap();
        MapLayers layers = tiledMap.getLayers();

        TiledMapTileLayer layer = new TiledMapTileLayer(NUM_TILES, NUM_TILES, MapRasterTiles.TILE_SIZE, MapRasterTiles.TILE_SIZE);
        int index = 0;
        for (int j = NUM_TILES - 1; j >= 0; j--) {
            for (int i = 0; i < NUM_TILES; i++) {
                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                cell.setTile(new StaticTiledMapTile(new TextureRegion(mapTiles[index], MapRasterTiles.TILE_SIZE, MapRasterTiles.TILE_SIZE)));
                layer.setCell(i, j, cell);
                index++;
            }
        }
        layers.add(layer);
        setMarkers();
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        handleInput();

        camera.update();

        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        drawMarkers();

        stage.act(delta);
        stage.draw();
    }

    private void drawMarkers() {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();


        for(PixelPosition marker : markerArr){
            batch.draw(markerTexture, marker.x, marker.y);
        }

        if(showRestaurantInfo){
            layout.setText(font, restaurantDisplayName);
            float widthName = layout.width;
            float heightName = layout.height;

            layout.setText(font, restaurantDisplayStreet);
            float widthStreet = layout.width;
            float heightStreet = layout.height;

            float width = Math.max(widthName, widthStreet);
            float height = heightName + heightStreet;

            batch.draw(restInfo, positionOfDisplay.x + 10, positionOfDisplay.y, width + 20, height + 30);
            font.draw(batch, restaurantDisplayName, positionOfDisplay.x + 20, positionOfDisplay.y + 2*heightName + 20);
            font.draw(batch, restaurantDisplayStreet, positionOfDisplay.x + 20, positionOfDisplay.y + heightStreet + 10);
        }
        else if(addingMarker){

        }

        batch.end();
    }
    private void setMarkers(){
        FindIterable<Restaurant> docs = collection.find();// seznam restavracij
        for(Restaurant doc : docs){
            restaurants.add(doc);
            Geolocation MARKER_GEOLOCATION = new Geolocation(doc.getLoc().get(0), doc.getLoc().get(1));
            PixelPosition marker = MapRasterTiles.getPixelPosition(MARKER_GEOLOCATION.lat, MARKER_GEOLOCATION.lng, MapRasterTiles.TILE_SIZE, ZOOM, beginTile.x, beginTile.y, HEIGHT);
            markerArr.add(marker);
        }
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        batch.dispose();
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        touchPosition.set(x, y, 0);
        camera.unproject(touchPosition);
        return false;
    }

    public Restaurant isOnMarker(int x, int y){
        for(int i = 0; i < markerArr.size; i++){
            if(x > (markerArr.get(i).x - 20) && x< (markerArr.get(i).x - 20) + markerTexture.getWidth() &&
                    y > markerArr.get(i).y && y< markerArr.get(i).y + markerTexture.getHeight() ){
                return restaurants.get(i);
            }
        }
        List<Double> l = new ArrayList<>();
        return new Restaurant("null", false, "null", l);
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        float procX = x/900;
        float procY = y/900;
        procY = 1 - procY;
        float worldX = procX * WIDTH - 15;
        float worldY = procY * HEIGHT;
        PixelPosition marker = new PixelPosition((int)worldX,(int)worldY);

        Restaurant rest = isOnMarker((int)worldX, (int)worldY);
        if(rest.getIme().equals("null")){
            showRestaurantInfo = false;
            markerArr.add(marker);
            List<Double> l = new ArrayList<>();
            restaurants.add(new Restaurant("null", false, "null", l));
        }
        else{
            showRestaurantInfo = true;
            restaurantDisplayName = rest.getIme();
            restaurantDisplayName = restaurantDisplayName.replaceAll("š","s");
            restaurantDisplayName = restaurantDisplayName.replaceAll("č","c");
            restaurantDisplayName = restaurantDisplayName.replaceAll("ž","z");

            restaurantDisplayStreet = rest.getLokacija();
            restaurantDisplayStreet = restaurantDisplayStreet.replaceAll("š","s");
            restaurantDisplayStreet = restaurantDisplayStreet.replaceAll("č","c");
            restaurantDisplayStreet = restaurantDisplayStreet.replaceAll("ž","z");
            positionOfDisplay = marker;
            batch.setProjectionMatrix(camera.combined);
            batch.begin();
            font.draw(batch, rest.getIme(), worldX + 20, worldY + 10);
            batch.end();
        }

        return true;
    }

    @Override
    public boolean longPress(float x, float y) {
        float procX = x/900;
        float procy = y/900;
        procy = 1 - procy;
        float woroldX = procX * WIDTH - 15;
        float woroldY = procy * HEIGHT;
        for(PixelPosition marker :markerArr){
            if(woroldY < markerTexture.getHeight() + marker.y  && woroldY > marker.y){
                if (woroldX < markerTexture.getWidth() + marker.x &&  woroldX > marker.x){
                    markerArr.removeValue(marker,false);
                }
            }
        }
        return true;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        camera.translate(-deltaX, deltaY);
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        if (initialDistance >= distance)
            camera.zoom += 0.02;
        else
            camera.zoom -= 0.02;
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }

    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            camera.zoom += 0.02;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            camera.zoom -= 0.02;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            camera.translate(-3, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            camera.translate(3, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            camera.translate(0, -3, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            camera.translate(0, 3, 0);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) Gdx.app.exit();
        camera.zoom = MathUtils.clamp(camera.zoom, 0.5f, 2f);

        float effectiveViewportWidth = camera.viewportWidth * camera.zoom;
        float effectiveViewportHeight = camera.viewportHeight * camera.zoom;

        camera.position.x = MathUtils.clamp(camera.position.x, effectiveViewportWidth / 2f, WIDTH - effectiveViewportWidth / 2f);
        camera.position.y = MathUtils.clamp(camera.position.y, effectiveViewportHeight / 2f, HEIGHT - effectiveViewportHeight / 2f);
//        camera.position.x = effectiveViewportWidth;
//        camera.position.y = effectiveViewportHeight;
    }

}
