/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.servermulti;

import static com.mycompany.servermulti.ServerMulti.ANSI_BLUE;
import java.io.IOException;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author federico
 */
public class ServerMulti {
    // Costanti per i colori del prompt
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String RED_BOLD = "\033[1;31m";
    public static final String RESET = "\033[0m";

    /**
     * Metodo principale del server.
     */
    public static void main(String[] args) {
        // Porta del server
        int port = 2000;
        // Socket UDP
        DatagramSocket dSocket = null;
        // Datagramma UDP ricevuto dal client
        DatagramPacket inPacket;
        // Buffer per il contenuto del segmento da ricevere
        byte[] inBuffer;
        // Indirizzo del gruppo Multicast UDP
        InetAddress groupAddress;
        // Messaggio ricevuto
        String messageIn;
        // Messaggio da inviare
        String messageOut;

        try {
            // Inizializzazione del server
            System.out.println(ANSI_BLUE + "SERVER UDP" + RESET);
            dSocket = new DatagramSocket(port);
            System.out.println(ANSI_BLUE + "Apertura porta in corso!" + RESET);

            // Ciclo di ascolto per le richieste dei client
            while (true) {
                // Preparazione del buffer per il messaggio da ricevere
                inBuffer = new byte[256];

                // Ricezione del pacchetto dal client
                inPacket = new DatagramPacket(inBuffer, inBuffer.length);
                dSocket.receive(inPacket);

                // Recupero dell'indirizzo IP e della porta del client
                InetAddress clientAddress = inPacket.getAddress();
                int clientPort = inPacket.getPort();

                // Stampa a video del messaggio ricevuto dal client
                messageIn = new String(inPacket.getData(), 0, inPacket.getLength());
                System.out.println(RED_BOLD + "Messaggio ricevuto dal client " + clientAddress +
                        ":" + clientPort + "\n\t" + messageIn + RESET);

                // Preparazione del datagramma di risposta
                messageOut = "Ricevuta richiesta!";
                DatagramPacket outPacket = new DatagramPacket(messageOut.getBytes(), messageOut.length(),
                        clientAddress, clientPort);

                // Invio della risposta al client
                dSocket.send(outPacket);
                System.out.println(ANSI_BLUE + "Spedito messaggio al client: " + messageOut + RESET);

                // Invio di un messaggio al gruppo multicast
                groupAddress = InetAddress.getByName("239.255.255.250");
                int groupPort = 1900;
                messageOut = "Benvenuti a tutti!";
                outPacket = new DatagramPacket(messageOut.getBytes(), messageOut.length(), groupAddress, groupPort);
                dSocket.send(outPacket);
                System.out.println(ANSI_BLUE + "Spedito messaggio al gruppo: " + messageOut + RESET);
            }
        } catch (BindException ex) {
            Logger.getLogger(ServerMulti.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Porta già in uso");
        } catch (SocketException ex) {
            Logger.getLogger(ServerMulti.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Errore di creazione del socket e apertura del server");
        } catch (IOException ex) {
            Logger.getLogger(ServerMulti.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Errore di I/O");
        } finally {
            // Chiusura del socket alla fine dell'esecuzione
            if (dSocket != null)
                dSocket.close();
            System.out.println("Server chiuso");
        }
    }
}