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
import siarhei.luskanau.j2me.maps.MapsMIDlet;
import siarhei.luskanau.j2me.maps.form.settings.SettingsForm;

import com.sun.lwuit.Button;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.layouts.BoxLayout;

/**
 * @author <a href="mailto:siarhei.luskanau@gmail.com">Siarhei Luskanau</a>
 */
public class MainForm extends BaseForm {

    public MainForm() throws Exception {
        try {
            setLayout(new BoxLayout(BoxLayout.Y_AXIS));
            setTitle("Карты");

            Button mapButton = new Button("Карты");
            mapButton.setAlignment(CENTER);
            mapButton.addActionListener(new LogActionListener(
                    "Error when actionPerformed mapButton in MainForm.") {
                public void doAction(ActionEvent evt) throws Exception {
                    new MapsForm().show();
                }
            });
            addComponent(mapButton);

            Button locationButton = new Button("Местоположение");
            locationButton.setAlignment(CENTER);
            locationButton.addActionListener(new LogActionListener(
                    "Error when actionPerformed locationButton in MainForm.") {
                public void doAction(ActionEvent evt) throws Exception {
                    new LocationWaitDialog().show();
                }
            });
            addComponent(locationButton);

            Button settingButton = new Button("Настройки");
            settingButton.setAlignment(CENTER);
            settingButton.addActionListener(new LogActionListener(
                    "Error when actionPerformed settingButton in MainForm.") {
                public void doAction(ActionEvent evt) throws Exception {
                    new SettingsForm().show();
                }
            });
            addComponent(settingButton);

            Button aboutButton = new Button("О программе");
            aboutButton.setAlignment(CENTER);
            aboutButton.addActionListener(new LogActionListener(
                    "Error when actionPerformed aboutButton in MainForm.") {
                public void doAction(ActionEvent evt) throws Exception {
                    new AboutForm().show();
                }
            });
            addComponent(aboutButton);

            Button exitButton = new Button("Выход");
            exitButton.setAlignment(CENTER);
            exitButton.addActionListener(new LogActionListener(
                    "Error when actionPerformed exitButton in MainForm.") {
                public void doAction(ActionEvent evt) throws Exception {
                    MapsMIDlet.destroy();
                }
            });
            addComponent(exitButton);

        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when create MainForm.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

}
