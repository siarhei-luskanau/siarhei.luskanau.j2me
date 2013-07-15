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
import siarhei.luskanau.j2me.map.dao.MapsDao;
import siarhei.luskanau.j2me.map.dao.WebMapsDao;
import siarhei.luskanau.j2me.map.dao.storage.FileMapsDao;
import siarhei.luskanau.j2me.map.engine.MapEngine;
import siarhei.luskanau.j2me.map.entity.XyzCoord;
import siarhei.luskanau.j2me.map.taskpool.Task;

import com.sun.lwuit.Form;

/**
 * @author <a href="mailto:siarhei.luskanau@gmail.com">Siarhei Luskanau</a>
 */
public abstract class LoadMapsTask implements Task {

    protected void nextTask(XyzCoord xyzCoord, MapEngine engine, Vector mapsDaoStack, int current)
            throws Exception {
        try {
            boolean absent = true;
            for (int i = current + 1; i < mapsDaoStack.size(); i++) {
                MapsDao mapsDao = (MapsDao) mapsDaoStack.elementAt(i);
                if (mapsDao instanceof FileMapsDao) {
                    Task task = new StorageLoadMapsTask(xyzCoord, engine, mapsDaoStack, i);
                    ((AppMapReg) BaseReg.instance()).getFilePool().addTask(task);
                    absent = false;
                    break;
                }
                if (mapsDao instanceof WebMapsDao) {
                    Task task = new WebLoadMapsTask(null, engine, mapsDaoStack, i);
                    ((AppMapReg) BaseReg.instance()).getWebPool().addTask(task);
                    absent = false;
                    break;
                }
            }
            if (absent) {
                AppMapReg appReg = (AppMapReg) BaseReg.instance();
                Form form = appReg.getCurrentForm();
                if (form instanceof MapUpdate) {
                    MapUpdate mapUpdate = (MapUpdate) form;
                    mapUpdate.onMapAbsent(engine.getMapsName(xyzCoord));
                }
            }
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when nextTask in LoadMapsTask.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }
}
