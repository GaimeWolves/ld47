package com.gamewolves.ld47.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.gamewolves.ld47.Main;
import com.gamewolves.ld47.input.InputHandler;

/**
 * Yoinked from TheCodingTrain
 */
public class Crane
{
    private class Segment
    {
        public Vector2 a;
        public float angle = 0;
        public float len;
        public Vector2 b = new Vector2();

        public Segment(float x, float y, float len)
        {
            a = new Vector2(x, y);
            this.len = len;
            calculateB();
        }

        public Segment(Segment parent, float len)
        {
            a = parent.b.cpy();
            this.len = len;
            calculateB();
        }

        public void follow(Segment child)
        {
            float targetX = child.a.x;
            float targetY = child.a.y;
            follow(targetX, targetY);
        }

        public void follow(float tx, float ty)
        {
            Vector2 target = new Vector2(tx, ty);
            Vector2 dir = target.cpy().sub(a);
            angle = dir.angle();
            dir.setLength(len);
            dir.scl(-1);
            a = target.add(dir);
        }

        public void setA(Vector2 pos)
        {
            a = pos.cpy();
            calculateB();
        }

        public void calculateB()
        {
            float dx = len * (float)Math.cos(angle * MathUtils.degRad);
            float dy = len * (float)Math.sin(angle * MathUtils.degRad);
            b.set(a.x+dx, a.y+dy);
        }

        public void update()
        {
            calculateB();
        }
    }

    private static final float LENGTH = 70;

    private Segment[] segments = new Segment[3];
    private Vector2 head = new Vector2();
    private Vector2 base = new Vector2();

    public void loadResources(AssetManager assetManager)
    {

    }

    public void initialize()
    {
        segments[0] = new Segment(base.x, base.y, LENGTH);
        for (int i = 1; i < segments.length; i++)
            segments[i] = new Segment(segments[i - 1], LENGTH);
    }

    public void update(float deltaTime)
    {
        Vector2 pos = InputHandler.get().getMousePositionInWorld();

        int total = segments.length;
        Segment end = segments[total - 1];
        end.follow(pos.x, pos.y);
        end.update();

        for (int i = total - 2; i >= 0; i--)
        {
            segments[i].follow(segments[i + 1]);
            segments[i].update();
        }

        segments[0].setA(base);

        for (int i = 1; i < total; i++)
            segments[i].setA(segments[i - 1].b);

        head = end.b.cpy();
    }

    public void render(SpriteBatch batch)
    {
        Main.get().shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (Segment segment : segments)
            Main.get().shapeRenderer.line(segment.a, segment.b);
        Main.get().shapeRenderer.circle(head.x, head.y, 10);
        Main.get().shapeRenderer.end();
    }

    public void dispose(AssetManager assetManager)
    {

    }
}
