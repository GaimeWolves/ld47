package com.gamewolves.ld47.entities.guns;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.gamewolves.ld47.entities.BulletManager;

public abstract class Gun
{
    protected float fixedAngle;
    protected float actualAngle;
    protected float towerAngle;
    protected Vector2 shotPosition;
    protected BulletManager bulletManager;

    public abstract void loadResources(AssetManager assetManager);
    public abstract void initialize(BulletManager bulletManager, float fixedAngle);
    public abstract void update(float deltaTime);
    public abstract void render(SpriteBatch batch);
    public abstract void dispose(AssetManager assetManager);

    public void calcActualAngle(float towerAngle)
    {
        this.towerAngle = towerAngle;
        actualAngle = towerAngle + fixedAngle;
    }
}
