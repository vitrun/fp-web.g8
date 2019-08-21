package $package$

import cats.effect.{ ContextShift, Sync }
import $package$.domain.joke.{ JokeService, JokeValidationInterpreter }
import $package$.infrastructure.endpoint.JokeEndpoint
import $package$.infrastructure.repository.JokeRepositoryInterpreter
import doobie.util.transactor.Transactor
import org.http4s.HttpRoutes
import org.http4s.server.Router
import tapir.server.http4s.Http4sServerOptions
import tapir.swagger.http4s.SwaggerHttp4s
import tapir.docs.openapi._
import tapir.openapi.circe.yaml._

object Route {

  def jokeRoutes[F[_]: Sync](trx: Transactor[F])(
    implicit serverOptions: Http4sServerOptions[F],
    fcs: ContextShift[F]
  ): HttpRoutes[F] = {
    val repo                    = new JokeRepositoryInterpreter[F](trx)
    val validator               = new JokeValidationInterpreter[F](repo)
    val service: JokeService[F] = new JokeService[F](repo, validator)
    JokeEndpoint.endpoints(service)
  }

  def allRoutes[F[_]: Sync](trx: Transactor[F])(
    implicit serverOptions: Http4sServerOptions[F],
    fcs: ContextShift[F]
  ): HttpRoutes[F] = {
    val yaml = JokeEndpoint.apis.toOpenAPI("$name$", "1.0").toYaml
    Router("/docs" -> new SwaggerHttp4s(yaml).routes, "/" -> jokeRoutes(trx))
  }
}
