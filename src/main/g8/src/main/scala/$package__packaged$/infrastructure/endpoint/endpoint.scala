package $package$.infrastructure

import org.http4s.{ Request, Response }

package object endpoint {
  type HttpEndpoint[F[_]] = PartialFunction[Request[F], F[Response[F]]]
}
