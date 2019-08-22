package $package$.domain.$model$

import cats.Applicative
import cats.data.EitherT
import cats.implicits._
import $package$.domain.AlreadyExistError

class $model;format="cap"$ValidationInterpreter[F[_]: Applicative](repo: $model;format="cap"$Repository[F]) extends $model;format="cap"$ValidationAlgebra[F] {
  override def doesNotExist($model$: $model;format="cap"$): EitherT[F, AlreadyExistError, $model;format="cap"$] = EitherT {
    $model$.id.map(repo.get) match {
      case Some(_) => Either.left[AlreadyExistError, $model;format="cap"$](AlreadyExistError("\$$model$.id already exists")).pure[F]
      case _       => Either.right[AlreadyExistError, $model;format="cap"$]($model$).pure[F]
    }
  }
}
