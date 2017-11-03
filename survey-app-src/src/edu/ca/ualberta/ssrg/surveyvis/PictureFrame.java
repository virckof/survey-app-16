package edu.ca.ualberta.ssrg.surveyvis;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 * This class encapsulates potential picture frames required by the questions
 */
public class PictureFrame extends JPanel{

	/**
	 * Creates the panel for the question pictures
	 */

	private static final long serialVersionUID = 1L;

	private BufferedImage image;

	public PictureFrame(BufferedImage img) {
		this.image = img;
	}

	@Override
    protected void paintComponent(Graphics g) {
		int maxHeight = MainWindow.IMAGE_HEIGHT;
		int maxWidth = MainWindow.WIDTH - MainWindow.BORDER;

		// Get dimensions to resize the image
		Dimension dim = getScaleDimensions(image, maxWidth, maxHeight);

		// Resize the image to fill the frame
	    Image tmp = image.getScaledInstance((int)dim.getWidth(), (int)dim.getHeight(), Image.SCALE_SMOOTH);
	    BufferedImage sizedImage = new BufferedImage(maxWidth, maxHeight, BufferedImage.TYPE_INT_ARGB);

	    Graphics2D g2d = sizedImage.createGraphics();
	    g2d.drawImage(tmp, 0, 0, null);
	    g2d.dispose();

	    // Draw the image to the frame
        super.paintComponent(g);
        g.drawImage(sizedImage, 0, 0, null); // see javadoc for more info on the parameters
    }

	// Get dimensions to scale an image to a max width and height
	private Dimension getScaleDimensions(BufferedImage image, int maxWidth, int maxHeight) {
		float aspectRatio = (float)image.getHeight() / (float)image.getWidth();

		// Scale image to the max height, adjusting width with respect to aspect ratio
		int scaleToHeight = maxHeight;
		int scaleToWidth = (int) (maxHeight / aspectRatio);

		// If width is still too big, scale image to max width,
		//  adjusting height  with respect to aspect ratio
		if (scaleToWidth > maxWidth) {
			scaleToWidth = maxWidth;
			scaleToHeight = (int) (scaleToWidth * aspectRatio);
		}

		return new Dimension(scaleToWidth, scaleToHeight);
	}

}
