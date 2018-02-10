package frc.team4069.robot.vision;

import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.vision.VisionThread;
import frc.team4069.robot.vision.TapeTrackingPipeline.Line;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

// The static class that contains the vision threads and the data updated by them
public class VisionData {

    // The dimensions of the image captured by both cameras
    public static final int imageWidth = 320;
    public static final int imageHeight = 240;

    // The number of lines that will be found and averaged
    private static final int numLines = 4;

    // The X and Y positions of the center of the power cube found on screen
    public static int powerCubeXPos;
    public static int powerCubeYPos;
    // The average X position of the lines found in the captured image
    public static double averageLineX;
    // Output stream to smart dashboard
    private static CvSource outputStream;

    // Set up the vision threads and set them to run as the camera captures frames
    public static void configureVision() {

        // Create the first and second cameras, which are located on the lifting arm and at the
        // front of the robot, respectively
        CameraServer server = CameraServer.getInstance();
        UsbCamera frontCamera = server.startAutomaticCapture(0);
        frontCamera.setResolution(imageWidth, imageHeight);
//        UsbCamera armCamera = server.startAutomaticCapture(1);
//        armCamera.setResolution(imageWidth, imageHeight);

        // Initialize the front camera thread, updating the average position of the lines each frame
        VisionThread frontCameraVisionThread = new VisionThread(frontCamera,
                new TapeTrackingPipeline(),
                pipeline -> {
                    // Get the output from the pipeline
                    ArrayList<Line> lines = pipeline.filterLinesOutput();
                    System.out.println(lines.size());
                    // If there are at least four lines
                    if (lines.size() >= numLines) {
                        System.out.println("numLines is over 4");
                        // Display the blurred image on the smart dashboard
                        outputStream.putFrame(pipeline.blurOutput());
                        // Sort the lines
                        Collections.sort(lines, new LineComparator());
                        // Get the first four elements of the list
                        List<Line> firstLines = lines.subList(0, numLines);
                        // Get the average X position of the lines
                        double lineXAccumulator = 0;
                        for (int i = 0; i < numLines; i++) {
                            Line line = firstLines.get(i);
                            lineXAccumulator += (line.x1 + line.x2);
                        }
                        averageLineX = lineXAccumulator / (numLines * 2);
                    }
                }
        );

//        // Initialize the arm camera thread and update the position of the power cube every frame
//        VisionThread armCameraVisionThread = new VisionThread(armCamera,
//                new ArmCameraPipeline(),
//                pipeline -> {
//                    // If an image is in the field
//                    if (!pipeline.findContoursOutput().isEmpty()) {
//                        // Get the maximum extents of the contours
//                        Rect contourBounds = Imgproc
//                                .boundingRect(pipeline.findContoursOutput().get(0));
//                        // Set the public values accordingly
//                        powerCubeXPos = contourBounds.x + (contourBounds.width / 2);
//                        powerCubeYPos = contourBounds.x + (contourBounds.height / 2);
//                    }
//                }
//        );

        // Run the threads
        frontCameraVisionThread.start();
//        armCameraVisionThread.start();

        // Set up the output stream to the smart dashboard
        outputStream = CameraServer.getInstance()
                .putVideo("ProcessorOutput", 640, 480);
    }

    // Comparator that allows comparing the lengths of Line objects
    private static class LineComparator implements Comparator<Line> {

        @Override
        public int compare(Line line0, Line line1) {
            return lineLength(line0).compareTo(lineLength(line1));
        }

        private Double lineLength(Line line) {
            return Math.sqrt(Math.pow(line.x2 - line.x1, 2) + Math.pow(line.y2 - line.y1, 2));
        }
    }
}
