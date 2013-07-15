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

import java.util.Enumeration;

import siarhei.luskanau.j2me.core.config.ConfigDao;

import com.sun.lwuit.io.util.Log;

/**
 * @author <a href="mailto:siarhei.luskanau@gmail.com">Siarhei Luskanau</a>
 */
public class BaseConfig {

    protected ConfigDao configDao;

    /**
     * Initialization of ConfigDao
     */
    public BaseConfig(String storageName) throws Exception {
        try {
            configDao = new ConfigDao(storageName);
            Enumeration en = configDao.getKeys();
            for (; en != null && en.hasMoreElements();) {
                String key = (String) en.nextElement();
                String value = configDao.get(key);
                Log.p("BaseConfig: " + key + "=" + value);
            }
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when create BaseConfig.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public Enumeration getKeys() throws Exception {
        try {
            return configDao.getKeys();
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when getKeys in BaseConfig.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public String getFormConfig(String name, String key) throws Exception {
        try {
            return configDao.get("FORM." + name + "." + key);
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when getFormConfig in BaseConfig.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public void setFormConfig(String name, String key, String value) throws Exception {
        try {
            configDao.set("FORM." + name + "." + key, value);
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when setFormConfig in BaseConfig.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

}
