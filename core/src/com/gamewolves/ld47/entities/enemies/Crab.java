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
import com.gamewolves.ld47.Main;
import com.gamewolves.ld47.entities.BulletManager;
import com.gamewolves.ld47.entities.projectiles.AcydrProjectile;
import com.gamewolves.ld47.graphics.AnimatedSprite;
import com.gamewolves.ld47.physics.Physics;

/**
 * Name courtesy of Florian :d
 */
public class Crab extends Enemy
{
    private static float VELOCITY = 75;
    private static final float SPAWN_TIME = 2.f;

    private AnimatedSprite walk, spawn;

    private Body body;
    private float spawnTime;

    @Override
    public void loadResources(AssetManager assetManager)
    {
        walk = new AnimatedSprite((Texture) assetManager.get("enemies/3/crabwalk_walk.png"), 16, 16, .5f);
        walk.setCentered(true);
        walk.setScale(.75f, .75f);

        spawn = new AnimatedSprite((Texture) assetManager.get("enemies/3/crab_spawn.png"), 16, 16, SPAWN_TIME);
        spawn.setCentered(true);
        spawn.setScale(.75f, .75f);
    }

    @Override
    public void initialize(BulletManager bulletManager, Vector2 position, WaveManager waveManager)
    {
        super.initialize(bulletManager, position, waveManager);
        health = 1;
        contactDamage = .5f;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        CircleShape shape = new CircleShape();
        shape.setRadius(3);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.isSensor = true;
        fixtureDef.shape = shape;

        body = Physics.getWorld().createBody(bodyDef);
        (body.createFixture(fixtureDef)).setUserData(this);
        body.setTransform(position, 0);
        body.setActive(false);

        shape.dispose();
    }

    @Override
    public void update(float deltaTime, Vector2 playerPos)
    {
        super.update(deltaTime, playerPos);

        walk.setPosition(position);
        spawn.setPosition(position);

        if (isGrabbed)
            return;

        if (spawnTime >= 1)
        {
            if (!body.isActive())
                body.setActive(true);

            Vector2 dir = playerPos.cpy().sub(position);
            float dst = dir.len2();
            dir.nor();
            dir.scl(VELOCITY * deltaTime);
            if (dst > 50)
            {
                position.add(dir);
                body.setTransform(position, 0);
            }

            walk.update(deltaTime);
        }
        else
        {
            spawnTime += deltaTime;
            spawn.update(deltaTime);
        }
    }

    @Override
    public void render(SpriteBatch batch, Vector2 playerPos)
    {
        if (spawnTime >= 1)
            walk.render(batch);
        else
            spawn.render(batch);
    }

    @Override
    public void repell(Vector2 towerPos)
    {
        Vector2 dir = towerPos.cpy().sub(position).nor();
        position.add(dir.scl(40));
    }

    @Override
    public void dispose(AssetManager assetManager)
    {
        Physics.getWorld().destroyBody(body);
    }

    @Override
    public void grab() {
        super.grab();
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
