package db

/**
 * Created by preecha on 10/9/14 AD.
 */

case class Genome(name: String, id: Option[Int] = None)

trait GenomeComponent { this: DriverComponent =>

  import driver.simple._

  class Genomes(tag: Tag) extends Table[Genome](tag, "GENOMES"){

    def id = column[Option[Int]]("ID", O.PrimaryKey, O.AutoInc)
    def name = column[String]("NAME", O.NotNull)

    def * = (name, id) <> (Genome.tupled, Genome.unapply)

  }

  val genomes = TableQuery[Genomes]

}

