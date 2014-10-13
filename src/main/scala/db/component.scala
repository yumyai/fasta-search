package db

import java.io.{FileReader, BufferedReader}

import scala.slick.driver.JdbcProfile

trait DriverComponent {
  val driver: JdbcProfile
}