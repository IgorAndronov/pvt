##Architecture

According to the business requiriments the solution should fullfill SLA 1M requests per day.
So as target architecture the microservice architecture were selected. It gives the possibility of good horizontal scaling and spreafding of the load between regions.
For data storing purposees Cassandra DB were choosen as it gives very high perfomance for simple read/writes operations and easy to scale.

The solution is built on Spring Boot framework && Cloud stack and consist of the following elements:
UI Gatway - ZUUL
Srvice registry/discovery - EUREKA (monitoring url: http://host:port/eureka default host: localhost,  default port:)
Config server
Game service
Statistics service
Cassandra cluster



![Alt solution](src/main/resources/solution.png?raw=true "Architecture")

Game Algoritm explanation:
Assumptions:
1. According to the requriments the user is bad in generation random combinations. It means the user stratagy will be individual and can be changed un time based on previous results.
2. As many different users generates individual sequences of game combinations it has no sence to analize overall user statistics as it will lead to uniform distribution. Instead of this individual user statistics should be taken into account to consider personal nature of the user stratagy
3. As user stratajy can be changed in time the server algoritm should adjust its behavior based on recent last user actions.
For that purposess period for analises shouldn't be taken very big as in this case server wouldn't react dinamicaly enough and can produce similar results that user can mention and make a profit from this.

Algoritm description:
"game step(gs)" -user combination (stone or scissors or paper)
"gs" sequense example ("s" -stone; c -scissors, p- paper):
"s", "p", "c",..."p","s", "p","s", "p"

If statistics data is collected in "enough for prediction" amount(parametrised) then the following algoritm is applied:
Server simply collects last user "game steps" statistics and analize frequeency of last combination(gs).
First iteration:
select last combination(gs) ("p" in example) and search the frequency of the upcomming step(gs). Upcomming gs is the gs that goes immidiatly after current one(in example "c" 1time and "s" 2 times). If probability of some "gs"("s" in the example) is hire enough(parametrised) then this "gs" is considered as probable user next step(gs).
Second iteration: 
if probability level of step one is not high enough then combination of last two "gs"("s""p") is analised and algoritm is repeated
Iteration N:
same for n last "gs"
If probabilty level is not fullfiled for n-iterations then just more frequent "gs" is found and considered as most probable next user step(gs) 
If statistics amount is not enough for the prediction(parametrised) then random combination is used.



How to Run:

