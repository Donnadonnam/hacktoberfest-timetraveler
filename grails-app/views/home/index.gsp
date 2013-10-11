<!DOCTYPE html>
<html>
<head>
    <title><g:message code="page.title.settings.home"/></title>
    <meta name="layout" content="main"/>
    <r:require module="application"/>
</head>
<body>
    <h1>Time Traveler</h1>
    <hr/>
    <div class="container">

        <%-- Verizon product external ID 5947 --%>
        <div class="product_5947 row-fluid">
            <div class="product-details span2">
                <div class="product-image ">
                    <r:img dir="images" file="5947.jpg" width="198" height="400" alt="${g.message(code:'product.name.5947')}"/>
                </div>
                <h4 class="product-name"><g:message code="product.name.5947"/></h4>
            </div>

            <div id="chart_5947" class="span8"></div>

            <dl class="legend span2">
                <dt>
                    <span class="circle roi"></span>
                    <g:message code="legend.roi.dt"/>
                </dt>
                <dd><g:message code="legend.roi.dd"/></dd>
                <dt>
                    <span class="circle vol"></span>
                    <g:message code="legend.vol.dt"/>
                </dt>
                <dd><g:message code="legend.vol.dd"/></dd>
                <dt>
                    <span class="circle rvv"></span>
                    <g:message code="legend.rvv.dt"/>
                </dt>
                <dd><g:message code="legend.rvv.dd"/></dd>
            </dl>
    </div>

        <%-- Verizon product external ID 5954 --%>
        <div class="product_5954 row-fluid">
            <div class="product-details span2">
                <div class="product-image ">
                    <r:img dir="images" file="5954.png" width="171" height="338" alt="${g.message(code:'product.name.5954')}"/>
                </div>
                <h4 class="product-name"><g:message code="product.name.5954"/></h4>
            </div>

            <div id="chart_5954" class="span8"></div>

            <dl class="legend span2">
                <dt>
                    <span class="circle roi"></span>
                    <g:message code="legend.roi.dt"/>
                </dt>
                <dd><g:message code="legend.roi.dd"/></dd>
                <dt>
                    <span class="circle vol"></span>
                    <g:message code="legend.vol.dt"/>
                </dt>
                <dd><g:message code="legend.vol.dd"/></dd>
                <dt>
                    <span class="circle rvv"></span>
                    <g:message code="legend.rvv.dt"/>
                </dt>
                <dd><g:message code="legend.rvv.dd"/></dd>
            </dl>
        </div>

    </div>

    <r:script>
        google.load('visualization', '1', {'packages':['motionchart']});
        google.setOnLoadCallback(drawChart);
        function drawChart() {
            var options = {};
            options['showChartButtons'] = false;
            options['showSidePanel'] = false;
            options['showXMetricPicker'] = false;
            options['showYMetricPicker'] = false;
            options['state'] = '{"showTrails":true,"uniColorForNonSelected":false,"colorOption":"_UNIQUE_COLOR",' +
                    '"xZoomedIn":false,"playDuration":5000,"sizeOption":"_UNISIZE","xLambda":1,' +
                    '"duration":{"timeUnit":"D","multiplier":1},"xZoomedDataMin":1375315200000,' +
                    '"orderedByY":false,"xZoomedDataMax":1375401600000,"iconType":"BUBBLE","nonSelectedAlpha":0.4,' +
                    '"iconKeySettings":[{"key":{"dim0":"ROI"},"trailStart":"2013-09-25"},{"key":{"dim0":"Volume"},"trailStart":"2013-09-25"},{"key":{"dim0":"RvV"},"trailStart":"2013-09-25"}],' +
                    '"yZoomedIn":false,"yZoomedDataMax":2,"dimensions":{"iconDimensions":["dim0"]},"xAxisOption":"_TIME",' +
                    '"yZoomedDataMin":-1,"orderedByX":false,"yLambda":1,"yAxisOption":"2"}';
            options['width'] = 700;
            options['height'] = 400;

            var data_5947 = new google.visualization.DataTable();
            data_5947.addColumn('string', 'Metric');
            data_5947.addColumn('date', 'Date');
            data_5947.addColumn('number', '% change');
            data_5947.addRows([
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
            var chart_5947 = new google.visualization.MotionChart(document.getElementById('chart_5947'));
            chart_5947.draw(data_5947, options);


            var data_5954 = new google.visualization.DataTable();
            data_5954.addColumn('string', 'Metric');
            data_5954.addColumn('date', 'Date');
            data_5954.addColumn('number', '% change');
            data_5954.addRows([
                ['Volume', new Date (2013,8,25), 0],
                ['ROI',    new Date (2013,8,25), -5],
                ['RvV',    new Date (2013,8,25), -5],

                ['Volume', new Date (2013,8,26), 0],
                ['ROI',    new Date (2013,8,26), -4],
                ['RvV',    new Date (2013,8,26), -4],

                ['Volume', new Date (2013,8,27), -9],
                ['ROI',    new Date (2013,8,27), 0],
                ['RvV',    new Date (2013,8,27), 9],

                ['Volume', new Date (2013,8,28), -10],
                ['ROI',    new Date (2013,8,28), 5],
                ['RvV',    new Date (2013,8,28), 15],

                ['Volume', new Date (2013,8,29), 0],
                ['ROI',    new Date (2013,8,29), 4],
                ['RvV',    new Date (2013,8,29), 4],

                ['Volume', new Date (2013,8,30), -11],
                ['ROI',    new Date (2013,8,30), -16],
                ['RvV',    new Date (2013,8,30), -5]

            ]);
            var chart_5954 = new google.visualization.MotionChart(document.getElementById('chart_5954'));
            chart_5954.draw(data_5954, options);
        }
    </r:script>

</body>
</html>