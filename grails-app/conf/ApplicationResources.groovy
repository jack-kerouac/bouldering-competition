modules = {
    jquery {
        // JQUERY AND PLUGINS
        resource url: 'js/vendor/jquery.js'
        resource url: 'js/vendor/jquery.flot.js'
        resource url: 'js/vendor/jquery.flot.time.js'
    }
    foundation {
        // FOUNDATION AND PLUGINS
        resource url: 'js/vendor/custom.modernizr.js', disposition: 'head'
        resource url: 'js/foundation/foundation.js'
        resource url: 'js/foundation/foundation.topbar.js'

        resource url: 'css/normalize.css'
        resource url: 'css/foundation.min.css'
        resource url: 'css/foundation-icons.css'
    }
    angular {
        resource url: 'js/vendor/angular.js'
        resource url: 'js/vendor/angular-resource.js'
    }
    leaflet {
        // LEAFLET AND PLUGINS
        resource url: 'js/vendor/leaflet-src.js'
        resource url: 'css/leaflet.css'
    }
    application {
        dependsOn 'jquery', 'foundation', 'angular', 'leaflet'
        resource url: 'js/application.js'
        resource url: 'js/services.js'
        resource url: 'js/controllers.js'

        resource url: 'css/application.css'
    }
}