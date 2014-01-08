package bcomp.gym

/**
 * User: florian
 */
class Gym {

    String name

    static hasMany = [boulders: Boulder, floorPlans: FloorPlan]

    static constraints = {
        name blank: false
    }

    Gym(String name) {
        this.name = name
        this.boulders = [] as Set
        this.floorPlans = [] as Set
    }

}
