package com.carvoeirostudios.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import com.carvoeirostudios.main.Game;
import com.carvoeirostudios.world.Camera;
import com.carvoeirostudios.world.World;

public class Player extends Entity {

	public boolean right, up, left, down;
	public int right_dir = 0, left_dir = 1;
	public int dir = right_dir;
	public double speed = 1.4;

	private int frames = 0, maxFrames = 5, index = 0, maxIndex = 3;
	private boolean moved = false;
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;

	// verificar se tem arma
	private boolean hasGun = false;

	// criação da variável ammo
	public int ammo = 10;
	public int dshoot = 2;
	// verificar se está a disparar
	public boolean shoot = false, mouseShoot = false;

	// criação da vida do player
	public double life = 100, maxlife = 100;

	// saber localização do mouse para mira
	public int mx, my;

	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);

		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];

		for (int i = 0; i < 4; i++) {
			rightPlayer[i] = Game.spritesheet.getSprite(32 + (i * 16), 0, 16, 16);
		}

		for (int i = 0; i < 4; i++) {
			leftPlayer[i] = Game.spritesheet.getSprite(32 + (i * 16), 16, 16, 16);
		}
	}

	public void tick() {
		moved = false;
		if (right && World.isFree((int) (x + speed), this.getY())) {
			moved = true;
			dir = right_dir;
			x += speed;
		} else if (left && World.isFree((int) (x - speed), this.getY())) {
			moved = true;
			dir = left_dir;
			x -= speed;
		}
		if (up && World.isFree(this.getX(), (int) (y - speed))) {
			moved = true;
			y -= speed;
		} else if (down && World.isFree(this.getX(), (int) (y + speed))) {
			moved = true;
			y += speed;
		}

		if (moved) {
			frames++;
			if (frames == maxFrames) {
				frames = 0;
				index++;

				if (index > maxIndex) {
					index = 0;
				}
			}

		}

		checkCollisionLifePack();
		checkCollisionAmmo();
		checkCollisionWeapon();

		if (shoot) {
			shoot = false;
			if (hasGun && (dshoot > 0 || ammo > 0)) {
				dshoot--;

				if (dshoot == 0 && ammo > 0) {
					ammo -= 2;
					dshoot += 2;
				}
				// criar a bala e disparar
				// System.out.println("Disparo");
				int dx = 0;
				int px = 0;
				int py = 6;

				BulletShoot bullet = new BulletShoot(this.getX() + px, this.getY() + py, 3, 3, null, dx, 0);
				Game.bullets.add(bullet);
				// ammo --;
			}
		}
		// Usar o rato para disparar
		if (mouseShoot) {
			mouseShoot = false;

			double angle = 0;

			if (hasGun && (dshoot > 0 || ammo > 0)) {
				dshoot--;

				if (dshoot == 0 && ammo > 0) {
					ammo -= 2;
					dshoot += 2;
				}

				// calculo matemático para usar o rato
				// primeiro o y e depois o x
				int px = 0;
				int py = 8;

				if (dir == right_dir) {
					px = 18;
					angle = Math.atan2(my - (this.getY() + py - Camera.y), mx - (this.getX() + px - Camera.x));
				} else {
					px = -8;
					angle = Math.atan2(my - (this.getY() + py - Camera.y), mx - (this.getX() + px - Camera.x));
				}

				double dx = Math.cos(angle);
				double dy = Math.sin(angle);

				BulletShoot bullet = new BulletShoot(this.getX() + px, this.getY() + py, 3, 3, null, dx, dy);
				Game.bullets.add(bullet);
			}
		}
		// Sistema de Restart(Game Over)
		if (life <= 0) {
			// GAME OVER
			life = 0;
			Game.gameState = "GAME_OVER";
		}

		updateCamera();
	}

	public void updateCamera() {
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH / 2), 0, World.WIDTH * 16 - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT / 2), 0, World.HEIGHT * 16 - Game.HEIGHT);
	}

	// Para o funcionamento da colisão e uso do life pack
	public void checkCollisionLifePack() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if (atual instanceof Lifepack) {
				if (Entity.isCollidding(/* (esta classe) */this, atual)) {
					life += 8;
					if (life >= 100)

						life = 100;
					Game.entities.remove(i);
					return;
				}
			}
		}
	}

	// Para o funcionamento da colisão e uso da ammo
	public void checkCollisionAmmo() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if (atual instanceof Bullet) {
				if (Entity.isCollidding(/* (esta classe) */this, atual)) {
					ammo += 4;
					// se a arma estiver com 0 balas
					if (dshoot == 0) {
						// retira 2 das balas coletadas e passa-as para a munição da arma
						ammo -= 2;
						dshoot += 2;
					}
					// System.out.println("Munição atual:" + ammo);
					Game.entities.remove(i);
				}
			}
		}
	}

	public void checkCollisionWeapon() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if (atual instanceof Weapon) {
				if (Entity.isCollidding(/* (esta classe) */this, atual)) {
					hasGun = true;
					// System.out.println("Apanhou arma");
					Game.entities.remove(i);
				}
			}
		}
	}

	public void render(Graphics g) {
		if (dir == right_dir) {
			g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
			if (hasGun) {
				g.drawImage(Entity.WEAPON_RIGHT, this.getX() + 8 - Camera.x, this.getY() - Camera.y, null);
			}
		} else if (dir == left_dir) {
			g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
			if (hasGun) {
				g.drawImage(Entity.WEAPON_LEFT, this.getX() - 8 - Camera.x, this.getY() - Camera.y, null);
			}
		}
	}
}