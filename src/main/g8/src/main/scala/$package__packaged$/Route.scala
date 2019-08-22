package $package$

import cats.effect.{ ContextShift, Sync }
import $package$.domain.$model$.{ $model;format="cap"$Service, $model;format="cap"$ValidationInterpreter }
import $package$.infrastructure.endpoint.$model;format="cap"$Endpoint
import $package$.infrastructure.repository.$model;format="cap"$RepositoryInterpreter
import doobie.util.transactor.Transactor
import org.http4s.HttpRoutes
import org.http4s.server.Router
import tapir.server.http4s.Http4sServerOptions
import tapir.swagger.http4s.SwaggerHttp4s
import tapir.docs.openapi._
import tapir.openapi.circe.yaml._

object Route {

  def $model$Routes[F[_]: Sync](trx: Transactor[F])(
    implicit serverOptions: Http4sServerOptions[F],
    fcs: ContextShift[F]
  ): HttpRoutes[F] = {
    val repo                    = new $model;format="cap"$RepositoryInterpreter[F](trx)
    val validator               = new $model;format="cap"$ValidationInterpreter[F](repo)
    val service: $model;format="cap"$Service[F] = new $model;format="cap"$Service[F](repo, validator)
    $model;format="cap"$Endpoint.endpoints(service)
  }

  def allRoutes[F[_]: Sync](trx: Transactor[F])(
    implicit serverOptions: Http4sServerOptions[F],
    fcs: ContextShift[F]
  ): HttpRoutes[F] = {
    val yaml = $model;format="cap"$Endpoint.apis.toOpenAPI("$name$", "1.0").toYaml
    Router("/docs" -> new SwaggerHttp4s(yaml).routes, "/" -> $model$Routes(trx))
  }
}
