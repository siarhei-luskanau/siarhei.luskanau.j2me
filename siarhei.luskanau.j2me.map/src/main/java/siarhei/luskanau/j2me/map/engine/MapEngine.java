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

import java.util.Enumeration;
import java.util.Hashtable;

import siarhei.luskanau.j2me.map.entity.LlzCoord;
import siarhei.luskanau.j2me.map.entity.XyzCoord;

/**
 * @author <a href="mailto:siarhei.luskanau@gmail.com">Siarhei Luskanau</a>
 */
public abstract class MapEngine {

    public abstract String getEngineName() throws Exception;

    public abstract String[] getMapTypes() throws Exception;

    public abstract void setMapType(String mapType) throws Exception;

    public abstract String getMapsName(XyzCoord xyzCoord) throws Exception;

    public abstract String getMapsUrl(XyzCoord xyzCoord) throws Exception;

    public abstract int latitude2px(double latitude, int zoom) throws Exception;

    public abstract double px2latitude(int pxy, int zoom) throws Exception;

    public int correctPx(int px, int zoom) throws Exception {
        try {
            int scale = 1 << (zoom + 8);
            for (; px < 0;) {
                px = px + scale;
            }
            for (; px >= scale;) {
                px = px - scale;
            }
            return px;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when correctPx in MapEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public XyzCoord correctXyzCoord(XyzCoord xyzCoord) throws Exception {
        try {
            int scale = 1 << xyzCoord.getZoom();
            int y = xyzCoord.getY();
            for (; y >= scale;) {
                y = y - scale;
            }
            for (; y < 0;) {
                y = y + scale;
            }
            xyzCoord.setY(y);
            int x = xyzCoord.getX();
            for (; x >= scale;) {
                x = x - scale;
            }
            for (; x < 0;) {
                x = x + scale;
            }
            xyzCoord.setX(x);
            return xyzCoord;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when correctXyzCoord in MapEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public int longitude2px(double longitude, int zoom) throws Exception {
        try {
            if (longitude > 180) {
                longitude -= 360;
            }
            longitude = 0.5 + longitude / 360;
            int px = (int) (longitude * (1 << (zoom + 8)));
            return px;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when longitude2px in MapEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public double px2longitude(int px, int zoom) throws Exception {
        try {
            int scale = 1 << (zoom + 8);
            double coef = (double) px / (double) scale;
            coef = (2 * coef) - 1;
            double longitude = coef * 180;
            return longitude;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when px2longitude in MapEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public XyzCoord toXyzCoord(LlzCoord llzCoord) throws Exception {
        try {
            int zoom = llzCoord.getZoom();
            int y = latitude2px(llzCoord.getLatitude(), zoom) >> 8;
            int x = longitude2px(llzCoord.getLongitude(), zoom) >> 8;
            XyzCoord xyzCoord = new XyzCoord();
            xyzCoord.setZoom(zoom);
            xyzCoord.setX(x);
            xyzCoord.setY(y);
            return xyzCoord;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when toXyzCoord in MapEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    protected String getParams(Hashtable params) throws Exception {
        try {
            StringBuffer buffer = new StringBuffer();
            Enumeration en = params.keys();
            if (en.hasMoreElements()) {
                String key = (String) en.nextElement();
                String value = (String) params.get(key);
                buffer.append(key).append("=").append(value);
            }
            for (; en.hasMoreElements();) {
                String key = (String) en.nextElement();
                String value = (String) params.get(key);
                buffer.append("&").append(key).append("=").append(value);
            }
            return buffer.toString();
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when getParams in MapEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

}
