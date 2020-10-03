package com.gamewolves.ld47.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
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
import com.gamewolves.ld47.physics.Physics;

public class Tower
{
    private static final float MAX_VELOCITY = 90;
    public static final float RADIUS = 100;

    private float angle = 0;
    private float velocity = 0;
    private float acceleration = 360;
    private Vector2 position = new Vector2();
    private Body body;

    private BulletManager bulletManager;

    private Array<Gun> guns = new Array<>();

    public void loadResources(AssetManager assetManager)
    {

    }

    public void initialize(BulletManager bulletManager)
    {
        this.bulletManager = bulletManager;

        BasicGun basicGun = new BasicGun();
        basicGun.loadResources(Main.get().assetManager);
        basicGun.initialize(bulletManager, 0);
        guns.add(basicGun);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;

        CircleShape shape = new CircleShape();
        shape.setRadius(10);

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

        for (Gun gun : guns)
        {
            gun.calcActualAngle(angle);
            gun.update(deltaTime);
        }
    }

    public void render(SpriteBatch batch)
    {
        Main.get().shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        Main.get().shapeRenderer.circle(0, 0, RADIUS);
        Main.get().shapeRenderer.end();

        Main.get().shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        Main.get().shapeRenderer.circle(position.x, position.y, 10);
        Main.get().shapeRenderer.end();

        for (Gun gun : guns)
            gun.render(batch);
    }

    public void dispose(AssetManager assetManager)
    {
        for (Gun gun : guns)
            gun.dispose(assetManager);
    }

    public Vector2 getPosition()
    {
        return position.cpy();
    }
}
