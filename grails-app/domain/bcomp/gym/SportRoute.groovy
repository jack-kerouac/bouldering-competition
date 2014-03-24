package bcomp.gym

class SportRoute extends Route {

    static mapping = {
        discriminator "sport-route"
    }

    static embedded = ['initialGrade']


    SportGrade initialGrade

    public SportRoute() {
        this.initialGrade = SportGrade.zero();
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof SportRoute)) return false

        SportRoute sportRoute = (SportRoute) o

        if (id != sportRoute.id) return false

        return true
    }

    int hashCode() {
        return (id != null ? id.hashCode() : 0)
    }
}
