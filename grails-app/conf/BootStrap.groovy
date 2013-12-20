import bcomp.Gym
import bcomp.Section

class BootStrap {

    def init = { servletContext ->
        Gym hg = new Gym(name: 'Heavens Gate')
        hg.sections = [] as Set
        hg.sections.add(new Section(name: 'Panic Room'))
        hg.sections.add(new Section(name: 'Großes Dach'))
        hg.sections.add(new Section(name: 'Grotte'))
        hg.save()
    }
    def destroy = {
    }
}
