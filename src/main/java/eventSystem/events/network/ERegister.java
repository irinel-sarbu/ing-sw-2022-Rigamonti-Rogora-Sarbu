package eventSystem.events.network;

import eventSystem.events.Event;

/**
 * Special Event. Used when client is connecting to server to register himself.
 */
public class ERegister extends Event {
    private final String nickname;

    /**
     * Default constructor
     *
     * @param nickname to register
     */
    public ERegister(final String nickname) {
        this.nickname = nickname;
    }

    /**
     * Nickname getter
     *
     * @return nickname
     */
    public String getNickname() {
        return nickname;
    }

    @Override
    public String toString() {
        return "ERegister {  nickname: '" + nickname + "' }";
    }
}
