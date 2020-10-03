package com.gamewolves.ld47.entities.projectiles;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.gamewolves.ld47.Main;
import com.gamewolves.ld47.entities.BulletManager;

public class BasicProjectile extends Projectile
{
    private static final float ACCELERATION = 100;

    @Override
    public void loadResources(AssetManager assetManager) {

    }

    @Override
    public void initialize(BulletManager bulletManager, Vector2 direction, Vector2 position, boolean isPlayerShot)
    {
        super.initialize(bulletManager, direction, position, isPlayerShot);

        velocity = direction.cpy();
        velocity.setLength(ACCELERATION);
    }

    @Override
    public void update(float deltaTime)
    {
        super.update(deltaTime);
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
