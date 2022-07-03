package com.gcstudios.entities;

import java.awt.image.BufferedImage;
import java.awt.*;
import com.gcstudios.world.World;

public class Enemy extends Entity {

	public boolean right = true, left = false;
	
	public int vida = 1;

	public Enemy(double x, double y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
	}

	// Movimento do inimigo
	public void tick() {
		if (World.isFree((int)x,(int)(y+1))) {
			y+=1;
		} 
		
			if (right && World.isFree((int)(x+speed), (int)y)) {
					x+=speed;
					if (World.isFree((int)(x+16),(int)y+1)) {
						right = false;
						left = true;
					}
				} else {
					right = false;
					left = true;
				}
			
			if (left && World.isFree((int)(x-speed), (int)y)) {
				x-=speed;
				if (World.isFree((int)(x-16), (int)y+1)) {
					right = true;
					left = false;
				}
			} else {
				right = true;
				left = false;
			}
		}

	public void render(Graphics g) {
		if (right)
			sprite = Entity.Enemy1_RIGHT;
		else if (left)
			sprite = Entity.Enemy1_LEFT;
		super.render(g);
	}
}