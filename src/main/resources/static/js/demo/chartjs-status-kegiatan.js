/**
 * Created by Oddi Muhammad Ikbar on 13/07/2017.
 */

$(function () {
var doughnutData1 = [
    {
        value: 200,
        color: "#a3e1d4",
        highlight: "#1ab394",
        label: "Batal"
    },
    {
        value: 50,
        color: "#dedede",
        highlight: "#1ab394",
        label: "Selesai"
    },
    {
        value: 100,
        color: "#b5b8cf",
        highlight: "#1ab394",
        label: "Ditunda"
    }
];

var doughnutOptions1 = {
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

var cty = document.getElementById("doughnutChart1").getContext("2d");
var chartKegiatan = new Chart(cty).Doughnut(doughnutData1, doughnutOptions1);

});