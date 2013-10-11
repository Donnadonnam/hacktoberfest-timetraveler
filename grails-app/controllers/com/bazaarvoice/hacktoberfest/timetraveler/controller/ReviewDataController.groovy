package com.bazaarvoice.hacktoberfest.timetraveler.controller

import com.bazaarvoice.hacktoberfest.timetraveler.service.DataService
import grails.converters.JSON
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsParameterMap

import javax.servlet.http.HttpServletRequest

class ReviewDataController {

    static allowedMethods = [index: 'GET']

    def index() {
        // parse request params to inform service calls
        // get request params like so:
        // groovy_data_type varx = request[request_param_name] as groovy_data_type
        final DataServiceRequestContext ctx = getRequestContext(params)
        final def roiData = DataService.getRoiData(ctx)

        render roiData as JSON
    }

    private static DataServiceRequestContext getRequestContext(GrailsParameterMap params) {
        final String clientName = params['clientName']
        final String productId = params['productId']
        return new DataServiceRequestContext(clientName, productId)
    }
}
