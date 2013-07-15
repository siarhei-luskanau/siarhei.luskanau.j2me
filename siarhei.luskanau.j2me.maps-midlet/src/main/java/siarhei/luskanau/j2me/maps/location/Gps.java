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

/**
 * @author <a href="mailto:siarhei.luskanau@gmail.com">Siarhei Luskanau</a>
 */
public class Gps {

    public final String JSR_TYPE = "JSR_TYPE";

    public final String BLUETOOTH_TYPE = "BLUETOOTH_TYPE";

    private boolean supportedGps;

    private boolean supportedJsrGps;

    private boolean supportedBluetoothGps;

    private String[] supportedGpsTypes;

    public Gps() throws Exception {
        try {
            // supportedJsrGps = System.getProperty("microedition.location.version") != null;
            supportedJsrGps = true;
            // supportedJsrGps = false;
            // supportedBluetoothGps = System.getProperty("bluetooth.api.version") != null;
            supportedBluetoothGps = true;
            // supportedBluetoothGps = false;
            supportedGps = supportedJsrGps && supportedBluetoothGps;
            if (supportedJsrGps || supportedBluetoothGps) {
                supportedGpsTypes = new String[] { JSR_TYPE, BLUETOOTH_TYPE };
            } else if (supportedJsrGps) {
                supportedGpsTypes = new String[] { JSR_TYPE };
            } else if (supportedBluetoothGps) {
                supportedGpsTypes = new String[] { BLUETOOTH_TYPE };
            }
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when create Gps.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public String[] getSupportedGpsTypes() {
        return supportedGpsTypes;
    }

    public boolean getSSupportedGps() {
        return supportedGps;
    }

    public boolean getSSupportedJsrGps() {
        return supportedJsrGps;
    }

    public boolean getSSupportedBluetoothGps() {
        return supportedBluetoothGps;
    }

    public GpsWrapper createGpsWrapper(String gpsType) throws Exception {
        try {
            GpsWrapper gpsWrapper = null;
            if (JSR_TYPE.endsWith(gpsType)) {
                gpsWrapper = (GpsWrapper) Class.forName("siarhei.luskanau.location.JsrGpsWrapper")
                        .newInstance();
            } else if (BLUETOOTH_TYPE.endsWith(gpsType)) {
                gpsWrapper = (GpsWrapper) Class.forName("siarhei.luskanau.location.BluetoothGpsWrapper")
                        .newInstance();
            }
            return gpsWrapper;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when createGpsWrapper in Gps.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

}
