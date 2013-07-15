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

package siarhei.luskanau.j2me.maps.form;

import siarhei.luskanau.j2me.core.utils.LogActionListener;

import com.sun.lwuit.Button;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.layouts.BoxLayout;

/**
 * @author <a href="mailto:siarhei.luskanau@gmail.com">Siarhei Luskanau</a>
 */
public class AboutForm extends BaseForm {

    public AboutForm() throws Exception {
        try {
            setLayout(new BoxLayout(BoxLayout.Y_AXIS));
            setTitle("О программе");

            TextArea textArea = new TextArea();
            textArea.setText("Author:\nSiarhei Luskanau");
            textArea.setEditable(false);
            addComponent(textArea);

            Button backButton = new Button("Назад");
            backButton.setAlignment(CENTER);
            backButton.addActionListener(new LogActionListener(
                    "Error when actionPerformed backButton in AboutForm.") {
                public void doAction(ActionEvent evt) throws Exception {
                    new MainForm().show();
                }
            });
            addComponent(backButton);

        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when create AboutForm.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

}
