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

package siarhei.luskanau.j2me.map.task;

import java.util.Vector;

import siarhei.luskanau.j2me.core.app.BaseReg;
import siarhei.luskanau.j2me.map.app.AppMapReg;
import siarhei.luskanau.j2me.map.dao.storage.FileMapsDao;
import siarhei.luskanau.j2me.map.engine.MapEngine;
import siarhei.luskanau.j2me.map.entity.Map;
import siarhei.luskanau.j2me.map.entity.XyzCoord;
import siarhei.luskanau.j2me.map.taskpool.Task;

import com.sun.lwuit.Form;
import com.sun.lwuit.io.util.Log;

/**
 * @author <a href="mailto:siarhei.luskanau@gmail.com">Siarhei Luskanau</a>
 */
public class StorageLoadMapsTask extends LoadMapsTask {

    private XyzCoord xyzCoord;
    private MapEngine engine;
    private Vector mapsDaoStack;
    private int current;

    public StorageLoadMapsTask(XyzCoord xyzCoord, MapEngine engine, Vector mapsDaoStack, int current) {
        this.xyzCoord = xyzCoord;
        this.engine = engine;
        this.mapsDaoStack = mapsDaoStack;
        this.current = current;
    }

    public void doTask() throws Exception {
        try {
            AppMapReg appReg = (AppMapReg) BaseReg.instance();
            Form form = appReg.getCurrentForm();
            if (form instanceof MapUpdate) {
                MapUpdate mapUpdate = (MapUpdate) form;
                if (xyzCoord == null) {
                    xyzCoord = mapUpdate.getXyzCoordForUpdate();
                }
                if (xyzCoord == null) {
                    return;
                }
                FileMapsDao fileMapsDao = (FileMapsDao) mapsDaoStack.elementAt(current);
                Map map = null;
                try {
                    map = fileMapsDao.getMap(xyzCoord, engine);
                } catch (Throwable t) {
                    StringBuffer message = new StringBuffer();
                    message.append("Error when getMap in doTask in StorageLoadMapsTask.");
                    message.append("\n\t").append(t.toString());
                    Log.p(message.toString(), Log.ERROR);
                }
                if (map != null) {
                    mapUpdate.onMapUpdate(map);
                } else {
                    nextTask(xyzCoord, engine, mapsDaoStack, current);
                }
            }
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when doTask in StorageLoadMapsTask.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public int getType() throws Exception {
        return Task.OTHER_TYPE;
    }

}
