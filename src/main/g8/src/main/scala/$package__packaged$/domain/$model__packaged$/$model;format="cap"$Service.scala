package $package$.domain.$model$

import cats.{ Functor, Monad }
import cats.data.EitherT
import $package$.domain.{ AlreadyExistError, NotExistError }

class $model;format="cap"$Service[F[_]](repository: $model;format="cap"$Repository[F], validator: $model;format="cap"$ValidationAlgebra[F]) {

  def get(id: Long)(implicit F: Functor[F]): EitherT[F, NotExistError.type, $model;format="cap"$] =
    EitherT.fromOptionF(repository.get(id), NotExistError)

  def create($model$: $model;format="cap"$)(implicit M: Monad[F]): EitherT[F, AlreadyExistError, $model;format="cap"$] =
    for {
      _     <- validator.doesNotExist($model$)
      saved <- EitherT.liftF(repository.create($model$))
    } yield saved
}

object $model;format="cap"$Service {
  def apply[F[_]](repository: $model;format="cap"$Repository[F], validator: $model;format="cap"$ValidationAlgebra[F]): $model;format="cap"$Service[F] =
    new $model;format="cap"$Service(repository, validator)
}
