package main;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Main {
	
	public static void main(String[] args) {
		Image randomImage = null;
		try {
			randomImage = ImageIO.read(new File("random-image-file"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		ImageEdit imageEdit = new ImageEdit((BufferedImage)randomImage);
		
		
		/*
		 * Change image through the imageEdit functions
		 */
		
		
		File file = new File("random-image-output-file");
		try {
			ImageIO.write(imageEdit.image, "jpg", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
