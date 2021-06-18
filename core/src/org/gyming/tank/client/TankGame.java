package org.gyming.tank.client;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.google.gson.Gson;
import org.gyming.tank.connection.ConnectMsg;
import org.gyming.tank.connection.GameFrame;
import org.gyming.tank.connection.MsgIO;
import org.gyming.tank.object.GameObject;

import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

public class TankGame extends Game {
    public int playerID, playerMP, playerType;
    public int nowFrame, lastFireFrame;
    public ActionGroup actionGroup;
    public LinkedBlockingQueue<GameObject> toBeDeleted;
    public StartScreen startScreen;
    public MainScreen mainScreen;
    public GameOverScreen gameOverScreen;
    public CharacterSelectionScreen characterSelectionScreen;
    public Skin skin;
    MsgIO S2C, C2S;
    LinkedBlockingQueue<GameFrame> download;
    LinkedBlockingQueue<String> queue;
    Array<Image> characterFlags;
    private String userName, roomName;
    private String serverAddress;
    private int port;

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public boolean buildConnection() {
        nowFrame = 0;
        lastFireFrame = 0;
        try {
            C2S = new MsgIO(new Socket(serverAddress, port));
            S2C = new MsgIO(new Socket(serverAddress, port));
            S2C.send(userName);
            C2S.send(userName);
            Gson gson = new Gson();
            C2S.send(gson.toJson(new ConnectMsg("join", userName, roomName)));
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void create() {
        skin = new Skin(Gdx.files.internal("skin.json")) {
            // 重写 json loader 以在 skin JSON 中支持 FreeType 字体
            @Override
            protected Json getJsonLoader(final FileHandle skinFile) {
                Json json = super.getJsonLoader(skinFile);
                final Skin skin = this;

                json.setSerializer(FreeTypeFontGenerator.class, new Json.ReadOnlySerializer<FreeTypeFontGenerator>() {
                    @Override
                    public FreeTypeFontGenerator read(Json json,
                                                      JsonValue jsonData, Class type) {
                        String path = json.readValue("font", String.class, jsonData);
                        jsonData.remove("font");

                        FreeTypeFontGenerator.Hinting hinting = FreeTypeFontGenerator.Hinting.valueOf(json.readValue("hinting",
                                String.class, "AutoMedium", jsonData));
                        jsonData.remove("hinting");

                        Texture.TextureFilter minFilter = Texture.TextureFilter.valueOf(
                                json.readValue("minFilter", String.class, "Nearest", jsonData));
                        jsonData.remove("minFilter");

                        Texture.TextureFilter magFilter = Texture.TextureFilter.valueOf(
                                json.readValue("magFilter", String.class, "Nearest", jsonData));
                        jsonData.remove("magFilter");

                        FreeTypeFontGenerator.FreeTypeFontParameter parameter = json.readValue(FreeTypeFontGenerator.FreeTypeFontParameter.class, jsonData);
                        parameter.hinting = hinting;
                        parameter.minFilter = minFilter;
                        parameter.magFilter = magFilter;
                        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(skinFile.parent().child(path));
                        BitmapFont font = generator.generateFont(parameter);
                        skin.add(jsonData.name, font);
                        if (parameter.incremental) {
                            generator.dispose();
                            return null;
                        }
                        else {
                            return generator;
                        }
                    }
                });

                return json;
            }
        };

        // 添加角色阵营旗帜
        characterFlags = new Array<>();
        Image characterFlag = new Image(new Texture(Gdx.files.internal("0_flag.png")));
        float scale = Math.max(100f / characterFlag.getWidth(), 100f / characterFlag.getHeight());
        characterFlag.setScale(scale, scale);
        characterFlag.setPosition(10f, Gdx.graphics.getHeight() - 110f);
        characterFlags.add(characterFlag);
        characterFlag = new Image(new Texture(Gdx.files.internal("1_flag.png")));
        scale = Math.max(100f / characterFlag.getWidth(), 100f / characterFlag.getHeight());
        characterFlag.setScale(scale, scale);
        characterFlag.setPosition(10f, Gdx.graphics.getHeight() - 110f);
        characterFlags.add(characterFlag);
        characterFlag = new Image(new Texture(Gdx.files.internal("2_flag.png")));
        scale = Math.max(100f / characterFlag.getWidth(), 100f / characterFlag.getHeight());
        characterFlag.setScale(scale, scale);
        characterFlag.setPosition(10f, Gdx.graphics.getHeight() - 110f);
        characterFlags.add(characterFlag);

        // 初始化数据结构和不同页面
        actionGroup = new ActionGroup();
        startScreen = new StartScreen(this);
        characterSelectionScreen = new CharacterSelectionScreen(this);
        mainScreen = new MainScreen(this);
        gameOverScreen = new GameOverScreen(this);
        download = new LinkedBlockingQueue<>();
        queue = new LinkedBlockingQueue<>();
        toBeDeleted = new LinkedBlockingQueue<>();
        setScreen(startScreen);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        startScreen.dispose();
        mainScreen.dispose();
        gameOverScreen.dispose();
        characterSelectionScreen.dispose();
        skin.dispose();
    }
}