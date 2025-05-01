import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

const socketUrl = 'http://localhost:8080/ws'; // Your backend WebSocket endpoint

let stompClient = null;

export const connectWebSocket = (onMessageCallback) => {
  const socket = new SockJS(socketUrl);
  stompClient = new Client({
    webSocketFactory: () => socket,
    reconnectDelay: 5000,
    onConnect: () => {
      console.log('Connected to WebSocket');
      stompClient.subscribe('/topic/analytics', (message) => {
        const body = JSON.parse(message.body);
        console.log("body: ",body);
        onMessageCallback(body);
      });
    },
    debug: (str) => console.log(str),
  });

  stompClient.activate();
};

export const disconnectWebSocket = () => {
  if (stompClient) stompClient.deactivate();
};
