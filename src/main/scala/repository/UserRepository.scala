package repository

import scalikejdbc._
import model.{Admin, RegularUser, User}
import java.util.ResourceBundle

object UserRepository:

  val bundle: ResourceBundle = ResourceBundle.getBundle("messages")

  def setup(): Unit =
    Class.forName("org.h2.Driver")
    ConnectionPool.singleton("jdbc:h2:file:./users.db", "user", "pass")
    DB autoCommit { implicit session =>
      sql"""
        create table if not exists users (
          id bigint auto_increment primary key,
          username varchar(64),
          password varchar(64),
          role varchar(20)
        )
      """.execute.apply()

      val count = sql"select count(1) from users".map(_.int(1)).single.apply().getOrElse(0)
      if count == 0 then
        Seq(
          ("admin", "admin123", "admin"),
          ("user", "user123", "user")
        ).foreach { case (u, p, r) =>
          sql"insert into users (username, password, role) values ($u, $p, $r)".update.apply()
        }
    }

  def findUser(username: String, password: String): Option[User] =
    DB readOnly { implicit session =>
      sql"select * from users where username = $username and password = $password"
        .map { rs =>
          rs.string("role") match
            case "admin" => Admin(rs.string("username"), rs.string("password"))
            case _        => RegularUser(rs.string("username"), rs.string("password"))
        }.single.apply()
    }
