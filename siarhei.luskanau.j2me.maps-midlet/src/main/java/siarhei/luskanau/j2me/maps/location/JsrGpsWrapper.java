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

import javax.microedition.location.Location;
import javax.microedition.location.LocationListener;
import javax.microedition.location.LocationProvider;
import javax.microedition.location.QualifiedCoordinates;

import siarhei.luskanau.j2me.core.utils.LogActionListener;
import siarhei.luskanau.j2me.map.entity.LlzCoord;

import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.io.util.Log;

/**
 * @author <a href="mailto:siarhei.luskanau@gmail.com">Siarhei Luskanau</a>
 */
public class JsrGpsWrapper extends GpsWrapper implements LocationListener {

    private LocationProvider locationProvider;

    public JsrGpsWrapper() throws Exception {
    }

    public void start() throws Exception {
        try {
            if (locationProvider == null) {
                locationProvider = LocationProvider.getInstance(null);
                locationProvider.setLocationListener(this, 1, -1, -1);
            }
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when start in JsrGpsWrapper.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public void stop() throws Exception {
        try {
            if (locationProvider != null) {
                locationProvider.reset();
            }
            locationProvider = null;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when stop in JsrGpsWrapper.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public void locationUpdated(final LocationProvider locationProvider, final Location location) {
        new LogActionListener("Error when locationUpdated in JsrGpsWrapper.") {
            public void doAction(ActionEvent evt) throws Exception {
                if (location != null && location.isValid()) {
                    QualifiedCoordinates qc = location.getQualifiedCoordinates();
                    LlzCoord llzCoord = new LlzCoord(qc.getLatitude(), qc.getLongitude(), -1);
                    locationUpdated(llzCoord);
                }
            }
        }.actionPerformed(null);
    }

    public void providerStateChanged(LocationProvider locationProvider, int newState) {
        Log.p("JsrGpsWrapper.providerStateChanged() newState=" + newState);
    }

}
