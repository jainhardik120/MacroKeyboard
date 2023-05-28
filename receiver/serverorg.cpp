#undef UNICODE
#define WIN32_LEAN_AND_MEAN
#include <windows.h>
#include <winsock2.h>
#include <ws2tcpip.h>
#include <stdlib.h>
#include <stdio.h>
#include <iostream>
#include "json.hpp"

using json = nlohmann::json;
#pragma comment(lib, "Ws2_32.lib")

#define DEFAULT_BUFLEN 512
#define DEFAULT_PORT "27015"

void sendKeyboardStringInput(std::string data)
{
    auto json = json::parse(data);
    std::string input = json["string"];
    const wchar_t *keys = new wchar_t[input.length() + 1];
    mbstowcs((wchar_t *)keys, input.c_str(), input.length() + 1);

    INPUT *inputBuffer = new INPUT[input.length() * 2];
    ZeroMemory(inputBuffer, sizeof(INPUT) * input.length() * 2);


    int inputIndex = 0;
    for (int i = 0; i < input.length(); i++)
    {
        inputBuffer[inputIndex].type = INPUT_KEYBOARD;
        inputBuffer[inputIndex].ki.wVk = 0;
        inputBuffer[inputIndex].ki.wScan = keys[i];
        inputBuffer[inputIndex].ki.dwFlags = KEYEVENTF_UNICODE;
        inputIndex++;
        inputBuffer[inputIndex].type = INPUT_KEYBOARD;
        inputBuffer[inputIndex].ki.wVk = 0;
        inputBuffer[inputIndex].ki.wScan = keys[i];
        inputBuffer[inputIndex].ki.dwFlags = KEYEVENTF_UNICODE | KEYEVENTF_KEYUP;
        inputIndex++;
    }

    SendInput(inputIndex, inputBuffer, sizeof(INPUT));

    delete[] keys;
    delete[] inputBuffer;
}

void moveMouse(std::string data)
{
    auto json = json::parse(data);
    int x = json["x"];
    int y = json["y"];
    INPUT inputs[2] = {};
    ZeroMemory(inputs, sizeof(inputs));
    inputs[0].type = INPUT_MOUSE;
    inputs[0].mi.dx = -1000000;
    inputs[0].mi.dy = -1000000;
    inputs[0].mi.dwFlags = MOUSEEVENTF_MOVE;
    inputs[1].type = INPUT_MOUSE;
    inputs[1].mi.dx = x;
    inputs[1].mi.dy = y;
    inputs[1].mi.dwFlags = MOUSEEVENTF_MOVE;
    SendInput(ARRAYSIZE(inputs), inputs, sizeof(INPUT));
}

void mouseButtonClick(std::string key)
{
}

void keyComboArr(WORD *keys, int n)
{
    INPUT *inputs = new INPUT[2 * n];
    ZeroMemory(inputs, sizeof(INPUT) * 2 * n);
    for (int i = 0; i < n; i++)
    {
        inputs[i].type = INPUT_KEYBOARD;
        inputs[i].ki.wVk = keys[i];
        inputs[2 * n - i - 1].type = INPUT_KEYBOARD;
        inputs[2 * n - i - 1].ki.wVk = keys[i];
        inputs[2 * n - i - 1].ki.dwFlags = KEYEVENTF_KEYUP;
    }
    SendInput(2 * n, inputs, sizeof(INPUT));
    delete[] inputs;
}

void keyCombosPress(std::string data)
{
    auto objectJson = json::parse(data);
    auto json = objectJson["keys"];
    WORD *keys = new WORD[json.size()];
    for (int i = 0; i < json.size(); i++)
    {
        keys[i] = json[i]["key"];
    }
    keyComboArr(keys, json.size());
    delete[] keys;
}

void delayMillis(std::string data)
{
    auto json = json::parse(data);
    int delay = json["delay"];
    Sleep(delay);
}

void handlePress(char *buff, int len)
{
    std::cout << buff << std::endl;
    auto json = json::parse(buff);
    for (int i = 0; i < json.size(); i++)
    {
        int type = json[i]["type"];
        std::string data = json[i]["data"];
        switch (type)
        {
        case 1:
            sendKeyboardStringInput(data);
            break;
        case 2:
            keyCombosPress(data);
            break;
        case 3:
            moveMouse(data);
            break;
        case 4:
            mouseButtonClick(data);
            break;
        case 5:
            delayMillis(data);
            break;
        default:
            break;
        }
    }
}

int main(void)
{
    WSADATA wsaData;
    int iResult;

    SOCKET ListenSocket = INVALID_SOCKET;
    SOCKET ClientSocket = INVALID_SOCKET;

    struct addrinfo *result = NULL;
    struct addrinfo hints;

    int iSendResult;
    char recvbuf[DEFAULT_BUFLEN];
    int recvbuflen = DEFAULT_BUFLEN;

    iResult = WSAStartup(MAKEWORD(2, 2), &wsaData);
    if (iResult != 0)
    {
        printf("WSAStartup failed with error: %d\n", iResult);
        return 1;
    }

    ZeroMemory(&hints, sizeof(hints));
    hints.ai_family = AF_INET;
    hints.ai_socktype = SOCK_STREAM;
    hints.ai_protocol = IPPROTO_TCP;
    hints.ai_flags = AI_PASSIVE;

    iResult = getaddrinfo(NULL, DEFAULT_PORT, &hints, &result);
    if (iResult != 0)
    {
        printf("getaddrinfo failed with error: %d\n", iResult);
        WSACleanup();
        return 1;
    }

    ListenSocket = socket(result->ai_family, result->ai_socktype, result->ai_protocol);
    if (ListenSocket == INVALID_SOCKET)
    {
        printf("socket failed with error: %ld\n", WSAGetLastError());
        freeaddrinfo(result);
        WSACleanup();
        return 1;
    }

    iResult = bind(ListenSocket, result->ai_addr, (int)result->ai_addrlen);
    if (iResult == SOCKET_ERROR)
    {
        printf("bind failed with error: %d\n", WSAGetLastError());
        freeaddrinfo(result);
        closesocket(ListenSocket);
        WSACleanup();
        return 1;
    }

    freeaddrinfo(result);

    while (1)
    {
        iResult = listen(ListenSocket, SOMAXCONN);
        if (iResult == SOCKET_ERROR)
        {
            printf("listen failed with error: %d\n", WSAGetLastError());
            closesocket(ListenSocket);
            WSACleanup();
            return 1;
        }
        printf("Waiting for connections...\n");

        ClientSocket = accept(ListenSocket, NULL, NULL);
        if (ClientSocket == INVALID_SOCKET)
        {
            printf("accept failed with error: %d\n", WSAGetLastError());
            closesocket(ListenSocket);
            WSACleanup();
            return 1;
        }

        printf("Connected...\n");

        do
        {
            ZeroMemory(recvbuf, sizeof(recvbuf));
            iResult = recv(ClientSocket, recvbuf, recvbuflen, 0);
            if (iResult > 0)
            {
                handlePress(recvbuf, iResult);
            }
            else if (iResult == 0)
            {
                printf("Connection closing...\n");
            }
            else
            {
                printf("recv failed with error: %d\n", WSAGetLastError());
                closesocket(ClientSocket);
                WSACleanup();
                return 1;
            }
        } while (iResult > 0);

        iResult = shutdown(ClientSocket, SD_SEND);
        if (iResult == SOCKET_ERROR)
        {
            printf("shutdown failed with error: %d\n", WSAGetLastError());
            closesocket(ClientSocket);
            WSACleanup();
            return 1;
        }
        closesocket(ClientSocket);
    }
    closesocket(ListenSocket);
    WSACleanup();
    return 0;
}