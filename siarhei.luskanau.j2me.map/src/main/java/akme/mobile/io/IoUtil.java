package akme.mobile.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

import javax.microedition.io.Connection;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.io.file.FileConnection;

import akme.mobile.util.StringUtil;

public abstract class IoUtil {

	public static final String FILE_ROOT = "file:///";
	public static final String CFCARD_ROOT = FILE_ROOT+"CFCard/";
	public static final String SDCARD_ROOT = FILE_ROOT+"SDCard/";
	public static final String STORE_ROOT = FILE_ROOT+"store/";
	public static final String SYSTEM_ROOT = FILE_ROOT+"system/";
	public static final String HTTP_NEWLINE = "\r\n";
	public static final String UNIX_NEWLINE = "\n";
	public static final String HTTP_LOCATION_HEADER = "Location";

	/**
	 * Check if the given URL exists.
	 */
	public static final boolean exists(String url) {
		if (url == null) return false;
		try {
			if (url != null && url.startsWith("file:")) {
				FileConnection fcon = (FileConnection)Connector.open(url);
				final boolean result = fcon != null && fcon.exists();
				if (fcon != null) fcon.close();
				return result;
			} else {
				Connector.openInputStream(url).close();
				return true;
			}
		}
		catch (Exception ex) {
			return false;
		}
	}

	/**
	 * Create a directory structure if it doesn't exist, may throw an IOException.
	 */
	public static boolean createDirectoriesIfNeeded(String path) throws IOException {
		FileConnection fcon = null;
		for (int i=0, j=path.indexOf('/', path.indexOf("//")+2); 
		j<path.length() && j != -1; 
		i = j, j=path.indexOf('/', i+1)) {
			try {
				String dir = path.substring(i, j+1); 
				fcon = (FileConnection) Connector.open(dir);
				if (fcon.exists()) {
					if (!fcon.isDirectory()) return false;
				} else {
					fcon.create();
				}
			}
			finally {
				closeQuiet(fcon);
			}
		}
		fcon = null;
		return true;
	}

	/**
	 * Close quietly, snuffing any IOException.
	 */
	public static final void closeQuiet(Connection con) {
		if (con != null) try { con.close(); } catch (IOException ex) {} 
	}

	/**
	 * Close quietly, snuffing any IOException.
	 */
	public static final void closeQuiet(InputStream ins) {
		if (ins != null) try { ins.close(); } catch (IOException ex) {} 
	}

	/**
	 * Close quietly, snuffing any IOException.
	 */
	public static final void closeQuiet(OutputStream ous) {
		if (ous != null) try { ous.close(); } catch (IOException ex) {} 
	}

	/**
	 * Close quietly, snuffing any IOException.
	 */
	public static final void closeQuiet(Reader ins) {
		if (ins != null) try { ins.close(); } catch (IOException ex) {} 
	}

	/**
	 * Close quietly, snuffing any IOException.
	 */
	public static final void closeQuiet(Writer ous) {
		if (ous != null) try { ous.close(); } catch (IOException ex) {} 
	}

	/**
	 * Handle an HTTP REDIRECT by header or body with Location: ...
	 */
	public static final void handleHttpRedirect(HttpConnection con) throws IOException { 
		int status = con.getResponseCode(); 
		String redirect = con.getHeaderField(HTTP_LOCATION_HEADER);
		if (status == HttpConnection.HTTP_TEMP_REDIRECT) {
			if (redirect != null) {
				// This the standard HTTP 1.1 behaviour, move on to the redirection uri (basically restarting again).
				con.close();
				con = (HttpConnection) Connector.open(redirect);
			} else {
				// Parse the content of the HTTP response, if any.
				// Lookup for a "Location" header, if found, set value to the redirection variable
				InputStreamReader ins = new InputStreamReader(con.openInputStream());
				char[] buf = new char[HTTP_LOCATION_HEADER.length()+1];
				int n = ins.read(buf);
				if (HTTP_LOCATION_HEADER.equalsIgnoreCase(new String(buf, 0, n))) {
					StringBuffer sb = new StringBuffer(64);
					for (; (n=ins.read(buf)) != -1;) {
						int newpos = StringUtil.indexOf('\n', buf, 0, n);
						if (newpos != -1) {
							sb.append(buf, 0, newpos);
							break;
						}
						sb.append(buf, 0, n);
					}
					redirect = sb.toString();
					sb = null;
				}
				if (redirect != null) {
					// Since location was found, fall back to the standard behaviour.
				} else {
					long begin_wait = System.currentTimeMillis();
					while (System.currentTimeMillis() - begin_wait < 1000 || status != HttpConnection.HTTP_OK) {
						try { Thread.sleep(100); }
						catch (InterruptedException ex) { break; }
						status = con.getResponseCode();
					};
					if (status == HttpConnection.HTTP_OK) {
						// Once again we're back on tracks, continue processing as if no error has ever happen 
					} else {
						// Here we're really hopeless. Either the server did provided a valid redirection uri, 
						// or the device did not preserved it. The best option is probably to fail by throwing an exception.
					};
				};
			};
		} else {// Handle other error codes here
		}
		// Handle success here (status == 200)
	}

}
