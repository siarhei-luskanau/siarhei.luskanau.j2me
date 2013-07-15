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

import siarhei.luskanau.j2me.core.storage.StorageEngine;

/**
 * @author <a href="mailto:siarhei.luskanau@gmail.com">Siarhei Luskanau</a>
 */
public class MemoryStorageEngine extends StorageEngine {

    public void open() throws Exception {
        // TODO Auto-generated method stub

    }

    public long append(String name, byte[] data) throws Exception {
        // TODO Auto-generated method stub
        return 0;
    }

    public void compression() throws Exception {
        // TODO Auto-generated method stub
    }

    public void createIndex() throws Exception {
        // TODO Auto-generated method stub
    }

    public void delete(long position) throws Exception {
        // TODO Auto-generated method stub
    }

    public void loadIndex() throws Exception {
        // TODO Auto-generated method stub
    }

    public void open(String storageName, boolean readOnly) throws Exception {
        // TODO Auto-generated method stub
    }

    public byte[] read(long position) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    public void saveIndex() throws Exception {
        // TODO Auto-generated method stub
    }

    public void update(long position, String name, byte[] data) throws Exception {
        // TODO Auto-generated method stub
    }

}
