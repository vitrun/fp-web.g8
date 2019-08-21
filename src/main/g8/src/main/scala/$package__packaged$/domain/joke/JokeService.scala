package $package$.domain.joke

import cats.{ Functor, Monad }
import cats.data.EitherT
import $package$.domain.{ AlreadyExistError, NotExistError }

class JokeService[F[_]](repository: JokeRepository[F], validator: JokeValidationAlgebra[F]) {

  def get(id: Long)(implicit F: Functor[F]): EitherT[F, NotExistError.type, Joke] =
    EitherT.fromOptionF(repository.get(id), NotExistError)

  def create(joke: Joke)(implicit M: Monad[F]): EitherT[F, AlreadyExistError, Joke] =
    for {
      _     <- validator.doesNotExist(joke)
      saved <- EitherT.liftF(repository.create(joke))
    } yield saved
}

object JokeService {
  def apply[F[_]](repository: JokeRepository[F], validator: JokeValidationAlgebra[F]): JokeService[F] =
    new JokeService(repository, validator)
}
