package bcomp.gym

class Boulder {

    static constraints = {
        grade nullable: false
        locationDescription nullable: true
    }

    static belongsTo = [gym: Gym]

    String locationDescription

    String grade

}
