package com.gamewolves.ld47.entities.enemies;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.gamewolves.ld47.entities.BulletManager;

public abstract class Enemy
{
    protected float health;
    protected boolean isDisposable;
    protected Vector2 position;
    protected BulletManager bulletManager;
    protected WaveManager waveManager;
    protected float contactDamage;
    protected boolean isGrabbed;
    protected boolean isBeingLasered;

    public abstract void loadResources(AssetManager assetManager);

    public void initialize(BulletManager bulletManager, Vector2 position, WaveManager waveManager)
    {
        this.bulletManager = bulletManager;
        this.waveManager = waveManager;
        this.position = position;
    }

    public void update(float deltaTime, Vector2 playerPos)
    {
        if (isBeingLasered)
            hit(deltaTime * 10);
    }

    public abstract void render(SpriteBatch batch, Vector2 playerPos);
    public abstract void dispose(AssetManager assetManager);

    public void grab()
    {
        isGrabbed = true;
    }

    public void release()
    {
        isGrabbed = false;
    }

    public abstract void repell(Vector2 towerPos);

    public void setLasered(boolean lasered) { isBeingLasered = lasered; }

    public void setPosition(Vector2 pos)
    {
        position = pos;
    }

    public Vector2 getPosition() { return position.cpy(); }

    public void hit(float damage)
    {
        health -= damage;

        if (health <= 0)
            isDisposable = true;
    }

    public boolean isDisposable() { return isDisposable; }

    public boolean isGrabbed() { return isGrabbed; }

    public float getContactDamage() { return contactDamage; }
}
