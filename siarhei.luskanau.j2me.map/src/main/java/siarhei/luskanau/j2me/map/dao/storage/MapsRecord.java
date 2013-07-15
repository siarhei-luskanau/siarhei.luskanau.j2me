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

package siarhei.luskanau.j2me.map.dao.storage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Enumeration;
import java.util.Hashtable;

import siarhei.luskanau.j2me.core.storage.Index;
import siarhei.luskanau.j2me.core.storage.Record;
import siarhei.luskanau.j2me.map.entity.Map;

/**
 * @author <a href="mailto:siarhei.luskanau@gmail.com">Siarhei Luskanau</a>
 */
public class MapsRecord extends Record {

    private final String NAME_KEY = "name";

    public String getName() {
        return "MapsRecord";
    }

    public byte[] toByteArray(Object entity) throws Exception {
        try {
            Map map = (Map) entity;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            DataOutputStream dataOutput = new DataOutputStream(output);

            dataOutput.writeUTF(map.getName());
            byte[] data = map.getData();
            if (data != null) {
                dataOutput.writeInt(data.length);
            } else {
                dataOutput.writeInt(0);
            }
            dataOutput.write(data);

            dataOutput.flush();
            output.flush();
            return output.toByteArray();
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when toByteArray in MapsRecord.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public Object toEntity(byte[] bytes) throws Exception {
        try {
            DataInputStream dataInput = new DataInputStream(new ByteArrayInputStream(bytes));
            Map map = new Map();
            map.setName(dataInput.readUTF());
            int size = dataInput.readInt();
            byte[] data = null;
            if (size > 0) {
                data = new byte[size];
                dataInput.read(data);
                map.setData(data);
            }
            return map;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when toEntity in MapsRecord.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public void doIndex(byte[] bytes, Index index, long position) throws Exception {
        try {
            Map map = (Map) toEntity(bytes);
            doIndex(map, index, position);
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when doIndex byte[] in MapsRecord.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    private void doIndex(Map map, Index index, long position) throws Exception {
        try {
            index.add(NAME_KEY, map.getName(), true, position);
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when doIndex map in MapsRecord.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public Map select(String name) throws Exception {
        try {
            long position = getIndex().findUnique(NAME_KEY, name);
            byte[] bytes = getStorageEngine().read(position);
            Map map = (Map) toEntity(bytes);
            return map;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when select in MapsRecord.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public boolean exist(String name) throws Exception {
        try {
            long position = getIndex().findUnique(NAME_KEY, name);
            if (position >= 0) {
                return true;
            } else {
                return false;
            }
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when exist in MapsRecord.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public void append(Map map) throws Exception {
        try {
            long position = getStorageEngine().append(getName(), toByteArray(map));
            doIndex(map, getIndex(), position);
            // getStorageEngine().saveIndex();
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when append in MapsRecord.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public Enumeration selectNames() throws Exception {
        try {
            Hashtable names = getIndex().getKeyHashtable(NAME_KEY);
            return names.keys();
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when selectNames in MapsRecord.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

}
