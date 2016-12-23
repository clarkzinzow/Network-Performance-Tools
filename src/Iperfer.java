import java.io.*;
import java.math.BigInteger;
import java.net.*;

/* *
 * A tool for measuring network bandwidth.  In client mode, Iperfer will print a one
 * line summary, including the total number of bytes sent (in kilobytes) and the rate
 * at which traffic could be sent (Mbps).  In server mode, Iperfer will print a one
 * line summary, including the total number of bytes received (in kilobytes) and the
 * rate at which traffic could be read (Mbps).
 *
 * Usage:
 * 
 *   - Client Mode:
 *     
 *     $ java Iperfer -c -h [server hostname] -p [server port] -t [time]
 *
 *     -c               Indicates this is the iperf client that generates the data.
 *     server hostname  The hostname or IP address of the iperf server which will
 *                      consume data.
 *     server port      The port on which the remote host is waiting to consume data;
 *                      the port should be in the range 1024 < server port < 65536.
 *     time             The duration in seconds for which data should be generated.
 *
 *   - Server Mode:
 *
 *     $ java Iperfer -s -p [listen port]
 *
 *     -s           Indicates this is the iperf server which should consume data.
 *     listen port  The port on which the host is waiting to consume data; the port
 *                  should be in the range 1024 < listen port < 65536.
 * */

public class Iperfer {

	public static void main(String[] args) {
		if(args.length < 3 || (args.length > 7) ||
				(args.length > 3 && !args[0].equals("-c"))) {
			System.out.println("Error: missing or additional arguments");
			System.exit(1);
		}
		Boolean isClient = (args.length > 3);
		String serverName = "";
		int port = 0;
		int time = 0;
		try {
			if(isClient) {
				serverName = (args[1].equals("-h")) ? args[2] : null;
				port = (args[3].equals("-p")) ? Integer.parseInt(args[4]) : -1;
				time = (args[5].equals("-t")) ? Integer.parseInt(args[6]) : -1;
				if((serverName == null) || (port == -1) || (time == -1)) {
					System.out.println("Error: missing or additional arguments");
					System.exit(1);
				}
				if((port < 1024) || (port > 65535)) {
					System.out.println("Error: port number must be in the range"
							+ " 1024 to 65535");
					System.exit(1);
				}
			}
			else {
				port = (args[1].equals("-p")) ? Integer.parseInt(args[2]) : -1;
				if(port == -1) {
					System.out.println("Error: missing or additional arguments");
					System.exit(1);
				}
				if((port < 1024) || (port > 65535)) {
					System.out.println("Error: port number must be in the range"
							+ " 1024 to 65535");
					System.exit(1);
				}
			}
		}
		catch(NumberFormatException n) {
			System.out.println("Error: missing or additional arguments");
			System.exit(1);
		}

		try {

			if(isClient) {
				Socket client = new Socket(serverName, port);
				OutputStream outToServer = client.getOutputStream();
				DataOutputStream out = new DataOutputStream(outToServer);
				int numBytesSent = 0;
				int bytesSentPerWrite = 1000;
				long sendTime = (time & 0x00000000ffffffffL) * 1000;
				long t = System.currentTimeMillis();
				long end = t + sendTime;
				while(System.currentTimeMillis() < end) {
					out.write(new byte[bytesSentPerWrite], 0, bytesSentPerWrite);
					numBytesSent += bytesSentPerWrite;
				}
				double numKBSent = ((double) numBytesSent) / bytesSentPerWrite;
				double bandwidth = 
				    ((numKBSent / bytesSentPerWrite)*8) / time;
				System.out.println("Total number of kilobytes sent: " +
									numKBSent);
				System.out.println("Bandwidth (Mbps): " + bandwidth);
                                client.close();
			}
			else {
				ServerSocket serverSocket = new ServerSocket(port);
				Socket server = serverSocket.accept();
				DataInputStream in = new DataInputStream(server.getInputStream());
				int bytesRead = 0;
				int numBytesRead = 0;
				byte[] byteInput = new byte[1000];
				long before = System.currentTimeMillis();
				do {
					bytesRead = in.read(byteInput, 0, 1000);
					if(bytesRead > 0) {
						numBytesRead += bytesRead;
					}
				} while(bytesRead != -1);
				long after = System.currentTimeMillis();
				int adjust = 1000;
				int numKBRead = numBytesRead / adjust;
				long timeServer = (after - before) / 1000;
				double bandwidth = 
				    ((numKBRead / adjust)*8) / timeServer;
				System.out.println("Number of kilobytes received: " +
									numKBRead);
				System.out.println("Bandwidth: " + bandwidth);
                                server.close();
			}

		}
		catch(UnknownHostException u) {
			System.out.println("Error");
			System.exit(1);
		}
		catch(IOException i) {
			System.out.println("Error");
			System.exit(1);
		}

	}

}
