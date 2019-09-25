package com.example.measurehearthrate.Processing;

import android.media.Image;
import android.util.Log;

import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * This abstract class is used to process images.
 *
 */
public class ImageProcessing {
    public ImageProcessing(){};
    public ArrayList<Double> yuv2rgb(byte[] yuv, int width, int height) {
        ArrayList<Double> listr = new ArrayList<>();
        ArrayList<Double> listg = new ArrayList<>();
        ArrayList<Double> listb = new ArrayList<>();
        int total = width * height;
        int[] rgb = new int[total];
        int Y, Cb = 0, Cr = 0, index = 0;
        int R, G, B;
        int numR = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Y = yuv[y * width + x];
                if (Y < 0) Y += 255;

                if ((x & 1) == 0) {
                    Cr = yuv[(y >> 1) * (width) + x + total];
                    Cb = yuv[(y >> 1) * (width) + x + total + 1];

                    if (Cb < 0) Cb += 127; else Cb -= 128;
                    if (Cr < 0) Cr += 127; else Cr -= 128;
                }

                R = Y + Cr + (Cr >> 2) + (Cr >> 3) + (Cr >> 5);
                G = Y - (Cb >> 2) + (Cb >> 4) + (Cb >> 5) - (Cr >> 1) + (Cr >> 3) + (Cr >> 4) + (Cr >> 5);
                B = Y + Cb + (Cb >> 1) + (Cb >> 2) + (Cb >> 6);

                // Approximation
				R = (int) (Y + 1.40200 * Cr);
			    G = (int) (Y - 0.34414 * Cb - 0.71414 * Cr);
				B = (int) (Y + 1.77200 * Cb);

                if (R < 0) R = 0; else if (R > 255) R = 255;
                if (G < 0) G = 0; else if (G > 255) G = 255;
                if (B < 0) B = 0; else if (B > 255) B = 255;

                listr.add(R * 1.0);
                listb.add(B * 1.0);
                listg.add(G * 1.0);
                if(R > G && R > B && R>=127 && G <= 80 && B <= 80) {
//                    Log.d("log:-", String.valueOf(index) + " " + String.valueOf(R) + " " + String.valueOf(G) + " " + String.valueOf(B));
                    numR += 1;
                }
                rgb[index++] = 0xff000000 + (R << 16) + (G << 8) + B;
            }
        }

        Double average = listr.stream().mapToDouble(val -> val).average().orElse(0.0);
        Double averageg = listg.stream().mapToDouble(val -> val).average().orElse(0.0);
        Double averageb = listb.stream().mapToDouble(val -> val).average().orElse(0.0);
        Log.d("SUM:-", String.valueOf(numR) + " " + String.valueOf(average) + " " + String.valueOf(averageg) + " " + String.valueOf(averageb));
        ArrayList<Double> res = new ArrayList<>();
        res.add((double)numR/(width*height));
        res.add(average);
        res.add(averageg);
        res.add(averageb);
        return res;
    }
    private int decodeYUV420SPtoRedSum(byte[] yuv420sp, int width, int height) {
        if (yuv420sp == null) return 0;
        final int frameSize = width * height;

        ArrayList<Double> listr = new ArrayList<>();
        ArrayList<Double> listg = new ArrayList<>();
        ArrayList<Double> listb = new ArrayList<>();

        int sum = 0;
        for (int j = 0, yp = 0; j < height; j++) {
            int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
            for (int i = 0; i < width; i++, yp++) {
                int y = (0xff & yuv420sp[yp]) - 16;
                if (y < 0) y = 0;
                if ((i & 1) == 0) {
                    v = (0xff & yuv420sp[uvp++]) - 128;
                    u = (0xff & yuv420sp[uvp++]) - 128;
                }
                int y1192 = 1192 * y;
                int r = (y1192 + 1634 * v);
                int g = (y1192 - 833 * v - 400 * u);
                int b = (y1192 + 2066 * u);

                if (r < 0) r = 0;
                else if (r > 262143) r = 262143;
                if (g < 0) g = 0;
                else if (g > 262143) g = 262143;
                if (b < 0) b = 0;
                else if (b > 262143) b = 262143;
                int pixel = 0xff000000 | ((r << 6) & 0xff0000) | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
                int red = (pixel >> 16) & 0xff;
                sum += red;
            }
        }
        return sum;
    }

    /**
     * Given a byte array representing a yuv420sp image, determine the average
     * amount of red in the image. Note: returns 0 if the byte array is NULL.
     *
     * @param yuv420sp
     *            Byte array representing a yuv420sp image
     * @param width
     *            Width of the image.
     * @param height
     *            Height of the image.
     * @return int representing the average amount of red in the image.
     */
    public ArrayList<Double> decodeYUV420SPtoRedAvg(byte[] yuv420sp, int width, int height) {
        if (yuv420sp == null) return new ArrayList();

        final int frameSize = width * height;

        // int sum = decodeYUV420SPtoRedSum(yuv420sp, width, height);
        return yuv2rgb(yuv420sp, width, height);
    }
    public byte[] YUV_420_888toNV21(Image image) {

        int width = image.getWidth();
        int height = image.getHeight();
        int ySize = width*height;
        int uvSize = width*height/4;

        byte[] nv21 = new byte[ySize + uvSize*2];

        ByteBuffer yBuffer = image.getPlanes()[0].getBuffer(); // Y
        ByteBuffer uBuffer = image.getPlanes()[1].getBuffer(); // U
        ByteBuffer vBuffer = image.getPlanes()[2].getBuffer(); // V

        int rowStride = image.getPlanes()[0].getRowStride();
        assert(image.getPlanes()[0].getPixelStride() == 1);

        int pos = 0;

        if (rowStride == width) { // likely
            yBuffer.get(nv21, 0, ySize);
            pos += ySize;
        }
        else {
            int yBufferPos = width - rowStride; // not an actual position
            for (; pos<ySize; pos+=width) {
                yBufferPos += rowStride - width;
                yBuffer.position(yBufferPos);
                yBuffer.get(nv21, pos, width);
            }
        }

        rowStride = image.getPlanes()[2].getRowStride();
        int pixelStride = image.getPlanes()[2].getPixelStride();

        assert(rowStride == image.getPlanes()[1].getRowStride());
        assert(pixelStride == image.getPlanes()[1].getPixelStride());

        if (pixelStride == 2 && rowStride == width && uBuffer.get(0) == vBuffer.get(1)) {
            // maybe V an U planes overlap as per NV21, which means vBuffer[1] is alias of uBuffer[0]
            byte savePixel = vBuffer.get(1);
            vBuffer.put(1, (byte)0);
            if (uBuffer.get(0) == 0) {
                vBuffer.put(1, (byte)255);
                if (uBuffer.get(0) == 255) {
                    vBuffer.put(1, savePixel);
                    vBuffer.get(nv21, ySize, uvSize);

                    return nv21; // shortcut
                }
            }

            // unfortunately, the check failed. We must save U and V pixel by pixel
            vBuffer.put(1, savePixel);
        }

        // other optimizations could check if (pixelStride == 1) or (pixelStride == 2),
        // but performance gain would be less significant

        for (int row=0; row<height/2; row++) {
            for (int col=0; col<width/2; col++) {
                int vuPos = col*pixelStride + row*rowStride;
                nv21[pos++] = vBuffer.get(vuPos);
                nv21[pos++] = uBuffer.get(vuPos);
            }
        }

        return nv21;
    }
    public byte[] YUV_420_888toNV21_2(Image image) {

        int width = image.getWidth();
        int height = image.getHeight();
        int ySize = width*height;
        int uvSize = width*height/4;

        byte[] nv21 = new byte[ySize + uvSize*2];

        ByteBuffer yBuffer = image.getPlanes()[0].getBuffer(); // Y
        ByteBuffer uBuffer = image.getPlanes()[1].getBuffer(); // U
        ByteBuffer vBuffer = image.getPlanes()[2].getBuffer(); // V

        int rowStride = image.getPlanes()[0].getRowStride();
        assert(image.getPlanes()[0].getPixelStride() == 1);

        int pos = 0;

        if (rowStride == width) { // likely
            yBuffer.get(nv21, 0, ySize);
            pos += ySize;
        }
        else {
            for (; pos<ySize; pos+=width) {
                yBuffer.get(nv21, pos, width);
                yBuffer.position(yBuffer.position() + rowStride - width); // skip
            }
        }

        rowStride = image.getPlanes()[2].getRowStride();
        int pixelStride = image.getPlanes()[2].getPixelStride();

        assert(rowStride == image.getPlanes()[1].getRowStride());
        assert(pixelStride == image.getPlanes()[1].getPixelStride());

        if (pixelStride == 2 && rowStride == width && uBuffer.get(0) == vBuffer.get(1)) {
            // maybe V an U planes overlap as per NV21, which means vBuffer[1] is alias of uBuffer[0]
            byte savePixel = vBuffer.get(1);
            vBuffer.put(1, (byte)0);
            if (uBuffer.get(0) == 0) {
                vBuffer.put(1, (byte)255);
                if (uBuffer.get(0) == 255) {
                    vBuffer.put(1, savePixel);
                    vBuffer.get(nv21, ySize, uvSize);

                    return nv21; // shortcut
                }
            }

            // unfortunately, the check failed. We must save U and V pixel by pixel
            vBuffer.put(1, savePixel);
        }

        // other optimizations could check if (pixelStride == 1) or (pixelStride == 2),
        // but performance gain would be less significant

        for (int row=0; row<height/2; row++) {
            for (int col=0; col<width/2; col++) {
                nv21[pos++] = vBuffer.get(col + row*rowStride);
                nv21[pos++] = uBuffer.get(col + row*rowStride);
            }
        }

        return nv21;
    }

    public Double getRGBIntFromPlanes(Image image) {
        ArrayList<Double> list = new ArrayList<>();
        ArrayList<Double> listg = new ArrayList<>();
        ArrayList<Double> listb = new ArrayList<>();

        ByteBuffer buffer0 = image.getPlanes()[0].getBuffer();
        byte[] Y1 = new byte[buffer0.remaining()];
        buffer0.get(Y1);
        ByteBuffer buffer1 = image.getPlanes()[1].getBuffer();
        byte[] U1 = new byte[buffer1.remaining()];
        buffer1.get(U1);
        ByteBuffer buffer2 = image.getPlanes()[2].getBuffer();
        byte[] V1 = new byte[buffer2.remaining()];
        buffer2.get(V1);
        int Width = image.getWidth();
        int Heigh = image.getHeight();
        byte[] ImageRGB = new byte[image.getHeight()*image.getWidth()*4];
        int index = 0;
        for(int i = 0; i<Heigh-1; i++){
            for (int j = 0; j<Width; j++){
                int Y = Y1[i*Width+j]&0xFF;
                int U = U1[(i/2)*(Width/2)+j/2]&0xFF;
                int V = V1[(i/2)*(Width/2)+j/2]&0xFF;
                double R,G,B;
                R = Y + 1.402*(U-128);
                G = Y - 0.34414*(V-128) - 0.71414*(U-128);
                B = Y + 1.772*(V-128);
                if (R>255) {
                    R = 255;
                } else if (R<0) {
                    R = 0;
                }
                if (G>255) {
                    G = 255;
                } else if (G<0) {
                    G = 0;
                }
                if (B>255) {
                    B = 255;
                } else if (B<0) {
                    B = 0;
                }
                list.add(R);
                listg.add(G);
                listb.add(B);
            }
        }
        Double average = list.stream().mapToDouble(val -> val).average().orElse(0.0);
        Double averageg = listg.stream().mapToDouble(val -> val).average().orElse(0.0);
        Double averageb = listb.stream().mapToDouble(val -> val).average().orElse(0.0);
        Log.d("log:-", String.valueOf(average) + " " + String.valueOf(averageg) + " " + String.valueOf(averageb));
        return average;
    }
}
