package eventSystem.events.local;

public class EUpdateNickname extends LocalEvent {
    private final String nickname;

    public EUpdateNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public String toString() {
        return "EUpdateServerInfo { nickname: '" + nickname + " }";
    }
}
