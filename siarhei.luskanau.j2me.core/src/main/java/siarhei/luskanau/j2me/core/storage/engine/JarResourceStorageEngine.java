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

package siarhei.luskanau.j2me.core.storage.engine;

import java.io.DataInputStream;
import java.io.InputStream;

import siarhei.luskanau.j2me.core.storage.Index;
import siarhei.luskanau.j2me.core.storage.RawData;
import siarhei.luskanau.j2me.core.storage.Record;
import siarhei.luskanau.j2me.core.storage.StorageEngine;

import com.sun.lwuit.io.util.Log;

/**
 * @author <a href="mailto:siarhei.luskanau@gmail.com">Siarhei Luskanau</a>
 */
public class JarResourceStorageEngine extends StorageEngine {

    private String storageName;

    public JarResourceStorageEngine(String storageName) throws Exception {
        try {
            this.storageName = storageName;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when create JarResourceStorageEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public void open() throws Exception {
        try {
            try {
                loadIndex();
            } catch (Exception e) {
                createIndex();
            }
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when open in JarResourceStorageEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public void loadIndex() throws Exception {
        try {
            InputStream inputStream = openIndexInputStream();
            DataInputStream dataInput = new DataInputStream(inputStream);
            for (;;) {
                try {
                    String indexName = dataInput.readUTF();
                    boolean unique = dataInput.readBoolean();
                    String key = dataInput.readUTF();
                    String value = dataInput.readUTF();
                    long position = dataInput.readLong();
                    Log.p("JarResourceStorageEngine.loadIndex " + indexName + " " + unique + " " + key + " "
                            + value);
                    Index index = getIndex(indexName);
                    index.add(key, value, unique, position);
                } catch (Exception e) {
                    // тихо e.printStackTrace();
                    break;
                }
            }
            dataInput.close();
            inputStream.close();
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when loadIndex in JarResourceStorageEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public void createIndex() throws Exception {
        try {
            InputStream inputStream = openStorageInputStream();
            DataInputStream dataInput = new DataInputStream(inputStream);
            long position = 0;
            for (;;) {
                try {
                    int size = dataInput.readInt();
                    long cnt = 4;
                    cnt += size;
                    if (size > 0) {
                        byte[] rocordData = new byte[size];
                        dataInput.read(rocordData);
                        RawData rawData = fromRocordData(rocordData);
                        String name = rawData.getName();
                        if (rawData.isValid()) {
                            Record record = storage.getRecords(name);
                            Index index = getIndex(name);
                            record.doIndex(rawData.getData(), index, position);
                        }
                    }
                    position += cnt;
                } catch (Exception e) {
                    // тихо e.printStackTrace();
                    break;
                }
            }
            dataInput.close();
            inputStream.close();
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when createIndex in JarResourceStorageEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public byte[] read(long position) throws Exception {
        try {
            byte[] data = null;
            InputStream inputStream = openStorageInputStream();
            inputStream.skip(position);
            DataInputStream dataInput = new DataInputStream(inputStream);
            int size = dataInput.readInt();
            if (size > 0) {
                byte[] rocordData = new byte[size];
                dataInput.read(rocordData);
                RawData rawData = fromRocordData(rocordData);
                if (rawData.isValid()) {
                    data = rawData.getData();
                }
            }
            dataInput.close();
            inputStream.close();
            return data;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when read in JarResourceStorageEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    private InputStream openStorageInputStream() throws Exception {
        try {
            String url = "/" + storageName + ".dat";
            InputStream inputStream = getClass().getResourceAsStream(url);
            if (inputStream == null) {
                throw new RuntimeException("Jar resource not found! " + url);
            }
            return inputStream;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when openStorageInputStream in JarResourceStorageEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    private InputStream openIndexInputStream() throws Exception {
        try {
            String url = "/" + storageName + ".idx";
            InputStream inputStream = getClass().getResourceAsStream(url);
            if (inputStream == null) {
                throw new RuntimeException("Jar resource not found! " + url);
            }
            return inputStream;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when openIndexInputStream in JarResourceStorageEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public long append(String name, byte[] data) throws Exception {
        try {
            throw new RuntimeException("Not implemented!");
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when append in JarResourceStorageEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public void compression() throws Exception {
        try {
            throw new RuntimeException("Not implemented!");
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when compression in JarResourceStorageEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public void delete(long position) throws Exception {
        try {
            throw new RuntimeException("Not implemented!");
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when delete in JarResourceStorageEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public void saveIndex() throws Exception {
        try {
            throw new RuntimeException("Not implemented!");
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when saveIndex in JarResourceStorageEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public void update(long position, String name, byte[] data) throws Exception {
        try {
            throw new RuntimeException("Not implemented!");
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when update in JarResourceStorageEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

}
