package com.gamewolves.ld47.entities.projectiles;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.gamewolves.ld47.entities.BulletManager;
import com.gamewolves.ld47.entities.enemies.Enemy;

public abstract class Projectile
{
    protected Vector2 position = new Vector2();
    protected Vector2 velocity = new Vector2();
    protected Vector2 acceleration = new Vector2();

    protected BulletManager bulletManager;
    protected boolean isPlayerShot;
    protected boolean isDisposable;
    protected boolean canBeDisposed = true;

    protected float damage;

    public abstract void loadResources(AssetManager assetManager);

    public void initialize(BulletManager bulletManager, Vector2 direction, Vector2 position, boolean isPlayerShot)
    {
        this.position = position;
        this.bulletManager = bulletManager;
        this.isPlayerShot = isPlayerShot;
        bulletManager.addProjectile(this);
    }

    public void update(float deltaTime, Vector2 towerPos, Array<Enemy> enemies)
    {
        velocity.add(acceleration.cpy().scl(deltaTime));
        position.add(velocity.cpy().scl(deltaTime));
    }

    public abstract void render(SpriteBatch batch);
    public abstract void dispose(AssetManager assetManager);

    public void setDisposable() { isDisposable = true; }

    public boolean isDisposable()
    {
        return isDisposable;
    }
    public boolean isPlayerShot() { return isPlayerShot; }
    public float getDamage() { return damage; }
    public Vector2 getPosition()
    {
        return position.cpy();
    }
    public boolean canBeDisposed() { return canBeDisposed; }
}
