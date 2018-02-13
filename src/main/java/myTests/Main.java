package myTests;


import java.net.InetAddress;
import java.util.Base64;

import org.ice4j.Transport;
import org.ice4j.TransportAddress;
import org.ice4j.ice.Agent;
import org.ice4j.ice.IceMediaStream;
import org.ice4j.ice.harvest.StunCandidateHarvester;

public class Main {
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
		int port = 5001; // Choose any port
		agent.createComponent(stream, Transport.UDP, port, port, port+100);
		
		String toSend = SdpUtils.createSDPDescription(agent); //Each computer sends this information
		// This information describes all the possible IP addresses and ports
		
	
		
		System.out.println(Base64.getEncoder().encodeToString(toSend.getBytes()));
		
		
		
		
		
		
	}

}
