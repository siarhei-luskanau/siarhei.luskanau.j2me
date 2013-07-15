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

import java.util.Hashtable;

import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;

import siarhei.luskanau.j2me.core.storage.Index;
import siarhei.luskanau.j2me.core.storage.RawData;
import siarhei.luskanau.j2me.core.storage.Record;
import siarhei.luskanau.j2me.core.storage.StorageEngine;

import com.sun.lwuit.io.util.Log;

/**
 * @author <a href="mailto:siarhei.luskanau@gmail.com">Siarhei Luskanau</a>
 */
public class RmsStorageEngine extends StorageEngine {

    private String storagName;

    public RmsStorageEngine(String storagName) throws Exception {
        try {
            this.storagName = storagName;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when create RmsStorageEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public void open() throws Exception {
        try {
            loadIndex();
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when open in RmsStorageEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public long append(String name, byte[] data) throws Exception {
        RecordStore recordStore = null;
        try {
            recordStore = openRecordStore();
            return justAppend(name, data, recordStore);
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when append in RmsStorageEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        } finally {
            close(recordStore);
        }
    }

    public long justAppend(String name, byte[] data, RecordStore recordStore) throws Exception {
        try {
            RawData rawData = new RawData(true, name, data);
            byte[] recordBytes = toRocordData(rawData);
            return recordStore.addRecord(recordBytes, 0, recordBytes.length);
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when justAppend in RmsStorageEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public byte[] read(long position) throws Exception {
        RecordStore recordStore = null;
        try {
            recordStore = openRecordStore();
            return justRead(position, recordStore);
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when read in RmsStorageEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        } finally {
            close(recordStore);
        }
    }

    public byte[] justRead(long position, RecordStore recordStore) throws Exception {
        try {
            RawData rawData = fromRocordData(recordStore.getRecord((int) position));
            if (rawData.isValid()) {
                return rawData.getData();
            }
            return null;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when justRead in RmsStorageEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public void delete(long position) throws Exception {
        RecordStore recordStore = null;
        try {
            recordStore = openRecordStore();
            recordStore.deleteRecord((int) position);
            createIndex();
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when delete in RmsStorageEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        } finally {
            close(recordStore);
        }
    }

    public void update(long position, String name, byte[] data) throws Exception {
        RecordStore recordStore = null;
        try {
            recordStore = openRecordStore();
            justUpdate(position, name, data, recordStore);
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when update in RmsStorageEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        } finally {
            close(recordStore);
        }
    }

    public void justUpdate(long position, String name, byte[] data, RecordStore recordStore) throws Exception {
        try {
            RawData rawData = new RawData(true, name, data);
            byte[] recordBytes = toRocordData(rawData);
            recordStore.setRecord((int) position, recordBytes, 0, recordBytes.length);
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when justUpdate in RmsStorageEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public void compression() throws Exception {
        // TODO Auto-generated method stub
    }

    public void loadIndex() throws Exception {
        createIndex();
        // TODO Auto-generated method stub
    }

    public void saveIndex() throws Exception {
        // TODO Auto-generated method stub
    }

    public void createIndex() throws Exception {
        RecordStore recordStore = null;
        try {
            indexes = new Hashtable();
            recordStore = openRecordStore();
            Log.p("RecordStore " + recordStore.getName() + " SizeAvailable " + recordStore.getSizeAvailable());
            RecordEnumeration re = recordStore.enumerateRecords(null, null, false);
            for (; re.hasNextElement();) {
                int position = re.nextRecordId();
                RawData rawData = fromRocordData(recordStore.getRecord(position));
                String name = rawData.getName();
                if (rawData.isValid()) {
                    Record record = storage.getRecords(name);
                    Index index = getIndex(name);
                    record.doIndex(rawData.getData(), index, position);
                }
            }
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when createIndex in RmsStorageEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        } finally {
            close(recordStore);
        }
    }

    private RecordStore openRecordStore() throws Exception {
        try {
            RecordStore recordStore = RecordStore.openRecordStore(storagName, true, RecordStore.AUTHMODE_ANY,
                    true);
            return recordStore;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when openRecordStore in RmsStorageEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    /*
     * private RecordStore openIndexRecordStore(boolean remove) throws Exception { try { String name =
     * storagName + ".idx"; if (remove) { RecordStore.deleteRecordStore(name); } RecordStore recordStore =
     * RecordStore.openRecordStore(name, true); return recordStore; } catch (Throwable t) { StringBuffer
     * message = new StringBuffer(); message.append("Error when openIndexRecordStore in RmsStorageEngine.");
     * message.append("\n\t").append(t.toString()); throw new Exception(message.toString()); } }
     */

    private void close(RecordStore recordStore) throws Exception {
        try {
            recordStore.closeRecordStore();
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when close in RmsStorageEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

}
