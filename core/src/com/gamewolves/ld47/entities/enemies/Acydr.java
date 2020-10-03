package com.gamewolves.ld47.entities.enemies;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.gamewolves.ld47.Main;
import com.gamewolves.ld47.entities.BulletManager;
import com.gamewolves.ld47.physics.Physics;

/**
 * Name courtesy of Florian :d
 */
public class Acydr extends Enemy
{
    private static float CHARGE_TIME = 2f;
    private static float VELOCITY = 50;

    private enum State
    {
        Walking,
        Firing
    }

    private State state;
    private float chargeTime = 0;
    private Body body;

    @Override
    public void loadResources(AssetManager assetManager) {

    }

    @Override
    public void initialize(BulletManager bulletManager, Vector2 position)
    {
        state = State.Walking;
        this.position = position;
        health = 3;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        CircleShape shape = new CircleShape();
        shape.setRadius(10);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.isSensor = true;
        fixtureDef.shape = shape;

        body = Physics.getWorld().createBody(bodyDef);
        (body.createFixture(fixtureDef)).setUserData(this);
        body.setTransform(position, 0);

        shape.dispose();
    }

    @Override
    public void update(float deltaTime, Vector2 playerPos)
    {
        if (isGrabbed)
            return;

        if (state == State.Walking)
        {
            Vector2 dir = playerPos.cpy().sub(position);
            dir.nor().scl(VELOCITY * deltaTime);
            position.add(dir);
            body.setTransform(position, 0);

            if (position.dst2(playerPos) <= 10000) {
                state = State.Firing;
                chargeTime = 0;
            }
        }
        else
        {
            chargeTime += deltaTime;

            if (chargeTime >= CHARGE_TIME)
            {
                state = State.Walking;
                //Todo: shoot
            }
        }
    }

    @Override
    public void render(SpriteBatch batch, Vector2 playerPos)
    {
        Main.get().shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        Main.get().shapeRenderer.setColor(1, 1, state == State.Walking ? 1 : 0, 1);
        Main.get().shapeRenderer.circle(position.x, position.y, 10);
        Main.get().shapeRenderer.setColor(1,1,1,1);
        Main.get().shapeRenderer.end();
    }

    @Override
    public void dispose(AssetManager assetManager) {

    }

    @Override
    public void grab() {
        super.grab();
        state = State.Walking;
        chargeTime = 0;
    }

    @Override
    public void release() {
        super.release();
    }

    @Override
    public void setPosition(Vector2 pos) {
        super.setPosition(pos);
        body.setTransform(pos, 0);
    }
}
