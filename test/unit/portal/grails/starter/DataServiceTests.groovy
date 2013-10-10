package portal.grails.starter

import com.bazaarvoice.hacktoberfest.timetraveler.controller.DataServiceRequestContext
import com.bazaarvoice.hacktoberfest.timetraveler.service.DataService
import grails.test.mixin.TestFor
import groovy.json.JsonOutput

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(DataService)
class DataServiceTests {

    void testSomething() {
        final def reviewData = DataService.getRatingOverTimeData(new DataServiceRequestContext("verizon"))
        System.out.println(JsonOutput.toJson(reviewData))
    }
}
