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

package siarhei.luskanau.j2me.map.task;

import siarhei.luskanau.j2me.core.utils.LogActionListener;
import siarhei.luskanau.j2me.map.dao.storage.FileMapsDao;
import siarhei.luskanau.j2me.map.entity.Map;
import siarhei.luskanau.j2me.map.taskpool.Task;

import com.sun.lwuit.events.ActionEvent;

/**
 * @author <a href="mailto:siarhei.luskanau@gmail.com">Siarhei Luskanau</a>
 */
public class StorageSaveMapsTask implements Task {

    private Map map;
    private FileMapsDao fileMapsDao;

    public StorageSaveMapsTask(Map map, FileMapsDao fileMapsDao) {
        this.map = map;
        this.fileMapsDao = fileMapsDao;
    }

    public void doTask() throws Exception {
        new LogActionListener("Error when setMap in doTask in StorageSaveMapsTask.") {
            public void doAction(ActionEvent evt) throws Exception {
                fileMapsDao.setMap(map, null, null);
            }
        }.actionPerformed(null);
    }

    public int getType() throws Exception {
        return Task.OTHER_TYPE;
    }

}
