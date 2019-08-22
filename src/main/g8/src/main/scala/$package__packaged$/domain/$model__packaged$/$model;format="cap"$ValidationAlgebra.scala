package $package$.domain.$model$

import cats.data.EitherT
import $package$.domain.AlreadyExistError

trait $model;format="cap"$ValidationAlgebra[F[_]] {
  def doesNotExist($model$: $model;format="cap"$): EitherT[F, AlreadyExistError, $model;format="cap"$]
}
