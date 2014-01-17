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

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof Gym)) return false

        Gym gym = (Gym) o

        if (name != gym.name) return false

        return true
    }

    int hashCode() {
        return (name != null ? name.hashCode() : 0)
    }
}
