package com.gamewolves.ld47.entities.guns;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.gamewolves.ld47.Main;
import com.gamewolves.ld47.entities.BulletManager;
import com.gamewolves.ld47.entities.Tower;
import com.gamewolves.ld47.entities.projectiles.BasicProjectile;

public class BasicGun extends Gun
{
    private static final float FIRE_COOLDOWN = 1.f;

    private float cooldownTime = 0;

    @Override
    public void loadResources(AssetManager assetManager) {

    }

    @Override
    public void initialize(BulletManager bulletManager, float fixedAngle)
    {
        this.fixedAngle = fixedAngle;
        this.bulletManager = bulletManager;
    }

    @Override
    public void update(float deltaTime)
    {
        cooldownTime += deltaTime;

        if (cooldownTime > FIRE_COOLDOWN)
        {
            cooldownTime -= FIRE_COOLDOWN;

            BasicProjectile projectile = new BasicProjectile();
            projectile.loadResources(Main.get().assetManager);
            projectile.initialize(bulletManager, Vector2.X.cpy().setAngle(actualAngle), shotPosition, true);
        }
    }

    @Override
    public void render(SpriteBatch batch)
    {
        Vector2 towerPos = new Vector2(1, 0);
        towerPos.setLength(Tower.RADIUS);
        towerPos.setAngle(towerAngle);

        Vector2 gunAnchor = new Vector2(1, 0);
        gunAnchor.setLength(10);
        gunAnchor.setAngle(actualAngle);
        gunAnchor.add(towerPos);

        Vector2 gunDir = new Vector2(1, 0);
        gunDir.setAngle(actualAngle);
        shotPosition = gunAnchor.cpy().add(gunDir.scl(10));

        Main.get().shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        Main.get().shapeRenderer.setColor(1, 0 ,0 ,1);
        Main.get().shapeRenderer.line(gunAnchor, shotPosition);
        Main.get().shapeRenderer.setColor(1, 1 ,1 ,1);
        Main.get().shapeRenderer.end();
    }

    @Override
    public void dispose(AssetManager assetManager)
    {

    }
}
