package $package$.domain

sealed trait ValidationError extends Product with Serializable

case object NotExistError extends ValidationError

case class AlreadyExistError(error: String) extends ValidationError
