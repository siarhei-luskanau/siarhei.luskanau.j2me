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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Enumeration;
import java.util.Hashtable;

import siarhei.luskanau.j2me.core.storage.Index;
import siarhei.luskanau.j2me.core.storage.Record;

/**
 * Класс, представляющий собой запись в хранилище StorageEngine и методы работы с записями.
 * 
 * @author <a href="mailto:siarhei.luskanau@gmail.com">Siarhei Luskanau</a>
 */
public class ConfigRecord extends Record {

    /**
     * Название поля по котораму строится индекс
     */
    private final String KEY = "key";

    /**
     * Имя типа записи в хранилище
     */
    public String getName() {
        return "config";
    }

    public byte[] toByteArray(Object entity) throws Exception {
        try {
            Config config = (Config) entity;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            DataOutputStream dataOutput = new DataOutputStream(output);

            dataOutput.writeUTF(config.getKey());
            dataOutput.writeUTF(config.getValue());

            dataOutput.flush();
            output.flush();
            return output.toByteArray();
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when toByteArray in ConfigRecord.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public Object toEntity(byte[] bytes) throws Exception {
        try {
            DataInputStream dataInput = new DataInputStream(new ByteArrayInputStream(bytes));
            Config config = new Config();

            config.setKey(dataInput.readUTF());
            config.setValue(dataInput.readUTF());

            return config;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when toEntity in ConfigRecord.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public void doIndex(byte[] bytes, Index index, long position) throws Exception {
        try {
            Config config = (Config) toEntity(bytes);
            doIndex(config, index, position);
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when doIndex byte[] in ConfigRecord.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    private void doIndex(Config config, Index index, long position) throws Exception {
        try {
            index.add(KEY, config.getKey(), true, position);
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when doIndex Config in ConfigRecord.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public Config select(String key) throws Exception {
        try {
            Config config = null;
            long position = getIndex().findUnique(KEY, key);
            if (position >= 0) {
                byte[] bytes = getStorageEngine().read(position);
                config = (Config) toEntity(bytes);
            }
            return config;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when select in ConfigRecord.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public void delete(String key) throws Exception {
        try {
            long position = getIndex().findUnique(KEY, key);
            if (position >= 0) {
                getStorage().getStorageEngine().delete(position);
                getIndex().delete(position);
                getStorageEngine().saveIndex();
            }
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when delete in ConfigRecord.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public boolean exist(String key) throws Exception {
        try {
            long position = getIndex().findUnique(KEY, key);
            if (position >= 0) {
                return true;
            } else {
                return false;
            }
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when exist in ConfigRecord.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public void update(Config config) throws Exception {
        try {
            long position = getIndex().findUnique(KEY, config.getKey());
            if (position >= 0) {
                getStorageEngine().update(position, getName(), toByteArray(config));
            } else {
                insert(config);
            }
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when update in ConfigRecord.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public void insert(Config config) throws Exception {
        try {
            long position = getStorageEngine().append(getName(), toByteArray(config));
            doIndex(config, getIndex(), position);
            getStorageEngine().saveIndex();
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when insert in ConfigRecord.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public Enumeration selectKeys() throws Exception {
        try {
            Hashtable names = getIndex().getKeyHashtable(KEY);
            return names.keys();
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when selectKeys in ConfigRecord.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

}
