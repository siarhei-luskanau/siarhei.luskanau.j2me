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

import java.util.Hashtable;

/**
 * @author <a href="mailto:siarhei.luskanau@gmail.com">Siarhei Luskanau</a>
 */
public class L10n {

    static private L10n l10n;

    private Hashtable l10nHashtable = new Hashtable();

    private String defaultLocale = "";

    static public L10n instance() {
        if (l10n == null) {
            l10n = new L10n();
        }
        return l10n;
    }

    private L10n() {
    }

    static public String string(String key) {
        return l10n.get(key);
    }

    private Hashtable getLocale(String locale) {
        return (Hashtable) l10nHashtable.get(locale);
    }

    private void setLocale(String locale, Hashtable hashtable) {
        l10nHashtable.put(locale, hashtable);
    }

    public void set(String key, String value) {
        set(defaultLocale, key, value);
    }

    public void set(String locale, String key, String value) {
        Hashtable hashtable = getLocale(locale);
        if (hashtable == null) {
            hashtable = new Hashtable();
            setLocale(locale, hashtable);
        }
        hashtable.put(key, value);
    }

    public String get(String key) {
        return get(defaultLocale, key);
    }

    public String get(String locale, String key) {
        return (String) getLocale(locale).get(key);
    }

}
