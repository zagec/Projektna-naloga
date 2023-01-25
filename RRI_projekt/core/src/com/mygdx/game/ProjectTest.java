package com.mygdx.game;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static com.mongodb.client.model.Filters.eq;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import com.mygdx.game.utils.Geolocation;
import com.mygdx.game.utils.MapRasterTiles;
import com.mygdx.game.utils.Marker;
import com.mygdx.game.utils.PixelPosition;
import com.mygdx.game.utils.ZoomXY;
import com.mygdx.game.utils.config.Config;
import com.mygdx.game.utils.db.ConnectToDB;
import com.mygdx.game.utils.db.Restaurant;
import com.mygdx.game.utils.db.RestaurantRating;
import com.mygdx.game.utils.db.User;
import com.mygdx.game.utils.other.NumberFilter;

import org.bson.Document;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProjectTest extends ScreenAdapter implements GestureDetector.GestureListener {

    public static SpriteBatch batch;

    public BitmapFont font;
    public BitmapFont fontName;
    public BitmapFont fontGreeting;
    public BitmapFont font2;

    private Vector3 touchPosition;

    private TiledMap tiledMap;
    private TiledMapRenderer tiledMapRenderer;
    private OrthographicCamera camera;

    public static Texture[] mapTiles;
    public static Texture markerTexture;
    public static Texture markerTextureAdding;
    public static Texture markerTextureDelete;
    public static ZoomXY beginTile;   // top left tile

    private final RestaurantkoMap game;
    private Viewport viewport;
    private Stage stage;
    private Skin skin;

    public static final int NUM_TILES = 6;
    public static final int ZOOM = 15;
    public static final Geolocation CENTER_GEOLOCATION = new Geolocation(46.557314, 15.637771);
    private final int WIDTH = MapRasterTiles.TILE_SIZE * NUM_TILES;
    private final int HEIGHT = MapRasterTiles.TILE_SIZE * NUM_TILES;

    //database connection
    CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
    CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));
    private final ConnectToDB db =  new ConnectToDB();
    private final MongoCollection<Restaurant> restaurantCollection = db.database.getCollection("resturants", Restaurant.class).withCodecRegistry(pojoCodecRegistry);
    private final MongoCollection<User> userCollection = db.database.getCollection("users", User.class).withCodecRegistry(pojoCodecRegistry);
    private final MongoCollection<RestaurantRating> restaurantRatingCollection = db.database.getCollection("restaurantratings", RestaurantRating.class).withCodecRegistry(pojoCodecRegistry);
    Array<Marker> markerArr = new Array<>();
    Array<Restaurant> restaurants = new Array<>();
    Array<Restaurant> deletedRestaurants = new Array<>();
    int restaurantSize = 0;
    boolean firstRead = true;
    Array<User> users = new Array<>();
    Array<RestaurantRating> ratings = new Array<>();

    public static GlyphLayout layout = new GlyphLayout();
    Texture restInfo;
    boolean showRestaurantInfo = false;
    boolean addingMarker = false;
    boolean deleteMarkerActive = false;

    // display elements
    PixelPosition positionOfDisplay = new PixelPosition(0,0);
    String restaurantDisplayName = "null";
    String restaurantDisplayStreet = "null";
    String restaurantDisplayCenaBrezBona = "0";
    String restaurantDisplayCenaSBonom = "0";
    String restaurantDisplayRating = "0";
    String restaurantDisplayCoords = "0";

    boolean loginActive = false;
    String filter = "null";
    String filterValue = "3";
    float filterRating = 2.5f;
    String filterPrice= "max";
    Label ratingAmountDisplay;
    User loggedInAdmin = new User();


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
        batch = new SpriteBatch();
//        skin = new Skin();
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));


        font = new BitmapFont();
        font.getData().setScale(1.2f);
        font.setColor(Color.BLACK);

        font2 = new BitmapFont();
        font2.getData().setScale(1.2f);
        font2.setColor(Color.BLACK);

        fontName = new BitmapFont();
        fontName.getData().setScale(2f);
        fontName.setColor(Color.ORANGE);

        fontGreeting = new BitmapFont();
        fontGreeting.getData().setScale(3f);
        fontGreeting.setColor(Color.BLUE);


        camera = new OrthographicCamera();
        camera.setToOrtho(false, WIDTH, HEIGHT);
        camera.position.set(WIDTH / 2f, HEIGHT / 2f, 0);
        camera.viewportWidth = WIDTH / 2f;
        camera.viewportHeight = HEIGHT / 2f;
        camera.zoom = 2f;
        camera.update();

        touchPosition = new Vector3();
        stage = new Stage(viewport, game.getBatch());

//        try {
//            //in most cases, geolocation won't be in the center of the tile because tile borders are predetermined (geolocation can be at the corner of a tile)
//            ZoomXY centerTile = MapRasterTiles.getTileNumber(CENTER_GEOLOCATION.lat, CENTER_GEOLOCATION.lng, ZOOM);
//            mapTiles = MapRasterTiles.getRasterTileZone(centerTile, NUM_TILES);
//            //you need the beginning tile (tile on the top left corner) to convert geolocation to a location in pixels.
//            beginTile = new ZoomXY(ZOOM, centerTile.x - ((NUM_TILES - 1) / 2), centerTile.y - ((NUM_TILES - 1) / 2));
//            markerTexture = MapRasterTiles.getTextureMarker();
//            markerTextureAdding = MapRasterTiles.getTextureMarkerAdding();
//            markerTextureDelete = MapRasterTiles.getTextureMarkerDelete();
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

        setRatings();
        setMarkers(filter, filterValue);
        setUsers();

        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = fontName;
        textButtonStyle.fontColor = Color.WHITE;

        stage.addActor(filterButton());
        stage.addActor(loginBtn());

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(new GestureDetector(this));
        Gdx.input.setInputProcessor(multiplexer);

        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            @Override
            public void run()
            {
                if(!loggedInAdmin.getUsername().equals("null")) updateDatabase();
            }
        });
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

        for(Marker marker : markerArr){
            batch.draw(marker.getText(), marker.getPos().x, marker.getPos().y);
        }

        if(showRestaurantInfo){
            font.getData().setScale(1.8f * camera.zoom);
            fontName.getData().setScale(2.2f * camera.zoom);

            layout.setText(fontName, restaurantDisplayName.toUpperCase(Locale.ROOT));
            float widthName = layout.width;
            float heightName = layout.height;

            layout.setText(font, restaurantDisplayStreet);
            float widthStreet = layout.width;
            float heightStreet = layout.height;

            String stringCenaZBonom = "Cena z bonom: " + restaurantDisplayCenaSBonom + "e";
            layout.setText(font, stringCenaZBonom);
            float widthPrice1 = layout.width;
            float heightPrice1 = layout.height;

            String stringCenaBrezBona = "Cena brez bona: " + restaurantDisplayCenaBrezBona + "e";
            layout.setText(font, stringCenaBrezBona);
            float widthPrice2 = layout.width;
            float heightPrice2 = layout.height;

            String ranking = "Restaurant rating: " + restaurantDisplayRating;
            layout.setText(font, ranking);
            float widthRating = layout.width;
            float heightRating = layout.height;

            float width = Math.max(Math.max(Math.max(Math.max(widthName, widthStreet), widthPrice1), widthPrice2), widthRating);
            float height = 2f*heightName + heightStreet + heightPrice1 + heightPrice2 + heightRating;

            int tempX = positionOfDisplay.x;
            float zoomVal = camera.zoom / 2f;
            if(positionOfDisplay.x + width + 20 > (camera.position.x + (WIDTH * zoomVal /2))) tempX = (int) (positionOfDisplay.x - width);

            batch.draw(restInfo, tempX + 10, positionOfDisplay.y, width + 20, height + 30);
            fontName.draw(batch, restaurantDisplayName.toUpperCase(Locale.ROOT), tempX + 20, positionOfDisplay.y + 5f*heightName + 20);
            font.draw(batch, restaurantDisplayStreet, tempX + 20, positionOfDisplay.y + 4.9f*heightStreet + 10);
            font.draw(batch, stringCenaZBonom, tempX + 20, positionOfDisplay.y + 3.6f*heightPrice1 + 10);
            font.draw(batch, stringCenaBrezBona, tempX + 20, positionOfDisplay.y + 2.3f*heightPrice2 + 10);
            font.draw(batch, ranking, tempX + 20, positionOfDisplay.y + heightRating + 10);
        }

        if(!loggedInAdmin.getUsername().equals("null")){
            String greeting = "Hello admin " + loggedInAdmin.getUsername();
            layout.setText(fontGreeting, greeting);
            float widthGreeting = layout.width;
            float heightGreeting = layout.height;

            fontGreeting.draw(batch, greeting, WIDTH - widthGreeting - 20, HEIGHT - heightGreeting -150 );
        }

        batch.end();
    }
    
    private void setMarkers(String filter, String filterValue){
        markerArr.clear();
        restaurants.clear();

        FindIterable<Restaurant> docs = restaurantCollection.find();// seznam restavracij
        for(Restaurant doc : docs){
            if(filter.equals("null")){
                Geolocation MARKER_GEOLOCATION = new Geolocation(doc.getLoc().get(0), doc.getLoc().get(1));
                PixelPosition marker = MapRasterTiles.getPixelPosition(MARKER_GEOLOCATION.lat, MARKER_GEOLOCATION.lng, MapRasterTiles.TILE_SIZE, ZOOM, beginTile.x, beginTile.y, HEIGHT);
                markerArr.add(new Marker(marker, markerTexture));
                float rating = getRatingForRest(doc);
                restaurants.add(new Restaurant(doc, rating));

            }
            else if(filter.equals("price")){
                if(toFloat(doc.getCenaSStudentskimBonom()) < toFloat(filterValue)){
                    Geolocation MARKER_GEOLOCATION = new Geolocation(doc.getLoc().get(0), doc.getLoc().get(1));
                    PixelPosition marker = MapRasterTiles.getPixelPosition(MARKER_GEOLOCATION.lat, MARKER_GEOLOCATION.lng, MapRasterTiles.TILE_SIZE, ZOOM, beginTile.x, beginTile.y, HEIGHT);
                    markerArr.add(new Marker(marker, markerTexture));
                    float rating = getRatingForRest(doc);
                    restaurants.add(new Restaurant(doc, rating));
                }
            }
            else if(filter.equals("ranking")){
                Geolocation MARKER_GEOLOCATION = new Geolocation(doc.getLoc().get(0), doc.getLoc().get(1));
                PixelPosition marker = MapRasterTiles.getPixelPosition(MARKER_GEOLOCATION.lat, MARKER_GEOLOCATION.lng, MapRasterTiles.TILE_SIZE, ZOOM, beginTile.x, beginTile.y, HEIGHT);
                float rating = getRatingForRest(doc);
                if(rating > toFloat(filterValue)){
                    markerArr.add(new Marker(marker, markerTexture));
                    restaurants.add(new Restaurant(doc, rating));
                }
            }
            else{
                String[] parts = filterValue.split("_");
                String filterValue1 = parts[0];
                String filterValue2 = parts[1];
                if(toFloat(doc.getCenaSStudentskimBonom()) < toFloat(filterValue1)){
                    Geolocation MARKER_GEOLOCATION = new Geolocation(doc.getLoc().get(0), doc.getLoc().get(1));
                    PixelPosition marker = MapRasterTiles.getPixelPosition(MARKER_GEOLOCATION.lat, MARKER_GEOLOCATION.lng, MapRasterTiles.TILE_SIZE, ZOOM, beginTile.x, beginTile.y, HEIGHT);
                    float rating = getRatingForRest(doc);
                    if(rating > toFloat(filterValue2)){
                        markerArr.add(new Marker(marker, markerTexture));
                        restaurants.add(new Restaurant(doc, rating));
                    }
                }
            }
        }
        if(firstRead){
            restaurantSize = restaurants.size;
            firstRead = false;
        }
    }

    private String getStreet(List<Double> latLng) throws IOException {
        String urlString = String.format("https://maps.googleapis.com/maps/api/geocode/json?latlng=%s,%s&key=AIzaSyD8ZBqOfD5LH46rOa-E6kkWEGQKzc2v-lM", latLng.get(0), latLng.get(1));
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");
        InputStreamReader reader = new InputStreamReader(connection.getInputStream());
        BufferedReader rd = new BufferedReader(reader);

        JSONObject json = new JSONObject(new JSONTokener(reader));
//        JSONObject json = new JSONObject(new JSONTokener(streamReader));
        String streetName = json.getJSONArray("results").getJSONObject(0).getString("formatted_address");
        streetName = streetName.replace(" 2000", "");
        streetName = streetName.replace(", Slovenia", "");
        return streetName;
//    }
    }

    private void setUsers() {
        FindIterable<User> docs = userCollection.find();// seznam userjev
        for (User doc : docs){
            users.add(doc);
        }
    }

    private void setRatings(){
        FindIterable<RestaurantRating> docs = restaurantRatingCollection.find();// seznam ratingsov restavracij
        for (RestaurantRating doc : docs){
            if(!isRestaurantInArr(doc))
                ratings.add(doc);
        }
    }

    private boolean isRestaurantInArr(RestaurantRating rest) {
        for(RestaurantRating restRating : ratings){
            if(restRating.isSameRestaurant(rest)){
                float newRating = (restRating.getStarRating() + rest.getStarRating())/2f;
                newRating = (float) (Math.round(newRating * 10.0) / 10.0);
                restRating.setStarRating(newRating);
                return true;
            }
        }
        return false;
    }

    private float getRatingForRest(Restaurant rest){
        for(RestaurantRating rating : ratings){
            if(rating.getRestaurant_tk().equals(rest.getId())){
                return rating.getStarRating();
            }
        }
        return 0;
    }

    @Override
    public void dispose() {
        markerTexture.dispose();
        restInfo.dispose();
        batch.dispose();
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        touchPosition.set(x, y, 0);
        camera.unproject(touchPosition);
        return false;
    }

    public Restaurant isOnMarker(int x, int y, float zoomVal){
        float w = (markerArr.get(markerArr.size-1).getPos().x - 20) + markerTexture.getWidth();
        float h = (markerArr.get(markerArr.size-1).getPos().y + markerTexture.getHeight());
        float markerWidth = (markerTexture.getWidth());
        float markerHeight = (markerTexture.getHeight());
        for(int i = 0; i < markerArr.size; i++){
            if(x > (markerArr.get(i).getPos().x - 15) && x< (markerArr.get(i).getPos().x - 15) + markerWidth &&
                    y > markerArr.get(i).getPos().y && y< markerArr.get(i).getPos().y + markerHeight ){
                return restaurants.get(i);
            }
        }
        List<Double> l = new ArrayList<>();
        return new Restaurant(new ObjectId(), "null", "null", "0","0", l);
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        float procX = x/900;
        float procY = y/900;
        procY = 1 - procY;

        float zoomVal = camera.zoom / 2f;
        float screenSize = WIDTH * zoomVal;
        PixelPosition firstPX = new PixelPosition((int)(camera.position.x - (screenSize / 2)), (int)(camera.position.y - (screenSize / 2)));

        float worldX = procX * screenSize - 15 + firstPX.x;
        float worldY = procY *  screenSize + firstPX.y;
        PixelPosition marker = new PixelPosition((int)worldX,(int)worldY);

        Restaurant rest = isOnMarker((int)worldX, (int)worldY, camera.zoom);
        if(rest.getIme().equals("null")){
            showRestaurantInfo = false;
            if(addingMarker){
                removeActor("markerTable");
                addingMarker = false;
                markerArr.removeIndex(markerArr.size-1);
            }
            if(!loginActive && !loggedInAdmin.getUsername().equals("null")) stage.addActor(createMarkerEntryUI(marker));
        }
        else{
            removeActor("markerTable");

            showRestaurantInfo = true;
            restaurantDisplayName = rest.getIme();
            restaurantDisplayStreet = rest.getLokacija();
            restaurantDisplayCenaBrezBona = rest.getCenaBrezStudentskegaBona();
            restaurantDisplayCenaSBonom = rest.getCenaSStudentskimBonom();
            restaurantDisplayRating = (rest.getRating() != 0f) ? Float.toString(rest.getRating()) : "no ratings yet";
            restaurantDisplayCoords = rest.getLoc().get(0).toString() + ", " + rest.getLoc().get(1).toString();
            positionOfDisplay = marker;

            batch.setProjectionMatrix(camera.combined);
            batch.begin();
            font.draw(batch, rest.getIme(), worldX + 20, worldY + 10);
            batch.end();
        }

        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        if(!loggedInAdmin.getUsername().equals("null")){
            stage.addActor(deleteMarkerTable(x, y));
            removeActor("remove");
            return true;
        }
        return false;
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
        if(!loginActive && !deleteMarkerActive){
            if (Gdx.input.isKeyPressed(Input.Keys.E)) {
                camera.zoom += 0.02;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
                camera.zoom -= 0.02;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                camera.translate(-5, 0, 0);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                camera.translate(5, 0, 0);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                camera.translate(0, -5, 0);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                camera.translate(0, 5, 0);
            }
            if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
                Gdx.app.exit();
            }
        }
        camera.zoom = MathUtils.clamp(camera.zoom, 0.5f, 2f);

        float effectiveViewportWidth = camera.viewportWidth * camera.zoom;
        float effectiveViewportHeight = camera.viewportHeight * camera.zoom;

        camera.position.x = MathUtils.clamp(camera.position.x, effectiveViewportWidth / 2f, WIDTH - effectiveViewportWidth / 2f);
        camera.position.y = MathUtils.clamp(camera.position.y, effectiveViewportHeight / 2f, HEIGHT - effectiveViewportHeight / 2f);
    }

    private Float toFloat(String strFloat){
        strFloat = strFloat.replaceAll(",",".");
        return Float.parseFloat(strFloat);
    }

    private Actor filterButton() {
        TextButton filterBtn = new TextButton("Filter selection", skin);
        filterBtn.setName("filterBtn");
        filterBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(!loginActive) stage.addActor(filterUI());
            }
        });

        filterBtn.setPosition(5, viewport.getWorldHeight() - filterBtn.getHeight() - 5);

        return filterBtn;
    }

    private Actor loginBtn() {
        TextButton loginBtn = new TextButton("Admin login", skin);
        loginBtn.setName("adminLogin");

        loginBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stage.addActor(loginTable());
                loginActive = true;
            }
        });

        loginBtn.setPosition(viewport.getWorldWidth() - loginBtn.getWidth() - 5, viewport.getWorldHeight() - loginBtn.getHeight() - 5);

        return loginBtn;
    }

    private Actor logoutBtn() {
        TextButton logoutBtn = new TextButton("Admin logout", skin);
        logoutBtn.setName("adminLogout");

        logoutBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                loggedInAdmin = new User();
                removeActor("adminLogout");
                stage.addActor(loginBtn());
            }
        });

        logoutBtn.setPosition(viewport.getWorldWidth() - logoutBtn.getWidth() - 5, viewport.getWorldHeight() - logoutBtn.getHeight() - 5);

        return logoutBtn;
    }

    private void removeActor(String actor){
        for(Actor a : stage.getActors()){
            if(a.getName().equals(actor)){
                a.remove();
            }
        }
    }

    private Actor filterUI() {
        Table table = new Table();
        table.setName("filterTable");

        Texture texture = new Texture(Gdx.files.internal("gray_background.jpg"));
        TextureRegion backgroundRegion = new TextureRegion(texture);
        table.setBackground(new TextureRegionDrawable(backgroundRegion));

        // title
        Label showRestaurants = new Label("SHOW RESTAURANTS WITH: ", skin);
        showRestaurants.setColor(Color.ORANGE);
        showRestaurants.setFontScale(1.2f);

        TextButton close = new TextButton("x", skin);
        close.setColor(Color.RED);
        close.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                removeActor("filterTable");
            }
        });

        table.add(close).align(Align.right).row();
        table.add(showRestaurants).padBottom(10).row();

        // price filter
        final CheckBox checkboxPrice = new CheckBox("", skin);
        if(filter.equals("null") || filter.equals("ranking")) checkboxPrice.setChecked(false);
        else if(filter.equals("price") || filter.equals("price_ranking")) checkboxPrice.setChecked(true);

        Label price = new Label("Meal price less then:", skin);
        price.setWidth(50);
        final TextField priceField = new TextField("", skin);
        priceField.setText(filterPrice);
        priceField.setTextFieldFilter(new NumberFilter());

        layout.setText(font2, "Meal price less then: ");
        Table textTable = new Table();
        textTable.add(checkboxPrice).padRight(10);
        textTable.add(price).width(layout.width).left().padBottom(10);
        textTable.add(priceField).width(50).padBottom(10).row();
        table.add(textTable).colspan(2).row();

        // rating filter
        final CheckBox checkboxRanking = new CheckBox("", skin);
        if(filter.equals("null") || filter.equals("price")) checkboxRanking.setChecked(false);
        else if(filter.equals("ranking") || filter.equals("price_ranking")) checkboxRanking.setChecked(true);

        Label rating = new Label("Rating more then:", skin);
        rating.setWidth(50);
        ratingAmountDisplay = new Label(String.valueOf(filterRating), skin);
        Button plus = new Button(skin, "default");
        Button minus = new Button(skin, "default2");

        plus.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(filterRating < 5) {
                    filterRating += 0.5;
                    ratingAmountDisplay.setText(String.valueOf(filterRating));
                }
            }
        });

        minus.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(filterRating > 0.5){
                    filterRating -= 0.5;
                    ratingAmountDisplay.setText(String.valueOf(filterRating));
                }
            }
        });

//         buttons
        Table moneyTable = new Table();
        moneyTable.add(minus);
        moneyTable.add(ratingAmountDisplay);
        moneyTable.add(plus);
        moneyTable.center();

        layout.setText(font2, "Rating more then: ");
        Table textTable2 = new Table();
        textTable2.add(checkboxRanking).padRight(10);
        textTable2.add(rating).width(layout.width).left().padBottom(10);
        textTable2.add(moneyTable).width(50).padBottom(10).row();
        table.add(textTable2).colspan(2).row();

        TextButton saveBtn = new TextButton("Find", skin);
        saveBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                boolean price = checkboxPrice.isChecked();
                boolean ranking = checkboxRanking.isChecked();
                if(!price && !ranking){
                    filter = "null";
                    filterPrice = correctFormPrice(priceField.getText());
                    setMarkers(filter, "0");
                }
                else if(price && !ranking){
                    String str = correctFormPrice(priceField.getText());
                    filterPrice = correctFormPrice(priceField.getText());
                    if(str.equals("20")) priceField.setText("max");
                    else priceField.setText(str);
                    filter = "price";
                    setMarkers(filter, str);
                }
                else if(!price && ranking){
                    filter = "ranking";
                    setMarkers(filter, Float.toString(filterRating));
                    filterPrice = correctFormPrice(priceField.getText());
                }
                else{
                    String str = correctFormPrice(priceField.getText());
                    filterPrice = correctFormPrice(priceField.getText());
                    if(str.equals("20")) priceField.setText("max");
                    else priceField.setText(str);
                    String str2 = Float.toString(filterRating);
                    filter = "price_ranking";
                    setMarkers(filter, str+"_"+str2);
                }
            }
        });

        TextButton clearBtn = new TextButton("Clear", skin);
        clearBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                priceField.setText("max price");
                checkboxPrice.setChecked(false);
                checkboxRanking.setChecked(false);
                setMarkers("null", "null");
                filterPrice = "max_price";
            }
        });

        Table buttonTable = new Table();
        buttonTable.add(saveBtn);
        buttonTable.add(clearBtn);
        buttonTable.center();

        table.add(buttonTable).colspan(2);

        table.pack();
        table.setPosition(5, viewport.getWorldHeight() - table.getHeight());
        return table;
    }

    private String correctFormPrice(String str){
        if(str.equals("max")) {
            return "20";
        }
        if(str.contains(","))
            str = str.replaceAll(",", ".");

        int num = 0, pos = 0;
        for(int i = 0; i<str.length(); i++){
            if(str.charAt(i) == '.') {
                num++;
                pos = i;
                if(num >= 2){
                    str = str.substring(0, pos);
                    break;
                }
            }
        }

        if(str.equals("")) {
            str = "20";
            return "20";
        }
        else
            return str;
    }

    private Actor createMarkerEntryUI(final PixelPosition markerInp){
        addingMarker = true;
        Table table = new Table();
        table.setName("markerTable");
        markerArr.add(new Marker(markerInp, markerTextureAdding));

        Texture texture = new Texture(Gdx.files.internal("gray_background.jpg"));
        TextureRegion backgroundRegion = new TextureRegion(texture);
        table.setBackground(new TextureRegionDrawable(backgroundRegion));

        Label addMarker = new Label("Add restaurant info", skin);
        addMarker.setColor(Color.ORANGE);
        addMarker.setFontScale(1.2f);

        Label name = new Label("restaurant name:", skin);
        final TextField nameField = new TextField("", skin);

        final Label priceWithBon = new Label("Price with bon:", skin);
        final TextField priceWithBonField = new TextField("", skin);
        priceWithBonField.setTextFieldFilter(new NumberFilter());

        Label priceWithoutBon = new Label("Price without bon:", skin);
        final TextField priceWithoutBonField = new TextField("", skin);
        priceWithoutBonField.setTextFieldFilter(new NumberFilter());

        TextButton close = new TextButton("x", skin);
        Label labelX = new Label("X", skin);
        labelX.setFontScale(1.2f);
        close.setColor(Color.RED);
        close.setLabel(labelX);
        close.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                markerArr.removeIndex(markerArr.size - 1);
                addingMarker = false;
                removeActor("markerTable");
            }
        });

        table.add(close).colspan(6).align(Align.right).row();
        table.add(addMarker).padBottom(10).row();
        table.add(name);
        table.add(nameField);

        table.add(priceWithBon);
        table.add(priceWithBonField);

        table.add(priceWithoutBon);
        table.add(priceWithoutBonField).row();

        final TextButton saveBtn = new TextButton("Save", skin);
        saveBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String name = nameField.getText();
                String priceBon = priceWithBonField.getText();
                String priceNoBon = priceWithoutBonField.getText();
                if(!name.equals("") && !priceBon.equals("") && !priceNoBon.equals("")){
                    double[] geo = MapRasterTiles.getGeolocation(markerInp, MapRasterTiles.TILE_SIZE, ZOOM, beginTile.x, beginTile.y, HEIGHT);
                    markerArr.removeIndex(markerArr.size - 1);
                    markerArr.add(new Marker(markerInp, markerTexture));

                    List<Double> l = new ArrayList<Double>();
                    l.add(geo[0]);
                    l.add(geo[1]);
                    try {
                        restaurants.add(new Restaurant(new ObjectId(), name, getStreet(l), priceBon,priceNoBon, l));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    addingMarker = false;
                    removeActor("markerTable");
                }
            }
        });

        Table buttonTable = new Table();
        buttonTable.add(saveBtn);
        buttonTable.center();

        table.add(buttonTable).padTop(10).colspan(6);
        table.pack();
        table.setPosition(0, 0);
        return table;
    }

    private Actor loginTable() {
        Table table = new Table();
        table.setName("loginTable");

        Texture texture = new Texture(Gdx.files.internal("gray_background.jpg"));
        TextureRegion backgroundRegion = new TextureRegion(texture);
        table.setBackground(new TextureRegionDrawable(backgroundRegion));

        Label label = new Label("Login", skin);
        label.setColor(Color.ORANGE);
        label.setFontScale(1.2f);

        Label username = new Label("Username: ", skin);
        final TextField usernameField = new TextField("", skin);

        final Label password = new Label("password: ", skin);
        final TextField passwordField = new TextField("", skin);

        table.add(label).colspan(2).padBottom(10).row();

        table.add(username).padBottom(10);
        table.add(usernameField).padBottom(10).row();

        table.add(password).padBottom(10);
        table.add(passwordField).padBottom(10).row();

        TextButton saveBtn = new TextButton("Save", skin);
        saveBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String username = usernameField.getText();
                String password = passwordField.getText();

                // na doben nacin nisem mogel importat, ni delovalo
//                BCryptPasswordEncoder passwordEncoder  = new BCryptPasswordEncoder();
//
//                for(User usr : users){
//                    if(usr.getPassword().equals(username)){
//                        if (passwordEncoder.matches(usr.getPassword(), password)) {
////                            loggedInAdmin = usr;
//                        }
//                    }
//
//                }
                for(User usr : users){
                    if(usr.getUsername().equals(username)){
                        loggedInAdmin = usr;
                        stage.addActor(logoutBtn());
                        removeActor("adminLogin");
                    }
                }
                removeActor("loginTable");
                loginActive = false;
            }
        });

        TextButton backBtn = new TextButton("Back", skin);
        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                removeActor("loginTable");
                loginActive = false;
            }
        });

        Table buttonTable = new Table();
        buttonTable.add(saveBtn);
        buttonTable.add(backBtn);
        buttonTable.center();

        table.add(buttonTable).padTop(10).colspan(6);
        table.center();
        table.pack();

        table.setPosition(viewport.getWorldWidth() / 2f - table.getWidth()/2f, viewport.getWorldHeight() / 2f - table.getHeight()/2f);
        return table;
    }

    private Actor deleteMarkerTable(final float posX, final float posY) {
        Table table = new Table();
        table.setName("deleteMarkerTable");

        Marker delete = null;
        float procX = posX/900;
        float procY = posY/900;
        procY = 1 - procY;
        float zoomVal = camera.zoom / 2f;
        float screenSize = WIDTH * zoomVal;
        PixelPosition firstPX = new PixelPosition((int)(camera.position.x - (screenSize / 2)), (int)(camera.position.y - (screenSize / 2)));

        int markerIndex = 0;
        float worldX = procX * screenSize - 15 + firstPX.x;
        float worldY = procY *  screenSize + firstPX.y;
        for(int i=0; i<markerArr.size; i++){
            if(worldY < markerTexture.getHeight() + markerArr.get(i).getPos().y  && worldY > markerArr.get(i).getPos().y){
                if (worldX < markerTexture.getWidth() + markerArr.get(i).getPos().x &&  worldX > markerArr.get(i).getPos().x){
                    markerArr.get(i).setText(markerTextureDelete);
                    delete = markerArr.get(i);
                    markerIndex = i;
                }
            }
        }
        
        final Restaurant rest = isOnMarker((int)worldX, (int)worldY, camera.zoom);
        if(rest.getIme().equals("null")) {
            table.setName("remove");
            return table;
        }

        Texture texture = new Texture(Gdx.files.internal("gray_background.jpg"));
        TextureRegion backgroundRegion = new TextureRegion(texture);
        table.setBackground(new TextureRegionDrawable(backgroundRegion));

        Label label = new Label("Brisanje markerja", skin);
        label.setColor(Color.ORANGE);
        label.setFontScale(1.2f);

        Label text = new Label("Zelis zares izbrisati marker za restavracijo " + rest.getIme() + "?", skin);

        table.add(label).colspan(2).padBottom(10).row();
        table.add(text).row();

        TextButton saveBtn = new TextButton("Ja", skin);
        final Marker finalDelete = delete;
        saveBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                markerArr.removeValue(finalDelete,false);
                deletedRestaurants.add(rest);
                removeActor("deleteMarkerTable");
                deleteMarkerActive = false;
            }
        });

        TextButton backBtn = new TextButton("Ne", skin);
        final int finalMarkerIndex = markerIndex;
        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                removeActor("deleteMarkerTable");
                markerArr.get(finalMarkerIndex).setText(markerTexture);
                deleteMarkerActive = false;
            }
        });

        Table buttonTable = new Table();
        buttonTable.add(saveBtn);
        buttonTable.add(backBtn);
        buttonTable.center();

        table.add(buttonTable).padTop(10).colspan(6);
        table.center();
        table.pack();

        table.setPosition(viewport.getWorldWidth() / 2f - table.getWidth()/2f, viewport.getWorldHeight() / 2f - table.getHeight()/2f);
        return table;
    }

    public void updateDatabase(){
        for(int i=restaurantSize; i<restaurants.size; i++){
            restaurantCollection.insertOne(restaurants.get(i));
        }

        for(Restaurant rest : deletedRestaurants) {
            Bson query = eq("_id", rest.getId());
            restaurantCollection.deleteOne(query);
        }

    }
}
