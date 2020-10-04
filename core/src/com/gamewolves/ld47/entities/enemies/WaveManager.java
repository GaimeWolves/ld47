package com.gamewolves.ld47.entities.enemies;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.gamewolves.ld47.Main;
import com.gamewolves.ld47.entities.BulletManager;
import com.gamewolves.ld47.entities.Tower;

public class WaveManager
{
    private class Wave
    {
        public int acydrCount;
        public int voxylCount;
        public int magisterCount;

        public Wave(int acydrCount, int voxylCount, int magisterCount)
        {
            this.acydrCount = acydrCount;
            this.voxylCount = voxylCount;
            this.magisterCount = magisterCount;
        }

        public int remaining()
        {
            return acydrCount + voxylCount + magisterCount;
        }

        public Enemy getRandomEnemy(Vector2 posRef)
        {
            if (acydrCount == 0 && voxylCount == 0 && magisterCount == 0)
                return null;

            int type = -1;

            while (type == -1) {
                switch (type = MathUtils.random(2)) {
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
                    case 2:
                        if (magisterCount > 0)
                            magisterCount--;
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
                case 2:
                    posRef.set(Magister.getValidSpawnPosition());
                    return new Magister();
            }

            return null;
        }
    }

    private static final float WAVE_TIME = 30;

    // Initial "balanced" waves
    private Array<Wave> waves = new Array<>(new Wave[] {
            new Wave(3, 3, 0),
            new Wave(3, 3, 0),
            new Wave(4, 4, 0),
            new Wave(3, 3, 1),
            new Wave(4, 4, 1),

            new Wave(5, 5, 1),
            new Wave(5, 5, 1),
            new Wave(6, 6, 1),
            new Wave(6, 6, 2),
            new Wave(6, 8, 2),

            new Wave(6, 8, 2),
            new Wave(7, 8, 2),
            new Wave(7, 10, 2),
            new Wave(7, 10, 3),
            new Wave(7, 10, 3),

            new Wave(8, 12, 3),
            new Wave(8, 12, 3),
            new Wave(9, 12, 3),
            new Wave(9, 14, 4),
            new Wave(10, 14, 4)
    });

    private Array<Enemy> enemies = new Array<>();
    private BulletManager bulletManager;
    private int waveNumber = 0;
    private Wave wave;
    private float time = -10;
    private float spawnTime = 0;
    private int spawned = 0;
    private Label waveLabel, etaLabel, remainingLabel;

    public void loadResources(AssetManager assetManager)
    {

    }

    public void initialize(BulletManager bulletManager)
    {
        this.bulletManager = bulletManager;
        wave = waves.get(waveNumber);
        spawnTime = WAVE_TIME / wave.remaining();

        waveLabel = new Label("WAVE " + (waveNumber + 1), new Label.LabelStyle(Main.get().font, Color.WHITE));
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

    private void nextWave(Tower tower)
    {
        time = -10;
        spawned = 0;
        waveNumber++;

        if (waveNumber < waves.size)
            wave = waves.get(waveNumber);
        else {
            int acydr = (int)(Math.floor((waveNumber + 2) / 4f) + 5);
            int voxyl = (int)(Math.floor((waveNumber + 1) / 3f));
            int magister = (int)(Math.floor((waveNumber + 3) / 4f));
            wave = new Wave(acydr, voxyl, magister);
        }

        if (waveNumber == 5 || waveNumber == 10 || waveNumber == 15)
            tower.unlockNextGun();

        waveLabel.setText("WAVE " + (waveNumber + 1));
        spawnTime = WAVE_TIME / wave.remaining();
    }

    public void update(float deltaTime, Tower tower)
    {
        time += deltaTime;
        if (time > spawnTime * spawned)
        {
            spawned++;
            Vector2 pos = new Vector2();
            Enemy enemy = wave.getRandomEnemy(pos);

            if (enemy != null)
            {
                enemy.loadResources(Main.get().assetManager);
                enemy.initialize(bulletManager, pos, this);
                enemies.add(enemy);
            }
        }

        if (time >= WAVE_TIME)
            nextWave(tower);
        else if (time < 0)
            etaLabel.setText("ETA. " + (int)(-time));
        else
            etaLabel.setText("REM. " + (int)(WAVE_TIME - time));

        remainingLabel.setText(enemies.size + (enemies.size == 1 ? " ENEMY" : " ENEMIES") + " ALIVE");

        waveLabel.setPosition(0, Main.get().Height * .5f - 10, Align.center);
        etaLabel.setPosition(waveLabel.getX(Align.center), waveLabel.getY(Align.center) - waveLabel.getPrefHeight(), Align.center);
        remainingLabel.setPosition(etaLabel.getX(Align.center), etaLabel.getY(Align.center) - etaLabel.getPrefHeight(), Align.center);

        for (int i = 0; i < enemies.size; i++)
        {
            enemies.get(i).update(deltaTime, tower.getPosition());

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

    public void addEnemy(Enemy enemy, Vector2 position)
    {
        enemy.loadResources(Main.get().assetManager);
        enemy.initialize(bulletManager, position, this);
        enemies.add(enemy);
    }

    public Array<Enemy> getEnemies() { return enemies; }
}
