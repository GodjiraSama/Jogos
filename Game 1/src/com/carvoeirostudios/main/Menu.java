package com.carvoeirostudios.main;

import java.awt.*;

public class Menu {

	public String[] options = { "novo jogo", "carregar jogo", "sair para o ambiente de trabalho" };

	public int currentOption = 0;

	public int maxOption = options.length - 1;

	public boolean up, down, enter;

	public boolean pause = false;

	// indicação da opção máxima e mínima possível no menu
	public void tick() {
		if (up) {
			up = false;
			currentOption--;
			if (currentOption < 0)
				currentOption = maxOption;
		}
		if (down) {
			down = false;
			currentOption++;
			if (currentOption > maxOption)
				currentOption = 0;
		}

		if (enter) {
			enter = false;
			// Caso selecione novo jogo
			if (options[currentOption] == "novo jogo" || options[currentOption] == "continuar") {
				Game.gameState = "Normal";
				//Som ao começar um novo jogo
				Sound.Newgame.play();
				pause = false;
			}
			// Caso selecione carregar jogo
			else if (options[currentOption] == "carregar jogo") {
				// Game.gameState = "Normal";
				Sound.select.play();
			}
			// Caso selecione sair para o ambiente de trabalho
			else if (options[currentOption] == "sair para o ambiente de trabalho") {
				Sound.select.play();
				System.exit(1);
			}
		}
	}

	public void render(Graphics g) {
		// Criação do Fundo do menu

		// Criação do backgroud do menu
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(new Color(0, 0, 0, 100));
		g2.fillRect(0, 0, Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE);

		// Criação do Título do Jogo no menu
		g.setColor(Color.blue);
		g.setFont(new Font("calibri", Font.BOLD, 27));
		g.drawString("Meu 1º Jogo", (Game.WIDTH * Game.SCALE) - 880 / 2, (Game.HEIGHT * Game.SCALE) - 870 / 2);

		// Criação das opções de escolha no menu
		g.setColor(Color.white);
		g.setFont(new Font("calibri", Font.BOLD, 18));

		if (pause == false)
			g.drawString("Novo Jogo", (Game.WIDTH * Game.SCALE) - 1340 / 2, +250);
		else {
			g.drawString("Continuar", (Game.WIDTH * Game.SCALE) - 1340 / 2, +250);
		}
		g.drawString("Carregar Jogo", (Game.WIDTH * Game.SCALE) - 1340 / 2, +350);
		g.drawString("Sair para o ambiente de trabalho", (Game.WIDTH * Game.SCALE) - 1340 / 2, +450);

		// Seta colocada na opção atual
		if (options[currentOption] == "novo jogo") {
			g.drawString(">", (Game.WIDTH * Game.SCALE) - 1370 / 2, +250);
		} else if (options[currentOption] == "carregar jogo") {
			g.drawString(">", (Game.WIDTH * Game.SCALE) - 1370 / 2, +350);
		} else if (options[currentOption] == "sair para o ambiente de trabalho") {
			g.drawString(">", (Game.WIDTH * Game.SCALE) - 1370 / 2, +450);
		}
	}

}
