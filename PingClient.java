/*
Name: Manpreet Kaur
Login Name: mka111
Student Id: 301294614
*/
import java.io.*;
import java.net.*;
import java.util.*;

/*
* Server to process ping requests over UDP. java PingClient host port
*/
public class PingClient
{

public static void main(String[] args) throws Exception
{
// Get command line argument.
if (args.length < 2) {
System.out.println("Required arguments: host and port");
return;
}
//int count = 3;
String host =args[0];
int port = Integer.parseInt(args[1]);

int count;
if (args.length == 2)
{
	count = 3;
}
else{
count = Integer.parseInt(args[2]);
}
int recievedPackt = 0;
int sendPackt = 0;
int loss =0;
long min = 1000;
long max = 0;
long Total = 0;
DatagramPacket reply;

//Create a datagram socket for receiving and sending UDP packets
// through the port specified on the command line.
DatagramSocket socket = new DatagramSocket();
InetAddress Ip =InetAddress.getByName(host);
// Processing loop.

// You should write the client so that it sends
// 10 ping requests to the server, separated by
// approximately one second.
System.out.println("Pinging with csil-cpu6.csil.sfu.ca");
for(int i=0;i<count;i++){
	//Keep track of when the time begins
	long startTime = System.currentTimeMillis();

long TimeOfSent = System.nanoTime();
String string = "Ping "+ i + " " + TimeOfSent + "\n";
byte[] buf = string.getBytes();
DatagramPacket request = new DatagramPacket(buf, buf.length,Ip,port );
socket.send(request);
sendPackt = sendPackt+1;
try {
socket.setSoTimeout(1000);
//Create a datagram packet to hold incomming UDP packet.
reply = new DatagramPacket(new byte[1024], 1024);
socket.receive(reply);
long time = (System.currentTimeMillis() - startTime);
//Printing reply from each address
System.out.println("Reply from "+ reply.getAddress().getHostAddress() + ": bytes=" + buf.length +
		            " time=" + time +"ms");
//To keep track of Average
Total = Total + time;
recievedPackt = recievedPackt+1;

if(time < min){
	min = time;
}
if(time > max){
	max = time;
}


}catch(IOException e)
{   // To calculate how many times we lose a packet.
	loss = loss+1;
 System.out.println("Timeout, no reply from the server. ");
}
//Simulate network delay.
Thread.sleep(1000); //For client to wait up to one second for a reply. 1sec = 1000milliseconds

}
long Average = Total/recievedPackt;
System.out.println("Ping statistics for "+ Ip);
System.out.println("Packets: Sent = " + sendPackt + ", Recieved = "+  recievedPackt + ", Lost = " + loss );
System.out.println("Approximate round trip times in milli-seconds:");
System.out.println("Minimum = " + min + "ms, Maximum = "+  max  + "ms, Average: "+ Average + "ms");

}


/*
* Print ping data to the standard output stream.
*/
private static void printData(DatagramPacket request) throws Exception
{
   // Obtain references to the packet's array of bytes.
   byte[] buf = request.getData();

   // Wrap the bytes in a byte array input stream,
   // so that you can read the data as a stream of bytes.
   ByteArrayInputStream bais = new ByteArrayInputStream(buf);

   // Wrap the byte array output stream in an input stream reader,
   // so you can read the data as a stream of characters.
   InputStreamReader isr = new InputStreamReader(bais);

   // Wrap the input stream reader in a bufferred reader,
   // so you can read the character data a line at a time.
   // (A line is a sequence of chars terminated by any combination of \r and \n.)
   BufferedReader br = new BufferedReader(isr);

   // The message data is contained in a single line, so read this line.
   String line = br.readLine();

   // Print host address and data received from it.
   System.out.println(
      "Received from " +
      request.getAddress().getHostAddress() +
      ": " +
      new String(line) );
}

}
