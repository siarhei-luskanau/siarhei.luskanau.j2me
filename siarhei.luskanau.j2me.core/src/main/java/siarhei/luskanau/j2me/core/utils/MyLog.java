/*
 * Licensed by the authors under the Creative Commons
 * Attribution-ShareAlike 2.0 Generic (CC BY-SA 2.0)
 * License:
 *
 * http://creativecommons.org/licenses/by-sa/2.0/
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package siarhei.luskanau.j2me.core.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.io.file.FileSystemRegistry;

import com.sun.lwuit.io.util.Log;

/**
 * @author <a href="mailto:siarhei.luskanau@gmail.com">Siarhei Luskanau</a>
 */
public class MyLog extends Log {

    private PrintStream susOut;
    private Vector appenders;

    public MyLog(String name) {
        susOut = System.out;
        if (getLevel() != Log.DEBUG) {
            return;
        }
        appenders = new Vector();
        appenders.addElement(susOut);
        addFile("file:///E:/" + name + ".log.txt", appenders);
        logSysInfo();
    }

    private void logSysInfo() {
        print("# microedition", DEBUG);
        testMaxSizeMemory();
        logSysInfo("microedition.platform");
        logSysInfo("microedition.configuration");
        logSysInfo("microedition.profiles");
        logSysInfo("microedition.encoding");
        logSysInfo("microedition.locale");
        logSysInfo("microedition.commports");
        logSysInfo("microedition.hostname");

        print("# (JSR-75 Optional package) FileConnection API", DEBUG);
        logSysInfo("microedition.io.file.FileConnection.version");
        logSysInfo("file.separator");

        print("# (JSR-75 Optional package) PIM API", DEBUG);
        logSysInfo("microedition.pim.version");

        print("# (JSR-82) Bluetooth API", DEBUG);
        logSysInfo("bluetooth.api.version");

        print("# (JSR 135) Mobile Media API 1.2", DEBUG);
        logSysInfo("microedition.media.version");
        logSysInfo("supports.mixing");
        logSysInfo("supports.audio.capture");
        logSysInfo("supports.video.capture");
        logSysInfo("supports.recording");
        logSysInfo("audio.encodings");
        logSysInfo("video.encodings");
        logSysInfo("video.snapshot.encodings");
        logSysInfo("streamable.contents");

        print("# (JSR-179) Location API", DEBUG);
        logSysInfo("microedition.location.version");

        print("# (JSR-184) Mobile 3D Graphics API 1.1", DEBUG);
        logSysInfo("microedition.m3g.version");

        print("# (JSR-226) Scalable 2D Vector Graphics API", DEBUG);
        logSysInfo("microedition.m2g.version");
        logSysInfo("microedition.m2g.svg.baseProfile");
        logSysInfo("microedition.m2g.svg.version");

        print("# (JSR-234) AMMS API", DEBUG);
        logSysInfo("microedition.amms.version");
        logSysInfo("supports.mediacapabilities");
        logSysInfo("tuner.modulations");
        logSysInfo("audio.samplerates");
        logSysInfo("audio3d.simultaneouslocations");
        logSysInfo("camera.orientations");
        logSysInfo("camera.resolutions");

        print("# (JSR-256) Mobile Sensor API", DEBUG);
        logSysInfo("microedition.sensor.version");

        print("# Nokia UI API 1.3", DEBUG);
        logSysInfo("com.nokia.mid.ui.version");
        logSysInfo("com.nokia.mid.ui.joystick_event");
        logSysInfo("com.nokia.mid.ui.softnotification");

    }

    private void logSysInfo(String propertyName) {
        try {
            String property = System.getProperty(propertyName);
            if (property != null) {
                StringBuffer buffer = new StringBuffer();
                buffer.append(propertyName).append(" : ").append(property);
                print(buffer.toString(), DEBUG);
            }
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when logSysInfo ").append(propertyName).append(" in MyLog.");
            message.append("\n\t").append(t.toString());
            susOut.println(message.toString());
        }
    }

    private void testMaxSizeMemory() {
        try {
            Runtime runtime = Runtime.getRuntime();
            long total = runtime.totalMemory();
            try {
                Vector vector = new Vector();
                for (;;) {
                    vector.addElement(new byte[8192]);
                    total = runtime.totalMemory();
                }
            } catch (OutOfMemoryError e) {
            }
            runtime.gc();
            runtime.gc();
            StringBuffer buffer = new StringBuffer();
            buffer.append("MaxSizeMemory : ").append(total);
            print(buffer.toString(), DEBUG);
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when testMaxSizeMemory in MyLog.");
            message.append("\n\t").append(t.toString());
            susOut.println(message.toString());
        }
    }

    protected void print(String text, int level) {
        if (getLevel() > level) {
            return;
        }
        StringBuffer messageBuffer = new StringBuffer();
        switch (level) {
        case Log.DEBUG:
            messageBuffer.append("[DEBUG]");
            break;
        case Log.INFO:
            messageBuffer.append("[INFO]");
            break;
        case Log.WARNING:
            messageBuffer.append("[WARNING]");
            break;
        case Log.ERROR:
            messageBuffer.append("[ERROR]");
            break;
        default:
            messageBuffer.append("[]");
        }
        messageBuffer.append(" ");

        Calendar time = Calendar.getInstance();
        time.setTime(new Date());

        messageBuffer.append(time.get(Calendar.YEAR)).append("-").append(time.get(Calendar.MONTH) + 1)
                .append("-").append(time.get(Calendar.DAY_OF_MONTH)).append(" ")
                .append(time.get(Calendar.HOUR_OF_DAY)).append(":").append(time.get(Calendar.MINUTE))
                .append(":").append(time.get(Calendar.SECOND)).append(",")
                .append(time.get(Calendar.MILLISECOND));

        messageBuffer.append(" - ");
        messageBuffer.append(text);
        messageBuffer.append("\n");

        for (Enumeration en = appenders.elements(); en.hasMoreElements();) {
            try {
                PrintStream appenser = (PrintStream) en.nextElement();
                appenser.write(messageBuffer.toString().getBytes("utf-8"));
            } catch (Throwable t) {
                StringBuffer message = new StringBuffer();
                message.append("Error when write in appenser in MyLog.");
                message.append("\n\t").append(t.toString());
                susOut.println(message.toString());
            }
        }
    }

    private void addFile(String url, Vector vector) {
        try {
            if (url == null) {
                Enumeration en = FileSystemRegistry.listRoots();
                for (; en.hasMoreElements();) {
                    String root = (String) en.nextElement();
                    url = "file:///" + root + "log.txt";
                }
            }
            FileConnection c = (FileConnection) Connector.open(url);
            if (!c.exists()) {
                c.create();
            }
            PrintStream printStream = new FlushedPrintStream(c.openOutputStream(c.fileSize()));
            vector.addElement(printStream);
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when addFile in MyLog.");
            message.append(" File: ").append(url);
            message.append("\n\t").append(t.toString());
            susOut.println(message.toString());
        }
    }

    private class FlushedPrintStream extends PrintStream {
        public FlushedPrintStream(OutputStream output) {
            super(output);
        }

        public void print(boolean arg0) {
            super.print(arg0);
            super.flush();
        }

        public void print(char arg0) {
            super.print(arg0);
            super.flush();
        }

        public void print(int arg0) {
            super.print(arg0);
            super.flush();
        }

        public void print(long arg0) {
            super.print(arg0);
            super.flush();
        }

        public void print(float arg0) {
            super.print(arg0);
            super.flush();
        }

        public void print(double arg0) {
            super.print(arg0);
            super.flush();
        }

        public void print(char[] arg0) {
            super.print(arg0);
            super.flush();
        }

        public void print(String arg0) {
            super.print(arg0);
            super.flush();
        }

        public void print(Object arg0) {
            super.print(arg0);
            super.flush();
        }

        public void println() {
            super.println();
            super.flush();
        }

        public void println(boolean arg0) {
            super.println(arg0);
            super.flush();
        }

        public void println(char arg0) {
            super.println(arg0);
            super.flush();
        }

        public void println(int arg0) {
            super.println(arg0);
            super.flush();
        }

        public void println(long arg0) {
            super.println(arg0);
            super.flush();
        }

        public void println(float arg0) {
            super.println(arg0);
            super.flush();
        }

        public void println(double arg0) {
            super.println(arg0);
            super.flush();
        }

        public void println(char[] arg0) {
            super.println(arg0);
            super.flush();
        }

        public void println(String arg0) {
            super.println(arg0);
            super.flush();
        }

        public void println(Object arg0) {
            super.println(arg0);
            super.flush();
        }

        public void write(int arg0) {
            super.write(arg0);
            super.flush();
        }

        public void write(byte[] arg0, int arg1, int arg2) {
            super.write(arg0, arg1, arg2);
            super.flush();
        }

        public void write(byte[] arg0) throws IOException {
            super.write(arg0);
            super.flush();
        }
    }

}
