fp-web.g8
===
A web project template using FP techniques in scala

# Installation
```sh
brew update && brew install giter8
g8 vitrun/fp-web.g8
```
# Stack
- [Http4s](http://http4s.org/) as the web server.  
- [ZIO](https://zio.dev/) for functional effect handling
- [Circe](https://circe.github.io/circe/) for json serialization
- [Doobie](https://github.com/tpolecat/doobie) for database access
- [ScalaCheck](https://www.scalacheck.org/) for property based testing
- [Tapir](https://github.com/softwaremill/tapir) for API definition and swagger docs
- [Ammonite](https://ammonite.io/) for REPL

# Architecture

### The domain package
The domain package constitutes the things inside our domain. It is deliberately free of the ugliness of JDBC, JSON, HTTP, and the rest.

- `Service`
- `Repository`
- `models`

### The infrastructure package
The infrastructure package is where the ugliness lives. It has HTTP things, JDBC things, and the like.
- `endpoint`
- `repository`

# Reference
- [scala-pet-store](https://github.com/pauljamescleary/scala-pet-store)

# License
To the extent possible under law, the author(s) have dedicated all copyright and related
and neighboring rights to this template to the public domain worldwide.
This template is distributed without any warranty. See <http://creativecommons.org/publicdomain/zero/1.0/>.

