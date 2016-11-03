import grails.rest.hal.association.issue.CustomHalJsonCollectionRenderer
import grails.rest.render.hal.HalJsonCollectionRenderer
import grails.rest.render.hal.HalJsonRenderer

beans = {
    halTeamRenderer(HalJsonRenderer, grails.rest.hal.association.issue.Team)
    halTeamCollectionRenderer(HalJsonCollectionRenderer, grails.rest.hal.association.issue.Team) {
        collectionName = 'teams'
    }

    halMemberRenderer(HalJsonRenderer, grails.rest.hal.association.issue.Member)
    halMemberCollectionRenderer(CustomHalJsonCollectionRenderer, grails.rest.hal.association.issue.Member) {
        collectionName = 'members'
    }
}