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
import siarhei.luskanau.j2me.map.entity.LlzCoord;
import siarhei.luskanau.j2me.maps.location.BluetoothGpsWrapper;
import siarhei.luskanau.j2me.maps.location.GpsUpdateListener;
import siarhei.luskanau.j2me.maps.location.GpsWrapper;

import com.sun.lwuit.Button;
import com.sun.lwuit.Label;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.layouts.BoxLayout;

/**
 * @author <a href="mailto:siarhei.luskanau@gmail.com">Siarhei Luskanau</a>
 */
public class LocationWaitDialog extends BaseDialog implements GpsUpdateListener {

    public LocationWaitDialog() throws Exception {
        try {
            setLayout(new BoxLayout(BoxLayout.Y_AXIS));
            addComponent(new Label("update location"));
            GpsWrapper gpsWrapper = appReg.getGpsWrapper();
            if (gpsWrapper == null) {
                appReg.setGpsWrapper(new BluetoothGpsWrapper());
                appReg.getGpsWrapper().start();
            }

            Button backButton = new Button("Назад");
            backButton.setAlignment(CENTER);
            backButton.addActionListener(new LogActionListener(
                    "Error when actionPerformed backButton in LocationWaitDialog.") {
                public void doAction(ActionEvent evt) throws Exception {
                    new MainForm().show();
                }
            });
            addComponent(backButton);
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when create LocationWaitDialog.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public void onGpsUpdate(LlzCoord llzCoord) throws Exception {
        try {
            new LocationForm(llzCoord).show();
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when onGpsUpdate in LocationWaitDialog.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

}
