package grails.rest.hal.association.issue

import grails.rest.Resource

@Resource(uri = '/members', formats = ['hal'])
class Member {
    static belongsTo = [team: Team]

    String name
}
