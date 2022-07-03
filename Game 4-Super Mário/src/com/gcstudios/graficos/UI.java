package com.gcstudios.graficos;

import java.awt.Font;
import java.awt.Graphics;

import com.gcstudios.entities.Player;
import com.gcstudios.main.Game;

public class UI {
	public void render(Graphics g) {
		 
		g.setFont(new Font("Arial",Font.BOLD,17));
		g.drawString("Moedas: "+Player.currentCoin+"/" + Player.maxCoin,(Game.WIDTH * Game.SCALE) - 130,30);
	}
}