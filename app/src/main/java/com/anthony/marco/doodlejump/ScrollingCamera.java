package com.anthony.marco.doodlejump;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by marco on 4-1-2017.
 */

public class ScrollingCamera {
    private ArrayList<Entity> entities;
    private float x, y;
    private float xOffset, yOffset;
    private Rect bounds;

    public ScrollingCamera(Rect bounds) {
        entities = new ArrayList<>();
        this.bounds = bounds;
    }

    public void update(){
        for (Entity entity : entities) {
            entity.update();
        }

        //yOffset -= 2;
    }

    public void draw(Canvas canvas) {
        int totalDrawn = 0;
        for (Entity entity : entities) {
            if (isInScreen(entity)) {
                totalDrawn++;
                entity.draw(this, canvas);
            }
        }

        //Log.i("ScrollingCamera", "Total drawn: " + totalDrawn);
    }

    public void setEntities(ArrayList<Entity> entities) {
        this.entities = entities;
    }

    private boolean isInScreen(Entity entity) {
        float relativeXPos = getRelativeX(entity.getX());
        float relativeYPos = getRelativeY(entity.getY());

        return (relativeXPos >= bounds.left && relativeXPos + entity.getWidth() <= bounds.right
                && relativeYPos >= bounds.top && relativeYPos + entity.getHeight() <= bounds.bottom);
    }

    public float getRelativeX(float xPos){
        return xPos - xOffset;
    }

    public float getRelativeY(float yPos){
        return yPos - yOffset;
    }
}
