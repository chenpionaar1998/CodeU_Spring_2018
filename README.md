# CodeU Example Project

This is an example chat application. It iss complete and functional, but leaves
plenty of room for improvement.

## Step 1: Download Java

Check whether Java is already installed by opening a console and typing:

```
javac -version
```

If this prints out a version number, then Java is already installed. Skip to
step 2. If the version number is less than `javac_1.8`, upgrade to the newest version of Java by following these instructions:

Download the JDK (not the JRE) from [here](http://www.oracle.com/technetwork/java/javase/downloads/jdk9-downloads-3848520.html).

Retry the `javac -version` command **in a new console window** to check the
installation. If a version number does not show up, then update the `PATH`
environment variable so that it contains Java's `bin` directory. Follow [these
directions](https://www.java.com/en/download/help/path.xml) to do so.

## Step 2: Download Maven

This project uses [Maven](https://maven.apache.org/) to compile and run the
code. Maven also manages the dependencies, runs the dev server, and deploys to the App
Engine.

Download Maven from [here](https://maven.apache.org/download.cgi). Unzip the
folder anywhere.

Make sure there is a `JAVA_HOME` environment variable that points to the existing Java
installation, and then add Maven's `bin` directory to the `PATH` environment
variable. Instructions for both can be found
[here](https://maven.apache.org/install.html).

Open a console window and execute `mvn -v` to confirm that Maven is correctly
installed.

## Step 3: Install Git

This project uses [Git](https://git-scm.com/) for source version control and
[GitHub](https://github.com/) to host the repository.

Download Git from [here](https://git-scm.com/downloads).

Make sure Git is on `PATH` by executing this command:

```
git --version
```

If you don't see a version number, then make sure Git is on your `PATH`.

## Step 4: Setup your repository

Follow the instructions in the first project to get the repository set up.

## Step 5: Run a development server

In order to test changes locally, you must run the server locally, on your
own computer.

To do this, open a console to your `codeu_project_2018` directory and execute this command:

```
mvn clean appengine:devserver
```

This tells Maven to clean (delete old compiled files) and then run a local
App Engine server.

You should now be able to use a local version of the chat app by opening your
browser to [http://localhost:8080](http://localhost:8080).

## Step 6: Make a change!

- Bring down the existing server by pressing `ctrl+c` in the console running the
App Engine devserver.
- Modify a `.java` or `.jsp` file. (Update the homepage by editing the
`index.jsp` file.)
- Bring the devserver back up by executing `mvn clean appengine:devserver`
again.
- Refresh your browser to see your changes!
