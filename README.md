# Spot-A-Bee

This was a university client project from semester 2 of year 1, based on android. The client's goal for this project was to crowdsource the gathering of data on bees in Wales, but more specifically, where the bees are, and what plants are they gathering at. A user would use this app to take pictures of any bees that they may see, and hopefully, the app would be able to identify the flower in the picture (if the bee was on one). This data would then go back to the company so that they could start to implement measures to increase the local bee populations.

As a retrospective, we were not able to fully fill the client's request. The app could successfully identify 10 different flowers with above ~80% accuracy, but it was a longshot from the goal of all flowers, domestic and exotic. Additionally, the app did have some stability problems, some of which may still persist.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

What things you need to install the software and how to install them

```
.Net Core 2.2
PostgreSQL
```

### Running

To run the web server on localhost, you will need a local PostgreSQL Database and .Net Core 2.2 installed. I have my PostgreSQL DB running on port 5432 using the credentials user: postgres password: admin, though these settings can be tweaked in ~/Persistence/Configuration/DatabaseConnectionHandler.cs.

With a DB running, just navigate to ~/Web and run

```
dotnet run
```

Navigate in to https://localhost:5001/login in your browser of choice to begin using the system. There are 6 default accounts in the system, 2 admin, 4 candidate, and all use the password "pass". Login with admin@asg.com, admin2@asg.com, candidate@asg.com, ..., candidate4@asg.com

## Running the tests

Explain how to run the automated tests for this system

### Break down into end to end tests

Explain what these tests test and why

```
Give an example
```

### And coding style tests

Explain what these tests test and why

```
Give an example
```

## Deployment

Add additional notes about how to deploy this on a live system

## Built With

* [Dropwizard](http://www.dropwizard.io/1.0.2/docs/) - The web framework used
* [Maven](https://maven.apache.org/) - Dependency Management
* [ROME](https://rometools.github.io/rome/) - Used to generate RSS Feeds

## Contributing

Please read [CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) for details on our code of conduct, and the process for submitting pull requests to us.

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/your/project/tags). 

## Authors

* **DevilDude4427** - *Entire Project* - [Devildude4427](https://github.com/Devildude4427)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* Thanks to the guys over at Spot-a-bee for this project, as it was very fun to work on. Their link [Spot-a-bee](https://spotabee.buzz/home)
