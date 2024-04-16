/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.servermulti;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Date;

/**
 *
 * @author federico
 */
public class ServerMulti {
    private static final String MULTICAST_ADDRESS = "225.4.5.6";
    private static final int PORT = 6789;
    private static final int TTL = 1;

    public static void main(String[] args) {
        try {
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            MulticastSocket multicastSocket = new MulticastSocket();

            for (int i = 0; i < 20; i++) { // Invia per 20 secondi
                String message = "Data e ora: " + new Date();
                byte[] buffer = message.getBytes();
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                multicastSocket.send(packet);
                Thread.sleep(1000); // Attende un secondo prima di inviare il prossimo messaggio
            }

            multicastSocket.leaveGroup(group);
            multicastSocket.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
