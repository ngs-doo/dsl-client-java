object Developers {
  lazy val members = Map(
    "hperadin"  -> "Hrvoje Peradin"
  , "rinmalavi" -> "Marin Vila"
  , "melezov"   -> "Marko Elezović"
  , "zapov"     -> "Rikard Pavelić"
  )

  def toXml =
    <developers>
      {members map { case (nick, name) =>
        <developer>
          <id>{ nick }</id>
          <name>{ name }</name>
          <url>https://github.com/{ nick }</url>
        </developer>
      }}
    </developers>
}
