package net.github.nikistadnik.springRaspberryJavaServer.smartDevices.cameras;


import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.javacpp.BytePointer;

public class FrameToBytes {
    private static final OpenCVFrameConverter.ToMat converterToMat = new OpenCVFrameConverter.ToMat();

    public static byte[] frameToJpegBytes(Frame frame) {
        Mat mat = converterToMat.convert(frame);
        if (mat == null) return null;

        BytePointer bp = new BytePointer();
        boolean ok = opencv_imgcodecs.imencode(".jpg", mat, bp);
        if (!ok) return null;

        byte[] imageBytes = new byte[(int) bp.limit()];
        bp.get(imageBytes);
        bp.deallocate();  // free native memory
        return imageBytes;
    }
}

/*
public class FrameToBytes {

    private static final OpenCVFrameConverter.ToMat converterToMat = new OpenCVFrameConverter.ToMat();

    public static byte[] frameToJpegBytes(Frame frame) {
        Mat mat = converterToMat.convert(frame);
        if (mat == null || mat.empty()) return null;

        // Convert to 3-channel BGR if necessary
        Mat bgr = new Mat();
        int channels = mat.channels();
        if (channels == 1) {
            opencv_imgproc.cvtColor(mat, bgr, opencv_imgproc.COLOR_GRAY2BGR);
        } else if (channels == 3) {
            mat.copyTo(bgr);
        } else if (channels == 4) {
            opencv_imgproc.cvtColor(mat, bgr, opencv_imgproc.COLOR_RGBA2BGR);
        } else {
            return null; // unsupported format
        }

        BytePointer buf = new BytePointer();
        boolean ok = opencv_imgcodecs.imencode(".jpg", bgr, buf);
        if (!ok || buf == null) {
            if (buf != null) buf.deallocate();
            return null;
        }

        byte[] bytes = new byte[(int) buf.limit()];
        buf.get(bytes);
        buf.deallocate();
        return bytes;
    }
}

 */
