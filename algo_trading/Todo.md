* Create a blocking queue implementation that can be used to add events
* At schedule, assign job to a producer that adds it to a consumer queue
* Scheduler to define the next time to schedule job based on response from client
* Consumer picks up the job and executes it
* On error, exponential back-off for scheduler