# Queue management simulation

This project aims to simulate the concurrent handling of multiple clients by the help of threads. A _client_ (_task_) has an ID, an arrival time and a service time.
A scheduler assigns each incoming task to one of the queues (each runs on a different thread). The scheduler has two possible assignment strategies: shortest queue or the least time.

![Simulation UI](/images/process.png)

A complete documentation can be found [here](PT2024_Documentation_Muresan_Andrei_Assignement_1.doc).

## Features

- Control the simulation parameters
- Visualize status of each queue at discrete time intervals
- Obtain final stats
- Log file with the status of each queue at all times + stats

> [!TIP]
> Try to play with as many input combinations to observe different results. (Don't worry, input checking is implemented)

![Set the parameters](/images/inputs.png)

![Stats](/images/results.png)

## Author
Muresan Andrei   
[LinkedIn](https://www.linkedin.com/in/andrei-muresan-muri/)
[GitHub](https://github.com/andrei-muri)