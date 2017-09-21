package com.somethingdweeby.game;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import com.somethingdweeby.game.gfx.Player;
import com.somethingdweeby.game.gfx.Screen;
import com.somethingdweeby.game.gfx.SpriteSheet;

public class Game extends Canvas implements Runnable{
	
	private static final long serialVersionUID = 1L;
	
	public static final int WIDTH = 320;
	public static final int HEIGHT = WIDTH/12*9;
	public static final int SCALE = 3;
	public static final String NAME = "Game";
	
	private JFrame frame;
	
	public boolean running = false;
	public int tickCount = 0;
	
	GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice gs = ge.getDefaultScreenDevice();
    GraphicsConfiguration gc = gs.getDefaultConfiguration();
	
	private BufferedImage image = gc.createCompatibleImage(WIDTH, HEIGHT);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	
	
	public Screen screen;
	private Player player;
	public InputHandler input;
	
	public Game() {
		setMinimumSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		setMaximumSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		
		frame = new JFrame(NAME);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		
		frame.add(this,BorderLayout.CENTER);
		frame.pack();
		
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	public void init() {
		screen = new Screen(WIDTH,HEIGHT,new SpriteSheet("/FullTextMap.png",16));
		player = new Player(WIDTH,HEIGHT,new SpriteSheet("/PlaceHolder_Char.png",32),screen);
		input = new InputHandler(this);
	}
	
	public synchronized void start() {
		running = true;
		new Thread(this).start();
	}
	
	public synchronized void stop() {
		running = false;
	}
	
	public void run() {
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000D/60D;
		
		int ticks = 0;
		int frames = 0;
		
		long lastTimer = System.currentTimeMillis();
		double delta = 0;
		
		init();
		
		while(running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / nsPerTick;
			lastTime = now;
			boolean shouldRender = true;
			
			while(delta >= 1) {
				ticks++;
				tick();
				delta -= 1;
				shouldRender = true;
			}
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(shouldRender) {
				frames++;
				render();
			}
			
			if(System.currentTimeMillis() - lastTimer >= 1000) {
				lastTimer += 1000;
				System.out.println("X: "+player.playerX/32+", Y: "+player.playerY/32+"\nTicks: "+ticks+", Frames: "+frames);
				frames = 0;
				ticks=0;
			}
		}
	}
	
	public void tick() {
		int[] playerOffsets = new int[4];
		playerOffsets[0]=-16;
		playerOffsets[1]=0;
		playerOffsets[2]=-6;
		playerOffsets[3]=-6;
		tickCount++;
		int isWalking=0;
		if(input.up.isPressed()&&!(player.isColliding(-16-playerOffsets[2],-17-playerOffsets[0])||player.isColliding(15+playerOffsets[3],-17-playerOffsets[0]))) {
			player.playerY-=1;
			isWalking+=1;
		}
		if(input.down.isPressed()&&!(player.isColliding(-16-playerOffsets[2],16+playerOffsets[1])||player.isColliding(15+playerOffsets[3],16+playerOffsets[1]))) {
			player.playerY+=1;
			isWalking+=2;
		}
		if(input.left.isPressed()&&!(player.isColliding(-17-playerOffsets[2],-16-playerOffsets[0])||player.isColliding(-17-playerOffsets[2],15+playerOffsets[1]))) {
			player.playerX-=1;
			isWalking+=4;
		}
		if(input.right.isPressed()&&!(player.isColliding(16+playerOffsets[3],-16-playerOffsets[0])||player.isColliding(16+playerOffsets[3],15+playerOffsets[1]))) {
			player.playerX+=1;
			isWalking+=8;
		}
		if(isWalking>0) {
			player.isWalking=true;
			if(isWalking==1)player.direction=3;
			if(isWalking==2)player.direction=0;
			if(isWalking==4)player.direction=1;
			if(isWalking==8)player.direction=2;
		}else {
			player.isWalking=false;
		}
	}
	
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if(bs==null) {
			createBufferStrategy(3);
			return;
		}
		player.offsetMath();
		screen.renderLayer(pixels, WIDTH,1);
		
		screen.renderLayer(pixels, WIDTH,2);
		player.render(pixels, WIDTH);
		screen.renderLayer(pixels, WIDTH,3);
		
		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		
		g.dispose();
		bs.show();
	}
	
	public static void main(String[] args) {
		new Game().start();
	}
}
