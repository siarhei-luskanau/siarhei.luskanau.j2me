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
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import siarhei.luskanau.j2me.core.storage.Index;
import siarhei.luskanau.j2me.core.storage.RawData;
import siarhei.luskanau.j2me.core.storage.Record;
import siarhei.luskanau.j2me.core.storage.StorageEngine;
import siarhei.luskanau.j2me.core.utils.LogActionListener;

import com.sun.lwuit.events.ActionEvent;

/**
 * @author <a href="mailto:siarhei.luskanau@gmail.com">Siarhei Luskanau</a>
 */
public class FileStorageEngine extends StorageEngine {

    private String baseUrl;

    public FileStorageEngine(String baseUrl) throws Exception {
        try {
            this.baseUrl = baseUrl;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when create FileStorageEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public void open() throws Exception {
        try {
            loadIndex();
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when open in FileStorageEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public long append(String name, byte[] data) throws Exception {
        FileConnection connection = null;
        try {
            connection = openFileConnection();
            return justAppend(name, data, connection);
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when append in FileStorageEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        } finally {
            close(connection);
        }
    }

    public long justAppend(String name, byte[] data, FileConnection connection) throws Exception {
        OutputStream outputStream = null;
        try {
            long position = justLength(connection);
            outputStream = openOutputStream(connection, position);
            DataOutputStream dataOutput = new DataOutputStream(outputStream);

            RawData rawData = new RawData(true, name, data);
            byte[] rocordData = toRocordData(rawData);
            dataOutput.writeInt(rocordData.length);
            dataOutput.write(rocordData);

            dataOutput.flush();
            outputStream.flush();
            dataOutput.close();
            outputStream.close();
            outputStream = null;

            return position;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when justAppend in FileStorageEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        } finally {
            close(outputStream);
        }
    }

    public byte[] read(long position) throws Exception {
        FileConnection connection = null;
        try {
            connection = openFileConnection();
            return justRead(position, connection);
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when read in FileStorageEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        } finally {
            close(connection);
        }
    }

    public byte[] justRead(long position, FileConnection connection) throws Exception {
        InputStream inputStream = null;
        try {
            byte[] data = null;
            inputStream = openInputStream(connection, position);
            DataInputStream dataInput = new DataInputStream(inputStream);

            int size = dataInput.readInt();
            if (size > 0) {
                byte[] rocordData = new byte[size];
                dataInput.read(rocordData);
                RawData rawData = fromRocordData(rocordData);
                if (rawData.isValid()) {
                    dataInput.close();
                    data = rawData.getData();
                }
            }

            dataInput.close();
            return data;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when justRead in FileStorageEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        } finally {
            close(inputStream);
        }
    }

    public void delete(long position) throws Exception {
        try {
            throw new RuntimeException("Not implemented!");
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when delete in FileStorageEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public void update(long position, String name, byte[] data) throws Exception {
        try {
            throw new RuntimeException("Not implemented!");
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when update in FileStorageEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public void compression() throws Exception {
        try {
            throw new RuntimeException("Not implemented!");
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when compression in FileStorageEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public void loadIndex() throws Exception {
        FileConnection connection = null;
        InputStream inputStream = null;
        try {
            connection = openIndexConnection(false);
            if (connection.fileSize() > 0) {
                inputStream = openInputStream(connection);
                DataInputStream dataInput = new DataInputStream(inputStream);

                for (;;) {
                    try {
                        String indexName = dataInput.readUTF();
                        boolean unique = dataInput.readBoolean();
                        String key = dataInput.readUTF();
                        String value = dataInput.readUTF();
                        long position = dataInput.readLong();
                        Index index = getIndex(indexName);
                        index.add(key, value, unique, position);
                    } catch (Exception e) {
                        // тихо e.printStackTrace();
                        break;
                    }
                }
            } else {
                createIndex();
                saveIndex();
            }

        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when loadIndex in FileStorageEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        } finally {
            close(inputStream);
            close(connection);
        }
    }

    public void saveIndex() throws Exception {
        FileConnection connection = null;
        OutputStream outputStream = null;
        try {
            if (indexes == null) {
                return;
            }
            connection = openIndexConnection(true);
            outputStream = openOutputStream(connection);
            for (Enumeration en = indexes.keys(); en.hasMoreElements();) {
                String indexName = (String) en.nextElement();
                Index index = (Index) indexes.get(indexName);
                index.save(outputStream, indexName);
            }
            outputStream.flush();
            outputStream.close();
            outputStream = null;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when saveIndex in FileStorageEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        } finally {
            close(outputStream);
            close(connection);
        }
    }

    public void createIndex() throws Exception {
        FileConnection connection = null;
        InputStream inputStream = null;
        try {
            connection = openFileConnection();
            inputStream = openInputStream(connection);
            long storageSize = justLength(connection);
            DataInputStream dataInput = new DataInputStream(inputStream);
            long position = 0;

            for (; storageSize > position;) {
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
            }

            dataInput.close();
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when createIndex in FileStorageEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        } finally {
            close(inputStream);
            close(connection);
        }
    }

    public long justLength(FileConnection connection) throws Exception {
        try {
            return connection.fileSize();
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when length in FileStorageEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    private FileConnection openFileConnection() throws Exception {
        try {
            FileConnection connection = (FileConnection) Connector.open(baseUrl + ".dat");
            if (!connection.exists()) {
                connection.create();
            }
            return connection;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when openFileConnection in FileStorageEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    private FileConnection openIndexConnection(boolean remove) throws Exception {
        try {
            FileConnection connection = (FileConnection) Connector.open(baseUrl + ".idx");
            if (remove && connection.exists()) {
                connection.delete();
            }
            if (!connection.exists()) {
                connection.create();
            }
            return connection;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when openFileConnection in FileStorageEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    private InputStream openInputStream(FileConnection connection) throws Exception {
        return openInputStream(connection, -1);
    }

    private InputStream openInputStream(FileConnection connection, long position) throws Exception {
        try {
            InputStream inputStream = connection.openInputStream();
            if (position > 0) {
                inputStream.skip(position);
            }
            return inputStream;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when openInputStream in FileStorageEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    private OutputStream openOutputStream(FileConnection connection) throws Exception {
        return openOutputStream(connection, -1);
    }

    private OutputStream openOutputStream(FileConnection connection, long position) throws Exception {
        try {
            OutputStream outputStream = null;
            if (position > 0) {
                outputStream = connection.openOutputStream(position);
            } else {
                outputStream = connection.openOutputStream();
            }
            return outputStream;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when openOutputStream in FileStorageEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    private void close(final InputStream inputStream) {
        new LogActionListener("Error when close InputStream in FileStorageEngine.") {
            public void doAction(ActionEvent evt) throws Exception {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
        }.actionPerformed(null);
    }

    private void close(final OutputStream outputStream) {
        new LogActionListener("Error when close OutputStream in FileStorageEngine.") {
            public void doAction(ActionEvent evt) throws Exception {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            }
        }.actionPerformed(null);
    }

    private void close(final FileConnection connection) {
        new LogActionListener("Error when close FileConnection in FileStorageEngine.") {
            public void doAction(ActionEvent evt) throws Exception {
                if (connection != null) {
                    connection.close();
                }
            }
        }.actionPerformed(null);
    }

}
