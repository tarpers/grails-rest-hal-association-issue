package grails.rest.hal.association.issue

class BootStrap {

    def init = { servletContext ->
        new Team(name: 'Team A')
            .addToMembers(name: 'Member A')
            .addToMembers(name: 'Member B')
            .save()
    }

    def destroy = {
    }
}
