package util

import java.time.LocalDate
import java.sql.Date

object DateExtensions:
  extension (d: Date)
    def toLocalDateSafe: LocalDate =
      if d != null then d.toLocalDate else LocalDate.now()

  extension (ld: LocalDate)
    def toSqlDate: Date = Date.valueOf(ld)

