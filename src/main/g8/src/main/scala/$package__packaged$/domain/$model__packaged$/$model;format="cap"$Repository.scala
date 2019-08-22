package $package$.domain.$model$

trait $model;format="cap"$Repository[F[_]] {
  def create($model$: $model;format="cap"$): F[$model;format="cap"$]

  def get(id: Long): F[Option[$model;format="cap"$]]

}
