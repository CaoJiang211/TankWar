package com.cj.tank;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Missile {
	public static final int XSPEED = 10;
	public static final int YSPEED = 10;
	
	public static final int WIDTH = 10;
	public static final int HEIGHT = 10;
	
	int x, y;
	
	private boolean good ;

	Direction dir;
	
	private boolean live = true;
	private TankClient tc;
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image[] missileImages = null;
	private static Map<String, Image> imgs = new HashMap<String ,Image>();
	
	static {
		missileImages = new Image[] {
				tk.getImage(Tank.class.getClassLoader().getResource("image/missileL.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("image/missileLU.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("image/missileU.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("image/missileRU.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("image/missileR.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("image/missileRD.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("image/missileD.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("image/missileLD.gif")),
		};
		imgs.put("L",  missileImages[0]);
		imgs.put("LU", missileImages[1]);
		imgs.put("U", missileImages[2]);
		imgs.put("RU", missileImages[3]);
		imgs.put("R", missileImages[4]);
		imgs.put("RD", missileImages[5]);
		imgs.put("D", missileImages[6]);
		imgs.put("LD", missileImages[7]);
	}
	
	public Missile(int x, int y,Direction dir) {
		this.x = x;
		this.y = y;
		this.dir = dir;
	}
	
	public Missile(int x, int y, boolean good, Direction dir, TankClient tc) {
		this.x = x;
		this.y = y;
		this.dir = dir;
		this.good = good;
		this.tc = tc;
	}
	
	public void draw (Graphics g) {
		if(!live) {
			tc.missiles.remove(this);
			return;
		}
		
		switch(dir) {
		case L:
			g.drawImage(imgs.get("L"),x ,y ,null);
			break;
		case LU:
			g.drawImage(imgs.get("LU"),x ,y ,null);
			break;
		case U:
			g.drawImage(imgs.get("U"),x ,y ,null);
			break;
		case RU:
			g.drawImage(imgs.get("RU"),x ,y ,null);
			break;
		case R:
			g.drawImage(imgs.get("R"),x ,y ,null);
			break;
		case RD:
			g.drawImage(imgs.get("RD"),x ,y ,null);
			break;
		case D:
			g.drawImage(imgs.get("D"),x ,y ,null);
			break;
		case LD:
			g.drawImage(imgs.get("LD"),x ,y ,null);
			break;
		}
		
		move();
	}

	private void move() {		
		switch(dir) {
		case L:
			x -= XSPEED;
			break;
		case LU:
			x -= XSPEED;
			y -= YSPEED;
			break;
		case U:
			y -= YSPEED;
			break;
		case RU:
			x += XSPEED;
			y -= YSPEED;
			break;
		case R:
			x += XSPEED;
			break;
		case RD:
			x += XSPEED;
			y += YSPEED;
			break;
		case D:
			y += YSPEED;
			break;
		case LD:
			x -= XSPEED;
			y += YSPEED;
			break;
		}
		
		if (x < 0 || y < 0 || x > TankClient.GAME_WIDTH || 
				y >TankClient.GAME_HEIGHT) {
			live = false;
			tc.missiles.remove(this);
		}
	}
	
	public boolean isLive() {
		return live;
	}
	
	public Rectangle getRect(){
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
	
	public boolean hitTank(Tank t ) {
		if(this.live && this.getRect().intersects(t.getRect()) && t.isLive()
				&& this.good != t.isGood()){
			if(t.isGood()) {
				t.setLife(t.getLife() - 20);
				if(t.getLife() <= 0) t.setLive(false);
			} else {
				t.setLive(false);
			}
			
			this.live = false;
			Explode e = new Explode(x, y, tc) ;
			tc.explodes.add(e);
			return true;
		}
		return false;
	}

	public boolean hitTanks(List<Tank> tanks) {
		for(int i=0; i<tanks.size(); i++) {
			if(hitTank(tanks.get(i))) {
				return true;
			}
		}return false;
	}
	
	public boolean hitWall(Wall w) {
		if(this.live && this.getRect().intersects(w.getRect())) {
			this.live = false;
			return true;
		}
		return false ;
	}
}
