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

package siarhei.luskanau.j2me.maps.location;

import java.io.InputStreamReader;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

import siarhei.luskanau.j2me.core.utils.StringTokenizer;
import siarhei.luskanau.j2me.map.entity.LlzCoord;
import siarhei.luskanau.j2me.maps.app.AppConfig;
import siarhei.luskanau.j2me.maps.app.AppReg;

import com.sun.lwuit.io.util.Log;

/**
 * @author <a href="mailto:siarhei.luskanau@gmail.com">Siarhei Luskanau</a>
 */
public class BluetoothGpsWrapper extends GpsWrapper {

    private boolean isActive;

    public BluetoothGpsWrapper() throws Exception {
    }

    public void start() throws Exception {
        new Thread(new Runnable() {
            public void run() {
                isActive = true;
                int errorsCnt = 0;
                for (; isActive;) {
                    StreamConnection connection = null;
                    InputStreamReader reader = null;
                    try {
                        AppConfig appConfig = ((AppReg) AppReg.instance()).getAppConfig();
                        String url = "btspp://" + appConfig.getGpsBluetoothAddress() + ":1";
                        connection = (StreamConnection) Connector.open(url, Connector.READ);
                        reader = new InputStreamReader(connection.openInputStream());

                        StringBuffer buffer = new StringBuffer();
                        int read;
                        for (; isActive && (read = reader.read()) > 0;) {
                            if (read != 13) {
                                if ('$' == (char) read) {
                                    buffer = new StringBuffer();
                                }
                                buffer.append((char) read);
                                if (buffer.length() > 8192) {
                                    buffer = new StringBuffer();
                                }
                            } else {
                                String string = buffer.toString().trim();
                                buffer = new StringBuffer();
                                if (string.startsWith("$GPGGA")) {
                                    // Log.p("BluetoothGpsWrapper " + string);
                                    StringTokenizer tokenizer = new StringTokenizer(string, ",");
                                    // $GPGGA - Global Positioning System Fix Data
                                    tokenizer.nextElement();
                                    // utc - Fix taken
                                    tokenizer.nextElement();

                                    // latitude
                                    String latitudeString = (String) tokenizer.nextElement();
                                    double latitude = Double.parseDouble(latitudeString.substring(0, 2));
                                    latitude = latitude + Double.parseDouble(latitudeString.substring(2))
                                            / 60.0;
                                    // northHemi
                                    tokenizer.nextElement();

                                    String longitudeString = (String) tokenizer.nextElement(); // longitude
                                    double longitude = Double.parseDouble(longitudeString.substring(0, 3));
                                    longitude = longitude + Double.parseDouble(longitudeString.substring(3))
                                            / 60.0;

                                    // eastHemi quality - Fix quality: 0 = invalid, 1 = GPS fix (SPS), 2 =
                                    // DGPS fix, 3 = PPS fix, 4 = Real Time Kinematic, 5 = Float RTK, 6 =
                                    // estimated (dead reckoning) (2.3 feature), 7 = Manual input mode, 8 =
                                    // Simulation mode
                                    tokenizer.nextElement();

                                    int quality = Integer.parseInt((String) tokenizer.nextElement());
                                    if (quality <= 0) {
                                        continue;
                                    }
                                    // tokenizer.nextElement(); // nSat - Number of satellites being tracked
                                    // tokenizer.nextElement(); // horDilution - Horizontal dilution of
                                    // position
                                    // tokenizer.nextElement(); // altitude - Altitude, Meters, above mean sea
                                    // level
                                    // tokenizer.nextElement(); // altitudeUnit - Altitude, Meters, above mean
                                    // sea level
                                    // tokenizer.nextElement(); // geoidalHeight - Height of geoid (mean sea
                                    // level) above WGS84 ellipsoid
                                    // tokenizer.nextElement(); // geoidalHeightUnit - Height of geoid (mean
                                    // sea level) above WGS84 ellipsoid
                                    // tokenizer.nextElement(); // diffCorrection - time in seconds since last
                                    // DGPS update
                                    // tokenizer.nextElement(); // diffStationId - DGPS station ID number
                                    // tokenizer.nextElement(); // checksum - //the checksum data, always
                                    // begins with *
                                    LlzCoord llzCoord = new LlzCoord(latitude, longitude, -1);
                                    locationUpdated(llzCoord);
                                }
                            }
                        }

                    } catch (Throwable t) {
                        errorsCnt++;
                        StringBuffer message = new StringBuffer();
                        message.append("Error ").append(errorsCnt)
                                .append(" when read dataInputStream in BluetoothGpsWrapper.");
                        message.append("\n\t").append(t.toString());
                        Log.p(message.toString(), Log.ERROR);
                        if (errorsCnt >= 5) {
                            isActive = false;
                        }
                        try {
                            if (reader != null) {
                                reader.close();
                            }
                        } catch (Throwable e) {
                        }
                        try {
                            if (connection != null) {
                                connection.close();
                            }
                        } catch (Throwable e) {
                        }
                    }
                }
            }
        }).start();
    }

    public void stop() throws Exception {
        isActive = false;
    }

}
