package cli;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Main class for the command line application.
 * @author TJ Aditya
 */

public class BandcampDownloader {

	/**
	 * @param args Contains the url and path.
	 */
	public static void main(String[] args) {

		final String style = "[*]";
		String str_url = "";
		String path = BandcampDownloader.class
	                      .getProtectionDomain()
	                      .getCodeSource()
	                      .getLocation()
	                      .getPath();
		if(args.length == 1) {
			str_url = args[0];
		} else{
			System.out.println("Usage: java BandcampDownloader <url>");
			System.exit(1);
		}
		ExecutorService exec = Executors.newCachedThreadPool();
		try {
			System.out.println(style + "Initializing..");
			URL url = new URL(str_url);
			URLConnection con = url.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String str = "";
			while ((str = in.readLine()) != null) {
				if (str.contains("\"file\""))
					break;
			}
			in.close();
			String[] s = str.split(",");
			String song_url = "", title = "";
			System.out.println(style + "Download started..");
			for (int i = 0; i < s.length; i++) {
				if (s[i].startsWith("\"title\"")) {
					title = s[i].replaceAll("\"", "").replaceAll("\\\\", "")
							.replace("}", "").substring(6).replace("/", "-").replace("\\", "-");
				} else if (s[i].startsWith("\"file\"")) {
					song_url = s[i].replaceAll("\"", "").replaceAll("\\\\", "")
							.substring(5).replace("}", "").replace("]", "");
					exec.execute(new DownloadWorker(song_url, title, path));
				}
			}
			exec.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
