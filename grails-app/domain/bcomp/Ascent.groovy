package bcomp

class Ascent {

    enum Style {
        flash, top
    }

    Date date
    Style style
    int tries

    static belongsTo = [boulder: Boulder]

    static constraints = {
        boulder nullable: false
        tries validator: { val, obj -> obj.style == Style.flash ? val == 1 : val >= 2}
    }
}
