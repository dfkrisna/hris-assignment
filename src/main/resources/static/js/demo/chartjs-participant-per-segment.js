$(function () {


    var doughnutData = [
        {
            value: 250,
            color: "#8dd3c7",
            highlight: "#8dd3c7",
            label: "Pra Pemilih"
        },
        {
            value: 170,
            color: "#ffffb3",
            highlight: "#ffffb3",
            label: "Pemilih Pemula"
        },
        {
            value: 42,
            color: "#80b1d3",
            highlight: "#80b1d3",
            label: "Pemilih Kegamaan"
        },
        {
            value: 32,
            color: "#bebada",
            highlight: "#bebada",
            label: "Lansia"
        },
        {
            value: 11,
            color: "#fb8072",
            highlight: "#fb8072",
            label: "Disabilitas"
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
    var myNewChart = new Chart(ctx).Doughnut(doughnutData, doughnutOptions);

});

