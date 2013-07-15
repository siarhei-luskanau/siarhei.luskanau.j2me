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

package siarhei.luskanau.j2me.map.dao;

import java.util.Enumeration;
import java.util.Vector;

import siarhei.luskanau.j2me.map.engine.MapEngine;
import siarhei.luskanau.j2me.map.entity.Map;
import siarhei.luskanau.j2me.map.entity.XyzCoord;

/**
 * @author <a href="mailto:siarhei.luskanau@gmail.com">Siarhei Luskanau</a>
 */
public class MemoryMapsDao implements MapsDao {

    private Vector cache = new Vector();
    private int maxSize = 12;

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public Map getMap(XyzCoord xyzCoord, MapEngine engine) throws Exception {
        try {
            String name = engine.getMapsName(xyzCoord);
            for (Enumeration en = cache.elements(); en.hasMoreElements();) {
                Map currentMap = (Map) en.nextElement();
                if (currentMap.getName().equals(name)) {
                    return currentMap;
                }
            }
            return null;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when getMap in MemoryMapsDao.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public void setMap(Map map, XyzCoord xyzCoord, MapEngine engine) throws Exception {
        try {
            String name = map.getName();
            for (Enumeration en = cache.elements(); en.hasMoreElements();) {
                Map currentMap = (Map) en.nextElement();
                if (currentMap.getName().equals(name)) {
                    return;
                }
            }
            cache.addElement(map);
            for (; cache.size() > maxSize;) {
                cache.removeElementAt(0);
            }
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when setMap in MemoryMapsDao.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

}
