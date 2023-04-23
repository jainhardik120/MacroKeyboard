import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.AWTException;

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

    public static void main(String[] args) {
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
                System.out.println(string);
                Robot robot = new Robot();
                for (int i = 0; i < string.length(); i++) {
                    if (Character.isUpperCase(string.charAt(i))) {
                        robot.keyPress(KeyEvent.VK_SHIFT);
                        robot.keyPress(Character.toUpperCase(string.charAt(i)));
                        robot.keyRelease(KeyEvent.VK_SHIFT);
                    }
                    else
                        robot.keyPress(Character.toUpperCase(string.charAt(i)));
                    sleep(10);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch(AWTException e){
                throw new RuntimeException(e);
            }

        }
    }
}