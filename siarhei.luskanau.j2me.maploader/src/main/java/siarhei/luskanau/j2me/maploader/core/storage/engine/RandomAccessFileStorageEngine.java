package siarhei.luskanau.j2me.maploader.core.storage.engine;

import java.io.ByteArrayOutputStream;
import java.io.RandomAccessFile;
import java.util.Enumeration;

import siarhei.luskanau.j2me.core.storage.Index;
import siarhei.luskanau.j2me.core.storage.RawData;
import siarhei.luskanau.j2me.core.storage.Record;
import siarhei.luskanau.j2me.core.storage.StorageEngine;

public class RandomAccessFileStorageEngine extends StorageEngine {

    private String baseUrl;

    private RandomAccessFile storageFile;

    private RandomAccessFile indexFile;

    public RandomAccessFileStorageEngine(String baseUrl) throws Exception {
        try {
            this.baseUrl = baseUrl;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when create RandomAccessFileStorageEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public void open() throws Exception {
        try {
            openStorageFile();
            openIndexFile(false);
            loadIndex();
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when open in RandomAccessFileStorageEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public long append(String name, byte[] data) throws Exception {
        try {
            long position = storageFile.length();
            storageFile.seek(position);

            RawData rawData = new RawData(true, name, data);
            byte[] rocordData = toRocordData(rawData);
            storageFile.writeInt(rocordData.length);
            storageFile.write(rocordData);

            return position;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when append in RandomAccessFileStorageEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public byte[] read(long position) throws Exception {
        try {
            storageFile.seek(position);
            byte[] data = null;
            int size = storageFile.readInt();
            if (size > 0) {
                byte[] rocordData = new byte[size];
                storageFile.read(rocordData);
                RawData rawData = fromRocordData(rocordData);
                if (rawData.isValid()) {
                    data = rawData.getData();
                }
            }
            return data;
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when read in RandomAccessFileStorageEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public void delete(long position) throws Exception {
        try {
            throw new RuntimeException("Not implemented!");
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when delete in RandomAccessFileStorageEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public void update(long position, String name, byte[] data) throws Exception {
        try {
            throw new RuntimeException("Not implemented!");
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when update in RandomAccessFileStorageEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public void compression() throws Exception {
        try {
            throw new RuntimeException("Not implemented!");
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when compression in RandomAccessFileStorageEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    public void loadIndex() throws Exception {
        try {
            indexFile.seek(0);
            long fileSize = indexFile.length();
            if (fileSize > 0) {
                for (; indexFile.getFilePointer() < fileSize;) {
                    String indexName = indexFile.readUTF();
                    boolean unique = indexFile.readBoolean();
                    String key = indexFile.readUTF();
                    String value = indexFile.readUTF();
                    long position = indexFile.readLong();
                    Index index = getIndex(indexName);
                    index.add(key, value, unique, position);
                }
            } else {
                createIndex();
                saveIndex();
            }
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when loadIndex in RandomAccessFileStorageEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void saveIndex() throws Exception {
        try {
            openIndexFile(true);
            if (indexes == null) {
                return;
            }
            indexFile.seek(0);
            for (Enumeration en = indexes.keys(); en.hasMoreElements();) {
                String indexName = (String) en.nextElement();
                Index index = (Index) indexes.get(indexName);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                index.save(outputStream, indexName);
                indexFile.write(outputStream.toByteArray());
            }
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when saveIndex in RandomAccessFileStorageEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    @Override
    public void createIndex() throws Exception {
        try {
            openIndexFile(true);
            if (indexes == null) {
                return;
            }
            long storageSize = storageFile.length();
            long position = 0;
            indexFile.seek(position);

            for (; storageSize > position;) {
                int size = storageFile.readInt();
                long cnt = 4;
                cnt += size;
                if (size > 0) {
                    byte[] rocordData = new byte[size];
                    storageFile.read(rocordData);
                    RawData rawData = fromRocordData(rocordData);
                    String name = rawData.getName();
                    if (rawData.isValid()) {
                        Record record = storage.getRecords(name);
                        Index index = getIndex(name);
                        record.doIndex(rawData.getData(), index, position);
                    }
                }
                position += cnt;
            }
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when createIndex in RandomAccessFileStorageEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    private void openStorageFile() throws Exception {
        try {
            storageFile = new RandomAccessFile(baseUrl + ".dat", "rw");
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when openStorageFile in RandomAccessFileStorageEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

    private void openIndexFile(boolean remove) throws Exception {
        try {
            indexFile = new RandomAccessFile(baseUrl + ".idx", "rw");
            if (remove) {
                indexFile.setLength(0);
            }
        } catch (Throwable t) {
            StringBuffer message = new StringBuffer();
            message.append("Error when openIndexFile in RandomAccessFileStorageEngine.");
            message.append("\n\t").append(t.toString());
            throw new Exception(message.toString());
        }
    }

}
