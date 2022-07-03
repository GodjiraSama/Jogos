package com.gcstudios.entities;

import java.awt.image.BufferedImage;
import java.awt.*;

import com.gcstudios.main.Game;
import com.gcstudios.main.Sound;
import com.gcstudios.world.Camera;
import com.gcstudios.world.World;

public class Player extends Entity {

	public boolean right, up, left, down;

	// Declaração da variável relacionada com o pac man virado para a esquerda
	public BufferedImage sprite_left;

	// Declaração da variável encarregue de analisar se estou a ir para a esquerda
	// ou para a direita
	public int lastX = 1;

	public Player(int x, int y, int width, int height, int speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);

		// Indicação da posição da sprite do pac man virado para a esquerda
		sprite_left = Game.spritesheet.getSprite(48, 0, 16, 16);
	}

	public void tick() {
		depth = 1;

		if (right && World.isFree((int) (x + speed), this.getY())) {
			x += speed;
			lastX = 1;

		} else if (left && World.isFree((int) (x - speed), this.getY())) {
			x -= speed;
			lastX = -1;
		}

		if (up && World.isFree(this.getX(), (int) (y - speed))) {
			y -= speed;

		} else if (down && World.isFree(this.getX(), (int) (y + speed))) {
			y += speed;

		}

		verificarPegaCereja();
		
		if(Game.frutas_contagens == Game.frutas_atual)
		{
			//System.out.println("Ganhou o jogo");
			World.restartGame();
		}
	}

	// Verificação da colisão com as cerejas e remoção das mesmas
	public void verificarPegaCereja() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity current = Game.entities.get(i);
			if (current instanceof Cereja) {
				if (Entity.isColidding(this, current)) {
					Game.frutas_atual++;
					Sound.eat.play();
					Game.entities.remove(i);
					return;
				}
			}
		}
	}

	public void render(Graphics g) {
		// Renderizar as sprites de acordo se vou para a direita ou para a esquerda
		// Se última direção for a direita:
		if (lastX == 1) {
			super.render(g);
			// Caso seja a esquerda:
		} else {
			g.drawImage(sprite_left, this.getX() - Camera.x, this.getY() - Camera.y, null);
		}
	}
}