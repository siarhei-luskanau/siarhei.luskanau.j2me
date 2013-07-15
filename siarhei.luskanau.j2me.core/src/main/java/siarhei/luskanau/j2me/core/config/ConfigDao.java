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

package siarhei.luskanau.j2me.core.config;

import java.util.Enumeration;
import java.util.Hashtable;

import siarhei.luskanau.j2me.core.storage.Storage;
import siarhei.luskanau.j2me.core.storage.StorageEngine;
import siarhei.luskanau.j2me.core.storage.engine.RmsStorageEngine;

/**
 * Класс для хранения property "key=value" в RMS
 * 
 * @author <a href="mailto:siarhei.luskanau@gmail.com">Siarhei Luskanau</a>
 */
public class ConfigDao {

    public final String STORAGE_NAME = "AppConfig";

    private ConfigRecord configRecord;

    /**
     * таблица для хранения property в памяти
     */
    private Hashtable configHashtable = new Hashtable();

    public ConfigDao() throws Exception {
        this(null);
    }

    /**
     * Конструктор, в нем загружается property из хранилища
     * 
     * @throws Exception
     */
    public ConfigDao(String storageName) throws Exception {
        try {
            if (storageName == null) {
                storageName = STORAGE_NAME;
            }
            StorageEngine storageEngine = new RmsStorageEngine(storageName);
            Storage storage = new Storage(storageEngine);
            configRecord = new ConfigRecord();
            storage.setRecord(configRecord);
            storageEngine.open();
            Enumeration en = configRecord.selectKeys();
            for (; en != null && en.hasMoreElements();) {
                String key = (String) en.nextElement();
                Config config = configRecord.select(key);
                configHashtable.put(config.getKey(), config.getValue());
            }
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when create ConfigDao.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public Enumeration getKeys() throws Exception {
        try {
            return configHashtable.keys();
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when getKeys in ConfigDao.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    /**
     * @param key
     *            ключ в конфиге
     * @return строка
     * @throws Exception
     */
    public String get(String key) throws Exception {
        try {
            String value = (String) configHashtable.get(key);
            return value;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when get in ConfigDao.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    /**
     * @param key
     *            ключ в конфиге
     * @param value
     *            строка
     * @throws Exception
     */
    public void set(String key, String value) throws Exception {
        try {
            if (value != null) {
                configHashtable.put(key, value);
                Config config = new Config(key, value);
                if (configRecord.exist(key)) {
                    configRecord.update(config);
                } else {
                    configRecord.insert(config);
                }
            } else {
                configHashtable.remove(key);
                if (configRecord.exist(key)) {
                    configRecord.delete(key);
                }
            }
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when set in ConfigDao.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

}
