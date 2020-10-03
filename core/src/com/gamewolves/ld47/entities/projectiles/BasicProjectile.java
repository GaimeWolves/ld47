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
import com.gamewolves.ld47.Main;
import com.gamewolves.ld47.entities.BulletManager;
import com.gamewolves.ld47.graphics.AnimatedSprite;
import com.gamewolves.ld47.physics.Physics;

public class BasicProjectile extends Projectile
{
    private static final float ACCELERATION = 100;

    private Body body;
    private Sprite projectileSprite;
    private AnimatedSprite trailSprite;

    @Override
    public void loadResources(AssetManager assetManager)
    {
        Texture projectileTexture = assetManager.get("cracter/weapons/ws_1.png");
        Texture trailTexture = assetManager.get("cracter/weapons/wsanim_1.png");

        projectileSprite = new Sprite(projectileTexture);
        trailSprite = new AnimatedSprite(trailTexture, 8, 8, 1f);
    }

    @Override
    public void initialize(BulletManager bulletManager, Vector2 direction, Vector2 position, boolean isPlayerShot)
    {
        super.initialize(bulletManager, direction, position, isPlayerShot);
        damage = 1;

        projectileSprite.setOriginCenter();
        projectileSprite.setOriginBasedPosition(position.x, position.y);
        projectileSprite.setRotation(direction.angle());

        trailSprite.setCentered(true);
        trailSprite.setPosition(position.cpy().sub(direction.cpy().setLength((projectileSprite.getWidth() + trailSprite.getWidth()) * .5f)));
        trailSprite.setRotation(direction.angle());

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

        projectileSprite.setOriginBasedPosition(position.x, position.y);
        projectileSprite.setRotation(velocity.angle());

        trailSprite.setPosition(position.cpy().sub(velocity.cpy().setLength((projectileSprite.getWidth() + trailSprite.getWidth()) * .5f - 2)));
        trailSprite.setRotation(velocity.angle());
        trailSprite.update(deltaTime);
    }

    @Override
    public void render(SpriteBatch batch)
    {
        projectileSprite.draw(batch);
        trailSprite.render(batch);
    }

    @Override
    public void dispose(AssetManager assetManager)
    {
        Physics.getWorld().destroyBody(body);
    }
}
