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

import siarhei.luskanau.j2me.core.app.BaseTheme;

import com.sun.lwuit.plaf.Style;

/**
 * @author <a href="mailto:siarhei.luskanau@gmail.com">Siarhei Luskanau</a>
 */
public class AppTheme extends BaseTheme {

    public static final String RED_STATE_FILE = "/red.png";

    public void setMyTheme() throws Exception {
        try {
            super.setMyTheme();
            setSelectedStyle("RadioButton");
            setUnSelectedStyle("RadioButton");

        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when setMyTheme in MyTheme.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    private void setSelectedStyle(String id) {
        Style style = manager.getComponentSelectedStyle(id);
        style.setBgColor(0xAAAAAA);
        manager.setComponentSelectedStyle(id, style);
    }

    private void setUnSelectedStyle(String id) {
        Style style = manager.getComponentStyle(id);
        style.setBgColor(0xEEEEEE);
        manager.setComponentStyle(id, style);
    }

}
