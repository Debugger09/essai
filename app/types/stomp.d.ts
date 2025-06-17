declare module '@stomp/stompjs' {
  export interface IMessage {
    body: string;
    headers: { [key: string]: string };
  }

  export class Client {
    constructor(config: {
      brokerURL: string;
      debug?: (str: string) => void;
      reconnectDelay?: number;
      heartbeatIncoming?: number;
      heartbeatOutgoing?: number;
    });

    onConnect?: () => void;
    onStompError?: (frame: { headers: { [key: string]: string }; body: string }) => void;

    activate(): void;
    deactivate(): void;
    subscribe(destination: string, callback: (message: IMessage) => void): void;
  }
} 