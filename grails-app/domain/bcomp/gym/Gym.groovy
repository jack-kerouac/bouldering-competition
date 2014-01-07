package bcomp.gym

/**
 * User: florian
 */
class Gym {

    String name

    static hasMany = [sections: Section, floorPlans: FloorPlan]

    static mapping = {
        sections lazy: false, fetch: 'join'
    }

    static constraints = {
        name blank: false
    }

    Gym(String name) {
        this.name = name
        this.sections = [] as Set
        this.floorPlans = [] as Set
    }

}
