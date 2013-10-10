package com.bazaarvoice.hacktoberfest.timetraveler.service

import com.bazaarvoice.hacktoberfest.timetraveler.controller.DataServiceRequestContext
import com.bazaarvoice.hacktoberfest.timetraveler.service.util.Constants
import grails.converters.JSON
import grails.plugins.rest.client.RestBuilder
import org.apache.commons.lang.math.RandomUtils
import org.joda.time.DateTime
import org.joda.time.Interval

class DataService {
    static def RestBuilder rest = new RestBuilder()

    static Map<String, List<RatingOverTimeData>> getRatingOverTimeData(DataServiceRequestContext ctx) {
        final String clientName = ctx.clientName
        final def productTuples = Constants.getClientProductTuples(clientName)
        final List<Interval> intervals = getIntervals(ctx)

        return new HashMap<String, List<RatingOverTimeData>>() {{
            for (final def productTuple : productTuples) {
                List<RatingOverTimeData> ratingsOverTime = new ArrayList<RatingOverTimeData>()

                String productId = productTuple.get(0)
                String productName = productTuple.get(1)
                ratingsOverTime.addAll(buildVolumeTuples(clientName, productId, intervals))
                ratingsOverTime.addAll(buildConversionTuples(clientName, productId, intervals))

                put(productName, ratingsOverTime)
            }
        }}
    }

    static Collection<RatingOverTimeData> buildConversionTuples(String clientName, String productId, final List<Interval> intervals) {
        // LUVEEN TODO use real transaction data to compute conversion
        return new ArrayList<RatingOverTimeData>() {{
            for (def interval : intervals)
            add(new RatingOverTimeData(
                    Metric.RoI,
                    keyFromInterval(interval),
                    RandomUtils.nextInt(4) + 1
            ))
        }}
    }

    /*for (def productId : productIds) {
                String reviewsString = rest.get(Constants.getClientReviewsUrl(clientName, productId))
                String pageviewsString = rest.get(Constants.getClientPageviewsUrl(clientName, ))
                JSONObject reviewsJson = JSON.parse(reviewsString)
                JSONObject pageviewsJson = JSON.parse(pageviewsString)
                List<Review> reviews = new ArrayList<Review>()
                List<Review> pageviews = new ArrayList<Pageview>()

                reviewsJson.each { id, review -> reviews.add(review.Rating) }
                pageviewsJson.each { id, pageview -> pageviews.add(pageview.pageTypes.product.withReviews) }

            }*/
    private static Collection<RatingOverTimeData> buildVolumeTuples(String clientName, String productId, final List<Interval> intervals) {
        def response = rest.get(Constants.getClientReviewsUrl(clientName, productId))
        def reviewsJson = JSON.parse(response)
        System.out.println("reviewsString: " + response.json);
        System.out.println("reviewsJson: " + reviewsJson);
        Map<Interval, Integer> volumes = new HashMap<Interval, Integer>() {{
            for (def interval : intervals) {
                put(interval, 0)
            }
        }}

        reviewsJson.each { id, review -> def d = new DateTime(review.SubmissionTime); def i = new Interval(d, d.plusDays(1)); volumes.put(i, volumes.get(i) + 1) }
        accumulateVolumes(volumes)

        return new ArrayList<RatingOverTimeData>() {{
            for (def interval : volumes.keySet()) {
                def value = volumes.get(interval)
                add(new RatingOverTimeData(
                        Metric.Volume,
                        keyFromInterval(interval),
                        value
                ))
            }
        }}
    }

    private static void accumulateVolumes(HashMap volumes) {
        List<Interval> aggregatedIntervals = new ArrayList<Interval>()

        aggregatedIntervals.addAll(volumes.keySet())

        Collections.sort(aggregatedIntervals, new Comparator<Interval>() {
            int compare(Interval t, Interval t1) {
                return t.start.compareTo(t1.start)
            }
        })

        int total = 0;
        Interval prevInterval = null
        for (def i : aggregatedIntervals) {
            total += volumes.get(i)

            if (prevInterval != null) {
                volumes.put(i, total)
            }
        }
    }

    private static List<Interval> getIntervals(DataServiceRequestContext ctx) {
        // LUVEEN TODO actually use ctx?
        return new ArrayList<Interval>() {{
            DateTime end = DateTime.parse("2013-09-30")
            DateTime start = DateTime.parse("2013-09-01")

            for (DateTime i = start; i <= end; i = i.plusDays(1)) {
                add new Interval(i, i.plusDays(1))
            }
        }}
    }

    private static keyFromInterval(Interval interval) {
        return new Tuple(
                interval.start.year().get(),
                interval.start.monthOfYear().get() - 1,
                interval.start.dayOfMonth().get())
    }

    // get all reviews on the product
    // bucket by time interval
    // for each interval, calculate average rating over an interval starting at the earliest start date, ending at the end date for the interval
}

class RatingOverTimeData {
    /*
       {
           ["ROI", date, metric],
           ["RvV", date, metric],
           ["Vol", date, metric],
       }
    */
    Metric key
    Tuple yearMonthDay
    Number value

    RatingOverTimeData(Metric key, Tuple yearMonthDay, Number value) {
        this.key = key
        this.yearMonthDay = yearMonthDay
        this.value = value
    }
}

enum Metric {
    RoI("RoI"),
//    RvV("RvV"),
    Volume("Vol")

    private String label

    Metric(String label) {
        this.label = label
    }

    public String getLabel() {
        return label
    }

    @Override
    String toString() {
        return getLabel()
    }
}