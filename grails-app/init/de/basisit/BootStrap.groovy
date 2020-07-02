package de.basisit

import de.basisit.conferences.User

class BootStrap {

    def init = { servletContext ->
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
        User u = new User(username: "admin", password: "admin").save()
    }

    def destroy = {
    }
}
