package com.gamewolves.ld47.entities.guns;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.gamewolves.ld47.Main;
import com.gamewolves.ld47.entities.BulletManager;
import com.gamewolves.ld47.entities.projectiles.BasicProjectile;
import com.gamewolves.ld47.entities.projectiles.HomingProjectile;

public class HomingGun extends Gun
{
    private static final float FIRE_COOLDOWN = .5f;

    private float cooldownTime = 0;

    private Sprite sprite;
    private Sound shootSound;

    @Override
    public void loadResources(AssetManager assetManager)
    {
        shootSound = assetManager.get("sound/shoot.wav");

        sprite = new Sprite((Texture) assetManager.get("cracter/weapons/weapon_2.png"));
        sprite.setOriginCenter();
        sprite.setScale(0.7f);
    }

    @Override
    public void initialize(BulletManager bulletManager, float fixedAngle)
    {
        this.fixedAngle = fixedAngle;
        this.bulletManager = bulletManager;
    }

    @Override
    public void update(float deltaTime, Vector2 towerPos)
    {
        cooldownTime += deltaTime;
        towerPosition = towerPos;

        sprite.setOriginBasedPosition(towerPosition.x, towerPosition.y);
        sprite.setRotation(actualAngle);

        shotOffset = new Vector2(1, 0);
        shotOffset.setAngle(actualAngle);
        shotOffset.scl(8);

        if (cooldownTime > FIRE_COOLDOWN)
        {
            cooldownTime -= FIRE_COOLDOWN;

            HomingProjectile projectile = new HomingProjectile();
            projectile.loadResources(Main.get().assetManager);
            projectile.initialize(bulletManager, Vector2.X.cpy().setAngle(actualAngle), towerPosition.cpy().add(shotOffset), true);

            projectile = new HomingProjectile();
            projectile.loadResources(Main.get().assetManager);
            projectile.initialize(bulletManager, Vector2.X.cpy().setAngle(actualAngle).scl(-1), towerPosition.cpy().add(shotOffset.cpy().scl(-1)), true);

            shootSound.play(0.125f);
        }
    }

    @Override
    public void render(SpriteBatch batch)
    {
        sprite.draw(batch);
    }

    @Override
    public void dispose(AssetManager assetManager)
    {

    }
}
