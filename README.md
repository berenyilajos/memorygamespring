# memorygamespring
### Lásd még a további branch-eket: multiple-db, multiple-db-java11, multiple-db-java11-xa.

Rendszerfeltételek:

A program 8-as Javával futtatható. Maven: 3.3.9 vagy 3.5.0

Adatbázis:
Oracle 10 XE (de 11-es adatbázis is megteszi)

A projektben levő com.oracle.ojdbc6 dependency-ről:
Ez local maven repóból való, vagyis a fő projektben található (a memorygamespring mappában).
A memorygame-dao projekt pom.xml-ben van hozzáadva a projecthez.

Először hozzuk létre az MG user/schema-t (jelszó 'mg'), a createMGuser.sql segítségével.
Hozzuk létre a táblákat a usersResultCreate.sql futtatásával.
Ez pl. SQLDeveloperben egyben lefuttatható,
de pl. az Oracle 10 Xe böngészős adatbáziskezelőjében (apex) a "/" jelekkel elválasztott részeket egyenként kell futtatni.
Utána az alapadatokat a csv (először a users, majd a result) file-okból importáljuk,
vagy a usereket regisztráció útján is létre lehet hozni.

A memorygamespring mappában futtasuk az "mvn install" parancsot.

Ezután a memorygame/target mappából futtatható a java -jar memorygame.jar paranccsal.

Ezután az oldal elérhető a localhost:8484/memorygame url-en.

Wadl file információk: localhost:8484/memorygame/game/application.wadl

Jó játékot!
