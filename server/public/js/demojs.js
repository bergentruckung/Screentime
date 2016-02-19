$(document).ready(function() {
    var ctx = document.getElementById("myChart").getContext("2d");
    var timeinhours = [];
    for (var i = 0; i < 24; i++) {
        timeinhours[i] = i;
    }

    var timeinminutes = [];
    for (i = 0; i < 10; i++) {
        timeinminutes[i] = [];
        for (j = 0; j < 24; j++) {
            timeinminutes[i][j] = 0;
        }
    }

    var viewDate = new Date(2016, 1, 19);
    $.post("api/getUserDetails/", { email: 'josepjoice@gmail.com' }, function(data) {
        for (i = 0, len1 = data.length; i < len1; i++) {
            for (j = 0, len2 = data[i].data.length; j < len2; j++) {

                var startD = new Date(data[i].data[j].START);
                var stopD = new Date(data[i].data[j].STOP);

                var tempDate = new Date(viewDate);
                var temp2Date = new Date(startD);

                tempDate.setHours(startD.getHours() + 1);
                while (viewDate.toDateString() === temp2Date.toDateString() && temp2Date < stopD) {
                    var elapsed = tempDate - temp2Date;
                    timeinminutes[i][temp2Date.getHours()] = Math.round(elapsed / (1000 * 60));
                    tempDate.setHours(tempDate.getHours() + 1);
                    temp2Date.setHours(temp2Date.getHours() + 1);
                    temp2Date.setMinutes(0);
                }
                console.log(data[i].data[j].type);
            }
        }
        console.log(timeinminutes[i]);
        var data = {
            labels: timeinhours,
            datasets: [{
                label: "My First dataset",
                fillColor: "rgba(61,245,0,0.3)",
                strokeColor: "rgba(46,184,0,1)",
                pointColor: "rgba(220,220,220,1)",
                pointStrokeColor: "#fff",
                pointHighlightFill: "#fff",
                pointHighlightStroke: "rgba(220,220,220,1)",
                data: timeinminutes[0]
            }, {
                label: "My Second dataset",
                fillColor: "rgba(46,0,255,0.2)",
                strokeColor: "rgba(151,187,205,1)",
                pointColor: "rgba(151,187,205,1)",
                pointStrokeColor: "#fff",
                pointHighlightFill: "#fff",
                pointHighlightStroke: "rgba(151,187,205,1)",
                data: timeinminutes[1]
            }]
        };

        var myLineChart = new Chart(ctx).Line(data, {
            bezierCurve: false,
                legendTemplate : "<ul class=\"<%=name.toLowerCase()%>-legend\"><% for (var i=0; i<datasets.length; i++){%><li><span style=\"background-color:<%=datasets[i].strokeColor%>\"></span><%if(datasets[i].label){%><%=datasets[i].label%><%}%></li><%}%></ul>"
        });

    });



});
