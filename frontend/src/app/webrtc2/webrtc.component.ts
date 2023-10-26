import { Component } from "@angular/core";
import { MyConnection } from "./MyConnection";
import {v4 as uuidv4} from 'uuid';

@Component({
    selector: "webrtc-page",
    templateUrl: "./webrtc.component.html"
})
export class WebrtcComponent2 {


    connections: Array<MyConnection>;

    newConnection!: MyConnection;

    ws!: WebSocket;
    id: string;

    constructor() {
        this.connections = new Array();
        this.id = uuidv4();
    }

    connectToWebsocket() {
        this.ws = new WebSocket("ws://" + window.location.hostname + ":8081/webrtc2");
        this.ws.onopen = () => {this.initialize();}

        this.ws.onmessage = (msg) => {
            console.log("Got message", msg.data);
            var content = JSON.parse(msg.data);
            var data = content.data;
            switch (content.event) {
            // when somebody wants to call us
            case "offer":
                this.handleOffer(data, content.from, content.wsId);
                break;
            case "answer":
                this.handleAnswer(data);
                break;
            // when a remote peer sends an ice candidate to us
            case "candidate":
                this.handleCandidate(data);
                break;
            default:
                break;
            }
        };

        this.newConnection = new MyConnection(uuidv4(), this.id, this.connections, this.ws);
    }


    handleOffer(data: any, from: string, wsId: string) {
        console.log("handleOffer")
        if (this.newConnection?.getState() == 'connected') {
            this.newConnection = new MyConnection(uuidv4(), this.id, this.connections, this.ws)
        }
        this.newConnection.handleOffer(data, from, wsId);
    }

    handleAnswer(data: any) {
        console.log("handleAnswer")
        if (this.newConnection?.getState() == 'connected') {
            this.newConnection = new MyConnection(uuidv4(), this.id, this.connections, this.ws)
        }
        this.newConnection.handleAnswer(data, () => {});
    }

    handleCandidate(data: any) {
        console.log("handleCandidate")
        this.newConnection.handleCandidate(data);
    }


    initialize() {
        // console.log("initialized")
        this.send({id: this.id});
    }

    send(message:any) {
        this.ws.send(JSON.stringify(message));
    }

    createOffer() {
        this.newConnection!.createOffer();
    }
}