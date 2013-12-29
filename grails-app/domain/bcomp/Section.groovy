package bcomp

class Section {

    String name

    static belongsTo = [gym: Gym]

    static constraints = {
        name blank: false
    }

}
