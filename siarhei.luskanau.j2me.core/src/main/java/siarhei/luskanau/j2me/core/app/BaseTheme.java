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

package siarhei.luskanau.j2me.core.app;

import java.util.Hashtable;

import com.sun.lwuit.list.DefaultListCellRenderer;
import com.sun.lwuit.plaf.Border;
import com.sun.lwuit.plaf.UIManager;

/**
 * @author <a href="mailto:siarhei.luskanau@gmail.com">Siarhei Luskanau</a>
 */
public class BaseTheme {

    protected UIManager manager = UIManager.getInstance();

    public void setMyTheme() throws Exception {
        try {
            DefaultListCellRenderer.setShowNumbersDefault(false);
            setResourceBundle();

            setLabel();
            setButton();
            setCheckBox();
            setListRender();
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when setMyTheme in MyTheme.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    private void setResourceBundle() {
        Hashtable resourceBundle = manager.getResourceBundle();
        if (resourceBundle == null) {
            resourceBundle = new Hashtable();
            manager.setResourceBundle(resourceBundle);
        }
        resourceBundle.put("cancel", "Отмена");
        resourceBundle.put("ok", "Сохранить");
    }

    private void setLabel() {
        Hashtable themeProps = new Hashtable();

        themeProps.put("Label.transparency", "0");

        manager.addThemeProps(themeProps);
    }

    private void setButton() {
        Hashtable themeProps = new Hashtable();

        themeProps.put("Button.press#padding", "10,10,10,10");
        themeProps.put("Button.press#bgColor", "777777");
        themeProps.put("Button.press#border", Border.createEmpty());

        themeProps.put("Button.sel#padding", "10,10,10,10");
        themeProps.put("Button.sel#bgColor", "AAAAAA");
        themeProps.put("Button.sel#border", Border.createEmpty());

        themeProps.put("Button.padding", "10,10,10,10");
        themeProps.put("Button.bgColor", "EEEEEE");
        themeProps.put("Button.border", Border.createEmpty());

        manager.addThemeProps(themeProps);
    }

    private void setCheckBox() {
        Hashtable themeProps = new Hashtable();

        themeProps.put("CheckBox.press#padding", "10,10,10,10");
        themeProps.put("CheckBox.press#bgColor", "777777");

        themeProps.put("CheckBox.sel#padding", "10,10,10,10");
        themeProps.put("CheckBox.sel#bgColor", "AAAAAA");

        themeProps.put("CheckBox.padding", "10,10,10,10");
        themeProps.put("CheckBox.bgColor", "EEEEEE");

        manager.addThemeProps(themeProps);
    }

    private void setListRender() {
        Hashtable themeProps = new Hashtable();

        themeProps.put("ListRenderer.sel#padding", "10,10,10,10");
        themeProps.put("ListRenderer.padding", "10,10,10,10");

        themeProps.put("ListRenderer.transparency", "255");
        themeProps.put("ListRenderer.bgColor", "EEEEEE");
        themeProps.put("ListRenderer.sel#transparency", "255");
        themeProps.put("ListRenderer.sel#bgColor", "AAAAAA");

        themeProps.put("ListRendererFocus.transparency", "255");
        themeProps.put("ListRendererFocus.bgColor", "AAAAAA");

        manager.addThemeProps(themeProps);
    }

}
