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

package siarhei.luskanau.j2me.map.form;

import siarhei.luskanau.j2me.map.entity.LlzCoord;

import com.sun.lwuit.Form;

/**
 * @author <a href="mailto:siarhei.luskanau@gmail.com">Siarhei Luskanau</a>
 */
public abstract class BaseMapsForm extends Form {

    protected int getZoom() throws Exception {
        return 9;
    }

    protected void setZoom(int zoom) throws Exception {
    }

    protected LlzCoord getLlzCoord() throws Exception {
        return new LlzCoord(0, 0, getZoom());
    }

    protected void setLlzCoord(LlzCoord llzCoord) throws Exception {
    }

    protected void onBack() throws Exception {
    }

    protected void onZoomIn() throws Exception {
    }

    protected void onZoomOut() throws Exception {
    }

    protected void onLeft() throws Exception {
    }

    protected void onRigth() throws Exception {
    }

    protected void onUp() throws Exception {
    }

    protected void onDown() throws Exception {
    }

}
