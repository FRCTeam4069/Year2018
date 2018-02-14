package frc.team4069.robot.vision;

import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.vision.VisionThread;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

// The static class that contains the arm camera vision thread and the data created by it
public class ArmCameraData {

    // The dimensions of the image captured by the camera
    public static final int imageWidth = 320;
    public static final int imageHeight = 240;

    // The X and Y positions of the center of the power cube found on screen
    public static int powerCubeXPos;
    public static int powerCubeYPos;

    // Output stream to smart dashboard
    private static CvSource outputStream;

    // Set up the vision threads and set them to run as the camera captures frames
    public static void configureVision() {

        // Create the arm camera
        CameraServer server = CameraServer.getInstance();
        UsbCamera armCamera = server.startAutomaticCapture(1);
        armCamera.setResolution(imageWidth, imageHeight);

        // Initialize the arm camera thread and update the position of the power cube every frame
        VisionThread armCameraVisionThread = new VisionThread(armCamera,
                new ArmCameraPipeline(),
                pipeline -> {
                    // If an image is in the field
                    if (!pipeline.findContoursOutput().isEmpty()) {
                        // Get the maximum extents of the contours
                        Rect contourBounds = Imgproc
                                .boundingRect(pipeline.findContoursOutput().get(0));
                        // Set the public values accordingly
                        powerCubeXPos = contourBounds.x + (contourBounds.width / 2);
                        powerCubeYPos = contourBounds.x + (contourBounds.height / 2);
                    }
                }
        );

        // Run the threads
        armCameraVisionThread.start();

        // Set up the output stream to the smart dashboard
        outputStream = CameraServer.getInstance()
                .putVideo("ArmCamera", imageWidth, imageHeight);
    }
}
