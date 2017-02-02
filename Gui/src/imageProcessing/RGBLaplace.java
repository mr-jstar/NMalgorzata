package imageProcessing;

import java.awt.image.BufferedImage;

class RGBLaplace extends AbstractBufferedImageOp {

    /**
     * A convenience method for getting ARGB pixels from an image. This tries to
     * avoid the performance penalty of BufferedImage.getRGB unmanaging the
     * image.
     *
     * @param image a BufferedImage object
     * @param x the left edge of the pixel block
     * @param y the right edge of the pixel block
     * @param width the width of the pixel arry
     * @param height the height of the pixel arry
     * @param pixels the array to hold the returned pixels. May be null.
     * @return the pixels
     * @see #setRGB
     */
    public int[] getRGB(BufferedImage image, int x, int y, int width, int height, int[] pixels) {
        int type = image.getType();
        if (type == BufferedImage.TYPE_INT_ARGB || type == BufferedImage.TYPE_INT_RGB) {
            return (int[]) image.getRaster().getDataElements(x, y, width, height, pixels);
        }
        return image.getRGB(x, y, width, height, pixels, 0, width);
    }

    /**
     * A convenience method for setting ARGB pixels in an image. This tries to
     * avoid the performance penalty of BufferedImage.setRGB unmanaging the
     * image.
     *
     * @param image a BufferedImage object
     * @param x the left edge of the pixel block
     * @param y the right edge of the pixel block
     * @param width the width of the pixel arry
     * @param height the height of the pixel arry
     * @param pixels the array of pixels to set
     * @see #getRGB
     */
    public void setRGB(BufferedImage image, int x, int y, int width, int height, int[] pixels) {
        int type = image.getType();
        if (type == BufferedImage.TYPE_INT_ARGB || type == BufferedImage.TYPE_INT_RGB) {
            image.getRaster().setDataElements(x, y, width, height, pixels);
        } else {
            image.setRGB(x, y, width, height, pixels, 0, width);
        }
    }

    private void brightness(int[] row) {
        for (int i = 0; i < row.length; i++) {
            int rgb = row[i];
            int r = rgb >> 16 & 0xff;
            int g = rgb >> 8 & 0xff;
            int b = rgb & 0xff;
            row[i] = (r + g + b) / 3;
        }
    }

    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        int width = src.getWidth();
        int height = src.getHeight();

        if (dst == null) {
            dst = createCompatibleDestImage(src, null);
        }

        int[] row1 = null;
        int[] row2 = null;
        int[] row3 = null;
        int[] pixels = new int[width];
        row1 = getRGB(src, 0, 0, width, 1, row1);
        row2 = getRGB(src, 0, 0, width, 1, row2);
        brightness(row1);
        brightness(row2);
        for (int y = 0; y < height; y++) {
            if (y < height - 1) {
                row3 = getRGB(src, 0, y + 1, width, 1, row3);
                brightness(row3);
            }
            pixels[0] = pixels[width - 1] = 0xff000000;
            for (int x = 1; x < width - 1; x++) {
                int l1 = row2[x - 1];
                int l2 = row1[x];
                int l3 = row3[x];
                int l4 = row2[x + 1];

                int l = row2[x];
                int max = Math.max(Math.max(l1, l2), Math.max(l3, l4));
                int min = Math.min(Math.min(l1, l2), Math.min(l3, l4));

                int gradient = (int) (0.5f * Math.max((max - l), (l - min)));

                int r = ((row1[x - 1] + row1[x] + row1[x + 1]
                        + row2[x - 1] - (8 * row2[x]) + row2[x + 1]
                        + row3[x - 1] + row3[x] + row3[x + 1]) > 0)
                                ? gradient : (128 + gradient);
                pixels[x] = r;
            }
            setRGB(dst, 0, y, width, 1, pixels);
            int[] t = row1;
            row1 = row2;
            row2 = row3;
            row3 = t;
        }

        row1 = getRGB(dst, 0, 0, width, 1, row1);
        row2 = getRGB(dst, 0, 0, width, 1, row2);
        for (int y = 0; y < height; y++) {
            if (y < height - 1) {
                row3 = getRGB(dst, 0, y + 1, width, 1, row3);
            }
            pixels[0] = pixels[width - 1] = 0xff000000;
            for (int x = 1; x < width - 1; x++) {
                int r = row2[x];
                r = (((r <= 128)
                        && ((row1[x - 1] > 128)
                        || (row1[x] > 128)
                        || (row1[x + 1] > 128)
                        || (row2[x - 1] > 128)
                        || (row2[x + 1] > 128)
                        || (row3[x - 1] > 128)
                        || (row3[x] > 128)
                        || (row3[x + 1] > 128)))
                                ? ((r >= 128) ? (r - 128) : r) : 0);

                pixels[x] = 0xff000000 | (r << 16) | (r << 8) | r;
            }
            setRGB(dst, 0, y, width, 1, pixels);
            int[] t = row1;
            row1 = row2;
            row2 = row3;
            row3 = t;
        }

        return dst;
    }

    @Override
    public String toString() {
        return "RGBLaplace";
    }
}
