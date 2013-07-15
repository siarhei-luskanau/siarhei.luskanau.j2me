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

import java.util.Vector;

import siarhei.luskanau.j2me.map.dao.MemoryMapsDao;
import siarhei.luskanau.j2me.map.dao.SimpleMapsDao;
import siarhei.luskanau.j2me.map.dao.WebMapsDao;
import siarhei.luskanau.j2me.map.engine.GoogleMapEngine;
import siarhei.luskanau.j2me.map.engine.MapEngine;
import siarhei.luskanau.j2me.map.engine.OpenStreetMapEngine;
import siarhei.luskanau.j2me.map.form.SimpleMapsForm;
import siarhei.luskanau.j2me.maps.app.AppConfig;
import siarhei.luskanau.j2me.maps.app.AppReg;

import com.sun.lwuit.io.util.Log;

/**
 * @author <a href="mailto:siarhei.luskanau@gmail.com">Siarhei Luskanau</a>
 */
public class BaseAppMapsForm extends SimpleMapsForm {

    protected AppReg appReg;
    protected MapEngine mapEngine;
    protected Vector mapsDaoStack;

    public BaseAppMapsForm() throws Exception {
        try {
            Log.p("BaseAppMapsForm");
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when create BaseAppMapsForm.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    protected MapEngine getMapEngine() throws Exception {
        return mapEngine;
    }

    protected Vector getMapsDaoStack() throws Exception {
        return mapsDaoStack;
    }

    protected void setupMapEngineAndStack() throws Exception {
        try {
            AppConfig appConfig = getAppReg().getAppConfig();

            mapsDaoStack = new Vector();
            mapsDaoStack.addElement(new MemoryMapsDao());
            mapsDaoStack.addElement(new SimpleMapsDao("/copy_maps/"));

            // try {
            // JarMapsDao jarMapsDao = new JarMapsDao();
            // for (Enumeration en = jarMapsDao.getNamesMap(); en.hasMoreElements();) {
            // System.out.println("jar " + en.nextElement());
            // }
            // mapsDaoStack.addElement(jarMapsDao);
            // } catch (Throwable t) {
            // // тихо t.printStackTrace();
            // }

            // try {
            // FileMapsDao fileMapsDao = new FileMapsDao("file:///E:/copy_maps/");
            // for (Enumeration en = fileMapsDao.getNamesMap(); en.hasMoreElements();) {
            // System.out.println("file " + en.nextElement());
            // }
            // mapsDaoStack.addElement(fileMapsDao);
            // } catch (Throwable t) {
            // t.printStackTrace();
            // }

            if (appConfig.getUseWeb()) {
                mapsDaoStack.addElement(new WebMapsDao());
            }

            String mapsEngine = appConfig.getMapsEngineName();
            OpenStreetMapEngine openStreetMapEngine = new OpenStreetMapEngine();
            GoogleMapEngine googleMapEngine = new GoogleMapEngine();
            if (openStreetMapEngine.getEngineName().equals(mapsEngine)) {
                mapEngine = openStreetMapEngine;
            } else if (googleMapEngine.getEngineName().equals(mapsEngine)) {
                mapEngine = googleMapEngine;
            } else {
                throw new RuntimeException("Incorrect map engine " + mapsEngine);
            }
            try {
                mapEngine.setMapType(appConfig.getMapsType());
            } catch (Exception e) {
                appConfig.setMapsType(mapEngine.getMapTypes()[0]);
                mapEngine.setMapType(appConfig.getMapsType());
            }
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when setupMapEngineAndStack in BaseAppMapsForm.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    protected AppReg getAppReg() {
        if (appReg == null) {
            appReg = (AppReg) AppReg.instance();
        }
        return appReg;
    }

    protected void onShowCompleted() {
        super.onShowCompleted();
        appReg.setCurrentForm(this);
    }

    protected void onBack() throws Exception {
        appReg.show(appReg.MAIN_FORM);
    }

}
