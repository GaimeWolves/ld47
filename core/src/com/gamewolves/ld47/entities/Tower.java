package com.gamewolves.ld47.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.gamewolves.ld47.Main;
import com.gamewolves.ld47.entities.guns.BasicGun;
import com.gamewolves.ld47.entities.guns.Gun;
import com.gamewolves.ld47.graphics.AnimatedSprite;
import com.gamewolves.ld47.physics.Physics;

public class Tower
{
    private static final float MAX_VELOCITY = 90;
    public static final float RADIUS = 40;

    private float angle = 0;
    private float velocity = 0;
    private float acceleration = 360;
    private Vector2 position = new Vector2();
    private Body body;

    private BulletManager bulletManager;

    private Array<Gun> guns = new Array<>();

    private AnimatedSprite[] idleAnimations = new AnimatedSprite[3];
    private float animTime = 0;
    private int animation = 0;

    public void loadResources(AssetManager assetManager)
    {
        Texture idle0 = assetManager.get("cracter/prof_idle_1.png");
        Texture idle1 = assetManager.get("cracter/prof_idle_2.png");
        Texture idle2 = assetManager.get("cracter/prof_idle_3.png");

        idleAnimations[0] = new AnimatedSprite(idle0, idle0.getWidth(), idle0.getHeight(), 3f);
        idleAnimations[1] = new AnimatedSprite(idle1, idle0.getWidth(), idle0.getHeight(), 3f);
        idleAnimations[2] = new AnimatedSprite(idle2, idle0.getWidth(), idle0.getHeight(), 3f);

        idleAnimations[0].setCentered(true);
        idleAnimations[1].setCentered(true);
        idleAnimations[2].setCentered(true);

        idleAnimations[0].setScale(0.6f, 0.6f);
        idleAnimations[1].setScale(0.6f, 0.6f);
        idleAnimations[2].setScale(0.6f, 0.6f);
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
            gun.update(deltaTime);
        }
    }

    public void render(SpriteBatch batch)
    {
        idleAnimations[animation].render(batch);

        batch.end();
        for (Gun gun : guns)
            gun.render(batch);
        batch.begin();
    }

    public void dispose(AssetManager assetManager)
    {
        Physics.getWorld().destroyBody(body);

        for (Gun gun : guns)
            gun.dispose(assetManager);
    }

    public Vector2 getPosition()
    {
        return position.cpy();
    }
}
