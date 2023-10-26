import { Component } from "@angular/core";
import { TokenService } from "../core/service/token.service";

@Component({
    selector: "webrtc-page",
    templateUrl: "./webrtc.component.html"
})
export class WebrtcComponent {

    ws!: WebSocket;

    message: string = "";

    messages: Array<string> = [];
    error: any;

    peerConnection!: RTCPeerConnection;

    secondPeer!: RTCPeerConnection;

    dataChannel!: RTCDataChannel;   

    data2!: RTCDataChannel;   


    constructor(public tokenService: TokenService) {

    }

    connect() {
        this.ws = new WebSocket("ws://" + window.location.hostname + ":8081/webrtc");
        
        let peerConnection = new RTCPeerConnection();
        let dataChannel = peerConnection.createDataChannel("dataChannel");
        dataChannel.onerror = (error) => console.log("Error:", error);
        dataChannel.onclose = () => console.log("Data channel is closed");

        peerConnection.createOffer()
            .then(offer => {
                this.send(
                    {
                        event: "offer",
                        data: offer
                    }
                )
            })
            .catch();

        peerConnection.onicecandidate = (event) => {
            if (event.candidate) {
                this.send({
                    event: "candidate",
                    data: event.candidate
                })
            }
        }

        // peerConnection.addIceCandidate(new RTCIceCandidate(candidate));
    }

    disconnect() {
        if (this.ws != null) {
            this.ws.close();
        }
    }

    onKey(event: any) {
        this.message = event.target.value;
    }

    send(message:any) {
        this.ws.send(JSON.stringify(message));
    }

    makeCall() {
    }

    connectToWebsocket() {
        this.ws = new WebSocket("ws://" + window.location.hostname + ":8081/webrtc");
        this.ws.onopen = () => {this.initialize(); this.initialize2();}

        this.ws.onmessage = (msg) => {
            console.log("Got message", msg.data);
            var content = JSON.parse(msg.data);
            var data = content.data;
            this.thirdPeerOrigin = content.x;
            switch (content.event) {
            // when somebody wants to call us
            case "offer":
                this.handleOffer(data, content.x);
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
    }

    initialize() {
        this.peerConnection = new RTCPeerConnection();

        this.peerConnection.onicecandidate = (event) => {
            if (event.candidate) {
                this.send({
                    event: "candidate",
                    data: event.candidate,
                    x: this.thirdPeerOrigin
                })
            }
        }

        this.dataChannel = this.peerConnection.createDataChannel("dataChannel");
        this.dataChannel.onerror = (error) => console.log("Error:", error);
        this.dataChannel.onclose = () => console.log("Data channel is closed");
        this.dataChannel.onmessage = (event) => console.log("Message:", event.data);

        this.peerConnection.ondatachannel = (event) => {
            console.log("ondatachannel");
            console.log(this.dataChannel);
            console.log(event);
            this.dataChannel = event.channel
        };

        // this.peerConnection.addEventListener("track", async (e) => {
        //     console.log("eventlistener")
        //     let videoElem = document.getElementById("remote") as HTMLVideoElement;
        //     // event.streams.forEach(stream => alert(stream))
        //     videoElem.srcObject = e.streams[0];
        // });
        
        this.peerConnection.ontrack = (event) => {
            console.log("ON THRAKSADLWd")
            // let videoElem = document.getElementById("remote-1") as HTMLVideoElement;
            let videoElem = document.getElementById("remote-1") as HTMLVideoElement;
            // event.streams.forEach(stream => alert(stream))
            videoElem.srcObject = event.streams[0];
        };

        

        console.log("initialized")
    }

    initialize2() {
        this.secondPeer = new RTCPeerConnection();

        this.secondPeer.onicecandidate = (event) => {
            if (event.candidate) {
                this.send({
                    event: "candidate",
                    data: event.candidate,
                    x: this.thirdPeerOrigin
                })
            }
        }

        this.data2 = this.secondPeer.createDataChannel("dataChannel");
        this.data2.onerror = (error) => console.log("Error:", error);
        this.data2.onclose = () => console.log("Data channel is closed");
        this.data2.onmessage = (event) => console.log("Message:", event.data);

        this.secondPeer.ondatachannel = (event) => {
            console.log("ondatachannel");
            console.log(this.dataChannel);
            console.log(event);
            this.data2 = event.channel
        };

        // this.peerConnection.addEventListener("track", async (e) => {
        //     console.log("eventlistener")
        //     let videoElem = document.getElementById("remote") as HTMLVideoElement;
        //     // event.streams.forEach(stream => alert(stream))
        //     videoElem.srcObject = e.streams[0];
        // });
        
        this.secondPeer.ontrack = (event) => {
            console.log("ON THRAKSADLWd")
            // let videoElem = document.getElementById("remote-1") as HTMLVideoElement;
            let videoElem = document.getElementById("remote-2") as HTMLVideoElement;
            // event.streams.forEach(stream => alert(stream))
            videoElem.srcObject = event.streams[0];
        };

        

        console.log("initialized")
    }
    
    createOffer() {
        console.log("createOffer")
        let result = prompt("AAAAA");
        console.log(result);
        this.peerConnection.createOffer()
        .then(offer => {
            this.send(
                {
                    event: "offer",
                    data: offer,
                    x: result
                }
            );
            this.peerConnection.setLocalDescription(offer);
        })
        .catch();
        // if (this.peerConnection.signalingState == "stable") {
        //     this.secondPeer.createOffer()
        //     .then(offer => {
        //         this.send(
        //             {
        //                 event: "offer",
        //                 data: offer
        //             }
        //         );
        //         this.secondPeer.setLocalDescription(offer);
        //     })
        //     .catch();
        // } else {
        //     this.peerConnection.createOffer()
        //         .then(offer => {
        //             this.send(
        //                 {
        //                     event: "offer",
        //                     data: offer
        //                 }
        //             );
        //             this.peerConnection.setLocalDescription(offer);
        //         })
        //         .catch();
        // }
    }

    thirdPeerOrigin!: string;

    handleOffer(offer: any, zxc?: string) {
        console.log("handleOffer");
        console.log("Offer=", offer);
        console.log(this.peerConnection.signalingState)
        if (this.peerConnection.signalingState == "stable") {
            this.secondPeer.setRemoteDescription(new RTCSessionDescription(offer));
            console.log("XXXXXXXXXXXXXX = ", zxc)
            this.secondPeer.createAnswer()
                .then(answer => {
                    this.secondPeer.setLocalDescription(answer);
                    this.send({
                        event: "answer",
                        data: answer,
                        x: zxc
                    });
                })
                .catch(error => {
                    alert("Error creation an answer sssssss")
                });
        } else {
        this.peerConnection.setRemoteDescription(new RTCSessionDescription(offer));

        this.peerConnection.createAnswer()
            .then(answer => {
                this.peerConnection.setLocalDescription(answer);
                this.send({
                    event: "answer",
                    data: answer
                });
            })
            .catch(error => {
                alert("Error creation an answer")
            });
        }
        // this.peerConnection.createAnswer((answer: any) => {
        //     this.peerConnection.setLocalDescription(answer);
        //     this.send({
        //         event: "answer",
        //         data: answer
        //     });
        // }, (error) => {alert("Error creation an answer")}
        // );
    }

    handleCandidate(candidate: any) {
        console.log("handleCandidate");
        console.log("candidate=", candidate);

        console.log(this.peerConnection.signalingState)
        if (this.peerConnection.signalingState == "stable") {
            this.secondPeer.addIceCandidate(new RTCIceCandidate(candidate));
        } else
        this.peerConnection.addIceCandidate(new RTCIceCandidate(candidate));
    }

    handleAnswer(answer: any) {
        console.log("handleAnswer")
        if (this.peerConnection.signalingState == "stable") {
            this.secondPeer.setRemoteDescription(new RTCSessionDescription(answer));
        } else 
        this.peerConnection.setRemoteDescription(new RTCSessionDescription(answer));
        console.log("Connection established successfully!!");
    }

    sendMessage() {
        console.log(this.message)
        this.dataChannel.send(this.message);
    }



    getMediaStream() {
        const constraints = {
            video: true,audio : false
        };
        navigator.mediaDevices.getUserMedia(constraints)
            .then((stream) => {
                let localVideo = document.getElementById("local") as HTMLVideoElement;
                // localVideo.srcObject = stream;
                stream.getTracks().forEach((track) => {
                    this.peerConnection.addTrack(track, stream);
                })
            })
            .catch((error) => {
                console.log(error)
            });

        
    }
}