package com.company;

import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Hash {

    String hashString;
    Thread prodThread;
    Thread[] consThread;
    LinkedList<String> sharedQueue = new LinkedList<>();
    int size = 10000;
    char[] dictionary;

    public Hash() {
        hashString = "";
    }

    public void setContent(String hash) {
        hashString = new String(hash).toLowerCase();
    }

    public void createDictionary() {
        final int ALPHAVITE_SIZE = 26;
        dictionary = new char[ALPHAVITE_SIZE];
        for (char c = 'a'; c <= 'z'; c++) dictionary[c - '0' - 49] = c;
    }

    public void passwordSelection(int streamCount) throws NoSuchAlgorithmException, InterruptedException {
        this.createDictionary();
        streamCount = Math.max(1, streamCount);

        this.consThread = new Thread[streamCount];
        for(int i = 0; i < streamCount; i++){
            this.consThread[i] = new Thread(new Consumer(this.sharedQueue, this.hashString), "Consumer");
        }
        this.prodThread = new Thread(new Producer(this.sharedQueue, this.size, this.dictionary), "Producer");

        prodThread.start();
        for(Thread ct : consThread){
            ct.start();
        }

    }
}
