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

import siarhei.luskanau.j2me.map.entity.XyzCoord;

/**
 * @author <a href="mailto:siarhei.luskanau@gmail.com">Siarhei Luskanau</a>
 */
public class OpenStreetMapEngine extends BaseMapEngine {

    // http://c.tile.openstreetmap.org/17/76829/43030.png
    // http://c.tah.openstreetmap.org/Tiles/17/76829/43030.png

    public final String MAPNIK_TYPE = "mapnik";
    public final String OSMARENDER_TYPE = "osmarender";

    private final int MAPNIK_INT = 1;
    private final int OSMARENDER_INT = 2;

    private final String MAPNIK_URL_1 = "http://";
    private final String MAPNIK_URL_2 = ".tile.openstreetmap.org";

    private final String OSMARENDER_URL_1 = "http://";
    private final String OSMARENDER_URL_2 = ".tah.openstreetmap.org/Tiles/tile";

    private int currentMapType;
    private int domen = 0;

    public String getEngineName() throws Exception {
        return "Open Street Map";
    }

    public void setMapType(String mapType) throws Exception {
        try {
            if (MAPNIK_TYPE.equals(mapType)) {
                currentMapType = MAPNIK_INT;
            } else if (OSMARENDER_TYPE.equals(mapType)) {
                currentMapType = OSMARENDER_INT;
            } else {
                throw new RuntimeException("Incorrect map type " + mapType);
            }
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when create OpenStreetMapEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public String[] getMapTypes() throws Exception {
        String[] mapTypes = { MAPNIK_TYPE, OSMARENDER_TYPE };
        return mapTypes;
    }

    public String getMapsName(XyzCoord xyzCoord) throws Exception {
        try {
            StringBuffer buffer = new StringBuffer();
            buffer.append("osm-");
            switch (currentMapType) {
            case MAPNIK_INT:
                buffer.append(MAPNIK_TYPE);
                break;
            case OSMARENDER_INT:
                buffer.append(OSMARENDER_TYPE);
                break;
            default:
                throw new RuntimeException("Incorrect map type " + currentMapType);
            }
            buffer.append("-z").append(xyzCoord.getZoom());
            buffer.append("x").append(xyzCoord.getX());
            buffer.append("y").append(xyzCoord.getY());
            return buffer.toString();
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when getMapsName in OpenStreetMapEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public String getMapsUrl(XyzCoord xyzCoord) throws Exception {
        try {
            StringBuffer buffer = new StringBuffer();
            switch (currentMapType) {
            case MAPNIK_INT:
                buffer.append(MAPNIK_URL_1).append(getDomen()).append(MAPNIK_URL_2);
                break;
            case OSMARENDER_INT:
                buffer.append(OSMARENDER_URL_1).append(getDomen()).append(OSMARENDER_URL_2);
                break;
            default:
                throw new RuntimeException("Incorrect map type " + currentMapType);
            }
            buffer.append("/").append(xyzCoord.getZoom());
            buffer.append("/").append(xyzCoord.getX());
            buffer.append("/").append(xyzCoord.getY());
            buffer.append(".png");
            return buffer.toString();
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when getMapsUrl in OpenStreetMapEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    private String getDomen() throws Exception {
        try {
            domen++;
            switch (domen) {
            case 1:
                return "a";
            case 2:
                return "b";
            default:
                domen = 0;
                return "c";
            }
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when getDomen in OpenStreetMapEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

}
