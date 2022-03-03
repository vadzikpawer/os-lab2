package com.company;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Consumer implements Runnable {

    final Queue<String> sharedQueue;
    String content;
    String answer;
    static int countNull = 0;
    public Consumer(Queue<String> sharedQueue, String c) {
        this.sharedQueue = sharedQueue;
        this.content = c;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String check = consume();
                if (check != null && Hash(check).equals(this.content)) {
                    System.out.println("Done! Your Pass - " + check);
                    Main.find = true;
                    System.out.println("Программа выполнялась приблизительно " + ((System.currentTimeMillis() - Main.time)/1000) + "s");
                    this.answer = check;
                }
                if(Main.find){
                    synchronized (sharedQueue){
                        sharedQueue.notifyAll();
                    }
                    return;
                }
            } catch (InterruptedException | NoSuchAlgorithmException ex) {
                ex.printStackTrace();
            }
        }
    }

    // Метод, извлекающий элементы из общей очереди
    private String consume() throws InterruptedException {
        synchronized (sharedQueue) {
            if (sharedQueue.isEmpty()) { // Если пуста, надо ждать
                sharedQueue.wait();
            }
            sharedQueue.notifyAll();
            return sharedQueue.poll();
        }
    }

    public static String Hash(String s) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashInBytes = md.digest(s.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : hashInBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();

    }

}
