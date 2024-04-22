/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.servermulti;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author federico
 */
public class ClientMulti {
    // Costanti per i colori del prompt
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String RED_BOLD = "\033[1;31m";
    public static final String GREEN_UNDERLINED = "\033[4;32m";
    public static final String RESET = "\033[0m";

    /**
     * Metodo principale del client.
     */
    public static void main(String[] args) {
        // Porta del server
        int port = 2000;
        // Porta del gruppo multicast
        int portGroup = 1900;
        // Indirizzo del server
        InetAddress serverAddress;
        // Socket UDP
        DatagramSocket dSocket = null;
        // Socket multicast UDP
        MulticastSocket mSocket = null;
        // Indirizzo del gruppo multicast UDP
        InetAddress group;
        // Datagramma UDP per la richiesta al server
        DatagramPacket outPacket;
        // Datagramma UDP per la risposta dal server
        DatagramPacket inPacket;
        // Buffer di lettura
        byte[] inBuffer = new byte[256];
        byte[] inBufferG = new byte[1024];
        // Messaggi di richiesta e risposta
        String messageOut = "Richiesta comunicazione";
        String messageIn;

        try {
            // Inizializzazione del client
            System.out.println(RED_BOLD + "CLIENT UDP" + RESET);

            // Invio di una richiesta al server
            serverAddress = InetAddress.getLocalHost();
            System.out.println(RED_BOLD + "Indirizzo del server trovato!" + RESET);
            dSocket = new DatagramSocket();
            outPacket = new DatagramPacket(messageOut.getBytes(), messageOut.length(), serverAddress, port);
            dSocket.send(outPacket);
            System.out.println(RED_BOLD + "Richiesta al server inviata!" + RESET);

            // Ricezione della risposta dal server
            inPacket = new DatagramPacket(inBuffer, inBuffer.length);
            dSocket.receive(inPacket);
            messageOut = new String(inPacket.getData(), 0, inPacket.getLength());
            System.out.println(ANSI_BLUE + "Lettura dei dati ricevuti dal server" + RESET);
            messageIn = new String(inPacket.getData(), 0, inPacket.getLength());
            System.out.println(ANSI_BLUE + "Messaggio ricevuto dal server " + serverAddress +
                    ":" + port + "\n\t" + messageIn + RESET);

            // Ricezione di un messaggio dal gruppo multicast
            mSocket = new MulticastSocket(portGroup);
            group = InetAddress.getByName("239.255.255.250");
            mSocket.joinGroup(group);
            inPacket = new DatagramPacket(inBufferG, inBufferG.length);
            mSocket.receive(inPacket);
            messageIn = new String(inPacket.getData(), 0, inPacket.getLength());
            System.out.println(GREEN_UNDERLINED + "Lettura dei dati ricevuti dai partecipanti al gruppo" + RESET);
            System.out.println(GREEN_UNDERLINED + "Messaggio ricevuto dal gruppo " + group +
                    ":" + portGroup + "\n\t" + messageIn + RESET);
            mSocket.leaveGroup(group);
        } catch (IOException ex) {
            Logger.getLogger(ClientMulti.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Errore di I/O");
        } finally {
            // Chiusura dei socket alla fine dell'esecuzione
            if (dSocket != null)
                dSocket.close();
            if (mSocket != null)
                mSocket.close();
        }
    }
}