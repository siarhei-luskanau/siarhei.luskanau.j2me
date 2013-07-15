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

package siarhei.luskanau.j2me.maps.app;

import siarhei.luskanau.j2me.core.app.BaseConfig;
import siarhei.luskanau.j2me.core.utils.StringTokenizer;
import siarhei.luskanau.j2me.map.engine.GoogleMapEngine;
import siarhei.luskanau.j2me.map.engine.MapEngine;
import siarhei.luskanau.j2me.map.engine.OpenStreetMapEngine;
import siarhei.luskanau.j2me.map.entity.LlzCoord;

/**
 * Class for save settings
 * 
 * @author <a href="mailto:siarhei.luskanau@gmail.com">Siarhei Luskanau</a>
 */
public class AppConfig extends BaseConfig {

    final private String BASE_FILE_URL = "BASE_FILE_URL";
    final private String LLZ_COORD = "LLZ_COORD";
    final private String GPS_ENABLE = "GPS_ENABLE";
    final private String GPS_TYPE = "GPS_TYPE";
    final private String GPS_BLUETOOTH_ADDRESS = "GPS_BLUETOOTH_ADDRESS";
    final private String GPS_BLUETOOTH_NAME = "GPS_BLUETOOTH_NAME";
    final private String LOCATION_FORM_ZOOM = "LOCATION_FORM_ZOOM";
    final private String USE_WEB = "USE_WEB";
    final private String MAPS_ENGINE = "MAPS_ENGINE";
    final private String MAPS_TYPE = "MAPS_TYPE";

    public AppConfig() throws Exception {
        super("MapsConfig");
    }

    public String getFileBaseUrl() throws Exception {
        try {
            String url = configDao.get(BASE_FILE_URL);
            if (url == null) {
                url = "";
                setBaseFileUrl(url);
            }
            return url;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when getFileBaseUrl in AppConfig.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public void setBaseFileUrl(String baseFileUrl) throws Exception {
        try {
            configDao.set(BASE_FILE_URL, baseFileUrl);
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when setBaseFileUrl in AppConfig.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public LlzCoord getLlzCoord() throws Exception {
        try {
            LlzCoord llzCoord = new LlzCoord();
            String value = configDao.get(LLZ_COORD);
            if (value != null) {
                StringTokenizer tokenizer = new StringTokenizer(value, " ");
                llzCoord.setLatitude(Double.parseDouble((String) tokenizer.nextElement()));
                llzCoord.setLongitude(Double.parseDouble((String) tokenizer.nextElement()));
                llzCoord.setZoom(Integer.parseInt((String) tokenizer.nextElement()));
            } else {
                llzCoord.setLatitude(52.444847894367115);
                llzCoord.setLongitude(31.0198974609375);
                llzCoord.setZoom(0);
                setLlzCoord(llzCoord);
            }
            return llzCoord;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when getLlzCoord in AppConfig.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public void setLlzCoord(LlzCoord llzCoord) throws Exception {
        try {
            String value = new StringBuffer().append(llzCoord.getLatitude()).append(" ")
                    .append(llzCoord.getLongitude()).append(" ").append(llzCoord.getZoom()).toString();
            configDao.set(LLZ_COORD, value);
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when setLlzCoord in AppConfig.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public boolean getGpsEnable() throws Exception {
        try {
            boolean enable = false;
            String enableString = configDao.get(GPS_ENABLE);
            if (enableString == null) {
                setGpsEnable(enable);
            } else {
                enable = String.valueOf(true).equals(enableString);
            }
            return enable;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when getGpsEnable in AppConfig.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public void setGpsEnable(boolean enable) throws Exception {
        try {
            configDao.set(GPS_ENABLE, String.valueOf(enable));
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when setGpsEnable in AppConfig.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public String getGpsType() throws Exception {
        try {
            return configDao.get(GPS_TYPE);
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when getGpsType in AppConfig.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public void setGpsType(String gpsType) throws Exception {
        try {
            configDao.set(GPS_TYPE, gpsType);
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when setGpsType in AppConfig.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public String getGpsBluetoothAddress() throws Exception {
        try {
            String address = configDao.get(GPS_BLUETOOTH_ADDRESS);
            if (address == null) {
                address = "001C88309685";
                setGpsBluetoothAddress(address);
            }
            return address;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when getGpsBluetoothAddress in AppConfig.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public void setGpsBluetoothAddress(String address) throws Exception {
        try {
            configDao.set(GPS_BLUETOOTH_ADDRESS, address);
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when setGpsBluetoothAddress in AppConfig.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public String getGpsBluetoothName() throws Exception {
        try {
            String name = configDao.get(GPS_BLUETOOTH_NAME);
            if (name == null) {
                name = "iBT A+ GPS";
                setGpsBluetoothName(name);
            }
            return name;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when getGpsBluetoothName in AppConfig.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public void setGpsBluetoothName(String name) throws Exception {
        try {
            configDao.set(GPS_BLUETOOTH_NAME, name);
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when setGpsBluetoothName in AppConfig.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public int getLocationFormZoom() throws Exception {
        try {
            int zoom = 11;
            String zoomString = configDao.get(LOCATION_FORM_ZOOM);
            if (zoomString == null) {
                setLocationFormZoom(zoom);
            } else {
                zoom = Integer.parseInt(zoomString);
            }
            return zoom;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when getLocationFormZoom in AppConfig.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public void setLocationFormZoom(int zoom) throws Exception {
        try {
            configDao.set(LOCATION_FORM_ZOOM, String.valueOf(zoom));
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when setLocationFormZoom in AppConfig.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public MapEngine getMapsEngine() throws Exception {
        try {
            MapEngine mapEngine = null;
            String mapsEngineName = getMapsEngineName();

            OpenStreetMapEngine openStreetMapEngine = new OpenStreetMapEngine();
            GoogleMapEngine googleMapEngine = new GoogleMapEngine();
            if (openStreetMapEngine.getEngineName().equals(mapsEngineName)) {
                mapEngine = openStreetMapEngine;
            } else if (googleMapEngine.getEngineName().equals(mapsEngineName)) {
                mapEngine = googleMapEngine;
            } else {
                mapEngine = openStreetMapEngine;
                setMapsEngineName(mapEngine.getEngineName());
            }
            return mapEngine;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when getMapsEngine in AppConfig.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public boolean getUseWeb() throws Exception {
        try {
            boolean use = true;
            String useString = configDao.get(USE_WEB);
            if (useString != null) {
                if ("false".equals(useString)) {
                    use = false;
                } else {
                    use = true;
                }
            } else {
                setUseWeb(use);
            }
            return use;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when getUseWeb in AppConfig.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public void setUseWeb(boolean use) throws Exception {
        try {
            configDao.set(USE_WEB, (use ? "true" : "false"));
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when setUseWeb in AppConfig.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public String getMapsEngineName() throws Exception {
        try {
            String mapsEngine = configDao.get(MAPS_ENGINE);
            if (mapsEngine == null) {
                mapsEngine = new OpenStreetMapEngine().getEngineName();
                setMapsEngineName(mapsEngine);
            }
            return mapsEngine;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when getMapsEngineName in AppConfig.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public void setMapsEngineName(String mapsEngine) throws Exception {
        try {
            configDao.set(MAPS_ENGINE, mapsEngine);
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when getMapsEngineName in AppConfig.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public String getMapsType() throws Exception {
        try {
            String mapsType = configDao.get(MAPS_TYPE);
            if (mapsType == null) {
                String mapsEngine = getMapsEngineName();
                OpenStreetMapEngine openStreetMapEngine = new OpenStreetMapEngine();
                GoogleMapEngine googleMapEngine = new GoogleMapEngine();
                if (openStreetMapEngine.getEngineName().equals(mapsEngine)) {
                    mapsType = openStreetMapEngine.getMapTypes()[0];
                } else if (googleMapEngine.getEngineName().equals(mapsEngine)) {
                    mapsType = googleMapEngine.getMapTypes()[0];
                } else {
                    throw new RuntimeException("Incorrect map engine " + mapsEngine);
                }
                setMapsType(mapsType);
            }
            return mapsType;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when getMapsType in AppConfig.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public void setMapsType(String mapsType) throws Exception {
        try {
            configDao.set(MAPS_TYPE, mapsType);
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when setMapsType in AppConfig.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

}
