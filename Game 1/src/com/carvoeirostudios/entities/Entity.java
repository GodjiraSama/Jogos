package com.carvoeirostudios.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import com.carvoeirostudios.main.Game;
import com.carvoeirostudios.world.Camera;

public class Entity {
	
	protected double x;
	protected double y;
	protected int width;
	protected int height;

	private BufferedImage sprite;

	public Entity(int x, int y, int width, int height, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
	}

	public void setX(int newX) {
		this.x = newX;
	}

	public void setY(int newY) {
		this.y = newY;
	}

	public int getX() {
		return (int) this.x;
	}

	public int getY() {
		return (int) this.y;
	}

	public int getWidth() {
		return (int) this.width;
	}

	public int getHeight() {
		return (int) this.height;
	}

	public void tick() {

	}

	public static boolean isCollidding(Entity e1, Entity e2) {
		Rectangle e1Mask = new Rectangle(e1.getX(),e1.getY(),width,height); 
		Rectangle e2Mask = new Rectangle(e2.getX(),e2.getY(),width,height);

		return e1Mask.intersects(e2Mask);
	}

	public void render(Graphics g) {
		g.drawImage(sprite, this.getX() - Camera.x, this.getY() - Camera.y, null);
	}
}