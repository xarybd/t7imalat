import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.util.Random;

public class PerlinNoiseFlowEffect extends JPanel {

    private BufferedImage image;
    private int[][] noise;
    private int width, height;

    public PerlinNoiseFlowEffect() {
        try {
            image = ImageIO.read(new File("background.jpg")); // Resmin yolu
            width = image.getWidth();
            height = image.getHeight();
            noise = generatePerlinNoise(width, height);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Timer timer = new Timer(30, e -> {
            updateNoise();
            repaint();
        });
        timer.start();
    }

    private void updateNoise() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                noise[y][x] += 1;
            }
        }
    }

    private int[][] generatePerlinNoise(int width, int height) {
        int[][] noise = new int[height][width];
        Random rand = new Random();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                noise[y][x] = rand.nextInt(256);
            }
        }
        return noise;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int pixel = image.getRGB(x, y);
                    int alpha = (pixel >> 24) & 0xff;
                    int red = (pixel >> 16) & 0xff;
                    int green = (pixel >> 8) & 0xff;
                    int blue = pixel & 0xff;

                    int noiseValue = noise[y][x];
                    red = (red + noiseValue) / 2;
                    green = (green + noiseValue) / 2;
                    blue = (blue + noiseValue) / 2;

                    int newPixel = (alpha << 24) | (red << 16) | (green << 8) | blue;
                    newImage.setRGB(x, y, newPixel);
                }
            }
            g.drawImage(newImage, 0, 0, getWidth(), getHeight(), null);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Perlin Noise Flow Effect");
        PerlinNoiseFlowEffect panel = new PerlinNoiseFlowEffect();
        frame.add(panel);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
