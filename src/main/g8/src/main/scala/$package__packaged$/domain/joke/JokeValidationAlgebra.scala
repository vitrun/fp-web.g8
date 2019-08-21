package $package$.domain.joke

import cats.data.EitherT
import $package$.domain.AlreadyExistError

trait JokeValidationAlgebra[F[_]] {
  def doesNotExist(joke: Joke): EitherT[F, AlreadyExistError, Joke]
}
