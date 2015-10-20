package Handlers;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Content {
	
	//particles
	public static BufferedImage[][] ParticleRed = load("/Sprites/other/EnergyParticleRed.png", 5, 5);
	public static BufferedImage[][] ParticleBlue = load("/Sprites/other/EnergyParticleBlue.png", 5, 5);
	public static BufferedImage[][] ParticlePurple = load("/Sprites/other/EnergyParticlePurple.png", 5, 5);
	public static BufferedImage[][] ParticleYellow = load("/Sprites/other/EnergyParticleYellow.png", 5, 5);
	
	//Enemies
	public static BufferedImage[][] UFO = load("/Sprites/Enemies/UFO.png", 40, 20);
	public static BufferedImage[][] AlienShooter = load("/Sprites/Enemies/alienshooter.png", 40, 40);
	public static BufferedImage[][] Alien = load("/Sprites/Enemies/alien.png", 30, 30);
	public static BufferedImage[][] Aliensoldier=load("/Sprites/Enemies/aliensoldier.png", 40, 40);
	
	//boss
	public static BufferedImage[][] Boss1 = load("/Sprites/Enemies/boss.png", 60, 70);
	
	//objects
	public static BufferedImage[][] EnergyFire = load("/Sprites/Enemies/energyfire.png", 8, 8);
	public static BufferedImage[][] Explosion = load("/Sprites/Enemies/Explosion.png", 15, 15);
	public static BufferedImage[][] HealthOrb = load("/Sprites/other/healthorb.png", 15, 15);
	public static BufferedImage[][] Door = load("/Sprites/other/door.png", 40, 40);
	public static BufferedImage[][] Teleport = load("/Sprites/other/teleport.png", 40, 40);
	public static BufferedImage[][] ufoDoor = load("/Sprites/other/ufoDoor.png", 40, 40);
	public static BufferedImage[][] Coin = load("/Sprites/other/coin.png", 15, 15);
	
	public static BufferedImage[][] load(String s, int w, int h) {
		BufferedImage[][] ret;
		try {
			BufferedImage spritesheet = ImageIO.read(Content.class.getResourceAsStream(s));
			int width = spritesheet.getWidth() / w;
			int height = spritesheet.getHeight() / h;
			ret = new BufferedImage[height][width];
			for(int i = 0; i < height; i++) {
				for(int j = 0; j < width; j++) {
					ret[i][j] = spritesheet.getSubimage(j * w, i * h, w, h);
				}
			}
			return ret;
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("Error loading graphics.");
			System.exit(0);
		}
		return null;
	}
}
