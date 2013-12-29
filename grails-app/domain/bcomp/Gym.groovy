package bcomp

/**
 * User: florian
 */
class Gym {

    String name

    static hasMany = [sections: Section]

    static mapping = {
        sections lazy: false, fetch: 'join'
    }

    static constraints = {
        name blank: false
    }

    Gym(String name) {
        this.name = name
        this.sections = [] as Set
    }

    public void addSection(Section section) {
        section.gym = this;
        sections.add(section);
    }

}
