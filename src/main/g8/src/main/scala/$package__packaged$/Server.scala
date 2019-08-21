package $package$

import cats.effect.{ Async, Blocker, ConcurrentEffect, ContextShift, Resource, Timer }
import $package$.config.{ DatabaseConfig, ServerConfig }
import doobie.hikari.HikariTransactor
import doobie.util.ExecutionContexts
import io.circe.config.parser
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.Logger
import org.http4s.implicits._

import scala.concurrent.ExecutionContext

object Server {

  def dbTransactor[F[_]: Async: ContextShift](
    dbc: DatabaseConfig,
    connEc: ExecutionContext,
    blocker: Blocker
  ): Resource[F, HikariTransactor[F]] =
    HikariTransactor.newHikariTransactor[F](
      dbc.driver,
      dbc.url,
      dbc.user,
      dbc.password,
      connEc,
      blocker
    )

  def stream[F[_]: ConcurrentEffect](implicit T: Timer[F], C: ContextShift[F]) =
    for {
      dbConf <- Resource.liftF(parser.decodePathF[F, DatabaseConfig]("all.db"))
      conn   <- ExecutionContexts.fixedThreadPool(dbConf.connections.poolSize)
      txnEc  <- ExecutionContexts.cachedThreadPool[F]
      trx    <- dbTransactor(dbConf, conn, Blocker.liftExecutionContext(txnEc))

      serverConf <- Resource.liftF(
                     parser.decodePathF[F, ServerConfig]("all.server")
                   )

      httpApp      = Route.allRoutes(trx).orNotFound
      finalHttpApp = Logger.httpApp(logHeaders = true, logBody = false)(httpApp)

      server <- BlazeServerBuilder[F]
                 .bindHttp(serverConf.port, serverConf.host)
                 .withHttpApp(finalHttpApp)
                 .resource
    } yield server
}
