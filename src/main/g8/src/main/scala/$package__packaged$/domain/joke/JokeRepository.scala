package $package$.domain.joke

trait JokeRepository[F[_]] {
  def create(joke: Joke): F[Joke]

  def get(id: Long): F[Option[Joke]]

}
