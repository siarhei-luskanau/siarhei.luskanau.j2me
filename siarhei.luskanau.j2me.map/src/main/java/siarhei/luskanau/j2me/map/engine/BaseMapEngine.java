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

package siarhei.luskanau.j2me.map.engine;

import akme.mobile.util.MathUtil;

/**
 * @author <a href="mailto:siarhei.luskanau@gmail.com">Siarhei Luskanau</a>
 */
public abstract class BaseMapEngine extends MapEngine {

    public int latitude2px(double latitude, int zoom) throws Exception {
        try {
            int scale = 1 << (zoom + 8);
            double coefficient = 0.5 - ((MathUtil.log(Math.tan((Math.PI / 4)
                    + (Math.toRadians(0.5 * latitude)))) / Math.PI) / 2.0);
            int px = (int) (scale * coefficient);
            return px;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when latitude2px in BaseMapEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public double px2latitude(int px, int zoom) throws Exception {
        try {
            int scale = 1 << (zoom + 8);

            double coef = (double) px / (double) scale;
            coef = 1 - 2 * coef;
            double latitude = (2 * MathUtil.atan(MathUtil.exp(Math.PI * coef))) - (Math.PI / 2);
            latitude *= (180 / Math.PI);
            return latitude;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when px2latitude in BaseMapEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

}
