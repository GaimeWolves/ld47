package com.gamewolves.ld47.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.gamewolves.ld47.Main;
import com.gamewolves.ld47.entities.guns.BasicGun;
import com.gamewolves.ld47.entities.guns.ExplosiveGun;
import com.gamewolves.ld47.entities.guns.Gun;
import com.gamewolves.ld47.entities.guns.HomingGun;
import com.gamewolves.ld47.entities.guns.LaserGun;
import com.gamewolves.ld47.graphics.AnimatedSprite;
import com.gamewolves.ld47.physics.Physics;
import com.gamewolves.ld47.utils.MathUtils;

public class Tower
{
    private static final float MAX_VELOCITY = 90;
    public static final float RADIUS = 40;

    private float angle = 0;
    private float velocity = 0;
    private float acceleration = 360;
    private Vector2 position = new Vector2();
    private Body body;
    private float health = 50;

    private BulletManager bulletManager;

    private Array<Gun> guns = new Array<>();

    private AnimatedSprite[] idleAnimations = new AnimatedSprite[3];
    private float animTime = 0;
    private int animation = 0;

    private Texture healthBarBg, healthBar;
    private Label upgradeLabel;
    private float textTime;

    private Sound upgradeSound;

    public void loadResources(AssetManager assetManager)
    {
        upgradeSound = assetManager.get("sound/newgun.wav");

        Texture idle0 = assetManager.get("cracter/prof_idle_1.png");
        Texture idle1 = assetManager.get("cracter/prof_idle_2.png");
        Texture idle2 = assetManager.get("cracter/prof_idle_3.png");
        healthBarBg = assetManager.get("cracter/health_bg.png");
        healthBar = assetManager.get("cracter/health_bar.png");

        idleAnimations[0] = new AnimatedSprite(idle0, idle0.getWidth(), idle0.getHeight(), 3f);
        idleAnimations[1] = new AnimatedSprite(idle1, idle0.getWidth(), idle0.getHeight(), 3f);
        idleAnimations[2] = new AnimatedSprite(idle2, idle0.getWidth(), idle0.getHeight(), 3f);

        idleAnimations[0].setCentered(true);
        idleAnimations[1].setCentered(true);
        idleAnimations[2].setCentered(true);

        idleAnimations[0].setScale(0.7f, 0.7f);
        idleAnimations[1].setScale(0.7f, 0.7f);
        idleAnimations[2].setScale(0.7f, 0.7f);

        upgradeLabel = new Label("UPGRADE!", new Label.LabelStyle(Main.get().font, Color.WHITE));
        upgradeLabel.setAlignment(Align.center);
        upgradeLabel.setFontScale(0.25f);
        upgradeLabel.setPosition(position.x, position.y + 10, Align.center);
    }

    public void initialize(BulletManager bulletManager)
    {
        this.bulletManager = bulletManager;

        BasicGun basicGun = new BasicGun();
        basicGun.loadResources(Main.get().assetManager);
        basicGun.initialize(bulletManager, 0);
        guns.add(basicGun);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        CircleShape shape = new CircleShape();
        shape.setRadius(5);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.isSensor = true;
        fixtureDef.shape = shape;

        body = Physics.getWorld().createBody(bodyDef);
        (body.createFixture(fixtureDef)).setUserData(this);

        shape.dispose();
    }

    public void update(float deltaTime)
    {
        if (Gdx.input.isKeyPressed(Input.Keys.D))
            velocity -= acceleration * deltaTime;
        else if (Gdx.input.isKeyPressed(Input.Keys.A))
            velocity += acceleration * deltaTime;
        else
            velocity *= .8f;

        if (Math.abs(velocity) > MAX_VELOCITY)
            velocity = velocity > 0 ? MAX_VELOCITY : -MAX_VELOCITY;

        angle += (velocity * deltaTime) % 360;

        position = new Vector2(1, 0);
        position.setLength(RADIUS);
        position.setAngle(angle);

        body.setTransform(position, 0);

        for (AnimatedSprite sprite : idleAnimations)
        {
            sprite.setPosition(position);
            sprite.setRotation(angle);
        }

        idleAnimations[animation].update(deltaTime);
        animTime += deltaTime;
        if (animTime > 3f)
        {
            animation = (animation + 1) % idleAnimations.length;
            animTime = idleAnimations[animation].getTime();
        }

        for (Gun gun : guns)
        {
            gun.calcActualAngle(angle);
            gun.update(deltaTime, position);
        }

        health += deltaTime * .25f;
        if (health > 50)
            health = 50;

        upgradeLabel.setPosition(position.x, position.y + 20 - (10 * textTime), Align.center);
        if (textTime > 0) {
            textTime -= deltaTime;
            if (textTime < 0)
                textTime = 0;

            Color color = new Color();
            Color.argb8888ToColor(color, MathUtils.HSLtoRGB((textTime * 4) % 1, 1, .5f));
            upgradeLabel.setColor(color);
        }
    }

    public void render(SpriteBatch batch)
    {
        idleAnimations[animation].render(batch);
        for (Gun gun : guns)
            gun.render(batch);
    }

    public void renderUiInWorldSpace(SpriteBatch batch)
    {
        Color color = batch.getColor().cpy();
        upgradeLabel.draw(batch, Math.min(textTime, batch.getColor().a));
        batch.setColor(color);
    }

    public void renderUI(SpriteBatch batch)
    {
        float bgWidth = Main.get().Width - 10;
        batch.draw(healthBarBg, -bgWidth * .5f, 6 - Main.get().Height * .5f, bgWidth, healthBarBg.getHeight());
        float barWidth = bgWidth - 30;
        int actualWidth = (int) (barWidth * (health / 50.f));
        batch.draw(healthBar, -barWidth * .5f, 8 - Main.get().Height * .5f, 0, 0, actualWidth, healthBar.getHeight());
    }

    public void dispose(AssetManager assetManager)
    {
        Physics.getWorld().destroyBody(body);

        for (Gun gun : guns)
            gun.dispose(assetManager);
    }

    public boolean hit(float damage)
    {
        health -= damage;
        if (health < 0)
            health = 0;
        return health <= 0;
    }

    public void unlockNextGun()
    {
        if (guns.size == 1) {
            HomingGun homingGun = new HomingGun();
            homingGun.loadResources(Main.get().assetManager);
            homingGun.initialize(bulletManager, 90);
            guns.add(homingGun);
        }
        else if (guns.size == 2) {
            ExplosiveGun explosiveGun = new ExplosiveGun();
            explosiveGun.loadResources(Main.get().assetManager);
            explosiveGun.initialize(bulletManager, -45);
            guns.add(explosiveGun);
        }
        else {
            LaserGun laserGun = new LaserGun();
            laserGun.loadResources(Main.get().assetManager);
            laserGun.initialize(bulletManager, 45);
            guns.add(laserGun);
        }

        textTime = 1;
        upgradeSound.play(0.125f);
    }

    public Vector2 getPosition()
    {
        return position.cpy();
    }
}
