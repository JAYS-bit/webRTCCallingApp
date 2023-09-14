package com.example.logup.repository;

import android.content.Context;
import android.provider.MediaStore;

import com.example.logup.remote.FirebaseClient;
import com.example.logup.utils.DataModel;
import com.example.logup.utils.DataModelType;
import com.example.logup.utils.ErrorCallBacks;
import com.example.logup.utils.NewEventCallback;
import com.example.logup.utils.SuccessCallBack;
import com.example.logup.webrtc.MyPeerConnectionObserver;
import com.example.logup.webrtc.WebRTCClient;
import com.google.gson.Gson;

import org.webrtc.IceCandidate;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.SessionDescription;
import org.webrtc.SurfaceViewRenderer;

public class MainRepository implements WebRTCClient.Listener {

    public Listener listener;
    private FirebaseClient firebaseClient;
    private String target;
    private final Gson gson= new Gson();
    private WebRTCClient webRTCClient;
    private static MainRepository instance;
    private SurfaceViewRenderer remoteView;

    private  String currentUserName;

    private MainRepository(){
        this.firebaseClient = new FirebaseClient();
    }

    private void updateCurrentUserName(String username){
        currentUserName=username;
    }
    public static MainRepository getInstance(){

        if(instance==null){
            instance= new MainRepository();
        }

        return instance;

    }

    public void login(String username, Context context, SuccessCallBack callBack){

        firebaseClient.login(username,()->{
            updateCurrentUserName(username);
            this.webRTCClient= new WebRTCClient(context,new MyPeerConnectionObserver(){
                @Override
                public void onAddStream(MediaStream mediaStream) {
                    super.onAddStream(mediaStream);
                    try{
                        mediaStream.videoTracks.get(0).addSink(remoteView);
                    }catch (Exception e){e.printStackTrace();}

                }

                @Override
                public void onConnectionChange(PeerConnection.PeerConnectionState newState) {
                    super.onConnectionChange(newState);
                    if (newState==PeerConnection.PeerConnectionState.CONNECTED && listener!=null){
                       listener.webrtcConnected();
                    }
                    if (newState==PeerConnection.PeerConnectionState.CLOSED ||
                            newState==PeerConnection.PeerConnectionState.DISCONNECTED){
                        if(listener!=null) {
                            listener.webrtcClosed();
                        }
                    }
                }

                @Override
                public void onIceCandidate(IceCandidate iceCandidate) {
                    super.onIceCandidate(iceCandidate);
                    webRTCClient.sendIceCandidate(iceCandidate,target);
                }
            },username);
            webRTCClient.listener=this;
            callBack.onSuccess();
        });
    }
    public void initLocalView(SurfaceViewRenderer view){
        webRTCClient.initLocalSurfaceView(view);
    }
    public void initRemoteView(SurfaceViewRenderer view){
        webRTCClient.initRemoteSurfaceView(view);
        this.remoteView = view;
    }
    public void startCall(String target){
        webRTCClient.call(target);
    }

    public void switchCamera() {
        webRTCClient.switchCamera();
    }

    public void toggleAudio(Boolean shouldBeMuted){
        webRTCClient.toggleAudio(shouldBeMuted);
    }
    public void toggleVideo(Boolean shouldBeMuted){
        webRTCClient.toggleVideo(shouldBeMuted);
    }
    public void sendCallRequest(String target, ErrorCallBacks errorCallBack){
        firebaseClient.sendMessageToOtherUsers(
                new DataModel(target,currentUserName,null, DataModelType.StartCall),errorCallBack
        );
    }

    public void endCall(){
        webRTCClient.closeConnection();
    }







    public void subscribeForLatestEvent(NewEventCallback callback){

        firebaseClient.observeIncomingLatestEvent(model -> {

            switch(model.getType()){
                case Offer:
                    this.target=model.getSender();
                    webRTCClient.onRemoteSessionReceived(new SessionDescription(
                            SessionDescription.Type.OFFER,model.getData()
                    ));
                    webRTCClient.answer(model.getSender());
                    break;
                case Answer:
                    this.target=model.getSender();
                    webRTCClient.onRemoteSessionReceived(new SessionDescription(
                            SessionDescription.Type.ANSWER,model.getData()
                    ));
                    break;
                case IceCandidate:
                    try{
                        IceCandidate candidate = gson.fromJson(model.getData(),IceCandidate.class);
                        webRTCClient.addIceCandidate(candidate);
                    }catch (Exception e){
                      e.printStackTrace();
                    }

                    break;
                case StartCall:
                    this.target=model.getSender();
                    callback.onNewEventReceived(model);
                    break;
            }


        });

    }

    @Override
    public void onTransferDataToOtherPeer(DataModel dataModel) {
        firebaseClient.sendMessageToOtherUsers(dataModel,()->{});
    }

    public interface Listener{
        void webrtcConnected();
        void webrtcClosed();
    }
}
