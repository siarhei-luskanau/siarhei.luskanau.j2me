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

import siarhei.luskanau.j2me.map.entity.LlzCoord;
import siarhei.luskanau.j2me.maps.location.GpsUpdateListener;

import com.sun.lwuit.Container;

/**
 * @author <a href="mailto:siarhei.luskanau@gmail.com">Siarhei Luskanau</a>
 */
public class LocationForm extends BaseAppMapsForm implements GpsUpdateListener {

    protected LlzCoord llzCoord;
    protected Container pointContainer;

    public LocationForm(LlzCoord llzCoord) throws Exception {
        try {

            llzCoord.setZoom(appReg.getAppConfig().getLocationFormZoom());
            this.llzCoord = llzCoord;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when onGpsUpdate in LocationForm.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    protected LlzCoord getLlzCoord() throws Exception {
        try {
            return llzCoord;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when getLlzCoord in LocationForm.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public void onGpsUpdate(LlzCoord llzCoord) throws Exception {
        try {
            llzCoord.setZoom(getZoom());
            this.llzCoord = llzCoord;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when onGpsUpdate in LocationForm.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

}
