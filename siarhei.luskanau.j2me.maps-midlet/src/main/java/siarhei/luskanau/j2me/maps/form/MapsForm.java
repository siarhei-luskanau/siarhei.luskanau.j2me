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
import siarhei.luskanau.j2me.map.task.MapUpdate;

import com.sun.lwuit.io.util.Log;

/**
 * @author <a href="mailto:siarhei.luskanau@gmail.com">Siarhei Luskanau</a>
 */
public class MapsForm extends BaseAppMapsForm implements MapUpdate {

    public MapsForm() throws Exception {
        try {
            Log.p("MapsForm");
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when create MapsForm.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    protected boolean hasNavigateButton() {
        return true;
    }

    protected LlzCoord getLlzCoord() throws Exception {
        try {
            return appReg.getAppConfig().getLlzCoord();
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when getLlzCoord in MapsForm.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    protected void setLlzCoord(LlzCoord llzCoord) throws Exception {
        try {
            appReg.getAppConfig().setLlzCoord(llzCoord);
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when setLlzCoord in MapsForm.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

}
