package com.gamewolves.ld47.entities.enemies;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.gamewolves.ld47.Main;
import com.gamewolves.ld47.entities.BulletManager;

public class WaveManager
{
    private class Wave
    {
        public int acydrCount;

        public Wave(int acydrCount) {
            this.acydrCount = acydrCount;
        }

        public int remaining()
        {
            return acydrCount;
        }

        public Enemy getRandomEnemy()
        {
            if (acydrCount == 0)
                return null;

            int type = -1;

            while (type == -1) {
                switch (type = MathUtils.random(1)) {
                    case 0:
                        if (acydrCount > 0)
                            acydrCount--;
                        else
                            type = -1;
                }
            }

            switch (type)
            {
                case 0:
                    return new Acydr();
            }

            return null;
        }
    }

    private static final float WAVE_TIME = 30;

    private Array<Wave> waves = new Array<>(new Wave[] {
            new Wave(5),
            new Wave(6),
            new Wave(7),
            new Wave(8),
            new Wave(9)
    });

    private Array<Enemy> enemies = new Array<>();
    private BulletManager bulletManager;
    private int wave = 0;
    private float time = 0;
    private float spawnTime = 0;
    private int spawned = 0;

    public void loadResources(AssetManager assetManager)
    {

    }

    public void initialize(BulletManager bulletManager)
    {
        this.bulletManager = bulletManager;
        spawnTime = WAVE_TIME / waves.get(wave).remaining();
    }

    private void nextWave()
    {
        time = 0;
        spawned = 0;
        wave++;
        spawnTime = WAVE_TIME / waves.get(wave).remaining();
    }

    public void update(float deltaTime, Vector2 playerPos)
    {
        time += deltaTime;
        if (time > spawnTime * spawned)
        {
            spawned++;
            Enemy enemy = waves.get(wave).getRandomEnemy();

            if (enemy != null)
            {
                enemy.loadResources(Main.get().assetManager);
                enemy.initialize(bulletManager, new Vector2(400, 400).rotate(MathUtils.random(360)));
                enemies.add(enemy);
            }
        }

        if (time >= WAVE_TIME + 10)
            nextWave();

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
