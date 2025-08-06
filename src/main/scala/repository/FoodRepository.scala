package repository

import model.FoodItem
import scalikejdbc._
import java.time.LocalDate
import util.DateExtensions.*

object FoodRepository {

  // Step 1: Setup the DB (H2)
  def setup(): Unit =
    Class.forName("org.apache.derby.jdbc.EmbeddedDriver")
    ConnectionPool.singleton("jdbc:derby:nutrition.db;create=true", "", "")

    // Step 2: Create table if it doesn't exist
    DB autoCommit { implicit session =>
      sql"""
        create table if not exists food_items (
          id bigint auto_increment primary key,
          name varchar(255),
          calories int,
          protein double,
          fat double,
          carbs double,
          category varchar(100),
          date_added date
        )
      """.execute.apply()
    }

  // Step 3: Get all food items
  def getAll(): List[FoodItem] =
    println("Loading food items from DB...")
    DB readOnly { implicit session =>
      sql"select * from food_items"
        .map { rs =>
          println(s"Found row: ${rs.string("name")}")
          FoodItem(
            id = rs.long("id"),
            name = rs.string("name"),
            calories = rs.int("calories"),
            protein = rs.double("protein"),
            fat = rs.double("fat"),
            carbs = rs.double("carbs"),
            category = rs.string("category"),
            dateAdded = rs.date("date_added").toLocalDateSafe
          )
        }.list.apply()
    }

  // Step 4: Insert new food item
  def insert(food: FoodItem): Unit =
    DB autoCommit { implicit session =>
      sql"""
        insert into food_items (name, calories, protein, fat, carbs, category, date_added)
        values (${food.name}, ${food.calories}, ${food.protein}, ${food.fat}, ${food.carbs}, ${food.category}, ${food.dateAdded.toSqlDate})
      """.update.apply()
    }

  def update(food: FoodItem): Unit =
    DB autoCommit { implicit session =>
      sql"""
        update food_items
        set name = ${food.name},
            calories = ${food.calories},
            protein = ${food.protein},
            fat = ${food.fat},
            carbs = ${food.carbs},
            category = ${food.category},
            date_added = ${food.dateAdded.toSqlDate}
        where id = ${food.id}
      """.update.apply()
    }

  def delete(id: Long): Unit =
    DB autoCommit { implicit session =>
      sql"delete from food_items where id = $id"
    }

}