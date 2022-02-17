package com.espressif.esptouch.android.connect;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

public class Connection {
    private static Connection INSTANCE = null;
    private static final int SERVERPORT = 3333;
    private static String SERVER_IP = "192.168.4.1";
    private static ClientThread clientThread;
    private static MutableLiveData<String> msg = new MutableLiveData<>();

    private Connection() {
    }

    public void setNull(){
        INSTANCE = null;
    }
    public static Connection getInstance() {
        if (INSTANCE == null) {
            synchronized (Connection.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Connection();
                    clientThread = new ClientThread();
                    Thread thread = new Thread(clientThread);
                    thread.start();
                }
            }
        }
        return INSTANCE;
    }

    public ClientThread getClientThread() {
        return clientThread;
    }

    public MutableLiveData<String> getMsg() {
        return msg;
    }

    public void sendMessage(String msg) {
        // Below is for testing purpose
//        String a = "1 1 1 " + msg.substring(2) + " 2 0 0 3 0 0";
//        getMsg().postValue(a);
        getClientThread().sendMessage(msg);
    }

//    static String arr[] = {"7 1 1 0 2 0 0 3 0 0", "8 1 1 15 2 0 0 3 0 0", "5 1 1 8 2 0 0 3 0 0", "4 1 1 7 2 0 0 3 0 0"};

    private static class ClientThread implements Runnable {

        private Socket socket;
        private BufferedReader input;
        private DataInputStream dis;

        private final String TAG = ClientThread.class.getSimpleName();

        @Override
        public void run() {
//            while (true) {
//                try {
//                    Thread.sleep(5000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                Random random = new Random();
//                int int_random = random.nextInt(4);
//                msg.postValue(arr[int_random]);
//            }
            try {
                Log.e(TAG, "server ip: " + SERVER_IP);
                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
                socket = new Socket(serverAddr, SERVERPORT);
                InputStream is = socket.getInputStream();               // SOCKET READ
                byte[] buffer = new byte[1024];
                int read;
                String message = null;

                while ((read = is.read(buffer)) != -1) {
                    message = new String(buffer, 0, read);
                    System.out.print(message);
                    System.out.flush();
                    Log.e(TAG, "message from server: " + message);
                    msg.postValue(message);
//                showMessage(message, clientTextColor);
                }


            } catch (UnknownHostException e1) {
                e1.printStackTrace();
                Log.e(TAG, "UnknownHostException: ");
            } catch (IOException e1) {
                e1.printStackTrace();
                Log.e(TAG, "IOException:");
            }
//        showMessage("Connected to Server...", clientTextColor);
        }

        public void sendMessage(final String message) {
            new Thread(() -> {
                try {
                    if (null != socket) {
                        //  SOCKET WRITE
                        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                        /*PrintWriter out = new PrintWriter(socket.getOutputStream());
                        out.println();
                        out.flush();*/
                        Log.e(TAG, "sendMessage: " + message);
                        out.println(message);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Exception: " + e.getMessage());
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
