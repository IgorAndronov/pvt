##Architecture

According to the business requiriments the solution should fullfill SLA 1M requests per day.
So as target architecture the microservice architecture were selected. It gives the possibility of good horizontal scaling and spreafding of the load between regions.
For data storing purposees Cassandra DB were choosen as it gives very high perfomance for simple read/writes operations and easy to scale.

The solution is built on Spring Cloud stack.

![Alt solution](src/main/resources/solution.png?raw=true "Architecture")
