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

import siarhei.luskanau.j2me.core.app.WelcomeCanvas;
import siarhei.luskanau.j2me.map.app.AppMapReg;
import siarhei.luskanau.j2me.map.entity.LlzCoord;
import siarhei.luskanau.j2me.map.taskpool.FilePool;
import siarhei.luskanau.j2me.map.taskpool.WebPool;
import siarhei.luskanau.j2me.maps.form.AboutForm;
import siarhei.luskanau.j2me.maps.form.LocationForm;
import siarhei.luskanau.j2me.maps.form.LocationWaitDialog;
import siarhei.luskanau.j2me.maps.form.MainForm;
import siarhei.luskanau.j2me.maps.form.MapsForm;
import siarhei.luskanau.j2me.maps.form.settings.SettingsForm;
import siarhei.luskanau.j2me.maps.location.GpsWrapper;

import com.sun.lwuit.Form;

/**
 * @author <a href="mailto:siarhei.luskanau@gmail.com">Siarhei Luskanau</a>
 */
public class AppReg extends AppMapReg {

    final public String MAIN_FORM = "MAIN_FORM";
    final public String MAPS_FORM = "MAPS_FORM";
    final public String LOCATION_WAIT_DIALOG = "LOCATION_WAIT_DIALOG";
    final public String LOCATION_FORM = "LOCATION_FORM";
    final public String SETTING_FORM = "SETTING_FORM";
    final public String ABOUT_FORM = "ABOUT_FORM";

    private AppConfig appConfig;
    private LlzCoord lastGpsPoint;
    private GpsWrapper gpsWrapper;

    public void init(WelcomeCanvas welcomeCanvas) throws Exception {
        try {
            welcomeCanvas.setStatusKoef(0.9).setStatus("Load settings...");
            setAppConfig(new AppConfig());
            setWebPool(new WebPool());
            setFilePool(new FilePool());

            welcomeCanvas.setStatusKoef(1.0).setStatus("Done...");
            new MainForm().show();
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when init in AppReg.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public Form getDefaultForm() throws Exception {
        try {
            return new MainForm();
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when getDefaultForm in AppReg.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public Form getFormForId(String formId) throws Exception {
        try {
            if (formId == null) {
                return null;
            } else if (MAIN_FORM.equals(formId)) {
                return new MainForm();
            } else if (MAPS_FORM.equals(formId)) {
                return new MapsForm();
            } else if (LOCATION_WAIT_DIALOG.equals(formId)) {
                return new LocationWaitDialog();
            } else if (LOCATION_FORM.equals(formId)) {
                return new LocationForm(null);
            } else if (SETTING_FORM.equals(formId)) {
                return new SettingsForm();
            } else if (ABOUT_FORM.equals(formId)) {
                return new AboutForm();
            }
            return null;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when getFormForId in AppReg.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public AppConfig getAppConfig() {
        return appConfig;
    }

    public void setAppConfig(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    public LlzCoord getLastGpsPoint() throws Exception {
        return lastGpsPoint;
    }

    public void setLastGpsPoint(LlzCoord lastGpsPoint) throws Exception {
        this.lastGpsPoint = lastGpsPoint;
    }

    public GpsWrapper getGpsWrapper() throws Exception {
        return gpsWrapper;
    }

    public void setGpsWrapper(GpsWrapper gpsWrapper) throws Exception {
        this.gpsWrapper = gpsWrapper;
    }

}
