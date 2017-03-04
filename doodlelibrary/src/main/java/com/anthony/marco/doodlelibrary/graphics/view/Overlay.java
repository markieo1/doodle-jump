package com.anthony.marco.doodlelibrary.graphics.view;

import android.content.Context;
import android.graphics.Canvas;

import com.anthony.marco.doodlelibrary.graphics.view.ui.Button;
import com.anthony.marco.doodlelibrary.graphics.view.ui.GuiItem;
import com.anthony.marco.doodlelibrary.logic.scene.Scene;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marco on 25-2-2017.
 */

public abstract class Overlay {
	protected Scene scene;

	protected List<GuiItem> guiItems;

	public Overlay(Scene scene) {
		this.guiItems = new ArrayList<>();
		this.scene = scene;
	}

	public void loadResources(Context context) {
	}

	public void update(double dt) {
		for (GuiItem guiItem : guiItems) {
			guiItem.update(dt);
		}
	}

	public void draw(Canvas canvas) {
		for (GuiItem guiItem : guiItems) {
			guiItem.draw(canvas);
		}
	}

	public void onTouch(float x, float y) {
		for (GuiItem guiItem : guiItems) {
			if (guiItem instanceof Button) {
				if (((Button) guiItem).getDrawingRectangle().contains(x, y))
					((Button) guiItem).click();
			}
		}
	}
}
