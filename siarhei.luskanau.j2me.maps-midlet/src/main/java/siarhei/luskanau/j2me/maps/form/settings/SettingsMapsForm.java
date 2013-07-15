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

import siarhei.luskanau.j2me.core.utils.LogActionListener;
import siarhei.luskanau.j2me.maps.form.BaseForm;
import siarhei.luskanau.j2me.maps.form.MainForm;

import com.sun.lwuit.Button;
import com.sun.lwuit.CheckBox;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.layouts.BoxLayout;

/**
 * @author <a href="mailto:siarhei.luskanau@gmail.com">Siarhei Luskanau</a>
 */
public class SettingsMapsForm extends BaseForm {

    public SettingsMapsForm() throws Exception {
        try {
            setLayout(new BoxLayout(BoxLayout.Y_AXIS));
            setTitle("Настройки карт");

            CheckBox checkBox = new CheckBox("Загружать из Интернета");
            checkBox.setSelected(appReg.getAppConfig().getUseWeb());
            checkBox.addActionListener(new LogActionListener(
                    "Error when actionPerformed backButton in SettingsMapsForm.") {
                public void doAction(ActionEvent evt) throws Exception {
                    CheckBox checkBox = (CheckBox) evt.getSource();
                    appReg.getAppConfig().setUseWeb(checkBox.isSelected());
                }
            });
            addComponent(checkBox);

            Button engineButton = new Button("Источник: " + appReg.getAppConfig().getMapsEngineName());
            engineButton.addActionListener(new LogActionListener(
                    "Error when actionPerformed engineButton in SettingsMapsForm.") {
                public void doAction(ActionEvent evt) throws Exception {
                    new SettingsMapsEngineForm().show();
                }
            });
            addComponent(engineButton);

            Button typeButton = new Button("Тип: " + appReg.getAppConfig().getMapsType());
            typeButton.addActionListener(new LogActionListener(
                    "Error when typeButton sourceButton in SettingsMapsForm.") {
                public void doAction(ActionEvent evt) throws Exception {
                    new SettingsMapsTypeForm().show();
                }
            });
            addComponent(typeButton);

            Button backButton = new Button("Назад");
            backButton.setAlignment(CENTER);
            backButton.addActionListener(new LogActionListener(
                    "Error when actionPerformed backButton in SettingsMapsForm.") {
                public void doAction(ActionEvent evt) throws Exception {
                    new MainForm().show();
                }
            });
            addComponent(backButton);

        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when create SettingsMapsForm.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

}
