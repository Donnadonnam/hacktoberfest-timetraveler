<!DOCTYPE html>
<html>
<head>
    <title><g:message code="page.title.settings.home"/></title>
    <meta name="layout" content="main"/>
    <r:require module="application"/>
</head>
<body>
    <h1>Time Traveler</h1>

    <%-- For Verizon product external ID 5947 --%>
    <div class="container row-fluid">
        <div class="product-details span4">
            <div class="product-image ">
                <r:img dir="images" file="5947.jpg" width="198" height="400" alt="${g.message(code:'product.name')}"/>
            </div>
            <h4 class="product-name"><g:message code="product.name"/></h4>
            <dl class="legend">
                <dt><span class="circle vol"></span> Volume</dt>
                    <dd>total reviews in the past 30 days</dd>
                <dt><span class="circle roi"></span> ROI</dt>
                    <dd>conversion rate (orders / unique visitors)</dd>
                <dt><span class="circle rvv"></span> RvV</dt>
                    <dd>ROI % change - Volume % change</dd>
            </dl>
        </div>

        <div id="chart_div" class="span8"></div>
    </div>


    <r:script>
        google.load('visualization', '1', {'packages':['motionchart']});
        google.setOnLoadCallback(drawChart);
        function drawChart() {
            var data = new google.visualization.DataTable();
            data.addColumn('string', 'Metric');
            data.addColumn('date', 'Date');
            data.addColumn('number', '% change');
            data.addRows([
                //['Volume', new Date (2013,8,24), 26],
                //['ROI',    new Date (2013,8,24), 4],
                //['RvV',    new Date (2013,8,24), 2],

                ['Volume', new Date (2013,8,25), -4],
                ['ROI',    new Date (2013,8,25), -8],
                ['RvV',    new Date (2013,8,25), -4],

                ['Volume', new Date (2013,8,26), -4],
                ['ROI',    new Date (2013,8,26), -8],
                ['RvV',    new Date (2013,8,26), -4],

                ['Volume', new Date (2013,8,27), 0],
                ['ROI',    new Date (2013,8,27), 22],
                ['RvV',    new Date (2013,8,27), 22],

                ['Volume', new Date (2013,8,28), -13],
                ['ROI',    new Date (2013,8,28), 30],
                ['RvV',    new Date (2013,8,28), 42],

                ['Volume', new Date (2013,8,29), 0],
                ['ROI',    new Date (2013,8,29), -11],
                ['RvV',    new Date (2013,8,29), -11],

                ['Volume', new Date (2013,8,30), 0],
                ['ROI',    new Date (2013,8,30), -20],
                ['RvV',    new Date (2013,8,30), -20]

            ]);
            var chart = new google.visualization.MotionChart(document.getElementById('chart_div'));
            var options = {};
            options['showChartButtons'] = false;
            options['showSidePanel'] = false;
            options['showXMetricPicker'] = false;
            options['showYMetricPicker'] = false;
            options['state'] = '{"showTrails":true,"uniColorForNonSelected":false,"colorOption":"_UNIQUE_COLOR",' +
                    '"xZoomedIn":false,"time":"2013-09-25","playDuration":5000,"sizeOption":"_UNISIZE",' +
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