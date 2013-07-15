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

import java.util.Hashtable;

/**
 * @author <a href="mailto:siarhei.luskanau@gmail.com">Siarhei Luskanau</a>
 */
public class Storage {

    private StorageEngine storageEngine;

    private Hashtable records = new Hashtable();

    public Storage(StorageEngine storageEngine) throws Exception {
        try {
            this.storageEngine = storageEngine;
            storageEngine.setStorage(this);
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when create Storage.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public StorageEngine getStorageEngine() {
        return storageEngine;
    }

    public Record getRecords(Record record) throws Exception {
        return getRecords(record.getName());
    }

    public Record getRecords(String name) throws Exception {
        try {
            Record record = (Record) records.get(name);
            return record;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when getRecords in Storage.");
            message.append(" Name is: ").append(name);
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public void setRecord(Record record) throws Exception {
        try {
            record.setStorage(this);
            records.put(record.getName(), record);
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when setRecord in Storage.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

}
