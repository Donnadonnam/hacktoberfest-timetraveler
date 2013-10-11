package com.bazaarvoice.hacktoberfest.timetraveler.service

import com.bazaarvoice.hacktoberfest.timetraveler.controller.DataServiceRequestContext
import com.bazaarvoice.hacktoberfest.timetraveler.service.util.Constants
import grails.plugins.rest.client.RestBuilder
import org.apache.commons.lang.StringUtils
import org.apache.commons.lang.builder.ToStringBuilder
import org.apache.commons.lang.math.RandomUtils
import org.joda.time.DateTime
import org.joda.time.Interval

class DataService {
    static def RestBuilder rest = new RestBuilder()
    static Map<String, Map<String, List<String>>> CACHE = new HashMap<>()

    static Map<String, List<String>> getRatingOverTimeData(DataServiceRequestContext ctx) {
        final String clientName = ctx.clientName
        assert(StringUtils.isNotEmpty(clientName))

        if (CACHE.containsKey(clientName)) {
            return CACHE.get(clientName)
        }

        final List<Interval> intervals = get30DayIntervals(ctx)
        final def productTuples = Constants.getClientProductTuples(clientName)

        final result = new HashMap<String, List<String>>() {{
            for (final def productTuple : productTuples) {
                List<String> ratingsOverTime = new ArrayList<String>()

                String productId = productTuple.get(0)
                String productName = productTuple.get(1)
                ratingsOverTime.addAll(buildVolumeTuples(clientName, productId, intervals))
                ratingsOverTime.addAll(buildConversionTuples(clientName, productId, intervals))

                put(productName, ratingsOverTime)
            }
        }}

        CACHE.put(clientName, result)
        return result
    }

    static Collection<String> buildConversionTuples(String clientName, String productId, final List<Interval> intervals) {
        // LUVEEN TODO use real transaction data to compute conversion
        return new ArrayList<String>() {{
            for (def interval : intervals)
            add(new RatingOverTimeData(
                    Metric.RoI,
                    keyFromInterval(interval),
                    RandomUtils.nextInt(4) + 1
            ).toString())
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
    private static Collection<String> buildVolumeTuples(String clientName, String productId, final List<Interval> intervals) {
        Map<Interval, Integer> volumes = new LinkedHashMap<Interval, Integer>() {{
            for (def interval : intervals) {
                put(interval, 0)
            }
        }}

        /*System.out.println("Keys in volumes map:")
        for (final k : volumes.keySet()) {
            System.out.println(k)
        }*/

        for (def interval : intervals) {
            def response = rest.get(Constants.getClientReviewsUrl(clientName, productId, interval))
            def reviewsJson = response?.json?.Results

//            System.out.println("reviewsString: " + response.json);
//            System.out.println("reviewsJson: " + reviewsJson);

            for (final review in reviewsJson) {
//                System.out.println(review)
                final volume = volumes.get(interval)
//                System.out.println("i: " + interval + ", volume: " + volume)
                volumes.put(interval, volume + 1)
            }
//            reviewsJson.each { id, review -> System.out.println(review); def d = new DateTime(review.SubmissionTime); def i = get30DayIntervalEndingAt(d); volumes.put(i, volumes.get(i) + 1) }
        }
//        accumulateVolumes(volumes)

        return new ArrayList<String>() {{
            for (def interval : volumes.keySet()) {
                def value = volumes.get(interval)
                add(new RatingOverTimeData(
                        Metric.Volume,
                        keyFromInterval(interval),
                        value
                ).toString())
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

    private static List<Interval> get30DayIntervals(DataServiceRequestContext ctx) {
        // LUVEEN TODO actually use ctx?
        return new ArrayList<Interval>() {{
            DateTime end = DateTime.parse("2013-09-30")
            DateTime start = DateTime.parse("2013-09-01")

            for (DateTime i = start; i <= end; i = i.plusDays(1)) {
                add get30DayIntervalEndingAt(i)
            }
        }}
    }

    private static Interval get30DayIntervalEndingAt(DateTime i) {
        DateTime x = getStartOfDay(i)
        return new Interval(x.minusDays(30), x)
    }

    private static Interval get30DayIntervalStartingAt(DateTime i) {
        DateTime x = getStartOfDay(i)
        return new Interval(x, x.plusDays(30))
    }

    private static DateTime getStartOfDay(DateTime i) {
        i.withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0)
    }

    private static keyFromInterval(Interval interval) {
        return new Tuple(
                interval.end.year().get(),
                interval.end.monthOfYear().get() - 1,
                interval.end.dayOfMonth().get())
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

    private String yearMonthDayToString()  {
        final def result = String.valueOf(yearMonthDay.get(0)) + "-" + String.valueOf(yearMonthDay.get(1)) + "-" + String.valueOf(yearMonthDay.get(2))
        return result
    }

    @Override
    String toString() {
        return "[" + key.toString() + ", " + yearMonthDayToString() + ", " + value + "]"
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