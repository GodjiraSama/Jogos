package com.gcstudios.entities;

import java.awt.image.BufferedImage;
import java.awt.*;
import com.gcstudios.main.Game;
import com.gcstudios.world.World;

public class Player extends Entity{
	
	public boolean isPressed = false;
	
	public Player(int x, int y, int width, int height,double speed,BufferedImage sprite) {
		super(x, y, width, height,speed,sprite);
	}
	
	//Para fazer subir o pássaro quando carregamos no espaço
	public void tick(){
		depth = 2;
		if(!isPressed)
		{
			y+=2;
		}
		else
		{
			if(y > 0)
			{
				y-=2;
			}
		}
		
		//Caso passemos do tamanho da janela e caso vamos contra os tubos dá Game Over
		if(y > Game.HEIGHT)
		{
			World.restartGame();
		}
		
		for(int i = 0; i < Game.entities.size(); i++)
		{
			Entity e = Game.entities.get(i);
			if(e != this)
			{
				if(Entity.isColidding(this, e))
				{
					//Game Over
					World.restartGame();
					return;
				}
			}
		}
	}
	
	//Para renderizar quando o pássaro está a cair e quando não está
	public void render(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		if(!isPressed)
		{
			g2.rotate(Math.toRadians(20), this.getX() + width/2 ,this.getY() + height/2);
			g2.drawImage(sprite, this.getX(), this.getY(), null);
			g2.rotate(Math.toRadians(-20), this.getX() + width/2 ,this.getY() + height/2);
		}
		else
		{
			g2.rotate(Math.toRadians(-20), this.getX() + width/2 ,this.getY() + height/2);
			g2.drawImage(sprite, this.getX(), this.getY(), null);
			g2.rotate(Math.toRadians(20), this.getX() + width/2 ,this.getY() + height/2);
		}
	super.render(g);
		g2.rotate(-30,this.getX()/2,this.getY()/2);
	}
}