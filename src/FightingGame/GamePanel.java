package FightingGame;

import Entity.HPconfig;
import playerSetup.Player1;
import playerSetup.Player2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.math.*;
import java.net.URL;

import Entity.*;

public class GamePanel extends JPanel implements Runnable {
	Image bg;
	public static final int originalTitleSize = 16;
	public static final int scale = 3;
	public static final double FPS = 60;
	int countTest = 0;
	private final int minuteGame = 1;
	private int minute;
	private int second;
	private int totalFame = minuteGame*60*60;
	private boolean moving = false;
	private boolean kickStatus = false;
	//UI
	Image icon;
	//SOund
	static Sound sound = new Sound();
	//setup status of game
	public static int statusGame=0;
	// set window scale
	public static final int titleSize = originalTitleSize * scale;
	public final int maxScreenCol = 27;
	public final int maxScreenRow = 16;
	final int screenWidth = 1278;// 960px
	final int screenHeight = 720;// 720px
	//how to choose option
	public static int statusChoose =0;
	keyHandler keyH = new keyHandler(KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_D, KeyEvent.VK_A, 'k');
	keyHandler keyH1 = new keyHandler(KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_RIGHT, KeyEvent.VK_LEFT,
			KeyEvent.VK_L);
	Player1 player1 = new Player1(this, keyH, 200, 550);
	Player2 player2 = new Player2(this, keyH1, screenWidth - 200, 550);
	Thread gameThread;
	HPconfig hp = new HPconfig();
	private int distance2player;

	public GamePanel() {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		ImageIcon j = new ImageIcon("C:\\Users\\Admin\\Desktop\\ProjectOOP\\png\\png\\bg_Game.png");
		bg = j.getImage();
		ImageIcon u = new ImageIcon("C:\\Users\\Admin\\Desktop\\ProjectOOP\\png\\png\\bgMenu.gif");
		icon = u.getImage();
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		this.addKeyListener(keyH1);
		this.setFocusable(true);
	}
	public void startGame() {
		gameThread = new Thread(this);
		gameThread.start();
	}

	@Override
	public void run() {
		
		playMusic(1);
		keyH.setPunchKickKey(KeyEvent.VK_J, KeyEvent.VK_K);
		double drawInterval = 1000000000 / FPS;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		long timer = 0;
		while (gameThread != null) {
			distance2player = Math.abs(player1.getX()-player2.getX());
			if (moving)
				player1.skillStatus = false;
			if (kickStatus)
				player1.kickStatus = false;
			currentTime = System.nanoTime();
			delta += (currentTime - lastTime) / drawInterval;
			lastTime = currentTime;
			if (delta >= 1) {
				player1.update();
				player2.update();
				setupStatus();
				checkKick();
				repaint();
				delta--;
			}
			
		}
		
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		if(statusGame==0) {
			menuDraw(g2);
		}
		
		if(statusGame==1) {
		g.drawImage(bg, 0, 0, null);
		if (player1.skill1.status && !player1.skill1.coming)
			player1.skill1.draw(g2);
		if (player1.skill1.coming) {
			player1.skill1.draw(g2, Color.blue);
			player1.skill1.coming = false;
		}
		
		minute = totalFame/3600;
		second = (totalFame/60)%60;
		hp.draw(g2,second,minute);
		hp.draw1(g2);
		player1.draw1(g2);
		player2.draw1(g2);
		//win
		g2.dispose();
		}
	}
	//draw menu
	private void menuDraw(Graphics2D g2) {
		String title = "GAME PRO VIP DANH NHAU DUNG DUNG";
		g2.setColor(new Color(74, 116, 53));
		g2.drawImage(icon, 0, 0,1278,720,null);
		//Shadow
		g2.setFont(g2.getFont().deriveFont(Font.BOLD,40));
		g2.setColor(Color.gray);
		g2.drawString(title, 273, 123);
		//title Game
		g2.setColor(Color.white);
		g2.drawString(title, 270, 120);
		//Detail
		g2.setFont(g2.getFont().deriveFont(Font.BOLD,36));
		if(statusChoose == 0) {
			g2.setColor(Color.black);
			g2.drawString("=>", 300, 250);
			g2.drawString("START GAME ", 400, 250);
		}else {
			g2.setColor(Color.white);
			g2.drawString("START GAME ", 400, 250);
		}
		if(statusChoose == 1) {
			g2.setColor(Color.black);
			g2.drawString("=>", 300, 350);
			g2.drawString("SETTINGS ", 400, 350);
		}else {
			g2.setColor(Color.white);
			g2.drawString("SETTINGS ", 400, 350);
		}
		if(statusChoose == 2) {
			g2.setColor(Color.black);
			g2.drawString("=>", 300, 450);
			g2.drawString("QUIT ", 400, 450);
		}else {
			g2.setColor(Color.white);
			g2.drawString("QUIT ", 400, 450);
		}
	}

	// check skill
	private void checkSkill(Player player1, Player player2) {
		if (player1.skill1 == null)
			player1.skill1 = new Skills();
		if (player1.skillStatus) {
			player1.skill1.setX(player1.getX() + 10);
			player1.skill1.setY(player1.getY() - 10);
			player1.skill1.status = true;
			moving = true;
		}
		player1.skillStatus = false;
		if ((player1.skill1.getX() < player2.getX() + 10 && player1.skill1.getX() > player2.getX() - 4)
				&& (player1.skill1.getY() < player2.getY() && player1.skill1.getY() > player2.getY() - 70)) {
			player1.skill1.coming = true;
			player2.setHp(player2.getHp() - player1.skill1.getDame());
			hp.setHp2(player2.getHp());
			player2.setX(player2.getX() + 2);
			System.out.println(player2.getHp());
			player1.skill1 = null;
			player1.skill1 = new Skills();
			moving = false;
		}
		if (player1.skill1.getX() > 1200) {
			moving = false;
			player1.skill1 = null;
			player1.skill1 = new Skills();
		}
		if (player1.skill1.status && !player1.skill1.coming) {
			player1.skill1.update();
		}
	}

	// check kick
	private void checkKick() {
		if (player1.kickStatus) {
			kickStatus = true;
			player1.kick.setX(player1.getX() + 30);
			player1.kick.setY(player1.getY() + 50);
		}
		if (player1.kickCount > 30) {
			kickStatus = false;
			player1.kickCount = 0;
		}
		if (Math.abs(player1.getX() - player2.getX()) < 70 && player1.kickCount == 15) {
			
			player2.setHp(player2.getHp() - player1.kick.getDame());
			player2.setX(player2.getX()+10);
			player2.setDirector("left");
			hp.setHp2(player2.getHp());
			hp.setPower2(hp.getPower2()+5);
			
		}

		if (player2.getHp() <= 0) {
			JOptionPane.showMessageDialog(null, "Player1 Win");
			sound.stop();
			gameThread = null;
			System.exit(0);
		}
	}
	// check punch
	private void checkPunch(Player p1,Player p2) {
	
	}
	// check win if timeup
	private void checkWinTimeUp() {
		if(player1.getHp()>player2.getHp()) {
			JOptionPane.showMessageDialog(null, "Player1 Win");
			gameThread = null;
			
		}
	}
	// thiet lap cac trang thai
	private void setupStatus() {
		if (kickStatus) player1.kickCount++;
		player2.frameCountStand++;
		player2.frameCountWalk++;
		player1.frameCountPunch++;
		player1.frameCountStand++;
		player1.frameCountWalk++;
		if(statusGame==1) {
			if(totalFame>0) {
				totalFame--;
			}
			}
		countTest++;
		player1.setEnemiesPos(player2.getX());
		if(Math.abs(player1.getX()-player2.getX())<=(titleSize-10)) {
			player1.blocked = true;
			player2.blocked = true;
		}else {
			player1.blocked = false;
			player2.blocked = false;
		}
	}
	
	// get sound
	public static void playMusic(int i ) {
		sound.setFile(i);
		sound.play();
		sound.loop();
	}
	
	public void playMusicNoLoop(int i ) {
		sound.setFile(i);
		sound.play();
	}
}