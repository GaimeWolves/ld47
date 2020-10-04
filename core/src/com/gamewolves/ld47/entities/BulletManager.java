package com.gamewolves.ld47.entities;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.gamewolves.ld47.Main;
import com.gamewolves.ld47.entities.enemies.Enemy;
import com.gamewolves.ld47.entities.projectiles.Projectile;

public class BulletManager
{
    private Array<Projectile> projectiles = new Array<>();

    public void addProjectile(Projectile projectile)
    {
        projectiles.add(projectile);
    }

    public void update(float deltaTime, Vector2 towerPos, Array<Enemy> enemies)
    {
        for (int i = 0; i < projectiles.size; i++)
        {
            projectiles.get(i).update(deltaTime, towerPos, enemies);

            // Check bounds
            Vector2 pos = projectiles.get(i).getPosition();
            if (pos.x < -Main.get().Width - 10 || pos.x > Main.get().Width + 10 || pos.y < -Main.get().Height - 10 || pos.y > Main.get().Height + 10)
                projectiles.removeIndex(i--).dispose(Main.get().assetManager);
        }

        for (int i = 0; i < projectiles.size; i++)
            if (projectiles.get(i).isDisposable())
                projectiles.removeIndex(i--).dispose(Main.get().assetManager);
    }

    public void render(SpriteBatch batch)
    {
        for (Projectile projectile : projectiles)
            projectile.render(batch);
    }

    public void dispose(AssetManager assetManager)
    {
        for (Projectile projectile : projectiles)
            projectile.dispose(assetManager);

        projectiles.clear();
    }
}
