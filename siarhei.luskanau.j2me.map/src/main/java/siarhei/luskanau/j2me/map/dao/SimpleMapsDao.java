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

import java.io.InputStream;

import siarhei.luskanau.j2me.map.engine.MapEngine;
import siarhei.luskanau.j2me.map.entity.Map;
import siarhei.luskanau.j2me.map.entity.XyzCoord;

/**
 * @author <a href="mailto:siarhei.luskanau@gmail.com">Siarhei Luskanau</a>
 */
public class SimpleMapsDao implements MapsDao {

    private String baseUrl;

    public SimpleMapsDao(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Map getMap(XyzCoord xyzCoord, MapEngine engine) throws Exception {
        try {
            Map map = null;
            String name = engine.getMapsName(xyzCoord);
            String url = baseUrl + name;
            InputStream inputStream = getClass().getResourceAsStream(url);
            if (inputStream != null) {
                byte[] data = new byte[inputStream.available()];
                inputStream.read(data);
                map = new Map();
                map.setName(name);
                map.setData(data);
            }
            return map;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when getMap in SimpleMapsDao.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public void setMap(Map map, XyzCoord xyzCoord, MapEngine engine) throws Exception {
        // Do nothing
    }

}
