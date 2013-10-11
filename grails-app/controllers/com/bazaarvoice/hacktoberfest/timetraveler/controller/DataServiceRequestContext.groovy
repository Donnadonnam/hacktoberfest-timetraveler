package com.bazaarvoice.hacktoberfest.timetraveler.controller

class DataServiceRequestContext {
    String clientName
    String productId

    DataServiceRequestContext(String clientName, String productId) {
        this.clientName = clientName
        this.productId = productId
    }
}
