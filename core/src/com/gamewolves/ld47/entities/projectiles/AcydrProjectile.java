package com.gamewolves.ld47.entities.projectiles;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.gamewolves.ld47.Main;
import com.gamewolves.ld47.entities.BulletManager;
import com.gamewolves.ld47.entities.enemies.Enemy;
import com.gamewolves.ld47.physics.Physics;

public class AcydrProjectile extends Projectile
{
    private static final float ACCELERATION = 100;

    private Body body;
    private Sprite sprite;

    @Override
    public void loadResources(AssetManager assetManager)
    {
        sprite = new Sprite((Texture) assetManager.get("enemies/1/shot.png"));
        sprite.setOriginCenter();
    }

    @Override
    public void initialize(BulletManager bulletManager, Vector2 direction, Vector2 position, boolean isPlayerShot)
    {
        super.initialize(bulletManager, direction, position, isPlayerShot);
        damage = 5;

        sprite.setOriginBasedPosition(position.x, position.y);

        velocity = direction.cpy();
        velocity.setLength(ACCELERATION);

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
    public void update(float deltaTime, Vector2 towerPos, Array<Enemy> enemies)
    {
        super.update(deltaTime, towerPos, enemies);
        body.setTransform(position, 0);
        sprite.setOriginBasedPosition(position.x, position.y);
        sprite.rotate(deltaTime * 180);
    }

    @Override
    public void render(SpriteBatch batch)
    {
        batch.end();
        batch.begin();
        sprite.draw(batch);
        batch.end();
        batch.begin();
    }

    @Override
    public void dispose(AssetManager assetManager)
    {
        Physics.getWorld().destroyBody(body);
    }
}
