package com.gamewolves.ld47.entities.projectiles;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
import com.gamewolves.ld47.entities.enemies.Enemy;
import com.gamewolves.ld47.graphics.AnimatedSprite;
import com.gamewolves.ld47.physics.Physics;

public class HomingProjectile extends Projectile
{
    private static final float ACCELERATION = 150;

    private Body body;
    private Sprite projectileSprite;
    private AnimatedSprite trailSprite;

    private boolean hasBeenSplit;
    private float liveTime;

    @Override
    public void loadResources(AssetManager assetManager)
    {
        Texture projectileTexture = assetManager.get("cracter/weapons/ws_2.png");
        Texture trailTexture = assetManager.get("cracter/weapons/wsanim_2.png");

        projectileSprite = new Sprite(projectileTexture);
        trailSprite = new AnimatedSprite(trailTexture, 8, 8, 1f);
    }

    @Override
    public void initialize(BulletManager bulletManager, Vector2 direction, Vector2 position, boolean isPlayerShot)
    {
        super.initialize(bulletManager, direction, position, isPlayerShot);
        damage = hasBeenSplit ? .5f : 1;

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
    public void update(float deltaTime, Vector2 towerPos, Array<Enemy> enemies)
    {
        Enemy nearest = null;
        for (Enemy enemy : enemies) {
            float dstNew = enemy.getPosition().dst2(position);
            float oldDist = nearest == null ? Float.POSITIVE_INFINITY : nearest.getPosition().dst2(position);

            if (dstNew < 5000 && dstNew > 1)
            {
                if (dstNew < oldDist)
                    nearest = enemy;
            }
        }

        if (nearest != null) {
            velocity.setAngle(MathUtils.lerpAngleDeg(velocity.angle(), nearest.getPosition().sub(position).angle(),  Math.min(1, deltaTime * 5)));
        }

        super.update(deltaTime, towerPos, enemies);
        body.setTransform(position, 0);

        projectileSprite.setOriginBasedPosition(position.x, position.y);
        projectileSprite.setRotation(velocity.angle());

        trailSprite.setPosition(position.cpy().sub(velocity.cpy().setLength((projectileSprite.getWidth() + trailSprite.getWidth()) * .5f - 2)));
        trailSprite.setRotation(velocity.angle());
        trailSprite.update(deltaTime);

        liveTime += deltaTime;
        if (liveTime > (hasBeenSplit ? .8f : .3f)) {
            if (!hasBeenSplit) {
                for (float alpha = velocity.angle() - 46; alpha < velocity.angle() + 46; alpha += 45) {
                    Vector2 newDir = velocity.cpy().setAngle(alpha);
                    HomingProjectile projectile = new HomingProjectile();
                    projectile.hasBeenSplit = true;
                    projectile.loadResources(Main.get().assetManager);
                    projectile.initialize(bulletManager, newDir, position.cpy(), true);
                }
            }
            isDisposable = true;
        }
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
