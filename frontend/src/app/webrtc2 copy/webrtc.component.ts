import { Component } from "@angular/core";
import { MyConnection } from "./MyConnection";
import {v4 as uuidv4} from 'uuid';
import { animate } from "@angular/animations";

@Component({
    selector: "webrtc-page",
    templateUrl: "./webrtc.component.html"
})
export class WebrtcComponent3 {


    connections: Array<MyConnection>;

    newConnection!: MyConnection;

    ws!: WebSocket;
    id: string;


    conMap:Map<string, MyConnection>;

    remoteConMap: Map<string, MyConnection>;


    constructor() {
        this.connections = new Array();
        this.id = uuidv4();
        this.conMap = new Map();
        this.remoteConMap = new Map();
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
                this.handleAnswer(data, content.to, content.from);
                break;
            // when a remote peer sends an ice candidate to us
            case "candidate":
                this.handleCandidate(data, content.to);
                break;
            default:
                break;
            }
        };

    }


    handleOffer(data: any, from: string, wsId: string) {
        console.log("handleOffer")
        if (this.newConnection == undefined || this.newConnection.getState() == 'connected') {
            console.log("creating new connection")
            let connectionId = uuidv4();
            this.newConnection = new MyConnection(connectionId, this.id, this.connections, this.ws);

            this.conMap.set(connectionId, this.newConnection);
        }

        
        this.newConnection.handleOffer(data, from, wsId);
    }

    handleAnswer(data: any, to: string, from: string) {
        console.log("handleAnswer")

        let connection = this.remoteConMap.get(from);
        if (connection == undefined) {
            connection = new MyConnection(uuidv4(), this.id, this.connections, this.ws);
            this.remoteConMap.set(from, connection)
            connection.handleAnswer(data);
        }

        if (connection != undefined) {
            console.log("from = ", from);
            console.log(connection);
            connection.handleAnswer(data);
        } else {
            console.error("cannot handle answer")
        }
    }

    handleCandidate(data: any, to: string) {
        console.log("handleCandidate")

        let connection = this.conMap.get(to);
        if (connection != undefined) {
            connection.handleCandidate(data);
        } else {
            console.error("cannot handle candidate")
            console.log("to = ", to)
            console.log("map = ", this.conMap)
        }
    }


    initialize() {
        // console.log("initialized")
        this.send({id: this.id});
        console.log("connected")
    }

    send(message:any) {
        this.ws.send(JSON.stringify(message));
    }

    createOffer() {
        let connectionId = uuidv4();
        this.newConnection = new MyConnection(connectionId, this.id, this.connections, this.ws);

        this.conMap.set(connectionId, this.newConnection);
        this.newConnection!.createOffer();
    }

    message: string = "";
    onKey(event: any) {
        this.message = event.target.value;
    }
    sendData() {
        this.conMap.forEach((value, key, map) => {
            value.sendData(this.message);
        })
        // this.newConnection.sendData(this.message);
    }
}