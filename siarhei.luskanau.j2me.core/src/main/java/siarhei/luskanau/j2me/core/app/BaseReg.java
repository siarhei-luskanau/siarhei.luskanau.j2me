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

import com.sun.lwuit.Form;

/**
 * @author <a href="mailto:siarhei.luskanau@gmail.com">Siarhei Luskanau</a>
 */
public abstract class BaseReg {

    static private BaseReg instance;

    protected Hashtable registry = new Hashtable();

    protected Hashtable formsConfig = new Hashtable();

    static public BaseReg instance() {
        return instance;
    }

    static public void setInstance(BaseReg baseReg) {
        instance = baseReg;
    }

    public abstract void init(WelcomeCanvas welcomeCanvas) throws Exception;

    public Object getModule(String type) throws Exception {
        try {
            return registry.get(type);
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when getModule in BaseReg.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public void setModule(String type, Object module) throws Exception {
        try {
            if (module != null) {
                registry.put(type, module);
            } else {
                registry.remove(type);
            }
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when getModule in BaseReg.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public void show(String screenType) throws Exception {
        show(screenType, null);
    }

    public void show(String formId, Object parameter) throws Exception {
        try {
            Form form = null;
            if (formId == null) {
                throw new RuntimeException("screenType is null!");
            } else {
                form = getFormForId(formId);
            }
            if (form == null) {
                throw new RuntimeException("Incorrect screenType.");
            }
            form.putClientProperty("parameter", parameter);
            form.show();
        } catch (Throwable t) {
            getDefaultForm().show();

            StringBuffer message = new StringBuffer();
            message.append("Error when show in BaseReg.");
            message.append(" formId: ").append(formId);
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public abstract Form getFormForId(String formId) throws Exception;

    public abstract Form getDefaultForm() throws Exception;

    public Hashtable getFormsConfig() {
        return formsConfig;
    }

    public void setFormsConfig(Hashtable formsConfig) {
        this.formsConfig = formsConfig;
    }

}
