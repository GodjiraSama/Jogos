package com.carvoeirostudios.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JFrame;
import com.carvoeirostudios.entities.BulletShoot;
import com.carvoeirostudios.entities.Enemy;
import com.carvoeirostudios.entities.Entity;
import com.carvoeirostudios.entities.Player;
import com.carvoeirostudios.graficos.Spritesheet;
import com.carvoeirostudios.graficos.UI;
import com.carvoeirostudios.world.World;
import java.awt.*;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener {
	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	public static final int WIDTH = 240;
	public static final int HEIGHT = 160;
	public static final int SCALE = 3;

	// Indicar o level atual e o máximo de levels possíveis de se jogar
	private int CUR_LEVEL = 1, MAX_LEVEL = 3;
	private BufferedImage image;

	public static List<Entity> entities;
	public static List<Enemy> enemies;
	public static List<BulletShoot> bullets;

	public static Spritesheet spritesheet;

	public static World world;

	public static Player player;

	public static Random rand;

	public UI ui;

	// Criação de um estado
	public static String gameState = "MENU";

	// Criação de um estado para o Game Over
	private boolean showMessageGameOver = true;
	private int framesGameOver = 0;
	private boolean restartGame = false;
	// Criação de um estado para o Menu
	public Menu menu;

	// As componentes principais e importantes do jogo
	public Game() {
		rand = new Random();
		addKeyListener(this);// Ler teclas
		addMouseListener(this);// Ler mouse
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		initFrame();

		// Inicializar Objetos
		ui = new UI();
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		bullets = new ArrayList<BulletShoot>();

		// Spritesheets do player e do mundo
		spritesheet = new Spritesheet("/spritesheet.png");
		player = new Player(0, 0, 16, 16, spritesheet.getSprite(32, 0, 16, 16));
		entities.add(player);
		world = new World("/level1.png");

		// Inicialização do Menu
		menu = new Menu();
	}

	public void initFrame() {
		frame = new JFrame("Meu 1º Jogo");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}

	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		Game game = new Game();
		game.start();
	}

	// A parte lógica do Jogo
	public void tick() {
		if (gameState == "Normal") {
			this.restartGame = false;
			for (int i = 0; i < entities.size(); i++) {
				Entity e = entities.get(i);
				e.tick();
			}

			for (int i = 0; i < bullets.size(); i++) {
				bullets.get(i).tick();
			}

			if (enemies.size() == 0) {
				// Avançar para o próximo level
				CUR_LEVEL++;
				Sound.NextLevel.play();
				if (CUR_LEVEL > MAX_LEVEL) {
					CUR_LEVEL = 1;
				}
				// Restart do game
				String newWorld = "level" + CUR_LEVEL + ".png";
				World.restartGame(newWorld);
			}

		} else if (gameState == "GAME_OVER") {
			this.framesGameOver++;
			if (this.framesGameOver == 30) {
				this.framesGameOver = 0;
				if (this.showMessageGameOver) {
					this.showMessageGameOver = false;
				} else {
					this.showMessageGameOver = true;
				}
				if (restartGame) {
					this.restartGame = false;
					Game.gameState = "Normal";
					CUR_LEVEL = 1;
					String newWorld = "level" + CUR_LEVEL + ".png";
					World.restartGame(newWorld);
				}

			}
		} else if (gameState == "MENU") {
			menu.tick();
		}
	}

	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		g.setColor(new Color(0, 0, 0));
		g.fillRect(0, 0, WIDTH, HEIGHT);

		world.render(g);
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}
		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).render(g);
		}
		ui.render(g);

		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);

		// Munição no ecrã
		// Caso não tenha munição
		if (player.dshoot == 0 && player.ammo == 0) {
			g.setFont(new Font("calibri", Font.BOLD, 25));
			g.setColor(Color.white);
			g.drawString("Não tem munição", 10, 470);
		} else {
			g.setFont(new Font("calibri", Font.BOLD, 25));
			g.setColor(Color.white);
			g.drawString("Ammo: " + player.dshoot + "/" + player.ammo, 10, 470);
		}
		// Criação da tele de Game Over
		if (gameState == "GAME_OVER") {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(0, 0, 0, 100));
			g2.fillRect(0, 0, WIDTH * SCALE, HEIGHT * SCALE);
			g.setFont(new Font("calibri", Font.BOLD, 27));
			g.setColor(Color.white);
			g.drawString("Game Over", (WIDTH * SCALE) / 2 - 30, (HEIGHT * SCALE) / 2 + 5);
			if (showMessageGameOver) 
				g.drawString("> Pressione ESC para voltar ao menu <", (WIDTH * SCALE) / 2 - 140,
						(HEIGHT * SCALE) / 2 + 90);
				g.drawString("> Pressione Enter para reiniciar <", (WIDTH * SCALE) / 2 - 140,
						(HEIGHT * SCALE) / 2 + 50);
			
		} 
		else if (gameState == "MENU") {
			menu.render(g);
		}
		bs.show();
	}

	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		requestFocus(); // para mal inicia, poder usar o teclado

		while (isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;

			if (delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
			}
			if (System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS: " + frames);
				frames = 0;
				timer += 1000;
			}
		}
		stop();
	}

	// Ativar a movimentação manual do player
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			player.right = true;
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			player.left = true;
		}

		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			player.up = true;
			if (gameState == "MENU") {
				menu.up = true;
			}
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			player.down = true;

			if (gameState == "MENU") {
				menu.down = true;
			}
		}

		/*
		 * identificar e ativar a tecla para disparar if (e.getKeyCode() ==
		 * KeyEvent.VK_X) { player.shoot = true; }
		 */
	}

	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			player.right = false;
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			player.left = false;
		}

		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			player.up = false;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			player.down = false;
		}

		// Configuração para reiniciar o jogo em caso de Game Over pressionando o Enter
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			this.restartGame = true;
			Sound.select.play();
			if (gameState == "MENU") {
				menu.enter = true;
			}
		}
		// Configuração para na tela de Game Over voltar para o menu (Feito por Mim)
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			gameState = "MENU";
			menu.pause = true;
		}
	}

	// Configuração do rato
	public void keyTyped(KeyEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		player.mouseShoot = true;
		player.mx = (e.getX() / 3);
		player.my = (e.getY() / 3);
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {

	}
}