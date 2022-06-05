package eventSystem.events.network;

import eventSystem.events.Event;

/**
 * Abstract class for every Event that has to be transmitted from client to server
 */
public abstract class NetworkEvent extends Event {
    private String nickname;

    /**
     * Setter for sender nickname
     *
     * @param nickname sender nickname
     */
    public void setClientNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Getter for sender nickname
     */
    public String getClientNickname() {
        return nickname;
    }
}
