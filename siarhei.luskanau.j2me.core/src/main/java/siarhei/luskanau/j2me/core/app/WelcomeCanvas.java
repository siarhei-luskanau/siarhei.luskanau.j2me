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

package siarhei.luskanau.j2me.core.app;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.midlet.MIDlet;

import siarhei.luskanau.j2me.core.utils.StringTokenizer;

import com.sun.lwuit.geom.Dimension;

/**
 * Class for display a splash screen
 * 
 * @author <a href="mailto:siarhei.luskanau@gmail.com">Siarhei Luskanau</a>
 */
public class WelcomeCanvas extends Canvas {

    private MIDlet midlet;

    protected String logo;

    protected String author = "Siarhei Luskanau";

    private boolean hasError = false;

    private Font largeFont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_LARGE);

    private Font smallFont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);

    private String status = "Loading...";

    private double statusKoef = 0;

    public WelcomeCanvas(MIDlet midlet) {
        this.midlet = midlet;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    protected void paint(Graphics graphics) {
        try {
            // FullScreenMode
            setTitle(null);
            setFullScreenMode(true);
            // screen dimension
            Dimension screenSize = new Dimension(getWidth(), getHeight());

            // drawing background
            graphics.setColor(0xFFFFFF);
            graphics.fillRect(0, 0, screenSize.getWidth(), screenSize.getHeight());

            Dimension logoSize = getLogoSize();
            Dimension progressSize = getProgressSize(screenSize);
            Dimension statusSize = getStatusSize(screenSize);
            Dimension authorSize = getAuthorSize();

            // gap between components
            int gap = (screenSize.getHeight() - logoSize.getHeight() - statusSize.getHeight()
                    - progressSize.getHeight() - authorSize.getHeight())
                    / (4 + 1);

            // shift to drawing the component
            int shift = gap;

            // drawing Logo
            repaintLogo(graphics, shift, screenSize, logoSize);
            shift = shift + logoSize.getHeight() + gap;

            // drawing Author
            repaintAuthor(graphics, shift, screenSize, authorSize);
            shift = shift + authorSize.getHeight() + gap;

            // drawing Status
            repaintStatus(graphics, shift, screenSize, statusSize);
            shift = shift + statusSize.getHeight() + gap;

            // drawing Progress
            repaintProgress(graphics, shift, screenSize, progressSize);
            shift = shift + progressSize.getHeight() + gap;

        } catch (Throwable t) {
            setError(t);
        }
    }

    private void repaintLogo(Graphics graphics, int shift, Dimension screenSize, Dimension cmpSize) {
        graphics.setColor(0x000000);
        graphics.setFont(largeFont);
        graphics.drawString(logo, screenSize.getWidth() / 2 - cmpSize.getWidth() / 2, shift, Graphics.LEFT
                | Graphics.TOP);
    }

    private void repaintProgress(Graphics graphics, int shift, Dimension screenSize, Dimension cmpSize) {
        int hShift = 2;
        int vShift = 0;

        graphics.setColor(0xEEEEEE);
        graphics.fillRect(hShift, shift + vShift, screenSize.getWidth() - 2 * hShift, cmpSize.getHeight() - 2
                * vShift);

        hShift = hShift + 2;
        vShift = vShift + 2;
        graphics.setColor(0xAAAAAA);
        graphics.fillRect(hShift, shift + vShift, screenSize.getWidth() - 2 * hShift, cmpSize.getHeight() - 2
                * vShift);

        hShift = hShift + 2;
        vShift = vShift + 2;
        int statusShift = (int) ((screenSize.getWidth() - 2 * hShift) * (1 - statusKoef));
        graphics.setColor(0xFFFFFF);
        graphics.fillRect(hShift, shift + vShift, screenSize.getWidth() - 2 * hShift, cmpSize.getHeight() - 2
                * vShift);
        graphics.setColor(0x000000);
        graphics.fillRect(hShift, shift + vShift, screenSize.getWidth() - 2 * hShift - statusShift,
                cmpSize.getHeight() - 2 * vShift);

        // statusKoef

    }

    private void repaintStatus(Graphics graphics, int shift, Dimension screenSize, Dimension cmpSize) {
        graphics.setColor(0x000000);
        graphics.setFont(smallFont);
        StringTokenizer st = new StringTokenizer(status, "\n");
        for (int i = 0; st.hasMoreElements(); i++) {
            String string = (String) st.nextElement();
            graphics.drawString(string, screenSize.getWidth() / 2 - smallFont.stringWidth(string) / 2, shift
                    + (smallFont.getHeight() + 3) * (i - 1) + 20, Graphics.LEFT | Graphics.TOP);
        }
    }

    private void repaintAuthor(Graphics graphics, int shift, Dimension screenSize, Dimension cmpSize) {
        graphics.setColor(0x000000);
        graphics.setFont(smallFont);
        graphics.drawString(author, screenSize.getWidth() / 2 - cmpSize.getWidth() / 2, shift, Graphics.LEFT
                | Graphics.TOP);
    }

    private Dimension getLogoSize() {
        return new Dimension(largeFont.stringWidth(logo), largeFont.getHeight());
    }

    private Dimension getProgressSize(Dimension screenSize) {
        return new Dimension(screenSize.getWidth(), 10);
    }

    private Dimension getStatusSize(Dimension screenSize) {
        int cnt = new StringTokenizer(status, "\n").size();
        return new Dimension(0, smallFont.getHeight() * cnt + 3 * (cnt - 1));
    }

    private Dimension getAuthorSize() {
        return new Dimension(smallFont.stringWidth(author), smallFont.getHeight());
    }

    public WelcomeCanvas setStatus(String status) {
        this.status = status;
        repaint();
        return this;
    }

    public WelcomeCanvas setStatusKoef(double statusKoef) {
        this.statusKoef = statusKoef;
        repaint();
        return this;
    }

    public WelcomeCanvas setError(Throwable t) {
        hasError = true;
        status = status + "\n" + "Критическая ошика!" + "\n" + t.toString();
        t.printStackTrace();
        repaint();
        return this;
    }

    protected void pointerPressed(int x, int y) {
        if (hasError) {
            midlet.notifyDestroyed();
        }
    }

    protected void keyPressed(int keyCode) {
        if (hasError) {
            midlet.notifyDestroyed();
        }
    }

}
