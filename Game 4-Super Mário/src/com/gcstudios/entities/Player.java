package com.gcstudios.entities;

import java.awt.image.BufferedImage;
import java.awt.*;

import com.gcstudios.main.Game;
import com.gcstudios.world.Camera;
import com.gcstudios.world.World;

public class Player extends Entity {
	public boolean right, left;

	public static int life = 1;
	public static int currentCoin = 0;
	public static int maxCoin=0;
	
	public int dir = 1;
	private double gravity = 2;

	// Criação das variáveis referentes ao saltar
	public boolean jump = false;
	public boolean isJumping = false;
	public int jumpHeight = 40;
	public int jumpFrames = 0;

	// criação das variáveis referentes á animação
	private int framesAnimation = 0;
	private int maxFrames = 15;
	private int maxSprite = 2;
	private int curSprite = 0;

	public Player(int x, int y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
	}

	public void tick() {
		depth = 2;

		// Implementação da gravidade
		if (World.isFree((int) x, (int) (y + gravity)) && isJumping == false) {
			y += gravity;

			// Para ativar um novo pulo quando cair em cima do inimigo
			for (int i = 0; i < Game.entities.size(); i++) {
				Entity e = Game.entities.get(i);
				if (e instanceof Enemy) {
					if (Entity.isColidding(this, e)) {
						isJumping = true;
						jumpHeight = 32;

						// Remover vida do inimigo
						((Enemy) e).vida--;

						// Se a vida do inimigo cheagar a zero, remove-lo
						if (((Enemy) e).vida == 0) {
							Game.entities.remove(i);
							break;
						}
					}
				}
			}
		}

		// Se andar para a direita e ativar colisão
		if (right && World.isFree((int) (x + speed), (int) (y))) {
			x += speed;
			dir = 1;
		}
		// Se andar para a esquerda e ativar colisão
		else if (left && World.isFree((int) (x - speed), (int) (y))) {
			x -= speed;
			dir = -1;
		}
	}

	// Renderização da sprite do player de acordo para o lado em que está virado
	public void render(Graphics g) {

		// Animação (funciona mesmo que o boneca esteja parado)
		framesAnimation++;
		if (framesAnimation == maxFrames) {
			curSprite++;
			framesAnimation = 0;

			if (curSprite == maxSprite) {
				curSprite = 0;
			}
		}

		if (dir == 1) {
			sprite = Entity.Player_R[curSprite];
		} else if (dir == -1) {

			sprite = Entity.Player_L[curSprite];
		}
		super.render(g);

		// Sequência de verificações para executar o salto

		// Verificação caso o player esteja no ar
		if (jump) {
			if (!World.isFree(this.getX(), this.getY() + 1)) {
				isJumping = true;
			} else {
				jump = false;
			}
		}

		// Passos seguintes se o jogador tiver saltado
		if (isJumping) {
			if (World.isFree(this.getX(), this.getY() - 2)) {
				y -= 2;
				jumpFrames += 2;

				if (jumpFrames == jumpHeight) {
					isJumping = false;
					jump = false;
					jumpFrames = 0;
				}
			} else {
				isJumping = false;
				jump = false;
				jumpFrames = 0;
			}
		}

		// Para detetar colisão com os inimigos
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			if (e instanceof Enemy) {
				if (Entity.isColidding(this, e)) {
						life--;
						System.out.println("Levei dano");
						
				}
				if(life == 0)
				{
					System.exit(0);
				}
			}
		}

		//Detetar colisão com as moedas
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			if (e instanceof Coin) {
				if (Entity.isColidding(this, e)) {
						Game.entities.remove(i);
						Player.currentCoin++;
						System.out.println("Levei dano");
						break;
				}
			}
		}
		
		// Para a câmera acompanhar o player independentemente da altura do mapa
		Camera.x = Camera.clamp((int) x - Game.WIDTH / 2, 0, World.WIDTH * 16 - Game.WIDTH);
		Camera.y = Camera.clamp((int) y - Game.HEIGHT / 2, 0, World.HEIGHT * 16 - Game.HEIGHT);
	}
}