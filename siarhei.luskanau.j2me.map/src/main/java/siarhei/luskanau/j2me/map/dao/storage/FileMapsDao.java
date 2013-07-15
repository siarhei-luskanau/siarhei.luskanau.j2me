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

package siarhei.luskanau.j2me.map.dao.storage;

import java.util.Enumeration;

import siarhei.luskanau.j2me.core.storage.Storage;
import siarhei.luskanau.j2me.core.storage.StorageEngine;
import siarhei.luskanau.j2me.core.storage.engine.FileStorageEngine;
import siarhei.luskanau.j2me.map.dao.MapsDao;
import siarhei.luskanau.j2me.map.engine.MapEngine;
import siarhei.luskanau.j2me.map.entity.Map;
import siarhei.luskanau.j2me.map.entity.XyzCoord;

/**
 * @author <a href="mailto:siarhei.luskanau@gmail.com">Siarhei Luskanau</a>
 */
public class FileMapsDao implements MapsDao {

    private final String STORAGE_NAME = "maps";

    private MapsRecord mapsRecord;

    public FileMapsDao(String baseUrl) throws Exception {
        try {
            StorageEngine storageEngine = new FileStorageEngine(baseUrl + STORAGE_NAME);
            Storage storage = new Storage(storageEngine);
            mapsRecord = new MapsRecord();
            storage.setRecord(mapsRecord);
            storageEngine.open();
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when create FileMapsDao.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public Map getMap(XyzCoord xyzCoord, MapEngine engine) throws Exception {
        try {
            String name = engine.getMapsName(xyzCoord);
            if (mapsRecord.exist(name)) {
                return mapsRecord.select(name);
            }
            return null;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when getMap in FileMapsDao.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public void setMap(Map map, XyzCoord xyzCoord, MapEngine engine) throws Exception {
        try {
            String name = map.getName();
            if (!mapsRecord.exist(name)) {
                mapsRecord.append(map);
            }
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when setMap in FileMapsDao.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public Enumeration getNamesMap() throws Exception {
        try {
            return mapsRecord.selectNames();
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when getNamesMap in FileMapsDao.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

}
