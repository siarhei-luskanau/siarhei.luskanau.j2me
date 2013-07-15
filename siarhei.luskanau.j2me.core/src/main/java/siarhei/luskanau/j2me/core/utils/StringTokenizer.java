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

package siarhei.luskanau.j2me.core.utils;

import java.util.Enumeration;
import java.util.Vector;

/**
 * @author <a href="mailto:siarhei.luskanau@gmail.com">Siarhei Luskanau</a>
 */
public class StringTokenizer implements Enumeration {

    private Vector vector;

    private Enumeration enumeration;

    public StringTokenizer(String parsedString, String delim) {
        try {
            vector = new Vector();
            int ind = 0;
            for (;;) {
                if ((ind = parsedString.indexOf(delim)) > 0) {
                    String subString = parsedString.substring(0, ind);
                    vector.addElement(subString);
                    parsedString = parsedString.substring(ind + delim.length());
                } else {
                    vector.addElement(parsedString);
                    break;
                }
            }
            enumeration = vector.elements();
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when create StringTokenizer.");
            message.append("\n\t").append(t.toString());
            throw new RuntimeException(message.toString());
        }
    }

    public boolean hasMoreElements() {
        try {
            return enumeration.hasMoreElements();
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when hasMoreElements in StringTokenizer.");
            message.append("\n\t").append(t.toString());
            throw new RuntimeException(message.toString());
        }
    }

    public Object nextElement() {
        try {
            return enumeration.nextElement();
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when hasMoreElements in StringTokenizer.");
            message.append("\n\t").append(t.toString());
            throw new RuntimeException(message.toString());
        }
    }

    public int size() {
        return vector.size();
    }

}
