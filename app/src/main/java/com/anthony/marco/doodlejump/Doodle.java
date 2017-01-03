package com.anthony.marco.doodlejump;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

/**
 * Created by anthony on 3-1-2017.
 */

public class Doodle extends Entity {
    private int velocityX;
    private int velocityY;

    public Doodle(int x, int y, int height, int width, Bitmap image, int velocityX, int velocityY) {
        super(x, y, height, width, image);
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    public boolean checkCollision(ArrayList<Entity> entities) {
        return false;
    }

    public void handleInput() {
        //TODO: handle input..
    }

    @Override
    public void update() {
        super.update();

        int newX = this.getX() + velocityX;
        int newY = this.getY() + velocityY;

        setX(newX);
        setY(newY);

        // TODO: Add collision checks
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.YELLOW);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(getX(), getY(), getWidth(), paint);
    }
}
