package com.davidcryer.camerafilters.framework.opencv;

import android.graphics.Bitmap;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class ImageManipulator {
    private ColorEffect colorProcessing;
    private ImageEffect imageProcessing;
    private Mat intermediate;
    private Mat dest;

    static {
        System.loadLibrary("MyOpenCVLib");
    }

    public ImageManipulator(final int height, final int width) {
        intermediate = new Mat(height, width, CvType.CV_8SC4);
        dest = new Mat(height, width, CvType.CV_8SC4);
        colorProcessing = ColorEffect.RGBA;
        imageProcessing = ImageEffect.NONE;
    }

    public void colorProcessing(final ColorEffect colorProcessing) {
        this.colorProcessing = colorProcessing;
    }

    public void imageProcessing(final ImageEffect imageProcessing) {
        this.imageProcessing = imageProcessing;
    }

    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        return process(inputFrame);
    }

    private Mat process(final CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        processColors(inputFrame);
        processImage();
        return dest;
    }

    private void processColors(final CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        switch (colorProcessing) {
            default:
            case RGBA: {
                dest = inputFrame.rgba();
            } break;
            case BRGA: {
                Imgproc.cvtColor(inputFrame.rgba(), dest, Imgproc.COLOR_RGBA2BGRA, 4);
            } break;
            case GREY: {
                Imgproc.cvtColor(inputFrame.gray(), dest, Imgproc.COLOR_GRAY2RGBA, 4);//TODO what is the last variable?
            } break;
            case CANNY: {
                Imgproc.Canny(inputFrame.gray(), intermediate, 80, 75);//TODO what are these integers?
                Imgproc.cvtColor(intermediate, dest, Imgproc.COLOR_GRAY2RGBA, 4);//TODO what is the last variable?
            } break;
        }
    }

    private void processImage() {
        switch (imageProcessing) {
            case ZOOM: {
                final int r = dest.rows();
                final int c = dest.cols();
                final Mat zoomCorner = dest.submat(0, r, 0, c);
                final Mat zoomWindow = dest.submat(2 * r / 5, 3 * r / 5, 2 * c / 5, 3 * c / 5);
                final Mat zoomWindowClone = zoomWindow.clone();
                Imgproc.resize(zoomWindowClone, zoomCorner, zoomCorner.size());
                zoomCorner.release();
                zoomWindow.release();
                zoomWindowClone.release();
            } break;
            case PIXELISE: {
                Imgproc.resize(dest, intermediate, new Size(), 0.1, 0.1, Imgproc.INTER_NEAREST);
                Imgproc.resize(intermediate, dest, dest.size(), 0., 0., Imgproc.INTER_NEAREST);
            } break;
            case POSTERISE: {
                Imgproc.Canny(dest, intermediate, 80, 90);
                dest.setTo(new Scalar(0, 0, 0, 255), intermediate);
                Core.convertScaleAbs(dest, intermediate, 1. / 16, 0);
                Core.convertScaleAbs(intermediate, dest, 16, 0);
            } break;
        }
    }

    public void release() {
        intermediate.release();
        dest.release();
    }

    public Bitmap process(final byte[] photo, final int height, final int width) {
        final PhotoFrame frame = new PhotoFrame(photo, height, width);
        final Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(process(frame), bitmap);
        frame.release();
        return bitmap;
    }

    private static class PhotoFrame implements CameraBridgeViewBase.CvCameraViewFrame {
        private final int height;
        private final int width;
        final Mat rgba = new Mat();
        final Mat targRgba;
        final Mat targGray;

        private PhotoFrame(final byte[] bytes, final int height, final int width) {
            this.height = height;
            this.width = width;
            final Mat p = new Mat(height, width, CvType.CV_8UC1);
            p.put(0, 0, bytes);
            targRgba = Imgcodecs.imdecode(p, Imgcodecs.IMREAD_COLOR);
            targGray = Imgcodecs.imdecode(p, Imgcodecs.IMREAD_GRAYSCALE);
            p.release();
        }

        @Override
        public Mat rgba() {
            Imgproc.cvtColor(targRgba, rgba, Imgproc.COLOR_BGR2RGBA, 4);
            return rgba;
        }

        @Override
        public Mat gray() {
            return targGray.submat(0, height, 0, width);
        }

        private void release() {
            rgba.release();
            targRgba.release();
            targGray.release();
        }
    }
}
