# Branch coding exercise

Coding exercise for Jeff Chan

## Java version

This project requires Java 17.
[Download Here](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)

## Running the app

The following command ran in the root folder should start the application on port 8080 on your local machine
```
./gradlew clean bootRun
```

You may need to run `chmod +x gradlew` if the gradlew command has a permission issue.

## Using the app

This app supports one endpoint

```
{base URL}/version-control/users/{Github username}
```

If the Github username provided does not exist a 404 will be returned

## Design and architecture
I chose a pretty simple controller-service(-repository) architecture with an additional service class that acts as an API gateway out to Github.

Separating out the API gateway is to make it easier to either change to another version control host entirely entirely or to add a different one for another customer. 
To that end I had a vast majority of the DTO to DTO mapping logic and other mentions of Github separated out from the VersionControlController and VersionControlService.

For caching, I'm using Spring's built-in caching mechanism to cache off of the inputted username.
This can be tested/verified through the server logs. 
The first time a username is queried, the code will reach the log statements within the GithubGateway class.
Subsequent calls to the API with that username will still return the same data but the log statements will not output.

For error handling, the only error I'm specifically targeting is for non-existent Github users.
These will return a 404 back to the API consumer.
Other Github related errors will return as a generic 400 or 500 series HTTP error