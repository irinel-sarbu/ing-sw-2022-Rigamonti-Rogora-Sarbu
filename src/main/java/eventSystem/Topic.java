package eventSystem;

public class Topic {
    private static final String TOPIC_REGEX = "(^\\*$)|(^([A-Za-z]+)(\\.[A-Za-z]+)*)(\\.\\*$)?";

    private final TopicSection topic;

    public Topic(final String topic) {
        if (!topic.matches(TOPIC_REGEX)) {
            throw new IllegalArgumentException("String " + topic + " is not a valid topic");
        }

        this.topic = compile(topic);
    }

    private TopicSection compile(final String topic) {
        String[] splitTopic = topic.split("\\.");

        TopicSection newTopicSection = null;
        for (int i = splitTopic.length - 1; i >= 0; i--) {
            newTopicSection = new TopicSection(splitTopic[i], newTopicSection);
        }

        return newTopicSection;
    }

    public boolean contains(final String topic) {
        if (topic.contains("*")) {
            return false;
        }

        TopicSection match = compile(topic);
        return this.topic.contains(match);
    }

    private record TopicSection(String section, Topic.TopicSection subSection) {
        private boolean contains(final TopicSection section) {
            if (this.section.equals("*")) {
                return true;
            }

            if (section == null) {
                return false;
            }

            if (this.subSection == null) {
                return section.subSection == null;
            }

            return this.section.equals(section.section) && this.subSection.contains(section.subSection);
        }
    }
}
