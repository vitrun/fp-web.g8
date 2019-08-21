package $package$.domain.joke

import cats.Applicative
import cats.data.EitherT
import cats.implicits._
import $package$.domain.AlreadyExistError

class JokeValidationInterpreter[F[_]: Applicative](repo: JokeRepository[F]) extends JokeValidationAlgebra[F] {
  override def doesNotExist(joke: Joke): EitherT[F, AlreadyExistError, Joke] = EitherT {
    joke.id.map(repo.get) match {
      case Some(_) => Either.left[AlreadyExistError, Joke](AlreadyExistError("\$joke.id already exists")).pure[F]
      case _       => Either.right[AlreadyExistError, Joke](joke).pure[F]
    }
  }
}
