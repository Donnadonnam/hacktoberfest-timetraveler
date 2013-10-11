package com.bazaarvoice.hacktoberfest.timetraveler.service

import com.bazaarvoice.hacktoberfest.timetraveler.controller.DataServiceRequestContext
import com.bazaarvoice.hacktoberfest.timetraveler.service.util.Constants
import grails.plugins.rest.client.RestBuilder
import org.apache.commons.lang.StringUtils
import org.apache.commons.lang.math.RandomUtils
import org.joda.time.DateTime
import org.joda.time.Interval

class DataService {
    static def RestBuilder rest = new RestBuilder()
    static Map<Tuple, List<RatingOverTimeData>> CACHE = new HashMap<>()

    static Collection<Tuple> getRoiData(DataServiceRequestContext ctx) {
        final temp = getRatingOverTimeData(ctx)

        return new ArrayList<Tuple>() {{
            for (final datum in temp) {
                def tuple = new Tuple()
                tuple.set(0, datum.getKey().toString())
                tuple.set(1, datum.getYearMonthDay())
                tuple.set(2, datum.getValue())
                add(tuple)
            }
        }}
    }

    static List<RatingOverTimeData> getRatingOverTimeData(DataServiceRequestContext ctx) {
        final String clientName = ctx.clientName
        final String productId = ctx.productId
        assert(StringUtils.isNotEmpty(clientName) && StringUtils.isNotEmpty(productId))
        final cacheKey = new Tuple(clientName, productId)

        if (CACHE.containsKey(cacheKey)) {
            return CACHE.get(clientName)
        }

        final List<Interval> intervals = get30DayIntervals(ctx)

        final result = new ArrayList<RatingOverTimeData>() {{
            final volumeTuples = buildVolumeTuples(clientName, productId, intervals)
            final roiTuples = buildROITuples(clientName, productId, intervals)

            addAll(volumeTuples)
            addAll(roiTuples)
            addAll(buildRVVTuples(clientName, productId, intervals, volumeTuples, roiTuples))
        }}

        CACHE.put(cacheKey, result)
        return result
    }

    static List<String> tuplesToStrings(Collection<RatingOverTimeData> data) {
        return new ArrayList<String>() {{
            for (def datum : data)
                add(datum.toString())
        }}
    }

    private static Collection<RatingOverTimeData> buildVolumeTuples(String clientName, String productId, final List<Interval> intervals) {
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

    static Collection<RatingOverTimeData> buildROITuples(String clientName, String productId, final List<Interval> intervals) {
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

    static Collection<RatingOverTimeData> buildRVVTuples(String clientName, String productId, final List<Interval> intervals, Collection<RatingOverTimeData> volumeTuples, Collection<RatingOverTimeData> roiTuples) {
        return new ArrayList<RatingOverTimeData>() {{
            def volIt = volumeTuples.iterator()
            def roiIt = roiTuples.iterator()
            def intIt = intervals.iterator()

            def prevRoiTuple = null
            def prevVolumeTuple = null
            def prevInterval = null

            while (roiIt.hasNext() && volIt.hasNext() && intIt.hasNext()) {
                final RatingOverTimeData roiTuple = roiIt.next()
                final RatingOverTimeData volumeTuple = volIt.next()
                final Interval interval = intIt.next()

                add(new RatingOverTimeData(
                        Metric.RvV,
                        keyFromInterval(interval),
                        calculateRvvFor(prevInterval, prevVolumeTuple, prevRoiTuple, interval, volumeTuple, roiTuple)
                ))

                prevVolumeTuple = volumeTuple
                prevRoiTuple = roiTuple
                prevInterval = interval
            }
        }

            Number calculateRvvFor(Interval prevInterval, RatingOverTimeData prevVolumeTuple, RatingOverTimeData prevRoiTuple, Interval interval, RatingOverTimeData volumeTuple, RatingOverTimeData roiTuple) {
                Number term1
                Number term2

                if (prevVolumeTuple == null || prevRoiTuple == null || prevInterval == null) {
                    return 0
                }

                term1 = (roiTuple.value - prevRoiTuple.value) / 100
                term2 = (volumeTuple.value - prevVolumeTuple.value) / 100

                return term1 - term2
            }
        }
    }

    /*private static void accumulateVolumes(HashMap volumes) {
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
    }*/

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
        final result = "new Date(" + String.valueOf(yearMonthDay.get(0)) + "," + String.valueOf(yearMonthDay.get(1)) + "," + String.valueOf(yearMonthDay.get(2)) + ")"
        return result
    }

    @Override
    String toString() {
        return "[" + key.toString() + ", " + yearMonthDayToString() + ", " + value + "]"
    }
}

enum Metric {
    RoI("RoI"),
    RvV("RvV"),
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