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

package siarhei.luskanau.j2me.maps.form;

import siarhei.luskanau.j2me.core.utils.LogActionListener;
import siarhei.luskanau.j2me.maps.form.settings.SearchBluetoothForm;
import siarhei.luskanau.j2me.maps.form.settings.SettingsForm;
import siarhei.luskanau.j2me.maps.location.Gps;

import com.sun.lwuit.Button;
import com.sun.lwuit.ButtonGroup;
import com.sun.lwuit.CheckBox;
import com.sun.lwuit.Container;
import com.sun.lwuit.Label;
import com.sun.lwuit.RadioButton;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.plaf.Style;

/**
 * @author <a href="mailto:siarhei.luskanau@gmail.com">Siarhei Luskanau</a>
 */
public class GpsSettingForm extends BaseForm {

    private Gps gps;
    private CheckBox enableGps;
    private Container bluetoothContainer;

    public GpsSettingForm() throws Exception {
        try {
            setLayout(new BoxLayout(BoxLayout.Y_AXIS));
            setTitle("Настройки GPS");

            gps = new Gps();

            // блок выбора gps
            if (gps.getSSupportedGps()) {
                addEnableGpsCheckBox();
            } else {
                Label absentGps = new Label("Отсутствует GPS!");
                addComponent(absentGps);
            }

            if (gps.getSSupportedGps()) {
                String[] supportedGpsTypes = gps.getSupportedGpsTypes();
                if (supportedGpsTypes.length > 1) {
                    addGpsTypes(supportedGpsTypes);
                } else if (gps.JSR_TYPE.equals(supportedGpsTypes[0])) {
                    Label label = new Label(gps.JSR_TYPE);
                    addComponent(label);
                } else if (gps.BLUETOOTH_TYPE.equals(supportedGpsTypes[0])) {
                    Label label = new Label(gps.BLUETOOTH_TYPE);
                    addComponent(label);
                }
            }

            bluetoothContainer = new Container();
            Style style = bluetoothContainer.getStyle();
            bluetoothContainer.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
            style.setPadding(0, 0, 0, 0);
            style.setMargin(0, 0, 0, 0);
            bluetoothContainer.setUnselectedStyle(style);
            addComponent(bluetoothContainer);

            fillBluetoothContainer();

            Button backButton = new Button("Назад");
            backButton.setAlignment(CENTER);
            backButton.addActionListener(new LogActionListener(
                    "Error when actionPerformed backButton in GpsSettingForm.") {
                public void doAction(ActionEvent evt) throws Exception {
                    new SettingsForm().show();
                }
            });
            addComponent(backButton);

        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when create GpsSettingForm.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    private void addEnableGpsCheckBox() throws Exception {
        try {
            enableGps = new CheckBox();
            enableGps.setSelected(appReg.getAppConfig().getGpsEnable());
            enableGps.addActionListener(new LogActionListener(
                    "Error when actionPerformed CheckBox in GpsSettingForm.") {
                protected void doAction(ActionEvent evt) throws Exception {
                    appReg.getAppConfig().setGpsEnable(enableGps.isSelected());
                }
            });
            enableGps.setText("Use GPS");
            addComponent(enableGps);
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when addEnableGpsCheckBox in GpsSettingForm.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    private void addGpsTypes(String[] supportedGpsTypes) throws Exception {
        try {
            String gpsType = appReg.getAppConfig().getGpsType();
            RadioButton rb1 = new RadioButton(supportedGpsTypes[0]);
            if (supportedGpsTypes[0].equals(gpsType)) {
                rb1.setSelected(true);
            }
            RadioButton rb2 = new RadioButton(supportedGpsTypes[1]);
            if (supportedGpsTypes[1].equals(gpsType)) {
                rb2.setSelected(true);
            }
            ButtonGroup group1 = new ButtonGroup();
            group1.add(rb1);
            addComponent(rb1);
            group1.add(rb2);
            addComponent(rb2);
            ActionListener actionListener = new LogActionListener("") {
                protected void doAction(ActionEvent evt) throws Exception {
                    RadioButton radioButton = (RadioButton) evt.getSource();
                    appReg.getAppConfig().setGpsType(radioButton.getText());
                    fillBluetoothContainer();
                }
            };
            rb1.addActionListener(actionListener);
            rb2.addActionListener(actionListener);
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when addGpsTypes in GpsSettingForm.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    private void fillBluetoothContainer() throws Exception {
        try {
            bluetoothContainer.removeAll();
            if (gps.BLUETOOTH_TYPE.equals(appReg.getAppConfig().getGpsType())) {
                Label addressLabel = new Label(appReg.getAppConfig().getGpsBluetoothAddress());
                bluetoothContainer.addComponent(addressLabel);
                Label nameLabel = new Label(appReg.getAppConfig().getGpsBluetoothName());
                bluetoothContainer.addComponent(nameLabel);
                Button searchButton = new Button("Search");
                searchButton.setAlignment(CENTER);
                searchButton.addActionListener(new LogActionListener(
                        "Error when actionPerformed searchButton in GpsSettingForm.") {
                    public void doAction(ActionEvent evt) throws Exception {
                        new SearchBluetoothForm().show();
                    }
                });
                bluetoothContainer.addComponent(searchButton);
            }
            revalidate();
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when fillBluetoothContainer in GpsSettingForm.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

}
