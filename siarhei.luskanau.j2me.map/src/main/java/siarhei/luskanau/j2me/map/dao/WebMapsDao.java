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

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import siarhei.luskanau.j2me.map.engine.MapEngine;
import siarhei.luskanau.j2me.map.entity.Map;
import siarhei.luskanau.j2me.map.entity.XyzCoord;

import com.sun.lwuit.io.util.Log;

/**
 * @author <a href="mailto:siarhei.luskanau@gmail.com">Siarhei Luskanau</a>
 */
public class WebMapsDao implements MapsDao {

    public Map getMap(XyzCoord xyzCoord, MapEngine engine) throws Exception {
        HttpConnection connection = null;
        InputStream inputStream = null;
        try {
            String url = engine.getMapsUrl(xyzCoord);
            String name = engine.getMapsName(xyzCoord);
            Log.p(url);
            Map map = new Map();
            map.setName(name);
            connection = (HttpConnection) Connector.open(url, Connector.READ_WRITE);
            connection.setRequestProperty("User-Agent", System.getProperty("microedition.platform"));
            int rc = connection.getResponseCode();
            if (rc != HttpConnection.HTTP_OK) {
                throw new IOException("HTTP response code: " + rc + ". HTTP response message: "
                        + connection.getResponseMessage());
            }
            inputStream = connection.openInputStream();
            int len = Integer.valueOf(connection.getHeaderField("content-length")).intValue();
            if (len > 0) {
                int actual = 0;
                int bytesread = 0;
                byte[] data = new byte[len];
                while ((bytesread != len) && (actual != -1)) {
                    actual = inputStream.read(data, bytesread, len - bytesread);
                    bytesread += actual;
                }
                map.setData(data);
            } else {
                throw new IOException("HTTP response: no content-length field");
            }
            return map;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when getMap in WebMapsDao.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Throwable t) {
                StringBuffer message = new StringBuffer();
                message.append("Error when close inputStream in getMap in WebMapsDao.");
                message.append("\n\t").append(t.toString());
                Log.p(message.toString(), Log.ERROR);
            }
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (Throwable t) {
                StringBuffer message = new StringBuffer();
                message.append("Error when close connection in getMap in WebMapsDao.");
                message.append("\n\t").append(t.toString());
                Log.p(message.toString(), Log.ERROR);
            }
        }
    }

    public void setMap(Map map, XyzCoord xyzCoord, MapEngine engine) throws Exception {
        // Do nothing
    }

}
