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

import siarhei.luskanau.j2me.map.entity.LlzCoord;
import siarhei.luskanau.j2me.maps.app.AppReg;

import com.sun.lwuit.Form;

/**
 * @author <a href="mailto:siarhei.luskanau@gmail.com">Siarhei Luskanau</a>
 */
public abstract class GpsWrapper {

    private AppReg appReg;

    public GpsWrapper() throws Exception {
        try {
            appReg = (AppReg) AppReg.instance();
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when create GpsWrapper.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public abstract void start() throws Exception;

    public abstract void stop() throws Exception;

    protected void locationUpdated(final LlzCoord llzCoord) throws Exception {
        try {
            appReg.setLastGpsPoint(llzCoord);
            Form form = appReg.getCurrentForm();
            if (form instanceof GpsUpdateListener) {
                ((GpsUpdateListener) form).onGpsUpdate(llzCoord);
            }
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when locationUpdated in GpsWrapper.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

}
