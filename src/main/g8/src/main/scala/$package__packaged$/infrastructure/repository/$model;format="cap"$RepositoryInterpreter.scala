package $package$.infrastructure.repository

import cats.effect.Bracket
import cats.implicits._
import $package$.domain.$model$.{ $model;format="cap"$, $model;format="cap"$Repository }
import doobie.implicits._
import doobie.util.query.Query0
import doobie.util.transactor.Transactor
import doobie.util.update.Update0
import doobie.util.{ Get, Put }

class $model;format="cap"$RepositoryInterpreter[F[_]: Bracket[?[_], Throwable]](trx: Transactor[F]) extends $model;format="cap"$Repository[F] {
  override def create($model$: $model;format="cap"$): F[$model;format="cap"$] =
    $model;format="cap"$Sql.insert($model$).withUniqueGeneratedKeys[Long]("ID").map(id => $model$.copy(id = id.some)).transact(trx)

  override def get(id: Long): F[Option[$model;format="cap"$]] = $model;format="cap"$Sql.select(id).option.transact(trx)

}

private object $model;format="cap"$Sql {

  def fromStr(s: String): Set[String] = s.split(",").toSet
  def toStr(s: Set[String]): String   = s.mkString(",")

  implicit val tagsGet: Get[Set[String]] = Get[String].map(fromStr)
  implicit val tagsPut: Put[Set[String]] = Put[String].contramap(toStr)

  def select($model$Id: Long): Query0[$model;format="cap"$] = sql"""
    SELECT id, $model$, tags
    FROM $model$_$model$
    WHERE id = \$$model$Id
  """.query[$model;format="cap"$]

  def insert($model$: $model;format="cap"$): Update0 = sql"""
    INSERT INTO $model$_$model$ ($model$, tags) VALUES (\${$model$.content}, \${$model$.tags})
  """.update
}
