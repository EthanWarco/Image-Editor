package main;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Random;

public class ImageEdit {
	
	public BufferedImage image;
	public final int width;
	public final int height;
	
	public ImageEdit(BufferedImage image) {
		this.image = image;
		this.width = image.getWidth();
		this.height = image.getHeight();
	}
	
	
	
	public void motionBlur(BufferedImage baseImage, double angdeg, int radius) throws InterruptedException {
		final double theta = Math.toRadians(angdeg);
		final float yAngle = (float)Math.sin(theta), xAngle = (float)Math.cos(theta);
		final int ty = (int)((float)radius*(-yAngle)), tx = (int)((float)radius*xAngle);
		final int diameter = radius*2 + 1;
		final int wm = width-1, hm = height-1;

		Thread quadrant1 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(int x = width/2; x < width; x++) {
					for(int y = 0; y < height/2; y++) {
						int sy = y+ty, sx = x+tx;
						int r = 0, g = 0, b = 0;
						for(int dist = 0; dist < diameter; dist++) {
							int newX = (int)(sx + dist*(-xAngle)), newY = (int)(sy + dist*(-yAngle));
							if(newX > wm) newX = wm;
							if(newX < 0) newX = 0;
							if(newY > hm) newY = hm;
							if(newY < 0) newY = 0;
							
							int rgb = baseImage.getRGB(newX, newY);
							r += ((rgb >> 16) & 0xff);
							g += ((rgb >> 8) & 0xff);
							b += (rgb & 0xff);
						}
						image.setRGB(x, y, ((0xff << 24) | (r/diameter << 16) | (g/diameter << 8) | b/diameter));
					}
				}
			}
		});
		
		Thread quadrant2 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(int x = width/2; x < width; x++) {
					for(int y = height/2; y < height; y++) {
						int sy = y+ty, sx = x+tx;
						int r = 0, g = 0, b = 0;
						for(int dist = 0; dist < diameter; dist++) {
							int newX = (int)(sx + dist*(-xAngle)), newY = (int)(sy + dist*(-yAngle));
							if(newX > wm) newX = wm;
							if(newX < 0) newX = 0;
							if(newY > hm) newY = hm;
							if(newY < 0) newY = 0;
							
							int rgb = baseImage.getRGB(newX, newY);
							r += ((rgb >> 16) & 0xff);
							g += ((rgb >> 8) & 0xff);
							b += (rgb & 0xff);
						}
						image.setRGB(x, y, ((0xff << 24) | (r/diameter << 16) | (g/diameter << 8) | b/diameter));
					}
				}
			}
		});
		
		Thread quadrant3 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(int x = 0; x < width/2; x++) {
					for(int y = height/2; y < height; y++) {
						int sy = y+ty, sx = x+tx;
						int r = 0, g = 0, b = 0;
						for(int dist = 0; dist < diameter; dist++) {
							int newX = (int)(sx + dist*(-xAngle)), newY = (int)(sy + dist*(-yAngle));
							if(newX > wm) newX = wm;
							if(newX < 0) newX = 0;
							if(newY > hm) newY = hm;
							if(newY < 0) newY = 0;
							
							int rgb = baseImage.getRGB(newX, newY);
							r += ((rgb >> 16) & 0xff);
							g += ((rgb >> 8) & 0xff);
							b += (rgb & 0xff);
						}
						image.setRGB(x, y, ((0xff << 24) | (r/diameter << 16) | (g/diameter << 8) | b/diameter));
					}
				}
			}
		});
		
		Thread quadrant4 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(int x = 0; x < width/2; x++) {
					for(int y = 0; y < height/2; y++) {
						int sy = y+ty, sx = x+tx;
						int r = 0, g = 0, b = 0;
						for(int dist = 0; dist < diameter; dist++) {
							int newX = (int)(sx + dist*(-xAngle)), newY = (int)(sy + dist*(-yAngle));
							if(newX > wm) newX = wm;
							if(newX < 0) newX = 0;
							if(newY > hm) newY = hm;
							if(newY < 0) newY = 0;
							
							int rgb = baseImage.getRGB(newX, newY);
							r += ((rgb >> 16) & 0xff);
							g += ((rgb >> 8) & 0xff);
							b += (rgb & 0xff);
						}
						image.setRGB(x, y, ((0xff << 24) | (r/diameter << 16) | (g/diameter << 8) | b/diameter));
					}
				}
			}
		});
		
		quadrant1.start();
		quadrant2.start();
		quadrant3.start();
		quadrant4.start();
		
		quadrant1.join();
		quadrant2.join();
		quadrant3.join();
		quadrant4.join();
	}
	
	public void desaturate(BufferedImage baseImage, float factor) throws InterruptedException {
		Thread quadrant1 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(int x = width/2; x < width; x++) {
					for(int y = 0; y < height/2; y++) {
						int rgb = baseImage.getRGB(x, y);
						float[] hsb = Color.RGBtoHSB(((rgb >> 16) & 0xff), ((rgb >> 8) & 0xff), (rgb & 0xff), null);
						image.setRGB(x, y, Color.HSBtoRGB(hsb[0], Math.min(Math.max(0, hsb[1] - (hsb[1]*factor)), 1), hsb[2]));
					}
				}
			}
		});
		
		Thread quadrant2 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(int x = width/2; x < width; x++) {
					for(int y = height/2; y < height; y++) {
						int rgb = baseImage.getRGB(x, y);
						float[] hsb = Color.RGBtoHSB(((rgb >> 16) & 0xff), ((rgb >> 8) & 0xff), (rgb & 0xff), null);
						image.setRGB(x, y, Color.HSBtoRGB(hsb[0], Math.min(Math.max(0, hsb[1] - (hsb[1]*factor)), 1), hsb[2]));
					}
				}
			}
		});
		
		Thread quadrant3 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(int x = 0; x < width/2; x++) {
					for(int y = height/2; y < height; y++) {
						int rgb = baseImage.getRGB(x, y);
						float[] hsb = Color.RGBtoHSB(((rgb >> 16) & 0xff), ((rgb >> 8) & 0xff), (rgb & 0xff), null);
						image.setRGB(x, y, Color.HSBtoRGB(hsb[0], Math.min(Math.max(0, hsb[1] - (hsb[1]*factor)), 1), hsb[2]));
					}
				}
			}
		});
		
		Thread quadrant4 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(int x = 0; x < width/2; x++) {
					for(int y = 0; y < height/2; y++) {
						int rgb = baseImage.getRGB(x, y);
						float[] hsb = Color.RGBtoHSB(((rgb >> 16) & 0xff), ((rgb >> 8) & 0xff), (rgb & 0xff), null);
						image.setRGB(x, y, Color.HSBtoRGB(hsb[0], Math.min(Math.max(0, hsb[1] - (hsb[1]*factor)), 1), hsb[2]));
					}
				}
			}
		});
		
		quadrant1.start();
		quadrant2.start();
		quadrant3.start();
		quadrant4.start();
		
		quadrant1.join();
		quadrant2.join();
		quadrant3.join();
		quadrant4.join();
	}
	
	public void saturate(BufferedImage baseImage, float factor) throws InterruptedException {
		Thread quadrant1 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(int x = width/2; x < width; x++) {
					for(int y = 0; y < height/2; y++) {
						int rgb = baseImage.getRGB(x, y);
						float[] hsb = Color.RGBtoHSB(((rgb >> 16) & 0xff), ((rgb >> 8) & 0xff), (rgb & 0xff), null);
						image.setRGB(x, y, Color.HSBtoRGB(hsb[0], Math.min(Math.max(0, hsb[1] + (hsb[1]*factor)), 1), hsb[2]));
					}
				}
			}
		});
		
		Thread quadrant2 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(int x = width/2; x < width; x++) {
					for(int y = height/2; y < height; y++) {
						int rgb = baseImage.getRGB(x, y);
						float[] hsb = Color.RGBtoHSB(((rgb >> 16) & 0xff), ((rgb >> 8) & 0xff), (rgb & 0xff), null);
						image.setRGB(x, y, Color.HSBtoRGB(hsb[0], Math.min(Math.max(0, hsb[1] + (hsb[1]*factor)), 1), hsb[2]));
					}
				}
			}
		});
		
		Thread quadrant3 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(int x = 0; x < width/2; x++) {
					for(int y = height/2; y < height; y++) {
						int rgb = baseImage.getRGB(x, y);
						float[] hsb = Color.RGBtoHSB(((rgb >> 16) & 0xff), ((rgb >> 8) & 0xff), (rgb & 0xff), null);
						image.setRGB(x, y, Color.HSBtoRGB(hsb[0], Math.min(Math.max(0, hsb[1] + (hsb[1]*factor)), 1), hsb[2]));
					}
				}
			}
		});
		
		Thread quadrant4 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(int x = 0; x < width/2; x++) {
					for(int y = 0; y < height/2; y++) {
						int rgb = baseImage.getRGB(x, y);
						float[] hsb = Color.RGBtoHSB(((rgb >> 16) & 0xff), ((rgb >> 8) & 0xff), (rgb & 0xff), null);
						image.setRGB(x, y, Color.HSBtoRGB(hsb[0], Math.min(Math.max(0, hsb[1] + (hsb[1]*factor)), 1), hsb[2]));
					}
				}
			}
		});
		
		quadrant1.start();
		quadrant2.start();
		quadrant3.start();
		quadrant4.start();
		
		quadrant1.join();
		quadrant2.join();
		quadrant3.join();
		quadrant4.join();
	}
	
	public void glow(BufferedImage baseImage, int factor) throws InterruptedException {
		if(factor < 0) return;
		boxBlur(baseImage, 500 - factor);
		Thread quadrant1 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(int x = width/2; x < width; x++) {
					for(int y = 0; y < height/2; y++) {
						Color baseColor = new Color(baseImage.getRGB(x, y));
						Color blurColor = new Color(image.getRGB(x, y));
						int r = baseColor.getRed()+blurColor.getRed();
						int g = baseColor.getGreen()+blurColor.getGreen();
						int b = baseColor.getBlue()+blurColor.getBlue();
						image.setRGB(x, y, (0xff << 24) | (clamp(r) << 16) | (clamp(g) << 8) | clamp(b));
					}
				}
			}
		});
		
		Thread quadrant2 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(int x = width/2; x < width; x++) {
					for(int y = height/2; y < height; y++) {
						Color baseColor = new Color(baseImage.getRGB(x, y));
						Color blurColor = new Color(image.getRGB(x, y));
						int r = baseColor.getRed()+blurColor.getRed();
						int g = baseColor.getGreen()+blurColor.getGreen();
						int b = baseColor.getBlue()+blurColor.getBlue();
						image.setRGB(x, y, (0xff << 24) | (clamp(r) << 16) | (clamp(g) << 8) | clamp(b));
					}
				}
			}
		});
		
		Thread quadrant3 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(int x = 0; x < width/2; x++) {
					for(int y = height/2; y < height; y++) {
						Color baseColor = new Color(baseImage.getRGB(x, y));
						Color blurColor = new Color(image.getRGB(x, y));
						int r = baseColor.getRed()+blurColor.getRed();
						int g = baseColor.getGreen()+blurColor.getGreen();
						int b = baseColor.getBlue()+blurColor.getBlue();
						image.setRGB(x, y, (0xff << 24) | (clamp(r) << 16) | (clamp(g) << 8) | clamp(b));
					}
				}
			}
		});
		
		Thread quadrant4 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(int x = 0; x < width/2; x++) {
					for(int y = 0; y < height/2; y++) {
						Color baseColor = new Color(baseImage.getRGB(x, y));
						Color blurColor = new Color(image.getRGB(x, y));
						int r = baseColor.getRed()+blurColor.getRed();
						int g = baseColor.getGreen()+blurColor.getGreen();
						int b = baseColor.getBlue()+blurColor.getBlue();
						image.setRGB(x, y, (0xff << 24) | (clamp(r) << 16) | (clamp(g) << 8) | clamp(b));
					}
				}
			}
		});
		
		quadrant1.start();
		quadrant2.start();
		quadrant3.start();
		quadrant4.start();
		
		quadrant1.join();
		quadrant2.join();
		quadrant3.join();
		quadrant4.join();
	}
	
	public void cannyEdgeDetection(BufferedImage baseImage, int low, int high, float gaussianSigma) {
		try {
			grayScale(baseImage);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		gaussianBlur(baseImage, gaussianSigma);
		this.image = CannyEdgeDetector.detectEdges(this.image, high, low);
	}

	public void boxBlur(BufferedImage baseImage, int radius) throws InterruptedException {
		/* This is just like any other separated box blur, with the exception where at each step, the blurred pixel value is simply the average of all the pixels in this range. 
		 * This can be calculated quickly by keeping a running total at each point. what makes this fast is when you move the range to the right (down) one pixel, you subtract 
		 * the pixel at the left (top) end and add the pixel at the right (bottom) end. You still have to divide these running totals by 2r+1, but this can be sped up by precomputing 
		 * fixed-point values of n/(2r+1) for (0<=n<256) and storing them in dv[]
		 */
		
		if(radius <= 0) return;
		final int diameter = radius*2 + 1;
		final BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		final int[] dv = new int[256*diameter];
		for(int i = 0; i < dv.length; i++) dv[i] = i/diameter;
		
		Thread quadrant1 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				boxBlurAreaH(0, 0, width/2, height/2, radius, dv, newImage, baseImage);
			}
		});
		
		Thread quadrant2 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				boxBlurAreaH(width/2, 0, width, height/2, radius, dv, newImage, baseImage);
			}
		});
		
		Thread quadrant3 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				boxBlurAreaH(width/2, height/2, width, height, radius, dv, newImage, baseImage);
			}
		});
		
		Thread quadrant4 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				boxBlurAreaH(0, height/2, width/2, height, radius, dv, newImage, baseImage);
			}
		});
		
		quadrant1.start();
		quadrant2.start();
		quadrant3.start();
		quadrant4.start();
		
		quadrant1.join();
		quadrant2.join();
		quadrant3.join();
		quadrant4.join();
		

		Thread quadrant12 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				boxBlurAreaV(0, 0, width/2, height/2, radius, dv, newImage);
			}
		});
		
		Thread quadrant22 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				boxBlurAreaV(width/2, 0, width, height/2, radius, dv, newImage);
			}
		});
		
		Thread quadrant32 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				boxBlurAreaV(width/2, height/2, width, height, radius, dv, newImage);
			}
		});
		
		Thread quadrant42 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				boxBlurAreaV(0, height/2, width/2, height, radius, dv, newImage);
			}
		});
		
		quadrant12.start();
		quadrant22.start();
		quadrant32.start();
		quadrant42.start();
		
		quadrant12.join();
		quadrant22.join();
		quadrant32.join();
		quadrant42.join();
	}
	
	public void gaussianBlur(BufferedImage baseImage, float sigma) {
		int size = (int)(sigma*6.0);
		if(size%2 == 0) size--;
		int half = (size-1)/2;
		float sigmaDenominator = 2.0f*(sigma*sigma);
		float sigmaCalculation = 1.0f/(sigmaDenominator*(float)Math.PI);
		float[] kernel = new float[size*size];
		for(int x = -half; x <= half; x++) {
			for(int y = -half; y <= half; y++) {
				int realX = x+half, realY = y+half;
				kernel[realX + realY*size] = sigmaCalculation*(float)Math.pow(Math.E, -((x*x + y*y)/sigmaDenominator));
			}
		}
		try {
			applyKernel(baseImage, kernel, size, size);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void posterize(BufferedImage baseImage, int factor) throws InterruptedException {
		if(factor < 1 || factor > 8) return;
		final int level = (int) Math.pow(2, factor);
		Thread quadrant1 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(int x = width/2; x < width; x++) {
					for(int y = 0; y < height/2; y++) {
						int rgb = baseImage.getRGB(x, y);
						int red =(0xff & (rgb>>16));
						int green = (0xff & (rgb>>8));
						int blue = (0xff & rgb);
						red  = (red-(red%level));
						green= (green-(green%level));
						blue = (blue-(blue%level));
						if (red > 255) red =255;if(red<0) red=0;
						if (green > 255) green=255;if(green<0) green=0;
						if (blue >255) blue =255;if(blue<0) blue=0;
						rgb = (0xff000000 | red << 16 | green <<8 | blue);
						image.setRGB(x, y, rgb);
					}
				}
			}
		});
		
		Thread quadrant2 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(int x = width/2; x < width; x++) {
					for(int y = height/2; y < height; y++) {
						int rgb = baseImage.getRGB(x, y);
						int red =(0xff & (rgb>>16));
						int green = (0xff & (rgb>>8));
						int blue = (0xff & rgb);
						red  = (red-(red%level));
						green= (green-(green%level));
						blue = (blue-(blue%level));
						if (red > 255) red =255;if(red<0) red=0;
						if (green > 255) green=255;if(green<0) green=0;
						if (blue >255) blue =255;if(blue<0) blue=0;
						rgb = (0xff000000 | red << 16 | green <<8 | blue);
						image.setRGB(x, y, rgb);
					}
				}
			}
		});
		
		Thread quadrant3 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(int x = 0; x < width/2; x++) {
					for(int y = height/2; y < height; y++) {
						int rgb = baseImage.getRGB(x, y);
						int red =(0xff & (rgb>>16));
						int green = (0xff & (rgb>>8));
						int blue = (0xff & rgb);
						red  = (red-(red%level));
						green= (green-(green%level));
						blue = (blue-(blue%level));
						if (red > 255) red =255;if(red<0) red=0;
						if (green > 255) green=255;if(green<0) green=0;
						if (blue >255) blue =255;if(blue<0) blue=0;
						rgb = (0xff000000 | red << 16 | green <<8 | blue);
						image.setRGB(x, y, rgb);
					}
				}
			}
		});
		
		Thread quadrant4 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(int x = 0; x < width/2; x++) {
					for(int y = 0; y < height/2; y++) {
						int rgb = baseImage.getRGB(x, y);
						int red =(0xff & (rgb>>16));
						int green = (0xff & (rgb>>8));
						int blue = (0xff & rgb);
						red  = (red-(red%level));
						green= (green-(green%level));
						blue = (blue-(blue%level));
						if (red > 255) red =255;if(red<0) red=0;
						if (green > 255) green=255;if(green<0) green=0;
						if (blue >255) blue =255;if(blue<0) blue=0;
						rgb = (0xff000000 | red << 16 | green <<8 | blue);
						image.setRGB(x, y, rgb);
					}
				}
			}
		});
		
		quadrant1.start();
		quadrant2.start();
		quadrant3.start();
		quadrant4.start();
		
		quadrant1.join();
		quadrant2.join();
		quadrant3.join();
		quadrant4.join();
	}
	
	public void glass(BufferedImage baseImage, int factor) throws InterruptedException {
		if(factor > 100 || factor < 1) return;
		Random random = new Random();
		int distance = factor*2 + 1;
		Thread quadrant1 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(int x = width/2; x < width; x++) {
					for(int y = 0; y < height/2; y++) {
						int xd = random.nextInt(distance);
						int yd = random.nextInt(distance);
						int xs = x - factor;
						int ys = y - factor;
						if(xs + xd < baseImage.getWidth() && xs + xd >= 0 && ys + yd < baseImage.getHeight() && ys + yd >= 0) {
							image.setRGB(x, y, baseImage.getRGB(xs + xd, ys + yd));
						}
					}
				}
			}
		});
		
		Thread quadrant2 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(int x = width/2; x < width; x++) {
					for(int y = height/2; y < height; y++) {
						int xd = random.nextInt(distance);
						int yd = random.nextInt(distance);
						int xs = x - factor;
						int ys = y - factor;
						if(xs + xd < baseImage.getWidth() && xs + xd >= 0 && ys + yd < baseImage.getHeight() && ys + yd >= 0) {
							image.setRGB(x, y, baseImage.getRGB(xs + xd, ys + yd));
						}
					}
				}
			}
		});
		
		Thread quadrant3 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(int x = 0; x < width/2; x++) {
					for(int y = height/2; y < height; y++) {
						int xd = random.nextInt(distance);
						int yd = random.nextInt(distance);
						int xs = x - factor;
						int ys = y - factor;
						if(xs + xd < baseImage.getWidth() && xs + xd >= 0 && ys + yd < baseImage.getHeight() && ys + yd >= 0) {
							image.setRGB(x, y, baseImage.getRGB(xs + xd, ys + yd));
						}
					}
				}
			}
		});
		
		Thread quadrant4 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(int x = 0; x < width/2; x++) {
					for(int y = 0; y < height/2; y++) {
						int xd = random.nextInt(distance);
						int yd = random.nextInt(distance);
						int xs = x - factor;
						int ys = y - factor;
						if(xs + xd < baseImage.getWidth() && xs + xd >= 0 && ys + yd < baseImage.getHeight() && ys + yd >= 0) {
							image.setRGB(x, y, baseImage.getRGB(xs + xd, ys + yd));
						}
					}
				}
			}
		});
		
		quadrant1.start();
		quadrant2.start();
		quadrant3.start();
		quadrant4.start();
		
		quadrant1.join();
		quadrant2.join();
		quadrant3.join();
		quadrant4.join();
	}
	
	public void invert(BufferedImage baseImage) throws InterruptedException {
		Thread quadrant1 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(int x = width/2; x < width; x++) {
					for(int y = 0; y < height/2; y++) {
						int rgb = baseImage.getRGB(x, y);
						image.setRGB(x, y, (0xff << 24) | (255 - ((rgb >> 16) & 0xff) << 16) | (255 -  ((rgb >> 8) & 0xff) << 8) | 255 -  (rgb & 0xff));
					}
				}
			}
		});
		
		Thread quadrant2 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(int x = width/2; x < width; x++) {
					for(int y = height/2; y < height; y++) {
						int rgb = baseImage.getRGB(x, y);
						image.setRGB(x, y, (0xff << 24) | (255 - ((rgb >> 16) & 0xff) << 16) | (255 -  ((rgb >> 8) & 0xff) << 8) | 255 -  (rgb & 0xff));
					}
				}
			}
		});
		
		Thread quadrant3 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(int x = 0; x < width/2; x++) {
					for(int y = height/2; y < height; y++) {
						int rgb = baseImage.getRGB(x, y);
						image.setRGB(x, y, (0xff << 24) | (255 - ((rgb >> 16) & 0xff) << 16) | (255 -  ((rgb >> 8) & 0xff) << 8) | 255 -  (rgb & 0xff));
					}
				}
			}
		});
		
		Thread quadrant4 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(int x = 0; x < width/2; x++) {
					for(int y = 0; y < height/2; y++) {
						int rgb = baseImage.getRGB(x, y);
						image.setRGB(x, y, (0xff << 24) | (255 - ((rgb >> 16) & 0xff) << 16) | (255 -  ((rgb >> 8) & 0xff) << 8) | 255 -  (rgb & 0xff));
					}
				}
			}
		});
		
		quadrant1.start();
		quadrant2.start();
		quadrant3.start();
		quadrant4.start();
		
		quadrant1.join();
		quadrant2.join();
		quadrant3.join();
		quadrant4.join();
	}
	
	public void grayScale(BufferedImage baseImage) throws InterruptedException {
		Thread quadrant1 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(int x = width/2; x < width; x++) {
					for(int y = 0; y < height/2; y++) {
						int rgb1 = baseImage.getRGB(x, y);
						int rgb = (int)((rgb1 & 0xff) *0.114) + (int)(((rgb1 >> 16) & 0xff) * 0.299) + (int)(((rgb1 >> 8) & 0xff) * 0.587);
						image.setRGB(x, y, (0xff << 24) | (clamp(rgb) << 16) | (clamp(rgb) << 8) | clamp(rgb));
					}
				}
			}
		});
		
		Thread quadrant2 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(int x = width/2; x < width; x++) {
					for(int y = height/2; y < height; y++) {
						int rgb1 = baseImage.getRGB(x, y);
						int rgb = (int)((rgb1 & 0xff) *0.114) + (int)(((rgb1 >> 16) & 0xff) * 0.299) + (int)(((rgb1 >> 8) & 0xff) * 0.587);
						image.setRGB(x, y, (0xff << 24) | (clamp(rgb) << 16) | (clamp(rgb) << 8) | clamp(rgb));
					}
				}
			}
		});
		
		Thread quadrant3 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(int x = 0; x < width/2; x++) {
					for(int y = height/2; y < height; y++) {
						int rgb1 = baseImage.getRGB(x, y);
						int rgb = (int)((rgb1 & 0xff) *0.114) + (int)(((rgb1 >> 16) & 0xff) * 0.299) + (int)(((rgb1 >> 8) & 0xff) * 0.587);
						image.setRGB(x, y, (0xff << 24) | (clamp(rgb) << 16) | (clamp(rgb) << 8) | clamp(rgb));
					}
				}
			}
		});
		
		Thread quadrant4 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(int x = 0; x < width/2; x++) {
					for(int y = 0; y < height/2; y++) {
						int rgb1 = baseImage.getRGB(x, y);
						int rgb = (int)((rgb1 & 0xff) *0.114) + (int)(((rgb1 >> 16) & 0xff) * 0.299) + (int)(((rgb1 >> 8) & 0xff) * 0.587);
						image.setRGB(x, y, (0xff << 24) | (clamp(rgb) << 16) | (clamp(rgb) << 8) | clamp(rgb));
					}
				}
			}
		});
		
		quadrant1.start();
		quadrant2.start();
		quadrant3.start();
		quadrant4.start();
		
		quadrant1.join();
		quadrant2.join();
		quadrant3.join();
		quadrant4.join();
	}
	
	public void sharpen(BufferedImage baseImage, float factor) {
		final float[] sharpenKernel = {-factor/9.0f, -factor/9.0f, -factor/9.0f, -factor/9.0f, 1.0f+(8.0f*factor)/9.0f, -factor/9.0f, -factor/9.0f, -factor/9.0f, -factor/9.0f};
		try {
			applyKernel(baseImage, sharpenKernel, 3, 3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Helper Functions
	 */
	private void applyKernel(BufferedImage baseImage, float[] kernel, int kernelWidth, int kernelHeight) throws InterruptedException {
		int hRadius = (kernelWidth - 1)/2, vRadius = (kernelHeight - 1)/2;
		Thread quadrant1 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(int x = width/2; x < width; x++) {
					for(int y = 0; y < height/2; y++) {
						float r = 0, g = 0, b = 0;
						for(int xd = -hRadius; xd <= hRadius; xd++) {
							for(int yd = -vRadius; yd <= vRadius; yd++) {
								float f = kernel[(xd+hRadius) + (yd+vRadius)*kernelWidth];
								
								int newX = x+xd, newY = y+yd;
								if(newX >= width) newX = width-1;
								if(newX < 0) newX = 0;
								if(newY >= height) newY = height-1;
								if(newY < 0) newY = 0;
								
								int rgb = baseImage.getRGB(newX, newY);
								r += f * (float)((rgb >> 16) & 0xff);
								g += f * (float)((rgb >> 8) & 0xff);
								b += f * (float)(rgb & 0xff);
							}
						}
						
						image.setRGB(x, y, (0xff << 24) | (clamp(r) << 16) | (clamp(g) << 8) | clamp(b));
					}
				}
			}
		});
		
		Thread quadrant2 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(int x = 0; x < width/2; x++) {
					for(int y = 0; y < height/2; y++) {
						float r = 0, g = 0, b = 0;
						for(int xd = -hRadius; xd <= hRadius; xd++) {
							for(int yd = -vRadius; yd <= vRadius; yd++) {
								float f = kernel[(xd+hRadius) + (yd+vRadius)*kernelWidth];
								
								int newX = x+xd, newY = y+yd;
								if(newX >= width) newX = width-1;
								if(newX < 0) newX = 0;
								if(newY >= height) newY = height-1;
								if(newY < 0) newY = 0;
								
								int rgb = baseImage.getRGB(newX, newY);
								r += f * (float)((rgb >> 16) & 0xff);
								g += f * (float)((rgb >> 8) & 0xff);
								b += f * (float)(rgb & 0xff);
							}
						}
						
						image.setRGB(x, y, (0xff << 24) | (clamp(r) << 16) | (clamp(g) << 8) | clamp(b));
					}
				}
			}
		});
		
		Thread quadrant3 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(int x = 0; x < width/2; x++) {
					for(int y = height/2; y < height; y++) {
						float r = 0, g = 0, b = 0;
						for(int xd = -hRadius; xd <= hRadius; xd++) {
							for(int yd = -vRadius; yd <= vRadius; yd++) {
								float f = kernel[(xd+hRadius) + (yd+vRadius)*kernelWidth];
								
								int newX = x+xd, newY = y+yd;
								if(newX >= width) newX = width-1;
								if(newX < 0) newX = 0;
								if(newY >= height) newY = height-1;
								if(newY < 0) newY = 0;
								
								int rgb = baseImage.getRGB(newX, newY);
								r += f * (float)((rgb >> 16) & 0xff);
								g += f * (float)((rgb >> 8) & 0xff);
								b += f * (float)(rgb & 0xff);
							}
						}
						
						image.setRGB(x, y, (0xff << 24) | (clamp(r) << 16) | (clamp(g) << 8) | clamp(b));
					}
				}
			}
		});
		
		Thread quadrant4 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(int x = width/2; x < width; x++) {
					for(int y = height/2; y < height; y++) {
						float r = 0, g = 0, b = 0;
						for(int xd = -hRadius; xd <= hRadius; xd++) {
							for(int yd = -vRadius; yd <= vRadius; yd++) {
								float f = kernel[(xd+hRadius) + (yd+vRadius)*kernelWidth];
								
								int newX = x+xd, newY = y+yd;
								if(newX >= width) newX = width-1;
								if(newX < 0) newX = 0;
								if(newY >= height) newY = height-1;
								if(newY < 0) newY = 0;
								
								int rgb = baseImage.getRGB(newX, newY);
								r += f * (float)((rgb >> 16) & 0xff);
								g += f * (float)((rgb >> 8) & 0xff);
								b += f * (float)(rgb & 0xff);
							}
						}
						
						image.setRGB(x, y, (0xff << 24) | (clamp(r) << 16) | (clamp(g) << 8) | clamp(b));
					}
				}
			}
		});
		
		quadrant1.start();
		quadrant2.start();
		quadrant3.start();
		quadrant4.start();
		
		quadrant1.join();
		quadrant2.join();
		quadrant3.join();
		quadrant4.join();
	}
	
	private int clamp(float pixelValue) {
		int newValue = (int)(pixelValue+0.5f);
		if(newValue > 255) newValue = 255;
		if(newValue < 0) newValue = 0;
		return newValue;
	}
	
	private void boxBlurAreaH(int x1, int y1, int x2, int y2, int radius, int[] dv, BufferedImage newImage, BufferedImage baseImage) {
		final int hm = height-1;
		int rsum, gsum, bsum, rgb, back, front;
		for(int x = x1; x < x2; x++) {
			rsum=gsum=bsum=0;
			for(int yd = -radius; yd <= radius; yd++) {
				rgb = baseImage.getRGB(x, Math.max(yd+y1, 0));
				rsum += (rgb & 0xff0000)>>16;
				gsum += (rgb & 0x00ff00)>>8;
				bsum += rgb & 0x0000ff;
			}
			for(int y = y1; y < y2; y++) {
				newImage.setRGB(x, y, (0xff << 24) | ((dv[rsum] & 0xff) << 16) | ((dv[gsum] & 0xff) << 8) | (dv[bsum] & 0xff));
				
				back = baseImage.getRGB(x, Math.max(y - radius, 0));
				front = baseImage.getRGB(x, Math.min(y + radius + 1, hm));
				
				rsum += ((front & 0xff0000)-(back & 0xff0000))>>16;
				gsum += ((front & 0x00ff00)-(back & 0x00ff00))>>8;
				bsum += (front & 0x0000ff)-(back & 0x0000ff);
			}
		}
	}
	
	private void boxBlurAreaV(int x1, int y1, int x2, int y2, int radius, int[] dv, BufferedImage newImage) {
		final int wm = width-1;
		int rsum, gsum, bsum, rgb, back, front;
		
		for(int y = y1; y < y2; y++) {
			rsum=gsum=bsum=0;
			for(int xd = -radius; xd <= radius; xd++) {
				rgb = newImage.getRGB(Math.max(xd+x1, 0), y);
				rsum += (rgb & 0xff0000)>>16;
				gsum += (rgb & 0x00ff00)>>8;
				bsum += (rgb & 0x0000ff);
			}
			for(int x = x1; x < x2; x++) {
				image.setRGB(x, y, (0xff << 24) | ((dv[rsum] & 0xff) << 16) | ((dv[gsum] & 0xff) << 8) | (dv[bsum] & 0xff));
				
				back = newImage.getRGB(Math.max(x-radius, 0), y);
				front = newImage.getRGB(Math.min(x + radius + 1, wm), y);
				
				rsum += ((front & 0xff0000)-(back & 0xff0000))>>16;
				gsum += ((front & 0x00ff00)-(back & 0x00ff00))>>8;
				bsum += (front & 0x0000ff)-(back & 0x0000ff);
			}
		}
	}
}