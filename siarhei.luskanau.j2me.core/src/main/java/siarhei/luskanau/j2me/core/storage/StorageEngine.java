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

package siarhei.luskanau.j2me.core.storage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Hashtable;

/**
 * @author <a href="mailto:siarhei.luskanau@gmail.com">Siarhei Luskanau</a>
 */
public abstract class StorageEngine {

    protected Storage storage;

    protected Hashtable indexes = new Hashtable();

    public abstract void open() throws Exception;

    public abstract long append(String name, byte[] data) throws Exception;

    public abstract byte[] read(long position) throws Exception;

    public abstract void delete(long position) throws Exception;

    public abstract void update(long position, String name, byte[] data) throws Exception;

    public abstract void compression() throws Exception;

    public abstract void loadIndex() throws Exception;

    public abstract void saveIndex() throws Exception;

    public abstract void createIndex() throws Exception;

    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public Index getIndex(String name) throws Exception {
        try {
            Index index = (Index) indexes.get(name);
            if (index == null) {
                index = new Index();
                indexes.put(name, index);
            }
            return index;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when getIndex in StorageEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public byte[] toRocordData(RawData rawData) throws Exception {
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            DataOutputStream dataOutput = new DataOutputStream(output);

            dataOutput.writeBoolean(rawData.isValid());
            dataOutput.writeUTF(rawData.getName());

            byte[] data = rawData.getData();
            if (data != null) {
                dataOutput.writeInt(data.length);
                dataOutput.write(data);
            } else {
                dataOutput.writeInt(0);
            }

            dataOutput.flush();
            output.flush();
            return output.toByteArray();
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when toRocordData in StorageEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public RawData fromRocordData(byte[] rocordData) throws Exception {
        try {
            RawData rawData = new RawData();
            byte[] data = null;

            ByteArrayInputStream inputStream = new ByteArrayInputStream(rocordData);
            DataInputStream dataInput = new DataInputStream(inputStream);

            boolean valid = dataInput.readBoolean();
            rawData.setValid(valid);
            rawData.setName(dataInput.readUTF());

            if (valid) {
                int size = dataInput.readInt();
                if (size > 0) {
                    data = new byte[size];
                    dataInput.read(data);
                    rawData.setData(data);
                }
            }

            dataInput.close();
            return rawData;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when fromRocordData in StorageEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

}
