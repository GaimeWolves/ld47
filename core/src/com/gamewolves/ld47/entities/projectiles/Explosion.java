package com.gamewolves.ld47.entities.projectiles;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.gamewolves.ld47.Main;
import com.gamewolves.ld47.entities.BulletManager;
import com.gamewolves.ld47.entities.enemies.Enemy;
import com.gamewolves.ld47.graphics.AnimatedSprite;
import com.gamewolves.ld47.physics.Physics;

public class Explosion extends Projectile
{
    private Body body;
    private AnimatedSprite explosion;
    private float time = 0;

    private Sound explosionSound;

    @Override
    public void loadResources(AssetManager assetManager)
    {
        Texture explosionTexture = assetManager.get("cracter/weapons/explodeanim_3.png");
        explosionSound = assetManager.get("sound/explode.wav");
        explosionSound.play(0.125f);

        explosion = new AnimatedSprite(explosionTexture, 16, 16, .5f);
        explosion.setCentered(true);
        explosion.setScale(2.5f, 2.5f);
    }

    @Override
    public void initialize(BulletManager bulletManager, Vector2 direction, Vector2 position, boolean isPlayerShot)
    {
        super.initialize(bulletManager, direction, position, isPlayerShot);
        damage = 5;
        canBeDisposed = false;

        explosion.setCentered(true);
        explosion.setPosition(position);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        CircleShape shape = new CircleShape();
        shape.setRadius(24);

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

        explosion.setPosition(position);
        explosion.update(deltaTime);

        time += deltaTime;
        if (time > .5f)
            isDisposable = true;
    }

    @Override
    public void render(SpriteBatch batch)
    {
        explosion.render(batch);
    }

    @Override
    public void dispose(AssetManager assetManager)
    {
        Physics.getWorld().destroyBody(body);
    }
}
