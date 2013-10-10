package com.bazaarvoice.hacktoberfest.timetraveler.domain

class Product {
    String productExternalId
    String productId

    static constraints = {
        productExternalId blank: false
        productId blank: false, unique: true
    }
}
