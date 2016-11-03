package grails.rest.hal.association.issue

import grails.core.support.proxy.EntityProxyHandler
import grails.rest.Link
import grails.rest.render.RenderContext
import grails.rest.render.hal.HalJsonCollectionRenderer
import grails.web.mime.MimeType
import org.grails.datastore.mapping.model.PersistentEntity
import org.grails.datastore.mapping.model.types.Association
import org.grails.datastore.mapping.model.types.Basic
import org.grails.datastore.mapping.model.types.Embedded
import org.grails.datastore.mapping.model.types.ToOne
import org.springframework.http.HttpMethod

class CustomHalJsonCollectionRenderer extends HalJsonCollectionRenderer {

    CustomHalJsonCollectionRenderer(Class componentType) {
        super(componentType)
    }

    CustomHalJsonCollectionRenderer(Class componentType, MimeType... mimeTypes) {
        super(componentType, mimeTypes)
    }

    @Override
    protected Map<Association, Object> writeAssociationLinks(RenderContext context, Object object, Locale locale, Object writer, PersistentEntity entity, MetaClass metaClass) {
        writeExtraLinks(object, locale, writer)


        Map<Association, Object> associationMap = [:]
        for (Association a in entity.associations) {
            final propertyName = a.name
            if (!shouldIncludeProperty(context,object, propertyName)) {
                continue
            }
            final associatedEntity = a.associatedEntity
            if (!associatedEntity) {
                continue
            }
            if (proxyHandler.isInitialized(object, propertyName)) {
                if (a instanceof ToOne) {
                    final value = proxyHandler.unwrapIfProxy(metaClass.getProperty(object, propertyName))
                    if (a instanceof Embedded) {
                        // no links for embedded
                        associationMap[a] = value
                    } else if (value != null) {
                        final href = linkGenerator.link(resource: value, method: HttpMethod.GET, absolute: absoluteLinks)
                        final associationTitle = getLinkTitle(associatedEntity, locale)
                        final link = new Link(propertyName, href)
                        link.title = associationTitle
                        link.hreflang = locale
                        writeLink(link, locale, writer)
                        associationMap[a] = value
                    }
                } else if (!(a instanceof Basic)) {
                    associationMap[a] = metaClass.getProperty(object, propertyName)
                }

            } else if ((a instanceof ToOne) && (proxyHandler instanceof EntityProxyHandler)) {
                if (associatedEntity) {
                    final owner = a.owner
                    final proxy = mappingContext.getEntityReflector(owner).getProperty(object, propertyName)
                    final id = proxyHandler.getProxyIdentifier(proxy)
                    final href = linkGenerator.link(resource: associatedEntity.decapitalizedName, id: id, method: HttpMethod.GET, absolute: absoluteLinks)
                    final associationTitle = getLinkTitle(associatedEntity, locale)
                    def link = new Link(propertyName, href)
                    link.title = associationTitle
                    link.hreflang = locale
                    writeLink(link, locale, writer)
                }
            }
        }
        associationMap
    }
}
