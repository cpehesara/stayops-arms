package com.example.stayops.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;

public class ImageConverterUtil {

    public static File base64ToPng(String base64Image, String outputPath) throws IOException {
        if (base64Image.contains(",")){
            base64Image = base64Image.split(",")[1];
        }

        byte[] imageBytes = Base64.getDecoder().decode(base64Image);
        try (OutputStream out = new FileOutputStream(outputPath)){
            out.write(imageBytes);
        }
        return new File(outputPath);
    }

    public static String pngToBase64(File file) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(file);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", outputStream);

        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }

    public BufferedImage base64ToBufferedImage (String base64Image) throws IOException {
        if (base64Image.contains(",")){
            base64Image = base64Image.split(",")[1];
        }
        byte[] imageBytes = Base64.getDecoder().decode(base64Image);
        try (InputStream in = new ByteArrayInputStream(imageBytes)){
            return ImageIO.read(in);
        }
    }

    public static String bufferedImageToBase64(BufferedImage image) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", outputStream);
        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }
}
