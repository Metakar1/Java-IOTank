package org.gyming.tank.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import org.gyming.tank.client.MainScreen;
import org.gyming.tank.client.TankGame;
import org.gyming.tank.connection.GameAction;

public class PlayerObject extends GameObject {
    public  int playerSize;
    public  int playerSpeed;
    public  int playerHP;
    public  int playerFireGap;
    public  int bulletSize;
    public  int bulletATK;
    public  int bulletHP;
    public  int ratio;
    public  int gunHeight;
    public  int gunWidth;
    public  int cirR;
    static  int boarder;
    private int playerID;
    public  int playerType;
    public  int bulletSpeed;
    public  int bulletTime;
    public  int alpha;
    private String playerName;
    private Stage stage;
    public ProgressBar hpProgress;

//    public static int playerGunWidth =

    public PlayerObject(float speed, float direction, float posX, float posY, int hp, int playerID,
                        String playerName, TankGame game, Stage stage, Group[] group, int type) {
        super(speed, direction, posX, posY, hp, game, stage, group);
        playerType = type;
        alpha = 0;
        if(type==0) {
            playerSize = 30;
            playerSpeed = 25;
            playerHP = 100;
            ratio = 1;
            gunHeight = playerSize * ratio * 2 / 3 + 3;
            gunWidth = gunHeight * 5 / 6;
            cirR = playerSize * ratio - gunHeight / 2;
            boarder = 3 * ratio;

            bulletSpeed = 10;
            bulletHP = 10;
            bulletATK = 10;
            bulletSize = 10;
            playerFireGap = 250;
            bulletTime = 100;
        }
        else if(type==1) {
            playerSize = 25;
            playerSpeed = 15;
            playerHP = 100;
            ratio = 1;
            gunHeight = playerSize * ratio * 2 / 3 + 3;
            gunWidth = gunHeight * 5 / 6;
            cirR = playerSize * ratio - gunHeight / 2;
            boarder = 3 * ratio;

            bulletSpeed = 20;
            bulletHP = 10;
            bulletATK = 20;
            bulletSize = 12;
            playerFireGap = 400;
            bulletTime = 300;
        }
        else if(type==2) {
            playerSize = 40;
            playerSpeed = 20;
            playerHP = 200;
            ratio = 1;
            gunHeight = playerSize * ratio * 2 / 3 + 3;
            gunWidth = gunHeight * 5 / 6;
            cirR = playerSize * ratio - gunHeight / 2;
            boarder = 3 * ratio;

            bulletSpeed = 7;
            bulletHP = 10;
            bulletATK = 5;
            bulletSize = 5;
            playerFireGap = 30;
            bulletTime = 100;
        }
        this.setHp(playerHP);
        if(playerName.equals("f")) this.playerID = 0;
        else this.playerID = playerName.hashCode();
        this.playerName = playerName;
        this.stage = stage;
        this.playerType = playerID;
        System.out.println(this.playerType);
        this.identifier = this.playerID;
        this.hpProgress = new ProgressBar(0f, playerHP, 1f, false, game.skin, "progressbar-tank");
        this.texture = createTexture();
        hpProgress.setWidth(this.texture.getWidth()-10);
        hpProgress.setHeight(20);
    }

    public final int getPlayerID() {
        return playerID;
    }

    public final void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public final String getPlayerName() {
        return playerName;
    }

    public final void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    @Override
    protected Texture createTexture() {
//        System.out.println("3:"+playerID);
        int cutSize =playerSize*ratio;


        Pixmap pixmap = new Pixmap(cutSize * 2, cutSize * 2, Pixmap.Format.RGBA8888);

        pixmap.setColor(colorPool.getGunBoarderColor());
        pixmap.fillRectangle(cirR-gunWidth/2,0,gunWidth,gunHeight);

        pixmap.setColor(colorPool.getGunColor());
        pixmap.fillRectangle(cirR-gunWidth/2+boarder,0+boarder,gunWidth-boarder*2,gunHeight-boarder*2);


        pixmap.setColor(colorPool.getUserBoarderColor(playerID));
        pixmap.fillCircle(cirR, cirR+gunHeight-boarder*2, cirR);
        pixmap.setColor(colorPool.getUserColor(playerID));
        pixmap.fillCircle(cirR, cirR+gunHeight-boarder*2, cirR - boarder);

        Pixmap pixmap1 = new Pixmap(playerSize*2,playerSize*2,Pixmap.Format.RGBA8888);
        pixmap1.drawPixmap(pixmap,0,0,pixmap.getWidth(),pixmap.getHeight(),0,0,pixmap1.getWidth(),pixmap1.getHeight());
        Texture texture = new Texture(pixmap1);
        texture.setFilter (Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
//        System.out.println(colorPool.getUserColor(playerID));
//        System.out.println("***");
//        texture
        return texture;
    }

    @Override
    public void fire(GameAction action, float posX, float posY) {
//      System.out.println(360-gunDirection);
        double delta = 0;
        if(playerType==0)
            delta = 0;
        else if(playerType==1)
            delta = 5;
        else if(playerType==2)
            delta = 15;

        float diffX = MathUtils.sin((360-gunDirection)/180)*this.gunWidth;
        float diffY = MathUtils.cos((360-gunDirection)/180)*this.gunWidth;
        float rx = posX + this.cirR;
        float ry = posY + this.cirR;
//        System.out.println(Float.toString(diffX)+" "+Float.toString(diffY));
        rx += 50*MathUtils.sin((360-gunDirection)/180*(float) Math.PI);
        ry += 50*MathUtils.cos((360-gunDirection)/180*(float) Math.PI);
        BulletObject bullet = new BulletObject(this.bulletSpeed, action.getDirection()+ (float) MainScreen.dataMaker.nextGaussian()*(float) delta/180f*(float)Math.PI, rx - this.bulletSize, ry - this.bulletSize, this.bulletHP, playerID, game, stage, group,this.bulletSize,this.bulletATK,this.bulletTime);
//        System.out.println(playerID);
        group[0].addActor(bullet);
    }

    @Override
    protected void recoverSpeed() {
        if (speed > 0) {
            speed = Math.max(speed - acceleration, 0);
        }
        else {
            speed = Math.min(speed + acceleration, 0);
        }
    }
    @Override
    protected void getDmg()
    {
        if(this.dmg>100)
        {
            int cutSize =playerSize*ratio;
            Pixmap pixmap = new Pixmap(cutSize * 2, cutSize * 2, Pixmap.Format.RGBA8888);

            pixmap.setColor(Color.RED);
            pixmap.fillRectangle(cirR-gunWidth/2,0,gunWidth,gunHeight);

            pixmap.setColor(Color.RED);
            pixmap.fillRectangle(cirR-gunWidth/2+boarder,0+boarder,gunWidth-boarder*2,gunHeight-boarder*2);


            pixmap.setColor(Color.RED);
            pixmap.fillCircle(cirR, cirR+gunHeight-boarder*2, cirR);
            pixmap.setColor(Color.RED);
            pixmap.fillCircle(cirR, cirR+gunHeight-boarder*2, cirR - boarder);

            Pixmap pixmap1 = new Pixmap(playerSize*2,playerSize*2,Pixmap.Format.RGBA8888);
            pixmap1.drawPixmap(pixmap,0,0,pixmap.getWidth(),pixmap.getHeight(),0,0,pixmap1.getWidth(),pixmap1.getHeight());
            Texture texture = new Texture(pixmap1);
            texture.setFilter (Texture.TextureFilter.Linear, Texture.TextureFilter.Linear );
//        System.out.println(colorPool.getUserColor(playerID));
//        System.out.println("***");
//        texture
            this.texture = texture;
            this.dmg-=2;
//            System.out.println(this.dmg);
            if(this.dmg==100)
            {
                this.texture = createTexture();
            }
        }
    }
    @Override
    public void QSkill(GameAction action, float posX, float posY)
    {
        if(playerType==2)
        {
            float diffX = MathUtils.sin((360-gunDirection)/180)*this.gunWidth;
            float diffY = MathUtils.cos((360-gunDirection)/180)*this.gunWidth;
            float rx = posX + this.cirR;
            float ry = posY + this.cirR;
//        System.out.println(Float.toString(diffX)+" "+Float.toString(diffY));
//            rx += 50*MathUtils.sin((360-gunDirection)/180*(float) Math.PI);
//            ry += 50*MathUtils.cos((360-gunDirection)/180*(float) Math.PI);
            BulletObject bullet;
            for(int i=0;i<8;i++) {
                bullet = new BulletObject(1,(360f-45*i)/180*(float) Math.PI,rx - this.bulletSize * 6+40*MathUtils.sin((360f-45*i)/180*(float) Math.PI), ry - this.bulletSize * 6+40*MathUtils.cos((360f-45*i)/180*(float) Math.PI), this.bulletHP * 6, playerID, game, stage, group, this.bulletSize * 6, this.bulletATK * 100,1000000000);
                group[0].addActor(bullet);
            }
        }
        else if(playerType ==1)
        {
            this.alpha = 800;
        }
        else if(playerType ==0)
        {
            this.alpha = 300;
        }
    }


    public void die() {
        stage.getRoot().removeActor(this);
        group[1].removeActor(this);
        group[0].removeActor(this);
        this.setHp(0);
    }
}