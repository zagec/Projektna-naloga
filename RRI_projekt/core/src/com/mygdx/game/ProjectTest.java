package com.mygdx.game;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.or;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import com.badlogic.gdx.ApplicationAdapter;
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
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mygdx.game.utils.Geolocation;
import com.mygdx.game.utils.MapRasterTiles;
import com.mygdx.game.utils.PixelPosition;
import com.mygdx.game.utils.User;
import com.mygdx.game.utils.ZoomXY;
import com.mygdx.game.utils.config.Config;
import com.mygdx.game.utils.db.ConnectToDB;
import com.mygdx.game.utils.db.Restaurant;
import com.mygdx.game.utils.other.NumberFilter;

import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import jdk.internal.net.http.common.Pair;

public class ProjectTest extends ScreenAdapter implements GestureDetector.GestureListener {

    public static SpriteBatch batch;

    public BitmapFont font;
    public BitmapFont fontName;
    public BitmapFont fontGreeting;

    private Vector3 touchPosition;

    private TiledMap tiledMap;
    private TiledMapRenderer tiledMapRenderer;
    private OrthographicCamera camera;

    public static Texture[] mapTiles;
    public static Texture markerTexture;
    public static ZoomXY beginTile;   // top left tile

    private final RestaurantkoMap game;
    private Viewport viewport;
    private Stage stage;
    private Skin skin;

    public static final int NUM_TILES = 4;
    public static final int ZOOM = 15;
    public static final Geolocation CENTER_GEOLOCATION = new Geolocation(46.557314, 15.637771);
    private final int WIDTH = MapRasterTiles.TILE_SIZE * NUM_TILES;
    private final int HEIGHT = MapRasterTiles.TILE_SIZE * NUM_TILES;

    //databse connection
    CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
    CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));
    private final ConnectToDB db =  new ConnectToDB();
    private final MongoCollection<Restaurant> collection = db.database.getCollection("resturants", Restaurant.class).withCodecRegistry(pojoCodecRegistry);
    private final MongoCollection<User> userCollection = db.database.getCollection("users", User.class).withCodecRegistry(pojoCodecRegistry);
    Array<PixelPosition> markerArr = new Array<>();
    Array<Restaurant> restaurants = new Array<>();
    Array<User> users = new Array<>();

    public static GlyphLayout layout = new GlyphLayout();
    Texture restInfo;
    boolean showRestaurantInfo = false;
    boolean addingMarker = false;

    // display elements
    PixelPosition positionOfDisplay = new PixelPosition(0,0);
    String restaurantDisplayName = "null";
    String restaurantDisplayStreet = "null";
    String restaurantDisplayCenaBrezBona = "0";
    String restaurantDisplayCenaSBonom = "0";

    boolean loginActive = false;
    String filter = "null";
    String filterValue = "3";
    PixelPosition markerToAdd = new PixelPosition(0,0);

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

        try {
            //in most cases, geolocation won't be in the center of the tile because tile borders are predetermined (geolocation can be at the corner of a tile)
            ZoomXY centerTile = MapRasterTiles.getTileNumber(CENTER_GEOLOCATION.lat, CENTER_GEOLOCATION.lng, ZOOM);
            mapTiles = MapRasterTiles.getRasterTileZone(centerTile, NUM_TILES);
            //you need the beginning tile (tile on the top left corner) to convert geolocation to a location in pixels.
            beginTile = new ZoomXY(ZOOM, centerTile.x - ((NUM_TILES - 1) / 2), centerTile.y - ((NUM_TILES - 1) / 2));
            markerTexture = MapRasterTiles.getTextureMarker();

        } catch (IOException e) {
            e.printStackTrace();
        }

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

        setMarkers(filter, filterValue);
        setUsers();
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);


        //skins
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = fontName;
        textButtonStyle.fontColor = Color.WHITE;

        stage.addActor(filterButton());
        stage.addActor(loginBtn());

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(new GestureDetector(this));
        Gdx.input.setInputProcessor(multiplexer);
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
            font.getData().setScale(1.2f * camera.zoom);
            fontName.getData().setScale(1.6f * camera.zoom);

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

            float width =  Math.max(Math.max(Math.max(widthName, widthStreet), widthPrice1), widthPrice2);
            float height = 2f*heightName + heightStreet + heightPrice1 + heightPrice2;

            batch.draw(restInfo, positionOfDisplay.x + 10, positionOfDisplay.y, width + 20, height + 30);
            fontName.draw(batch, restaurantDisplayName.toUpperCase(Locale.ROOT), positionOfDisplay.x + 20, positionOfDisplay.y + 4f*heightName + 20);
            font.draw(batch, restaurantDisplayStreet, positionOfDisplay.x + 20, positionOfDisplay.y + 3.7f*heightStreet + 10);
            font.draw(batch, stringCenaZBonom, positionOfDisplay.x + 20, positionOfDisplay.y + 2.5f*heightPrice1 + 10);
            font.draw(batch, stringCenaBrezBona, positionOfDisplay.x + 20, positionOfDisplay.y + heightPrice2 + 10);
        }

        if(!loggedInAdmin.getUsername().equals("null")){
            String greeting = "Hello admin " + loggedInAdmin.getUsername();
            layout.setText(fontGreeting, greeting);
            float widthGreeting = layout.width;
            float heightGreeting = layout.height;

            fontGreeting.draw(batch, greeting, WIDTH - widthGreeting - 280, HEIGHT - heightGreeting -5);
        }

        batch.end();
    }
    private void setMarkers(String filter, String filterValue){
        markerArr.clear();
        restaurants.clear();

        FindIterable<Restaurant> docs = collection.find();// seznam restavracij
        for(Restaurant doc : docs){
            if(filter.equals("null")){
                Geolocation MARKER_GEOLOCATION = new Geolocation(doc.getLoc().get(0), doc.getLoc().get(1));
                PixelPosition marker = MapRasterTiles.getPixelPosition(MARKER_GEOLOCATION.lat, MARKER_GEOLOCATION.lng, MapRasterTiles.TILE_SIZE, ZOOM, beginTile.x, beginTile.y, HEIGHT);
                markerArr.add(marker);
                restaurants.add(doc);
            }
            else if(filter.equals("price")){
                if(toFloat(doc.getCenaSStudentskimBonom()) < toFloat(filterValue)){
                    Geolocation MARKER_GEOLOCATION = new Geolocation(doc.getLoc().get(0), doc.getLoc().get(1));
                    PixelPosition marker = MapRasterTiles.getPixelPosition(MARKER_GEOLOCATION.lat, MARKER_GEOLOCATION.lng, MapRasterTiles.TILE_SIZE, ZOOM, beginTile.x, beginTile.y, HEIGHT);
                    markerArr.add(marker);
                    restaurants.add(doc);
                }
            }
        }
    }

    private void setUsers() {
        FindIterable<User> docs = userCollection.find();// seznam restavracij
        for (User doc : docs)
            users.add(doc);
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

    public Restaurant isOnMarker(int x, int y){
        float w = (markerArr.get(markerArr.size-1).x - 20) + markerTexture.getWidth();
        float h = (markerArr.get(markerArr.size-1).y + markerTexture.getHeight());
        for(int i = 0; i < markerArr.size; i++){
            if(x > (markerArr.get(i).x - 20) && x< (markerArr.get(i).x - 20) + markerTexture.getWidth() &&
                    y > markerArr.get(i).y && y< markerArr.get(i).y + markerTexture.getHeight() ){
                return restaurants.get(i);
            }
        }
        List<Double> l = new ArrayList<>();
        return new Restaurant("null", "null", "0","0", l);
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
            if(!loginActive && !loggedInAdmin.getUsername().equals("null")) stage.addActor(createMarkerEntryUI());
            showRestaurantInfo = false;
            markerToAdd = marker;
//            double[] marker2 = MapRasterTiles.getCoords(markerToAdd, MapRasterTiles.TILE_SIZE, ZOOM, beginTile.x, beginTile.y, HEIGHT);
        }
        else{
            removeActor("markerTable");

            showRestaurantInfo = true;
            restaurantDisplayName = rest.getIme();
            restaurantDisplayStreet = rest.getLokacija();
            restaurantDisplayCenaBrezBona = rest.getCenaBrezStudentskegaBona();
            restaurantDisplayCenaSBonom = rest.getCenaSStudentskimBonom();
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
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) Gdx.app.exit();
        camera.zoom = MathUtils.clamp(camera.zoom, 0.5f, 2f);

        float effectiveViewportWidth = camera.viewportWidth * camera.zoom;
        float effectiveViewportHeight = camera.viewportHeight * camera.zoom;

        camera.position.x = MathUtils.clamp(camera.position.x, effectiveViewportWidth / 2f, WIDTH - effectiveViewportWidth / 2f);
        camera.position.y = MathUtils.clamp(camera.position.y, effectiveViewportHeight / 2f, HEIGHT - effectiveViewportHeight / 2f);
//        camera.position.x = effectiveViewportWidth;
//        camera.position.y = effectiveViewportHeight;
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

        Label showRestaurants = new Label("SHOW RESTAURANTS WITH: ", skin);
        showRestaurants.setColor(Color.ORANGE);
        showRestaurants.setFontScale(1.2f);

        Label price = new Label("Meal price less then:", skin);
        price.setWidth(50);
        final TextField priceField = new TextField("", skin);
        priceField.setTextFieldFilter(new NumberFilter());

        TextButton close = new TextButton("x", skin);
        close.setColor(Color.RED);
        close.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                removeActor("filterTable");
            }
        });

        TextButton saveBtn = new TextButton("Find", skin);
        saveBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String str = priceField.getText();
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
                    priceField.setText("max price");
                }
                else
                    priceField.setText(str);
                setMarkers("price", str);
            }
        });

        TextButton clearBtn = new TextButton("Clear", skin);
        clearBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setMarkers("null", "null");
            }
        });
        table.add(close).align(Align.right).row();

        table.add(showRestaurants).padBottom(10).row();
        layout.setText(font, "Meal price less then: ");


        Table textTable = new Table();
        textTable.add(price).width(layout.width).left().padBottom(10);
        textTable.add(priceField).width(50).padBottom(10).row();

        Table buttonTable = new Table();
        buttonTable.add(saveBtn);
        buttonTable.add(clearBtn);
        buttonTable.center();

        table.add(textTable).colspan(2).row();
        table.add(buttonTable).colspan(2);
        table.pack();

        table.setHeight(130);
//        table.setPosition(0 - table.getWidth(), viewport.getWorldHeight() - table.getHeight());
        table.setPosition(5, viewport.getWorldHeight() - table.getHeight());
        return table;
    }

    private Actor createMarkerEntryUI(){
        Table table = new Table();
        table.setName("markerTable");

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

        Label priceWithoutBon = new Label("Price without bon:", skin);
        final TextField priceWithoutBonField = new TextField("", skin);

        TextButton close = new TextButton("x", skin);
        Label labelX = new Label("X", skin);
        labelX.setFontScale(1.2f);
        close.setColor(Color.RED);
        close.setLabel(labelX);
        close.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
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

        TextButton saveBtn = new TextButton("Save", skin);
        saveBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String name = nameField.getText();
                String priceBon = priceWithBonField.getText();
                String priceNoBon = priceWithoutBonField.getText();
                if(!name.equals("") && !priceBon.equals("") && !priceNoBon.equals("")){
//                    Geolocation MARKER_GEOLOCATION = new Geolocation(doc.getLoc().get(0), doc.getLoc().get(1));
                    markerArr.add(markerToAdd);
                    List<Double> l = new ArrayList<>();
                    restaurants.add(new Restaurant(name, "nic", priceBon,priceNoBon, l));

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
        table.debug();

        Texture texture = new Texture(Gdx.files.internal("gray_background.jpg"));
        TextureRegion backgroundRegion = new TextureRegion(texture);
        table.setBackground(new TextureRegionDrawable(backgroundRegion));

        Label label = new Label("Login", skin);
        label.setColor(Color.ORANGE);
        label.setFontScale(1.2f);

        Label username = new Label("Username:", skin);
        final TextField usernameField = new TextField("", skin);

        final Label password = new Label("password:", skin);
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
//
//                BCryptPasswordEncoder passwordEncoder  = new BCryptPasswordEncoder();
//
//                for(User usr : users){
//                    if(usr.getPassword().equals(username)){
//                        if (passwordEncoder.matches(usr.getPassword(), password)) {
////                            loggedInAdmin = usr;
//                            System.out.println("LOGGED");
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
}
