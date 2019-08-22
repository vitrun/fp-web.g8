package $package$.infrastructure.repository

import cats.Applicative
import cats.implicits._
import $package$.domain.$model$.{ $model;format="cap"$, $model;format="cap"$Repository }

import scala.util.Random

class $model;format="cap"$RepositoryInMemory[F[_]: Applicative] extends $model;format="cap"$Repository[F] {
  override def create($model$: $model;format="cap"$): F[$model;format="cap"$] = {
    val new$model;format="cap"$ = $model$.copy(id = $model$.id orElse new Random().nextLong(20).some)
    new$model;format="cap"$.pure[F]
  }

  override def get(id: Long): F[Option[$model;format="cap"$]] =
    if (id == 1L) $model;format="cap"$(id.some, "It's a $model$").some.pure[F]
    else (None: Option[$model;format="cap"$]).pure[F]
}
