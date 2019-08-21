package $package$.infrastructure.endpoint

import cats.effect.{ ContextShift, Sync }
import cats.implicits._
import $package$.domain.joke.{ Joke, JokeService }
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
  val createJokeEndpoint = endpoint.post
    .in("joke")
    .in(jsonBody[Joke])
    .errorOut(
      oneOf[ErrorResponse](
        statusMapping(StatusCodes.InternalServerError, jsonBody[InternalServerErrorResponse]),
        statusMapping(StatusCodes.Conflict, jsonBody[AlreadyExistResponse])
      )
    )
    .out(
      oneOf(
        statusMapping(StatusCodes.Created, jsonBody[Joke])
      )
    )

  val getJokeEndPoint = endpoint.get
    .in("joke" / path[Long]("joke id"))
    .errorOut(
      oneOf(
        statusMapping(StatusCodes.NotFound, jsonBody[NotFoundResponse]),
        statusMapping(StatusCodes.InternalServerError, jsonBody[InternalServerErrorResponse])
      )
    )
    .out(jsonBody[Joke])
}

class JokeEndpoint[F[_]: Sync](jokeService: JokeService[F])(
  implicit serverOptions: Http4sServerOptions[F],
  fcs: ContextShift[F]
) extends Http4sDsl[F] {
  implicit val decoder: EntityDecoder[F, Joke] = jsonOf[F, Joke]
  implicit val encoder: EntityEncoder[F, Joke] = jsonEncoderOf[F, Joke]

  private def createJoke(): HttpRoutes[F] =
    Api.createJokeEndpoint.toRoutes { joke =>
      jokeService.create(joke).value.map {
        case Right(saved) => Right(saved)
        case Left(error)  => Left(AlreadyExistResponse(s"\$error"))
      }
    }

  private def getJoke: HttpRoutes[F] =
    Api.getJokeEndPoint.toRoutes { id =>
      jokeService.get(id).value.map {
        case Right(joke) => Right(joke)
        case Left(error) => Left(NotFoundResponse(s"\$id \$error"))
      }
    }

  def endpoints(): HttpRoutes[F] = createJoke() <+> getJoke
}

object JokeEndpoint {

  def endpoints[F[_]: Sync](service: JokeService[F])(
    implicit serverOptions: Http4sServerOptions[F],
    fcs: ContextShift[F]
  ): HttpRoutes[F] = new JokeEndpoint[F](service).endpoints()

  val apis = List(Api.createJokeEndpoint, Api.getJokeEndPoint)
}
