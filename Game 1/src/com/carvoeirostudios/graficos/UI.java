package com.carvoeirostudios.graficos;

import java.awt.*;

import com.carvoeirostudios.main.Game;

public class UI {
	public void render(Graphics g) {
		// Barra de Vida
		g.setColor(Color.red);
		g.fillRect(8, 4, 50, 8);
		g.setColor(Color.green);
		g.fillRect(8, 4, (int) ((Game.player.life / Game.player.maxlife) * 50), 8);
		g.setColor(Color.white);
		g.setFont(new Font("calibri", Font.BOLD, 8));
		g.drawString((int) Game.player.life + "/" + (int) Game.player.maxlife, 60, 10);
	}
}
