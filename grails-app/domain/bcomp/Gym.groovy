package bcomp

/**
 * User: florian
 */
class Gym {

    String name

    static hasMany = [sections: Section]

    static constraints = {
        name blank: false
    }

}
