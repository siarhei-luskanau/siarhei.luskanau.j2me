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

package siarhei.luskanau.j2me.map.taskpool;

import java.util.Enumeration;

/**
 * @author <a href="mailto:siarhei.luskanau@gmail.com">Siarhei Luskanau</a>
 */
public class WebPool extends TaskPool {

    private int currentType;

    public Task getAndRemoveNextTask() throws Exception {
        try {
            Task task = null;
            synchronized (tasks) {
                if (!tasks.isEmpty()) {
                    for (int i = 0; i < 3; i++) {
                        if (task != null) {
                            break;
                        }
                        if (currentType == Task.DEVICE_TYPE) {
                            currentType = Task.MAP_TYPE;
                        } else if (currentType == Task.MAP_TYPE) {
                            currentType = Task.OTHER_TYPE;
                        } else {
                            currentType = Task.DEVICE_TYPE;
                        }
                        for (Enumeration en = tasks.elements(); en.hasMoreElements();) {
                            Task currTask = (Task) en.nextElement();
                            if (currTask.getType() == currentType) {
                                task = currTask;
                                break;
                            }
                        }
                    }
                }
            }
            tasks.removeElement(task);
            return task;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when getAndRemoveNextTask in TaskPool.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

}
