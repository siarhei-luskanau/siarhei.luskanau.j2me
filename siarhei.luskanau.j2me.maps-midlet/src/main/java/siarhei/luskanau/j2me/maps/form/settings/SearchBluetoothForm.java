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

package siarhei.luskanau.j2me.maps.form.settings;

import java.util.Vector;

import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;

import siarhei.luskanau.j2me.core.utils.LogActionListener;
import siarhei.luskanau.j2me.maps.form.BaseForm;
import siarhei.luskanau.j2me.maps.form.GpsSettingForm;

import com.sun.lwuit.Button;
import com.sun.lwuit.Container;
import com.sun.lwuit.Label;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.io.util.Log;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.plaf.Style;

/**
 * @author <a href="mailto:siarhei.luskanau@gmail.com">Siarhei Luskanau</a>
 */
public class SearchBluetoothForm extends BaseForm implements DiscoveryListener {

    private Container bluetoothContainer;
    private Button cancelButton;
    private DiscoveryAgent discoveryAgent;
    private DiscoveryListener discoveryListener;
    private Vector vector = new Vector();

    public SearchBluetoothForm() throws Exception {
        try {
            discoveryListener = this;
            setLayout(new BoxLayout(BoxLayout.Y_AXIS));

            bluetoothContainer = new Container();
            Style style = bluetoothContainer.getStyle();
            bluetoothContainer.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
            style.setPadding(0, 0, 0, 0);
            style.setMargin(0, 0, 0, 0);
            bluetoothContainer.setUnselectedStyle(style);
            addComponent(bluetoothContainer);

            LocalDevice localDevice = LocalDevice.getLocalDevice();
            discoveryAgent = localDevice.getDiscoveryAgent();
            discoveryAgent.startInquiry(DiscoveryAgent.GIAC, this);

            cancelButton = new Button("Отмена");
            cancelButton.setAlignment(CENTER);
            cancelButton.addActionListener(new LogActionListener(
                    "Error when actionPerformed cancelButton in GpsSettingForm.") {
                public void doAction(ActionEvent evt) throws Exception {
                    discoveryAgent.cancelInquiry(discoveryListener);
                    removeComponent(cancelButton);
                }
            });
            addComponent(cancelButton);

            Button backButton = new Button("Назад");
            backButton.setAlignment(CENTER);
            backButton.addActionListener(new LogActionListener(
                    "Error when actionPerformed backButton in SearchBluetoothForm.") {
                public void doAction(ActionEvent evt) throws Exception {
                    new SettingsForm().show();
                }
            });
            addComponent(backButton);

        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when create SearchBluetoothForm.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public void deviceDiscovered(final RemoteDevice remoteDevice, DeviceClass deviceClass) {
        new LogActionListener("Error when deviceDiscovered in SearchBluetoothForm.") {
            public void doAction(ActionEvent evt) throws Exception {
                String name = remoteDevice.getFriendlyName(false);
                String address = remoteDevice.getBluetoothAddress();
                Button button = new Button(name + " " + address);
                button.setAlignment(CENTER);
                Log.p("deviceDiscovered: " + name + " " + address);
                button.addActionListener(new RemoteDeviceActionListener(address, name));
                bluetoothContainer.addComponent(button);
                vector.addElement(remoteDevice);
                repaint();
            }
        }.actionPerformed(null);
    }

    public void inquiryCompleted(final int discType) {
        new LogActionListener("Error when inquiryCompleted in SearchBluetoothForm.") {
            public void doAction(ActionEvent evt) throws Exception {
                removeComponent(cancelButton);
                repaint();
                Log.p("SearchBluetoothForm.inquiryCompleted() discType=" + discType);
                for (int i = 0; i < vector.size(); i++) {
                    try {
                        RemoteDevice remoteDevice = (RemoteDevice) vector.elementAt(i);
                        Log.p("searchServices: " + remoteDevice.getBluetoothAddress() + " "
                                + remoteDevice.getFriendlyName(false));
                        UUID[] uuidSet = new UUID[] { new UUID(0x1101) };
                        discoveryAgent.searchServices(null, uuidSet, remoteDevice, discoveryListener);
                    } catch (Exception e) {
                        Log.p("inquiryCompleted: " + e.getMessage(), Log.ERROR);
                        addComponent(new Label("inquiryCompleted: " + e.getMessage()));
                    }
                }
                repaint();
            }
        }.actionPerformed(null);
    }

    public void serviceSearchCompleted(int transID, int respCode) {
        addComponent(new Button("serviceSearchCompleted"));
        repaint();
    }

    public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
        try {
            Log.p("transID=" + transID);
            for (int i = 0; i < servRecord.length; i++) {
                ServiceRecord serviceRecord = servRecord[i];
                RemoteDevice remoteDevice = serviceRecord.getHostDevice();
                String name = remoteDevice.getFriendlyName(false);
                String address = remoteDevice.getBluetoothAddress();

                Log.p("services: " + name + " " + address);
                addComponent(new Button("services: " + name + " " + address));

                String string = "URL: "
                        + serviceRecord.getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
                if (string != null) {
                    Log.p(string);
                    addComponent(new Label(string));
                }

                if (string != null) {
                    Log.p(serviceRecord.getConnectionURL(ServiceRecord.AUTHENTICATE_NOENCRYPT, false));
                    addComponent(new Label(string));
                }

                if (string != null) {
                    Log.p(serviceRecord.getConnectionURL(ServiceRecord.AUTHENTICATE_ENCRYPT, false));
                    addComponent(new Label(string));
                }
            }
        } catch (Exception e) {
            Log.p("servicesDiscovered: " + e.getMessage(), Log.ERROR);
            addComponent(new Label("servicesDiscovered: " + e.getMessage()));
        }
        repaint();
    }

    private class RemoteDeviceActionListener extends LogActionListener {

        private String address;
        private String name;

        public RemoteDeviceActionListener(String address, String name) {
            super("Error when doAction in RemoteDeviceActionListener in GpsSettingForm.");
            this.address = address;
            this.name = name;
        }

        protected void doAction(ActionEvent evt) throws Exception {
            appReg.getAppConfig().setGpsBluetoothAddress(address);
            appReg.getAppConfig().setGpsBluetoothName(name);
            new GpsSettingForm().show();
        }

    }

}
