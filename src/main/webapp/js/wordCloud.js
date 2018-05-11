var fill = d3.scale.category20();

var width = 700;
var height = 700;

var randomNum = document.getElementById("randomName").innerHTML;
var userScale = d3.scale.linear().range([10,80]);
var file = "./api/stats/" + randomNum + ".json";

d3.json(file, function(data){
  if (data.length > 100){
    var users = data.slice(0,100).map(function(data){return {text: data.name, size: data.messageCount}});
  }else{
    var users = data.map(function(data){return {text: data.name, size: data.messageCount}});
  }
  userScale.domain([
    d3.min(users, function(data){return data.size}),
    d3.max(users, function(data){return data.size})
  ]);
  d3.layout.cloud()
      .size([width, height])
      .words(users)
      .padding(0)
      .font("Impact")
      .fontSize(function(data) { return userScale(data.size); })
      .on("end", draw)
      .start();
});

function draw(words) {
  d3.select("#wordCloud").append("svg")
      .attr("width", width)
      .attr("height", height)
    .append("g")
      .attr("transform", "translate(" + width / 2 + "," + height / 2 + ")")
    .selectAll("text")
      .data(words)
    .enter().append("text")
      .style("font-size", function(d) { return d.size + "px"; })
      .style("font-family", "Impact")
      .style("fill", function(d, i) { return fill(i); })
      .attr("text-anchor", "middle")
      .attr("transform", function(d) {
        return "translate(" + [d.x, d.y] + ")rotate(" + d.rotate + ")";
      })
      .text(function(d) { return d.text; });
}
