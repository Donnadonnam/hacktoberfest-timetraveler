package com.bazaarvoice.hacktoberfest.timetraveler.controller

import com.bazaarvoice.hacktoberfest.timetraveler.service.DataService
import grails.converters.JSON
import groovy.json.JsonOutput

import javax.servlet.http.HttpServletRequest

class ReviewDataController {

    static allowedMethods = [index: 'GET']

    def index() {
        // parse request params to inform service calls
        // get request params like so:
        // groovy_data_type varx = request[request_param_name] as groovy_data_type
        final DataServiceRequestContext ctx = getRequestContext(request)
        final def reviewData = DataService.getRatingOverTimeData(ctx)
        System.out.println(JsonOutput.toJson(reviewData))
        render reviewData as JSON
    }

    private static DataServiceRequestContext getRequestContext(HttpServletRequest request) {
        final String clientName = request['clientName']
        return new DataServiceRequestContext(clientName)
    }
}
