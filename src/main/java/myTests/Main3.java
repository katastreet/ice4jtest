package myTests;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Base64;
import java.util.Scanner;

import javax.xml.crypto.Data;

import org.ice4j.Transport;
import org.ice4j.TransportAddress;
import org.ice4j.ice.Agent;
import org.ice4j.ice.IceMediaStream;
import org.ice4j.ice.IceProcessingState;
import org.ice4j.ice.harvest.StunCandidateHarvester;

public class Main3 {
	public static void main(String[] args) throws Throwable {
		Agent agent = new Agent(); // A simple ICE Agent

		/*** Setup the STUN servers: ***/
		String[] hostnames = new String[] {"jitsi.org","numb.viagenie.ca","stun.ekiga.net"};
		// Look online for actively working public STUN Servers. You can find free servers.
		// Now add these URLS as Stun Servers with standard 3478 port for STUN servrs.
		for(String hostname: hostnames){
		   try {
		      // InetAddress qualifies a url to an IP Address, if you have an error here, make sure the url is reachable and correct
		      TransportAddress ta = new TransportAddress(InetAddress.getByName(hostname), 3478, Transport.UDP);
		      // Currently Ice4J only supports UDP and will throw an Error otherwise
		      agent.addCandidateHarvester(new StunCandidateHarvester(ta));
		   } catch (Exception e) { e.printStackTrace();}
		}
		
		IceMediaStream stream = agent.createMediaStream("audio");
		int port = 5000; // Choose any port
		agent.createComponent(stream, Transport.UDP, port, port, port+100);
		
		String toSend = SdpUtils.createSDPDescription(agent); //Each computer sends this information
		// This information describes all the possible IP addresses and ports
		
	
		System.out.println("local sdp info:");
		System.out.println(Base64.getEncoder().encodeToString(toSend.getBytes()));
		
		Scanner uScanner = new Scanner(System.in);
        
		System.out.println("select remote sdp info:");
		String remoteRecieved = uScanner.nextLine();
		remoteRecieved = new String(Base64.getDecoder().decode(remoteRecieved));
		
		System.out.println(remoteRecieved);
		
		SdpUtils.parseSDP(agent, remoteRecieved);
		
		StateListener myStateListener = new StateListener();
		agent.addStateChangeListener(myStateListener); // We will define this class soon
		// You need to listen for state change so that once connected you can then use the socket.
		agent.startConnectivityEstablishment(); // This will do all the work for you to connect
		
		
		while (IceProcessingState.TERMINATED != agent.getState()) {
            System.out.println("Connectivity Establishment in process");
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
			}
		
		System.out.println("connection successful");
		while(true) {
			System.out.println("enter data to send:");
			String datatoSend = uScanner.nextLine();
		DatagramPacket packet = new DatagramPacket(new byte[10000],10000);
		packet.setAddress(myStateListener.hostname);
		packet.setPort(myStateListener.port);
		packet.setData(datatoSend.getBytes());
		myStateListener.wrapper.send(packet);
		
		DatagramPacket recievedPacket = new DatagramPacket(new byte[10000], 10000);
		
		myStateListener.wrapper.receive(recievedPacket);
		
		System.out.println(new String(recievedPacket.getData()));
		}
	
		
	}

}
