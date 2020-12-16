package alroy214.xkik.enums;

public enum Message {
    MESSAGE_HINT("activity_new_message_hint"),
    MESSAGE_TYPING("is_typing_");

    // declaring private variable for getting values
    private String string;

    // getter method
    public String getString()
    {
        return this.string;
    }

    // enum constructor - cannot be public or protected
    private Message(String string)
    {
        this.string = string;
    }
}
