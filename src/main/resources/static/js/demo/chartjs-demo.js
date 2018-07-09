$(function () {


    var doughnutData = [
        {
            value: 200,
            color: "#a3e1d4",
            highlight: "#1ab394",
            label: "APBD"
        },
        {
            value: 50,
            color: "#dedede",
            highlight: "#1ab394",
            label: "APBN"
        },
        {
            value: 100,
            color: "#b5b8cf",
            highlight: "#1ab394",
            label: "CSR"
        }
    ];


    var doughnutOptions = {
        segmentShowStroke: true,
        segmentStrokeColor: "#fff",
        segmentStrokeWidth: 2,
        percentageInnerCutout: 0, // This is 0 for Pie charts
        animationSteps: 100,
        animationEasing: "easeOutBounce",
        animateRotate: true,
        animateScale: false,
        responsive: true,
    };


    var ctx = document.getElementById("doughnutChart").getContext("2d");
    var myNewChart = new Chart(ctx, {
        type: 'pie',
        data: {
            labels: ["Green", "Blue", "Gray", "Purple", "Yellow", "Red", "Black"],
            datasets: [{
            backgroundColor: [
                "#2ecc71",
                "#3498db",
                "#95a5a6",
                "#9b59b6",
                "#f1c40f",
                "#e74c3c",
                "#34495e"
            ],
            data: [12, 19, 3, 17, 28, 24, 7]
            }]
            }
    });
    // Define a plugin to provide data labels
    Chart.plugins.register({
        afterDatasetsDraw: function(chart, easing) {
            // To only draw at the end of animation, check for easing === 1

            chart.data.datasets.forEach(function (dataset, i) {
                var meta = chart.getDatasetMeta(i);
                if (!meta.hidden) {
                    meta.data.forEach(function(element, index) {
                        // Draw the text in black, with the specified font
                        ctx.fillStyle = 'rgb(0, 0, 0)';

                        var fontSize = 16;
                        var fontStyle = 'normal';
                        var fontFamily = 'Open Sans';
                        ctx.font = Chart.helpers.fontString(fontSize, fontStyle, fontFamily);

                        // Just naively convert to string for now
                        var dataString = dataset.data[index].toString();

                        // Make sure alignment settings are correct
                        ctx.textAlign = 'center';
                        ctx.textBaseline = 'middle';

                        var padding = 5;
                        var position = element.tooltipPosition();
                        var x = position.x + padding;
                        var y = position.y + padding;
                        ctx.fillText(dataString, x, y);
                    });
                }
            });
        }
    });
});