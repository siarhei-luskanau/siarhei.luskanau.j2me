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

import java.util.Vector;

import com.sun.lwuit.io.util.Log;

/**
 * Class for run tasks in sequence
 * 
 * @author <a href="mailto:siarhei.luskanau@gmail.com">Siarhei Luskanau</a>
 */
public class TaskPool {

    protected Vector tasks = new Vector();

    private TaskRunnable taskRunner = new TaskRunnable(this);

    public void addTask(Task task) throws Exception {
        try {
            if (task != null) {
                synchronized (tasks) {
                    tasks.addElement(task);
                    taskRunner.pick();
                }
            }
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when addTask in TaskPool.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public void removeTask(Task task) throws Exception {
        try {
            if (task != null) {
                synchronized (tasks) {
                    tasks.removeElement(task);
                }
            }
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when removeTask in TaskPool.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public Task getAndRemoveNextTask() throws Exception {
        try {
            Task task = null;
            synchronized (tasks) {
                if (!tasks.isEmpty()) {
                    task = (Task) tasks.elementAt(0);
                    tasks.removeElement(task);
                }
            }
            return task;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when getAndRemoveNextTask in TaskPool.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    private class TaskRunnable implements Runnable {
        private TaskPool taskPool;
        private Boolean bisy = Boolean.FALSE;

        public TaskRunnable(TaskPool taskPool) {
            this.taskPool = taskPool;
        }

        public void pick() {
            synchronized (bisy) {
                if (bisy == Boolean.FALSE) {
                    bisy = Boolean.TRUE;
                    new Thread(this).start();
                }
            }
        }

        public void run() {
            try {
                Task task = null;
                for (; (task = taskPool.getAndRemoveNextTask()) != null;) {
                    try {
                        task.doTask();
                    } catch (Throwable t) {
                        StringBuffer message = new StringBuffer();
                        message.append("Error when Task.doTask() in TaskRunner in TaskPool.");
                        message.append("\n\t").append(t.toString());
                        Log.p(message.toString(), Log.ERROR);
                    }
                }
                synchronized (bisy) {
                    bisy = Boolean.FALSE;
                }
            } catch (Throwable t) {
                StringBuffer message = new StringBuffer();
                message.append("Error when run in TaskRunner in TaskPool.");
                message.append("\n\t").append(t.toString());
                Log.p(message.toString(), Log.ERROR);
            }
        }
    }

}
