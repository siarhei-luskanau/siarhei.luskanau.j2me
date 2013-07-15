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

package siarhei.luskanau.j2me.map.component;

import java.util.Enumeration;
import java.util.Vector;

import siarhei.luskanau.j2me.core.app.BaseReg;
import siarhei.luskanau.j2me.map.app.AppMapReg;
import siarhei.luskanau.j2me.map.dao.MapsDao;
import siarhei.luskanau.j2me.map.dao.MemoryMapsDao;
import siarhei.luskanau.j2me.map.dao.SimpleMapsDao;
import siarhei.luskanau.j2me.map.dao.WebMapsDao;
import siarhei.luskanau.j2me.map.dao.storage.FileMapsDao;
import siarhei.luskanau.j2me.map.dao.storage.JarMapsDao;
import siarhei.luskanau.j2me.map.engine.MapEngine;
import siarhei.luskanau.j2me.map.entity.LlzCoord;
import siarhei.luskanau.j2me.map.entity.Map;
import siarhei.luskanau.j2me.map.entity.XyzCoord;
import siarhei.luskanau.j2me.map.task.MapUpdate;
import siarhei.luskanau.j2me.map.task.StorageLoadMapsTask;
import siarhei.luskanau.j2me.map.task.WebLoadMapsTask;
import siarhei.luskanau.j2me.map.taskpool.Task;

import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.geom.Dimension;
import com.sun.lwuit.io.util.Log;
import com.sun.lwuit.layouts.CoordinateLayout;
import com.sun.lwuit.plaf.Style;

/**
 * @author <a href="mailto:siarhei.luskanau@gmail.com">Siarhei Luskanau</a>
 */
public class MapComponent implements MapUpdate {

    private Container container;
    private MapEngine mapEngine;
    private Vector mapsDaoStack;
    private int step = 50;
    private LlzCoord center;
    private Vector tiles = new Vector();

    public MapComponent(MapEngine mapEngine, Vector mapsDaoStack) throws Exception {
        try {
            this.mapEngine = mapEngine;
            this.mapsDaoStack = mapsDaoStack;
            container = new Container();
            Style style = new Style();
            style.setMargin(0, 0, 0, 0);
            style.setPadding(0, 0, 0, 0);
            style.setBgTransparency(0);
            container.setUnselectedStyle(style);
            container.setSelectedStyle(style);
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when create MapComponent.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public XyzCoord getXyzCoordForUpdate() throws Exception {
        try {
            for (Enumeration en = tiles.elements(); en.hasMoreElements();) {
                Tile tile = (Tile) en.nextElement();
                if (tile.isNeedUpdate()) {
                    return tile.getXyzCoord();
                }
            }
            return null;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when getMapForUpdate in MapComponent.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public void onMapUpdate(Map map) throws Exception {
        try {
            String name = map.getName();
            if (map.getData() != null) {
                byte[] data = map.getData();
                Image image = Image.createImage(data, 0, data.length);
                for (Enumeration en = tiles.elements(); en.hasMoreElements();) {
                    Tile tile = (Tile) en.nextElement();
                    if (name.equals(tile.getName())) {
                        tile.setMap(image);
                    }
                }
            }
            container.getComponentForm().repaint();
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when onMapUpdate in MapComponent.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public void onMapAbsent(String name) throws Exception {
        try {
            for (Enumeration en = tiles.elements(); en.hasMoreElements();) {
                Tile tile = (Tile) en.nextElement();
                if (name.equals(tile.getName())) {
                    tile.setAbsentText();
                }
            }
            container.getComponentForm().repaint();
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when onMapAbsent in MapComponent.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public void setSize(int width, int height) throws Exception {
        try {
            container.setLayout(new CoordinateLayout(width, height));
            container.setPreferredW(width);
            container.setPreferredH(height);
            step = Math.min(width, height) / 4;
            int tilesCountY = (height / 256) + 1 + (height % 256 > 0 ? 1 : 0);
            int tilesCountX = (width / 256) + 1 + (width % 256 > 0 ? 1 : 0);
            int maxSize = 2 * tilesCountY * tilesCountX;
            for (int i = 0; i < mapsDaoStack.size(); i++) {
                MapsDao mapsDao = (MapsDao) mapsDaoStack.elementAt(i);
                if (mapsDao instanceof MemoryMapsDao) {
                    MemoryMapsDao memoryMapsDao = (MemoryMapsDao) mapsDao;
                    memoryMapsDao.setMaxSize(maxSize);
                }
            }
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when calculateSize in MapComponent.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public LlzCoord getCurrentLlzCoord() throws Exception {
        try {
            LlzCoord llzCoord = new LlzCoord();
            llzCoord.setZoom(center.getZoom());
            llzCoord.setLatitude(center.getLatitude());
            llzCoord.setLongitude(center.getLongitude());
            return llzCoord;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when getCurrentLlzCoord in MapComponent.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public LlzCoord moveLeft() throws Exception {
        try {
            LlzCoord llzCoord = new LlzCoord();
            int zoom = center.getZoom();
            llzCoord.setZoom(zoom);
            llzCoord.setLatitude(center.getLatitude());
            int pxCenter = mapEngine.longitude2px(center.getLongitude(), zoom);
            int x = mapEngine.correctPx(pxCenter - step, zoom);
            llzCoord.setLongitude(mapEngine.px2longitude(x, zoom));
            return llzCoord;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when moveLeft in MapComponent.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public LlzCoord moveRight() throws Exception {
        try {
            LlzCoord llzCoord = new LlzCoord();
            int zoom = center.getZoom();
            llzCoord.setZoom(zoom);
            llzCoord.setLatitude(center.getLatitude());
            int pxCenter = mapEngine.longitude2px(center.getLongitude(), zoom);
            int x = mapEngine.correctPx(pxCenter + step, zoom);
            llzCoord.setLongitude(mapEngine.px2longitude(x, zoom));
            return llzCoord;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when moveRight in MapComponent.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public LlzCoord moveUp() throws Exception {
        try {
            LlzCoord llzCoord = new LlzCoord();
            int zoom = center.getZoom();
            llzCoord.setZoom(zoom);
            llzCoord.setLongitude(center.getLongitude());
            int pyCenter = mapEngine.latitude2px(center.getLatitude(), zoom);
            int y = pyCenter - step;
            if (y < 0) {
                y = 0;
            }
            llzCoord.setLatitude(mapEngine.px2latitude(y, zoom));
            return llzCoord;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when moveUp in MapComponent.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public LlzCoord moveDown() throws Exception {
        try {
            LlzCoord llzCoord = new LlzCoord();
            int zoom = center.getZoom();
            llzCoord.setZoom(zoom);
            llzCoord.setLongitude(center.getLongitude());
            int scale = 1 << (zoom + 8);
            int pyCenter = mapEngine.latitude2px(center.getLatitude(), zoom);
            int y = pyCenter + step;
            if (y >= scale) {
                y = scale;
            }
            llzCoord.setLatitude(mapEngine.px2latitude(y, zoom));
            return llzCoord;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when moveDown in MapComponent.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public void recalculate(LlzCoord center) throws Exception {
        try {
            this.center = center;
            int zoom = center.getZoom();
            zoom = center.getZoom();
            int scale = 1 << zoom;
            int scale8 = 1 << zoom + 8;
            int pxCenter = mapEngine.longitude2px(center.getLongitude(), zoom);
            int pyCenter = mapEngine.latitude2px(center.getLatitude(), zoom);
            int width = container.getPreferredW();
            int height = container.getPreferredH();
            int pxBase = pxCenter - width / 2;
            if (pxBase < 0) {
                for (; pxBase < 0;) {
                    pxBase = pxBase + scale8;
                }
            }
            int pyBase = pyCenter - height / 2;

            tiles = new Vector();
            container.removeAll();

            // clear used height of screen
            int pyUsed = 0;
            if (pyBase < 0) {
                pyUsed = -pyBase;
                for (; pyBase < 0;) {
                    pyBase = pyBase + scale8;
                }
            }
            // for rows of screen
            for (; pyUsed < height;) {
                // "y" position of tile
                int yCurrent = (pyBase + pyUsed) / 256;
                // "y" shift in pixel (relate tile)
                int dyCurrent = (pyBase + pyUsed) % 256;
                // height of visible tile part
                int tileHeight = 256 - dyCurrent;
                tileHeight = Math.min(tileHeight, height - pyUsed);

                // clear used weight of screen
                int pxUsed = 0;
                // for columns of screen
                for (; pxUsed < width;) {
                    // "x" position of tile
                    int xCurrent = (pxBase + pxUsed) / 256;
                    // "x" shift in pixel (relate tile)
                    int dxCurrent = (pxBase + pxUsed) % 256;
                    // weight of visible tile part
                    int tileWidth = 256 - dxCurrent;
                    tileWidth = Math.min(tileWidth, width - pxUsed);

                    // XyzCoord position of tile
                    XyzCoord xyzCoord = new XyzCoord(xCurrent, yCurrent, zoom);
                    xyzCoord = mapEngine.correctXyzCoord(xyzCoord);

                    String tileName = mapEngine.getMapsName(xyzCoord);
                    Tile tile = new Tile(xyzCoord, tileName);
                    tiles.addElement(tile);
                    Label label = tile.getLabel();
                    tile.setImageSize(dxCurrent, dyCurrent, tileWidth, tileHeight);
                    label.setX(pxUsed);
                    label.setY(pyUsed);
                    container.addComponent(label);

                    // increase used screen weight
                    pxUsed = pxUsed + tileWidth;
                }
                // increase used screen height
                pyUsed = pyUsed + tileHeight;
                if (yCurrent >= (scale - 1)) {
                    break;
                }
            }

            // refresh tile in Labels
            for (Enumeration en = tiles.elements(); en.hasMoreElements();) {
                Tile tile = (Tile) en.nextElement();
                boolean absent = true;
                if (tile.isNeedUpdate()) {
                    XyzCoord xyzCoord = tile.getXyzCoord();
                    for (int i = 0; i < mapsDaoStack.size(); i++) {
                        MapsDao mapsDao = (MapsDao) mapsDaoStack.elementAt(i);
                        Map map = null;
                        if (mapsDao instanceof MemoryMapsDao || mapsDao instanceof JarMapsDao
                                || mapsDao instanceof SimpleMapsDao) {
                            try {
                                map = mapsDao.getMap(xyzCoord, mapEngine);
                            } catch (Throwable t) {
                                StringBuffer message = new StringBuffer();
                                message.append("Error when getMap in recalculate in MapComponent.");
                                message.append("\n\t").append(t.toString());
                                Log.p(message.toString(), Log.ERROR);
                            }
                            if (map != null) {
                                absent = false;
                                onMapUpdate(map);
                                break;
                            }
                        } else {
                            if (mapsDao instanceof FileMapsDao) {
                                Task task = new StorageLoadMapsTask(null, mapEngine, mapsDaoStack, i);
                                ((AppMapReg) BaseReg.instance()).getFilePool().addTask(task);
                                absent = false;
                                break;
                            } else if (mapsDao instanceof WebMapsDao) {
                                Task task = new WebLoadMapsTask(null, mapEngine, mapsDaoStack, i);
                                ((AppMapReg) BaseReg.instance()).getWebPool().addTask(task);
                                absent = false;
                                break;
                            }
                        }
                    }
                }
                if (absent) {
                    Label label = tile.getLabel();
                    label.setText("---");
                }
            }
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when recalculate in MapComponent.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public Component getComponent() {
        return container;
    }

    public MapEngine getMapEngine() {
        return mapEngine;
    }

    private class Tile {
        private String name;
        private XyzCoord xyzCoord;
        private Label label = new Label();
        private int fromX;
        private int fromY;
        private int height;
        private int width;
        private boolean needUpdate = true;

        public Tile(XyzCoord xyzCoord, String name) throws Exception {
            try {
                this.xyzCoord = xyzCoord;
                this.name = name;
                Style style = new Style();
                style.setMargin(0, 0, 0, 0);
                style.setPadding(0, 0, 0, 0);
                style.setBgTransparency(0);
                // style.setBorder(Border.createLineBorder(1));
                label.setUnselectedStyle(style);
                label.setSelectedStyle(style);
                label.setAlignment(Label.CENTER);
                label.setVerticalAlignment(Label.CENTER);
            } catch (Throwable t) {
                StringBuffer message = new StringBuffer();
                message.append("Error when create Tile in MapComponent.");
                message.append("\n\t").append(t.toString());
                throw new Exception(message.toString());
            }
        }

        public boolean isNeedUpdate() {
            return needUpdate;
        }

        public void setImageSize(int fromX, int fromY, int width, int height) throws Exception {
            try {
                if (height > 0 && width > 0) {
                    this.fromX = fromX;
                    this.fromY = fromY;
                    this.width = width;
                    this.height = height;
                    label.setPreferredSize(new Dimension(width, height));
                }
            } catch (Throwable t) {
                StringBuffer message = new StringBuffer();
                message.append("Error when setImageSize in Tile in MapComponent.");
                message.append("\n\t").append(t.toString());
                throw new Exception(message.toString());
            }
        }

        public void setMap(Image image) throws Exception {
            try {
                needUpdate = false;
                subImage(image);
            } catch (Throwable t) {
                StringBuffer message = new StringBuffer();
                message.append("Error when setImageSize in Tile in MapComponent.");
                message.append("\n\t").append(t.toString());
                throw new Exception(message.toString());
            }
        }

        public void setAbsentText() throws Exception {
            try {
                needUpdate = false;
                label.setText("---");
            } catch (Throwable t) {
                StringBuffer message = new StringBuffer();
                message.append("Error when setAbsentText in Tile in MapComponent.");
                message.append("\n\t").append(t.toString());
                throw new Exception(message.toString());
            }
        }

        private void subImage(Image image) throws Exception {
            try {
                label.setIcon(null);
                if (image != null && height > 0 && width > 0) {
                    try {
                        label.setIcon(image.subImage(fromX, fromY, width, height, false));
                    } catch (Throwable t) {
                        String message = t.getClass().getName();
                        int dilim = message.lastIndexOf('.');
                        message = message.substring(dilim + 1, message.length());
                        label.setText(message);
                    }
                }
            } catch (Throwable t) {
                StringBuffer message = new StringBuffer();
                message.append("Error when subImage in Tile in MapComponent.");
                message.append("\n\t").append(t.toString());
                throw new Exception(message.toString());
            }
        }

        public Label getLabel() {
            return label;
        }

        public String getName() {
            return name;
        }

        public XyzCoord getXyzCoord() {
            return xyzCoord;
        }

    }

}
