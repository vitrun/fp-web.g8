package $package$.infrastructure.endpoint

import cats.data.Kleisli
import cats.effect.{ ContextShift, Sync }
import $package$.domain.joke.{ Joke, JokeService, JokeValidationInterpreter }
import $package$.infrastructure.repository.JokeRepositoryInMemory
import io.circe.generic.auto._
import org.http4s._
import org.http4s.circe._
import org.http4s.implicits._
import org.specs2.mutable.Specification
import zio.interop.catz._
import zio.{ DefaultRuntime, Task }

class JokeEndpointTest extends Specification {

  private val testRuntime                         = new DefaultRuntime {}
  implicit val encoder: EntityEncoder[Task, Joke] = jsonEncoderOf[Task, Joke]
  implicit val decoder: EntityDecoder[Task, Joke] = jsonOf[Task, Joke]

  def getRoute[F[_]: Sync](implicit C: ContextShift[F]): Kleisli[F, Request[F], Response[F]] = {
    val repo                    = new JokeRepositoryInMemory[F]()
    val validator               = new JokeValidationInterpreter[F](repo)
    val service: JokeService[F] = new JokeService[F](repo, validator)
    JokeEndpoint.endpoints(service).orNotFound
  }

  "Joke tests" >> {
    "create should return a joke" >> {
      val joke = Joke(None, "It's a joke")
      val req  = Request[Task](Method.POST, uri"/joke").withEntity(joke)
      val resp = testRuntime.unsafeRun(getRoute[Task].run(req))
      resp.status must beEqualTo(Status.Created)
      testRuntime.unsafeRun(resp.as[Joke]).joke must beEqualTo(joke.joke)
    }

    "return 200 and current stock" in {
      val request = Request[Task](Method.GET, uri"/joke/1")
      val stockResponse =
        testRuntime.unsafeRun(getRoute[Task].run(request))
      stockResponse.status must beEqualTo(Status.Ok)

      testRuntime.unsafeRun(stockResponse.as[Joke]).joke must beEqualTo(
        """It's a joke"""
      )
    }
  }
}
