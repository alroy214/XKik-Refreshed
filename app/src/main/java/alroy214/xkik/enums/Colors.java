package alroy214.xkik.enums;

public enum Colors {


    // Color Statics
    COLOR_CODE_BACKGROUND("background"),
    COLOR_CODE_TOOLBAR("gray_1"),
    COLOR_CODE_TERTIARY("gray_4"), //Tertiary Text
    COLOR_CODE_SECONDARY("gray_5"), //Secondary Text
    COLOR_CODE_PRIMARY("gray_6"), //Primary Text
    COLOR_CODE_WHITE("white"),
    COLOR_CODE_INNER_WAVE("wave"),
    COLOR_CODE_OUTGOING("outgoing"),
    COLOR_CODE_INCOMING("incoming"),
    COLOR_CODE_EXPRESSION("expression_bar_background_color");

    // declaring private variable for getting values
    private String color;

    // getter method
    public String getColor()
    {
        return this.color;
    }

    // enum constructor - cannot be public or protected
    private Colors(String color)
    {
        this.color = color;
    }

}
