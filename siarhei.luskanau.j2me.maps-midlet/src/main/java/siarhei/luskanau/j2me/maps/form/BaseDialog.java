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

import siarhei.luskanau.j2me.maps.app.AppReg;

import com.sun.lwuit.Dialog;
import com.sun.lwuit.io.util.Log;

/**
 * @author <a href="mailto:siarhei.luskanau@gmail.com">Siarhei Luskanau</a>
 */
public abstract class BaseDialog extends Dialog {

    protected AppReg appReg;

    public BaseDialog() throws Exception {
        try {
            appReg = (AppReg) AppReg.instance();
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when create BaseDialog.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    protected void onShow() {
        try {
            appReg.setCurrentForm(this);
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when onShow in BaseDialog.");
            message.append("\n\t").append(t.toString());
            Log.p(message.toString(), Log.ERROR);
        }
    }

}
