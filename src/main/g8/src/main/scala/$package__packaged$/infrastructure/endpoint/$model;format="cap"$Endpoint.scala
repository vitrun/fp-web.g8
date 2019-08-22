package $package$.infrastructure.endpoint

import cats.effect.{ ContextShift, Sync }
import cats.implicits._
import $package$.domain.$model$.{ $model;format="cap"$, $model;format="cap"$Service }
import $package$.domain.{
  AlreadyExistResponse,
  ErrorResponse,
  InternalServerErrorResponse,
  NotFoundResponse
}
import io.circe.generic.auto._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.{ EntityDecoder, EntityEncoder, HttpRoutes }
import tapir._
import tapir.json.circe._
import tapir.model.StatusCodes
import tapir.server.http4s._

object Api {
  val create$model;format="cap"$Endpoint = endpoint.post
    .in("$model$")
    .in(jsonBody[$model;format="cap"$])
    .errorOut(
      oneOf[ErrorResponse](
        statusMapping(StatusCodes.InternalServerError, jsonBody[InternalServerErrorResponse]),
        statusMapping(StatusCodes.Conflict, jsonBody[AlreadyExistResponse])
      )
    )
    .out(
      oneOf(
        statusMapping(StatusCodes.Created, jsonBody[$model;format="cap"$])
      )
    )

  val get$model;format="cap"$EndPoint = endpoint.get
    .in("$model$" / path[Long]("$model$ id"))
    .errorOut(
      oneOf(
        statusMapping(StatusCodes.NotFound, jsonBody[NotFoundResponse]),
        statusMapping(StatusCodes.InternalServerError, jsonBody[InternalServerErrorResponse])
      )
    )
    .out(jsonBody[$model;format="cap"$])
}

class $model;format="cap"$Endpoint[F[_]: Sync]($model$Service: $model;format="cap"$Service[F])(
  implicit serverOptions: Http4sServerOptions[F],
  fcs: ContextShift[F]
) extends Http4sDsl[F] {
  implicit val decoder: EntityDecoder[F, $model;format="cap"$] = jsonOf[F, $model;format="cap"$]
  implicit val encoder: EntityEncoder[F, $model;format="cap"$] = jsonEncoderOf[F, $model;format="cap"$]

  private def create$model;format="cap"$(): HttpRoutes[F] =
    Api.create$model;format="cap"$Endpoint.toRoutes { $model$ =>
      $model$Service.create($model$).value.map {
        case Right(saved) => Right(saved)
        case Left(error)  => Left(AlreadyExistResponse(s"\$error"))
      }
    }

  private def get$model;format="cap"$: HttpRoutes[F] =
    Api.get$model;format="cap"$EndPoint.toRoutes { id =>
      $model$Service.get(id).value.map {
        case Right($model$) => Right($model$)
        case Left(error) => Left(NotFoundResponse(s"\$id \$error"))
      }
    }

  def endpoints(): HttpRoutes[F] = create$model;format="cap"$() <+> get$model;format="cap"$
}

object $model;format="cap"$Endpoint {

  def endpoints[F[_]: Sync](service: $model;format="cap"$Service[F])(
    implicit serverOptions: Http4sServerOptions[F],
    fcs: ContextShift[F]
  ): HttpRoutes[F] = new $model;format="cap"$Endpoint[F](service).endpoints()

  val apis = List(Api.create$model;format="cap"$Endpoint, Api.get$model;format="cap"$EndPoint)
}
