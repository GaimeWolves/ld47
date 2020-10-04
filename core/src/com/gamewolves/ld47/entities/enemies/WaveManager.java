package com.gamewolves.ld47.entities.enemies;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.gamewolves.ld47.Main;
import com.gamewolves.ld47.entities.BulletManager;

public class WaveManager
{
    private class Wave
    {
        public int acydrCount;
        public int voxylCount;

        public Wave(int acydrCount, int voxylCount) {
            this.acydrCount = acydrCount;
            this.voxylCount = voxylCount;
        }

        public int remaining()
        {
            return acydrCount + voxylCount;
        }

        public Enemy getRandomEnemy(Vector2 posRef)
        {
            if (acydrCount == 0 && voxylCount == 0)
                return null;

            int type = -1;

            while (type == -1) {
                switch (type = MathUtils.random(1)) {
                    case 0:
                        if (acydrCount > 0)
                            acydrCount--;
                        else
                            type = -1;
                        break;
                    case 1:
                        if (voxylCount > 0)
                            voxylCount--;
                        else
                            type = -1;
                        break;
                }
            }

            switch (type)
            {
                case 0:
                    posRef.set(Acydr.getValidSpawnPosition());
                    return new Acydr();
                case 1:
                    posRef.set(Voxyl.getValidSpawnPosition());
                    return new Voxyl();
            }

            return null;
        }
    }

    private static final float WAVE_TIME = 30;

    private Array<Wave> waves = new Array<>(new Wave[] {
            new Wave(0, 10),
            new Wave(0, 10),
            new Wave(0, 10),
            new Wave(0, 10),
            new Wave(0, 10)
    });

    private Array<Enemy> enemies = new Array<>();
    private BulletManager bulletManager;
    private int wave = 0;
    private float time = 0;
    private float spawnTime = 0;
    private int spawned = 0;
    private Label waveLabel, etaLabel, remainingLabel;

    public void loadResources(AssetManager assetManager)
    {

    }

    public void initialize(BulletManager bulletManager)
    {
        this.bulletManager = bulletManager;
        spawnTime = WAVE_TIME / waves.get(wave).remaining();

        waveLabel = new Label("WAVE 1", new Label.LabelStyle(Main.get().font, Color.WHITE));
        waveLabel.setAlignment(Align.center);
        waveLabel.setFontScale(0.25f);
        waveLabel.setPosition(0, Main.get().Height * .5f - 10, Align.center);

        etaLabel = new Label("REM. 30S", new Label.LabelStyle(Main.get().font, Color.WHITE));
        etaLabel.setAlignment(Align.center);
        etaLabel.setFontScale(0.15f);
        etaLabel.setPosition(waveLabel.getX(Align.center), waveLabel.getY(Align.center) - waveLabel.getPrefHeight(), Align.center);

        remainingLabel = new Label("5 ENEMIES ALIVE", new Label.LabelStyle(Main.get().font, Color.WHITE));
        remainingLabel.setAlignment(Align.center);
        remainingLabel.setFontScale(0.15f);
        remainingLabel.setPosition(etaLabel.getX(Align.center), etaLabel.getY(Align.center) - etaLabel.getPrefHeight(), Align.center);
    }

    private void nextWave()
    {
        time = 0;
        spawned = 0;
        wave++;
        waveLabel.setText("WAVE " + (wave + 1));
        spawnTime = WAVE_TIME / waves.get(wave).remaining();
    }

    public void update(float deltaTime, Vector2 playerPos)
    {
        time += deltaTime;
        if (time > spawnTime * spawned)
        {
            spawned++;
            Vector2 pos = new Vector2();
            Enemy enemy = waves.get(wave).getRandomEnemy(pos);

            if (enemy != null)
            {
                enemy.loadResources(Main.get().assetManager);
                enemy.initialize(bulletManager, pos);
                enemies.add(enemy);
            }
        }

        if (time >= WAVE_TIME + 10)
            nextWave();
        else if (time > WAVE_TIME)
            etaLabel.setText("ETA. " + (int)(WAVE_TIME + 10 - time));
        else
            etaLabel.setText("REM. " + (int)(WAVE_TIME - time));

        remainingLabel.setText(enemies.size + (enemies.size == 1 ? " ENEMY" : " ENEMIES") + " ALIVE");

        waveLabel.setPosition(0, Main.get().Height * .5f - 10, Align.center);
        etaLabel.setPosition(waveLabel.getX(Align.center), waveLabel.getY(Align.center) - waveLabel.getPrefHeight(), Align.center);
        remainingLabel.setPosition(etaLabel.getX(Align.center), etaLabel.getY(Align.center) - etaLabel.getPrefHeight(), Align.center);

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

    public void renderUI(SpriteBatch batch)
    {
        waveLabel.draw(batch, 1);
        etaLabel.draw(batch, 1);
        remainingLabel.draw(batch, 1);
    }

    public void dispose(AssetManager assetManager)
    {
        for (Enemy enemy : enemies)
            enemy.dispose(assetManager);

        enemies.clear();
    }

    public Array<Enemy> getEnemies() { return enemies; }
}
