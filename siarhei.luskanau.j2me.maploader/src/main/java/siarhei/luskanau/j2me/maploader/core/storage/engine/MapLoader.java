package siarhei.luskanau.j2me.maploader.core.storage.engine;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import siarhei.luskanau.j2me.map.dao.MapsDao;
import siarhei.luskanau.j2me.map.engine.GoogleMapEngine;
import siarhei.luskanau.j2me.map.engine.MapEngine;
import siarhei.luskanau.j2me.map.engine.OpenStreetMapEngine;
import siarhei.luskanau.j2me.map.entity.LlzCoord;
import siarhei.luskanau.j2me.map.entity.Map;
import siarhei.luskanau.j2me.map.entity.XyzCoord;

public class MapLoader {

    private static final String DIR_STRING = "maps";
    private static OpenStreetMapEngine openStreetMapEngine;
    private static GoogleMapEngine googleMapEngine;
    private static RandomAccessFileMapsDao mapsDao;

    public static void main(String[] args) {
        try {
            openStreetMapEngine = new OpenStreetMapEngine();
            googleMapEngine = new GoogleMapEngine();
            mapsDao = new RandomAccessFileMapsDao("");

            // loadRectangle("Berezki", new LlzCoord(52.431033,30.997038, 0), new
            // LlzCoord(52.383474,31.108117, 0));
            // loadRectangle("Gomel", new LlzCoord(52.551014,30.841942, 0), new LlzCoord(52.349246,31.051426,
            // 0));

            // loadRectangle("Petersburg", new LlzCoord(60.05569,30.195808, 0), new
            // LlzCoord(59.865952,30.489006, 0));

            // loadRectangle("Klinci", new LlzCoord(52.781201,32.197638, 0), new LlzCoord(52.737778,32.285357,
            // 0));
            // loadRectangle("Novozybkov", new LlzCoord(52.562432,31.89703, 0), new
            // LlzCoord(52.518791,31.981831, 0));

            // loadRectangle("Minsk", new LlzCoord(53.971698,27.402935, 0), new LlzCoord(53.831115,27.703686,
            // 0));

            // loadRectangle("Chernigov", new LlzCoord(51.561129,31.171246, 0), new
            // LlzCoord(51.46605,31.368656, 0));

            loadRectangle("Kiev", new LlzCoord(50.538876, 30.340004, 0), new LlzCoord(50.334636, 30.68882, 0));

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private static void loadRectangle(String areaName, LlzCoord llzCoord1, LlzCoord llzCoord2) {
        try {
            int current = 0;
            int total = 0;

            for (int zoom = 0; zoom <= 17; zoom++) {
                llzCoord1.setZoom(zoom);
                llzCoord2.setZoom(zoom);
                XyzCoord xyzCoord1 = openStreetMapEngine.toXyzCoord(llzCoord1);
                XyzCoord xyzCoord2 = openStreetMapEngine.toXyzCoord(llzCoord2);

                int minX = Math.min(xyzCoord1.getX(), xyzCoord2.getX());
                int minY = Math.min(xyzCoord1.getY(), xyzCoord2.getY());
                int maxX = Math.max(xyzCoord1.getX(), xyzCoord2.getX()) + 1;
                int maxY = Math.max(xyzCoord1.getY(), xyzCoord2.getY()) + 1;

                if (zoom < 11) {
                    minX = minX - 2;
                    minY = minY - 2;
                    maxX = maxX + 2;
                    maxY = maxY + 2;
                } else if (zoom < 15) {
                    minX = minX - 1;
                    minY = minY - 1;
                    maxX = maxX + 1;
                    maxY = maxY + 1;
                }

                for (int x = minX; x <= maxX; x++) {
                    for (int y = minY; y <= maxY; y++) {
                        total = total + 1;
                    }
                }
            }

            for (int zoom = 0; zoom <= 17; zoom++) {
                llzCoord1.setZoom(zoom);
                llzCoord2.setZoom(zoom);
                XyzCoord xyzCoord1 = openStreetMapEngine.toXyzCoord(llzCoord1);
                XyzCoord xyzCoord2 = openStreetMapEngine.toXyzCoord(llzCoord2);

                int minX = Math.min(xyzCoord1.getX(), xyzCoord2.getX());
                int minY = Math.min(xyzCoord1.getY(), xyzCoord2.getY());
                int maxX = Math.max(xyzCoord1.getX(), xyzCoord2.getX()) + 1;
                int maxY = Math.max(xyzCoord1.getY(), xyzCoord2.getY()) + 1;

                if (zoom < 11) {
                    minX = minX - 2;
                    minY = minY - 2;
                    maxX = maxX + 2;
                    maxY = maxY + 2;
                } else if (zoom < 15) {
                    minX = minX - 1;
                    minY = minY - 1;
                    maxX = maxX + 1;
                    maxY = maxY + 1;
                }

                for (int x = minX; x <= maxX; x++) {
                    for (int y = minY; y <= maxY; y++) {
                        current = current + 1;
                        StringBuffer buffer = new StringBuffer();
                        buffer.append(areaName).append(" ");
                        buffer.append(current).append("/").append(total).append(" ");
                        buffer.append(Math.round(((double) current / (double) total) * 100)).append("%");
                        System.out.println(buffer.toString());

                        XyzCoord xyzCoord = new XyzCoord(x, y, zoom);
                        xyzCoord = openStreetMapEngine.correctXyzCoord(xyzCoord);

                        storage2dir(mapsDao, openStreetMapEngine, openStreetMapEngine.MAPNIK_TYPE, xyzCoord);
                        loadTile(openStreetMapEngine, openStreetMapEngine.MAPNIK_TYPE, xyzCoord);
                        dir2storage(mapsDao, openStreetMapEngine, openStreetMapEngine.MAPNIK_TYPE, xyzCoord);

                        storage2dir(mapsDao, googleMapEngine, googleMapEngine.HYBRID_TYPE, xyzCoord);
                        loadTile(googleMapEngine, googleMapEngine.HYBRID_TYPE, xyzCoord);
                        // dir2storage(mapsDao, googleMapEngine, googleMapEngine.HYBRID_TYPE, xyzCoord);

                    }
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private static void storage2dir(MapsDao mapsDao, MapEngine mapEngine, String type, XyzCoord xyzCoord) {
        try {
            mapEngine.setMapType(type);
            String mapNameString = mapEngine.getMapsName(xyzCoord);
            String fileNameString = DIR_STRING + File.separator + mapNameString;
            Map map = mapsDao.getMap(xyzCoord, mapEngine);

            if (map == null) {
                return;
            }

            File file = new File(fileNameString);
            if (file.exists()) {
                return;
            }

            if (!new File(DIR_STRING).exists()) {
                if (!new File(DIR_STRING).mkdir()) {
                    new File(DIR_STRING).mkdirs();
                }
            }

            DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(
                    fileNameString)));
            if (map.getData() != null) {
                out.write(map.getData());
            }
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("resource")
    private static void dir2storage(MapsDao mapsDao, MapEngine mapEngine, String type, XyzCoord xyzCoord) {
        try {
            mapEngine.setMapType(type);
            String mapNameString = mapEngine.getMapsName(xyzCoord);
            String fileNameString = DIR_STRING + File.separator + mapNameString;

            if (mapsDao.getMap(xyzCoord, mapEngine) != null) {
                return;
            }

            File file = new File(fileNameString);
            if (!file.exists()) {
                return;
            }

            byte[] data = new byte[(int) file.length()];
            DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(
                    fileNameString)));
            in.read(data);

            Map map = new Map();
            map.setName(mapNameString);
            map.setData(data);
            mapsDao.setMap(map, xyzCoord, mapEngine);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void loadTile(MapEngine mapEngine, String type, XyzCoord xyzCoord) {
        try {
            mapEngine.setMapType(type);
            String urlString = mapEngine.getMapsUrl(xyzCoord);
            String mapNameString = mapEngine.getMapsName(xyzCoord);
            String fileNameString = DIR_STRING + File.separator + mapNameString;
            StringBuffer buffer = new StringBuffer();
            buffer.append(mapNameString);

            if (new File(fileNameString).exists()) {
                buffer.append("\t").append("Exist");
                System.out.println(buffer.toString());
                return;
            }

            buffer.append("\t").append(urlString);

            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            int responseCode = urlConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                int len = Integer.valueOf(urlConnection.getHeaderField("content-length"));
                if (len > 0) {
                    InputStream inputStream = urlConnection.getInputStream();
                    int actual = 0;
                    int bytesread = 0;
                    byte[] data = new byte[len];
                    while ((bytesread != len) && (actual != -1)) {
                        actual = inputStream.read(data, bytesread, len - bytesread);
                        bytesread += actual;
                    }

                    File file = new File(DIR_STRING);
                    if (!file.exists()) {
                        if (!file.mkdir()) {
                            file.mkdirs();
                        }
                    }

                    DataOutputStream out = new DataOutputStream(new BufferedOutputStream(
                            new FileOutputStream(fileNameString)));
                    out.write(data);
                    out.flush();
                    out.close();
                }
                buffer.append("\t").append("OK");
                System.out.println(buffer.toString());
            } else {
                if (responseCode == HttpURLConnection.HTTP_FORBIDDEN) {
                    buffer.append("\t").append("FORBIDDEN");
                    System.out.println(buffer.toString());
                } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                    DataOutputStream out = new DataOutputStream(new BufferedOutputStream(
                            new FileOutputStream(fileNameString)));
                    out.flush();
                    out.close();
                    buffer.append("\t").append("NOT_FOUND");
                    System.out.println(buffer.toString());
                } else if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                    buffer.append("\t").append("HTTP_BAD_REQUEST");
                    System.out.println(buffer.toString());
                } else {
                    buffer.append("\t").append("Error ").append(responseCode);
                    System.out.println(buffer.toString());
                }
            }
            urlConnection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
