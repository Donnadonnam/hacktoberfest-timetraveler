<!DOCTYPE html>
<html>
<head>
    <title><g:message code="page.title.settings.home"/></title>
    <meta name="layout" content="main"/>
    <r:require module="application"/>
</head>
<body>
    <h1>Time Traveler</h1>

    <%-- For Verizon product external ID 5971 --%>
    <div id="chart_div"></div>

    <r:script>
        google.load('visualization', '1', {'packages':['motionchart']});
        google.setOnLoadCallback(drawChart);
        function drawChart() {
            var data = new google.visualization.DataTable();
            data.addColumn('string', 'Metric');
            data.addColumn('date', 'Date');
            data.addColumn('number', 'Change');
            data.addRows([
                ['Volume', new Date (2013,8,7), 3],
                ['ROI',    new Date (2013,8,7), 5],
                ['RvV',    new Date (2013,8,7), 2],

                ['Volume', new Date (2013,8,2), 0],
                ['ROI',    new Date (2013,8,2), 1],
                ['RvV',    new Date (2013,8,2), 1],

                ['Volume', new Date (2013,8,1), 0],
                ['ROI',    new Date (2013,8,1), 2],
                ['RvV',    new Date (2013,8,1), 2],

                ['Volume', new Date (2013,8,3), 0.00044903457566],
                ['ROI',    new Date (2013,8,3), 5],
                ['RvV',    new Date (2013,8,3), 2],

                ['Volume', new Date (2013,8,4), 3],
                ['ROI',    new Date (2013,8,4), 5],
                ['RvV',    new Date (2013,8,4), 2],

                ['Volume', new Date (2013,8,5), 3],
                ['ROI',    new Date (2013,8,5), 5],
                ['RvV',    new Date (2013,8,5), 2],

                ['Volume', new Date (2013,8,6), 3],
                ['ROI',    new Date (2013,8,6), 5],
                ['RvV',    new Date (2013,8,6), 2]

            ]);
            var chart = new google.visualization.MotionChart(document.getElementById('chart_div'));
            var options = {};
            options['showChartButtons'] = false;
            options['showSidePanel'] = false;
            options['showXMetricPicker'] = false;
            options['showYMetricPicker'] = false;
            options['state'] = '{"showTrails":true,"uniColorForNonSelected":false,"colorOption":"_UNIQUE_COLOR",' +
                    '"xZoomedIn":false,"time":"2013-09-01","playDuration":5000,"sizeOption":"_UNISIZE",' +
                    '"duration":{"timeUnit":"D","multiplier":1},"xZoomedDataMin":1375315200000,' +
                    '"orderedByY":false,"xZoomedDataMax":1375401600000,"iconType":"BUBBLE","nonSelectedAlpha":0.4,' +
                    '"xLambda":1,"iconKeySettings":[{"key":{"dim0":"Volume"},"trailStart":"2013-08-01"},{"key":{"dim0":"ROI"},' +
                    '"trailStart":"2013-08-01"},{"key":{"dim0":"RvV"},"trailStart":"2013-08-01"}],"yZoomedIn":false,' +
                    '"yZoomedDataMax":2,"dimensions":{"iconDimensions":["dim0"]},"xAxisOption":"_TIME",' +
                    '"yZoomedDataMin":-1,"orderedByX":false,"yLambda":1,"yAxisOption":"2"}';
            options['width'] = 800;
            options['height'] = 600;
            chart.draw(data, options);
        }
    </r:script>

</body>
</html>