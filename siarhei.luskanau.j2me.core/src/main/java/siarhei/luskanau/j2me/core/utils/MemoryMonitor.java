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

import java.util.Timer;
import java.util.TimerTask;

import com.sun.lwuit.io.util.Log;

/**
 * @author <a href="mailto:siarhei.luskanau@gmail.com">Siarhei Luskanau</a>
 */
public class MemoryMonitor {

    private static MemoryMonitor instanse;

    public static void begin() {
        if (instanse == null) {
            instanse = new MemoryMonitor();
        }
    }

    public static void begin(int period) {
        if (instanse == null) {
            instanse = new MemoryMonitor(period);
        }
    }

    private MemoryMonitor() {
        this(5);
    }

    private MemoryMonitor(int period) {
        new Timer().schedule(new StatisticTask(), 0, period * 1000);
    }

    private class StatisticTask extends TimerTask {

        public void run() {
            Runtime runtime = Runtime.getRuntime();
            runtime.gc();
            StringBuffer message = new StringBuffer();
            long free = runtime.freeMemory();
            long total = runtime.totalMemory();
            message.append("MemoryMonitor: ").append(total - free).append("/").append(total);
            Log.p(message.toString(), Log.DEBUG);
        }

    }

}
