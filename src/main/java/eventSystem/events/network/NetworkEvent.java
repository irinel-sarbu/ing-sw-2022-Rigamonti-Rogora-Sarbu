package eventSystem.events.network;

import eventSystem.events.Event;

public abstract class NetworkEvent extends Event {
    private String nickname;

    public void setClientNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getClientNickname() {
        return nickname;
    }
}
