import java.awt.*;
import java.awt.event.InputEvent;
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
    private static HashMap<Character, Character> keyMap = new HashMap<>();
    private static HashMap<String, Integer> commandKeys = new HashMap<>();

    public static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception ignored) {
        }
    }

    public static void printTitle() {
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

    public static void textOutput(String s, Robot robot){
        try{
            for (char letter : s.toCharArray()) {
                boolean shiftRequired = false;
                Character value = keyMap.get(letter);
                if (value != null) {
                    shiftRequired = true;
                    letter = value;
                } else if (Character.isUpperCase(letter)) {
                    shiftRequired = true;
                }
                int keyCode = KeyEvent.getExtendedKeyCodeForChar(letter);
                if (shiftRequired)
                    robot.keyPress(java.awt.event.KeyEvent.VK_SHIFT);
                robot.keyPress(keyCode);
                robot.keyRelease(keyCode);
                if (shiftRequired)
                    robot.keyRelease(java.awt.event.KeyEvent.VK_SHIFT);
            }
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }

    }

    public static void keyCombos(String s, Robot robot){
        String[] list = s.split(",");
        for (int i = 0; i < list.length; i++){
            try{
                if(commandKeys.get(list[i])!=null){
                    int keyCode = commandKeys.get(list[i]);
                    robot.keyPress(keyCode);
                } else if (list[i].length()==1) {
                    if(Character.isAlphabetic(list[i].charAt(0)) || Character.isDigit(list[i].charAt(0))){
                        int keyCode = KeyEvent.getExtendedKeyCodeForChar(list[i].charAt(0));
                        robot.keyPress(keyCode);
                    }
                }
            }catch (IllegalArgumentException e){
                e.printStackTrace();
            }
        }
        for (int i = list.length-1; i > -1; i--){
            try{
                if(commandKeys.get(list[i])!=null){
                    int keyCode = commandKeys.get(list[i]);
                    robot.keyRelease(keyCode);
                } else if (list[i].length()==1) {
                    if(Character.isAlphabetic(list[i].charAt(0)) || Character.isDigit(list[i].charAt(0))){
                        int keyCode = KeyEvent.getExtendedKeyCodeForChar(list[i].charAt(0));
                        robot.keyRelease(keyCode);
                    }
                }
            }catch (IllegalArgumentException e){
                e.printStackTrace();
            }
        }
    }
    public static void mouseMove(String s, Robot robot){
        String[] list = s.split(",");
        if(list.length==2){
            try{
                int x = Integer.parseInt(list[0]);
                int y = Integer.parseInt(list[1]);
                robot.mouseMove(x,y);
            }catch (NumberFormatException e){
                e.printStackTrace();
            }
        }
    }
    public static void mouseClick(String s, Robot robot){
        if(s.equals("left")){
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            sleep(5);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        } else if (s.equals("middle")) {
            robot.mousePress(InputEvent.BUTTON2_DOWN_MASK);
            sleep(5);
            robot.mouseRelease(InputEvent.BUTTON2_DOWN_MASK);
        } else if (s.equals("right")) {
            robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
            sleep(5);
            robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
        }
    }

    public static void main(String[] args) {
        printTitle();
        keyMap.put('!', '1');
        keyMap.put('@', '2');
        keyMap.put('#', '3');
        keyMap.put('$', '4');
        keyMap.put('%', '5');
        keyMap.put('^', '6');
        keyMap.put('&', '7');
        keyMap.put('*', '8');
        keyMap.put('(', '9');
        keyMap.put(')', '0');
        keyMap.put('{', '[');
        keyMap.put('}', ']');
        keyMap.put(':', ';');
        keyMap.put('"', '\'');
        keyMap.put('<', ',');
        keyMap.put('>', '.');
        keyMap.put('?', '/');
        keyMap.put('_', '-');
        keyMap.put('+', '=');
        keyMap.put('|', '\\');

        commandKeys.put("Ctrl", KeyEvent.VK_CONTROL);
        commandKeys.put("Win", KeyEvent.VK_WINDOWS);
        commandKeys.put("Shift", KeyEvent.VK_SHIFT);
        commandKeys.put("Left", KeyEvent.VK_LEFT);
        commandKeys.put("Right", KeyEvent.VK_RIGHT);
        commandKeys.put("Up", KeyEvent.VK_UP);
        commandKeys.put("Down", KeyEvent.VK_DOWN);
        commandKeys.put("Enter", KeyEvent.VK_ENTER);
        commandKeys.put("Alt", KeyEvent.VK_ALT);
        commandKeys.put("F1", KeyEvent.VK_F1);
        commandKeys.put("F2", KeyEvent.VK_F2);
        commandKeys.put("F3", KeyEvent.VK_F3);
        commandKeys.put("F4", KeyEvent.VK_F4);
        commandKeys.put("F5", KeyEvent.VK_F5);
        commandKeys.put("F6", KeyEvent.VK_F6);
        commandKeys.put("F7", KeyEvent.VK_F7);
        commandKeys.put("F8", KeyEvent.VK_F8);
        commandKeys.put("F9", KeyEvent.VK_F9);
        commandKeys.put("F10", KeyEvent.VK_F10);
        commandKeys.put("F11", KeyEvent.VK_F11);
        commandKeys.put("F12", KeyEvent.VK_F12);
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            robot = new Robot();
            System.out.println("System IP Address : " +
                    (localhost.getHostAddress()).trim());
            serverSocket = new ServerSocket(9155);

            System.out.println("Listening on 9155");
        } catch (IOException | AWTException e) {
            throw new RuntimeException(e);
        }
        while (true) {
            try {
                socket = serverSocket.accept();
                inputStreamReader = new InputStreamReader(socket.getInputStream());
                bufferedReader = new BufferedReader(inputStreamReader);
                robot = new Robot();
                string = bufferedReader.readLine();
                if (string.equals("Over")) {
                    break;
                }
                JSONObject jsonObject = new JSONObject(string);
                if (jsonObject.get("type").equals("1")) {
                    textOutput(jsonObject.getString("data"), robot);
                } else if (jsonObject.get("type").equals("2")) {
                    keyCombos(jsonObject.getString("data"), robot);
                } else if (jsonObject.get("type").equals("3")) {
                    mouseMove(jsonObject.getString("data"), robot);
                } else if (jsonObject.get("type").equals("4")) {
                    mouseClick(jsonObject.getString("data"), robot);
                } else if(jsonObject.get("type").equals("5")){
                    sleep(Long.parseLong(jsonObject.getString("data")));
                }
            } catch (IOException | JSONException | AWTException e) {
                e.printStackTrace();
            }

        }
    }
}