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

package siarhei.luskanau.j2me.core.storage;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * @author <a href="mailto:siarhei.luskanau@gmail.com">Siarhei Luskanau</a>
 */
public class Index {

    private Hashtable keys = new Hashtable();

    private Hashtable positions = new Hashtable();

    public void add(String key, String value, boolean unique, long position) throws Exception {
        try {
            Long positionLong = new Long(position);

            Hashtable keyHashtable = getKeyHashtable(key);

            if (unique) {
                Object o = keyHashtable.get(value);
                if (o instanceof Long) {
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("Object with unique key already exist!");
                    buffer.append(" Key:").append(key);
                    buffer.append(" Value:").append(value);
                    throw new RuntimeException(buffer.toString());
                }
                keyHashtable.put(value, positionLong);
            } else {
                Vector vectorValues = (Vector) keyHashtable.get(value);
                if (vectorValues == null) {
                    vectorValues = new Vector();
                    keyHashtable.put(value, vectorValues);
                }
                vectorValues.addElement(positionLong);
            }

            Vector linkVector = getLinksToPosition(positionLong);
            String[] link = { key, value };
            linkVector.addElement(link);
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when add in Index.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public void delete(long position) throws Exception {
        try {
            Long positionLong = new Long(position);
            Vector linkVector = getLinksToPosition(positionLong);
            positions.remove(positionLong);

            for (Enumeration en = linkVector.elements(); en.hasMoreElements();) {
                String[] link = (String[]) en.nextElement();
                String key = link[0];
                String value = link[1];
                Hashtable keyHashtable = getKeyHashtable(key);
                Object o = keyHashtable.get(value);
                if (o instanceof Long) {
                    keyHashtable.remove(value);
                } else {
                    Vector vectorValues = (Vector) keyHashtable.get(value);
                    vectorValues.removeElement(positionLong);
                    if (vectorValues.size() == 0) {
                        keyHashtable.remove(value);
                    }
                }
            }
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when remove in Index.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public Enumeration findEnumeration(String key, String value) throws Exception {
        try {
            Hashtable keyHashtable = getKeyHashtable(key);
            Object o = keyHashtable.get(value);
            if (o == null) {
                return null;
            } else if (o instanceof Vector) {
                Vector vector = (Vector) o;
                return vector.elements();
            } else {
                throw new RuntimeException("Is unique!");
            }
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when findEnumeration in Index.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public long findUnique(String key, String value) throws Exception {
        try {
            Hashtable keyHashtable = getKeyHashtable(key);
            Object o = keyHashtable.get(value);
            if (o != null) {
                if (o instanceof Long) {
                    return ((Long) o).longValue();
                } else {
                    throw new RuntimeException("Not unique!");
                }
            }
            return -1;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when findUnique in Index.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public boolean exist(String key, String value, boolean unique) throws Exception {
        try {
            throw new Exception("Not Implemented");
            /*
             * Hashtable keyHashtable = getKeyHashtable(key); return keyHashtable.containsKey(key);
             */
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when exist in Index.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    private Vector getLinksToPosition(Long positionLong) throws Exception {
        try {
            Vector linkVector = (Vector) positions.get(positionLong);
            if (linkVector == null) {
                linkVector = new Vector();
                positions.put(positionLong, linkVector);
            }
            return linkVector;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when getPositionHashtable in Index.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public Hashtable getKeyHashtable(String key) throws Exception {
        try {
            Hashtable hashtable = (Hashtable) keys.get(key);
            if (hashtable == null) {
                hashtable = new Hashtable();
                keys.put(key, hashtable);
            }
            return hashtable;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when getKeyHashtable in Index.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public void save(OutputStream outputStream, String indexName) throws Exception {
        try {
            DataOutput dataOutput = new DataOutputStream(outputStream);
            for (Enumeration enKeys = keys.keys(); enKeys.hasMoreElements();) {
                String key = (String) enKeys.nextElement();
                Hashtable keyHashtable = getKeyHashtable(key);
                for (Enumeration enValue = keyHashtable.keys(); enValue.hasMoreElements();) {
                    String value = (String) enValue.nextElement();
                    Object o = keyHashtable.get(value);
                    if (o instanceof Long) {
                        Long linkLong = (Long) o;
                        writeIndexItem(dataOutput, indexName, true, key, value, linkLong.longValue());
                    } else if (o instanceof Vector) {
                        Vector linkVector = (Vector) o;
                        for (Enumeration enLinkVector = linkVector.elements(); enLinkVector.hasMoreElements();) {
                            Long linkLong = (Long) enLinkVector.nextElement();
                            writeIndexItem(dataOutput, indexName, false, key, value, linkLong.longValue());
                        }
                    }
                }
            }
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when save in Index.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    private void writeIndexItem(DataOutput dataOutput, String indexName, boolean unique, String key,
            String value, long position) throws Exception {
        try {
            dataOutput.writeUTF(indexName);
            dataOutput.writeBoolean(unique);
            dataOutput.writeUTF(key);
            dataOutput.writeUTF(value);
            dataOutput.writeLong(position);
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when writeIndexItem in Index.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

}
