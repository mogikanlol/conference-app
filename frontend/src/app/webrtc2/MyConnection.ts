
export class MyConnection {

    connections: Array<MyConnection>;

    ws: WebSocket;

    peerConnection!: RTCPeerConnection;
    dataChannel!: RTCDataChannel;   
    wsId: string;

    connectionid: string;
    
    remote_id!: string;

    constructor(connectionId: string, id: string, connections: Array<MyConnection>, ws: WebSocket) {
        this.connections = connections;
        this.wsId = id;
        this.ws = ws;
        this.connectionid = connectionId;
        

        // this.ws.onopen = () => {this.initialize();}
        this.initialize();
    }

    initialize() {
        this.peerConnection = new RTCPeerConnection();

        // this.peerConnection.onicecandidate = (event) => {
        //     if (event.candidate) {
        //         this.send({
        //             event: "candidate",
        //             data: event.candidate
        //             ,
        //             origin_id: this.remote_id
        //         })
        //     }
        // }

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

        let remoteVideo = document.createElement("video");
        document.getElementById("video-div")?.appendChild(remoteVideo);
        remoteVideo.autoplay = true;

        const constraints = {
            video: true,audio : false
        };
        navigator.mediaDevices.getUserMedia(constraints)
            .then((stream) => {
                // let localVideo = document.getElementById("local") as HTMLVideoElement;
                // localVideo.srcObject = stream;
                // remoteVideo.srcObject = stream;
                stream.getTracks().forEach((track) => {
                    this.peerConnection.addTrack(track, stream);
                })
                
            })
            .catch((error) => {
                console.log(error)
            });

        this.peerConnection.ontrack = (event) => {
            console.log("ON THRAKSADLWd")
            // let videoElem = document.getElementById("remote") as HTMLVideoElement;
            remoteVideo.srcObject = event.streams[0];
        };

        

        console.log("initialized")
        // this.send({id: this.id});
    }


    createOffer() {
        console.log("createOffer")
        this.peerConnection.createOffer()
            .then(offer => {
                this.send(
                    {
                        event: "offer",
                        data: offer,
                        from: this.wsId,
                        wsId: this.wsId
                    }
                );
                this.peerConnection.setLocalDescription(offer);
            })
            .catch();
    }

    handleOffer(offer: any, from: string, wsId: string) {
        console.log("handleOffer")
        this.peerConnection.onicecandidate = (event) => {
            if (event.candidate) {
                this.send({
                    event: "candidate",
                    data: event.candidate,
                    to: from,
                    from: this.connectionid,
                    wsId: wsId
                })
            }
        }
        this.peerConnection.setRemoteDescription(new RTCSessionDescription(offer));

        this.peerConnection.createAnswer()
            .then(answer => {
                this.peerConnection.setLocalDescription(answer);
                this.send({
                    event: "answer",
                    data: answer,
                    to: from,
                    from: this.connectionid,
                    wsId: wsId
                });
            })
            .catch(error => {
                alert("Error creation an answer")
            });
        
    }

    handleCandidate(candidate: any) {
        console.log("handleCandidate")
        console.log(candidate)
        this.peerConnection.addIceCandidate(new RTCIceCandidate(candidate));
    }

    handleAnswer(answer: any, callback: any) {
        this.peerConnection.setRemoteDescription(new RTCSessionDescription(answer));
        console.log("Connection established successfully!!");
        callback();
    }

    send(message:any) {
        this.ws.send(JSON.stringify(message));
    }

    getState() {
        return this.peerConnection.connectionState;
    }
}