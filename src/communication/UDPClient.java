package communication;

import app.Main;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class UDPClient extends Thread{
    private DatagramSocket socket;
    private InetAddress address;
    private byte[] buffer;
    private Lock lock;

    public UDPClient() throws SocketException, UnknownHostException {
        socket = new DatagramSocket();
        address = InetAddress.getByName("localhost");
        lock = new ReentrantLock();

    }

    public String sendEcho(String msg) throws IOException {
        buffer = msg.getBytes();

        DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length, address, 8001);
        socket.send(datagramPacket);
        socket.setSoTimeout(2000);
        while(true) {
            try {
                datagramPacket = new DatagramPacket(buffer, buffer.length);
                socket.receive(datagramPacket);
                String received = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
                return received;
            }catch (SocketTimeoutException e) {
                return "r";
            }
        }
    }

    @Override
    public void run() {
        lock.lock();
        String echo = "";
        System.out.println("connecting...");
        try {
            while (!echo.equals("s")) {
                try {
                    echo = this.sendEcho("s");

                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (echo.equals("s")) {
                    System.out.println("connected to server");
                    echo = "s";
                } else if (echo.equals("r")) {
                    System.out.println("connecting...");
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } finally {

            lock.unlock();
        }
    }

    public void close() {
        socket.close();
    }
}
