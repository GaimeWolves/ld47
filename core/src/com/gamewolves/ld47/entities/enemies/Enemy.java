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
    protected float contactDamage;
    protected boolean isGrabbed;

    public abstract void loadResources(AssetManager assetManager);
    public abstract void initialize(BulletManager bulletManager, Vector2 position);
    public abstract void update(float deltaTime, Vector2 playerPos);
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

    public void setPosition(Vector2 pos)
    {
        position = pos;
    }

    public void hit(float damage)
    {
        health -= damage;

        if (health <= 0)
            isDisposable = true;
    }

    public boolean isDisposable() { return isDisposable; }
}
