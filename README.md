# `PrintServer` implementation with `java.rmi`

This is an implementation of a (fake) "print server" for the Authentication Lab in
the course [02239 - Data Security](https://kurser.dtu.dk/course/02239) at the 
Technical University of Denmark.

## Project structure

The project consists of three subprojects:

- `PrintServer.Common` is a java library containing interfaces shared between the client and the server.
- `PrintServer.Client` is java application with a "print server" RMI Client.
- `PrintServer.Server` is java application with a "print server" RMI Server.

## Building and Running

This project uses the `bazel` build tool. You can obtain bazel [here](https://bazel.build/install).

Building is as simple as running:

```sh
bazel build //:all
```

Once the build process has finished, start the server by running:

```sh
bazel run //:PrintServer.Server
```

Then start the client by running:

```sh
bazel run //:PrintServer.Client
```
