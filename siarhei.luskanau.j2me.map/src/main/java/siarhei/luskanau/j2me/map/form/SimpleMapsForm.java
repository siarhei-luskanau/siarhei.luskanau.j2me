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

import java.util.Vector;

import siarhei.luskanau.j2me.core.utils.LogActionListener;
import siarhei.luskanau.j2me.map.component.MapComponent;
import siarhei.luskanau.j2me.map.engine.MapEngine;
import siarhei.luskanau.j2me.map.entity.LlzCoord;
import siarhei.luskanau.j2me.map.entity.Map;
import siarhei.luskanau.j2me.map.entity.XyzCoord;
import siarhei.luskanau.j2me.map.task.MapUpdate;

import com.sun.lwuit.Button;
import com.sun.lwuit.Container;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.geom.Dimension;
import com.sun.lwuit.io.util.Log;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.layouts.CoordinateLayout;
import com.sun.lwuit.plaf.Border;
import com.sun.lwuit.plaf.Style;

/**
 * @author <a href="mailto:siarhei.luskanau@gmail.com">Siarhei Luskanau</a>
 */
public abstract class SimpleMapsForm extends BaseMapsForm implements MapUpdate {

    protected MapComponent mapComponent;

    protected Container controlContainer;

    protected Container mapInfo;

    public SimpleMapsForm() throws Exception {
        try {
            Log.p("SimpleMapsForm");
            revalidate();
            int width = getContentPane().getWidth();
            int height = getContentPane().getHeight();
            setLayout(new CoordinateLayout(width, height));
            setupMapEngineAndStack();
            mapComponent = new MapComponent(getMapEngine(), getMapsDaoStack());
            mapComponent.setSize(width, height);
            addComponent(mapComponent.getComponent());

            mapInfo = getMapInfo(width, height);
            if (mapInfo != null) {
                addComponent(mapInfo);
            }

            controlContainer = getControlContainer(width, height);
            if (controlContainer != null) {
                addComponent(controlContainer);
            }
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when create SimpleMapsForm.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    protected abstract void setupMapEngineAndStack() throws Exception;

    protected abstract Vector getMapsDaoStack() throws Exception;

    protected abstract MapEngine getMapEngine() throws Exception;

    protected void onShowCompleted() {
        new LogActionListener("Error when onShowCompleted in SimpleMapsForm.") {
            protected void doAction(ActionEvent evt) throws Exception {
                recalculate(getLlzCoord());
            }
        }.actionPerformed(null);
    }

    protected Container getMapInfo(int width, int height) throws Exception {
        try {
            Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
            container.getStyle().setBgTransparency(100);
            return container;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when getMapInfo in BaseAppMapsForm.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    protected void updateMapInfo(LlzCoord center) throws Exception {
        try {
            mapInfo.removeAll();
            if (mapInfo != null && center != null) {
                mapInfo.addComponent(new Label("Lon: " + center.getLongitude()));
                mapInfo.addComponent(new Label("Lat: " + center.getLatitude()));
                mapInfo.addComponent(new Label("Zoom: " + center.getZoom()));
            }
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when updateMapInfo in BaseAppMapsForm.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    protected boolean hasNavigateButton() {
        return false;
    }

    protected Container getControlContainer(int width, int height) throws Exception {
        try {
            Container container = new Container(new BorderLayout());
            container.setPreferredSize(new Dimension(width, height));
            Container bottomContainer = new Container();

            if (hasNavigateButton()) {
                Button leftButton = new Button(Image.createImage("/left_arrow.png"));
                setButtonSize(leftButton);
                leftButton.addActionListener(new LogActionListener(
                        "Error when actionPerformed leftButton in SimpleMapsForm.") {
                    public void doAction(ActionEvent evt) throws Exception {
                        onLeft();
                    }
                });
                bottomContainer.addComponent(leftButton);

                Button rightButton = new Button(Image.createImage("/right_arrow.png"));
                setButtonSize(rightButton);
                rightButton.addActionListener(new LogActionListener(
                        "Error when actionPerformed rightButton in SimpleMapsForm.") {
                    public void doAction(ActionEvent evt) throws Exception {
                        onRigth();
                    }
                });
                bottomContainer.addComponent(rightButton);

                Button upButton = new Button(Image.createImage("/up_arrow.png"));
                setButtonSize(upButton);
                upButton.addActionListener(new LogActionListener(
                        "Error when actionPerformed upButton in SimpleMapsForm.") {
                    public void doAction(ActionEvent evt) throws Exception {
                        onUp();
                    }
                });
                bottomContainer.addComponent(upButton);

                Button downButton = new Button(Image.createImage("/down_arrow.png"));
                setButtonSize(downButton);
                downButton.addActionListener(new LogActionListener(
                        "Error when actionPerformed downButton in SimpleMapsForm.") {
                    public void doAction(ActionEvent evt) throws Exception {
                        onDown();
                    }
                });
                bottomContainer.addComponent(downButton);
            }

            Button zoomInButton = new Button(Image.createImage("/zoom_in.png"));
            setButtonSize(zoomInButton);
            zoomInButton.addActionListener(new LogActionListener(
                    "Error when actionPerformed zoomInButton in SimpleMapsForm.") {
                public void doAction(ActionEvent evt) throws Exception {
                    onZoomIn();
                }
            });
            bottomContainer.addComponent(zoomInButton);

            Button zoomOutButton = new Button(Image.createImage("/zoom_out.png"));
            setButtonSize(zoomOutButton);
            zoomOutButton.addActionListener(new LogActionListener(
                    "Error when actionPerformed zoomOutButton in SimpleMapsForm.") {
                public void doAction(ActionEvent evt) throws Exception {
                    onZoomOut();
                }
            });
            bottomContainer.addComponent(zoomOutButton);

            Button backButton = new Button(Image.createImage("/exit.png"));
            setButtonSize(backButton);
            backButton.addActionListener(new LogActionListener(
                    "Error when actionPerformed backButton in SimpleMapsForm.") {
                public void doAction(ActionEvent evt) throws Exception {
                    onBack();
                }
            });
            bottomContainer.addComponent(backButton);

            container.addComponent(BorderLayout.SOUTH, bottomContainer);
            return container;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when getControlContainer in BaseAppMapsForm.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    protected void setButtonSize(Button button) {
        Style style = button.getStyle();
        style.setBgTransparency(0);
        style.setPadding(0, 0, 0, 0);
        style.setBorder(Border.getDefaultBorder());

        Style selectedStyle = button.getSelectedStyle();
        selectedStyle.setPadding(0, 0, 0, 0);
        selectedStyle.setBorder(Border.getDefaultBorder());

        Style pressedStyle = button.getPressedStyle();
        pressedStyle.setPadding(0, 0, 0, 0);
        pressedStyle.setBorder(Border.getDefaultBorder());
    }

    public XyzCoord getXyzCoordForUpdate() throws Exception {
        return mapComponent.getXyzCoordForUpdate();
    }

    public void onMapUpdate(Map map) throws Exception {
        mapComponent.onMapUpdate(map);
    }

    public void onMapAbsent(String name) throws Exception {
        mapComponent.onMapAbsent(name);
    }

    protected void recalculate(LlzCoord center) throws Exception {
        try {
            updateMapInfo(center);
            mapComponent.recalculate(center);
            repaint();
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when recalculate in BaseAppMapsForm.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    protected void onZoomIn() throws Exception {
        try {
            LlzCoord center = mapComponent.getCurrentLlzCoord();
            int zoom = center.getZoom();
            if (zoom < 17) {
                zoom = zoom + 1;
                center.setZoom(zoom);
                recalculate(center);
                setZoom(zoom);
            }
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when onZoomIn in BaseAppMapsForm.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    protected void onZoomOut() throws Exception {
        try {
            LlzCoord center = mapComponent.getCurrentLlzCoord();
            int zoom = center.getZoom();
            if (zoom > 0) {
                zoom = zoom - 1;
                center.setZoom(zoom);
                recalculate(center);
                setZoom(zoom);
            }
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when onZoomOut in BaseAppMapsForm.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    protected void onLeft() throws Exception {
        try {
            LlzCoord center = mapComponent.moveLeft();
            recalculate(center);
            setLlzCoord(center);
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when onLeft in BaseAppMapsForm.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    protected void onRigth() throws Exception {
        try {
            LlzCoord center = mapComponent.moveRight();
            recalculate(center);
            setLlzCoord(center);
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when onRigth in BaseAppMapsForm.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    protected void onUp() throws Exception {
        try {
            LlzCoord center = mapComponent.moveUp();
            recalculate(center);
            setLlzCoord(center);
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when onUp in BaseAppMapsForm.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    protected void onDown() throws Exception {
        try {
            LlzCoord center = mapComponent.moveDown();
            recalculate(center);
            setLlzCoord(center);
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when onDown in BaseAppMapsForm.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

}
