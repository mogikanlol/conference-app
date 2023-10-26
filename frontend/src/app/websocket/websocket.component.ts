import { Component } from "@angular/core";
import { TokenService } from "../core/service/token.service";

@Component({
    selector: "websocket-page",
    templateUrl: "./websocket.component.html"
})
export class WebsocketComponent {

    ws!: WebSocket;

    message: string = "";

    messages: Array<string> = [];
    error: any;

    constructor(public tokenService: TokenService) {

    }

    connect() {
        try {
            this.ws = new WebSocket("ws://" + window.location.hostname + ":8081/websocket");
            this.ws.onmessage = (data) => {
                this.messages.push(data.data)
                console.log(data);
            }
            this.ws.onerror = (e) => {
                alert("Error" + e.target);
                console.log(e);
            }
            console.log(this.ws)
            alert(this.ws.url);
        } catch (e) {
            this.error = e;
        }
    }

    disconnect() {
        if (this.ws != null) {
            this.ws.close();
        }
    }

    onKey(event: any) {
        this.message = event.target.value;
    }

    send() {
        // console.log(this.message);
        try {
            this.ws.send(this.message);
        } catch(e) {
            this.error = e;
        }
    }

    makeCall() {
    }
}