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

package siarhei.luskanau.j2me.map.engine;

import java.util.Hashtable;

import siarhei.luskanau.j2me.map.entity.XyzCoord;

/**
 * @author <a href="mailto:siarhei.luskanau@gmail.com">Siarhei Luskanau</a>
 */
public class GoogleMapEngine extends BaseMapEngine {

    public final String ROADMAP_TYPE = "roadmap";
    public final String SATELLITE_TYPE = "satellite";
    public final String HYBRID_TYPE = "hybrid";

    private final String ROADMAP_URL = "http://mt0.google.com/vt/lyrs=m@126&";
    private final String SATELLITE_URL = "http://khm0.google.ru/kh/v=60&";
    private final String HYBRID_URL = "http://mt0.google.com/vt/lyrs=y@126&";

    private String baseUrl;

    public String getEngineName() throws Exception {
        return "Google Maps";
    }

    public String[] getMapTypes() throws Exception {
        String[] mapTypes = { HYBRID_TYPE, SATELLITE_TYPE, ROADMAP_TYPE };
        return mapTypes;
    }

    public void setMapType(String mapType) throws Exception {
        try {
            if (ROADMAP_TYPE.equals(mapType)) {
                baseUrl = ROADMAP_URL;
            } else if (SATELLITE_TYPE.equals(mapType)) {
                baseUrl = SATELLITE_URL;
            } else if (HYBRID_TYPE.equals(mapType)) {
                baseUrl = HYBRID_URL;
            } else {
                throw new RuntimeException("Incorrect map type " + mapType);
            }
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when create GoogleMapEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public String getMapsName(XyzCoord xyzCoord) throws Exception {
        try {
            StringBuffer buffer = new StringBuffer();
            buffer.append("g-");
            if (ROADMAP_URL.equals(baseUrl)) {
                buffer.append(ROADMAP_TYPE);
            } else if (SATELLITE_URL.equals(baseUrl)) {
                buffer.append(SATELLITE_TYPE);
            } else if (HYBRID_URL.equals(baseUrl)) {
                buffer.append(HYBRID_TYPE);
            } else {
                throw new RuntimeException("Incorrect map type " + baseUrl);
            }
            buffer.append("-z").append(xyzCoord.getZoom());
            buffer.append("x").append(xyzCoord.getX());
            buffer.append("y").append(xyzCoord.getY());
            return buffer.toString();
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when getMapsName in GoogleMapEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public String getMapsUrl(XyzCoord xyzCoord) throws Exception {
        try {
            StringBuffer buffer = new StringBuffer();
            Hashtable params = new Hashtable();
            buffer.append(baseUrl);
            params.put("x", String.valueOf(xyzCoord.getX()));
            params.put("y", String.valueOf(xyzCoord.getY()));
            params.put("z", String.valueOf(xyzCoord.getZoom()));
            buffer.append(getParams(params));
            return buffer.toString();
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when getMapsUrl in GoogleMapEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

}
