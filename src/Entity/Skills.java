package Entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import FightingGame.GamePanel;

public class Skills implements Action {
	private int x;
	private int y;
	private int dame = 50;
	private int speed=4;
	public boolean status;
	public boolean coming=false;
	private BufferedImage skillImage;
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public Skills() {
		
	}
	public int getDame() {
		return dame;
	}
	public void setDame(int dame) {
		this.dame = dame;
	}
	public void setX(int x) {
		this.x = x;
	}
	public void setY(int y) {
		this.y = y;
	}
	public Skills(int x, int y) {
		this.x = x;
		this.y = y;
		status = true;
	}
	public Skills(String urlSkill,int x, int y,int dame) {
		this.x = x;
		this.y = y;
		this.dame= dame;
		try {
			skillImage = ImageIO.read(getClass().getResource(urlSkill));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update() {
		if(x<1200) x+=speed;
		else status = false;
	}

	@Override
	public void coming() {
		// TODO Auto-generated method stub
		
	}
	public void remove() {
		
	}
	@Override
	public void draw(Graphics2D g2,Color cl) {
		// TODO Auto-generated method stub
		g2.setColor(cl);
		g2.fillRect(x, y, GamePanel.titleSize, GamePanel.titleSize);
	}

}