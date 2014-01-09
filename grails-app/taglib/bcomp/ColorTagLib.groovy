package bcomp

class ColorTagLib {
    static defaultEncodeAs = 'html'

    /**
     * @attr color  REQUIRED    The color to output as CSS rgb value
     */
    def rgb = { attrs, body ->
        out << "rgb($attrs.color.red, $attrs.color.green, $attrs.color.blue)"
    }

}
