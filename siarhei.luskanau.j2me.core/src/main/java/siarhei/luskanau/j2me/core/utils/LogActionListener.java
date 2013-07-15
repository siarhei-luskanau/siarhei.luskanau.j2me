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

package siarhei.luskanau.j2me.core.utils;

import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.io.util.Log;

/**
 * @author <a href="mailto:siarhei.luskanau@gmail.com">Siarhei Luskanau</a>
 */
public abstract class LogActionListener implements ActionListener {

    private String errorMessage;

    public LogActionListener(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void actionPerformed(ActionEvent evt) {
        try {
            doAction(evt);
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append(errorMessage);
            message.append("\n\t").append(t.toString());
            Log.p(message.toString(), Log.ERROR);
        }
    }

    protected abstract void doAction(ActionEvent evt) throws Exception;

}
