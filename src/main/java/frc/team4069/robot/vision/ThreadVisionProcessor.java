package frc.team4069.robot.vision;

import edu.wpi.cscore.CvSource;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Preferences;
import frc.team4069.robot.Robot;
import frc.team4069.robot.util.LowPassFilter;
import frc.team4069.robot.vision.ThreadLIDAR.LidarSpot;
import java.util.ArrayList;
import java.util.Iterator;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt4;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class ThreadVisionProcessor implements Runnable {

    public static final Scalar RED = new Scalar(0, 0, 255);
    public static final Scalar BLUE = new Scalar(255, 0, 0);
    public static final Scalar GREEN = new Scalar(0, 255, 0);
    public static final Scalar ORANGE = new Scalar(0, 128, 255);
    public static final Scalar YELLOW = new Scalar(0, 255, 255);
    public static final Scalar PINK = new Scalar(255, 0, 255);
    public static final Scalar WHITE = new Scalar(255, 255, 255);
    // OpenCV constants
    public static final int CV_CAP_PROP_BRIGHTNESS = 10;
    public static final int CV_CAP_PROP_CONTRAST = 11;
    public static final int CV_CAP_PROP_EXPOSURE_ABSOLUTE = 39;
    public static final int CV_CAP_PROP_FRAME_WIDTH = 3;
    public static final int CV_CAP_PROP_FRAME_HEIGHT = 4;
    public double lastHeadingTargetSeen = 0.0;

    public double lastYCenter = 0.0;
    public double lastXCenter = 0.0;
    public double lastMapped = 0.0;

    public double arrowangle = 0.0;
    public int arrowdistance = 0;
    public ColourRegions cregions = new ColourRegions(); // really only 1 of these it does most everything
    Preferences prefs = Preferences.getInstance();
    private boolean mShowContours = true;
    private boolean mExitThread = false;
    private boolean mProcessFrames = true;
    private ThreadVideoCapture vcap_thread_instance;
    private Thread vcap_thread_handle;
    private CvSource outputStream; // where to send images for display on driver station's smart dashboard
    private Robot mRobot;

    private LowPassFilter xLowPass;
    private LowPassFilter yLowPass;
	
	// If enabled, normalizes the brightness of the image around an average brightness value
	private boolean valueScaling = false;
	private int baseValue = 100;

    public ThreadVisionProcessor(ThreadVideoCapture vidcapinstance, Thread vcap_handle,
            Robot irobot) {
        vcap_thread_instance = vidcapinstance; // to access getframe
        vcap_thread_handle = vcap_handle; // for thread control
        mRobot = irobot;
    }

    public void run() {
        Mat img = new Mat();
        Mat lastvalidimg = null;

        outputStream = CameraServer.getInstance().putVideo("ProcessorOutput", 640,
                480); // ProcessorOutput is name smart dashboard on driverstation will use to display this image

        xLowPass = new LowPassFilter(50);
        yLowPass = new LowPassFilter(50);

        // Adding regions increases processing time.
        // Range is from minR,minG,minB to maxR,maxG,maxB
//        cregions.addRange(21, 178, 81, 133, 255, 255); // RGB(21,178,81) to RGB(133,255,255)
//        cregions.addRange(22, 230, 101, 32, 255, 204); // 28, 236, 194, 32, 252, 204);
//        cregions.addRange(22, 239, 240, 46, 255, 255);

        // North bay competition
        //cregions.addRange(0x00, 0x58, 0x2D, 0x25, 0x66, 0x3D);

        // Other competition
        //cregions.addRange(0x07, 0x77, 0x38, 0x30, 0x94, 0x51);

        // Lower gym
        cregions.addRange(0x00, 0x90, 0x65, 0x24, 0xff, 85);

        while ((true) && (mExitThread == false)) {
            if (mProcessFrames) {
                img = vcap_thread_instance
                        .GetFrame(); // pull frame from ThreadVideoCapture.java safely
				
				if(valueScaling){
					Mat imgCopy = new Mat();
					Imgproc.cvtColor(img, imgCopy, Imgproc.COLOR_RGB2HSV);
					double averageValue = 0;
					for(int x = 0; x < img.cols(); x++){
						for(int y = 0; y < img.rows(); y++){
							averageValue += imgCopy.get(x, y)[2];
						}
					}
					averageValue /= img.cols() * img.rows();
					double averageDifference = averageValue - baseValue;
					for(int x = 0; x < img.cols(); x++){
						for(int y = 0; y < img.rows(); y++){
							double[] pixel = imgCopy.get(x, y);
							double modifiedValue = pixel[2] + averageDifference;
							if(modifiedValue > 255){
								modifiedValue = 255;
							}
							if(modifiedValue < 0){
								modifiedValue = 0;
							}
							imgCopy.put(x, y, pixel[0], pixel[1], modifiedValue);
						}
					}
					Imgproc.cvtColor(imgCopy, img, Imgproc.COLOR_HSV2RGB);
				}

                if (img != null) {
                    cregions.CalcAll(img); // look for stuff, calc things
                    cregions.DrawAll(img); // draw stuff based on the calc things
                    lastvalidimg = img; // save to send out incase we get a null from capturethread, stops pink empty frames showing up

                } // if img!=null
                if (lastvalidimg != null) {
                    outputStream.putFrame(lastvalidimg);
                }
            } // if processframes true
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } // while true
    } // run

    private double Lerp(double a1, double a2, double b1, double b2, double num) {
        return ((num - a1) / (a2 - a1)) * (b2 - b1) - b2;
    }

    // ----------------------------------------------------------------

    private void DrawLIDAR(int xp, int yp, double scale, Mat original) {
        LidarSpot ls = null;
        for (int i = 0; i < mRobot.threadLIDARInstance.history.length; i++) {
            ls = mRobot.threadLIDARInstance.getHistoryPoint(); // Pull 1st point
            LidarSpot ls2 = mRobot.threadLIDARInstance
                    .getHistoryPoint(); // Pull 2nd point (lidar rotates so this point should connect to point above)

            double rad = ls.az * (Math.PI / 180); // convert ugly angles to normal radians
            double rad2 = ls2.az * (Math.PI / 180);

            Vector2 vec = new Vector2(-Math.cos(rad), Math.sin(rad)); // and get some nice vectors
            Vector2 vec2 = new Vector2(-Math.cos(rad2), Math.sin(rad2));

            vec.scale(ls.dist
                    / 6); // scale down, lidar can do 40 meters! 4000cm, thats BIG scale to screen sizeish
            vec2.scale(ls2.dist / 6); // NOTE: May need to change /4 to like /8 in big rooms

            Point dpt = new Point(xp + vec.x, yp + vec.y); // Line needs Points so make points
            Point dpt2 = new Point(xp + vec2.x, yp + vec2.y + 1);

            if ((ls.az > 270 - 5) && (ls.az < 270 + 5)) // If near camera's vision, draw in green
            {
                Imgproc.line(original, dpt, dpt2, GREEN, 1);
            } else {
                Imgproc.line(original, dpt, dpt2, RED, 1); // if 'off' cameras site, draw in red
            }
        }
    }// DrawLIDAR

    // Simple draw arrow routine, given point for tail, a direction, a size (scale) and a Mat to draw on.
    //
    private void DrawArrow(int xp, int yp, double dir, double scale, Mat original) {
        double rad = dir * (Math.PI / 180);
        double lrad = rad + 3.14159 / 5; // left arrow head
        double rrad = rad - 3.14159 / 5; // right arrow head point

        Vector2 degvec = new Vector2(Math.cos(rad), Math.sin(rad)); // get vector pointing right
        Vector2 lradv = new Vector2(Math.cos(lrad), Math.sin(lrad));
        Vector2 rradv = new Vector2(Math.cos(rrad), Math.sin(rrad));

        lradv.scale(scale / 4); // scale up head left bit (1/4th vector length)
        rradv.scale(scale / 4); // scale up head right bit same size
        degvec.scale(scale); // main vector body is passed in size

        Vector2 vtip = new Vector2(xp, yp); // first we are the base point of arrow...
        vtip.add(degvec); // NOW we are the tip

        Vector2 lbit = vtip.clone(); // clone tip point and...
        lbit.sub(lradv); // offset to the left head point

        Vector2 rbit = vtip.clone(); // clone the tip point and...
        rbit.sub(rradv); // offset to the right head point

        Point start = new Point(xp, yp); // Now we draw, but draw needs Point's so get base of arrow
        Point tip = new Point(vtip.x, vtip.y); // Tip of arrow
        Point lefthead = new Point(lbit.x, lbit.y); // left head bit
        Point righthead = new Point(rbit.x, rbit.y); // right head bit

        // double csc = scale + scale * 0.15;
        // Imgproc.circle(original, start, (int) csc, BLUE);

        Imgproc.line(original, start, tip, YELLOW, 2); // draw main arrow body
        Imgproc.line(original, tip, lefthead, YELLOW, 2); // draw left bit
        Imgproc.line(original, tip, righthead, YELLOW, 2); // draw right bit 2 pixels wide

    } // DrawArrow

    // Given a center x,y a scale and a image Mat to draw on, pulls history points from the lidar thread and
    // draws them on the Mat
    // NOTE This needs to be fixed, it draws artifacts where the lidarthread enters points and gethistorypoint pulls them
    // Not dangerous, but ugly artifacts

    /**
     * ColurRange : simply stores a min RGB and maxRGB value
     *
     * @author EA
     */
    private class ColourRange {

        public Scalar cmin;
        public Scalar cmax;

        public ColourRange(int minr, int ming, int minb, int maxr, int maxg, int maxb) {
            cmin = new Scalar(minb, ming, minr);
            cmax = new Scalar(maxb, maxg, maxr);
        }
    }// class ColourRange

    /**
     * Really theres only 1 of these, there COULD be more...its the main workhorse of the visionprocessor Its CalcAll and DrawAll methods find targets, draw them and send the image out
     *
     * @author user
     */
    public class ColourRegions {

        public ArrayList<ColourRange> mRange = new ArrayList<ColourRange>();
        public ArrayList<Mat> mThresholds = new ArrayList<Mat>();
        public ArrayList<MatOfPoint> mContours = new ArrayList<MatOfPoint>();
        public MatOfPoint mLargestContour = null;
        public double mLargestContourArea = 0.0;
        public double mXCenter = 0.0;
        public double mYCenter = 0.0;
        public double mXGreenLine = 160.0;
        public double mYGreenLine = 120.0;
        public int mTargetVisible = 0;
        double mMinAreaAllowed = 49.0;
        private MatOfInt4 mHierarchy = new MatOfInt4();

        ColourRegions() {
        }

        /**
         * Add min/max colour range for finding target. This function can be called multiple times to add more ranges, though it costs a bit in time for each.
         */
        public void addRange(int minr, int ming, int minb, int maxr, int maxg, int maxb) {
            mRange.add(new ColourRange(minr, ming, minb, maxr, maxg,
                    maxb)); // Store range to threashold
            mThresholds
                    .add(new Mat()); // each range needs a threshold Mat to store into...one for one here.
        }

        public void setMinAreaAllowed(double ma) {
            mMinAreaAllowed = ma;
        }

        // Returns number of contours
        public int CalcAll(Mat img) {
            mContours.clear();
            for (int i = 0; i < mRange.size(); i++) {
                ColourRange wrange = mRange.get(i);
                Mat wthreshold = mThresholds.get(i);
                Core.inRange(img, wrange.cmin, wrange.cmax, wthreshold);
                Imgproc.blur(wthreshold, wthreshold, new Size(3, 3));
                ArrayList<MatOfPoint> tmpcontours = new ArrayList<MatOfPoint>();
                Imgproc.findContours(wthreshold, tmpcontours, mHierarchy, Imgproc.RETR_EXTERNAL,
                        Imgproc.CHAIN_APPROX_SIMPLE);
                mContours.addAll(tmpcontours); // keep all contours from all colour ranges
            }

            // now clear out any contours smaller than minAreaAllowed 6x6=36 etc 9x9=81
            // Not sure this is a good idea...
            mXCenter = 0.0;
            mYCenter = 0.0;
            mLargestContourArea = 0.0;
            mLargestContour = null;
            int numremoved = 0; // count number removed...
            Iterator<MatOfPoint> it = mContours.iterator();
            while (it.hasNext()) {
                MatOfPoint mop = (MatOfPoint) it.next();
                double area = Imgproc.contourArea(mop);
                if (area < mMinAreaAllowed) {
                    it.remove();
                    numremoved++;
                } else {
                    // Area larger than min, calc center points, keep largeest area found
                    mXCenter += mop.get(0, 0)[0]
                            + mop.size().width / 2; // don't add to xcenter for removed contours
                    mYCenter += mop.get(0, 0)[0] + mop.size().height / 2;
                    if (area > mLargestContourArea) {
                        mLargestContourArea = area; // if this is the largest found so far, remember its size
                        mLargestContour = mop; // and remember it.
                    } // if
                } // else
            } // for mop

            int numContours = mContours.size(); // might have gotten rid of all!

            if (numContours == 0) {
                if (numremoved > 0) {
                    // System.out.println("CHANGE??? REMOVED ALL CONTOURS! Maybe lower min Currently:"+mMinAreaAllowed); //warn us, we may want to change min area
                }
                return 0;
            }

            mXCenter /= numContours; // average center
            mYCenter /= numContours;
            return numContours;
        }// CalcAll

        /**
         * DrawAll mContours is populated, now eliminate ones below area threshold, and draw outline of all contours which have an area greater than 50% of the largest contour Calculate a lowpass filter x coordinate
         */

        public void DrawAll(Mat original) {
            double xAverage = 0.0;
            double yAverage = 0.0;
            int avgCtr = 0;
            RotatedRect[] minRect = new RotatedRect[mContours.size()];

            // Go through all contours and throw out any smaller than 50% of largest contour,
            // calc center point of all contours whic survive 50% test
            avgCtr = 0;
            xAverage = 0.0;
            yAverage = 0.0;
            for (int i = 0; i < mContours.size(); i++) {
                MatOfPoint2f mop2f = new MatOfPoint2f(mContours.get(i).toArray());
                minRect[i] = Imgproc.minAreaRect(mop2f);
                double curarea = Imgproc.contourArea(mContours.get(i));

                double contourWeight = curarea
                        / mLargestContourArea; // 0-1.0 for weight of this contour mLargest comes from CalcAll

                if ((mShowContours) && (contourWeight
                        > .5)) // Contour must be bigger than 50% of our largest contour to be kept
                {
                    Point[] rect_points = new Point[4];
                    minRect[i].points(rect_points);

                    for (int j = 0; j < 4; j++) // Draw contour in blue, sum x,y points
                    {
                        Imgproc.line(original, rect_points[j], rect_points[(j + 1) % 4], BLUE, 3);
                        xAverage += rect_points[j].x;
                        yAverage += rect_points[j].y;
                        avgCtr++;
                    }
                } // if visualize

                // Rect box = minRect[i].boundingRect();
                // Point center = new Point(box.x + box.width / 2, box.y + box.height / 2);
                // Point center2 = new Point(3 + (box.x + box.width / 2), 3 + (box.y + box.height / 2));
                // Imgproc.line(original, center, center2, YELLOW, 3);
            } // for i <contours.size

            // If contours survived culling in calc...

            if ((mContours.size() > 0) && (avgCtr > 0)) // avgCtr must be > 1 or div by zero error
            {
                mTargetVisible = 1;
                xAverage /= avgCtr; // get center of all contours
                yAverage /= avgCtr;

                double nx = xLowPass
                        .calculate(xAverage); // low pass filter them, prevents 'jumpy' crosshairs
                double ny = yLowPass.calculate(yAverage);

                Point avgpt = new Point(nx, 0); // crosshairs span screen
                Point avgpt2 = new Point(nx, 230); // yAverage+2); //give some size

                Point ypt = new Point(0, ny);
                Point ypt1 = new Point(320, ny);

                mXGreenLine = nx; // Store lowpass values for other threads to 'see' (note unsafe updates here might want to lock if x,y seem wrong a lot)
                mYGreenLine = ny;

                Point center = new Point(nx, ny);
                Imgproc.line(original, avgpt, avgpt2, GREEN, 1);
                Imgproc.line(original, ypt, ypt1, GREEN, 1);

                // Now read last heading from gyro thread
                lastHeadingTargetSeen = mRobot.threadGyroInstance.lastHeading;
            } else // no target/contours meet criteria, draw some history to maybe help player
            {
                mTargetVisible = 0;
                Point avgpt = new Point(mXGreenLine, 0); // use last seen location
                Point avgpt2 = new Point(mXGreenLine, 230); //
                Imgproc.line(original, avgpt, avgpt2, RED, 1); // draw in read if not recent
                Point ypt = new Point(0, mYGreenLine);
                Point ypt1 = new Point(320, mYGreenLine);
                Imgproc.line(original, ypt, ypt1, RED, 1);
            }

            Point centerbtm = new Point(0, 20);
            Point rht = new Point(0, 200);
            double head = mRobot.threadGyroInstance.lastHeading;
            Imgproc.putText(original, "HEADING:" + head, centerbtm, 0, 0.5, GREEN);

            Point lastsn = new Point(50, 230);
            Imgproc.putText(original, "Target Last Heading:" + lastHeadingTargetSeen, lastsn, 0,
                    0.5, YELLOW);

            DrawLIDAR(160, 120, 1, original);
            // set arrow angle and distance to the closest lidar spot to the camera with 265 <= angle <= 275
            arrowangle = mRobot.threadLIDARInstance.closestToCamera.az;
            arrowdistance = mRobot.threadLIDARInstance.closestToCamera.dist;

            DrawArrow(160, 120, arrowangle, arrowdistance / 6, original);

            Point cpt = new Point(160, 120);
            Imgproc.putText(original, "" + arrowdistance + "cm", cpt, 0, 0.5, GREEN);
        }// DrawAll
    }// ColourRegions class
}// class VisionThreadNew

// notes:
// check out canny call might be useful
// http://docs.opencv.org/2.4/doc/tutorials/imgproc/shapedescriptors/find_contours/find_contours.html?highlight=findcontours