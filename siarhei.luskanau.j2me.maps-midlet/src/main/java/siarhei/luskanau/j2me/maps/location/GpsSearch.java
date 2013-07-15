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

import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;

import siarhei.luskanau.j2me.core.utils.LogActionListener;
import siarhei.luskanau.j2me.maps.form.MainForm;

import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.io.util.Log;

/**
 * @author <a href="mailto:siarhei.luskanau@gmail.com">Siarhei Luskanau</a>
 */
public class GpsSearch implements DiscoveryListener {

    public GpsSearch() throws Exception {
        try {
            LocalDevice localDevice = LocalDevice.getLocalDevice();
            DiscoveryAgent discoveryAgent = localDevice.getDiscoveryAgent();

            RemoteDevice[] cachedDevices = discoveryAgent.retrieveDevices(DiscoveryAgent.CACHED);
            if (cachedDevices != null) {
                for (int i = 0; i < cachedDevices.length; i++) {
                    Log.p("cachedDevices[" + i + "].getBluetoothAddress() "
                            + cachedDevices[i].getBluetoothAddress());
                    Log.p("cachedDevices[" + i + "].getBluetoothAddress() "
                            + cachedDevices[i].getFriendlyName(false));
                }
            }

            RemoteDevice[] preknownDevices = discoveryAgent.retrieveDevices(DiscoveryAgent.PREKNOWN);
            if (preknownDevices != null) {
                for (int i = 0; i < preknownDevices.length; i++) {
                    Log.p("preknownDevices[" + i + "].getBluetoothAddress() "
                            + preknownDevices[i].getBluetoothAddress());
                    Log.p("preknownDevices[" + i + "].getBluetoothAddress() "
                            + preknownDevices[i].getFriendlyName(false));
                }
            }

            Log.p("GpsSearch.startInquiry GIAC " + discoveryAgent.startInquiry(DiscoveryAgent.GIAC, this));
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when create GpsSearch.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public void deviceDiscovered(final RemoteDevice btDevice, DeviceClass cod) {
        new LogActionListener("Error when deviceDiscovered in GpsSearch.") {
            public void doAction(ActionEvent evt) throws Exception {
                Log.p("GpsSearch.deviceDiscovered() " + btDevice.getBluetoothAddress());
                Log.p("GpsSearch.deviceDiscovered() " + btDevice.getFriendlyName(false));
            }
        }.actionPerformed(null);
    }

    public void inquiryCompleted(final int discType) {
        new LogActionListener("Error when inquiryCompleted in GpsSearch.") {
            public void doAction(ActionEvent evt) throws Exception {
                Log.p("GpsSearch.inquiryCompleted() discType=" + discType);
                new MainForm().show();
            }
        }.actionPerformed(null);
    }

    public void serviceSearchCompleted(int transID, int respCode) {
    }

    public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
    }

}
