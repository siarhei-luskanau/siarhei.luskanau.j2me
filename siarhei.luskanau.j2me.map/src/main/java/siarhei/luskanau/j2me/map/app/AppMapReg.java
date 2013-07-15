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

package siarhei.luskanau.j2me.map.app;

import siarhei.luskanau.j2me.core.app.BaseReg;
import siarhei.luskanau.j2me.map.taskpool.FilePool;
import siarhei.luskanau.j2me.map.taskpool.WebPool;

import com.sun.lwuit.Form;

/**
 * @author <a href="mailto:siarhei.luskanau@gmail.com">Siarhei Luskanau</a>
 */
public abstract class AppMapReg extends BaseReg {

    private Form currentForm;

    private WebPool webPool;

    private FilePool filePool;

    public WebPool getWebPool() {
        return webPool;
    }

    public void setWebPool(WebPool webPool) {
        this.webPool = webPool;
    }

    public FilePool getFilePool() {
        return filePool;
    }

    public void setFilePool(FilePool filePool) {
        this.filePool = filePool;
    }

    public Form getCurrentForm() {
        return currentForm;
    }

    public void setCurrentForm(Form currentForm) {
        this.currentForm = currentForm;
    }

}
