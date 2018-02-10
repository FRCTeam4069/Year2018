package frc.team4069.robot.vision;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.SerialPort.FlowControl;
import java.io.UnsupportedEncodingException;

public class LidarThread implements Runnable {

    public double lastAngle = 0.0;
    public int lastSignalStrength = 0;
    public double lastDistance = 0.0;
    public int lastStatus = 0;
    public String mDSResponse = "";
    public String mDXResponse = "";
    public String mMSResponse = "";
    public String mLRResponse = "";
    public String mLIResponse = "";
    public String mMIResponse = "";
    public String mIVResponse = "";
    public String mIDResponse = "";
    public String lastError = "";
    public String lastMessage = "";
    public boolean instructedToUpdate = false;
    public LidarSpot closestToCamera = new LidarSpot();
    public LidarSpot[] history = new LidarSpot[501];
    public int historyIndexIN = 0;
    public int historyIndexOUT = 0;
    // Note status 00=no error, 22=stopped to verify error, 55 = hardware trouble, 99 = resuming operation
    SerialPort mSerpt; // = new SerialPort(115200,SerialPort.Port.kOnboard);
    int mState = 0;
    int[] ulastPacket = new int[32]; // will hold unsigned values for last packet
    byte[] lastPacket = new byte[32];
    private boolean shouldResetClosestToCamera = true;
    private Object lock = new Object();
    private LidarSpot[] writeBuffer = new LidarSpot[501];

    public LidarThread() {
        for (int i = 0; i < writeBuffer.length; i++) {
            history[i] = new LidarSpot();
            writeBuffer[i] = new LidarSpot(); // pre-create history array will synchronize on these as written/read from
        }
    }

    /**
     * Get point from history, inited to all zeros, so 'safe' to just pull from
     */
    public LidarSpot getHistoryPoint() {
        synchronized (lock) {
            LidarSpot ls;
            synchronized (history[historyIndexOUT]) {
                ls = history[historyIndexOUT].clone();
            }
            historyIndexOUT++;

            historyIndexOUT %= 500;
            return ls;
        }
    }// getHistoryPoint

    private void addPointToHistory(double angle, int distance, int sigstr, int stat) {
        synchronized (lock) {
            synchronized (history[historyIndexIN]) {
                writeBuffer[historyIndexIN].az = angle;
                writeBuffer[historyIndexIN].dist = distance;
                writeBuffer[historyIndexIN].ss = sigstr;
                writeBuffer[historyIndexIN].stat = stat;
            }
            historyIndexIN++;
            if (historyIndexIN >= 500) {
                swapBuffers();
            }
            historyIndexIN %= 500;
        }
    }// assPointToHistory

    private void swapBuffers() {
        LidarSpot[] temp = history;
        history = writeBuffer;
        writeBuffer = temp;
    }

    public void run() {
        lastError = "Startup...";
        lastMessage = "Startup...";
        connectToSerial();
        System.out.println("Sending RR Reset command...");
        mSerpt.writeString("RR\r\n");
        doSleep(5000);

        // System.out.println("Startup DX Cmd:" + doDXCmd());
        // doSleep(2000);
        System.out.println("Calling IV command...");
        doIVCmd();
        lastMessage = mIVResponse;

        System.out.println("IVCMD:" + mIVResponse);

        // System.out.println("Calling do mi command");
        // doMICmd();

        // System.out.println("MICMD:" + mMIResponse);

        String o = doIDCmd();
        System.out.println("IntialID = " + o);
        lastMessage = mIDResponse;
        // doMSCmd("01"); //
        // doSleep(2000);
        // System.out.println("MOTOR2: " + mMSResponse);

        // Now loop till we get a ID which makes sense
        while (mState == 0) {
            doIDCmd();
            if (mIDResponse.contains("ID115200")) {
                lastMessage = mIDResponse;
                mState = 1;
            } else {
                System.out.println("ID fail len=" + mIDResponse.length() + " val=" + mIDResponse);
                System.out.println("Sending DX..." + doDXCmd());
                doSleep(1500);
            }

        } // while
        lastMessage = mIDResponse;
        System.out.println("ID Response: " + mIDResponse);

        doDSCmd(); // Now trigger stream...

        lastMessage = mDSResponse;
        System.out.println("GOT DS HEADER: " + mDSResponse);

        int bigctr = 0;

        while (mState == 1) {
            int lctr = 0;
            while (mSerpt.getBytesReceived() < 7) // Wait for 7 bytes to be in buffer
            {
                lctr++;
                doSleep(4);
                lastError = "Waiting for 7 - " + mSerpt.getBytesReceived() + " in queue " + lctr;
            } // while not 7 bytes waiting...

            lastPacket = mSerpt.read(7); // Grab 7 bytes
            mSerpt.readString();
            bigctr++;
            lastMessage = "Read packet(" + bigctr + ")";

            // System.out.print("PACKET(" + bigctr + ": ");
            bigctr++;
            for (int i = 0; i < lastPacket.length; i++) {
                int pval = lastPacket[i];
                if (pval < 0) {
                    pval += 256;
                }
                ulastPacket[i] = pval;
                // System.out.print("," + pval);
            }
            // System.out.println();
            // System.out.println("Converted packet");
            double angle = 0.0;
            int dist = 0;
            int ss = 0;

            int stat = 0;

            if (checkDSChecksum(ulastPacket)
                    == 1) // double azimuth, int distance, int signalstrength, int status)
            {
                stat = ulastPacket[0];
                dist = ulastPacket[3] + (ulastPacket[4] << 8);
                ss = ulastPacket[5];

                int ang = (ulastPacket[2] << 8) + ulastPacket[1];
                angle = 1.0 * ((ang >> 4) + ((ang & 15) / 16.0));
                addPointToHistory(angle, dist, ss, stat);

                // set distance from target to closest lidar point in front of camera
                if (angle >= 265 && angle <= 275) {
                    if (dist < closestToCamera.dist || shouldResetClosestToCamera) {
                        closestToCamera.az = angle;
                        closestToCamera.dist = dist;
                        closestToCamera.ss = ss;
                        closestToCamera.stat = stat;
                        shouldResetClosestToCamera = false;
                    }
                } else {
                    shouldResetClosestToCamera = true;
                }

                lastMessage =
                        "LIDAR: az:" + angle + ", dist:" + dist + ",sig:" + ss + ", stat:" + stat;
                // System.out.println("LIDAR: az:" + az + ", dist:" + dist + ",sig:" + ss + ", stat:" + stat);
                lastAngle = angle;
                lastDistance = dist;
                lastSignalStrength = ss;
                lastStatus = stat;
            } else {
                // System.out.println("ERR LIDARPACKET");
            }

            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
            }
        } // while mState==1

        if (mState == 99) // exit?
        {
            // doDXCmd();
            // mSerpt.reset();
            // mSerpt.free();
        }
    }// run

    /**
     * Passed in a unsigned int packet, checks the checksum, returns 0 if fail, 1 if success
     */
    int checkDSChecksum(int[] upkt) {
        int sum = 0;
        for (int i = 0; i < 6; i++) {
            int aval = upkt[i];
            sum += aval;
        }
        int val = sum % 255;

        int want = upkt[6];

        if (val != want) {
            lastError = "ChecksumFail Want:" + want + ", got:" + val;
            // System.out.println("ChecksumFail Want:" + want + ", got:" + val);
            return 0; // fail checksum
        }
        return 1;
    }// checkDSPacket

    /**
     * Pass in command like 'IV\r\n', will return response from the command
     */
    String doLIDARCommand(String cmd) {
        System.out.println("DOLIDARCOMMAND:.....");
        int numlf = 1;
        if ((cmd.contains("MS")) || (cmd.contains("LR"))) {
            numlf = 2;
        }

        doPrint("Docmd:" + cmd + " numlf=" + numlf + " length:" + cmd.length());
        int responseok = 0;
        String resp = "";

        while (responseok == 0) {
            int gotrsp = 0;

            int numsent = mSerpt.write(cmd.getBytes(), cmd.length());

            if (numsent != cmd.length()) {
                System.out.println("Error not sent all bytes!");
            }

            doPrint("Sent all " + numsent + " bytes for " + cmd + " command");

            byte[] response = new byte[32];
            gotrsp = readNonStreamingResponse(response, numlf); // -1 is timeout
            resp = "NoResponseTimeout";
            if (gotrsp == -1) {
                System.out.println("No Response Timeout");
                // return resp; // no response timeout
                doSleep(500);
                doPrint("Trying Command " + cmd + " Again");
            } else {
                responseok = 1;
            }
            try {
                resp = new String(response, "UTF-8");
                if (cmd.charAt(0) == resp.charAt(0) && (cmd.charAt(1) == resp.charAt(1))) {
                    return resp;
                } else {
                    responseok = 0; // nope, response was for wrong command!
                }
            } catch (UnsupportedEncodingException e) {
            }
        } // while responseok==0
        // System.out.println("LIDAR gotrsp=" + gotrsp + " Response:" + resp);

        // System.out.println("CMD "+cmd+", response:"+resp);
        return resp;
    }// doLIDARCommand

    /**
     * All packets returned from lidar end with LF (0x10) Except 2 which have LF in the middle too. We want to pull a response upto the end of packet, in streaming mode there could be quite a few packets queued up in the recieve buffer, lets get one at a time.
     *
     * @param tothis[] array to store packet too
     *
     * in byte[] array must be 32 in length minimum
     */
    // we only want well/formed packets, ones ending in LF
    int readNonStreamingResponse(byte[] tothis, int numlf) {
        long lastlooptime = System.currentTimeMillis();
        int finished = 0;
        int idx = 0;
        byte[] abyte = new byte[1];
        int maxsize = tothis.length;
        int numlffnd = 0;
        // System.out.println("readnonstream response numlf = " + numlf);
        // System.out.println("Readnonstream 0");
        lastlooptime = System.currentTimeMillis();
        while ((finished == 0) && (idx < maxsize)) {
            if (System.currentTimeMillis() - lastlooptime > 1000) {
                System.out.println("ReadNonStream TIMEOUT");
                return -1;
            }

            // Now LF is the termination char, except for a copule command which have TWO LF in them, sigh.
            // System.out.println("readnonstream 1");

            try {
                if (mSerpt.getBytesReceived() > 0) {
                    abyte = mSerpt.read(1);
                    if (abyte.length > 0) {
                        // System.out.println("readnonstream2");
                        tothis[idx] = abyte[0];
                        idx++;
                        if (abyte[0] == 10) {
                            numlffnd++;
                            System.out.println("GOTLF num=" + numlffnd);
                            if (numlffnd == numlf) {
                                finished = 1;
                            } else {
                                System.out.println("ADDING _");
                                tothis[idx - 1] = '_';
                            }
                        } // if LF found
                        lastlooptime = System.currentTimeMillis();

                    } // if abyte.len . 0
                    else {
                        System.out.println("failed read 1 byte len=" + abyte.length);
                    }
                } // if bytes waiting
            } // try
            catch (Exception e) {
                System.out.println("Reading packet exception: " + e.getMessage());
            }
        } // while
        return idx;
    }// readResponse

    /**
     * Start Data Acquisition Immediate response: DSssSSLF ss=status,SS=sum Datablocks follow and continue until stopped format: SAADDsC S=synch/Error, aa=azimuth, DD=distance cm, s=signal strength, C=checksum
     */
    String doDSCmd() {
        mDSResponse = doLIDARCommand("DS\r\n");
        return mDSResponse;
    }

    /**
     * Stop data acquisition Stops outputting data returns ex: DXssSSlf ss=status, SS=sum
     */
    String doDXCmd() {
        mDXResponse = doLIDARCommand("DX\r\n");
        return mDXResponse;
    }

    /**
     * Adjust Motor Speed Change rotation speed, valid values 00-10 must have 2 digits!
     *
     * @param speed 2 digit string 00-10 Return example: MShhLFssSSLF hh=speed hz, ss=status, SS=sum
     */
    String doMSCmd(String speed) {
        mMSResponse = doLIDARCommand("MS" + speed + "\r\n");
        return mMSResponse;
    }

    /**
     * Adjust Lidar Sample Rate Change sample rate valid values 01=500-600hz, 02=750-800hz, 03=1000-1075hz
     *
     * @param val 01,02 or 03 (500-600),(750-800),(1000-1075) respectively.
     */
    String doLRCmd(String val) {
        mLRResponse = doLIDARCommand("LR" + val + "\r\n");
        return mLRResponse;
    }

    /**
     * LiDAR information Returns sample rate in ASCII returns: LIssLF ss=speed 01=500-600hz, 02=750-800hz 03-1000-1075hz
     */
    String doLICmd() {
        mLIResponse = doLIDARCommand("LI\r\n");
        return mLIResponse;
    }

    /**
     * Motor Information Returns: MIssLF ss=speed example: 05
     */

    String doMICmd() {
        mMIResponse = doLIDARCommand("MI\r\n");
        return mMIResponse;
    }

    /**
     * Version Details Return: IVSWEEPppffhssssssssLF pp=protocol, ff=firmware version, h=hardwareversion, sssssss=serial number
     */
    String doIVCmd() {
        mIVResponse = doLIDARCommand("IV\r\n");
        return mIVResponse;
    }

    /**
     * Device Information Returns: ID115200LMDssrrLF L=laser state M=Mode D=diagnostic ss=motor speed (2 bytes) ex: 05 rr sample rate 4 bytes ex: 0500
     */
    String doIDCmd() {
        mIDResponse = doLIDARCommand("ID\r\n");
        return mIDResponse;
    }

    /**
     * Reset scanner, has NO return value
     */
    String doRRCmd() {
        mSerpt.writeString("RR\r\n"); // reset device, no response
        return "no resp";
    }

    void connectToSerial() {
        SerialPort.Port lastUSBRetry = SerialPort.Port.kMXP;
        int success = 0;
        while (success == 0) {
            try {
                mSerpt = new SerialPort(115200, lastUSBRetry, 8, SerialPort.Parity.kNone,
                        SerialPort.StopBits.kOne);
                mSerpt.disableTermination();
                success = 1;
                System.out.println("LIDAR Connected to port " + lastUSBRetry);
            } catch (Exception e) {
                switch (lastUSBRetry) {
                    case kMXP:
                        lastUSBRetry = SerialPort.Port.kUSB;
                        break;
                    case kUSB:
                        lastUSBRetry = SerialPort.Port.kUSB1;
                        break;
                    case kUSB1:
                        lastUSBRetry = SerialPort.Port.kUSB2;
                        break;

                    case kUSB2:
                        lastUSBRetry = SerialPort.Port.kMXP;
                        break;
                } // switch
                lastError = e.getMessage() + ", retrying port " + lastUSBRetry;
                System.out.println(
                        "SERIAL_ERR:" + e.getMessage() + ", retrying port " + lastUSBRetry);
                doSleep(1000);
            }
        }
        mSerpt.setFlowControl(FlowControl.kNone);
        // mSerpt.setWriteBufferMode(WriteBufferMode.kFlushOnAccess);
        mSerpt.setReadBufferSize(32767);
    }

    void disconnectFromSerial() {
        mSerpt.reset();
        mSerpt.flush();
        mSerpt.free();
    }

    void doSleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    void doPrint(String s) {
        s.replace('\n', '_');
        s.replace('\r', '_');
        System.out.println(s);
    }

    public class LidarSpot {

        public double az = 0.0;
        public int dist = 0;
        public int ss = 0;
        public int stat = 0;

        LidarSpot() {
            az = 0.0;
            dist = 0;
            ss = 0;
            stat = 0;
        }

        LidarSpot(double a, int dst, int sstr, int st) {
            az = a;
            dist = dst;
            ss = sstr;
            stat = st;
        }

        public LidarSpot clone() {
            LidarSpot ls = new LidarSpot();
            ls.az = az;
            ls.dist = dist;
            ls.ss = ss;
            ls.stat = stat;
            return ls;
        }
    } // class Lidarspot
}
