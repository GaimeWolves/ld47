package com.gamewolves.ld47.entities.projectiles;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.gamewolves.ld47.entities.BulletManager;

public abstract class Projectile
{
    protected Vector2 position = new Vector2();
    protected Vector2 velocity = new Vector2();
    protected Vector2 acceleration = new Vector2();

    protected BulletManager bulletManager;
    protected boolean isPlayerShot;
    protected boolean isDisposable;

    protected float damage;

    public abstract void loadResources(AssetManager assetManager);

    public void initialize(BulletManager bulletManager, Vector2 direction, Vector2 position, boolean isPlayerShot)
    {
        this.position = position;
        this.bulletManager = bulletManager;
        this.isPlayerShot = isPlayerShot;
        bulletManager.addProjectile(this);
    }

    public void update(float deltaTime)
    {
        velocity.add(acceleration.cpy().scl(deltaTime));
        position.add(velocity.cpy().scl(deltaTime));
    }

    public abstract void render(SpriteBatch batch);
    public abstract void dispose(AssetManager assetManager);

    public boolean isDisposable()
    {
        return isDisposable;
    }

    public Vector2 getPosition()
    {
        return position.cpy();
    }
}
