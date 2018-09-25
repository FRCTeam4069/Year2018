package frc.team4069.robot.vision;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

public class ThreadVideoCapture implements Runnable {

    public static final int VCT_USB = 0;
    public static final int VCT_IP = 1;

    // OpenCV constants
    public static final int CV_CAP_PROP_BRIGHTNESS = 10;
    public static final int CV_CAP_PROP_CONTRAST = 11;
    public static final int CV_CAP_PROP_EXPOSURE_ABSOLUTE = 39;
    public static final int CV_CAP_PROP_FRAME_WIDTH = 3;
    public static final int CV_CAP_PROP_FRAME_HEIGHT = 4;

    public boolean cameraConnected = false;

    public Mat[] FrameQueue;
    public int FrameInIndex = 0;
    public int FrameOutIndex = 0;
    int mFrameBufferDepth = 2;
    int mKeepRunning = 1;
    int mCameraConnectionType = VCT_USB;
    boolean mEnabled = true;
    private Object InLock;
    private Object OutLock;
    private Mat frame; // current frame being read, dont use this
    private VideoCapture vcap;

    public static void main(String args[]) {
        // (new Thread(new VideoCaptureThread())).start();
    }// main

    public void run() {
        FrameQueue = new Mat[mFrameBufferDepth];
        for (int i = 0; i < mFrameBufferDepth; i++) {
            FrameQueue[i] = new Mat();
        }
        InLock = new Object();
        OutLock = new Object();
        InitCapture();

        while (mKeepRunning == 1) {
            Capture();
        }
    }// run

    /**
     * Enable reading frames
     */
    public void Enable() {
        mEnabled = true;
    }

    /**
     * Disable reading frames, sleeps a lot
     */
    public void Disable() {
        mEnabled = false;
    }

    /*
     * InitCapture
     */
    private void InitCapture() {
        vcap = new VideoCapture();
        if (vcap == null) {
            System.out.println("vcap is null!");
        }
        switch (mCameraConnectionType) {
            case VCT_USB:
                SetupUSBCamera();
                break;
            case VCT_IP:
                SetupIPCamera("10.40.69.44"); // ip address of Axis camera
                break;
        }
    }// InitCapture

    /*
     * Read a frame into framequeue, update indexes allows upto 4 frames to be stored. IF no frames have been removed, update last frame location with new frame
     *
     */
    private void Capture() {
        while ((true) && (mKeepRunning == 1)) {
            if (mEnabled) {
                // System.out.println("About to read frame index="+FrameInIndex);
                boolean state = vcap
                        .read(FrameQueue[FrameInIndex]); // FrameInIndex is always 'safe' to write too by definition
                if (state == false) {
                    System.out.println("FAILED reading frame");
                } else {
                    int getcurin = FrameInIndex; // always safe to read...
                    getcurin++; // update locally to new values
                    getcurin %= (mFrameBufferDepth);
                    if (getcurin
                            != FrameOutIndex) // if we are not bumping up against frames waiting to be read...
                    {
                        synchronized (InLock) // only lock writes, reading is always ok
                        {
                            FrameInIndex = getcurin; // write in single statement
                        } // synchronized
                    } // if curin != out
                } // else frame read successful
                // outputStream.putFrame(frame);

                try {
                    Thread.sleep(60);// yeild some time 5ms == 200fps, no worries there
                } catch (InterruptedException e) {
                    System.out.println("Sleep fail in Capture");
                    e.printStackTrace();
                    mKeepRunning = 0;
                } // catch
            } // if enabled
            else {
                try {
                    Thread.sleep(250);// 4 times a sec check if we should run
                } catch (InterruptedException e) {
                    System.out.println("Sleep fail in Capture");
                    e.printStackTrace();
                    mKeepRunning = 0;
                } // catch

            } // else Enabled
        } // while true
    }// Capture

    /*
     * Get next available frame, returns clone
     */
    public Mat GetFrame() {
        if (FrameOutIndex != FrameInIndex) {
            Mat frameout = FrameQueue[FrameOutIndex]; // .clone(); // NOTE copy!
            int frametosend = FrameOutIndex; // safe to read...
            frametosend++;
            frametosend %= mFrameBufferDepth; // do manipulations locally

            synchronized (OutLock) // only lock write, reading is always safe
            {
                FrameOutIndex = frametosend; // update in single statment
            }
            return frameout;
        }
        return null; // no frames available if queue has been drained,
        // we could keep last frame sent and resend it, but calling routine could do that
    }// GetFrame

    /*****************************************************************************************
     * Init USB camera
     */
    private void SetupUSBCamera() {
        int videoStreamAddress = 0;
        System.out.println("Trying to connect to Camera stream... at: " + videoStreamAddress);
        int count = 1;

        while ((!vcap.open(videoStreamAddress)) && (mKeepRunning == 1)) {

            //System.out.println("Error connecting to camera stream, retrying " + count);
            count++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } // while !open

        vcap.set(Videoio.CAP_PROP_FRAME_WIDTH, 320);
        vcap.set(Videoio.CAP_PROP_FRAME_HEIGHT, 200);

        // if (!vcap.set(CV_CAP_PROP_EXPOSURE_ABSOLUTE, 0.1))
        // {
        // System.out.println("Error exp absolute");
        // }
        // vcap.set(Videoio.CAP_PROP_AUTO_EXPOSURE,0);
        if (!vcap.set(Videoio.CAP_PROP_EXPOSURE, 0)) {
            System.out.println("Error prop exposure");
        }

        if (!vcap.set(Videoio.CAP_PROP_BRIGHTNESS,
                0)) // .1)) //.1)) //; //CV_CAP_PROP_BRIGHTNESS, 1);
        {
            System.out.println("Error brightness");
        }
        if (!vcap.set(Videoio.CAP_PROP_CONTRAST, -1)) // ; //CV_CAP_PROP_CONTRAST, 0);
        {
            System.out.println("Error contrast");
        }
        System.out.println("vcap width = " + vcap.get(Videoio.CAP_PROP_FRAME_WIDTH));

        System.out.println("vcap height=" + vcap.get(Videoio.CAP_PROP_FRAME_HEIGHT));
        cameraConnected = true;
        System.out.println("Successfully connected to USB Camera Stream");

    } // SetupUSBCamera

    /**************************************************************************************
     * Sets up IP camera, must pass IP address as string ex 192.168.1.44
     */
    private void SetupIPCamera(String ip) {
        String videoStreamAddress = "http://" + ip + "/mjpg/video.mjpg";

        System.out.println("Trying to connect to Camera stream... at: " + videoStreamAddress);

        int count = 1;

        while ((!vcap.open(videoStreamAddress)) && (mKeepRunning == 1)) {
            System.out.println("Error connecting to camera stream, retrying " + count);
            count++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } // while !vcap.open
        cameraConnected = true;
        System.out.println("Successfully connected to IP Camera Stream");
    }// SetupIPCamera

}// VideoCaptureThread class
