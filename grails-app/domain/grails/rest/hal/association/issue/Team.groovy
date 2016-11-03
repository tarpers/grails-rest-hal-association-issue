package grails.rest.hal.association.issue

import grails.rest.Resource

@Resource(uri = '/teams', formats = ['hal'])
class Team {
    static hasMany = [members: Member]

    String name
}
