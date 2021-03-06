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
import siarhei.luskanau.j2me.map.engine.GoogleMapEngine;
import siarhei.luskanau.j2me.map.engine.MapEngine;
import siarhei.luskanau.j2me.map.engine.OpenStreetMapEngine;
import siarhei.luskanau.j2me.maps.form.BaseForm;

import com.sun.lwuit.List;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.layouts.BorderLayout;

/**
 * @author <a href="mailto:siarhei.luskanau@gmail.com">Siarhei Luskanau</a>
 */
public class SettingsMapsEngineForm extends BaseForm {

    public SettingsMapsEngineForm() throws Exception {
        try {
            setLayout(new BorderLayout());
            setScrollable(false);
            setTitle("Настройки источника карт");

            OpenStreetMapEngine openStreetMapEngine = new OpenStreetMapEngine();
            GoogleMapEngine googleMapEngine = new GoogleMapEngine();
            String[] types = { openStreetMapEngine.getEngineName(), googleMapEngine.getEngineName() };

            List list = new List(types);
            list.setSmoothScrolling(false);
            list.setOrientation(List.VERTICAL);
            list.setFixedSelection(List.FIXED_NONE_CYCLIC);
            list.addActionListener(new LogActionListener(
                    "Error when actionPerformed list in SettingsMapsEngineForm.") {
                public void doAction(ActionEvent evt) throws Exception {
                    List list = (List) evt.getSource();
                    appReg.getAppConfig().setMapsEngineName((String) list.getSelectedItem());
                    MapEngine mapEngine = appReg.getAppConfig().getMapsEngine();
                    appReg.getAppConfig().setMapsType(mapEngine.getMapTypes()[0]);
                    new SettingsMapsForm().show();
                }
            });

            String name = appReg.getAppConfig().getMapsEngineName();
            for (int i = 0; i < types.length; i++) {
                if (types[i].equals(name)) {
                    list.setSelectedIndex(i);
                }
            }
            addComponent(BorderLayout.CENTER, list);
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when create SettingsMapsEngineForm.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

}
