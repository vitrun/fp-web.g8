package $package$.domain.joke

case class Joke(id: Option[Long] = None, joke: String, tags: Set[String] = Set.empty)
