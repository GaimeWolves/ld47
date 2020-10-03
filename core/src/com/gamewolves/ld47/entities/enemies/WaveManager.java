package com.gamewolves.ld47.entities.enemies;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.gamewolves.ld47.Main;
import com.gamewolves.ld47.entities.BulletManager;

public class WaveManager
{
    private Array<Enemy> enemies = new Array<>();
    private BulletManager bulletManager;

    public void loadResources(AssetManager assetManager)
    {

    }

    public void initialize(BulletManager bulletManager)
    {
        this.bulletManager = bulletManager;

        enemies.add(new Acydr());
        enemies.get(0).loadResources(Main.get().assetManager);
        enemies.get(0).initialize(bulletManager, new Vector2(320, 0));
    }

    public void update(float deltaTime, Vector2 playerPos)
    {
        for (int i = 0; i < enemies.size; i++)
        {
            enemies.get(i).update(deltaTime, playerPos);

            if (enemies.get(i).isDisposable)
                enemies.removeIndex(i--).dispose(Main.get().assetManager);
        }
    }

    public void render(SpriteBatch batch, Vector2 playerPos)
    {
        for (Enemy enemy : enemies)
            enemy.render(batch, playerPos);
    }

    public void dispose(AssetManager assetManager)
    {
        for (Enemy enemy : enemies)
            enemy.dispose(assetManager);

        enemies.clear();
    }
}
