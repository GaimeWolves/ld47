package com.gamewolves.ld47.entities.enemies;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.gamewolves.ld47.Main;
import com.gamewolves.ld47.entities.BulletManager;
import com.gamewolves.ld47.entities.projectiles.AcydrProjectile;
import com.gamewolves.ld47.graphics.AnimatedSprite;
import com.gamewolves.ld47.physics.Physics;

/**
 * Name courtesy of Florian
 */
public class Voxyl extends Enemy
{
    private static float CHARGE_TIME = 1f;
    private static float VELOCITY = 100;

    private AnimatedSprite front, side, back, spawn, hatch;

    private enum State
    {
        Walking,
        Spawning,
        Charging
    }

    private State state;
    private float chargeTime = 0, spawnTime = 0;
    private Body body;

    private Vector2 lastDir = new Vector2();

    @Override
    public void loadResources(AssetManager assetManager)
    {
        front = new AnimatedSprite((Texture) assetManager.get("enemies/2/front_2.png"), 16, 32, 1f);
        side = new AnimatedSprite((Texture) assetManager.get("enemies/2/side_2.png"), 16, 32, 1f);
        back = new AnimatedSprite((Texture) assetManager.get("enemies/2/back_2.png"), 16, 32, 1f);
        spawn = new AnimatedSprite((Texture) assetManager.get("enemies/2/enemy_2_spawnanim.png"), 16, 32, 1f);
        hatch = new AnimatedSprite((Texture) assetManager.get("enemies/2/ausschluepf_2.png"), 16, 32, 1f);

        front.setCentered(true);
        side.setCentered(true);
        back.setCentered(true);
        spawn.setCentered(true);
        hatch.setCentered(true);
    }

    @Override
    public void initialize(BulletManager bulletManager, Vector2 position)
    {
        state = State.Spawning;
        this.position = position;
        this.bulletManager = bulletManager;
        health = 3;

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
        back.setPosition(position);
        side.setPosition(position);
        spawn.setPosition(position);
        hatch.setPosition(position);

        if (isGrabbed)
            return;

        if (state == State.Spawning) {
            spawn.update(deltaTime);
            hatch.update(deltaTime);

            spawnTime += deltaTime;
            if (spawnTime > 2f)
                state = State.Walking;
        }
        else if (state == State.Walking)
        {
            Vector2 dir = playerPos.cpy().sub(position);
            dir.nor();
            lastDir = dir.cpy();
            dir.scl(VELOCITY * deltaTime);
            position.add(dir);
            body.setTransform(position.cpy().add(0, -8), 0);

            if (position.dst2(playerPos) <= 2000) {
                state = State.Charging;
                //shoot.setTime(0);
            }

            front.update(deltaTime);
            back.update(deltaTime);
            side.update(deltaTime);
        }
        else
        {
            chargeTime += deltaTime;
            //shoot.update(deltaTime);

            if (chargeTime >= CHARGE_TIME)
            {
                isDisposable = true;

                AcydrProjectile projectile = new AcydrProjectile();
                projectile.loadResources(Main.get().assetManager);
                projectile.initialize(bulletManager, playerPos.cpy().sub(position.cpy().add(0, 8)).nor(), position.cpy().add(0, 8), false);
            }
        }
    }

    @Override
    public void render(SpriteBatch batch, Vector2 playerPos)
    {
        if (state == State.Spawning) {
            if (spawnTime < 1.f)
                spawn.render(batch);
            else
                hatch.render(batch);
        }
        else if (state == State.Walking)
        {
            float angle = lastDir.angle();

            if (angle <= 45)
            {
                side.setScale(1, 1);
                side.render(batch);
            }
            else if (angle <= 135)
                front.render(batch);
            else if (angle <= 225)
            {
                side.setScale(-1, 1);
                side.render(batch);
            }
            else if (angle <= 315)
                back.render(batch);
            else
            {
                side.setScale(1, 1);
                side.render(batch);
            }
        }
        else
        {
            spawn.render(batch);
        }
    }

    @Override
    public void dispose(AssetManager assetManager)
    {
        Physics.getWorld().destroyBody(body);
    }

    public static Vector2 getValidSpawnPosition()
    {
        return new Vector2(100, 0);
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
        body.setTransform(pos.cpy().add(0, -8), 0);
    }
}
