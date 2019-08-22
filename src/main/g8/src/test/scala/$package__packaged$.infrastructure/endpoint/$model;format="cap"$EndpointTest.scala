package $package$.infrastructure.endpoint

import cats.data.Kleisli
import cats.effect.{ ContextShift, Sync }
import $package$.domain.$model$.{ $model;format="cap"$, $model;format="cap"$Service, $model;format="cap"$ValidationInterpreter }
import $package$.infrastructure.repository.$model;format="cap"$RepositoryInMemory
import io.circe.generic.auto._
import org.http4s._
import org.http4s.circe._
import org.http4s.implicits._
import org.specs2.mutable.Specification
import zio.interop.catz._
import zio.{ DefaultRuntime, Task }

class $model;format="cap"$EndpointTest extends Specification {

  private val testRuntime                         = new DefaultRuntime {}
  implicit val encoder: EntityEncoder[Task, $model;format="cap"$] = jsonEncoderOf[Task, $model;format="cap"$]
  implicit val decoder: EntityDecoder[Task, $model;format="cap"$] = jsonOf[Task, $model;format="cap"$]

  def getRoute[F[_]: Sync](implicit C: ContextShift[F]): Kleisli[F, Request[F], Response[F]] = {
    val repo                    = new $model;format="cap"$RepositoryInMemory[F]()
    val validator               = new $model;format="cap"$ValidationInterpreter[F](repo)
    val service: $model;format="cap"$Service[F] = new $model;format="cap"$Service[F](repo, validator)
    $model;format="cap"$Endpoint.endpoints(service).orNotFound
  }

  "$model;format="cap"$ tests" >> {
    "create should return a $model$" >> {
      val $model$ = $model;format="cap"$(None, "It's a $model$")
      val req  = Request[Task](Method.POST, uri"/$model$").withEntity($model$)
      val resp = testRuntime.unsafeRun(getRoute[Task].run(req))
      resp.status must beEqualTo(Status.Created)
      testRuntime.unsafeRun(resp.as[$model;format="cap"$]).content must beEqualTo($model$.content)
    }

    "return 200 and current $model$" in {
      val request = Request[Task](Method.GET, uri"/$model$/1")
      val $model$Response =
        testRuntime.unsafeRun(getRoute[Task].run(request))
      $model$Response.status must beEqualTo(Status.Ok)

      testRuntime.unsafeRun($model$Response.as[$model;format="cap"$]).content must beEqualTo(
        """It's a $model$"""
      )
    }
  }
}
