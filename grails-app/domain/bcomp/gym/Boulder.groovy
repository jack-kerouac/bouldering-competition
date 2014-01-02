package bcomp.gym

class Boulder {

    static constraints = {
        grade nullable: false
        locationDescription nullable: true
    }

    static belongsTo = [section: Section]

    String locationDescription = ""

    String grade

}
