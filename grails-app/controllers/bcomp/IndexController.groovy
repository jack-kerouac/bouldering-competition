package bcomp

import org.springframework.security.access.annotation.Secured

@Secured('permitAll')
class IndexController {
    def index() {}
}
