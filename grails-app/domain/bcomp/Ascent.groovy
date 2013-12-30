package bcomp

import bcomp.aaa.User

class Ascent {

    enum Style {
        flash, top
    }

    Date date
    Style style
    int tries

    static belongsTo = [boulder: Boulder, boulderer: User]

    static constraints = {
        boulder nullable: false
        tries validator: { val, obj -> obj.style == Style.top ? val >= 2 : true}
    }
}
