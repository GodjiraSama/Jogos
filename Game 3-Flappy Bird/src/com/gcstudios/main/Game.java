package com.gcstudios.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JFrame;


import com.gcstudios.entities.Entity;
import com.gcstudios.entities.Player;
import com.gcstudios.graficos.Spritesheet;
import com.gcstudios.graficos.UI;
import com.gcstudios.world.TuboGenerator;

public class Game extends Canvas implements Runnable,KeyListener{

	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	public static final int WIDTH = 240;
	public static final int HEIGHT = 160;
	public static final int SCALE = 3;
	
	private BufferedImage image;
	
	public static List<Entity> entities;
	public static Spritesheet spritesheet;
	public static Player player;
	public static TuboGenerator tubogenerator;
	
	public UI ui;
	
	public static double score = 0;
	
	public Game(){
		addKeyListener(this);
		setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		initFrame();
		image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
		
		//Inicializando objetos.
		spritesheet = new Spritesheet("/spritesheet.png");
		entities = new ArrayList<Entity>();
		player = new Player(WIDTH/2 - 30,HEIGHT/2,16,16,2,spritesheet.getSprite(0,0,16,16));
		tubogenerator = new TuboGenerator();
		ui = new UI();
		entities.add(player);
	}
	
	public void initFrame(){
		frame = new JFrame("Flappy Bird");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public synchronized void start(){
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	public synchronized void stop(){
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]){
		Game game = new Game();
		game.start();
	}
	
	public void tick(){
		tubogenerator.tick();
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.tick();
		}
	}

	public void render(){
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null){
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		g.setColor(new Color(122,102,255));
		g.fillRect(0, 0,WIDTH,HEIGHT);
		
		/*Renderização do jogo*/
		//Graphics2D g2 = (Graphics2D) g;
		Collections.sort(entities,Entity.nodeSorter);
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}
		/***/
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0,WIDTH*SCALE,HEIGHT*SCALE,null);
		ui.render(g);
		bs.show();
	}
	
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		requestFocus();
		while(isRunning){
			long now = System.nanoTime();
			delta+= (now - lastTime) / ns;
			lastTime = now;
			if(delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
			}
			if(System.currentTimeMillis() - timer >= 1000){
				System.out.println("FPS: "+ frames);
				frames = 0;
				timer+=1000;
			}
		}
		stop();
	}

	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_SPACE)
		{
			// O pássaro a subir
			player.isPressed = true;
		}
	}

	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_SPACE)
		{
			// O pássaro está a descer 
			player.isPressed = false;
		}
		
		
	}

	public void keyTyped(KeyEvent e) {
		
		
	}	
}