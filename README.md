ice4j
=====
The Interactive Connectivity Establishment (ICE) protocol combines various NAT traversal utilities such as the STUN and TURN protocols in order to offer a powerful mechanism that allows Offer/Answer based protocols such as SIP and XMPP to traverse NATs.

This project provides a Java implementation of the ICE protocol that would be usable by both SIP and XMPP applications. The project also provides features such as socket sharing and support for Pseudo TCP.

ice4j is maintained by the [Jitsi](https://jitsi.org/) community. Use Jitsi's [dev mailing list](https://jitsi.org/Development/MailingLists) for questions and discussions.

Thanks
------
Work on this project was graciously funded by the [NLnet Foundation](https://nlnet.nl/). Thank you!



#simple test case for ice4j

run main2 and main3 simultaneously
enter main2 sdp info on main3 and vice versa(offer/answer)
when corresponding sdp info is added on either sides connection gets setup and
msg can be send to either sides

