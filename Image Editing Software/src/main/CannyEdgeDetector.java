package main;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;


class Pixel {
	
	public final int x;
	public final int y;
	public final int angle;
	
	public Pixel(int x, int y, int angle) {
		this.x = x;
		this.y = y;
		this.angle = angle;
	}
	
}

public class CannyEdgeDetector {
	
	private static BufferedImage image;
	private static Pixel[][] grid;
	private static int[][] magnitudes;
	private static int high;
	private static int low;
	private static Set<Pixel> strongEdges = new HashSet<Pixel>();
	private static Set<Pixel> weakEdges = new HashSet<Pixel>();
	private static final int[][] sobelx = {{1, 2, 1},
			  {0, 0, 0},
			  {-1, -2, -1}};
	private static final int[][] sobely = {{1, 0, -1},
			  {2, 0, -2},
			  {1, 0, -1}};
	
	public static BufferedImage detectEdges(BufferedImage image, int high, int low) {
		CannyEdgeDetector.image = image;
		CannyEdgeDetector.grid = new Pixel[image.getWidth()-2][image.getHeight()-2];
		CannyEdgeDetector.magnitudes = new int[image.getWidth()-2][image.getHeight()-2];
		CannyEdgeDetector.strongEdges = new HashSet<Pixel>();
		CannyEdgeDetector.weakEdges = new HashSet<Pixel>();
		CannyEdgeDetector.high = high;
		CannyEdgeDetector.low = low;
		
		calculateMagnitudesAndGradient();
		thin();
		doubleThreshold();
		hysteresis();
		finish();
		
		return image;
	}
	
	
	private static void hysteresis() {
		for(Pixel pixel : weakEdges) {
			boolean edge = false;
			for(int dx = -1; dx <= 1; dx++) {
				for(int dy = -1; dy <= 1; dy++) {
					Pixel neighbor = grid[pixel.x+dx][pixel.y+dy];
					if(strongEdges.contains(neighbor)) {
						edge = true;
						magnitudes[pixel.x][pixel.y] = 255;
						break;
					}
				}
				if(edge) break;
			}
		}
	}
	
	private static void doubleThreshold() {
		for(int x = 0; x < grid.length; x++) {
			for(int y = 0; y < grid[x].length; y++) {
				if(magnitudes[x][y] >= high) {
					magnitudes[x][y] = 255;
					strongEdges.add(grid[x][y]);
				} else if(magnitudes[x][y] > low) {
					magnitudes[x][y] = 0;
					weakEdges.add(grid[x][y]);
				} else {
					magnitudes[x][y] = 0;
				}
			}
		}
	}
	
	private static void thin() {
		int[][] maxMagnitudes = new int[magnitudes.length][magnitudes[0].length];
		for(int x = 1; x < magnitudes.length-1; x++) {
			for(int y = 1; y < magnitudes[x].length-1; y++) {
				maxMagnitudes[x][y] = magnitudes[x][y];
				switch(grid[x][y].angle) {
				case 0:
					if(magnitudes[x][y] < magnitudes[x-1][y] || magnitudes[x][y] < magnitudes[x+1][y]) {
						maxMagnitudes[x][y] = 0;
					}
					break;
				case 1:
					if(magnitudes[x][y] < magnitudes[x-1][y-1] || magnitudes[x][y] < magnitudes[x+1][y+1]) {
						maxMagnitudes[x][y] = 0;
					}
					break;
				case 2:
					if(magnitudes[x][y] < magnitudes[x][y-1] || magnitudes[x][y] < magnitudes[x][y+1]) {
						maxMagnitudes[x][y] = 0;
					}
					break;
				case 3:
					if(magnitudes[x][y] < magnitudes[x+1][y-1] || magnitudes[x][y] < magnitudes[x-1][y+1]) {
						maxMagnitudes[x][y] = 0;
					}
					break;
				}
			}
		}
		magnitudes = maxMagnitudes;
	}
	
	private static void calculateMagnitudesAndGradient() {
		for(int x = 0; x < grid.length; x++) {
			for(int y = 0; y < grid[x].length; y++) {
				double gx = 0, gy = 0;
				
				for(int dx = -1, i = 0; dx <= 1; dx++, i++) {
					for(int dy = -1, j = 0; dy <= 1; dy++, j++) {
						Color color = new Color(image.getRGB(x + dx+1, y + dy+1));
						int average = (color.getGreen() + color.getRed() + color.getBlue())/3;
						gy+=average*sobely[i][j];
						gx+=average*sobelx[i][j];
					}
				}
				
				magnitudes[x][y] = (int)Math.abs(Math.sqrt(gx*gx+gy*gy));
				double theta = Math.atan2(gy, gx);
				int angle = 0;
				if(theta <= 22.5 || theta > 157.5)   angle = 0; //0 degrees
				else if(theta <= 67.5)               angle = 1; //45 degrees
				else if(theta <= 112.5)              angle = 2; //90 degrees
				else if(theta <= 157.5)              angle = 3; //135 degrees
				grid[x][y] = new Pixel(x, y, angle);
			}
		}
	}
	
	private static void finish() {
		for(int x = 0; x < magnitudes.length; x++) {
			for(int y = 0; y < magnitudes[x].length; y++) {
				int m = magnitudes[x][y];
				image.setRGB(x+1, y+1, (0xff << 24) | (m << 16) | (m << 8) | m);
			}
		}
	}
	
}