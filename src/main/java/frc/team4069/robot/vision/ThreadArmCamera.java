package frc.team4069.robot.vision;

import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

public class ThreadArmCamera implements Runnable {

    // Dimensions of the image
    public final int width = 320;
    public final int height = 240;
    // The X and Y positions of the center of the power cube found on screen
    public int powerCubeXPos;
    public int powerCubeYPos;
    private ArmCameraPipeline pipeline;
    private VideoCapture vcap;

    // Set up the vision threads and set them to run as the camera captures frames
    public ThreadArmCamera() {
        pipeline = new ArmCameraPipeline();
        vcap = new VideoCapture();
        vcap.open(1);
        vcap.set(Videoio.CAP_PROP_FRAME_WIDTH, width);
        vcap.set(Videoio.CAP_PROP_FRAME_HEIGHT, height);

    }

    // Grab frames and set the global position of the power cube
    public void run() {
        while (true) {
            Mat frame = new Mat();
            vcap.read(frame);
            // If an image is in the field
            if (!pipeline.findContoursOutput().isEmpty()) {
                // Get the maximum extents of the contours
                Rect contourBounds = Imgproc
                        .boundingRect(pipeline.filterContoursOutput().get(0));
                // Set the public values accordingly
                powerCubeXPos = contourBounds.x + (contourBounds.width / 2);
                powerCubeYPos = contourBounds.x + (contourBounds.height / 2);
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

