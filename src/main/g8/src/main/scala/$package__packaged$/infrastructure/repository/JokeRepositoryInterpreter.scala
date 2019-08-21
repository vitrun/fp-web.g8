package $package$.infrastructure.repository

import cats.effect.Bracket
import cats.implicits._
import $package$.domain.joke.{ Joke, JokeRepository }
import doobie.implicits._
import doobie.util.query.Query0
import doobie.util.transactor.Transactor
import doobie.util.update.Update0
import doobie.util.{ Get, Put }

class JokeRepositoryInterpreter[F[_]: Bracket[?[_], Throwable]](trx: Transactor[F]) extends JokeRepository[F] {
  override def create(joke: Joke): F[Joke] =
    JokeSql.insert(joke).withUniqueGeneratedKeys[Long]("ID").map(id => joke.copy(id = id.some)).transact(trx)

  override def get(id: Long): F[Option[Joke]] = JokeSql.select(id).option.transact(trx)

}

private object JokeSql {

  def fromStr(s: String): Set[String] = s.split(",").toSet
  def toStr(s: Set[String]): String   = s.mkString(",")

  implicit val tagsGet: Get[Set[String]] = Get[String].map(fromStr)
  implicit val tagsPut: Put[Set[String]] = Put[String].contramap(toStr)

  def select(jokeId: Long): Query0[Joke] = sql"""
    SELECT id, joke, tags
    FROM joke_joke
    WHERE id = \$jokeId
  """.query[Joke]

  def insert(joke: Joke): Update0 = sql"""
    INSERT INTO joke_joke (joke, tags) VALUES (\${joke.joke}, \${joke.tags})
  """.update
}
