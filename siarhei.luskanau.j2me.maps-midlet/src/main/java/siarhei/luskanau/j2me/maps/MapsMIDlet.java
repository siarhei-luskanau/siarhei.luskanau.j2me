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

package siarhei.luskanau.j2me.maps;

import javax.microedition.midlet.MIDlet;

import siarhei.luskanau.j2me.core.app.BaseReg;
import siarhei.luskanau.j2me.core.app.WelcomeCanvas;
import siarhei.luskanau.j2me.core.utils.MemoryMonitor;
import siarhei.luskanau.j2me.core.utils.MyLog;
import siarhei.luskanau.j2me.maps.app.AppReg;
import siarhei.luskanau.j2me.maps.app.AppTheme;

import com.sun.lwuit.Display;
import com.sun.lwuit.io.util.Log;

/**
 * @author <a href="mailto:siarhei.luskanau@gmail.com">Siarhei Luskanau</a>
 */
public class MapsMIDlet extends MIDlet {

    private static MapsMIDlet midlet;

    private WelcomeCanvas welcomeCanvas;

    /**
     * Correct destroy application
     */
    public static void destroy() {
        midlet.notifyDestroyed();
    }

    public MapsMIDlet() {
        midlet = this;
        welcomeCanvas = new WelcomeCanvas(midlet);
        welcomeCanvas.setLogo("Maps");
        javax.microedition.lcdui.Display.getDisplay(this).setCurrent(welcomeCanvas);
        new Thread(new LoadingThread()).start();
    }

    protected void destroyApp(boolean arg0) {
    }

    protected void pauseApp() {
    }

    protected void startApp() {
    }

    private class LoadingThread implements Runnable {
        public void run() {
            try {
                welcomeCanvas.setStatusKoef(0.1).setStatus("Logger...");
                Log.install(new MyLog("maps"));

                welcomeCanvas.setStatusKoef(0.5).setStatus("Memory monitor...");
                MemoryMonitor.begin(5);

                welcomeCanvas.setStatusKoef(0.6).setStatus("Load library...");
                Display.init(midlet);

                welcomeCanvas.setStatusKoef(0.8).setStatus("Load theme...");
                new AppTheme().setMyTheme();

                BaseReg.setInstance(new AppReg());
                AppReg.instance().init(welcomeCanvas);
            } catch (Throwable t) {
                welcomeCanvas.setError(t);
            }
        }
    }

}
