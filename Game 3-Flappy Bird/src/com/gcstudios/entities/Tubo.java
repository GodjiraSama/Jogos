package com.gcstudios.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.gcstudios.main.Game;

import java.awt.*;

public class Tubo extends Entity{
	
	public Tubo(double x, double y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
	}

	public void tick()
	{
		x--;
		//se j� tivermos passado pelo tubo ele � removido
		if(x + width <= 0)
		{
			//Para aumentar a nossa pontua��o a cada tubo que passarmos
			Game.score+=0.5;
			Game.entities.remove(this);
			return;
		}
	}
	
	
	public void render(Graphics g)
	{
		if(sprite != null)
		{
			g.drawImage(sprite, this.getX(), this.getY(), width, height, null);
		}
		else
		{
			g.setColor(Color.green);
			g.fillRect(this.getX(), this.getY(), width, height);
		}
	}

}
