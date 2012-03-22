package cli;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Worker class which creates worker threads.
 * @author TJ Aditya
 */
 
class DownloadWorker implements Runnable {

	private String song_url;
	private String title;
	private String path;
	private final String style = "[*]";

	/**
	* Constructor to initialize a few things.
	* @param song_url The string representation of song URL.
	* @param title Title of the song.
	* @param path The string representation of output path.
	*/
	DownloadWorker(String song_url, String title, String path) {
		this.song_url = song_url;
		this.title = title;
		this.path = path;
	}
	
	@Override
	public void run() {
		try {
			System.out.println(style + "Started downloading " + title + ".mp3");
			URL url = new URL(song_url);
			URLConnection con = url.openConnection();
			InputStream is = con.getInputStream();
			OutputStream out = new BufferedOutputStream(new FileOutputStream(new File(
					path + "/" + title + ".mp3")));
			byte[] buf = new byte[1024];
			int byteRead = 0, byteWritten = 1;
			while ((byteRead = is.read(buf)) != -1) {
				out.write(buf, 0, byteRead);
				byteWritten += byteRead;
			}
			System.out.println(style + title + ".mp3....completed!");
			is.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
