import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.AWTException;
import java.util.HashMap;

import org.json.*;

public class Server {
    private static ServerSocket serverSocket;
    private static Robot robot;
    private static Socket socket;
    private static InputStreamReader inputStreamReader;
    private static BufferedReader bufferedReader;
    private static String string;

    public static void sleep(long ms) {
        try {Thread.sleep(ms);} catch (Exception ignored) {}
    }

    public static void printTitle(){
        System.out.println("                  .----.");
        System.out.println("      .---------. | == |");
        System.out.println(
                "      |.-\"\"\"\"\"-.| |----|                                           __                   ___.                              .___");
        System.out.println(
                "      ||       || | == |   _____  _____     ____ _______   ____   |  | __  ____  ___.__.\\_ |__    ____ _____  _______   __| _/");
        System.out.println(
                "      ||       || |----|  /     \\ \\__  \\  _/ ___\\\\_  __ \\ /  _ \\  |  |/ /_/ __ \\<   |  | | __ \\  /  _ \\\\__  \\ \\_  __ \\ / __ | ");
        System.out.println(
                "      |'-.....-'| |::::| |  Y Y  \\ / __ \\_\\  \\___ |  | \\/(  <_> ) |    < \\  ___/ \\___  | | \\_\\ \\(  <_> )/ __ \\_|  | \\// /_/ | ");
        System.out.println(
                "      `\"\")---(\"\"` |___.| |__|_|  /(____  / \\___  >|__|    \\____/  |__|_ \\ \\___  >/ ____| |___  / \\____/(____  /|__|   \\____ | ");
        System.out.println(
                "    /:::::::::::\\ \"_   \"       \\/      \\/      \\/                      \\/     \\/ \\/          \\/             \\/             \\/ ");
        System.out.println("   /:::=======:::\\`\\`\\");
        System.out.println("   `\"\"\"\"\"\"\"\"\"\"\"\"\"`  '-'");
    }

    public static void func(String s1) throws IOException,
            AWTException, InterruptedException {
        HashMap<String, Integer> Key2 = new HashMap<String, Integer>();
        Key2.put("(", 0x5B);
        Key2.put("\\", 0x5C);
        Key2.put(")", 0x5D);
        Key2.put("+", 0x6B);
        Key2.put(".", 0x6C);
        Key2.put("-", 0x6D);
        Key2.put("/", 0x6F);
        Key2.put("&", 0x96);
        Key2.put("*", 0x97);
        Key2.put("\"", 0x98);
        Key2.put("<", 0x99);
        Key2.put(">", 0xa0);
        Key2.put("{", 0xa1);
        Key2.put("}", 0xa2);
        Key2.put("@", 0x0200);
        Key2.put(":", 0x0201);
        Key2.put("^", 0x0202);
        Key2.put("$", 0x0202);
        Key2.put("€", 0x0204);
        Key2.put("!", 0x0205);
        Key2.put("#", 0x0205);
        Key2.put("_", 0x020B);
        Robot r = new Robot();
        for (int i = 0; i < s1.length(); i++) {
            if ((s1.charAt(i) >= 48 && s1.charAt(i) <= 57) || (s1.charAt(i) >= 65 && s1.charAt(i) <= 90)
                    || (s1.charAt(i) >= 97 && s1.charAt(i) <= 122) || s1.charAt(i)==' ') {
                if (Character.isUpperCase(s1.charAt(i))) {
                    robot.keyPress(KeyEvent.VK_SHIFT);
                    robot.keyPress(Character.toUpperCase(s1.charAt(i)));
                    robot.keyRelease(KeyEvent.VK_SHIFT);
                } else {
                    robot.keyPress(Character.toUpperCase(s1.charAt(i)));
                }
            } else {
                String x = "";
                x += s1.charAt(i);
                if (Key2.containsKey(x)) {
                    r.keyPress(Key2.get(x));
                }
            }
            Thread.sleep(20);

        }
    }

    public static void main(String[] args) {
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            System.out.println("System IP Address : " +
                    (localhost.getHostAddress()).trim());
            serverSocket = new ServerSocket(9155);
            robot = new Robot();
            System.out.println("Listening on 9155");
        } catch (IOException | AWTException e) {
            throw new RuntimeException(e);
        }
        while (true) {
            try {
                socket = serverSocket.accept();
                inputStreamReader = new InputStreamReader(socket.getInputStream());
                bufferedReader = new BufferedReader(inputStreamReader);
                string = bufferedReader.readLine();
                if (string.equals("Over")) {
                    break;
                }
                JSONObject jsonObject = new JSONObject(string);
                if (jsonObject.get("type").equals("1")) {
                    func(jsonObject.get("data").toString());
                } else if (jsonObject.get("type").equals("2")){

                } else if (jsonObject.get("type").equals("3")) {

                } else if (jsonObject.get("type").equals("4")) {

                }
            } catch (IOException | JSONException | InterruptedException | AWTException e) {
                throw new RuntimeException(e);
            }

        }
    }
}