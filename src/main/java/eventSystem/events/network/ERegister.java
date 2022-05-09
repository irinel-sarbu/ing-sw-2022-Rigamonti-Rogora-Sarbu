package eventSystem.events.network;

import eventSystem.events.Event;

public class ERegister extends Event {
    private final String nickname;

    public ERegister(final String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public String toString() {
        return "ERegister {  nickname: '" + nickname + "' }";
    }
}
