package com.gamewolves.ld47.entities.guns;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.gamewolves.ld47.Main;
import com.gamewolves.ld47.entities.BulletManager;
import com.gamewolves.ld47.entities.Tower;
import com.gamewolves.ld47.entities.projectiles.BasicProjectile;
import com.gamewolves.ld47.graphics.AnimatedSprite;
import com.gamewolves.ld47.physics.Physics;

public class LaserGun extends Gun
{
    private static final float FIRE_COOLDOWN = 4.f;
    private static final float ACTIVE_TIME = 2.f;

    private float cooldownTime = 0;

    private Sprite sprite;
    private AnimatedSprite laserBase, laserBeam;

    private Body body;
    private Sound laserSound;

    @Override
    public void loadResources(AssetManager assetManager)
    {
        laserSound = assetManager.get("sound/laser.wav");

        Texture texture = assetManager.get("cracter/weapons/weapon_4.png");
        Texture baseTexture = assetManager.get("cracter/weapons/laser_start.png");
        Texture beamTexture = assetManager.get("cracter/weapons/laser.png");

        sprite = new Sprite(texture);
        sprite.setOriginCenter();
        sprite.setScale(0.7f);

        laserBase = new AnimatedSprite(baseTexture, 16, 16, 1.f);
        laserBase.setCentered(true);

        laserBeam = new AnimatedSprite(beamTexture, 16, 16, 1.f);
        laserBeam.setCentered(true);
    }

    @Override
    public void initialize(BulletManager bulletManager, float fixedAngle)
    {
        this.fixedAngle = fixedAngle;
        this.bulletManager = bulletManager;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(250, 4);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.isSensor = true;
        fixtureDef.shape = shape;

        body = Physics.getWorld().createBody(bodyDef);
        (body.createFixture(fixtureDef)).setUserData(this);
        body.setActive(false);

        shape.dispose();
    }

    @Override
    public void update(float deltaTime, Vector2 towerPos)
    {
        cooldownTime = (cooldownTime + deltaTime) % FIRE_COOLDOWN;

        if (cooldownTime < ACTIVE_TIME)
        {
            if (body.isActive())
                body.setActive(false);
        }
        else
        {
            if (!body.isActive())
            {
                body.setActive(true);
                laserSound.play(0.125f);
            }
        }

        towerPosition = towerPos;

        sprite.setOriginBasedPosition(towerPosition.x, towerPosition.y);
        sprite.setRotation(actualAngle);

        shotOffset = new Vector2(1, 0);
        shotOffset.setAngle(actualAngle);
        shotOffset.scl(8);

        laserBase.setRotation(actualAngle);
        laserBeam.setRotation(actualAngle);

        laserBase.update(deltaTime);
        laserBeam.update(deltaTime);

        body.setTransform(towerPosition, actualAngle * MathUtils.degRad);
    }

    @Override
    public void render(SpriteBatch batch)
    {
        sprite.draw(batch);

        if (cooldownTime > ACTIVE_TIME) {
            laserBase.setScale(1, 1);
            laserBase.setPosition(towerPosition.cpy().add(shotOffset.cpy().setLength(8 + laserBase.getWidth() * .5f)));
            laserBase.render(batch);

            for (int i = 1; i < 50; i++) {
                float offset = laserBeam.getWidth() * i;
                laserBeam.setPosition(towerPosition.cpy().add(shotOffset.cpy().setLength(8 + laserBase.getWidth() * .5f + offset)));
                laserBeam.render(batch);
            }

            laserBase.setScale(-1, 1);
            laserBase.setPosition(towerPosition.cpy().add(shotOffset.cpy().setLength(8 + laserBase.getWidth() * .5f).scl(-1)));
            laserBase.render(batch);

            for (int i = 1; i < 50; i++) {
                float offset = laserBeam.getWidth() * i;
                laserBeam.setPosition(towerPosition.cpy().add(shotOffset.cpy().setLength(8 + laserBase.getWidth() * .5f + offset).scl(-1)));
                laserBeam.render(batch);
            }
        }
    }

    @Override
    public void dispose(AssetManager assetManager)
    {

    }
}
