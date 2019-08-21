package $package$.infrastructure.repository

import cats.Applicative
import cats.implicits._
import $package$.domain.joke.{ Joke, JokeRepository }

import scala.util.Random

class JokeRepositoryInMemory[F[_]: Applicative] extends JokeRepository[F] {
  override def create(joke: Joke): F[Joke] = {
    val newJoke = joke.copy(id = joke.id orElse new Random().nextLong(20).some)
    newJoke.pure[F]
  }

  override def get(id: Long): F[Option[Joke]] =
    if (id == 1L) Joke(id.some, "It's a joke").some.pure[F]
    else (None: Option[Joke]).pure[F]
}
