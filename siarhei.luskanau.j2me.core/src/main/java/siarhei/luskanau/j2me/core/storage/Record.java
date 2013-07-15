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

/**
 * Класс, представляющий собой запись в хранилище StorageEngine и методы работы с записью.
 * 
 * @author <a href="mailto:siarhei.luskanau@gmail.com">Siarhei Luskanau</a>
 */
public abstract class Record {

    private Storage storage;

    protected abstract String getName();

    protected abstract byte[] toByteArray(Object entity) throws Exception;

    protected abstract Object toEntity(byte[] bytes) throws Exception;

    public abstract void doIndex(byte[] bytes, Index index, long position) throws Exception;

    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    protected Index getIndex() throws Exception {
        try {
            return storage.getStorageEngine().getIndex(getName());
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when getIndex in Record.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public StorageEngine getStorageEngine() throws Exception {
        try {
            return storage.getStorageEngine();
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when getStorageEngine in Record.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

}
