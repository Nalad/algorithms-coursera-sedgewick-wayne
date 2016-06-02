import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver{
    private Picture picture;
    private int[] edgeTo;
    private double[] distTo;
    private double[] energy;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new java.lang.NullPointerException();
        }

        this.picture = new Picture(picture);
    }
    
    // current picture
    public Picture picture() {
        return new Picture(picture);
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if ((x < 0) || (x > (width() - 1)) || (y < 0) || (y > (height() - 1))) {
            throw new java.lang.IndexOutOfBoundsException();
        }

        if ((x == 0) || (x == width() - 1) || (y == 0) || (y == height() - 1)) {
            return 1000.0;
        }

        return Math.sqrt(gradientX(x, y) + gradientY(x, y));
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        Picture transposedImage = transposeImage(picture);

        SeamCarver sc = new SeamCarver(transposedImage);
        int[] transposedHorizontalSeam = sc.findVerticalSeam();

        return transposedHorizontalSeam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        edgeTo = new int[height() * width()];
        distTo = new double[height() * width()];
        energy = new double[height() * width()];

        for (int i = 0; i < edgeTo.length; i++) {
            if (i < width()) {
                distTo[i] = 0;
            } else {
                distTo[i] = Double.POSITIVE_INFINITY;
            }

            energy[i] = energy(positionInRow(i), positionInCol(i));
        }

        for (int y = 0; y < height() - 1; y++) {
            for (int x = 0; x < width(); x++) {
                if (x != 0) {
                    relax(position(x, y), position(x - 1, y + 1));
                }

                relax(position(x, y), position(x, y + 1));

                if (x != (width() - 1)) {
                    relax(position(x, y), position(x + 1, y + 1));
                }
            }
        }

        int smallest = 0;
        for (int i = 1; i < width(); i++) {
            if (distTo[position(i, height() - 1)] < distTo[position(smallest, height() - 1)]) {
                smallest = i;
            }
        }

        int[] seam = new int[height()];
        for (int i = seam.length - 1, next = smallest + i * width(); i >= 0; i--) {
            seam[i] = positionInRow(next);
            next = edgeTo[next];
        }

        return seam;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) {
            throw new java.lang.NullPointerException();
        }

        if ((seam.length != picture.width()) || (picture.height() <= 1)) {
            throw new java.lang.IllegalArgumentException();
        }

        checkSeam(seam, false);

        Picture transposedImage = transposeImage(picture);
        
        SeamCarver sc = new SeamCarver(transposedImage);
        sc.removeVerticalSeam(seam);

        picture = transposeImage(sc.picture());
        distTo = null;
        edgeTo = null;
        energy = null;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) {
            throw new java.lang.NullPointerException();
        }

        if ((seam.length != picture.height()) || (picture.width() <= 1)) {
            throw new java.lang.IllegalArgumentException();
        }

        checkSeam(seam, true);

        Picture withoutSeam = new Picture(width() - 1, height());
        for (int y = 0; y < height(); y++) {
            for (int x = 0, xFromImage = 0; x < width() - 1; x++, xFromImage++) {
                if (x == seam[y]) {
                    xFromImage++;
                }
                withoutSeam.set(x, y, picture.get(xFromImage, y));
            }
        }
        
        picture = withoutSeam;
        distTo = null;
        edgeTo = null;
        energy = null;
    }

    private void checkSeam(int[] seam, boolean isVertical) {
        for (int i = 1; i < seam.length; i++) {
            if (Math.abs(seam[i - 1] - seam[i]) > 1) {
                throw new java.lang.IllegalArgumentException();
            }
        }

        for (int i = 0; i < seam.length; i++) {
            if (isVertical && (seam[i] < 0 || seam[i] > (width() - 1))) {
                throw new java.lang.IllegalArgumentException();
            } else if (!isVertical && (seam[i] < 0 || seam[i] > (height() - 1))) {
                throw new java.lang.IllegalArgumentException();
            }
        }
    }

    private double gradientX(int x, int y) {
        Color pixelLeft = picture.get(x - 1, y);
        Color pixelRight = picture.get(x + 1, y);

        return Math.pow((pixelRight.getRed() - pixelLeft.getRed()), 2)
                + Math.pow(pixelRight.getGreen() - pixelLeft.getGreen(), 2)
                + Math.pow(pixelRight.getBlue() - pixelLeft.getBlue(), 2);
    }

    private double gradientY(int x, int y) {
        Color pixelUp = picture.get(x, y - 1);
        Color pixelDown = picture.get(x, y + 1);

        return Math.pow((pixelDown.getRed() - pixelUp.getRed()), 2)
                + Math.pow(pixelDown.getGreen() - pixelUp.getGreen(), 2)
                + Math.pow(pixelDown.getBlue() - pixelUp.getBlue(), 2);
    }

    private void relax(int v, int w) {
        if (distTo[w] > distTo[v] + energy[w]) {
            distTo[w] = distTo[v] + energy[w];
            edgeTo[w] = v;
        }
    }

    private int position(int x, int y) {
        return x + y * width();
    }

    private int positionInRow(int v) {
        return v - (v / width()) * width();
    }

    private int positionInCol(int v) {
        return v / width();
    }

    private Picture transposeImage(Picture toTranspose) {
        Picture transposedImage = new Picture(toTranspose.height(), toTranspose.width());
        for (int x = 0; x < toTranspose.width(); x++) {
            for (int y = 0; y < toTranspose.height(); y++) {
                transposedImage.set(y, x, toTranspose.get(x, y));
            }
        }

        return transposedImage;
    }
}
