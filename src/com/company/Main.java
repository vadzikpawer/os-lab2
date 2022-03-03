package com.company;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class Main {
    static int DICTIONARY_SIZE = 'z'-'a'+1;
    static char[] dictionary;
    public static boolean find;
    public static long time;

    public static void main(String[] args) throws JSONException, NoSuchAlgorithmException, InterruptedException {
        dictionary = new char[DICTIONARY_SIZE];
	    for (char i = 'a'; i <= 'z';i++){
            dictionary[i-'a'] = i;
        }

        Main.find = false;
        File f = new File("hash.json");
        String content = "";

        try (FileInputStream fin = new FileInputStream(f)) {
            int i = -1;
            while ((i = fin.read()) != -1) {
                content += (char) i;
            }
            System.out.println("\n");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        JSONArray mJsonArray = new JSONArray(content);
        JSONObject mJsonObject = new JSONObject();

        Hash[] hashs = new Hash[mJsonArray.length()];

        for (int i = 0; i < hashs.length; i++) {
            hashs[i] = new Hash();
        }

        for (int i = 0; i < mJsonArray.length(); i++) {
            mJsonObject = mJsonArray.getJSONObject(i);
            mJsonObject.getString("hash");
            hashs[i].setContent(mJsonObject.getString("hash"));
        }
        java.util.Scanner in = new java.util.Scanner(System.in);
        System.out.print("Введите количество потоков - ");
        int scount = in.nextInt();

        hashs[1].passwordSelection(scount);

    }
}
