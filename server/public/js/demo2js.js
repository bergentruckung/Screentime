$(document).ready(function() {
    var ctx = document.getElementById("myChart").getContext("2d");

    var viewDate = new Date(2016, 1, 19);
    $.post("api/getAllProcess/", function(data) {
        var timeinhours = [];
        for (var i = 0; i < 24; i++) {
            timeinhours[i] = i;
        }
        var types = [];
        var timeInMin = [];
        for (i = 0; i < 100; i++) {
            timeInMin[i] = [];
            for (j = 0; j < 24; j++) {
                timeInMin[i][j] = 0;
            }
        }

        var z = -1;
        var prev = '';
        for (k = 0, len1 = data.length; k < len1; k++) {
            if (data[k].type != prev) {
                //new type

                z++;
                types.push(data[k].type);
                startD = new Date(data[k].start);
                stopD = new Date(data[k].stop);

                console.log(data[k].type);
                var tempDate = new Date(viewDate);
                var temp2Date = new Date(startD);

                tempDate.setHours(startD.getHours() + 1);
                while (viewDate.toDateString() === temp2Date.toDateString() && temp2Date < stopD) {
                    var elapsed = tempDate - temp2Date;
                    timeInMin[z][temp2Date.getHours()] = Math.round(elapsed / (1000 * 60));
                    tempDate.setHours(tempDate.getHours() + 1);
                    temp2Date.setHours(temp2Date.getHours() + 1);
                    temp2Date.setMinutes(0);
                }
                prev = data[k].type;
            } else {
                //old one 
                var tempDate = new Date(viewDate);
                var temp2Date = new Date(startD);
                tempDate.setHours(startD.getHours() + 1);
                while (viewDate.toDateString() === temp2Date.toDateString() && temp2Date < stopD) {
                    var elapsed = tempDate - temp2Date;
                    timeInMin[z][temp2Date.getHours()] = Math.round(elapsed / (1000 * 60));
                    tempDate.setHours(tempDate.getHours() + 1);
                    temp2Date.setHours(temp2Date.getHours() + 1);
                    temp2Date.setMinutes(0);
                }
            }
        }

        function getRandomColor() {
              str1 = Math.floor(Math.random() * 255);
              str2 = Math.floor(Math.random() * 255);
              str3 = Math.floor(Math.random() * 255);
              str4 = Math.floor(Math.random() * 255);
              var d = "rgba(" + str1 +"," + str2  + "," + str3 +",0.5)";
              console.log(d);
              return d;
         }

        var datasets = [];
        for (var i = 0; i <= z; i++) {
            datasets.push({
                label: types[i],
                fillColor: getRandomColor(),
                strokeColor: getRandomColor(),
                pointColor: "rgba(220,220,220,1)",
                pointStrokeColor: "#fff",
                pointHighlightFill: "#fff",
                pointHighlightStroke: "rgba(220,220,220,1)",
                data: timeInMin[i]
            });
        }


        var data = {
            labels: timeinhours,
            datasets: datasets
        };

        var myLineChart = new Chart(ctx).Line(data, {
                legendTemplate : "<ul class=\"<%=name.toLowerCase()%>-legend\"><% for (var i=0; i<datasets.length; i++){%><li><span style=\"background-color:<%=datasets[i].strokeColor%>\"></span><%if(datasets[i].label){%><%=datasets[i].label%><%}%></li><%}%></ul>"
        });

         var legend = myLineChart.generateLegend();
         document.getElementById("legend").innerHTML= legend 

  //and append it to your page somewhere
    // $('#myChart').append(legend);

    });



});
