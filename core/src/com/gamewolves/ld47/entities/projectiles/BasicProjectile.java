package com.gamewolves.ld47.entities.projectiles;

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

public class BasicProjectile extends Projectile
{
    private static final float ACCELERATION = 100;

    private Body body;

    @Override
    public void loadResources(AssetManager assetManager) {

    }

    @Override
    public void initialize(BulletManager bulletManager, Vector2 direction, Vector2 position, boolean isPlayerShot)
    {
        super.initialize(bulletManager, direction, position, isPlayerShot);
        damage = 1;

        velocity = direction.cpy();
        velocity.setLength(ACCELERATION);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        CircleShape shape = new CircleShape();
        shape.setRadius(2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.isSensor = true;
        fixtureDef.shape = shape;

        body = Physics.getWorld().createBody(bodyDef);
        (body.createFixture(fixtureDef)).setUserData(this);
        body.setTransform(position, 0);

        shape.dispose();
    }

    @Override
    public void update(float deltaTime)
    {
        super.update(deltaTime);
        body.setTransform(position, 0);
    }

    @Override
    public void render(SpriteBatch batch)
    {
        Main.get().shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        Main.get().shapeRenderer.setColor(0,1,0,1);
        Main.get().shapeRenderer.circle(position.x, position.y, 2);
        Main.get().shapeRenderer.setColor(1,1,1,1);
        Main.get().shapeRenderer.end();
    }

    @Override
    public void dispose(AssetManager assetManager)
    {

    }
}
