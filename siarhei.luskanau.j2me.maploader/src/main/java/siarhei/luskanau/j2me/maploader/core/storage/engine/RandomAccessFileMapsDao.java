package siarhei.luskanau.j2me.maploader.core.storage.engine;

import java.io.File;
import java.util.Enumeration;

import siarhei.luskanau.j2me.core.storage.Storage;
import siarhei.luskanau.j2me.core.storage.StorageEngine;
import siarhei.luskanau.j2me.map.dao.MapsDao;
import siarhei.luskanau.j2me.map.dao.storage.MapsRecord;
import siarhei.luskanau.j2me.map.engine.MapEngine;
import siarhei.luskanau.j2me.map.entity.Map;
import siarhei.luskanau.j2me.map.entity.XyzCoord;

public class RandomAccessFileMapsDao implements MapsDao {

    private final String STORAGE_NAME = "maps";

    private MapsRecord mapsRecord;

    public RandomAccessFileMapsDao(String baseUrl) throws Exception {
        try {
            if (baseUrl == null) {
                baseUrl = "";
            }
            if (baseUrl.length() > 0 && !baseUrl.endsWith(File.separator)) {
                baseUrl = baseUrl + File.separator;
            }
            StorageEngine storageEngine = new RandomAccessFileStorageEngine(baseUrl + STORAGE_NAME);
            Storage storage = new Storage(storageEngine);
            mapsRecord = new MapsRecord();
            storage.setRecord(mapsRecord);
            storageEngine.open();
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when create FileMapsDao.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public Map getMap(XyzCoord xyzCoord, MapEngine engine) throws Exception {
        try {
            String name = engine.getMapsName(xyzCoord);
            if (mapsRecord.exist(name)) {
                return mapsRecord.select(name);
            }
            return null;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when getMap in FileMapsDao.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public void setMap(Map map, XyzCoord xyzCoord, MapEngine engine) throws Exception {
        try {
            String name = map.getName();
            if (!mapsRecord.exist(name)) {
                mapsRecord.append(map);
            }
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when setMap in FileMapsDao.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    @SuppressWarnings("rawtypes")
    public Enumeration getNamesMap() throws Exception {
        try {
            return mapsRecord.selectNames();
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when getNamesMap in FileMapsDao.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

}
