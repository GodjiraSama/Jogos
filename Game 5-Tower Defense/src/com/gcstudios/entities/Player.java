package com.gcstudios.entities;

import java.awt.image.BufferedImage;

public class Player extends Entity{

	public Player(int x, int y, int width, int height,double speed,BufferedImage sprite) {
		super(x, y, width, height,speed,sprite);
	}
	
	public void tick(){
	}
}