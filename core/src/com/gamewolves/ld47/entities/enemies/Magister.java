package com.gamewolves.ld47.entities.enemies;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.gamewolves.ld47.Main;
import com.gamewolves.ld47.entities.BulletManager;
import com.gamewolves.ld47.entities.projectiles.VoxylExplosion;
import com.gamewolves.ld47.graphics.AnimatedSprite;
import com.gamewolves.ld47.physics.Physics;

/**
 * Name courtesy of Florian
 */
public class Magister extends Enemy
{
    private static final float CHARGE_TIME = 4f;
    private static final float VELOCITY = 25;
    private static final int SPAWN_COUNT = 8;

    private AnimatedSprite front, side, charge;

    private enum State
    {
        Walking,
        Charging
    }

    private State state;
    private float chargeTime = 0;
    private int spawned;
    private Body body;

    private Vector2 lastDir = new Vector2();

    @Override
    public void loadResources(AssetManager assetManager)
    {
        front = new AnimatedSprite((Texture) assetManager.get("enemies/3/hex_front_back_anim.png"), 16, 32, .25f);
        side = new AnimatedSprite((Texture) assetManager.get("enemies/3/hex_side_anim.png"), 16, 32, .25f);
        charge = new AnimatedSprite((Texture) assetManager.get("enemies/3/hex_beschwör.png"), 16, 32, CHARGE_TIME);

        front.setUseOrigin(true);
        side.setUseOrigin(true);
        charge.setUseOrigin(true);

        front.setOrigin(front.getWidth() * .5f, front.getHeight() * .25f);
        side.setOrigin(side.getWidth() * .5f, side.getHeight() * .25f);
        charge.setOrigin(charge.getWidth() * .5f, charge.getHeight() * .25f);
    }

    @Override
    public void initialize(BulletManager bulletManager, Vector2 position, WaveManager waveManager)
    {
        super.initialize(bulletManager, position, waveManager);
        state = State.Walking;
        health = 10;
        contactDamage = 3;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        CircleShape shape = new CircleShape();
        shape.setRadius(5);

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
        super.update(deltaTime, playerPos);

        front.setPosition(position);
        side.setPosition(position);
        charge.setPosition(position);

        if (isGrabbed)
            return;

        if (state == State.Walking)
        {
            if (position.dst2(playerPos) <= 15000) {
                state = State.Charging;
                charge.setTime(0);
                chargeTime = 0;
                spawned = 0;
                return;
            }

            Vector2 dir = playerPos.cpy().sub(position);
            dir.nor();
            lastDir = dir.cpy();
            dir.scl(VELOCITY * deltaTime);
            position.add(dir);
            body.setTransform(position, 0);

            front.update(deltaTime);
            side.update(deltaTime);
        }
        else
        {
            chargeTime += deltaTime;
            charge.update(deltaTime);

            if (chargeTime > spawned * (CHARGE_TIME / 8)) {
                Vector2 pos = Vector2.X.cpy().setAngle(spawned * 45 + 90).setLength(16).add(position);
                waveManager.addEnemy(new Crab(), pos);
                spawned++;
            }

            if (chargeTime >= CHARGE_TIME)
                state = State.Walking;
        }
    }

    @Override
    public void render(SpriteBatch batch, Vector2 playerPos)
    {
        if (state == State.Walking)
        {
            float angle = lastDir.angle();

            if (angle <= 45)
            {
                side.setScale(1, 1);
                side.render(batch);
            }
            else if (angle <= 135)
            {
                front.setScale(-1, 1);
                front.render(batch);
            }
            else if (angle <= 225)
            {
                side.setScale(-1, 1);
                side.render(batch);
            }
            else if (angle <= 315)
            {
                front.setScale(1, 1);
                front.render(batch);
            }
            else
            {
                side.setScale(1, 1);
                side.render(batch);
            }
        }
        else
        {
            charge.render(batch);
        }
    }

    @Override
    public void dispose(AssetManager assetManager)
    {
        Physics.getWorld().destroyBody(body);
    }

    public static Vector2 getValidSpawnPosition()
    {
        return new Vector2(400, 400).rotate(MathUtils.random(360));
    }

    @Override
    public void repell(Vector2 towerPos)
    { }

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
